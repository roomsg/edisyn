/***
 Copyright 2024 by Sean Luke
 Licensed under the Apache License version 2.0
 */

package edisyn.synth.novationastation;

import edisyn.Midi;
import edisyn.Model;
import edisyn.Synth;
import edisyn.gui.SelectedTextField;
import edisyn.util.StringUtility;

import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.util.*;

import static edisyn.synth.novationastation.SysexMessage.Type.*;

/**
 * This class is the main EntryPoint into Edisyn
 */
public class NovationAStation extends Synth {
    private static final String[] BANKS = Boundaries.BANKS.getValues();
    private static final String[] PATCHES = Boundaries.PATCH_NUMBERS.getValues();
    private static final String KEY_BANK = "bank";
    private static final String KEY_PATCH_NUMBER = "number";
    private static final String KEY_SOFTWARE_VERSION = "swversion";
    private static final String KEY_VERSION_INCREMENT = "swversionincrement";
    // not (yet) used, should become used in (future) devicePanel
    //private static final String KEY_FULL_VERSION = "swversionstring";

    /*
     * List of edisyn model keys to be ignored during SanityCheck
     * These are all parameters which do not yet have a UI control
     */
    private static final List<String> UNMAPPED_KEYS = Arrays.asList(
            KEY_SOFTWARE_VERSION,
            KEY_VERSION_INCREMENT,
            //KEY_FULL_VERSION
            Mappings.OSC_SELECT.getKey(),
            Mappings.PWM_SOURCE.getKey(),
            Mappings.LFO_SELECT.getKey(),
            Mappings.MIXER_SELECT.getKey()
    );

    public NovationAStation() {
        // build UI
        new UIBuilder(this).build();

        loadDefaults();

        // next only here actually to circumvent warning logs in SanityCheck
        initUnusedModelItems();
    }

    public static String getSynthName() {
        return "Novation A Station";
    }

    @Override
    public String getDefaultResourceFileName() {
        return "NovationAStation.init";
    }

    @Override
    public String getHTMLResourceFileName() {
        return "NovationAStation.html";
    }

    @Override
    public String getPatchLocationName(Model model) {
        int bank = model.get(KEY_BANK);
        int number = model.get(KEY_PATCH_NUMBER);
        if (bank >= 0 && bank <= 3 && number >= 0 && number <= 99) {
            String bankName = BANKS[bank];
            return String.format("%s%02d", bankName, number);
        }
        return null;
    }

    @Override
    public Model getNextPatchLocation(Model model) {
        int bank = model.get(KEY_BANK);
        bank = Math.max(bank, 0);
        bank = Math.min(bank, 3);
        int program = model.get(KEY_PATCH_NUMBER);
        int programindex = bank * 100 + program;
        ++programindex;

        Model newModel = buildModel();
        newModel.set(KEY_BANK, (programindex / 100) % 4);
        newModel.set(KEY_PATCH_NUMBER, programindex % 100);
        return newModel;
    }

    @Override
    public boolean gatherPatchInfo(String title, Model changeThis, boolean writing) {
        JComboBox<String> bank = new JComboBox<>(BANKS);
        bank.setEditable(false);
        bank.setMaximumRowCount(4);

        int currentBank = model.get(KEY_BANK);     // 0..3
        if (currentBank >= 0 && currentBank <= 3) {
            bank.setSelectedIndex(currentBank);
        }

        int currentPatch = model.get(KEY_PATCH_NUMBER);
        currentPatch = Math.min(currentPatch, 99);
        currentPatch = Math.max(currentPatch, 0);

        JTextField number = new SelectedTextField(String.valueOf(currentPatch), 3);

        while (true) {
            boolean result = showMultiOption(this, new String[]{"Bank", "Patch Number"},
                    new JComponent[]{bank, number}, title, "Enter the Bank and Patch number.");

            if (!result)
                return false;

            int n;
            try {
                n = Integer.parseInt(number.getText());
            } catch (NumberFormatException e) {
                showSimpleError(title, "The Patch Number must be an integer 0 ... 99");
                continue;
            }
            if (n < 0 || n > 99) {
                showSimpleError(title, "The Patch Number must be an integer 0 ... 99");
                continue;
            }

            changeThis.set(KEY_BANK, bank.getSelectedIndex());
            changeThis.set(KEY_PATCH_NUMBER, n);
            return true;
        }
    }

