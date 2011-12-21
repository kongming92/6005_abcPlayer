package player.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import music.ast.Music;

import org.junit.Test;

import player.Lexer.Token;
import player.Parser;
import player.Parser.ParseException;
import player.Type;

public class ParserTest {

    @Test
    public void singleNoteTest() throws ParseException {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(Type.NOTE_LETTER, "C"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void singleChordTest() throws ParseException {
    	 List<Token> tokens = new ArrayList<Token>();
    	 tokens.add(new Token(Type.BEGIN_MULTINOTE, "["));
         tokens.add(new Token(Type.NOTE_LETTER, "C"));
         tokens.add(new Token(Type.NOTE_LETTER, "E"));
         tokens.add(new Token(Type.NOTE_LETTER, "G"));
         tokens.add(new Token(Type.END_MULTINOTE, "]"));
         Parser p = new Parser(tokens.iterator());
         Music m = p.getMusic();
         String expected = "Music[Voice[Bar[Chord[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]]]]]";
         assertEquals(expected, m.toString());
    }
    
    @Test
    public void single2TupletTest() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.TUPLET, "2"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Tuplet[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void single3TupleTest() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.TUPLET, "3"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Tuplet[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void single4TupleTest() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.TUPLET, "4"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        tokens.add(new Token(Type.NOTE_LETTER, "c"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Tuplet[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1],Note[pitch=c,length=1/1]]]]]";
        assertEquals(expected, m.toString());
   }
    
    @Test
    public void testNoteAfterTuplet() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.TUPLET, "3"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        tokens.add(new Token(Type.NOTE_LETTER, "c"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Tuplet[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]],Note[pitch=c,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testUpOneOctave() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "c")); 
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=c,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testUpManyOctaves() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "c")); 
    	tokens.add(new Token(Type.OCTAVE, "'"));
    	tokens.add(new Token(Type.OCTAVE, "'"));
    	tokens.add(new Token(Type.OCTAVE, "'"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=c''',length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testDownManyOctaves() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.OCTAVE, ",,,"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,,,,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void longerIntegerLength() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "c")); 
    	tokens.add(new Token(Type.OCTAVE, "''"));
    	tokens.add(new Token(Type.DIGIT, "3"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=c'',length=3/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void fractionalLength() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.DIGIT, "1"));
    	tokens.add(new Token(Type.FRACTION_BAR, "/"));
    	tokens.add(new Token(Type.DIGIT, "8"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/8]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testNoNumerator() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.FRACTION_BAR, "/"));
    	tokens.add(new Token(Type.DIGIT, "8"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/8]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testNoDenominator() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.DIGIT, "1"));
    	tokens.add(new Token(Type.FRACTION_BAR, "/"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/2]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testRest() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "z")); 
    	tokens.add(new Token(Type.DIGIT, "1"));
    	tokens.add(new Token(Type.FRACTION_BAR, "/"));
    	tokens.add(new Token(Type.DIGIT, "4"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Rest[length=1/4]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testSharp() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.ACCIDENTAL, "^"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1],Accidental[pitch=D,value=1],Note[pitch=D,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testDoubleSharp() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.ACCIDENTAL, "^^"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Accidental[pitch=C,value=2],Note[pitch=C,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testFlat() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.ACCIDENTAL, "_"));
    	tokens.add(new Token(Type.NOTE_LETTER, "B")); 
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Accidental[pitch=B,value=-1],Note[pitch=B,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testDoubleFlat() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.ACCIDENTAL, "__"));
    	tokens.add(new Token(Type.NOTE_LETTER, "B")); 
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Accidental[pitch=B,value=-2],Note[pitch=B,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testNeutral() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.ACCIDENTAL, "^"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C")); 
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
    	tokens.add(new Token(Type.ACCIDENTAL, "="));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
    	Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Accidental[pitch=C,value=1],Note[pitch=C,length=1/1],Note[pitch=D,length=1/1],Accidental[pitch=C,value=0],Note[pitch=C,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testChordAccidental() throws ParseException {
		 List<Token> tokens = new ArrayList<Token>();
		 tokens.add(new Token(Type.BEGIN_MULTINOTE, "["));
		 tokens.add(new Token(Type.ACCIDENTAL, "^"));
		 tokens.add(new Token(Type.NOTE_LETTER, "C"));
		 tokens.add(new Token(Type.NOTE_LETTER, "E"));
		 tokens.add(new Token(Type.END_MULTINOTE, "]"));
		 Parser p = new Parser(tokens.iterator());
		 Music m = p.getMusic();
		 String expected = "Music[Voice[Bar[Accidental[pitch=C,value=1],Chord[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1]]]]]";
	     assertEquals(expected, m.toString());
    }
    
    @Test
    public void testTupletAccidental() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.TUPLET, "3"));
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
    	tokens.add(new Token(Type.ACCIDENTAL, "^"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Tuplet[Note[pitch=C,length=1/1],Accidental[pitch=E,value=1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testFieldDefaultLength() throws ParseException {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(Type.FIELD_DEFAULT_LENGTH, "4/4"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice]]";
        assertEquals(expected, m.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testFieldDefaultLengthInvalid() throws ParseException {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(Type.FIELD_DEFAULT_LENGTH, "7"));
        Parser p = new Parser(tokens.iterator());
        p.getMusic();
    }
    
    @Test
    public void testSingleRepeat() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        tokens.add(new Token(Type.BARLINE, ":|"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Repeat[Bar[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testSingleRepeatWithAddtlNotes() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.NOTE_LETTER, "E"));
        tokens.add(new Token(Type.NOTE_LETTER, "G"));
        tokens.add(new Token(Type.BARLINE, ":|"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        tokens.add(new Token(Type.NOTE_LETTER, "a"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Repeat[Bar[Note[pitch=C,length=1/1],Note[pitch=E,length=1/1],Note[pitch=G,length=1/1]]],Bar[Note[pitch=D,length=1/1],Note[pitch=F,length=1/1],Note[pitch=a,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testBeginEndRepeat() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.BARLINE, "|:"));
    	tokens.add(new Token(Type.NOTE_LETTER, "A"));
        tokens.add(new Token(Type.BARLINE, "|"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
    	tokens.add(new Token(Type.BARLINE, "|"));
        tokens.add(new Token(Type.BARLINE, ":|"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1]],Repeat[Bar[Note[pitch=A,length=1/1]],Bar[Note[pitch=D,length=1/1]]],Bar[Note[pitch=F,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testBasicAltRepeat() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.BARLINE, "|:"));
    	tokens.add(new Token(Type.NOTE_LETTER, "A"));
        tokens.add(new Token(Type.BARLINE, "|"));
        tokens.add(new Token(Type.NTH_REPEAT, "[1"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
        tokens.add(new Token(Type.BARLINE, ":|"));
        tokens.add(new Token(Type.NTH_REPEAT, "[2"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1]],Repeat[Bar[Note[pitch=A,length=1/1]][1 Bar[Note[pitch=D,length=1/1]]] 2[ Bar[Note[pitch=F,length=1/1]]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testAltRepeatNoStart() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.BARLINE, "|"));
        tokens.add(new Token(Type.NTH_REPEAT, "[1"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
        tokens.add(new Token(Type.BARLINE, ":|"));
        tokens.add(new Token(Type.NTH_REPEAT, "[2"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Repeat[Bar[Note[pitch=C,length=1/1]][1 Bar[Note[pitch=D,length=1/1]]] 2[ Bar[Note[pitch=F,length=1/1]]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testRepeatsNoStart() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.BARLINE, ":|"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
    	tokens.add(new Token(Type.BARLINE, "|"));
    	tokens.add(new Token(Type.NOTE_LETTER, "E"));
    	tokens.add(new Token(Type.BARLINE, ":|"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Repeat[Bar[Note[pitch=C,length=1/1]]],Repeat[Bar[Note[pitch=D,length=1/1]],Bar[Note[pitch=E,length=1/1]]],Bar[Note[pitch=F,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testRepeatsWithDoubleBar() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
    	tokens.add(new Token(Type.NOTE_LETTER, "C"));
        tokens.add(new Token(Type.BARLINE, "||"));
    	tokens.add(new Token(Type.NOTE_LETTER, "D"));
    	tokens.add(new Token(Type.BARLINE, "|"));
    	tokens.add(new Token(Type.NOTE_LETTER, "E"));
    	tokens.add(new Token(Type.BARLINE, ":|"));
        tokens.add(new Token(Type.NOTE_LETTER, "F"));
        tokens.add(new Token(Type.BARLINE, ":|"));
        Parser p = new Parser(tokens.iterator());
        Music m = p.getMusic();
        String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1]],Repeat[Bar[Note[pitch=D,length=1/1]],Bar[Note[pitch=E,length=1/1]]],Repeat[Bar[Note[pitch=F,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testRepeatsWithDoubeBar() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(Type.NOTE_LETTER, "C"));
	    tokens.add(new Token(Type.BARLINE, "|]"));
		tokens.add(new Token(Type.NOTE_LETTER, "D"));
		tokens.add(new Token(Type.BARLINE, "|"));
		tokens.add(new Token(Type.NOTE_LETTER, "E"));
		tokens.add(new Token(Type.BARLINE, "|"));
	    tokens.add(new Token(Type.NOTE_LETTER, "F"));
	    tokens.add(new Token(Type.BARLINE, ":|"));
	    Parser p = new Parser(tokens.iterator());
	    Music m = p.getMusic();
	    String expected = "Music[Voice[Bar[Note[pitch=C,length=1/1]],Repeat[Bar[Note[pitch=D,length=1/1]],Bar[Note[pitch=E,length=1/1]],Bar[Note[pitch=F,length=1/1]]]]]";
        assertEquals(expected, m.toString());
    }
    
    @Test
    public void testEndAltEnding() throws ParseException {
    	List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(Type.NOTE_LETTER, "C"));
		tokens.add(new Token(Type.NOTE_LETTER, "D"));
		tokens.add(new Token(Type.BARLINE, "|"));
		tokens.add(new Token(Type.NOTE_LETTER, "E"));
	    tokens.add(new Token(Type.NOTE_LETTER, "F"));
	    tokens.add(new Token(Type.BARLINE, "|"));
	    tokens.add(new Token(Type.NTH_REPEAT, "[1"));
	    tokens.add(new Token(Type.NOTE_LETTER, "C"));
	    tokens.add(new Token(Type.NOTE_LETTER, "D"));
	    tokens.add(new Token(Type.BARLINE, ":|"));
	    tokens.add(new Token(Type.NTH_REPEAT, "[2"));
	    tokens.add(new Token(Type.NOTE_LETTER, "A"));
	    tokens.add(new Token(Type.NOTE_LETTER, "B"));
	    tokens.add(new Token(Type.BARLINE, "|"));	
	    tokens.add(new Token(Type.NOTE_LETTER, "c"));
	    tokens.add(new Token(Type.BARLINE, "|]"));
	    tokens.add(new Token(Type.NOTE_LETTER, "d"));
	    Parser p = new Parser(tokens.iterator());
	    Music m = p.getMusic();
	    String expected = "Music[Voice[Repeat[Bar[Note[pitch=C,length=1/1],Note[pitch=D,length=1/1]],Bar[Note[pitch=E,length=1/1],Note[pitch=F,length=1/1]][1 Bar[Note[pitch=C,length=1/1],Note[pitch=D,length=1/1]]] 2[ Bar[Note[pitch=A,length=1/1],Note[pitch=B,length=1/1]],Bar[Note[pitch=c,length=1/1]]]],Bar[Note[pitch=d,length=1/1]]]]";
        assertEquals(expected, m.toString());
    }
}
