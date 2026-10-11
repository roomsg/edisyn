/**
 * Copyright 2024 by Sean Luke
 * Licensed under the Apache License version 2.0
 */

package edisyn.synth.novationnova;

import edisyn.Recognize;

public class NovationNovaRec extends Recognize {
    public static boolean recognize(byte[] data) {
        try {
            SysexMessage.parse(data);
            return true;
            } catch (Throwable t) {
            return false;
            }
        }
    }
