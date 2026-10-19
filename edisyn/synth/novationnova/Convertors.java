package edisyn.synth.novationnova;

import edisyn.Model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edisyn.synth.novationnova.Mappings.*;

/**
 * Registry of all available Convertors.
 * This registry is (statically) build from the <code>Mappings</code> enum
 * <p>
 * As a side effect of this, it additionally validates the <code>Mappings</code> to ensure there are no double
 * usages defined for each and every(edisyn) model key and (MIDI) CC, NRPN, (sysex) byte index
 *
 * @see Mappings
 * <p>
 * TODO room for code improvement/reorg here (refactor code, introducing base class, generalize implementation...)
 */
class Convertors {
    // get convertor for a given (Edisyn model) key, Optional.empty if none
    static Optional<Convertor> getByKey(String key) {
        return BY_KEY.containsKey(key) ? Optional.of(BY_KEY.get(key)) : Optional.empty();
        }

    // get convertor for a given (MIDI SysEx dataDump) byteIndex, Optional.empty if none
    static Optional<Convertor> getByIndex(int cc) {
        return BY_INDEX.containsKey(cc) ? Optional.of(BY_INDEX.get(cc)) : Optional.empty();
        }

    // get convertor for a given (MIDI) CC, Optional.empty if none
    static Optional<Convertor> getByCC(int cc) {
        return BY_CC.containsKey(cc) ? Optional.of(BY_CC.get(cc)) : Optional.empty();
        }

    // get convertor for a given (MIDI) NRPN, Optional.empty if none
    static Optional<Convertor> getByNRPN(int nrpn) {
        return BY_NRPN.containsKey(nrpn) ? Optional.of(BY_NRPN.get(nrpn)) : Optional.empty();
        }

    private static final Map<String, Convertor> BY_KEY = buildKeyMap();
    private static final Map<Integer, Convertor> BY_INDEX = buildIndexMap();
    private static final Map<Integer, Convertor> BY_CC = buildCCMap();
    private static final Map<Integer, Convertor> BY_NRPN = buildNRPNMap();

    private static Map<String, Convertor> buildKeyMap() {
        return Collections.unmodifiableMap(Stream.of(Mappings.values())
            .collect(Collectors.toMap(
                    Mappings::getKey,
                    Mappings::getConvertor,
                    (convertor1, convertor2) -> {
                        if (convertor1 != convertor2) {
                            throw new IllegalStateException("Cannot assign different convertors to the same key.");
                            }
                        return convertor1;
                        }
                    )));
        }

    private static Map<Integer, Convertor> buildIndexMap() {
        return Collections.unmodifiableMap(Stream.of(Mappings.values())
            .map(Mappings::getConvertor)
            .filter(convertor -> convertor.getByteIndex().isPresent())
            .collect(Collectors.toMap(
                    convertor -> convertor.getByteIndex().getAsInt(),
                    Function.identity(),
                    (convertor1, convertor2) -> {
                        if (convertor1 != convertor2) {
                            throw new IllegalStateException("Cannot assign different convertors to the same byteIndex: " + convertor1.getByteIndex());
                            }
                        return convertor1;
                        }
                    )));
        }

    private static Map<Integer, Convertor> buildCCMap() {
        return Collections.unmodifiableMap(Stream.of(Mappings.values())
            .map(Mappings::getConvertor)
            .filter(convertor -> convertor.getCC().isPresent())
            .collect(Collectors.toMap(
                    convertor -> convertor.getCC().getAsInt(),
                    Function.identity(),
                    (convertor1, convertor2) -> {
                        if (convertor1 != convertor2) {
                            throw new IllegalStateException("Cannot assign different convertors to the same CC: " + convertor1.getCC());
                            }
                        return convertor1;
                        }
                    )));
        }

