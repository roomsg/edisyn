package edisyn.synth.novationnova;

import edisyn.Synth;
import edisyn.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;

import static edisyn.gui.Style.COLOR_GLOBAL;
import static edisyn.synth.novationnova.Mappings.*;

/**
 * This class has the responsibility to build up the UI.
 * This also implies linking the different UI-components to the Edisyn synth model
 */
class UIBuilder {
    private final Synth synth;

    UIBuilder(Synth synth) {
        this.synth = Objects.requireNonNull(synth);
        }

    void build() {
        // General PANEL
        JComponent generalPanel = new SynthPanel(synth);
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        hbox.add(createGlobal(COLOR_GLOBAL()));
        hbox.add(createGeneral(Style.COLOR_A()));
        hbox.addLast(createPortamento(Style.COLOR_C()));
        vbox.add(hbox);

        vbox.add(createOscillator(1, Style.COLOR_B()));
        vbox.add(createOscillator(2, Style.COLOR_B()));
        vbox.add(createOscillator(3, Style.COLOR_B()));
        hbox = new HBox();
        hbox.add(createXMod(1, Style.COLOR_B()));
        hbox.add(createXMod(2, Style.COLOR_B()));
        vbox.add(hbox);

        hbox = new HBox();
        hbox.addLast(createNoise(Style.COLOR_B()));
        vbox.add(hbox);

        generalPanel.add(vbox, BorderLayout.CENTER);
        synth.addTab("General", generalPanel);

        // Envelope, LFO, Filter PANEL
        JComponent envelopeLfoFilterPanel = new SynthPanel(synth);
        vbox = new VBox();
        vbox.add(createEnvelope(1, Style.COLOR_A()));
        vbox.add(createEnvelope(2, Style.COLOR_A()));
        vbox.add(createEnvelope(3, Style.COLOR_A()));

        hbox = new HBox();
        hbox.add(createLFO(1, Style.COLOR_B()));
        vbox.add(hbox);
        hbox = new HBox();
        hbox.addLast(createLFO(2, Style.COLOR_B()));
        vbox.add(hbox);

        vbox.add(createFilter(Style.COLOR_C()));

        envelopeLfoFilterPanel.add(vbox, BorderLayout.CENTER);
        synth.addTab("Env LFO Filter", envelopeLfoFilterPanel);

        // ARP, EFFECTS PANEL
        JComponent arpEffectsPanel = new SynthPanel(synth);
        vbox = new VBox();
        hbox = new HBox();
        hbox.addLast(createARP(Style.COLOR_A()));
        vbox.add(hbox);
        hbox = new HBox();
        hbox.add(createEffectConfig(Style.COLOR_B()));
        hbox.addLast(createDelay(Style.COLOR_C()));
        vbox.add(hbox);
        hbox = new HBox();
        hbox.add(createReverb(Style.COLOR_A()));
        hbox.add(createChorus(Style.COLOR_B()));
        hbox.addLast(createDistortion(Style.COLOR_C()));
        vbox.add(hbox);
        hbox = new HBox();
        hbox.add(createPan(Style.COLOR_B()));
        hbox.add(createEqualizer(Style.COLOR_A()));
        hbox.addLast(createVocoder(Style.COLOR_B()));
        vbox.add(hbox);

        arpEffectsPanel.add(vbox, BorderLayout.CENTER);
        synth.addTab("Arp Effects", arpEffectsPanel);
        }

    private JComponent createGlobal(Color color) {
        Category globalCategory = new Category(synth, "Novation Nova", color);

        HBox hbox = new HBox();

        VBox vbox = new VBox();
        HBox hbox2 = new HBox();
        hbox2.add(new PatchDisplay(synth, 4));
        vbox.add(hbox2);
        hbox.add(vbox);

        globalCategory.add(hbox, BorderLayout.WEST);
        return globalCategory;
        }

