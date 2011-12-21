package player;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Musical lexical analyzer. Converts a String to a set of iterable tokens
 */
public class Lexer implements Iterable<Lexer.Token> {

    /**
     * Token in the stream.
     */
    public static class Token {
        final Type type;
        final String text;

        /**
         * Construct a Token
         */
        public Token(Type type, String text) {
            this.type = type;
            this.text = text;
        }

        /**
         * Construct a Token
         */
        public Token(Type type) {
            this(type, null);
        }

        /**
         * Return a string version of a token
         */
        public String toString() {
            return "Token[type=" + type + ",text=" + text + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((text == null) ? 0 : text.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        /**
         * Check if token is equal to object. return boolean if token is equal
         * otherwise return false
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Token other = (Token) obj;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            if (type != other.type)
                return false;
            return true;
        }
    }

    /**
     * New exception that will be thrown in Lexer
     */
    @SuppressWarnings("serial")
    public static class TokenMismatchException extends RuntimeException {
        public TokenMismatchException(String message) {
            super(message);
        }
    }

    private StringBuffer myinput = null;

    /**
     * 
     * Lexer: tokenizes input
     * 
     * method hasnext() returns a boolean true if there is another token in the
     * input and false if there are no more tokens
     * 
     * method next() returns the next token in the sequence
     * 
     * @param String
     *            Input (not modified) must consist only of digits, decimal
     *            points *,+,-,/,(,); alphabet character, colons, and percent
     *            signs, carets, underscores, and whitespaces.
     * 
     */
    public Lexer(String input) {
        myinput = new StringBuffer(input);

    }

    /**
     * Regex for Tokens
     */
    private final Pattern NOTE = Pattern.compile("[A-Ga-gz]");
    private final Pattern DIGIT = Pattern.compile("^\\d+");
    private final Pattern FLAT = Pattern.compile("_+");
    private final Pattern NEUTRAL = Pattern.compile("=+");
    private final Pattern SHARP = Pattern.compile("\\^+");
    private final Pattern OCTAVEUP = Pattern.compile("'+");
    private final Pattern OCTAVEDOWN = Pattern.compile(",+");

    /**
     * Special inner class to implement the Iterator interface, allowing the
     * Parser to not be Lexer-specific.
     * 
     */
    private class LexerIterator implements Iterator<Lexer.Token> {

        /**
         * method hasNext() returns a boolean true if there is another token in
         * the input and false if there are no more tokens
         */
        public boolean hasNext() {

            while (myinput.length() > 0
                    && (myinput.charAt(0) == '\t' || myinput.charAt(0) == '\n'
                            || myinput.charAt(0) == ' ' || myinput.charAt(0) == '\r')) {

                myinput.deleteCharAt(0);

            }

            if (myinput != null && myinput.length() > 0) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Created a StringBuffer myinput copy of Input to modify at each call
         * of next(), the next relevant substring in sequence is removed from
         * myinput and a corresponding token is returned. the substring may
         * consist of 1 or more characters that form the regex of a type. Note
         * that calling next() will modify the StringBuffer and the tokens can
         * then be returned in an iterator
         */
        public Token next() {
            /** Is there any data to process? */
            if (myinput.length() == 0) {
                return new Token(Type.EOF, null);
            }
            /** Remove whitespace and other leading junk. */
            final int LOOK_AT = 0;
            while (true) {
                switch (myinput.charAt(LOOK_AT)) {
                case '\t':
                case ' ':
                case '\n':
                case '\r':
                    myinput.deleteCharAt(LOOK_AT);
                    continue;
                }
                break;
            }
            /**
             * Check once more, after we deleted trailing junk.
             */
            if (myinput.length() == 0) {
                return new Token(Type.EOF, null);
            }

            /**
             * use helper methods to check if next Token is of the following
             * types: field, barline, tuplet, flat, sharp, octaveup, octavedown
             */
            Token result = getField();
            if (result != null) {
                return result;
            }
            result = getBarline();
            if (result != null) {
                return result;
            }

            result = getTuplet();
            if (result != null) {
                return result;
            }
            result = getFlat();
            if (result != null) {
                return result;
            }
            result = getSharp();
            if (result != null) {
                return result;
            }
            result = getOctaveUp();
            if (result != null) {
                return result;
            }
            result = getOctaveDown();
            if (result != null) {
                return result;
            }

            if (myinput.charAt(0) == '|') {
                myinput.deleteCharAt(0);
                return new Token(Type.BARLINE, "|");
            }
            if (myinput.charAt(0) == '%') {
                myinput.deleteCharAt(0);
                int newLinePos = myinput.indexOf("\n");
                result = new Token(Type.COMMENT, myinput.substring(0,
                        newLinePos));
                myinput.delete(0, newLinePos);
                return result;
            }

            Matcher m = NOTE.matcher(myinput.substring(0, 1));
            if (m.matches()) {
                String a1 = myinput.substring(0, 1);
                myinput.deleteCharAt(0);

                return new Token(Type.NOTE_LETTER, a1);
            }

            result = getDigit();
            if (result != null) {
                return result;
            }
            result = getNeutral();
            if (result != null) {
                return result;
            }

            if (myinput.charAt(0) == '/') {
                myinput.deleteCharAt(0);
                return new Token(Type.FRACTION_BAR);
            }
            if (myinput.charAt(0) == '[') {
                myinput.deleteCharAt(0);
                return new Token(Type.BEGIN_MULTINOTE);
            }
            if (myinput.charAt(0) == ']') {
                myinput.deleteCharAt(0);
                return new Token(Type.END_MULTINOTE);
            }

            throw new TokenMismatchException(
                    "Bad tokens check your grammar and syntax! " + myinput);

        }

        @Override
        public void remove() {
            // Do nothing?
        }
    }

    /**
     * Helper method used to convert an array of Strings to appropriate Tokens.
     * 
     * @param baseLen
     *            Minimal length of the string
     * @param string
     *            Array of String to convert
     * @param stringType
     *            Array of Type to convert to
     * @return A token
     */
    private Token get(int baseLen, String[] string, Type[] stringType) {
        if (myinput.length() < baseLen) {
            return null;
        }
        assert string.length == stringType.length;
        Token result = null;
        if (myinput.length() < baseLen) {
            return null;
        }
        for (int i = 0; (i < string.length); i++) {
            String extract = myinput.substring(0, 2);
            if (string[i].equals(extract)) {
                result = new Token(stringType[i], extract);
                myinput.delete(0, 2);
                return result;
            }
        }
        return null;
    }

    /**
     * helper method to check if the next token is a field token modifies
     * myinput returns the next token if next token is a field token or null if
     * it is not
     */
    private Token getField() {
        String[] fields = { "X:", "T:", "C:", "K:", "L:", "M:", "Q:", "V:" };
        Type[] fieldType = { Type.FIELD_INDEX_NUMBER, Type.FIELD_TITLE,
                Type.FIELD_COMPOSER_NAME, Type.FIELD_KEY,
                Type.FIELD_DEFAULT_LENGTH, Type.FIELD_METER, Type.FIELD_TEMPO,
                Type.FIELD_VOICE };
        assert fields.length == fieldType.length;
        Token result = null;
        if (myinput.length() < 2) {
            return null;
        }
        for (int i = 0; (i < fields.length); i++) {
            if (fields[i].equals(myinput.substring(0, 2))) {
                myinput.delete(0, 2);
                int newLinePos = myinput.indexOf("\n");
                String field = myinput.substring(0, newLinePos);
                int nocomment = field.indexOf("%");
                if (nocomment != -1) {
                    result = new Token(fieldType[i], field.substring(0,
                            nocomment).trim());
                } else {
                    result = new Token(fieldType[i], field.trim());
                }
                myinput.delete(0, newLinePos);
                return result;
            }
        }
        return null;
    }

    /**
     * helper method to check if the next token is a Barline token modifies
     * myinput returns the next token if next token is a Barline token or null
     * if it is not
     */
    private Token getBarline() {
        String[] barlines = { "||", "[|", "|]", ":|", "|:", "[1", "[2" };
        Type[] barlineTypes = { Type.BARLINE, Type.BARLINE, Type.BARLINE,
                Type.BARLINE, Type.BARLINE, Type.NTH_REPEAT, Type.NTH_REPEAT };
        return get(2, barlines, barlineTypes);
    }

    /**
     * helper method to check if the next token is a Tuplet token modifies
     * myinput returns the next token if next token is a Tuplet token or null if
     * it is not
     */
    private Token getTuplet() {
        if (myinput.length() < 1) {
            return null;
        }
        if (myinput.charAt(0) == '(') {
            myinput.deleteCharAt(0);
            Token digit = getDigit();
            if (digit == null) {
                throw new TokenMismatchException("Bad tuplet token! " + myinput);
            }
            return new Token(Type.TUPLET, digit.text);
        }
        return null;
    }

    /**
     * helper method to check if the next token is a Digit token modifies
     * myinput returns the next token if next token is a Digit token or null if
     * it is not
     */

    private Token getDigit() {
        Matcher n = DIGIT.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);
            myinput.delete(0, end);
            return new Token(Type.DIGIT, a);
        }
        return null;
    }

    /**
     * helper method to check if the next token is a Sharp token modifies
     * myinput returns the next token if next token is a Sharp token or null if
     * it is not
     */
    private Token getSharp() {
        Matcher n = SHARP.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);
            myinput.delete(0, end);
            if (a.length() > 2) {
                throw new TokenMismatchException("Note is way too sharp!"
                        + myinput);
            }
            return new Token(Type.ACCIDENTAL, a);
        }
        return null;
    }