    private static Map<Integer, Convertor> buildNRPNMap() {
        return Collections.unmodifiableMap(Stream.of(Mappings.values())
            .map(Mappings::getConvertor)
            .filter(convertor -> convertor.getNRPN().isPresent())
            .collect(Collectors.toMap(
                    convertor -> convertor.getNRPN().getAsInt(),
                    Function.identity(),
                    (convertor1, convertor2) -> {
                        if (convertor1 != convertor2) {
                            throw new IllegalStateException("Cannot assign different convertors to the same NRPN: " + convertor1.getNRPN());
                            }
                        return convertor1;
                        }
                    )));
        }

    /**
     * enum defining some predefined (so-called packed) convertors
     * These convertors are handling data where different parameters are combined (as in: 'packed') in a single value
     * The definition of these convertors are solely driven by the Nova specification
     */
    enum Packed implements Convertor {
        PACKED_000(null, null, 0) {
            @Override
                public void toModel(Model model, int value) {
                if (value >= 0 && value < 5)
                    model.set(OSC1_WAVEFORM.getKey(), value);
                // [5-9] unused
                else if (value >= 10 && value < 15)
                    model.set(OSC2_WAVEFORM.getKey(), value - 10);
                // [15-19] unused
                else if (value >= 20 && value < 25)
                    model.set(OSC3_WAVEFORM.getKey(), value - 20);
                // [25-30] unused
                else if (value >= 30 && value < 35)
                    model.set(OSC1_OCTAVE.getKey(), value - 30);
                else if (value >= 35 && value < 40)
                    model.set(OSC2_OCTAVE.getKey(), value - 35);
                else if (value >= 40 && value < 45)
                    model.set(OSC3_OCTAVE.getKey(), value - 40);
                else if (value >= 45 && value < 70)
                    model.set(OSC1_SEMITONE.getKey(), value - 45);
                else if (value >= 70 && value < 95)
                    model.set(OSC2_SEMITONE.getKey(), value - 70);
                else if (value >= 95 && value < 120)
                    model.set(OSC3_SEMITONE.getKey(), value - 95);
                else if (value >= 120 && value < 122)
                    model.set(PORTAMENTO_TYPE.getKey(), value - 120);
                else if (value >= 122 && value < 124)
                    model.set(PORTAMENTO_MODE.getKey(), value - 122);
                else if (value >= 124 && value < 126)
                    model.set(LFO1_DELAY_TRIGGER.getKey(), value - 124);
                else if (value >= 126 && value < 128)
                    model.set(LFO2_DELAY_TRIGGER.getKey(), value - 126);
                else
                    System.out.println("#000?" + value);
                }

            @Override
                public int toSynth(Model model, String key) {
                if (key.equals(OSC1_WAVEFORM.getKey())) {
                    return model.get(key);
                } else if (key.equals(OSC2_WAVEFORM.getKey())) {
                    return model.get(key) + 10;
                } else if (key.equals(OSC3_WAVEFORM.getKey())) {
                    return model.get(key) + 20;
                } else if (key.equals(OSC1_OCTAVE.getKey())) {
                    return model.get(key) + 30;
                } else if (key.equals(OSC2_OCTAVE.getKey())) {
                    return model.get(key) + 35;
                } else if (key.equals(OSC3_OCTAVE.getKey())) {
                    return model.get(key) + 40;
                } else if (key.equals(OSC1_SEMITONE.getKey())) {
                    return model.get(key) + 45;
                } else if (key.equals(OSC2_SEMITONE.getKey())) {
                    return model.get(key) + 70;
                } else if (key.equals(OSC3_SEMITONE.getKey())) {
                    return model.get(key) + 95;
                } else if (key.equals(PORTAMENTO_TYPE.getKey())) {
                    return model.get(key) + 120;
                } else if (key.equals(PORTAMENTO_MODE.getKey())) {
                    return model.get(key) + 122;
                } else if (key.equals(LFO1_DELAY_TRIGGER.getKey())) {
                    return model.get(key) + 124;
                } else if (key.equals(LFO2_DELAY_TRIGGER.getKey())) {
                    return model.get(key) + 126;
                } else
                    System.out.println("#000?" + key);
                return 0;
                }
            },
        PACKED_001(null, null, 1) {
            @Override
            public void toModel(Model model, int value) {
                if (value >= 0 && value < 3)
                    model.set(FILTER_SLOPE.getKey(), value);
                if (value >= 3 && value < 15)
                    model.set(FILTER_TYPE.getKey(), value - 3);
                // 15 unused
                if (value >= 16 && value < 20)
                    model.set(LFO1_WAVEFORM.getKey(), value - 16);
                else if (value >= 20 && value < 24)
                    model.set(LFO2_WAVEFORM.getKey(), value - 20);
                else if (value >= 24 && value < 26)
                    model.set(LFO1_TRIGGER.getKey(), value - 24);
                else if (value >= 26 && value < 28)
                    model.set(LFO2_TRIGGER.getKey(), value - 26);
                else if (value >= 28 && value < 31)
                    model.set(LFO1_RANGE.getKey(), value - 28);
                else if (value >= 31 && value < 34)
                    model.set(LFO2_RANGE.getKey(), value - 31);
                else if (value >= 34 && value < 42) {
                    int val = value - 34;
                    model.set(ENVELOPE3_TRIGGER.getKey(), 0x1 & (val));
                    model.set(ENVELOPE2_TRIGGER.getKey(), 0x1 & (val >>> 1));
                    model.set(ENVELOPE1_TRIGGER.getKey(), 0x1 & (val >>> 2));
                }
                else if (value >= 42 && value < 52)
                    model.set(GLIDE_TYPE.getKey(), value - 42);
                else if (value >= 52 && value < 55)
                    model.set(CHORUS_TYPE.getKey(), value - 52);
                else if (value >= 55 && value < 59)
                    model.set(PANNING_EFFECT_TYPE.getKey(), value - 55);
                    // TODO
                else if (value >= 70 && value < 72)
                    model.set(ARP_CONSTANT_PITCH.getKey(), value - 70);
                else if (value >= 72 && value < 74)
                    model.set(ARP_ON_OFF.getKey(), value - 72);
                else if (value >= 74 && value < 76)
                    model.set(ARP_KEY_SYNC.getKey(), value - 74);
                else if (value >= 76 && value < 79)
                    model.set(ARP_PATTERN_BANK.getKey(), value - 76);
                else if (value >= 79 && value < 81)
                    model.set(ARP_LATCH_TYPE.getKey(), value - 79);
                else if (value >= 81 && value < 86)
                    model.set(ARP_QUANTIZE.getKey(), value - 81);
                else if (value >= 86 && value < 90)
                    model.set(ARP_VELOCITY.getKey(), value - 86);
                else if (value >= 90 && value < 92)
                    model.set(ARP_MUTE.getKey(), value - 90);
                else if (value >= 92 && value < 94)
                    model.set(ARP_REALTIME_TRANSPOSE.getKey(), value - 92);
                else if (value >= 102 && value < 105)
                    model.set(ARP_OUTPUT.getKey(), value - 102);
                // undocumented
                else if (value >= 122 && value < 128)
                    model.set(ARP_FILL_IN.getKey(), value - 122);
                else
                    // TODO
                    System.out.println("#001?" + value);
            }

            @Override
            public int toSynth(Model model, String key) {
                if (key.equals(FILTER_SLOPE.getKey())) {
                    return model.get(key);
                } else if (key.equals(FILTER_TYPE.getKey())) {
                    return model.get(key) + 3;
                } else if (key.equals(LFO1_WAVEFORM.getKey())) {
                    return model.get(key) + 16;
                } else if (key.equals(LFO2_WAVEFORM.getKey())) {
                    return model.get(key) + 20;
                } else if (key.equals(LFO1_TRIGGER.getKey())) {
                    return model.get(key) + 24;
                } else if (key.equals(LFO2_TRIGGER.getKey())) {
                    return model.get(key) + 26;
                } else if (key.equals(LFO1_RANGE.getKey())) {
                    return model.get(key) + 28;
                } else if (key.equals(LFO2_RANGE.getKey())) {
                    return model.get(key) + 31;
                } else if (key.equals(ENVELOPE1_TRIGGER.getKey())
                        || key.equals(ENVELOPE2_TRIGGER.getKey())
                        || key.equals(ENVELOPE3_TRIGGER.getKey())
                ) {
                    int aggregatedValue =
                            (model.get(ENVELOPE1_TRIGGER.getKey()) << 2)
                            | (model.get(ENVELOPE2_TRIGGER.getKey()) << 1)
                            | (model.get(ENVELOPE3_TRIGGER.getKey()));
                    return aggregatedValue + 34;
                } else if (key.equals(GLIDE_TYPE.getKey())) {
                    return model.get(key) + 42;
                } else if (key.equals(CHORUS_TYPE.getKey())) {
                    return model.get(key) + 52;
                } else if (key.equals(PANNING_EFFECT_TYPE.getKey())) {
                    return model.get(key) + 55;
                } else if (key.equals(ARP_CONSTANT_PITCH.getKey())) {
                    return model.get(key) + 70;
                } else if (key.equals(ARP_ON_OFF.getKey())) {
                    return model.get(key) + 72;
                } else if (key.equals(ARP_KEY_SYNC.getKey())) {
                    return model.get(key) + 74;
                } else if (key.equals(ARP_PATTERN_BANK.getKey())) {
                    return model.get(key) + 76;
                } else if (key.equals(ARP_LATCH_TYPE.getKey())) {
                    return model.get(key) + 79;
                } else if (key.equals(ARP_QUANTIZE.getKey())) {
                    return model.get(key) + 81;
                } else if (key.equals(ARP_VELOCITY.getKey())) {
                    return model.get(key) + 86;
                } else if (key.equals(ARP_MUTE.getKey())) {
                    return model.get(key) + 90;
                } else if (key.equals(ARP_REALTIME_TRANSPOSE.getKey())) {
                    return model.get(key) + 92;
                } else if (key.equals(ARP_OUTPUT.getKey())) {
                    return model.get(key) + 102;
                } else if (key.equals(ARP_FILL_IN.getKey())) {
                    return model.get(key) + 122;
                } else
                    System.out.println("#001?" + key);
                return 0;
            }
        },
        PACKED_084(null, null, 84) {
            @Override
            public void toModel (Model model,int value){
                if (value >= 0 && value < 25)
                    model.set(OSC1_BENDWHEEL_AMOUNT.getKey(), value);
                else if (value >= 25 && value < 50)
                    model.set(OSC2_BENDWHEEL_AMOUNT.getKey(), value - 25);
                else if (value >= 50 && value < 75)
                    model.set(OSC3_BENDWHEEL_AMOUNT.getKey(), value - 50);
                // TODO
                else if (value >= 84 && value < 86)
                    model.set(OSC_MODE.getKey(), value - 84);
                else if (value >= 86 && value < 88)
                    model.set(POLYPHONY_MODE.getKey(), value - 86);
                else
                    // TODO
                    System.out.println("#084?" + value);
            }

            @Override
            public int toSynth (Model model, String key){
                if (key.equals(OSC1_BENDWHEEL_AMOUNT.getKey())) {
                    return model.get(key);
                } else if (key.equals(OSC2_BENDWHEEL_AMOUNT.getKey())) {
                    return model.get(key) + 25;
                } else if (key.equals(OSC3_BENDWHEEL_AMOUNT.getKey())) {
                    return model.get(key) + 50;
                } else if (key.equals(OSC_MODE.getKey())) {
                    return model.get(key) + 84;
                } else if (key.equals(POLYPHONY_MODE.getKey())) {
                    return model.get(key) + 86;
                }
                    System.out.println("#084?" + key);
                return 0;
            }
        },
        PACKED_100(null, null, 100) {
            @Override
            public void toModel (Model model,int value){
                if (value >= 0 && value < 35)
                    model.set(DELAY_TIME_SYNC.getKey(), value);
                // TODO
                else if (value >= 79 && value < 81)
                    model.set(CONSTANT_GATE.getKey(), value - 79);
                else if (value == 81) // unison off
                    model.set(UNISON_VOICES.getKey(), 0); // explicit off
                else if (value == 82) // unison on, voices see next
                    ; // NOOP
                // TODO - fix on/off thingy
                else if (value >= 83 && value < 90)
                    model.set(UNISON_VOICES.getKey(), value - 83 + 1); // +1 since synthetic "Off" value
                // TODO
                else if (value >= 92 && value < 94)
                    model.set(DCO_VCO_MODE.getKey(), value - 92); // +1 since synthetic "Off" value
                else if (value >= 100 && value < 102)
                    model.set(PANNING_EFFECT_ON_OFF.getKey(), value - 100);
                else if (value >= 102 && value < 110)
                    model.set(FILTER_SPECIAL_WIDTH.getKey(), value - 102);
                else if (value >= 110 && value < 112)
                    // Delay Ratio is spread over 2 packed NRPNS ?!?!
                    model.set(DELAY_RATIO.getKey(), value - 110 + 11);
                else
                    // TODO
                    System.out.println("#100?" + value);
            }

            @Override
            public int toSynth (Model model, String key){
                if (key.equals(DELAY_TIME_SYNC.getKey())) {
                    return model.get(key);
                } else if (key.equals(CONSTANT_GATE.getKey())) {
                    return model.get(key) + 79;
                    // TODO - verify/fix next (on/off/voices)
                } else if (key.equals(UNISON_VOICES.getKey())) {
                    return model.get(key) + 83;
                } else if (key.equals(DCO_VCO_MODE.getKey())) {
                    return model.get(key) + 92;
                } else if (key.equals(PANNING_EFFECT_ON_OFF.getKey())) {
                    return model.get(key) + 100;
                } else if (key.equals(FILTER_SPECIAL_WIDTH.getKey())) {
                    return model.get(key) + 102;
                } else if (key.equals(DELAY_RATIO.getKey())) {
                    // TODO - fix
                    // part2 of delay ratio, currently ignored
                    return model.get(key) -11 + 110;
                }
                System.out.println("#100?" + key);
                return 0;
            }
        },
        PACKED_109(null, null, 109) {
            @Override
            public void toModel (Model model, int value){
                // reserved
                if (value >= 1 && value < 5)
                    model.set(ARP_OCTAVES.getKey(), value - 1);
                else if (value >= 5 && value < 9)
                    model.set(ARP_GATE_TIME.getKey(), value - 5);
                // reserved
                else if (value >= 96 && value < 107)
                    model.set(DELAY_RATIO.getKey(), value - 96);
                // reserved
                else
                    // TODO
                    System.out.println("#109?" + value);
            }

            @Override
            public int toSynth (Model model, String key) {
                if (key.equals(ARP_OCTAVES.getKey())) {
                    return model.get(key) + 1;
                } else if (key.equals(ARP_GATE_TIME.getKey())) {
                    return model.get(key) + 5;
                } else if (key.equals(DELAY_RATIO.getKey())) {
                    return model.get(key) + 96;
                }
                System.out.println("#109?" + key);
                return 0;
            }
        },
        PACKED_110(null, null, 110) {
            @Override
            public void toModel (Model model, int value){
                if (value >= 0 && value < 34)
                    model.set(ARP_SPEED_SYNC.getKey(), value);
                // TODO
                else if (value >= 40 && value < 75)
                    model.set(LFO1_SPEED_SYNC.getKey(), value - 40);
                else if (value >= 78 && value < 80)
                    model.set(ARP_OUTPUT_RANGING.getKey(), value - 78);
                else if (value >= 80 && value < 115)
                    model.set(LFO2_SPEED_SYNC.getKey(), value - 80);
                else
                    // TODO
                    System.out.println("#110?" + value);
            }

            @Override
            public int toSynth (Model model, String key){
                if (key.equals(ARP_SPEED_SYNC.getKey())) {
                    return model.get(key);
                } else if (key.equals(LFO1_SPEED_SYNC.getKey())) {
                    return model.get(key) + 40;
                } else if (key.equals(ARP_OUTPUT_RANGING.getKey())) {
                    return model.get(key) + 78;
                } else if (key.equals(LFO2_SPEED_SYNC.getKey())) {
                    return model.get(key) + 80;
                }
                System.out.println("#110?" + key);
                return 0;
            }
        },
        PACKED_113(null, null, 113) {
            @Override
            public void toModel (Model model, int value){
                if (value >= 1 && value < 3)
                    model.set(POLYPHONY_CONFIG.getKey(), value - 1);
                else if (value >= 36 && value < 55)
                    model.set(EFFECTS_CONFIG.getKey(), value - 36);
                else if (value >= 71 && value < 76) {
                    model.set(EFFECTS_INPUT.getKey(), value - 71);
                }
                else
                    // TODO
                    System.out.println("#113?" + value);
            }

            @Override
            public int toSynth (Model model, String key){
                if (key.equals(POLYPHONY_CONFIG.getKey())) {
                    return model.get(key) + 1;
                } else if (key.equals(EFFECTS_CONFIG.getKey())) {
                    return model.get(key) + 36;
                } else if (key.equals(EFFECTS_INPUT.getKey())) {
                    return model.get(key) + 71;
                }
                // TODO
                System.out.println("#113?" + key);
                return 0;
            }
        },
        PACKED_CC_100(null, 100, null) {
            @Override
            public void toModel (Model model, int value){
                // bits 0-2: early reflection level
                int erLevel = value & 7;
                model.set(REVERB_EARLY_REFLECTION.getKey(), erLevel);
                // bits 3-6: reverb type
                int reverbType = value >>> 3;
                model.set(REVERB_TYPE.getKey(), reverbType);
            }

            @Override
            public int toSynth (Model model, String key){
                int erLevel = model.get(REVERB_EARLY_REFLECTION.getKey());
                int reverbType = model.get(REVERB_TYPE.getKey());
                return (erLevel) | (reverbType << 3);
            }
        };