    private JComponent createGeneral(Color color) {
        Category categoryGeneral = new Category(synth, "General", color);

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.add(createChooser("Polyphony Mode", POLYPHONY_MODE));
        vbox.add(createChooser("Oscillator Mode", OSC_MODE));
        vbox.add(createCheckBox("Constant Gate", CONSTANT_GATE));
        hbox.add(vbox);

        vbox = new VBox();
        vbox.add(createChooser("DCO/VCO", DCO_VCO_MODE));
        // TODO - move to Portamento ?
        vbox.add(createChooser("Glide Type", GLIDE_TYPE));
        hbox.add(vbox);

        hbox.add(createLabelledDial(Arrays.asList("Unison", "Voices"), UNISON_VOICES, color));
        hbox.add(createLabelledDial(Arrays.asList("Program", "Volume"), PROGRAM_VOLUME, color));

        categoryGeneral.add(hbox, BorderLayout.CENTER);
        return categoryGeneral;
        }


    private JComponent createOscillator(final int osc, Color color) {
        Category category = new Category(synth, "Oscillator " + osc, color);
        category.makePasteable("osc");

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.add(createChooser("Waveform", Mappings.find("OSC%d_WAVEFORM", osc)));
        hbox.add(vbox);

        hbox.add(createLabelledDial("Octave", Mappings.find("OSC%d_OCTAVE", osc), color));
        hbox.add(createLabelledDial("Semitone", Mappings.find("OSC%d_SEMITONE", osc), color));
        hbox.add(createLabelledDial("Detune", Mappings.find("OSC%d_FINE_TUNE", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Bend Wheel", "Depth"), Mappings.find("OSC%d_BENDWHEEL_AMOUNT", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Key", "Sync"), Mappings.find("OSC%d_KEY_SYNC", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Sync", "Skew"), Mappings.find("OSC%d_SYNC_SKEW", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Sync", "Formant", "Width"), Mappings.find("OSC%d_SYNC_FORMANT_WIDTH", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "LFO1", "Amount"), Mappings.find("OSC%d_MODWHEEL_LFO1_AMOUNT", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "LFO1", "Amount"), Mappings.find("OSC%d_AFTERTOUCH_LFO1_AMOUNT", osc), color));

        // mod matrix
        Map<String, Mappings> modulationDestinationMappings = new HashMap<>();
        modulationDestinationMappings.put("Mix", Mappings.find("OSC%d_MIX", osc));
        modulationDestinationMappings.put("Pitch", Mappings.find("OSC%d_PITCH", osc));
        modulationDestinationMappings.put("Width", Mappings.find("OSC%d_WIDTH", osc));
        modulationDestinationMappings.put("Sync", Mappings.find("OSC%d_SYNC", osc));
        modulationDestinationMappings.put("Soften", Mappings.find("OSC%d_SOFTEN", osc));
        JComponent modulationPanel = createModulationPanel(Mappings.find("MODULATION_OSC%d", osc), modulationDestinationMappings, color);
        hbox.add(modulationPanel);

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createXMod(final int xmod, Color color) {
        Category category = new Category(synth, "Xmod " + xmod + "x3", color);
        category.makePasteable("xmod");

        Map<String, Mappings> modulationDestinationMappings = new HashMap<>();
        modulationDestinationMappings.put("Mix", Mappings.find("XMOD%d_MIX", xmod));
        JComponent modulationPanel = createModulationPanel(Mappings.find("MODULATION_XMOD%d", xmod), modulationDestinationMappings, color);

        category.add(modulationPanel, BorderLayout.CENTER);
        return category;
    }

    private JComponent createNoise(Color color) {
        Category category = new Category(synth, "Noise", color);

        Map<String, Mappings> modulationDestinationMappings = new HashMap<>();
        modulationDestinationMappings.put("Mix", NOISE_MIX);
        modulationDestinationMappings.put("Soften", NOISE_SOFTEN);
        JComponent modulationPanel = createModulationPanel(MODULATION_NOISE, modulationDestinationMappings, color);

        category.add(modulationPanel, BorderLayout.CENTER);
        return category;
    }

    private JComponent createModulationPanel(Mappings modulationDestinationMapping, Map<String, Mappings> mappings, Color color) {
        HBox hbox = new HBox();

        VBox vbox = new VBox();
        Chooser chooser = createChooser("Modulation Destination", modulationDestinationMapping);
        vbox.add(chooser);
        hbox.add(vbox);

        List<String> modulationDestinations = Arrays.asList(modulationDestinationMapping.getBoundaries().getValues());
        Set<String> modulationDestinationsSet = new HashSet<>(modulationDestinations);
        Set<String> expectedModulationDestinations = mappings.keySet();
        if (!modulationDestinationsSet.equals(expectedModulationDestinations)) {
            throw new IllegalStateException("Unexpected modulation destinations:" + modulationDestinationsSet + " VS " + expectedModulationDestinations);
        }

        List<JPanel> panels = new ArrayList<>();
        for (String modulationDestination:modulationDestinations) {
            JPanel panel = new JPanel();
            panels.add(panel);
            Mappings modulationDesination = mappings.get(modulationDestination);
            // TODO small hack here for the sake of consistent layout
            panel.add(createLabelledDial(Arrays.asList("Level", "-"), modulationDesination, color));
            panel.add(createLabelledDial(Arrays.asList("Lfo1", "Depth"), Mappings.valueOf(modulationDesination.name() + "_LFO1_DEPTH"), color));
            panel.add(createLabelledDial(Arrays.asList("Lfo2", "Depth"), Mappings.valueOf(modulationDesination.name() + "_LFO2_DEPTH"), color));
            panel.add(createLabelledDial(Arrays.asList("Env2", "Depth"), Mappings.valueOf(modulationDesination.name() + "_ENV2_DEPTH"), color));
            panel.add(createLabelledDial(Arrays.asList("Env3", "Depth"), Mappings.valueOf(modulationDesination.name() + "_ENV3_DEPTH"), color));
            panel.add(createLabelledDial(Arrays.asList("Wheel", "Depth"), Mappings.valueOf(modulationDesination.name() + "_WHEEL_DEPTH"), color));

            // keep UI consistent
            panel.setBackground(Style.BACKGROUND_COLOR());

            hbox.add(panel);
        }

        // show/hide controls upon user selected
        JComboBox combo = chooser.getCombo();
        combo.addItemListener(il -> {
            if (il.getStateChange() == ItemEvent.SELECTED) {
                refreshPanels(combo, panels);
            }
        });
        // next not working ?!
        //combo.setEnabled(combo.getItemCount() > 1);
        refreshPanels(combo, panels);
        return hbox;
    }

    public void refreshPanels(JComboBox combo, List<JPanel> panels) {
        int selection = combo.getSelectedIndex();
        for (int i = 0; i < panels.size(); ++i) {
            panels.get(i).setVisible(selection == i);
//            panels.get(i).updateUI();
        }
    }

    private JComponent createPortamento(Color color) {
        Category category = new Category(synth, "Portamento", color);

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.add(createChooser("Portamento Type", PORTAMENTO_TYPE));
        vbox.add(createChooser("Portamento Mode", PORTAMENTO_MODE));
        hbox.add(vbox);
        hbox.add(createLabelledDial(Arrays.asList("Portamento", "Time"), PORTAMENTO_TIME, color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createFilter(Color color) {
        Category category = new Category(synth, "Filter", color);
        VBox mainvbox = new VBox();

        HBox hbox = new HBox();
        VBox vbox1 = new VBox();
        vbox1.add(createChooser("Type", FILTER_TYPE));
        vbox1.add(createChooser("Slope", FILTER_SLOPE));
        hbox.add(vbox1);

        hbox.add(createLabelledDial("Frequency", FILTER_FREQUENCY, color));
        hbox.add(createLabelledDial("Resonance", FILTER_RESONANCE, color));
        hbox.add(createLabelledDial("Overdrive", FILTER_OVERDRIVE, color));
        hbox.add(createLabelledDial("Key Track", FILTER_KEY_TRACK, color));
        hbox.add(createLabelledDial("Q Normalize", FILTER_Q_NORMALIZE, color));
        hbox.add(createLabelledDial("Special Width", FILTER_SPECIAL_WIDTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "LFO2", "Frequency", "Amount"), FILTER_MODWHEEL_LFO2_FREQUENCY_AMOUNT, color));
        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "LFO2", "Resonance", "Amount"), FILTER_MODWHEEL_LFO2_RESONANCE_AMOUNT, color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "LFO2", "Frequency", "Amount"), FILTER_AFTERTOUCH_LFO2_FREQUENCY_AMOUNT, color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "LFO2", "Resonance", "Amount"), FILTER_AFTERTOUCH_LFO2_RESONANCE_AMOUNT, color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Frequency", "Amount"), FILTER_AFTERTOUCH_FREQUENCY_AMOUNT, color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Resonance", "Amount"), FILTER_AFTERTOUCH_RESONANCE_AMOUNT, color));

        mainvbox.add(hbox);

        hbox = new HBox();
        Map<String, Mappings> modulationDestinationMappings = new HashMap<>();
        modulationDestinationMappings.put("Frequency", FILTER_FREQUENCY);
        modulationDestinationMappings.put("Resonance", FILTER_RESONANCE);
        JComponent modulationPanel = createModulationPanel(MODULATION_FILTER, modulationDestinationMappings, color);
        hbox.add(modulationPanel);
        mainvbox.add(hbox);

        category.add(mainvbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createEnvelope(final int envelope, Color color) {
        Category category = new Category(synth,"Envelope " + envelope, color);
        category.makePasteable("env");

        HBox hbox = new HBox();
        // TODO - check trigger menu
        VBox vbox = new VBox();
        vbox.add(createChooser("Env Trigger", Mappings.find("ENVELOPE%d_TRIGGER", envelope)));
        hbox.add(vbox);

        // (generic) ADSR controls
        Mappings mappingsAttack = Mappings.find("ENVELOPE%d_ATTACK", envelope);
        hbox.add(createLabelledDial("Attack", mappingsAttack, color));
        Mappings mappingsDecay = Mappings.find("ENVELOPE%d_DECAY", envelope);
        hbox.add(createLabelledDial("Decay", mappingsDecay, color));
        Mappings mappingsSustain = Mappings.find("ENVELOPE%d_SUSTAIN", envelope);
        hbox.add(createLabelledDial("Sustain", mappingsSustain, color));
        Mappings mappingsRelease = Mappings.find("ENVELOPE%d_RELEASE", envelope);
        hbox.add(createLabelledDial("Release", mappingsRelease, color));

        // other env controls
        Mappings velocityDepth = Mappings.find("ENVELOPE%d_VELOCITY_DEPTH", envelope);
        hbox.add(createLabelledDial("Velocity Depth", velocityDepth, color));
        if (envelope == 1) {
            Mappings mappingsWheel = Mappings.find("ENVELOPE%d_WHEEL", envelope);
            hbox.add(createLabelledDial("Mod Wheel", mappingsWheel, color));
            Mappings mappingsAfterTouch = Mappings.find("ENVELOPE%d_AFTER_TOUCH", envelope);
            hbox.add(createLabelledDial("AfterTouch", mappingsAfterTouch, color));
        } else {
            Mappings mappingsDelay = Mappings.find("ENVELOPE%d_DELAY", envelope);
            Component delay = hbox.add(createLabelledDial("Delay", mappingsDelay, color));
            // wrt improved layouting, use delay for compensating missing control
            hbox.add(Strut.makeStrut(delay));
        }

        // ADSR display
        JComponent comp = new EnvelopeDisplay(synth, Color.red,
            new String[]{null, mappingsAttack.getKey(), mappingsDecay.getKey(), null, mappingsRelease.getKey()},
            new String[]{null, null, mappingsSustain.getKey(), mappingsSustain.getKey(), null},
            new double[]{0, 0.25 / 127.0, 0.25 / 127.0, 0.25, 0.25 / 127.0},
            new double[]{0, 1.0, 1.0 / 127.0, 1.0 / 127.0, 0});
        hbox.addLast(comp);

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createLFO(final int lfo, Color color) {
        Category category = new Category(synth, "LFO " + lfo, color);
        category.makePasteable("lfo");

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.add(createChooser("Waveform", Mappings.find("LFO%d_WAVEFORM", lfo)));
        vbox.add(createChooser("Range", Mappings.find("LFO%d_RANGE", lfo)));
        hbox.add(vbox);
        vbox = new VBox();
        vbox.add(createChooser("Trigger", Mappings.find("LFO%d_TRIGGER", lfo)));
        vbox.add(createChooser("Delay Trigger", Mappings.find("LFO%d_DELAY_TRIGGER", lfo)));
        hbox.add(vbox);

        Mappings mappingSync = find("LFO%d_SPEED_SYNC", lfo);
        hbox.add(createLabelledDial(Arrays.asList("Speed", "(Sync)"), mappingSync, color));
        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Speed", "(Non-Sync)"), Mappings.find("LFO%d_SPEED_NON_SYNC", lfo), color));
        hbox.add(createLabelledDial("Delay", Mappings.find("LFO%d_DELAY", lfo), color));
        // disable non-sync component when sync is in control: value != 0 (N/A)
        synth.getModel().register(mappingSync.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));

        hbox.add(createLabelledDial(Arrays.asList("Env3", "Speed", "Depth"), Mappings.find("LFO%d_ENV3_SPEED_AMOUNT", lfo), color));
        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "Speed", "Depth"), Mappings.find("LFO%d_MODWHEEL_SPEED_AMOUNT", lfo), color));
        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Speed", "Depth"), Mappings.find("LFO%d_AFTERTOUCH_SPEED_AMOUNT", lfo), color));
        hbox.add(createLabelledDial("Offset", Mappings.find("LFO%d_OFFSET", lfo), color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createARP(Color color) {
        Category category = new Category(synth, "Arpeggiator", color);

        HBox hbox = new HBox();

        VBox vbox = new VBox();
        vbox.add(createCheckBox("Enable", ARP_ON_OFF, true));
        vbox.add(createCheckBox("Latch", ARP_LATCH));
        vbox.add(createCheckBox("Mute", ARP_MUTE));
        vbox.add(createCheckBox("Key Reset", ARP_KEY_SYNC, true));
        vbox.add(createCheckBox("Constant Pitch", ARP_CONSTANT_PITCH));
        vbox.add(createCheckBox("Realtime Transpose", ARP_REALTIME_TRANSPOSE, true));
        vbox.add(createCheckBox("Output Ranging", ARP_OUTPUT_RANGING, true));
        hbox.add(vbox);

        vbox = new VBox();
        vbox.add(createChooser("Velocity", ARP_VELOCITY));
        vbox.add(createChooser("Edit Gate", ARP_EDIT_GATE));
        vbox.add(createChooser("Note Destination", ARP_OUTPUT));
        hbox.add(vbox);

        vbox = new VBox();
        vbox.add(createChooser("Fill In", ARP_FILL_IN));
        vbox.add(createChooser("Latch Type", ARP_LATCH_TYPE));
        vbox.add(createChooser("Quantize", ARP_QUANTIZE));
        hbox.add(vbox);

        hbox.add(createLabelledDial("Pattern Bank", ARP_PATTERN_BANK, color));
        hbox.add(createLabelledDial("Pattern", ARP_PATTERN, color));
        hbox.add(createLabelledDial("Octaves", ARP_OCTAVES, color));
        hbox.add(createLabelledDial("Gate Time", ARP_GATE_TIME, color));
        // TODO - enable/disable ?
        hbox.add(createLabelledDial(Arrays.asList("Rate", "(BPM)"), ARP_SPEED, color));
        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), ARP_SPEED_SYNC, color));

        category.add(hbox);
        return category;
        }

    private JComponent createEffectConfig(Color color) {
        Category category = new Category(synth, "Effect Config", color);
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.add(createChooser("Config", EFFECTS_CONFIG));
        vbox.add(createChooser("Pass to Effects", EFFECTS_INPUT));
        hbox.add(vbox);
        hbox.add(createLabelledDial(Arrays.asList("Config", "Morph"), EFFECTS_CONFIG_MORPH, color));
        hbox.add(createLabelledDial(Arrays.asList("Dry", "Level"), EFFECTS_DRY_LEVEL, color));
        category.add(hbox);
        return category;
    }
        //
    private JComponent createDelay(Color color) {
        Category category = new Category(synth, "Delay", color);
        HBox hbox = new HBox();
//
        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), DELAY_SEND_LEVEL, color));
