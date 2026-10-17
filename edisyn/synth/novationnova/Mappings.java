package edisyn.synth.novationnova;

import java.util.Locale;
import java.util.Objects;

/**
 * Enumeration containing all available synth/program parameters and their so-called 'mappings'.
 * <p>
 * Each and every entry is defining (the mapping between)
 * <ul>
 * <li>(Edisyn) model key</li>
 * <li>(MIDI) byte-index in (sysex) program dump</li>
 * <li>(MIDI) CC</li>
 * <li>(MIDI) NRPN</li>
 * </ul>
 *
 * <p>
 * Note we have two kind of mappings:
 * <ol>
 *     <li>the 'straight' mappings: one-to-one relationship between (Edisyn) model key and MIDI parameter</li>
 *     <li>the 'packed' mappings: one-to-many relationship between (Edisyn) model key and MIDI parameters</li>
 * </ol>
 * </p>
 */
enum Mappings {
    ////
    // Dummy entries, only here for the sake of code simplicity
    ////
    MODULATION_OSC1(null, null, null, Boundaries.MODULATION_DESTINATION_OSC),
    MODULATION_OSC2(null, null, null, Boundaries.MODULATION_DESTINATION_OSC),
    MODULATION_OSC3(null, null, null, Boundaries.MODULATION_DESTINATION_OSC),
    MODULATION_XMOD1(null, null, null, Boundaries.MODULATION_DESTINATION_XMOD),
    MODULATION_XMOD2(null, null, null, Boundaries.MODULATION_DESTINATION_XMOD),
    MODULATION_NOISE(null, null, null, Boundaries.MODULATION_DESTINATION_NOISE),
    MODULATION_FILTER(null, null, null, Boundaries.MODULATION_DESTINATION_FILTER),
    ////
    // program/patch & oscillators - global (= applying to all oscillators)
    ////
    POLYPHONY_MODE(Convertors.Packed.PACKED_084, Boundaries.POLYPHONY_MODE),
    OSC_MODE(Convertors.Packed.PACKED_084, Boundaries.OSC_MODE),
    GLIDE_TYPE(Convertors.Packed.PACKED_001, Boundaries.GLIDE_TYPES),
    PORTAMENTO_TYPE(Convertors.Packed.PACKED_000, Boundaries.PORTAMENTO_TYPE),
    PORTAMENTO_MODE(Convertors.Packed.PACKED_000, Boundaries.PORTAMENTO_MODE),
    PORTAMENTO_TIME(null, 5, null),
    UNISON_VOICES(Convertors.Packed.PACKED_100, Boundaries.UNISON_VOICES),
    CONSTANT_GATE(Convertors.Packed.PACKED_100, Boundaries.BOOLEAN),
    DCO_VCO_MODE(Convertors.Packed.PACKED_100, Boundaries.DCO_VCO),
    PROGRAM_VOLUME(null, 7, null),
    ////
    // Oscillators - individual
    // 3 identical oscillators are available:
    ////
    OSC1_WAVEFORM(Convertors.Packed.PACKED_000, Boundaries.OSC_WAVEFORMS),
    OSC1_OCTAVE(Convertors.Packed.PACKED_000, Boundaries.OSC_OCTAVES),
    OSC1_SEMITONE(Convertors.Packed.PACKED_000, Boundaries.OSC_SEMITONES),
    OSC1_FINE_TUNE(null, 11, null, Boundaries.CENTRIC_127),
    OSC1_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED_084, Boundaries.BEND_WHEEL),
    OSC1_KEY_SYNC(null, null, 78),
    OSC1_SYNC_SKEW(null, null, 81, Boundaries.CENTRIC_127),
    OSC1_SYNC_FORMANT_WIDTH(null, null, 75),
    OSC1_MODWHEEL_LFO1_AMOUNT(null, null, 69, Boundaries.CENTRIC_127),
    OSC1_AFTERTOUCH_LFO1_AMOUNT(null, null, 72, Boundaries.CENTRIC_127),
    //// osc1-mod matrix
    OSC1_MIX(null, 28, null),
    OSC1_MIX_LFO1_DEPTH(null, null, 14, Boundaries.CENTRIC_127),
    OSC1_MIX_LFO2_DEPTH(null, null, 15, Boundaries.CENTRIC_127),
    OSC1_MIX_ENV2_DEPTH(null, 36, null, Boundaries.CENTRIC_127),
    OSC1_MIX_ENV3_DEPTH(null, null, 13, Boundaries.CENTRIC_127),
    OSC1_MIX_WHEEL_DEPTH(null, null, 16, Boundaries.CENTRIC_127),
    OSC1_PITCH(null, null, 17),
    OSC1_PITCH_LFO1_DEPTH(null, 21, null, Boundaries.CENTRIC_127),
    OSC1_PITCH_LFO2_DEPTH(null, null, 2, Boundaries.CENTRIC_127),
    OSC1_PITCH_ENV2_DEPTH(null, 20, null, Boundaries.CENTRIC_127),
    OSC1_PITCH_ENV3_DEPTH(null, 66, null, Boundaries.CENTRIC_127),
    OSC1_PITCH_WHEEL_DEPTH(null, null, 3, Boundaries.CENTRIC_127),
    OSC1_WIDTH(null, 22, null, Boundaries.CENTRIC_127),
    OSC1_WIDTH_LFO1_DEPTH(null, 46, null, Boundaries.CENTRIC_127),
    OSC1_WIDTH_LFO2_DEPTH(null, 69, null, Boundaries.CENTRIC_127),
    OSC1_WIDTH_ENV2_DEPTH(null, 43, null, Boundaries.CENTRIC_127),
    OSC1_WIDTH_ENV3_DEPTH(null, null, 4, Boundaries.CENTRIC_127),
    OSC1_WIDTH_WHEEL_DEPTH(null, null, 5, Boundaries.CENTRIC_127),
    OSC1_SYNC(null, 49, null),
    OSC1_SYNC_LFO1_DEPTH(null, 55, null, Boundaries.CENTRIC_127),
    OSC1_SYNC_LFO2_DEPTH(null, null, 7, Boundaries.CENTRIC_127),
    OSC1_SYNC_ENV2_DEPTH(null, 52, null, Boundaries.CENTRIC_127),
    OSC1_SYNC_ENV3_DEPTH(null, null, 6, Boundaries.CENTRIC_127),
    OSC1_SYNC_WHEEL_DEPTH(null, null, 8, Boundaries.CENTRIC_127),
    OSC1_SOFTEN(null, 13, null),
    OSC1_SOFTEN_LFO1_DEPTH(null, null, 10, Boundaries.CENTRIC_127),
    OSC1_SOFTEN_LFO2_DEPTH(null, null, 11, Boundaries.CENTRIC_127),
    OSC1_SOFTEN_ENV2_DEPTH(null, 61, null, Boundaries.CENTRIC_127),
    OSC1_SOFTEN_ENV3_DEPTH(null, null, 9, Boundaries.CENTRIC_127),
    OSC1_SOFTEN_WHEEL_DEPTH(null, null, 12, Boundaries.CENTRIC_127),

    OSC2_WAVEFORM(Convertors.Packed.PACKED_000, Boundaries.OSC_WAVEFORMS),
    OSC2_OCTAVE(Convertors.Packed.PACKED_000, Boundaries.OSC_OCTAVES),
    OSC2_SEMITONE(Convertors.Packed.PACKED_000, Boundaries.OSC_SEMITONES),
    OSC2_FINE_TUNE(null, 23, null, Boundaries.CENTRIC_127),
    OSC2_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED_084, Boundaries.BEND_WHEEL),
    OSC2_KEY_SYNC(null, null, 79),
    OSC2_SYNC_SKEW(null, null, 82, Boundaries.CENTRIC_127),
    OSC2_SYNC_FORMANT_WIDTH(null, null, 76),
    OSC2_MODWHEEL_LFO1_AMOUNT(null, null, 70, Boundaries.CENTRIC_127),
    OSC2_AFTERTOUCH_LFO1_AMOUNT(null, null, 73, Boundaries.CENTRIC_127),
    //// osc2-mod matrix
    OSC2_MIX(null, 29, null),
    OSC2_MIX_LFO1_DEPTH(null, null, 30, Boundaries.CENTRIC_127),
    OSC2_MIX_LFO2_DEPTH(null, null, 31, Boundaries.CENTRIC_127),
    OSC2_MIX_ENV2_DEPTH(null, 37, null, Boundaries.CENTRIC_127),
    OSC2_MIX_ENV3_DEPTH(null, null, 29, Boundaries.CENTRIC_127),
    OSC2_MIX_WHEEL_DEPTH(null, null, 32, Boundaries.CENTRIC_127),
    OSC2_PITCH(null, null, 33),
    OSC2_PITCH_LFO1_DEPTH(null, 26, null, Boundaries.CENTRIC_127),
    OSC2_PITCH_LFO2_DEPTH(null, null, 18, Boundaries.CENTRIC_127),
    OSC2_PITCH_ENV2_DEPTH(null, 25, null, Boundaries.CENTRIC_127),
    OSC2_PITCH_ENV3_DEPTH(null, 67, null, Boundaries.CENTRIC_127),
    OSC2_PITCH_WHEEL_DEPTH(null, null, 19, Boundaries.CENTRIC_127),
    OSC2_WIDTH(null, 27, null, Boundaries.CENTRIC_127),
    OSC2_WIDTH_LFO1_DEPTH(null, 47, null, Boundaries.CENTRIC_127),
    OSC2_WIDTH_LFO2_DEPTH(null, 70, null, Boundaries.CENTRIC_127),
    OSC2_WIDTH_ENV2_DEPTH(null, 44, null, Boundaries.CENTRIC_127),
    OSC2_WIDTH_ENV3_DEPTH(null, null, 20, Boundaries.CENTRIC_127),
    OSC2_WIDTH_WHEEL_DEPTH(null, null, 21, Boundaries.CENTRIC_127),
    OSC2_SYNC(null, 50, null),
    OSC2_SYNC_LFO1_DEPTH(null, 56, null, Boundaries.CENTRIC_127),
    OSC2_SYNC_LFO2_DEPTH(null, null, 23, Boundaries.CENTRIC_127),
    OSC2_SYNC_ENV2_DEPTH(null, 53, null, Boundaries.CENTRIC_127),
    OSC2_SYNC_ENV3_DEPTH(null, null, 22, Boundaries.CENTRIC_127),
    OSC2_SYNC_WHEEL_DEPTH(null, null, 24, Boundaries.CENTRIC_127),
    OSC2_SOFTEN(null, 14, null),
    OSC2_SOFTEN_LFO1_DEPTH(null, null, 26, Boundaries.CENTRIC_127),
    OSC2_SOFTEN_LFO2_DEPTH(null, null, 27, Boundaries.CENTRIC_127),
    OSC2_SOFTEN_ENV2_DEPTH(null, 62, null, Boundaries.CENTRIC_127),
    OSC2_SOFTEN_ENV3_DEPTH(null, null, 25, Boundaries.CENTRIC_127),
    OSC2_SOFTEN_WHEEL_DEPTH(null, null, 28, Boundaries.CENTRIC_127),

    OSC3_WAVEFORM(Convertors.Packed.PACKED_000, Boundaries.OSC_WAVEFORMS),
    OSC3_OCTAVE(Convertors.Packed.PACKED_000, Boundaries.OSC_OCTAVES),
    OSC3_SEMITONE(Convertors.Packed.PACKED_000, Boundaries.OSC_SEMITONES),
    OSC3_FINE_TUNE(null, 12, null, Boundaries.CENTRIC_127),
    OSC3_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED_084, Boundaries.BEND_WHEEL),
    OSC3_KEY_SYNC(null, null, 80),
    OSC3_SYNC_SKEW(null, null, 83, Boundaries.CENTRIC_127),
    OSC3_SYNC_FORMANT_WIDTH(null, null, 77),
    OSC3_MODWHEEL_LFO1_AMOUNT(null, null, 71, Boundaries.CENTRIC_127),
    OSC3_AFTERTOUCH_LFO1_AMOUNT(null, null, 74, Boundaries.CENTRIC_127),
    //// osc3-mod matrix
    OSC3_MIX(null, 33, null),
    OSC3_MIX_LFO1_DEPTH(null, null, 46, Boundaries.CENTRIC_127),
    OSC3_MIX_LFO2_DEPTH(null, null, 47, Boundaries.CENTRIC_127),
    OSC3_MIX_ENV2_DEPTH(null, 38, null, Boundaries.CENTRIC_127),
    OSC3_MIX_ENV3_DEPTH(null, null, 45, Boundaries.CENTRIC_127),
    OSC3_MIX_WHEEL_DEPTH(null, null, 48, Boundaries.CENTRIC_127),
    OSC3_PITCH(null, null, 49),
    OSC3_PITCH_LFO1_DEPTH(null, 41, null, Boundaries.CENTRIC_127),
    OSC3_PITCH_LFO2_DEPTH(null, null, 34, Boundaries.CENTRIC_127),
    OSC3_PITCH_ENV2_DEPTH(null, 40, null, Boundaries.CENTRIC_127),
    OSC3_PITCH_ENV3_DEPTH(null, 68, null, Boundaries.CENTRIC_127),
    OSC3_PITCH_WHEEL_DEPTH(null, null, 35),
    OSC3_WIDTH(null, 42, null, Boundaries.CENTRIC_127),
    OSC3_WIDTH_LFO1_DEPTH(null, 48, null, Boundaries.CENTRIC_127),
    OSC3_WIDTH_LFO2_DEPTH(null, 71, null, Boundaries.CENTRIC_127),
    OSC3_WIDTH_ENV2_DEPTH(null, 45, null, Boundaries.CENTRIC_127),
    OSC3_WIDTH_ENV3_DEPTH(null, null, 36, Boundaries.CENTRIC_127),
    OSC3_WIDTH_WHEEL_DEPTH(null, null, 37, Boundaries.CENTRIC_127),
    OSC3_SYNC(null, 51, null),
    OSC3_SYNC_LFO1_DEPTH(null, 57, null, Boundaries.CENTRIC_127),
    OSC3_SYNC_LFO2_DEPTH(null, null, 39, Boundaries.CENTRIC_127),
    OSC3_SYNC_ENV2_DEPTH(null, 54, null, Boundaries.CENTRIC_127),
    OSC3_SYNC_ENV3_DEPTH(null, null, 38, Boundaries.CENTRIC_127),
    OSC3_SYNC_WHEEL_DEPTH(null, null, 40, Boundaries.CENTRIC_127),
    OSC3_SOFTEN(null, 15, null),
    OSC3_SOFTEN_LFO1_DEPTH(null, null, 42, Boundaries.CENTRIC_127),
    OSC3_SOFTEN_LFO2_DEPTH(null, null, 43, Boundaries.CENTRIC_127),
    OSC3_SOFTEN_ENV2_DEPTH(null, 63, null, Boundaries.CENTRIC_127),
    OSC3_SOFTEN_ENV3_DEPTH(null, null, 41, Boundaries.CENTRIC_127),
    OSC3_SOFTEN_WHEEL_DEPTH(null, null, 44, Boundaries.CENTRIC_127),

    //// xmod1-mod matrix
    XMOD1_MIX(null, 31, null),
    XMOD1_MIX_LFO1_DEPTH(null, null, 52, Boundaries.CENTRIC_127),
    XMOD1_MIX_LFO2_DEPTH(null, null, 53, Boundaries.CENTRIC_127),
    XMOD1_MIX_ENV2_DEPTH(null, null, 50, Boundaries.CENTRIC_127),
    XMOD1_MIX_ENV3_DEPTH(null, null, 51, Boundaries.CENTRIC_127),
    XMOD1_MIX_WHEEL_DEPTH(null, null, 54, Boundaries.CENTRIC_127),

    //// xmod2-mod matrix
    XMOD2_MIX(null, 4, null),
    XMOD2_MIX_LFO1_DEPTH(null, null, 57, Boundaries.CENTRIC_127),
    XMOD2_MIX_LFO2_DEPTH(null, null, 58, Boundaries.CENTRIC_127),
    XMOD2_MIX_ENV2_DEPTH(null, null, 55, Boundaries.CENTRIC_127),
    XMOD2_MIX_ENV3_DEPTH(null, null, 56, Boundaries.CENTRIC_127),
    XMOD2_MIX_WHEEL_DEPTH(null, null, 59, Boundaries.CENTRIC_127),

    //// noise-mod matrix
    NOISE_MIX(null, 30, null),
    NOISE_MIX_LFO1_DEPTH(null, null, 66, Boundaries.CENTRIC_127),
    NOISE_MIX_LFO2_DEPTH(null, null, 67, Boundaries.CENTRIC_127),
    NOISE_MIX_ENV2_DEPTH(null, 39, null, Boundaries.CENTRIC_127),
    NOISE_MIX_ENV3_DEPTH(null, null, 65, Boundaries.CENTRIC_127),
    NOISE_MIX_WHEEL_DEPTH(null, null, 68, Boundaries.CENTRIC_127),

    NOISE_SOFTEN(null, 24, null),
    NOISE_SOFTEN_LFO1_DEPTH(null, null, 62, Boundaries.CENTRIC_127),
    NOISE_SOFTEN_LFO2_DEPTH(null, null, 63, Boundaries.CENTRIC_127),
    NOISE_SOFTEN_ENV2_DEPTH(null, null, 60, Boundaries.CENTRIC_127),
    NOISE_SOFTEN_ENV3_DEPTH(null, null, 61, Boundaries.CENTRIC_127),
    NOISE_SOFTEN_WHEEL_DEPTH(null, null, 64, Boundaries.CENTRIC_127),
    ////
    // Envelopes
    // - envelope1 (ADSR): amplitude, including wheel & aftertouch
    // - envelope2 (ADSR): including delay
    // - envelope3 (ADSR): including delay
    ////
    ENVELOPE1_ATTACK(null, 108, null),
    ENVELOPE1_DECAY(null, 109, null),
    ENVELOPE1_SUSTAIN(null, 110, null),
    ENVELOPE1_RELEASE(null, 111, null),
    ENVELOPE1_TRIGGER(Convertors.Packed.PACKED_001, Boundaries.ENV_TRIGGERS),
    ENVELOPE1_VELOCITY_DEPTH(61, 112, null, Boundaries.CENTRIC_127),
    // env1 specific
    ENVELOPE1_WHEEL(null, null, 105, Boundaries.CENTRIC_127),
    ENVELOPE1_AFTER_TOUCH(null, null, 106, Boundaries.CENTRIC_127),

    ENVELOPE2_ATTACK(null, 114, null),
    ENVELOPE2_DECAY(null, 115, null),
    ENVELOPE2_SUSTAIN(null, 116, null),
    ENVELOPE2_RELEASE(null, 117, null),
    ENVELOPE2_TRIGGER(Convertors.Packed.PACKED_001, Boundaries.ENV_TRIGGERS),
    ENVELOPE2_VELOCITY_DEPTH(null, 118, null, Boundaries.CENTRIC_127),
    // env2/3 specific
    ENVELOPE2_DELAY(null, 113, null),

    ENVELOPE3_ATTACK(null, 75, null),
    ENVELOPE3_DECAY(null, 76, null),
    ENVELOPE3_SUSTAIN(null, 77, null),
    ENVELOPE3_RELEASE(null, 78, null),
    ENVELOPE3_TRIGGER(Convertors.Packed.PACKED_001, Boundaries.ENV_TRIGGERS),
    ENVELOPE3_VELOCITY_DEPTH(null, 79, null, Boundaries.CENTRIC_127),
    // env2/3 specific
    ENVELOPE3_DELAY(null, 74, null),

    ////
    // LFOs
    // - 2 identical LFOs are available
    ////
    LFO1_WAVEFORM(Convertors.Packed.PACKED_001, Boundaries.LFO_WAVE_FORMS),
    LFO1_SPEED_NON_SYNC(null, 16, null),
    LFO1_SPEED_SYNC(Convertors.Packed.PACKED_110, Boundaries.SYNC_RATES),
    LFO1_DELAY(null, 17, null),
    LFO1_TRIGGER(Convertors.Packed.PACKED_001, Boundaries.LFO_TRIGGER),
    LFO1_DELAY_TRIGGER(Convertors.Packed.PACKED_000, Boundaries.LFO_DELAY_TRIGGERS),
    LFO1_RANGE(Convertors.Packed.PACKED_001, Boundaries.LFO_RANGE),
    LFO1_OFFSET(null, 82, null),
    LFO1_ENV3_SPEED_AMOUNT(null, 80, null, Boundaries.CENTRIC_127),
    LFO1_MODWHEEL_SPEED_AMOUNT(null, null, 103, Boundaries.CENTRIC_127),
    LFO1_AFTERTOUCH_SPEED_AMOUNT(null, null, 85, Boundaries.CENTRIC_127),

    LFO2_WAVEFORM(Convertors.Packed.PACKED_001, Boundaries.LFO_WAVE_FORMS),
    LFO2_SPEED_NON_SYNC(null, 18, null),
    LFO2_SPEED_SYNC(Convertors.Packed.PACKED_110, Boundaries.SYNC_RATES),
    LFO2_DELAY(null, 19, null),
    LFO2_TRIGGER(Convertors.Packed.PACKED_001, Boundaries.LFO_TRIGGER),
    LFO2_DELAY_TRIGGER(Convertors.Packed.PACKED_000, Boundaries.LFO_DELAY_TRIGGERS),
    LFO2_RANGE(Convertors.Packed.PACKED_001, Boundaries.LFO_RANGE),
    LFO2_OFFSET(null, 83, null),
    LFO2_ENV3_SPEED_AMOUNT(null, 81, null, Boundaries.CENTRIC_127),
    LFO2_MODWHEEL_SPEED_AMOUNT(null, null, 104, Boundaries.CENTRIC_127),
    LFO2_AFTERTOUCH_SPEED_AMOUNT(null, null, 86, Boundaries.CENTRIC_127),
    ////
    // Filter
    ////
    FILTER_TYPE(Convertors.Packed.PACKED_001, Boundaries.FILTER_TYPES),
    FILTER_SLOPE(Convertors.Packed.PACKED_001, Boundaries.FILTER_SLOPES),
    FILTER_FREQUENCY(null, 105, null),
    FILTER_RESONANCE(null, 106, null),
    FILTER_OVERDRIVE(null, 104, null),
    FILTER_KEY_TRACK(null, 34, null),
    FILTER_Q_NORMALIZE(null, null, 127),
    FILTER_SPECIAL_WIDTH(Convertors.Packed.PACKED_100, Boundaries.FILTER_SPECIAL_WIDTH),
    FILTER_MODWHEEL_LFO2_FREQUENCY_AMOUNT(null, null, 89, Boundaries.CENTRIC_127),
    FILTER_MODWHEEL_LFO2_RESONANCE_AMOUNT(null, null, 90, Boundaries.CENTRIC_127),
    FILTER_AFTERTOUCH_LFO2_FREQUENCY_AMOUNT(null, null, 91, Boundaries.CENTRIC_127),
    FILTER_AFTERTOUCH_LFO2_RESONANCE_AMOUNT(null, null, 92, Boundaries.CENTRIC_127),
    FILTER_AFTERTOUCH_FREQUENCY_AMOUNT(null, null, 93, Boundaries.CENTRIC_127),
    FILTER_AFTERTOUCH_RESONANCE_AMOUNT(null, null, 94, Boundaries.CENTRIC_127),
    //// filter-mod matrix
    FILTER_FREQUENCY_LFO1_DEPTH(null, 60, null, Boundaries.CENTRIC_127),
    FILTER_FREQUENCY_LFO2_DEPTH(null, 35, null, Boundaries.CENTRIC_127),
    FILTER_FREQUENCY_ENV2_DEPTH(null, 107, null, Boundaries.CENTRIC_127),
    FILTER_FREQUENCY_ENV3_DEPTH(null, 59, null, Boundaries.CENTRIC_127),
    FILTER_FREQUENCY_WHEEL_DEPTH(null, null, 87, Boundaries.CENTRIC_127),
    FILTER_RESONANCE_LFO1_DEPTH(null, 73, null, Boundaries.CENTRIC_127),
    FILTER_RESONANCE_LFO2_DEPTH(null, 86, null, Boundaries.CENTRIC_127),
    FILTER_RESONANCE_ENV2_DEPTH(null, 72, null, Boundaries.CENTRIC_127),
    FILTER_RESONANCE_ENV3_DEPTH(null, 85, null, Boundaries.CENTRIC_127),
    FILTER_RESONANCE_WHEEL_DEPTH(null, null, 88, Boundaries.CENTRIC_127),

    ////
    // ARP
    ////
    ARP_ON_OFF(Convertors.Packed.PACKED_001, Boundaries.BOOLEAN),
    // special case: 0|127 values used as boolean (for checkbox) :
    // hence hack: use different boundary for mappings vs boundaries
    ARP_LATCH(new Convertors.ARP_LATCH("arp_latch", null, 65, null, Boundaries.NONE), Boundaries.BOOLEAN),
    ARP_KEY_SYNC(Convertors.Packed.PACKED_001, Boundaries.BOOLEAN),
    ARP_CONSTANT_PITCH(Convertors.Packed.PACKED_001, Boundaries.BOOLEAN),
    ARP_REALTIME_TRANSPOSE(Convertors.Packed.PACKED_001, Boundaries.BOOLEAN),
    ARP_MUTE(Convertors.Packed.PACKED_001, Boundaries.BOOLEAN),
    ARP_PATTERN(null, 3, null),
    ARP_SPEED(null, 9, null, Boundaries.ARP_NON_SYNC_RATES),
    ARP_SPEED_SYNC(Convertors.Packed.PACKED_110, Boundaries.ARP_SYNC_RATES),
    ARP_PATTERN_BANK(Convertors.Packed.PACKED_001, Boundaries.ARP_PATTERN_BANK),
    ARP_OCTAVES(Convertors.Packed.PACKED_109, Boundaries.ARP_OCTAVES),
    ARP_GATE_TIME(Convertors.Packed.PACKED_109, Boundaries.ARP_GATE_TIMES),
    ARP_LATCH_TYPE(Convertors.Packed.PACKED_001, Boundaries.ARP_LATCH_TYPE),
    ARP_QUANTIZE(Convertors.Packed.PACKED_001, Boundaries.ARP_QUANTIZE),
    ARP_VELOCITY(Convertors.Packed.PACKED_001, Boundaries.ARP_VELOCITY),
    ARP_EDIT_GATE(Convertors.Packed.PACKED_001, Boundaries.ARP_EDIT_GATE),
    ARP_OUTPUT(Convertors.Packed.PACKED_001, Boundaries.ARP_OUTPUT),
    ARP_OUTPUT_RANGING(Convertors.Packed.PACKED_113, Boundaries.BOOLEAN),
    ARP_FILL_IN(Convertors.Packed.PACKED_113, Boundaries.ARP_FILL_IN),
    ////
    // Effects - global
    ////
    EFFECTS_CONFIG(Convertors.Packed.PACKED_113, Boundaries.EFFECTS_CONFIG),
    EFFECTS_CONFIG_MORPH(null, 8, null, Boundaries.NONE),
    EFFECTS_INPUT(Convertors.Packed.PACKED_113, Boundaries.EFFECTS_INPUT),
    // effects drylevel (found in PAN menu.. ?)
    EFFECTS_DRY_LEVEL(null, null, 97, Boundaries.NONE),
    ////
    // Effects - equalizer
    ////
    EQUALIZER_COMB_FREQUENCY(null, null, 95),
    EQUALIZER_COMB_BOOST(null, null, 96, Boundaries.CENTRIC_127),
    EQUALIZER_TREBLE(null, null, 107, Boundaries.CENTRIC_127),
    EQUALIZER_BASS(null, null, 108, Boundaries.CENTRIC_127),
    ////
    // Effects - delay
    ////
    DELAY_SEND_LEVEL(null, 92, null),
    DELAY_SEND_MODWHEEL(null, 119, null, Boundaries.CENTRIC_127),
    DELAY_TIME_NON_SYNC(null, 101, null),
    DELAY_TIME_SYNC(Convertors.Packed.PACKED_100, Boundaries.DELAY_SYNC_RATES),
    DELAY_FEEDBACK(null, 102, null),
    DELAY_HF_DAMP(null, 103, null),
    DELAY_STEREO_WIDTH(null, null, 101),
    // TODO - also partly in PACKED_100 ?!?! guess this won't just work out of the box
    DELAY_RATIO(Convertors.Packed.PACKED_109, Boundaries.DELAY_RATIO),
    ////
    // Effects - chorus/flanger
    ////
    CHORUS_SEND_LEVEL(null, 93, null),