        private final OptionalInt byteIndex;
        private final OptionalInt cc;
        private final OptionalInt nrpn;

        Packed(Integer byteIndex, Integer cc, Integer nrpn) {
            this.byteIndex = toOptional(byteIndex);
            this.cc = toOptional(cc);
            this.nrpn = toOptional(nrpn);
            }

        @Override
        public OptionalInt getByteIndex() {
            return byteIndex;
            }

        @Override
        public OptionalInt getCC() {
            return cc;
            }

        @Override
        public OptionalInt getNRPN() {
            return nrpn;
            }

        private OptionalInt toOptional(Integer i) {
            return i == null ? OptionalInt.empty() : OptionalInt.of(i);
            }
        }

        // one special case: boolean modeled as 0|127
        static class ARP_LATCH extends Convertor.Straight {

            ARP_LATCH(String key, Integer byteIndex, Integer cc, Integer nrpn, Boundary boundary) {
                super(key, byteIndex, cc, nrpn, boundary);
            }

            @Override
            public void toModel(Model model, int value) {
                model.set(ARP_LATCH.getKey(), value / 127);
            }

            @Override
            public int toSynth(Model model, String key) {
                int modelValue = model.get(ARP_LATCH.getKey());
                return modelValue == -1 ? 0 : modelValue * 127;
            }
        }
}