    @Override
    public void changePatch(Model tempModel) {
        byte bank = (byte) tempModel.get(KEY_BANK);
        byte program = (byte) tempModel.get(KEY_PATCH_NUMBER);
        try {
            ++bank; // 1..4 in synth, while 0..3 in model
            // Bank change is CC 32
            tryToSendMIDI(new ShortMessage(ShortMessage.CONTROL_CHANGE, getChannelOut(), 32, bank));
            // Number change is PC
            tryToSendMIDI(new ShortMessage(ShortMessage.PROGRAM_CHANGE, getChannelOut(), program, 0));
        } catch (Exception e) {
            Synth.handleException(e);
        }
    }

    @Override
    public int parse(byte[] data, boolean fromFile) {
        try {
            SysexMessage message = SysexMessage.parse(data);
            model.set(KEY_SOFTWARE_VERSION, message.getSoftwareVersion());
            model.set(KEY_VERSION_INCREMENT, message.getVersionIncrement());
            //model.set(KEY_FULL_VERSION, message.getFullVersion());
            // update bank + program
            SysexMessage.Type type = message.getType();
            switch (type) {
                case CURRENT_PROGRAM_DUMP:
                case PROGRAM_DUMP:
                    return handleProgramDump(message);
                default:
                    System.err.println("Unsupported message received, type: " + type);
                    return PARSE_IGNORE;
            }
        } catch (Throwable t) {
            return PARSE_IGNORE;
        }
    }

    ;

    @Override
    public byte[] emit(Model tempModel, boolean toWorkingMemory, boolean toFile) {
        if (tempModel == null)
            tempModel = model;
        // only use tempModel for retrieval of bank & patchnumber !
        byte programBank = (byte) (toWorkingMemory ? 0 : tempModel.get(KEY_BANK) + 1);
        byte programNumber = (byte) (toWorkingMemory ? 0 : tempModel.get(KEY_PATCH_NUMBER));
        byte controlByte = (byte) (toWorkingMemory ? 0 : 1);
        // .. and use the "synth-model" for all real patch data !
        Model synthModel = model;
        SysexMessage.Type type = toWorkingMemory ? CURRENT_PROGRAM_DUMP : PROGRAM_DUMP;
        SysexMessage.Builder builder = new SysexMessage.Builder(type)
                .withControlByte(controlByte)
                .withProgramBank(programBank)
                .withProgramNumber(programNumber)
                .withSoftwareVersion((byte) synthModel.get(KEY_SOFTWARE_VERSION))
                .withVersionIncrement((byte) synthModel.get(KEY_VERSION_INCREMENT));
        for (int byteIndex = 0; byteIndex < type.getPayloadSize(); ++byteIndex) {
            Optional<Convertor> convertor = Convertors.getByIndex(byteIndex);
            if (convertor.isPresent()) {
                int value = convertor.get().toSynth(synthModel);
                builder.withPayload(byteIndex, (byte) value);
            }
        }
        byte[] bytes = builder.build().getBytes();
        // System.out.println(StringUtility.toHex(bytes));
        return bytes;
    }

    @Override
    public Object[] emitAll(String key) {
        Optional<Convertor> convertor = Convertors.getByKey(key);
        if (convertor.isPresent()) {
            Convertor mapping = convertor.get();
            if (mapping.getCC().isPresent()) {
                int value = mapping.toSynth(model);
                return buildCC(getChannelOut(), mapping.getCC().getAsInt(), value);
            } else if (mapping.getNRPN().isPresent()) {
                int value = mapping.toSynth(model) << 7;
                return buildNRPN(getChannelOut(), mapping.getNRPN().getAsInt(), value);
            }
        }
        System.err.println("Ignoring model key '" + key + "', since no convertor found");
        return super.emitAll(key);
    }

    @Override
    public byte[] requestCurrentDump() {
        return new SysexMessage.Builder(CURRENT_PROGRAM_DUMP_REQUEST)
                .build().getBytes();
    }

    @Override
    public byte[] requestDump(Model tempModel) {
        return new SysexMessage.Builder(PROGRAM_DUMP_REQUEST)
                .withProgramBank((byte) (1 + tempModel.get(KEY_BANK))) // 1..4 (while in model: 0..3))
                .withProgramNumber((byte) (tempModel.get(KEY_PATCH_NUMBER)))
                .build().getBytes();
    }

