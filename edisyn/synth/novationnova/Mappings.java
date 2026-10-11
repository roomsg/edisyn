package edisyn.synth.novationnova;

import java.util.Locale;
import java.util.Objects;

/**
 * Enumeration containing all available synth parameters and their so-called 'mappings'.
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
    // program/patch & oscillators - global (= applying to all oscillators)
    ////
    POLYPHONY_MODE(Convertors.Packed.PACKED84, Boundaries.POLYPHONY_MODE),
    OSC_MODE(Convertors.Packed.PACKED84, Boundaries.OSC_MODE),
    GLIDE_TYPE(Convertors.Packed.PACKED1, Boundaries.GLIDE_TYPES),
    PORTAMENTO_TYPE(null, null, null, Boundaries.PORTAMENTO_TYPE),
    PORTAMENTO_MODE(null, null, null, Boundaries.PORTAMENTO_MODE),
    PORTAMENTO_TIME(null, 5, null),
    UNISON_VOICES(Convertors.Packed.PACKED100, Boundaries.UNISON_VOICES),
    CONSTANT_GATE(Convertors.Packed.PACKED100, Boundaries.BOOLEAN),
    DCO_VCO_MODE(Convertors.Packed.PACKED100, Boundaries.DCO_VCO),
    PROGRAM_VOLUME(null, 7, null),
    ////
    // Oscillators - individual
    // 3 identical oscillators are available:
    OSC1_WAVEFORM(Convertors.Packed.PACKED0, Boundaries.OSC_WAVEFORMS),
    OSC1_OCTAVE(Convertors.Packed.PACKED0, Boundaries.OSC_OCTAVES),
    OSC1_SEMITONE(Convertors.Packed.PACKED0, Boundaries.OSC_SEMITONES),
    OSC1_FINE_TUNE(null, 11, null, Boundaries.CENTRIC_127),
    OSC1_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED84, Boundaries.BEND_WHEEL),
    OSC1_KEY_SYNC(null, null, 78),
    OSC1_SYNC_SKEW(null, null, 81, Boundaries.CENTRIC_127),
    OSC1_SYNC_FORMANT_WIDTH(null, null, 75),
    ////
    OSC1_MIX(null, 28, null),
    OSC1_PITCH(null, null, 17),
    OSC1_PULSE_WIDTH(null, 22, null, Boundaries.CENTRIC_127),
    OSC1_SYNC(null, 49, null),
    OSC1_SOFTEN(null, 13, null),
    //    OSC1_LFO1_DEPTH(10, 43, null, Boundaries.CENTRIC_127),
    //    OSC1_ENV2_DEPTH(11, 44, null, Boundaries.CENTRIC_127),
//    OSC1_LFO2_PULSE_WIDTH_MOD(13, 46, null, Boundaries.CENTRIC_127),
//    OSC1_ENV2_PULSE_WIDTH_MOD(14, 47, null, Boundaries.CENTRIC_127),
    OSC2_WAVEFORM(Convertors.Packed.PACKED0, Boundaries.OSC_WAVEFORMS),
    OSC2_OCTAVE(Convertors.Packed.PACKED0, Boundaries.OSC_OCTAVES),
    OSC2_SEMITONE(Convertors.Packed.PACKED0, Boundaries.OSC_SEMITONES),
    OSC2_FINE_TUNE(null, 23, null, Boundaries.CENTRIC_127),
    OSC2_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED84, Boundaries.BEND_WHEEL),
    OSC2_KEY_SYNC(null, null, 79),
    OSC2_SYNC_SKEW(null, null, 82, Boundaries.CENTRIC_127),
    OSC2_SYNC_FORMANT_WIDTH(null, null, 76),
    //
    OSC2_MIX(null, 29, null),
    OSC2_PITCH(null, null, 33),
    OSC2_PULSE_WIDTH(null, 27, null, Boundaries.CENTRIC_127),
    OSC2_SYNC(null, 50, null),
    OSC2_SOFTEN(null, 14, null),
    //    OSC2_LFO1_DEPTH(18, 51, null, Boundaries.CENTRIC_127),
    //    OSC2_ENV2_DEPTH(19, 52, null, Boundaries.CENTRIC_127),
//    OSC2_LFO2_PULSE_WIDTH_MOD(21, 54, null, Boundaries.CENTRIC_127),
//    OSC2_ENV2_PULSE_WIDTH_MOD(22, 55, null, Boundaries.CENTRIC_127),
//    OSC2_SYNCED_BY_1(Convertors.Packed.PACKED4, Boundaries.BOOLEAN),
    OSC3_WAVEFORM(Convertors.Packed.PACKED0, Boundaries.OSC_WAVEFORMS),
    OSC3_OCTAVE(Convertors.Packed.PACKED0, Boundaries.OSC_OCTAVES),
    OSC3_SEMITONE(Convertors.Packed.PACKED0, Boundaries.OSC_SEMITONES),
    OSC3_FINE_TUNE(null, 12, null, Boundaries.CENTRIC_127),
    OSC3_BENDWHEEL_AMOUNT(Convertors.Packed.PACKED84, Boundaries.BEND_WHEEL),
    OSC3_KEY_SYNC(null, null, 80),
    OSC3_SYNC_SKEW(null, null, 83, Boundaries.CENTRIC_127),
    OSC3_SYNC_FORMANT_WIDTH(null, null, 77),
    //
    OSC3_MIX(null, 33, null),
    OSC3_PITCH(null, null, 49),
    OSC3_PULSE_WIDTH(null, 42, null, Boundaries.CENTRIC_127),
    OSC3_SYNC(null, 51, null),
    OSC3_SOFTEN(null, 15, null),
//    OSC3_LFO1_DEPTH(26, 59, null, Boundaries.CENTRIC_127),
//    OSC3_ENV2_DEPTH(27, 60, null, Boundaries.CENTRIC_127),
//    OSC3_LFO2_PULSE_WIDTH_MOD(29, 62, null, Boundaries.CENTRIC_127),
//    OSC3_ENV2_PULSE_WIDTH_MOD(30, 63, null, Boundaries.CENTRIC_127),
    XMOD1_MIX(null, 31, null),
    XMOD1_MIX_LFO1_DEPTH(null, null, 52),
    XMOD1_MIX_LFO2_DEPTH(null, null, 53),
    XMOD1_MIX_ENV2_DEPTH(null, null, 50),
    XMOD1_MIX_ENV3_DEPTH(null, null, 51),
    XMOD1_MIX_WHEEL_DEPTH(null, null, 54),
    //
    XMOD2_MIX(null, 4, null),
    XMOD2_MIX_LFO1_DEPTH(null, null, 57),
    XMOD2_MIX_LFO2_DEPTH(null, null, 58),
    XMOD2_MIX_ENV2_DEPTH(null, null, 55),
    XMOD2_MIX_ENV3_DEPTH(null, null, 56),
    XMOD2_MIX_WHEEL_DEPTH(null, null, 59),
    //
    NOISE_MIX(null, 30, null),
    NOISE_MIX_LFO1_DEPTH(null, null, 66),
    NOISE_MIX_LFO2_DEPTH(null, null, 67),
    NOISE_MIX_ENV2_DEPTH(null, 39, null),
    NOISE_MIX_ENV3_DEPTH(null, null, 65),
    NOISE_MIX_WHEEL_DEPTH(null, null, 68),

    NOISE_SOFTEN(null, 24, null),
    NOISE_SOFTEN_LFO1_DEPTH(null, null, null),
    NOISE_SOFTEN_LFO2_DEPTH(null, null, null),
    NOISE_SOFTEN_ENV2_DEPTH(null, null, null),
    NOISE_SOFTEN_ENV3_DEPTH(null, null, null),
    NOISE_SOFTEN_WHEEL_DEPTH(null, null, null),
    ////
    // Envelopes
    // - envelope1: amplitude envelope (ADSR) + wheel + aftertouch
    // - envelope2: (ADSR) + delay
    // - envelope3: (ADSR) + delay
    ////
    ENVELOPE1_ATTACK(null, 108, null),
    ENVELOPE1_DECAY(null, 109, null),
    ENVELOPE1_SUSTAIN(null, 110, null),
    ENVELOPE1_RELEASE(null, 111, null),
    ENVELOPE1_VELOCITY_DEPTH(61, 112, null, Boundaries.CENTRIC_127),
    ENVELOPE1_WHEEL(null, null, 105, Boundaries.CENTRIC_127),
    ENVELOPE1_AFTER_TOUCH(null, null, 106, Boundaries.CENTRIC_127),
    ENVELOPE1_TRIGGER(Convertors.Packed.PACKED1, Boundaries.ENV_TRIGGERS),

    ENVELOPE2_DELAY(null, 113, null),
    ENVELOPE2_ATTACK(null, 114, null),
    ENVELOPE2_DECAY(null, 115, null),
    ENVELOPE2_SUSTAIN(null, 116, null),
    ENVELOPE2_RELEASE(null, 117, null),
    ENVELOPE2_VELOCITY_DEPTH(null, 118, null, Boundaries.CENTRIC_127),
    ENVELOPE2_TRIGGER(Convertors.Packed.PACKED1, Boundaries.ENV_TRIGGERS),

    ENVELOPE3_DELAY(null, 74, null),
    ENVELOPE3_ATTACK(null, 75, null),
    ENVELOPE3_DECAY(null, 76, null),
    ENVELOPE3_SUSTAIN(null, 77, null),
    ENVELOPE3_RELEASE(null, 78, null),
    ENVELOPE3_VELOCITY_DEPTH(null, 79, null, Boundaries.CENTRIC_127),
    ENVELOPE3_TRIGGER(Convertors.Packed.PACKED1, Boundaries.ENV_TRIGGERS),
    ////
    // LFOs
    // - 2 identical LFOs are available
    ////
    LFO1_WAVEFORM(Convertors.Packed.PACKED1, Boundaries.LFO_WAVE_FORMS),
    LFO1_SPEED_NON_SYNC(null, 16, null),
    LFO1_SPEED_SYNC(null, null, null, Boundaries.SYNC_RATES),
    LFO1_DELAY(null, 17, null),
    LFO1_TRIGGER(Convertors.Packed.PACKED1, Boundaries.LFO_TRIGGER),
    LFO1_RANGE(Convertors.Packed.PACKED1, Boundaries.LFO_RANGE),

    LFO2_WAVEFORM(Convertors.Packed.PACKED1, Boundaries.LFO_WAVE_FORMS),
    LFO2_SPEED_NON_SYNC(null, 18, null),
    LFO2_SPEED_SYNC(null, null, null, Boundaries.SYNC_RATES),
    LFO2_DELAY(null, 19, null),
    LFO2_TRIGGER(Convertors.Packed.PACKED1, Boundaries.LFO_TRIGGER),
    LFO2_RANGE(Convertors.Packed.PACKED1, Boundaries.LFO_RANGE),

    ////
    // Filter (low pass)
    ////
    FILTER_TYPE(Convertors.Packed.PACKED1, Boundaries.FILTER_TYPES),
    FILTER_SLOPE(Convertors.Packed.PACKED1, Boundaries.FILTER_SLOPES),
    FILTER_FREQ(null, 105, null),
    FILTER_RESONANCE(null, 106, null),
    FILTER_OVERDRIVE(null, 104, null),
    FILTER_KEY_TRACK(null, 34, null),
    FILTER_Q_NORMALIZE(null, null, 127),
    FILTER_SPECIAL_WIDTH(Convertors.Packed.PACKED100, Boundaries.FILTER_SPECIAL_WIDTH),
//    FILTER_ENV2_DEPTH(52, 107, null, Boundaries.CENTRIC_127),
//    FILTER_LFO2_DEPTH(51, 102, null, Boundaries.CENTRIC_127),
//    FILTER_MODWHEEL_FREQUENCY_DEPTH(48, null, 12, Boundaries.CENTRIC_127),
//    FILTER_AFTERTCH_FREQUENCY_DEPTH(49, null, 13, Boundaries.CENTRIC_127),
//    FILTER_BREATH_FREQUENCY_DEPTH(50, null, 14, Boundaries.CENTRIC_127),
//    FILTER_MODWHEEL_LFO2_FREQUENCY_DEPTH(53, null, 15, Boundaries.CENTRIC_127),
//    FILTER_AFTERTCH_LFO2_FREQUENCY_DEPTH(54, null, 16, Boundaries.CENTRIC_127),
//    FILTER_BREATH_LFO2_FREQUENCY_DEPTH(55, null, 17, Boundaries.CENTRIC_127),

//    ////
//    // ARP
//    ////
//    ARP_ON_OFF(Convertors.Packed.PACKED7, Boundaries.BOOLEAN),
//    ARP_OCTAVES(Convertors.Packed.PACKED7, Boundaries.ARP_OCTAVES),
//    ARP_KEY_SYNC(Convertors.Packed.PACKED7, Boundaries.BOOLEAN),
//    ARP_LATCH(Convertors.Packed.PACKED7, Boundaries.BOOLEAN),
//    ARP_NOTE_DESTINATION(Convertors.Packed.PACKED7, Boundaries.ARP_NOTE_DESTINATION),
//    ARP_RATE_NON_SYNC(84, 9, null, Boundaries.ARP_NON_SYNC_RATES),
//    ARP_RATE_SYNC(85, 87, null, Boundaries.ARP_SYNC_RATES),
//    ARP_GATE_TIME(86, 88, null),
//    ARP_PATTERN(87, 3, null, Boundaries.ARP_PATTERN),
//    // TODO not sure what this is, ARP latch by sustain pedal perhaps ?
//    // ARP_LATCH_ON_MOMENTARY(null, 64, null),
    ////
    // Effects - equalizer
    ////
    EFFECT_MORPH(null, 8, null);
//    EQUALIZER_LEVEL(92, null, null, Boundaries.EQUALIZER_LEVEL),
//    EQUALIZER_FREQUENCY(93, 34, null),
//    EQUALIZER_RATE_NON_SYNC(94, 35, null),
//    EQUALIZER_RATE_SYNC(95, 36, null, Boundaries.SYNC_RATES),
//    EQUALIZER_MOD_DEPTH(96, 37, null),
//    EQUALIZER_GLOBAL_SYNC(Convertors.Packed.PACKED9, Boundaries.EQUALIZER_GLOBAL_SYNC),
//    ////
//    // Effects - delay
//    ////
//    DELAY_SEND_LEVEL(100, 92, null),
//    DELAY_SEND_MODWHEEL(101, 18, null, Boundaries.CENTRIC_127),
//    DELAY_TIME_NON_SYNC(102, 19, null),
//    DELAY_TIME_SYNC(103, 20, null, Boundaries.DELAY_SYNC_RATES),
//    DELAY_FEEDBACK(104, 21, null),
//    DELAY_STEREO_WIDTH(null, null, null),
//    DELAY_RATIO(106, 23, null, Boundaries.DELAY_RATIO),
//    ////
//    // Effects - chorus/flanger
//    ////
//    CHORUS_SEND_LEVEL(110, 93, null),
//    CHORUS_TYPE(Convertors.Packed.PACKED8, Boundaries.CHORUS_TYPES),
//    CHORUS_SEND_MODWHEEL(111, 26, null, Boundaries.CENTRIC_127),
//    CHORUS_RATE_NON_SYNC(null, null, null),
//    CHORUS_RATE_SYNC(113, null, null, Boundaries.SYNC_RATES),
//    CHORUS_FEEDBACK(114, null, null, Boundaries.CENTRIC_127),
//    CHORUS_MOD_DEPTH(115, null, null),
//    CHORUS_MOD_CENTRE_POINT(116, null, null, Boundaries.CENTRIC_127),
//    CHORUS_GLOBAL_SYNC(Convertors.Packed.PACKED9, Boundaries.CHORUS_GLOBAL_SYNC),
//    ////
//    // Effects - reverb
//    ////
//    REVERB_SEND_LEVEL(107, 91, null),
//    REVERB_TYPE(Convertors.Packed.PACKED8, Boundaries.REVERB_TYPES),
//    REVERB_SEND_MODWHEEL(108, 24, null, Boundaries.CENTRIC_127),
//    REVERB_DECAY(109, 25, null),
//    ////
//    // Effects - distortion
//    ////
//    // NOTE - (undocumented) CC 90: realtime control for distortion level !
//    DISTORTION_LEVEL(97, 90, null),
//    DISTORTION_MODWHEEL(98, 16, null, Boundaries.CENTRIC_127),
//    DISTORTION_COMPENSATION(99, 17, null),
//    ////
//    // Effects - vocoder
//    ////
//    VOCODER_BALANCE(89, 95, null),
//    VOCODER_STEREO_WIDTH(90, null, null),
//    VOCODER_SIBILANCE_TYPE(Convertors.Packed.PACKED10, Boundaries.SIBILANCE_TYPES),
//    VOCODER_SIBILANCE_LEVEL(91, null, null),
//    ////
//    // Effects - panning
//    ////
//    PANNING_POSITION(117, 10, null, Boundaries.CENTRIC_127),
//    PANNING_MOD_DEPTH(120, 94, null),
//    PANNING_RATE_NON_SYNC(118, null, null),
//    PANNING_RATE_SYNC(119, null, null, Boundaries.SYNC_RATES),
//    PANNING_GLOBAL_SYNC(Convertors.Packed.PACKED9, Boundaries.PANNING_GLOBAL_SYNC),
//    ////
//    // various
//    ////
//    EXT_AUDIO_TRIGGER(Convertors.Packed.PACKED10, Boundaries.BOOLEAN),
//    EXT_AUDIO_TO_FX(Convertors.Packed.PACKED10, Boundaries.BOOLEAN),
//    // program volume - stored in patch
//    // NOTE: some ROM patches seem to contain invalid value (= 0)
//    PROGRAM_VOLUME(125, 119, null, Boundaries.CENTRIC_24),
//    // next 4 are not (yet) used, should/could become used in (future) devicePanel
////    OSC_SELECT(Convertors.Packed.PACKED11, Boundaries.OSC_SELECT),
//    MIXER_SELECT(Convertors.Packed.PACKED11, Boundaries.MIXER_SELECT),
//    PWM_SOURCE(Convertors.Packed.PACKED11, Boundaries.PWM_SOURCE),
//    LFO_SELECT(Convertors.Packed.PACKED11, Boundaries.LFO_SELECT),
//
//    ////
//    // known, yet non-used: listed here for completeness & to avoid noisy logs
//    ////
//    // CC7: Device volume (non-patch related; not stored within patch, only CC-evented)
//    DEVICE_VOLUME(null, 7, null),
//    BANK_SELECT(null, 32, null);

    ////
    // FYI - CC voids/ignores
    ////
    // CC0 -> (Bank) Ignored / not transmitted
    // CC1 -> modwheel (receive only)
    // CC2 -> breath control (receive only)
    // CC4 -> Not Applicable
    // CC6 -> used for NRPN value
    // CC11 -> Not Applicable
    // CC32 -> bank select
    // CC38, 39 -> Not Applicable
    // CC66 -> Not Applicable
    // CC86 -> Not Applicable
    // CC90 -> Not Applicable according to doc, however used for distortion level in practice
    // CC96, 97 -> Not Applicable
    // CC98 NRPN number
    // CC99, CC100, CC101-> Not Applicable
    // CC113 -> Not Applicable

    // NOTE: looks like (non-documented) Packed11convertor is coming in via NRPN 26

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
        this.convertor = Convertor.createStraight(key, byteIndex, cc, nrpn, boundaries);
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
