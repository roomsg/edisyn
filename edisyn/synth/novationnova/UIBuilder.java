package edisyn.synth.novationnova;

import edisyn.Synth;
import edisyn.gui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        hbox.addLast(createLFO(2, Style.COLOR_B()));
        vbox.add(hbox);

        vbox.add(createFilter(Style.COLOR_C()));

        envelopeLfoFilterPanel.add(vbox, BorderLayout.CENTER);
        synth.addTab("Env LFO Filter", envelopeLfoFilterPanel);

        // ARP, EFFECTS PANEL
        JComponent arpEffectsPanel = new SynthPanel(synth);
        vbox = new VBox();
        hbox = new HBox();
        hbox.add(createARP(Style.COLOR_A()));
        hbox.add(createEffectConfig(Style.COLOR_B()));
//        hbox.addLast(createVocoder(Style.COLOR_B()));
//        vbox.add(hbox);
//        hbox = new HBox();
//        hbox.add(createDelay(Style.COLOR_C()));
//        hbox.addLast(createReverb(Style.COLOR_A()));
//        vbox.add(hbox);
//        hbox = new HBox();
//        hbox.add(createChorus(Style.COLOR_B()));
//        hbox.addLast(createDistortion(Style.COLOR_C()));
//        vbox.add(hbox);
//        hbox = new HBox();
//        hbox.add(createEqualizer(Style.COLOR_A()));
//        hbox.addLast(createPan(Style.COLOR_B()));
//        vbox.add(hbox);
//
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

        // NOTE - would probably be nicer to have next 2 combined into a single dial [-24, +36]
        hbox.add(createLabelledDial("Octave", Mappings.find("OSC%d_OCTAVE", osc), color));
        hbox.add(createLabelledDial("Semitone", Mappings.find("OSC%d_SEMITONE", osc), color));
        hbox.add(createLabelledDial("Detune", Mappings.find("OSC%d_FINE_TUNE", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Bend Wheel", "Depth"), Mappings.find("OSC%d_BENDWHEEL_AMOUNT", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Key", "Sync"), Mappings.find("OSC%d_KEY_SYNC", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Sync", "Skew"), Mappings.find("OSC%d_SYNC_SKEW", osc), color));
        hbox.add(createLabelledDial(Arrays.asList("Sync", "Formant", "Width"), Mappings.find("OSC%d_SYNC_FORMANT_WIDTH", osc), color));
        // mod matrix
        hbox.add(createLabelledDial("Mix", Mappings.find("OSC%d_MIX", osc), color));
        hbox.add(createLabelledDial("Pitch", Mappings.find("OSC%d_PITCH", osc), color));
        hbox.add(createLabelledDial("Width", Mappings.find("OSC%d_PULSE_WIDTH", osc), color));
        hbox.add(createLabelledDial("Sync", Mappings.find("OSC%d_SYNC", osc), color));
        hbox.add(createLabelledDial("Soften", Mappings.find("OSC%d_SOFTEN", osc), color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createXMod(final int xmod, Color color) {
        Category category = new Category(synth, "Xmod " + xmod + "x3", color);
        category.makePasteable("xmod");

        HBox hbox = new HBox();

        VBox vbox = new VBox();
        TextLabel label = new TextLabel("<  Mix  >");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // hack - for esthetical reasons
        label.setPreferredSize(new Dimension(90,20));
        vbox.add(label, BorderLayout.NORTH);
        hbox.add(vbox);

        // mod matrix
        hbox.add(createLabelledDial("Level", Mappings.find("XMOD%d_MIX", xmod), color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo1", "Depth"), Mappings.find("XMOD%d_MIX_LFO1_DEPTH", xmod), color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo2", "Depth"), Mappings.find("XMOD%d_MIX_LFO2_DEPTH", xmod), color));
        hbox.add(createLabelledDial(Arrays.asList("Env2", "Depth"), Mappings.find("XMOD%d_MIX_ENV2_DEPTH", xmod), color));
        hbox.add(createLabelledDial(Arrays.asList("Env3", "Depth"), Mappings.find("XMOD%d_MIX_ENV3_DEPTH", xmod), color));
        hbox.add(createLabelledDial(Arrays.asList("Wheel", "Depth"), Mappings.find("XMOD%d_MIX_WHEEL_DEPTH", xmod), color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
    }

    private JComponent createNoise(Color color) {
        Category category = new Category(synth, "Noise", color);
        category.makePasteable("xmod");

        HBox hbox = new HBox();

        // mod matrix - level
        VBox vbox = new VBox();
        TextLabel label = new TextLabel("<  Mix  >");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // hack - for esthetical reasons
        label.setPreferredSize(new Dimension(90,20));
        vbox.add(label, BorderLayout.NORTH);
        hbox.add(vbox);

        hbox.add(createLabelledDial("Level", NOISE_MIX, color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo1", "Depth"), NOISE_MIX_LFO1_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo2", "Depth"), NOISE_MIX_LFO2_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Env2", "Depth"), NOISE_MIX_ENV2_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Env3", "Depth"), NOISE_MIX_ENV3_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Wheel", "Depth"), NOISE_MIX_WHEEL_DEPTH, color));

        // mod matrix - soften
        vbox = new VBox();
        label = new TextLabel("< Soften >");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // hack - for esthetical reasons
        label.setPreferredSize(new Dimension(90,20));
        vbox.add(label, BorderLayout.NORTH);
        hbox.add(vbox);

        hbox.add(createLabelledDial("Soften", NOISE_SOFTEN, color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo1", "Depth"), NOISE_SOFTEN_LFO1_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Lfo2", "Depth"), NOISE_SOFTEN_LFO2_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Env2", "Depth"), NOISE_SOFTEN_ENV2_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Env3", "Depth"), NOISE_SOFTEN_ENV3_DEPTH, color));
        hbox.add(createLabelledDial(Arrays.asList("Wheel", "Depth"), NOISE_SOFTEN_WHEEL_DEPTH, color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
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

//    private JComponent createPitchModulation(Color color) {
//        Category category = new Category(synth, "Pitch Mod", color);
//
//        HBox hbox = new HBox();
//        hbox.add(createLabelledDial(Arrays.asList("Modwheel", "Depth"), OSCS_MODWHEEL_PITCH_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Depth"), OSCS_AFTERTCH_PITCH_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Breath", "Depth"), OSCS_BREATH_PITCH_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "LFO1 Depth"), OSCS_MODWHEEL_LFO1_PITCH_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "LFO1 Depth"), OSCS_AFTERTCH_LFO1_PITCH_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Breath", "LFO1 Depth"), OSCS_BREATH_LFO1_PITCH_DEPTH, color));
//
//        category.add(hbox, BorderLayout.CENTER);
//        return category;
//        }
//
//    private JComponent createAmpModulation(Color color) {
//        Category category = new Category(synth, "Amp Mod", color);
//
//        HBox hbox = new HBox();
//        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "Depth"), OSCS_MODWHEEL_AMPLITUDE_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Depth"), OSCS_AFTERTCH_AMPLITUDE_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Breath", "Depth"), OSCS_BREATH_AMPLITUDE_DEPTH, color));
//
//        category.add(hbox, BorderLayout.CENTER);
//        return category;
//        }

//    private JComponent createExtAudio(Color color) {
//        Category category = new Category(synth, "External Audio", color);
//
//        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createCheckBox("Trigger", EXT_AUDIO_TRIGGER));
//        vbox.add(createCheckBox("Direct To FX", EXT_AUDIO_TO_FX));
//        hbox.add(vbox);
//        category.add(hbox, BorderLayout.CENTER);
//        return category;
//        }

    private JComponent createFilter(Color color) {
        Category category = new Category(synth, "Filter", color);
        HBox mainhbox = new HBox();

        VBox vbox1 = new VBox();
        vbox1.add(createChooser("Type", FILTER_TYPE));
        vbox1.add(createChooser("Slope", FILTER_SLOPE));
        mainhbox.add(vbox1);

        VBox vbox2 = new VBox();
        HBox hbox = new HBox();
        hbox.add(createLabelledDial("Frequency", FILTER_FREQ, color));
        hbox.add(createLabelledDial("Resonance", FILTER_RESONANCE, color));
        hbox.add(createLabelledDial("Overdrive", FILTER_OVERDRIVE, color));
        hbox.add(createLabelledDial("Key Track", FILTER_KEY_TRACK, color));
        hbox.add(createLabelledDial("Q Normalize", FILTER_Q_NORMALIZE, color));
        hbox.add(createLabelledDial("Special Width", FILTER_SPECIAL_WIDTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "Depth"), FILTER_MODWHEEL_FREQUENCY_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Mod Env", "Depth"), FILTER_ENV2_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("LFO2", "Depth"), FILTER_LFO2_DEPTH, color));
        vbox2.add(hbox);

//        hbox = new HBox();
//        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "Depth"), FILTER_AFTERTCH_FREQUENCY_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Breath", "Depth"), FILTER_BREATH_FREQUENCY_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Mod Wheel", "LFO2 depth"), FILTER_MODWHEEL_LFO2_FREQUENCY_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Aftertouch", "LFO2 depth"), FILTER_AFTERTCH_LFO2_FREQUENCY_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Breath", "LFO2 depth"), FILTER_BREATH_LFO2_FREQUENCY_DEPTH, color));
//        vbox2.add(hbox);
        mainhbox.add(vbox2);

        category.add(mainhbox, BorderLayout.CENTER);
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

        Mappings mappingsAttack = Mappings.find("ENVELOPE%d_ATTACK", envelope);
        hbox.add(createLabelledDial("Attack", mappingsAttack, color));

        Mappings mappingsDecay = Mappings.find("ENVELOPE%d_DECAY", envelope);
        hbox.add(createLabelledDial("Decay", mappingsDecay, color));

        Mappings mappingsSustain = Mappings.find("ENVELOPE%d_SUSTAIN", envelope);
        hbox.add(createLabelledDial("Sustain", mappingsSustain, color));

        Mappings mappingsRelease = Mappings.find("ENVELOPE%d_RELEASE", envelope);
        hbox.add(createLabelledDial("Release", mappingsRelease, color));

        // ADSR
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
        vbox.add(createChooser("Trigger", Mappings.find("LFO%d_TRIGGER", lfo)));
        vbox.add(createChooser("Range", Mappings.find("LFO%d_RANGE", lfo)));
        hbox.add(vbox);

        Mappings mappingSync = find("LFO%d_SPEED_SYNC", lfo);
        hbox.add(createLabelledDial(Arrays.asList("Speed", "(Sync)"), mappingSync, color));
        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Speed", "(Non-Sync)"), Mappings.find("LFO%d_SPEED_NON_SYNC", lfo), color));
        // disable non-sync component when sync is in control: value != 0 (N/A)
        synth.getModel().register(mappingSync.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));

        hbox.add(createLabelledDial("Delay", Mappings.find("LFO%d_DELAY", lfo), color));

        category.add(hbox, BorderLayout.CENTER);
        return category;
        }

    private JComponent createARP(Color color) {
        Category category = new Category(synth, "Arpeggiator", color);

        HBox hbox = new HBox();

//        VBox vbox = new VBox();
//        vbox.add(createCheckBox("Enable", ARP_ON_OFF));
//        vbox.add(createCheckBox("Key Sync", ARP_KEY_SYNC));
//        vbox.add(createCheckBox("Latch", ARP_LATCH));
//        hbox.add(vbox);
//
//        vbox = new VBox();
//        vbox.add(createChooser("Pattern", ARP_PATTERN));
//        vbox.add(createChooser("Note Destination", ARP_NOTE_DESTINATION));
//        hbox.add(vbox);
//
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), ARP_RATE_SYNC, color));
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(BPM)"), ARP_RATE_NON_SYNC, color));
//        hbox.add(createLabelledDial("Octaves", ARP_OCTAVES, color));
//        hbox.add(createLabelledDial("Gate Time", ARP_GATE_TIME, color));

        category.add(hbox);
        return category;
        }

    private JComponent createEffectConfig(Color color) {
        Category category = new Category(synth, "Effect Config", color);
        HBox hbox = new HBox();
        hbox.add(createLabelledDial(Arrays.asList("Effect", "Config", "Morph"), EFFECT_MORPH, color));
        category.add(hbox);
        return category;
    }
        //
//    private JComponent createDelay(Color color) {
//        Category category = new Category(synth, "Delay", color);
//        HBox hbox = new HBox();
//
//        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), DELAY_SEND_LEVEL, color));
//        hbox.add(createLabelledDial("Modwheel", DELAY_SEND_MODWHEEL, color));
//
//        hbox.add(createLabelledDial(Arrays.asList("Time", "(Sync)"), DELAY_TIME_SYNC, color));
//        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Time", "(Non-Sync)"), DELAY_TIME_NON_SYNC, color));
//        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(DELAY_TIME_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));
//
//        hbox.add(createLabelledDial("Feedback", DELAY_FEEDBACK, color));
//
//        hbox.add(createLabelledDial(Arrays.asList("Ratio", "(L-R)"), DELAY_RATIO, color));
//        hbox.add(createLabelledDial(Arrays.asList("Stereo", "Width"), DELAY_STEREO_WIDTH, color));
//
//        category.add(hbox);
//        return category;
//        }
//
//    private JComponent createReverb(Color color) {
//        Category category = new Category(synth, "Reverb", color);
//
//        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Type", REVERB_TYPE));
//        hbox.add(vbox);
//        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), REVERB_SEND_LEVEL, color));
//        hbox.add(createLabelledDial("Mod Wheel", REVERB_SEND_MODWHEEL, color));
//        hbox.add(createLabelledDial("Decay", REVERB_DECAY, color));
//        category.add(hbox);
//
//        return category;
//        }
//
//    private JComponent createChorus(Color color) {
//        Category category = new Category(synth, "Chorus", color);
//        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Type", CHORUS_TYPE));
//        vbox.add(createChooser("Global Sync", CHORUS_GLOBAL_SYNC));
//        hbox.add(vbox);
//        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), CHORUS_SEND_LEVEL, color));
//        hbox.add(createLabelledDial("Mod Wheel", CHORUS_SEND_MODWHEEL, color));
//
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), CHORUS_RATE_SYNC, color));
//        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Rate", "(Non-Sync)"), CHORUS_RATE_NON_SYNC, color));
//        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(CHORUS_RATE_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));
//
//        hbox.add(createLabelledDial("Feedback", CHORUS_FEEDBACK, color));
//        hbox.add(createLabelledDial("Depth", CHORUS_MOD_DEPTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Centre", "Point"), CHORUS_MOD_CENTRE_POINT, color));
//
//        category.add(hbox);
//        return category;
//        }
//
//    private JComponent createDistortion(Color color) {
//        Category category = new Category(synth, "Distortion", color);
//        HBox hbox = new HBox();
//
//        hbox.add(createLabelledDial(Arrays.asList("Send", "Level"), DISTORTION_LEVEL, color));
//        hbox.add(createLabelledDial("Mod Wheel", DISTORTION_MODWHEEL, color));
//        hbox.add(createLabelledDial("Compensation", DISTORTION_COMPENSATION, color));
//
//        category.add(hbox);
//        return category;
//        }
//
//    private JComponent createEqualizer(Color color) {
//        Category category = new Category(synth, "Equalizer", color);
//
//        HBox hbox = new HBox();
//
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Global Sync", EQUALIZER_GLOBAL_SYNC));
//        hbox.add(vbox);
//
//        hbox.add(createLabelledDial("Level", EQUALIZER_LEVEL, color));
//        hbox.add(createLabelledDial("Frequency", EQUALIZER_FREQUENCY, color));
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), EQUALIZER_RATE_SYNC, color));
//        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Rate", "(Non-Sync)"), EQUALIZER_RATE_NON_SYNC, color));
//        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(EQUALIZER_RATE_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));
//
//        hbox.add(createLabelledDial("Mod Depth", EQUALIZER_MOD_DEPTH, color));
//
//        category.add(hbox);
//        return category;
//        }
//
//
//    private JComponent createPan(Color color) {
//        Category category = new Category(synth, "Panning", color);
//
//        HBox hbox = new HBox();
//
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Global Sync", PANNING_GLOBAL_SYNC));
//        hbox.add(vbox);
//
//        hbox.add(createLabelledDial("Position", PANNING_POSITION, color));
//
//        hbox.add(createLabelledDial(Arrays.asList("Rate", "(Sync)"), PANNING_RATE_SYNC, color));
//        Component compNonSync = hbox.add(createLabelledDial(Arrays.asList("Rate", "(Non-Sync)"), PANNING_RATE_NON_SYNC, color));
//        // disable non-sync component when sync is in control: value != 0 (N/A)
//        synth.getModel().register(PANNING_RATE_SYNC.getKey(), (key, model) -> compNonSync.setEnabled(model.get(key) == 0));
//
//        hbox.add(createLabelledDial("Depth", PANNING_MOD_DEPTH, color));
//
//        category.add(hbox);
//        return category;
//        }
//
//    private JComponent createVocoder(Color color) {
//        Category category = new Category(synth, "Vocoder", color);
//
//        HBox hbox = new HBox();
//        VBox vbox = new VBox();
//        vbox.add(createChooser("Sibilance Type", VOCODER_SIBILANCE_TYPE));
//        hbox.add(vbox);
//        hbox.add(createLabelledDial("Balance", VOCODER_BALANCE, color));
//        hbox.add(createLabelledDial(Arrays.asList("Stereo", "Width"), VOCODER_STEREO_WIDTH, color));
//        hbox.add(createLabelledDial(Arrays.asList("Sibilance", "Level"), VOCODER_SIBILANCE_LEVEL, color));
//
//        category.add(hbox);
//        return category;
//        }

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
        // sanity
        if (mappings.getBoundaries() != Boundaries.BOOLEAN) {
            throw new IllegalStateException("only boolean allowed for checkbox");
            }
        return new CheckBox(label, synth, mappings.getKey());
        }
    }