    @Override
    public void handleSynthCCOrNRPN(Midi.CCData data) {
        Optional<Convertor> convertor = Optional.empty();
        OptionalInt value = OptionalInt.empty();
        if (data.type == Midi.CCDATA_TYPE_RAW_CC) {
            convertor = Convertors.getByCC(data.number);
            value = OptionalInt.of(data.value);
        } else if (data.type == Midi.CCDATA_TYPE_NRPN) {
            convertor = Convertors.getByNRPN(data.number);
            value = OptionalInt.of((data.value >> 7));
        }
        if (convertor.isPresent() && value.isPresent()) {
            if (convertor.get().getBoundary().validate(value.getAsInt())) {
                convertor.get().toModel(model, value.getAsInt());
            } else {
                System.err.println("Ignoring CC/NRPN value '" + value.getAsInt() + "' for " + convertor.get());
            }
        } else {
            System.out.println("Ignoring CC/NRPN msg:" + toString(data));
        }
    }

    @Override
    public void parseParameter(byte[] data) {
        // If your synth sent you a sysex message which was not recognized via
        // the recognize() method, it gets sent here.  Typically this is
        // a sysex message for a single parameter update.  If your synth sends
        // such things, implement this.  See also handleCCOrNRPNData() below.
        System.err.println("Unrecognized message received: " + StringUtility.toHex(data));
    }

    /**
     * Hack alert: Avoid race condition in Edisyn on linux.
     * Introducing a decent amount of delay after patch write make the diff here.
     * without this, the progress window (showing progress of the writes) never closes
     */
    @Override
    public int getPauseAfterWritePatch() {
        return 100;
    }

    @Override
    public JFrame sprout() {
        // This is a great big method in Synth.java, and handles building the JFrame and
        // constructing all of the menus.  It's called when the editor is having its GUI
        // constructed.   You may need to do some things here, such as turning off certain
        // menu options that your synthesizer cannot do.  Be sure to call super.sprout();
        // first.
        // TODO - what about doing a global request here ? as in: requesting device (non program) data
        return super.sprout();
    }

    @Override
    public boolean testVerify(Synth synth2, String key, Object obj1, Object obj2) {
        return UNMAPPED_KEYS.contains(key);
    }

    ////
    // librarian support
    ////
    @Override
    public String[] getBankNames() {
        return BANKS;
    }

    @Override
    public String[] getPatchNumberNames() {
        return PATCHES;
    }

    @Override
    public boolean getSupportsPatchWrites() {
        return true;
    }

    @Override
    public boolean librarianTested() {
        return true;
    }

    ////
    // some private aider methods
    ////
    private String toString(Midi.CCData data) {
        return "CCData {number:" + data.number + ", value:" + data.value + ", type:" + data.type + "}";
    }

    private int handleProgramDump(SysexMessage message) {
        byte programBank = message.getProgramBank();    // 1..4
        byte programNumber = message.getProgramNumber();  // 0..99
        if (programBank >= 1 && programBank <= 4) {
            model.set(KEY_BANK, --programBank); // 0..3 in model
            model.set(KEY_PATCH_NUMBER, programNumber);
        } else {
            // explicitly reset bank/number
            // this way we make sure the UI does not show any patch number when there is none exposed by the synth
            model.set(KEY_BANK, -1);
            model.set(KEY_PATCH_NUMBER, -1);
        }
        // update sound parameter values
        byte[] payload = message.getPayload();
        for (int i = 0; i < payload.length; ++i) {
            Optional<Convertor> convertor = Convertors.getByIndex(i);
            if (convertor.isPresent()) {
                int value = payload[i];
                Boundary boundary = convertor.get().getBoundary();
                if (boundary.validate(value)) {
                    convertor.get().toModel(model, value);
                } else {
                    System.err.println("Ignoring value '" + value + "' for " + convertor.get());
                }
            }
        }
        return PARSE_SUCCEEDED;
    }

    private void initUnusedModelItems() {
        model.setMinMax(KEY_SOFTWARE_VERSION, 0, 127);
        model.setMinMax(KEY_VERSION_INCREMENT, 0, 127);
        //
        model.setMinMax(Mappings.OSC_SELECT.getKey(), 0, 2);
        model.setMinMax(Mappings.PWM_SOURCE.getKey(), 0, 1);
        model.setMinMax(Mappings.MIXER_SELECT.getKey(), 0, 2);
        model.setMinMax(Mappings.LFO_SELECT.getKey(), 0, 1);
    }
}