//    CHORUS_TYPE(Convertors.Packed.PACKED8, Boundaries.CHORUS_TYPES),
    CHORUS_MODWHEEL_DEPTH(null, 94, null, Boundaries.CENTRIC_127),
//    CHORUS_RATE_SYNC(113, null, null, Boundaries.SYNC_RATES),
    CHORUS_RATE_NON_SYNC(null, 87, null),
    CHORUS_MOD_DEPTH(null, 88, null, Boundaries.CENTRIC_127),
    CHORUS_FEEDBACK(null, 89, null),
    ////
    // Effects - reverb
    ////
    REVERB_SEND_LEVEL(null, 91, null),
//    REVERB_TYPE(Convertors.Packed.PACKED8, Boundaries.REVERB_TYPES),
    REVERB_SEND_MODWHEEL(null, 84, null, Boundaries.CENTRIC_127),
    REVERB_DECAY(null, 95, null),
    // TODO - CHECK !spec'ed as CC 96 !?
//    REVERB_HF_DAMP(null, null, 96),
    ////
    // Effects - distortion
    ////
    DISTORTION_LEVEL(null, 90, null),
    DISTORTION_MODWHEEL(null, 58, null, Boundaries.CENTRIC_127),
    ////
    // Effects - vocoder
    ////
    VOCODER_BALANCE(null, null, 102),
