package music.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sound.Pitch;

/**
 * Process the Key Signature of an ABC song, and translate it into an
 * appropriate Map of Accidentals for us in the Visitors.
 * 
 */
public class KeySignature {

    private final List<Character> notes;
    private final int shift;
    private final String unparsedKey;

    private enum AccidentalType {
        SHARP, FLAT, NEUTRAL
    }

    public KeySignature() {
        notes = new ArrayList<Character>();
        shift = 0;
        unparsedKey = "";
    }

    /**
     * Construct a KeySignature from the given string. This will initialize the
     * list of notes that need to be made either sharp or flat
     * 
     * @param s
     *            String representing the key signature.
     */
    public KeySignature(String s) {
        if (s.length() < 1 || s.length() > 3) {
            throw new IllegalArgumentException(
                    "ERROR: Invalid key signature string");
        }
        unparsedKey = s;
        String lower = s.toLowerCase();
        String orderSharps = "FCGDAEB";
        String orderFlats = "BEADGCF";
        AccidentalType type;
        int number;

        if (lower.equals("c") || lower.equals("am")) {
            number = 0;
            type = AccidentalType.NEUTRAL;
        } else if (lower.equals("g") || lower.equals("em")) {
            number = 1;
            type = AccidentalType.SHARP;
        } else if (lower.equals("d") || lower.equals("bm")) {
            number = 2;
            type = AccidentalType.SHARP;
        } else if (lower.equals("a") || lower.equals("f#m")) {
            number = 3;
            type = AccidentalType.SHARP;
        } else if (lower.equals("e") || lower.equals("c#m")) {
            number = 4;
            type = AccidentalType.SHARP;
        } else if (lower.equals("b") || lower.equals("g#m")) {
            number = 5;
            type = AccidentalType.SHARP;
        } else if (lower.equals("f#") || lower.equals("d#m")) {
            number = 6;
            type = AccidentalType.SHARP;
        } else if (lower.equals("c#") || lower.equals("a#m")) {
            number = 7;
            type = AccidentalType.SHARP;
        } else if (lower.equals("f") || lower.equals("dm")) {
            number = 1;
            type = AccidentalType.FLAT;
        } else if (lower.equals("bb") || lower.equals("gm")) {
            number = 2;
            type = AccidentalType.FLAT;
        } else if (lower.equals("eb") || lower.equals("cm")) {
            number = 3;
            type = AccidentalType.FLAT;
        } else if (lower.equals("ab") || lower.equals("fm")) {
            number = 4;
            type = AccidentalType.FLAT;
        } else if (lower.equals("db") || lower.equals("bbm")) {
            number = 5;
            type = AccidentalType.FLAT;
        } else if (lower.equals("gb") || lower.equals("ebm")) {
            number = 6;
            type = AccidentalType.FLAT;
        } else if (lower.equals("cb") || lower.equals("abm")) {
            number = 7;
            type = AccidentalType.FLAT;
        } else {
            throw new IllegalArgumentException("ERROR: Invalid key given");
        }

        notes = new ArrayList<Character>();
        for (int i = 0; i < number; i++) {
            if (type == AccidentalType.SHARP) {
                notes.add(orderSharps.charAt(i));
            } else if (type == AccidentalType.FLAT) {
                notes.add(orderFlats.charAt(i));
            }
        }

        if (type == AccidentalType.SHARP) {
            shift = 1;
        } else if (type == AccidentalType.FLAT) {
            shift = -1;
        } else {
            shift = 0;
        }
    }

    public List<Character> getNotes() {
        List<Character> list = new ArrayList<Character>();
        list.addAll(notes);
        return list;
    }

    public int getShift() {
        return shift;
    }

    public Map<Pitch, Accidental> getDefaultAccidentals() {
        Map<Pitch, Accidental> defaultMap = new HashMap<Pitch, Accidental>();
        for (char c : notes) {
            Pitch p = new Pitch(c);
            defaultMap.put(p, new Accidental(p, shift));
        }
        return defaultMap;
    }

    public String toString() {
        return unparsedKey;
    }
}