//        hbox.add(createLabelledDial("Modwheel", DELAY_SEND_MODWHEEL, color));

        hbox.add(createLabelledDial(Arrays.asList("Time", "(Sync)"), DELAY_TIME_SYNC, color));
        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Time", "(Non-Sync)"), DELAY_TIME_NON_SYNC, color));
        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(DELAY_TIME_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));

        hbox.add(createLabelledDial("Feedback", DELAY_FEEDBACK, color));
        hbox.add(createLabelledDial(Arrays.asList("Ratio", "(L-R)"), DELAY_RATIO, color));
        hbox.add(createLabelledDial(Arrays.asList("Stereo", "Width"), DELAY_STEREO_WIDTH, color));
        hbox.add(createLabelledDial("HF Damp", DELAY_HF_DAMP, color));
        hbox.add(createLabelledDial("Mod Wheel", DELAY_SEND_MODWHEEL, color));

        category.add(hbox);
        return category;
        }
//
    private JComponent createReverb(Color color) {
        Category category = new Category(synth, "Reverb", color);

        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Type", REVERB_TYPE));
//        hbox.add(vbox);
        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), REVERB_SEND_LEVEL, color));
        hbox.add(createLabelledDial("Mod Wheel", REVERB_SEND_MODWHEEL, color));
        hbox.add(createLabelledDial("Decay", REVERB_DECAY, color));