//    VOCODER_STEREO_WIDTH(90, null, null),
//    VOCODER_SIBILANCE_TYPE(Convertors.Packed.PACKED10, Boundaries.SIBILANCE_TYPES),
//    VOCODER_SIBILANCE_LEVEL(91, null, null),
    ////
    // Effects - panning
    ////
    PANNING_POSITION(null, 10, null, Boundaries.CENTRIC_127),
    PANNING_EFFECTS(Convertors.Packed.PACKED_100, Boundaries.BOOLEAN),
    PANNING_DEPTH(null, null, 99),
    PANNING_RATE_NON_SYNC(null, null, 98),
//    PANNING_RATE_SYNC(119, null, null, Boundaries.SYNC_RATES),
//    PANNING_GLOBAL_SYNC(Convertors.Packed.PACKED9, Boundaries.PANNING_GLOBAL_SYNC),
    ////
    // various
    ////
//    EXT_AUDIO_TRIGGER(Convertors.Packed.PACKED10, Boundaries.BOOLEAN),
//    EXT_AUDIO_TO_FX(Convertors.Packed.PACKED10, Boundaries.BOOLEAN),
//
    ////
    // known, yet non-used: listed here for completeness & to avoid noisy logs
    ////
    BANK_SELECT(null, 32, null);

    ////
    // FYI - CC voids/ignores
    ////
    // CC0 -> (Bank) Ignored / not transmitted
    // CC1 -> modwheel (receive only)
    // CC2 -> breath control (receive only)
    // CC6 -> used for NRPN value
    // CC32 -> bank select LSB
    // CC98 NRPN LSB
    // CC99 NRPN MSB
    // CC100 RPN LSB -> Not received in practice (RPN MSB/LSB) -> mail sent
    // CC101 RPN MSB -> Not received in practice (RPN MSB/LSB) -> mail sent
    // [CC120,XX127] -> To be checked

    // key as used in the Edisyn Synth Model, deduced from enum name, made compliant with Edisyn naming conventions
    private final String key;
    // Convertor
    private final Convertor convertor;
    // Restrictions: defining boundaries on the data in terms of min or max value, or listing possible values (and string representation)
    private final Boundaries boundaries;

    Mappings(Integer byteIndex, Integer cc, Integer nrpn) {
        this(byteIndex, cc, nrpn, Boundaries.NONE);
        }

    Mappings(Integer byteIndex, Integer cc, Integer nrpn, Boundaries boundaries) {
        this.key = extractKey(this);
        this.convertor = new Convertor.Straight(key, byteIndex, cc, nrpn, boundaries);
        this.boundaries = Objects.requireNonNull(boundaries);
        }

    Mappings(Convertor convertor, Boundaries boundaries) {
        this.key = extractKey(this);
        this.convertor = Objects.requireNonNull(convertor);
        this.boundaries = Objects.requireNonNull(boundaries);
        }

    /**
     * Find a mapping object by name & index
     *
     * @param name, referring to name of enum, optionally containing a placeholder for an integer when we have multiple
     *              Mapping objects only differentiated by some sort of index. In that case, this name parameter should
     *              contain an integer placeholder (ref: the format used in <code>String.format</code>)
     * @param index used as argument in the (formatted string) name parameter
     * @return Mapping object
     * @throws IllegalArgumentException when none is found
     * @see java.lang.String#format(String, Object...)
     */
    static Mappings find(String name, int index) {
        return Mappings.valueOf(String.format(name, index));
        }

    /**
     * get (Edisyn) model key
     *
     * @return key, non null
     */
    public String getKey() {
        return key;
        }

    /**
     * get convertor object (Edisyn data VS MIDI data)
     *
     * @return convertor, non null
     */
    public Convertor getConvertor() {
        return convertor;
        }

    /**
     * get restrictions (boundaries imposed on the data)
     *
     * @return restrictions, non null
     */
    public Boundaries getBoundaries() {
        return boundaries;
        }

    // deduce (Edisym model) key from enum name, yet adding some conversion to adhere to the Edisyn naming conventions
    private String extractKey(Mappings mappings) {
        return mappings.name().replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
        }
    }
