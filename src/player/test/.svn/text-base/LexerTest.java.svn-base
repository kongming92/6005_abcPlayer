package player.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import player.Lexer;
import player.Lexer.TokenMismatchException;
import player.Type;

/**
 * Test the Lexer, by making sure that given a stream of characters, it produces
 * a stream of the correct Tokens. The testing strategy employed is test
 * all-symbols, ensuring that every possible token can be generated from the
 * appropriate series of characters.
 * 
 * Additional tests are then performed to ensure that the Lexer can correctly
 * emit combinations of tokens.
 * 
 */
public class LexerTest {
    private void runTest(Lexer l, Iterator<Lexer.Token> ilt)
            throws TokenMismatchException {
        Iterator<Lexer.Token> li = l.iterator();
        // Note: This assumes that the Token provides .equals(), which the
        // template implementation does not
        while (li.hasNext() && ilt.hasNext()) {
            assertEquals(ilt.next(), li.next());
        }
        assertFalse(li.hasNext());
        assertFalse(ilt.hasNext());
    }

    @Test
    public void noInputTest() {
        Lexer l = new Lexer("");
        assertFalse(l.iterator().hasNext());
    }

    @Test
    public void basicNoteTest() throws TokenMismatchException {
        String input = "ABCDEFGabcdefg";
        for (char c : input.toCharArray()) {
            String noteOnly = "" + c;
            Lexer l = new Lexer(noteOnly);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.NOTE_LETTER, noteOnly));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicAccidentalTest() throws TokenMismatchException {
        String inputs[] = { "^", "^^", "_", "__" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.ACCIDENTAL, input));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicOctaveShiftTest() throws TokenMismatchException {
        String inputs[] = { ",", "'", ",,", "''", ",,,", "'''" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.OCTAVE, input));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicDigitTest() throws TokenMismatchException {
        String inputs[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "123" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.DIGIT, input));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicFieldTest() throws TokenMismatchException {
        String[] fields = { "X:", "T:", "C:", "K:", "L:", "M:", "Q:", "V:" };
        Type[] fieldType = { Type.FIELD_INDEX_NUMBER, Type.FIELD_TITLE,
                Type.FIELD_COMPOSER_NAME, Type.FIELD_KEY,
                Type.FIELD_DEFAULT_LENGTH, Type.FIELD_METER, Type.FIELD_TEMPO,
                Type.FIELD_VOICE };
        String fieldValue = " Complete and utter junk! :: 0123 / [] :|\n";
        String expected = "Complete and utter junk! :: 0123 / [] :|";
        for (int i = 0; (i < fields.length); i++) {
            Lexer l = new Lexer(fields[i] + fieldValue);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(fieldType[i], expected));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicBarlinesTest() throws TokenMismatchException {
        String[] barlines = { "||", "[|", "|]", ":|", "|:", "[1", "[2" };
        Type[] barlineTypes = { Type.BARLINE, Type.BARLINE, Type.BARLINE,
                Type.BARLINE, Type.BARLINE, Type.NTH_REPEAT, Type.NTH_REPEAT };
        for (int i = 0; (i < barlines.length); i++) {
            Lexer l = new Lexer(barlines[i]);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(barlineTypes[i], barlines[i]));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicRandomSingleCharSymbolsTest()
            throws TokenMismatchException {
        String[] singleChar = { "[", "]", "/" };
        Type[] singleCharTypes = { Type.BEGIN_MULTINOTE, Type.END_MULTINOTE,
                Type.FRACTION_BAR };
        for (int i = 0; (i < singleChar.length); i++) {
            Lexer l = new Lexer(singleChar[i]);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(singleCharTypes[i]));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicTupletTest() throws TokenMismatchException {
        String[] tuplet = { "(2", "(3", "(20" };
        for (int i = 0; (i < tuplet.length); i++) {
            Lexer l = new Lexer(tuplet[i]);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.TUPLET, tuplet[i]
                    .substring(1)));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test(expected = TokenMismatchException.class)
    public void FailbadtupletTest() throws TokenMismatchException {
        String inputs[] = { "('" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(null);
            runTest(l, expectedResult.iterator());
        }
    }

    @Test(expected = TokenMismatchException.class)
    public void FailbadcharacterTest() throws TokenMismatchException {
        String inputs[] = { "v'" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(null);
            runTest(l, expectedResult.iterator());
        }
    }

    @Test(expected = TokenMismatchException.class)
    public void FailbadlineTest() throws TokenMismatchException {
        String inputs[] = { "1/3x" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.DIGIT, "1"));
            expectedResult.add(new Lexer.Token(Type.FRACTION_BAR));
            expectedResult.add(new Lexer.Token(Type.DIGIT, "3"));
            expectedResult.add(null);
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void RepeatTest() throws TokenMismatchException {
        String inputs[] = { "|:a:|" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.BARLINE, "|:"));
            expectedResult.add(new Lexer.Token(Type.NOTE_LETTER, "a"));
            expectedResult.add(new Lexer.Token(Type.BARLINE, ":|"));

            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void RepeatBarlineTest() throws TokenMismatchException {
        String inputs[] = { "|||" };
        for (String input : inputs) {
            Lexer l = new Lexer(input);
            List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
            expectedResult.add(new Lexer.Token(Type.BARLINE, "||"));
            expectedResult.add(new Lexer.Token(Type.BARLINE, "|"));
            runTest(l, expectedResult.iterator());
        }
    }

    @Test
    public void basicFractionTest() throws TokenMismatchException {
        String tuplet = "5/16";
        List<Lexer.Token> expectedResult = new ArrayList<Lexer.Token>();
        expectedResult.add(new Lexer.Token(Type.DIGIT, "5"));
        expectedResult.add(new Lexer.Token(Type.FRACTION_BAR));
        expectedResult.add(new Lexer.Token(Type.DIGIT, "16"));
        Lexer l = new Lexer(tuplet);
        runTest(l, expectedResult.iterator());
    }

}
