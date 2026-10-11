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
            PACKED0(null, null, 0) {
            @Override
                public void toModel(Model model, int value) {
                if (value >= 0 && value < 5)
                    model.set(OSC1_WAVEFORM.getKey(), value);
                else if (value >= 10 && value < 15)
                    model.set(OSC2_WAVEFORM.getKey(), value - 10);
                else if (value >= 20 && value < 25)
                    model.set(OSC3_WAVEFORM.getKey(), value - 20);
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
                else
                    System.out.println("#00?" + value);
                }

            @Override
                public int toSynth(Model model) {
                // TODO - Oh Dear, FW work I guess...
                return 0;
                }
            },
        PACKED1(null, null, 1) {
            @Override
            public void toModel(Model model, int value) {
                if (value >= 0 && value < 3)
                    model.set(FILTER_SLOPE.getKey(), value);
                if (value >= 3 && value < 15)
                    model.set(FILTER_TYPE.getKey(), value - 3);
                if (value >= 16 && value < 20)
                    model.set(LFO1_WAVEFORM.getKey(), value - 16);
                else if (value >= 20 && value < 24)
                    model.set(LFO2_WAVEFORM.getKey(), value - 20);
                else if (value >= 24 && value < 26)
                    model.set(LFO1_TRIGGER.getKey(), value - 24);
                else if (value >= 26 && value < 28)
                    model.set(LFO2_TRIGGER.getKey(), value -26);
                else if (value >= 28 && value < 31)
                    model.set(LFO1_RANGE.getKey(), value -28);
                else if (value >= 31 && value < 34)
                    model.set(LFO2_RANGE.getKey(), value -31);
                else if (value >= 34 && value < 42) {
                    int val = value - 34;
                    byte b = (byte) ((val & 0x7) ^ 0x7);
                    model.set(ENVELOPE3_TRIGGER.getKey(), 0x1 & (b >>> 0));
                    model.set(ENVELOPE2_TRIGGER.getKey(), 0x1 & (b >>> 1));
                    model.set(ENVELOPE1_TRIGGER.getKey(), 0x1 & (b >>> 2));
                }
                else if (value >= 42 && value < 52)
                    model.set(GLIDE_TYPE.getKey(), value -42);
                else
                    // TODO
                    System.out.println("#01?" + value);
            }

            @Override
            public int toSynth(Model model) {
                // TODO - Oh Dear, FW work I guess...
                return 0;
                }
        },
        PACKED84(null, null, 84) {
            @Override
            public void toModel (Model model,int value){
                if (value >= 0 && value < 25)
                    model.set(OSC1_BENDWHEEL_AMOUNT.getKey(), value);
                else if (value >= 25 && value < 50)
                    model.set(OSC2_BENDWHEEL_AMOUNT.getKey(), value - 25);
                else if (value >= 50 && value < 75)
                    model.set(OSC3_BENDWHEEL_AMOUNT.getKey(), value - 50);
                else if (value >= 84 && value < 86)
                    model.set(OSC_MODE.getKey(), value - 84);
                else if (value >= 86 && value < 88)
                    model.set(POLYPHONY_MODE.getKey(), value - 86);
                else
                    // TODO
                    System.out.println("#84?" + value);
            }

            @Override
            public int toSynth (Model model){
                // TODO - Oh Dear, FW work I guess...
                return 0;
            }
        },
        PACKED100(null, null, 100) {
            @Override
            public void toModel (Model model,int value){
                if (value >= 79 && value < 81)
                    model.set(CONSTANT_GATE.getKey(), value - 79);
                else if (value == 81) // unison off
                    model.set(UNISON_VOICES.getKey(), 0); // explicit off
                else if (value == 82) // unison on, voices see next
                    ; // NOOP
                // TODO - fix on/off thingy
                else if (value >= 83 && value < 90)
                    model.set(UNISON_VOICES.getKey(), value - 83 + 1); // +1 since synthetic "Off" value
                else if (value >= 92 && value < 94)
                    model.set(DCO_VCO_MODE.getKey(), value - 92); // +1 since synthetic "Off" value
                else if (value >= 102 && value < 110)
                    model.set(FILTER_SPECIAL_WIDTH.getKey(), value - 102);
                else
                    // TODO
                    System.out.println("#100?" + value);
            }

            @Override
            public int toSynth (Model model){
                // TODO - Oh Dear, FW work I guess...
                return 0;
            }
        },
        PACKED109(null, null, 109) {
            @Override
            public void toModel (Model model,int value){
                // TODO
                System.out.println("#109?" + value);
            }

            @Override
            public int toSynth (Model model){
                // TODO - Oh Dear, FW work I guess...
                return 0;
            }
        },
        PACKED110(null, null, 110) {
            @Override
            public void toModel (Model model,int value){
                // TODO
                System.out.println("#110?" + value);
            }

            @Override
            public int toSynth (Model model){
                // TODO - Oh Dear, FW work I guess...
                return 0;
            }
        },
        PACKED113(null, null, 113) {
            @Override
            public void toModel (Model model,int value){
                // TODO
                System.out.println("#113?" + value);
            }

            @Override
            public int toSynth (Model model){
                // TODO - Oh Dear, FW work I guess...
                return 0;
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
    }