    /**
     * helper method to check if the next token is a flat token modifies myinput
     * returns the next token if next token is a flat token or null if it is not
     */
    private Token getFlat() {
        Matcher n = FLAT.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);
            myinput.delete(0, end);
            if (a.length() > 2) {
                throw new TokenMismatchException("Note is way too flat!"
                        + myinput);
            }
            return new Token(Type.ACCIDENTAL, a);
        }
        return null;
    }

    /**
     * helper method to check if the next token is a octaveup token modifies
     * myinput returns the next token if next token is a octaveup token or null
     * if it is not
     */
    private Token getOctaveUp() {
        Matcher n = OCTAVEUP.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);

            myinput.delete(0, end);

            if (myinput.length() > 0 && (myinput.substring(0, 1).equals(","))) {
                System.out.println("bad");
                throw new TokenMismatchException(
                        "Cannot take one note both an octave up and down "
                                + myinput);

            }
            return new Token(Type.OCTAVE, a);
        }
        return null;
    }

    private Token getNeutral() {
        Matcher n = NEUTRAL.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);
            myinput.delete(0, end);
            return new Token(Type.ACCIDENTAL, a);
        }
        return null;
    }

    /**
     * helper method to check if the next token is a octavedown token modifies
     * myinput returns the next token if next token is a octavedown token or
     * null if it is not
     */

    private Token getOctaveDown() {
        Matcher n = OCTAVEDOWN.matcher(myinput.toString());
        String a = "";
        if (n.lookingAt()) {
            assert 0 == n.start();
            int end = n.end();
            a = myinput.substring(0, end);
            myinput.delete(0, end);
            if (myinput.length() > 0 && (myinput.substring(0, 1).equals("'"))) {
                System.out.println("bad");
                throw new TokenMismatchException(
                        "Cannot take one note both an octave up and down "
                                + myinput);

            }
            return new Token(Type.OCTAVE, a);
        }
        return null;
    }

    @Override
    public Iterator<Lexer.Token> iterator() {
        return new LexerIterator();
    }
}
