/***
    Copyright 2024 by Sean Luke
    Licensed under the Apache License version 2.0
*/

package edisyn.synth.novationnova;

import edisyn.Midi;
import edisyn.Model;
import edisyn.Synth;
import edisyn.gui.SelectedTextField;
import edisyn.util.StringUtility;

import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.util.*;

import static edisyn.synth.novationnova.SysexMessage.Type.*;

/**
 * This class is the main EntryPoint into Edisyn
 * TODO - currently mainly a copy from astation, needs to be lined up still with nova
 */
public class NovationNova extends Synth {
    private static final String[] BANKS = Boundaries.BANKS.getValues();
    private static final String[] PATCHES = Boundaries.PATCH_NUMBERS.getValues();
    private static final String PATCH_NAME_UNDEFINED = "Untitled";
    private static final String KEY_PATCH_NAME = "name";
    private static final String KEY_BANK = "bank";
    private static final String KEY_PATCH_NUMBER = "number";
    private static final String KEY_SOFTWARE_VERSION = "swversion";
    private static final String KEY_VERSION_INCREMENT = "swversionincrement";
    // not (yet) used, should become used in (future) devicePanel
    //private static final String KEY_FULL_VERSION = "swversionstring";

    public NovationNova() {
        // build UI
        new UIBuilder(this).build();

        loadDefaults();

        // next only here actually to circumvent warning logs in SanityCheck
        initUnusedModelItems();
        }

    public static String getSynthName() {
        return "Novation Nova";
        }

    @Override
    public String getDefaultResourceFileName() {
        // TODO - init file to be created still
        return "NovationNova.init";
        }

    @Override
    public String getHTMLResourceFileName() {
        return "NovationNova.html";
        }

    @Override
    public String getPatchName(Model model) {
        // TODO - revise, code from astation
        int bank = model.get(KEY_BANK);
        int patch = model.get(KEY_PATCH_NUMBER);
        return createPatchName(bank, patch).orElse(PATCH_NAME_UNDEFINED);
        }

    @Override
    public String getPatchLocationName(Model model) {
        // TODO - revise, code from astation
        int bank = model.get(KEY_BANK);
        int patch = model.get(KEY_PATCH_NUMBER);
        return createPatchName(bank, patch).orElse(null);
        }

    @Override
    public Model getNextPatchLocation(Model model) {
        // TODO - revise, code from astation
        int bank = model.get(KEY_BANK);
        bank = Math.max(bank, 0);
        bank = Math.min(bank, 3);
        int patch = model.get(KEY_PATCH_NUMBER);
        int patchIndex = bank * 100 + patch;
        ++patchIndex;

        int newBank = (patchIndex / 100) % 4;
        int newPatch = patchIndex % 100;
        return updateModel(buildModel(), newBank, newPatch);
        }