//        hbox.add(createLabelledDial("HF Damp", REVERB_HF_DAMP, color));
        category.add(hbox);

        return category;
        }
//
    private JComponent createChorus(Color color) {
        Category category = new Category(synth, "Chorus", color);
        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Type", CHORUS_TYPE));
//        vbox.add(createChooser("Global Sync", CHORUS_GLOBAL_SYNC));
//        hbox.add(vbox);
        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), CHORUS_SEND_LEVEL, color));
        hbox.add(createLabelledDial("Mod Wheel", CHORUS_MODWHEEL_DEPTH, color));
//
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), CHORUS_RATE_SYNC, color));
        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Rate", "(Non-Sync)"), CHORUS_RATE_NON_SYNC, color));
//        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(CHORUS_RATE_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));
//
        hbox.add(createLabelledDial("Feedback", CHORUS_FEEDBACK, color));
        hbox.add(createLabelledDial("Depth", CHORUS_MOD_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Centre", "Point"), CHORUS_MOD_CENTRE_POINT, color));
//
        category.add(hbox);
        return category;
        }
//
    private JComponent createDistortion(Color color) {
        Category category = new Category(synth, "Distortion", color);
        HBox hbox = new HBox();
//
        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), DISTORTION_LEVEL, color));
        hbox.add(createLabelledDial("Mod Wheel", DISTORTION_MODWHEEL, color));
