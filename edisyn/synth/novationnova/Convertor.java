package edisyn.synth.novationnova;

import edisyn.Model;

import java.util.OptionalInt;

/**
 * interface defining conversion of data between
 * <ul>
 *     <li>(edisyn) model</li>
 *     <li>(MIDI) CC</li>
 *     <li>(MIDI) NRPN</li>
 *     <li>(MIDI-SysEx) byteIndex</li>
 * </ul>
 */
interface Convertor {
    // update model for a given value (coming from CC, NRPN or byteIndex)
    void toModel(Model model, int value);

    // extract value from model (to be used in CC, NRPN or byteIndex).
    int toSynth(Model model, String key);

    // get (MIDI-SysEx) byteIndex
    OptionalInt getByteIndex();

    // get (MIDI) CC number
    OptionalInt getCC();

    // get (MIDI) NRPN number
    OptionalInt getNRPN();

    // get restrictions
    default Boundary getBoundary() {
        return Boundaries.NONE;
        }

    /**
     * Most straightforward implementation of a <code>Convertor</code>, based on a simple one-to-one conversion
     */
    class Straight implements Convertor {
        private final String key;
        private final OptionalInt byteIndex;
        private final OptionalInt cc;
        private final OptionalInt nrpn;
        private final Boundary boundary;

        Straight(String key, Integer byteIndex, Integer cc, Integer nrpn, Boundary boundary) {
            this.key = key;
            this.byteIndex = byteIndex == null ? OptionalInt.empty() : OptionalInt.of(byteIndex);
            this.cc = cc == null ? OptionalInt.empty() : OptionalInt.of(cc);
            this.nrpn = nrpn == null ? OptionalInt.empty() : OptionalInt.of(nrpn);
            this.boundary = boundary;
        }

        @Override
        public void toModel(Model model, int value) {
            model.set(key, value);
        }

        @Override
        public int toSynth(Model model, String key) {
            return model.get(key);
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

        @Override
        public Boundary getBoundary() {
            return boundary;
        }

        @Override
        public String toString() {
            return "Convertor.Straight{key='" + key + "'}";
        }
    }
}