    @Override
    public boolean gatherPatchInfo(String title, Model changeThis, boolean writing) {
        // TODO - revise, code from astation
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
            updateModel(changeThis, bank.getSelectedIndex(), n);
            return true;
            }
        }

    @Override
    public void changePatch(Model tempModel) {
        // TODO - revise, code from astation
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
        // TODO - revise, code from astation
        try {
            SysexMessage message = SysexMessage.parse(data);
            model.set(KEY_SOFTWARE_VERSION, message.getSoftwareVersion());
            model.set(KEY_VERSION_INCREMENT, message.getVersionIncrement());
            //System.out.println("version:" + message.getFullVersion());
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

    @Override
    public byte[] emit(Model tempModel, boolean toWorkingMemory, boolean toFile) {
        // TODO - revise, code from astation
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
            // TODO - revisit for nova
            /*
            if (convertor.isPresent()) {
                int value = convertor.get().toSynth(synthModel);
                builder.withPayload(byteIndex, (byte) value);
                }
            */
            }
        byte[] bytes = builder.build().getBytes();
        // System.out.println(StringUtility.toHex(bytes));
        return bytes;
        }

// TODO - check if we want/need do something with this (ref 2 param)
//    @Override
//    public Object[] emitAll(String key, int status)
//    {
//        return emitAll(key);
//    }
//
    @Override
    public Object[] emitAll(String key) {
        Optional<Convertor> convertorOpt = Convertors.getByKey(key);
        if (convertorOpt.isPresent()) {
            Convertor convertor = convertorOpt.get();
            if (convertor.getCC().isPresent()) {
                int value = convertor.toSynth(model, key);
                return buildCC(getChannelOut(), convertor.getCC().getAsInt(), value);
                } else if (convertor.getNRPN().isPresent()) {
                int value = convertor.toSynth(model, key) << 7;
                return buildNRPN(getChannelOut(), convertor.getNRPN().getAsInt(), value);
                }
            }
        System.err.println("Ignoring model key '" + key + "', since no convertor found");
        return super.emitAll(key);
        }

    @Override
    public byte[] requestCurrentDump() {
        // TODO - revise, code from astation
        return new SysexMessage.Builder(CURRENT_PROGRAM_DUMP_REQUEST)
            .build().getBytes();
        }

    @Override
    public byte[] requestDump(Model tempModel) {
        // TODO - revise, code from astation
        return new SysexMessage.Builder(PROGRAM_DUMP_REQUEST)
            .withProgramBank((byte) (1 + tempModel.get(KEY_BANK))) // 1..4 (while in model: 0..3))
            .withProgramNumber((byte) (tempModel.get(KEY_PATCH_NUMBER)))
            .build().getBytes();
        }


    @Override
    protected void handleInRawCC(ShortMessage message) {
        // hack alert, CC100/CC101 used as raw CC
        if (
//                message.getData1() == 96 ||
//                message.getData1() == 97 ||
                message.getData1() == 100 ||
                message.getData1() == 101
        ) {
            // System.out.println("hack-alert:" + message.getData1() + ", " + message.getData2());
            Midi.CCData ccdata = new Midi.CCData(0, message.getData1(), message.getData2(), message.getChannel(), false);
            handleSynthCCOrNRPN(ccdata);
        } else {
            super.handleInRawCC(message);
        }
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
        // the recognize() method, it gets sent here. Typically, this is
        // a sysex message for a single parameter update. If your synth sends
        // such things, implement this.  See also handleCCOrNRPNData() below.
        System.err.println("Unrecognized message received: " + StringUtility.toHex(data));
        }

    /**
     * Hack alert: Avoid race condition in Edisyn on linux.
     * Introducing a decent amount of delay (100) after patch write makes the diff here.
     * without this, the progress window (showing progress of the writes) is sometimes not closing
     * <p>
     * Meanwhile, the issue got fixed in Edisyn Core,
     * yet, let's keep this peace of code still here and wait and see if the fix in Edisyn Core survives
     * (since that part of the code has been igiving headaches in the past; seemingly far from obvious to get that
     * working on all different platforms, OSes, JREs, ...
     * </p>
     *
     * <p>
     * For now using it, not to avoid the race condition, yet to provide a more smooth/safe experience when writing
     * a lot of patches in batch. Without a delay, the unit does need some extra time to process this batch.
     * So, let's introduce a small delay, won't really worsen the  User Experience, but avoids potential choking on
     * the unit.
     * I never ran into issues while testing, yet my development machine is a pretty old beast & not exactly
     * the best reference wrt performance
     * </p>
     */
    @Override
    public int getPauseAfterWritePatch() {
        // TODO - revise, code from astation
        // the value used as a hack
        // return 100;
        // the value for smooth processing
        return 50;
        }

    ////
    // librarian support
    ////
    @Override
    public String[] getBankNames() {
        // TODO - revise, code from astation
        return BANKS;
        }

    @Override
    public String[] getPatchNumberNames() {
        // TODO - revise, code from astation
        return PATCHES;
        }

    @Override
    public boolean getSupportsPatchWrites() {
        // TODO - revise, code from astation
        return true;
        }

    @Override
    public boolean librarianTested() {
        // TODO - revise, code from astation
        return false;
        }

    ////
    // some private aider methods
    ////
    private String toString(Midi.CCData data) {
        return "CCData {number:" + data.number + ", value:" + data.value + ", type:" + data.type + "}";
        }

    private int handleProgramDump(SysexMessage message) {
        // TODO - revise, code from astation
        byte programBank = message.getProgramBank();    // 1..4
        byte programNumber = message.getProgramNumber();  // 0..99
        if (programBank >= 1 && programBank <= 4) {
            updateModel(model, programBank - 1 /* 0..3 in model */, programNumber);
            } else {
            // explicitly reset bank/number
            // this way we make sure the UI does not show any patch number when there is none exposed by the synth
            updateModel(model, -1, -1);
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

    private Model updateModel(Model modelToUpdate, int modelBank, int modelPatch) {
        // TODO - revise, code from astation
        modelBank = validateModelBank(modelBank);
        modelPatch = validateModelPatch(modelPatch);
        modelToUpdate.set(KEY_BANK, modelBank); // 0..3 in model
        modelToUpdate.set(KEY_PATCH_NUMBER, modelPatch);
        // also update patch_name in the model, wrt consistency
        modelToUpdate.set(KEY_PATCH_NAME, createPatchName(modelBank, modelPatch).orElse(PATCH_NAME_UNDEFINED));
        return modelToUpdate;
        }

    private Optional<String> createPatchName(int modelBank, int modelPatch) {
        // TODO - revise, code from astation
        modelBank = validateModelBank(modelBank);
        modelPatch = validateModelPatch(modelPatch);
        if ((modelBank == -1) || (modelPatch == -1)) {
            return Optional.empty();
            }
        String bankName = BANKS[modelBank];
        return Optional.of(String.format("%s%02d", bankName, modelPatch));
        }

    // validate bank number (in model !): 0..3 (and -1 used as sentinel)
    // note: in unit, valid bank numbers are: 1..4 !
    private int validateModelBank(int bank) {
        // TODO - revise, code from astation
        if (bank < -1 || bank > 3) {
            // definitely a programming mistake
            throw new IllegalStateException("Invalid bank " + bank);
            }
        return bank;
        }

    // validate patch numbers (in model): 0..99 (and -1 used as sentinel)
    private int validateModelPatch(int patch) {
        // TODO - revise, code from astation
        if (patch < -1 || patch > 99) {
            // definitely a programming mistake
            throw new IllegalStateException("Invalid patch " + patch);
            }
        return patch;
        }

    /*
     * Initialize some Edisyn model keys which do not yet have a UI control
     * Only here to satisfy the SanityCheck
     */
    private void initUnusedModelItems() {
        // TODO - revise, code from astation
        model.setMinMax(KEY_SOFTWARE_VERSION, 0, 127);
        model.setMinMax(KEY_VERSION_INCREMENT, 0, 127);
        //
//        model.setMinMax(Mappings.OSC_SELECT.getKey(), 0, 2);
//        model.setMinMax(Mappings.PWM_SOURCE.getKey(), 0, 1);
//        model.setMinMax(Mappings.MIXER_SELECT.getKey(), 0, 2);
//        model.setMinMax(Mappings.LFO_SELECT.getKey(), 0, 1);
        }
    }
