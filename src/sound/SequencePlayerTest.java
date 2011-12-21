package sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

public class SequencePlayerTest {

    private int addNote(SequencePlayer sp, Pitch p, int start, int length) {
        sp.addNote(p.toMidiNote(), start, length);
        return start + length;
    }

    @Test
    public void pieceOneTest() throws MidiUnavailableException,
            InvalidMidiDataException {
        final int QUARTER_NOTE = 12;
        SequencePlayer sp = new SequencePlayer(140, QUARTER_NOTE);
        int tick = 0;
        tick = addNote(sp, new Pitch('C'), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('C'), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('C'), tick, QUARTER_NOTE * 3 / 4);
        tick = addNote(sp, new Pitch('D'), tick, QUARTER_NOTE / 4);
        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE);

        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE * 3 / 4);
        tick = addNote(sp, new Pitch('D'), tick, QUARTER_NOTE / 4);
        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE * 3 / 4);
        tick = addNote(sp, new Pitch('F'), tick, QUARTER_NOTE / 4);
        tick = addNote(sp, new Pitch('G'), tick, QUARTER_NOTE * 2);

        Pitch tuplet = new Pitch('C').octaveTranspose(1);
        int tupletLength = QUARTER_NOTE / 3;
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tuplet = new Pitch('G');
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tuplet = new Pitch('E');
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tuplet = new Pitch('C');
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);
        tick = addNote(sp, tuplet, tick, tupletLength);

        tick = addNote(sp, new Pitch('G'), tick, QUARTER_NOTE * 3 / 4);
        tick = addNote(sp, new Pitch('F'), tick, QUARTER_NOTE / 4);
        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE * 3 / 4);
        tick = addNote(sp, new Pitch('D'), tick, QUARTER_NOTE / 4);
        tick = addNote(sp, new Pitch('C'), tick, QUARTER_NOTE * 2);

        // C C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 |
        // (3c1/2c1/2c1/2 (3G1/2G1/2G1/2 (3E1/2E1/2E1/2 (3C1/2C1/2C1/2 |
        // G3/4 F/4 E3/4 D/4 C2|]

        sp.play();
    }

    @Test
    public void pieceTwoTest() throws MidiUnavailableException, InvalidMidiDataException {
        final int QUARTER_NOTE = 6;
        SequencePlayer sp = new SequencePlayer(200, QUARTER_NOTE);
        int tick = 0;

        addNote(sp, new Pitch('F').accidentalTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        addNote(sp, new Pitch('F').accidentalTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        tick += QUARTER_NOTE / 2;

        addNote(sp, new Pitch('F').accidentalTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        tick += QUARTER_NOTE / 2;

        addNote(sp, new Pitch('F').accidentalTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('C').octaveTranspose(1), tick, QUARTER_NOTE / 2);

        addNote(sp, new Pitch('F').accidentalTranspose(1), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, QUARTER_NOTE);
        
        addNote(sp, new Pitch('G'), tick, QUARTER_NOTE);
        addNote(sp, new Pitch('B'), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('G').octaveTranspose(1), tick, QUARTER_NOTE);
        tick += QUARTER_NOTE;
        tick = addNote(sp, new Pitch('G'), tick, QUARTER_NOTE);
        tick += QUARTER_NOTE;
        
        tick = addNote(sp, new Pitch('C').octaveTranspose(1), tick, QUARTER_NOTE * 3 / 2);
        tick = addNote(sp, new Pitch('G'), tick, QUARTER_NOTE / 2);
        tick += QUARTER_NOTE;
        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE);

        tick = addNote(sp, new Pitch('E'), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('A'), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('B'), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('B').accidentalTranspose(-1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('A'), tick, QUARTER_NOTE);

        int tupletLength = QUARTER_NOTE *2 / 3;
        tick = addNote(sp, new Pitch('G'), tick, tupletLength);
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, tupletLength);
        tick = addNote(sp, new Pitch('G').octaveTranspose(1), tick, tupletLength);
        
        tick = addNote(sp, new Pitch('A').octaveTranspose(1), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('F').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('G').octaveTranspose(1), tick, QUARTER_NOTE / 2);

        tick += QUARTER_NOTE / 2;
        
        tick = addNote(sp, new Pitch('E').octaveTranspose(1), tick, QUARTER_NOTE);
        tick = addNote(sp, new Pitch('C').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('D').octaveTranspose(1), tick, QUARTER_NOTE / 2);
        tick = addNote(sp, new Pitch('B'), tick, QUARTER_NOTE * 3 / 4);
        
        // [^F1/2e1/2] [F1/2e1/2] z1/2 [F1/2e1/2] z1/2 [F1/2c1/2] [Fe] |
        // [GBg] z G z | c3/2 G1/2 z E | E1/2 A B _B1/2 A |
        // (3Geg a f1/2 g1/2 | z1/2 e c1/2 d1/2 B3/4 |]
        
        sp.play();

    }

}