//        hbox.add(createLabelledDial("Compensation", DISTORTION_COMPENSATION, color));
//
        category.add(hbox);
        return category;
        }

    private JComponent createEqualizer(Color color) {
        Category category = new Category(synth, "Equalizer", color);

        HBox hbox = new HBox();
        hbox.add(createLabelledDial("Bass", EQUALIZER_BASS, color));
        hbox.add(createLabelledDial("Treble", EQUALIZER_TREBLE, color));
        hbox.add(createLabelledDial("Comb Frequency", EQUALIZER_COMB_FREQUENCY, color));
        hbox.add(createLabelledDial("Comb Boost", EQUALIZER_COMB_BOOST, color));

        category.add(hbox);
        return category;
        }

    // TODO - still missing:
    // - pan type
    // - dry level
    //
    private JComponent createPan(Color color) {
        Category category = new Category(synth, "Panning", color);

        HBox hbox = new HBox();

        VBox vbox = new VBox();
        vbox.add(createCheckBox("Enable", PANNING_EFFECTS));
        hbox.add(vbox);

        hbox.add(createLabelledDial("Position", PANNING_POSITION, color));
        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Non-Sync)"), PANNING_RATE_NON_SYNC, color));
        hbox.add(createLabelledDial("Depth", PANNING_DEPTH, color));

        category.add(hbox);
        return category;
        }

    private JComponent createVocoder(Color color) {
        Category category = new Category(synth, "Vocoder", color);

        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Sibilance Type", VOCODER_SIBILANCE_TYPE));
//        hbox.add(vbox);
        hbox.add(createLabelledDial("Balance", VOCODER_BALANCE, color));
//        hbox.add(createLabelledDial(Arrays.asList("Stereo", "Width"), VOCODER_STEREO_WIDTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Sibilance", "Level"), VOCODER_SIBILANCE_LEVEL, color));
//
        category.add(hbox);
        return category;
        }

    // convenience method to create chooser
    private Chooser createChooser(String label, Mappings mappings) {
        Boundaries boundaries = mappings.getBoundaries();
        // sanity
        if (boundaries.getValues() == null) {
            throw new IllegalStateException("expecting values for a chooser component !");
            }
        return new Chooser(label, synth,
            mappings.getKey(),
            mappings.getBoundaries().getValues()) {
            };
        }

    // convenience method to create labelledDial with single label
    private LabelledDial createLabelledDial(String label, Mappings mappings, Color color) {
        return createLabelledDial(Arrays.asList(label), mappings, color);
        }

    // convenience method to create labelledDial with multiple labels
    private LabelledDial createLabelledDial(List<String> labels, Mappings mappings, Color color) {
        // sanity
        if (Objects.requireNonNull(labels).isEmpty()) {
            throw new IllegalStateException("at least one label required");
            }
        Boundaries boundaries = mappings.getBoundaries();
        LabelledDial result = new LabelledDial(labels.get(0), synth,
            mappings.getKey(),
            color, boundaries.getMin(), boundaries.getMax(), boundaries.getOffset()) {
            @Override
            public String map(int val) {
                String[] values = boundaries.getValues();
                if (values != null && values.length != 0) {
                    if (val >= boundaries.getMin() && val <= boundaries.getMax()) {
                        return boundaries.getValues()[val];
                        } else {
                        System.err.println("Invalid value '" + val + "' received for " + mappings.name());
                        return "??";
                        }
                    }
                return String.valueOf(val - boundaries.getOffset());
                }
            };
        for (int index = 1; index < labels.size(); ++index) {
            result.addAdditionalLabel(labels.get(index));
            }
        return result;
        }

    // convenience method to create checkbox
    private JComponent createCheckBox(String label, Mappings mappings) {
        return createCheckBox(label, mappings, false);
        }

    // convenience method to create checkbox
    private JComponent createCheckBox(String label, Mappings mappings, boolean flipped) {
        // sanity - too strict for ARP_LATCH though
        if (mappings.getBoundaries() != Boundaries.BOOLEAN) {
            throw new IllegalStateException("only boolean allowed for checkbox");
        }
        return new CheckBox(label, synth, mappings.getKey(), flipped);
        }
}
