package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import music.ast.Accidental;
import music.ast.Bar;
import music.ast.Chord;
import music.ast.KeySignature;
import music.ast.Music;

import music.ast.MusicalElementContainer;
import music.ast.MusicalLength;
import music.ast.MusicalType;
import music.ast.Note;
import music.ast.Repeat;
import music.ast.Rest;
import music.ast.Tuplet;
import music.ast.Voice;
import player.Lexer.Token;
import sound.Pitch;

/**
 * Parser class Takes a single lexer and iterates through Tokens. Ultimately
 * creates the Music object given by the Tokens through the call to getMusic()
 * Keeps states of currentVoice, parsingStates, and currentState, which itself
 * keeps states of currentRepeat, astStack, currentNote, currentAccidental,
 * barList, and altRepeat.
 * 
 * @author charlesliu
 * 
 */
public class Parser {

    private Iterator<Lexer.Token> lexer;
    private Music music; // overall Music object
    private Voice currentVoice; // current Voice object. Add to it
    private Map<String, ParsingState> parsingStates; // map of voice to the
                                                     // ParsingState
                                                     // associated with the
                                                     // voice
    private ParsingState currentState; // current ParsingState of the Voice.

    // this includes the current Notes/Accidentals/Repeats/Bars of the Voice
    // that are not yet
    // in the Voice object, as well as a Stack that keeps track of the path of
    // each element down the AST

    private static class ParsingState {
        Repeat currentRepeat;
        Stack<MusicalElementContainer> astStack;
        Note currentNote;
        int currentAccidental;
        boolean fractionBarProcessed;
        boolean hasAccidental;
        int altRepeat;
        List<Bar> barList;

        ParsingState() {
            this.astStack = new Stack<MusicalElementContainer>();
            this.currentNote = null;
            this.currentAccidental = 0;
            this.fractionBarProcessed = false;
            this.currentRepeat = null;
            this.hasAccidental = false;
            this.altRepeat = 0;
            this.barList = new ArrayList<Bar>();
        }
    }

    @SuppressWarnings("serial")
    public static class ParseException extends Exception {
        public ParseException(String s) {
            super(s);
        }
    }

    /**
     * Constructs a new Parser object
     * 
     * @param lexer
     *            - takes an Iterator. Can be lexer.iterator() or any other
     *            Iterable object
     */
    public Parser(Iterator<Lexer.Token> lexer) {
        this.lexer = lexer;
        this.music = new Music();
        this.currentVoice = new Voice("default");
        this.parsingStates = new HashMap<String, ParsingState>();
        ParsingState ps = new ParsingState();
        this.parsingStates.put("default", ps);
        this.currentState = ps;
        currentState.astStack.push(new Bar());
    }

    /**
     * Main parsing function. Returns a single Music object corresponding to the
     * Music representing the entirety of the tokens. Modifies - all fields
     * 
     * @return - Music object that corresponds to the entirety of the tokens.
     * @throws ParseException
     *             - on all failures that involve invalid Tokens or Tokens in
     *             the wrong location
     */
    public Music getMusic() throws ParseException {

        while (lexer.hasNext()) {
            Token token = null;
            try {
                token = lexer.next();
            } catch (Throwable t) {
                throw new ParseException(t.getMessage());
            }
            switch (token.type) {

            case COMMENT:
                break; // ignore

            case FIELD_INDEX_NUMBER:
                music.setIndexNumber(token.text);
                break;

            case FIELD_TITLE:
                music.setTitle(token.text);
                break;

            case FIELD_COMPOSER_NAME:
                music.setComposer(token.text);
                break;

            case FIELD_DEFAULT_LENGTH:
                MusicalLength length;
                String[] result = token.text.split("/"); // strict fraction -
                                                         // must have bar as
                                                         // spec. by grammar
                if (result.length != 2) {
                    throw new ParseException(
                            "ERROR: Default length is invalid.");
                }
                try {
                    length = new MusicalLength(Integer.parseInt(result[0]),
                            Integer.parseInt(result[1]));
                    music.setDefaultLength(length);
                } catch (Throwable t) {
                    throw new ParseException("Error reading default length");
                }
                break;

            case FIELD_METER:
                MusicalLength meter = null;
                if ("C".equals(token.text)) { // common time = 4/4 time
                    meter = new MusicalLength(4, 4);
                } else if ("C|".equals(token.text)) { // cut time = 2/2 time
                    meter = new MusicalLength(2, 2);
                } else {
                    String[] meterParts = token.text.split("/");
                    if (meterParts.length != 2) { // meter must be a fraction
                                                  // with num and denom
                        throw new ParseException("ERROR: Meter is invalid.");
                    }
                    meter = new MusicalLength(Integer.parseInt(meterParts[0]),
                            Integer.parseInt(meterParts[1]));
                }
                music.setMeter(meter);
                break;

            case FIELD_TEMPO:
                music.setTempo(Integer.parseInt(token.text));
                break;

            case FIELD_KEY:
                try {
                    // do a keysignature lookup to find out which notes are
                    // sharped or flatted
                    music.setKeySignature(new KeySignature(token.text));
                } catch (Throwable t) {
                    throw new ParseException(t.getMessage());
                }
                break;

            case FIELD_VOICE: // for all tokens with V:.
                // Either the voice declaration in the header --> create blank
                // Voices
                // Or a new voice declared in the body

                if (currentVoice == null) { // first Voice found
                    currentVoice = new Voice(token.text); // create new Voice
                                                          // and push to Stack
                    currentState = new ParsingState();
                    currentState.astStack.push(new Bar());
                } else {
                    // put currentVoice into Music
                    music.addVoice(currentVoice.getName(), currentVoice);
                    parsingStates.put(currentVoice.getName(), currentState);
                    Voice newVoice = music.getVoice(token.text);
                    if (newVoice != null) { // seen this Voice before
                        currentVoice = newVoice;
                        currentState = parsingStates
                                .get(currentVoice.getName());
                    } else { // never seen this Voice before
                        currentVoice = new Voice(token.text);
                        currentState = new ParsingState();
                        currentState.astStack.push(new Bar());
                    }
                }
                break;

            case NOTE_LETTER:
                formNote();
                if (token.text.length() != 1) {
                    throw new ParseException(
                            "ERROR: Multi-letter note encountered, this shouldn't occur!");
                }
                char pitchLetter = token.text.charAt(0);
                Pitch pitch;
                if (pitchLetter == 'z') {
                    pitch = null;
                } else {
                    pitch = new Pitch(Character.toUpperCase(pitchLetter));
                    if (Character.isLowerCase(pitchLetter)) {
                        pitch = pitch.octaveTranspose(1);
                    }
                }
                currentState.currentNote = new Note(pitch, new MusicalLength(1,
                        1));
                break;

            // assumes all octave modifiers come in single token
            case OCTAVE:
                if (currentState.currentNote == null) {
                    throw new ParseException(
                            "ERROR: Octave marker out of order");
                }
                pitch = currentState.currentNote.getPitch();
                if (pitch == null) {
                    throw new ParseException(
                            "ERROR: Rests cannot have octave modifiers");
                }
                int modifier = 0;
                switch (token.text.charAt(0)) {
                case ',': // down octave
                    if (currentState.currentNote.getPitch().getOctave() == 1) {
                        throw new ParseException(
                                "ERROR: Cannot shift down on lower-case letter");
                    }
                    modifier = -1;
                    break;
                case '\'': // up octave
                    if (currentState.currentNote.getPitch().getOctave() == 0) {
                        throw new ParseException(
                                "ERROR: Cannot shift up on upper-case letter");
                    }
                    modifier = 1;
                    break;
                default:
                    throw new ParseException("ERROR: Bad octave token \""
                            + token.text + "\"!");
                }
                pitch = currentState.currentNote.getPitch().octaveTranspose(
                        modifier * token.text.length());
                currentState.currentNote = new Note(pitch, new MusicalLength(1,
                        1));
                break;

            // Used in numerator and denominator of note and rest lengths
            case DIGIT:
                if (currentState.currentNote == null) {
                    throw new ParseException("ERROR: Digit token out of order");
                }
                if (currentState.fractionBarProcessed) {
                    currentState.currentNote = new Note(
                            currentState.currentNote.getPitch(),
                            new MusicalLength(currentState.currentNote
                                    .getLength().getNum(),
                                    Integer.parseInt(token.text)));
                } else {
                    currentState.currentNote = new Note(
                            currentState.currentNote.getPitch(),
                            new MusicalLength(Integer.parseInt(token.text),
                                    currentState.currentNote.getLength()
                                            .getDenom()));
                }
                break;

            case FRACTION_BAR:
                if (currentState.currentNote == null
                        || currentState.fractionBarProcessed) {
                    throw new ParseException("ERROR: Fraction bar out of order");
                }
                currentState.fractionBarProcessed = true;
                currentState.currentNote = new Note(
                        currentState.currentNote.getPitch(), new MusicalLength(
                                currentState.currentNote.getLength().getNum(),
                                2));
                // default with bar and no denom --> denom = 2
                break;

            case ACCIDENTAL: // double sharps, sharps, flat, double flat,
                             // natural
                formNote();
                int accidentalModifier = 0;
                switch (token.text.charAt(0)) {
                case '_':
                    accidentalModifier = -1;
                    break;
                case '^':
                    accidentalModifier = 1;
                    break;
                case '=':
                    accidentalModifier = 0;
                    break;
                default:
                    throw new ParseException("ERROR: Bad accidental token '"
                            + token.text + "'");
                }
                currentState.currentAccidental = accidentalModifier
                        * token.text.length();
                currentState.hasAccidental = true;
                break;

            case BEGIN_MULTINOTE:
                // set stack to have new Chord to push notes into
                formNote();
                currentState.astStack.push(new Chord());
                break;

            case END_MULTINOTE:
                // end chord
                // put chord into object on stack below it
                formNote();
                if (currentState.astStack.peek().getMusicalType() != MusicalType.CHORD) {
                    throw new ParseException(
                            "ERROR: End chord character without a chord present.");
                }
                Chord chord = (Chord) currentState.astStack.pop();
                currentState.astStack.peek().add(chord);
                break;

            case TUPLET:
                formNote();
                currentState.astStack.push(new Tuplet(Integer
                        .parseInt(token.text)));
                break;

            case BARLINE:
                // do for all types of barlines, repeat or not
                formNote();
                if (currentState.astStack.peek().getMusicalType() != MusicalType.BAR) {
                    throw new ParseException("ERROR: Invalid barline position.");
                }
                Bar bar = (Bar) currentState.astStack.pop();
                if (currentVoice == null) {
                    throw new ParseException(
                            "ERROR: No Voice, but encountered Barline?");
                }
                // if there are repeats, add bar to repeat
                if (currentState.currentRepeat != null) {
                    if (bar.getElements().size() > 0) {
                        switch (currentState.altRepeat) {
                        case 0:
                            currentState.currentRepeat.addNormalBar(bar);
                            break;
                        case 1:
                            currentState.currentRepeat.addToFirstEnding(bar);
                            break;
                        case 2:
                            currentState.currentRepeat.addToSecondEnding(bar);
                            break;
                        default:
                            throw new ParseException(
                                    "ERROR: Invalid n-th repeat state");
                        }
                    }
                } else {
                    currentState.barList.add(bar);
                }
                currentState.astStack.push(new Bar());

                if (token.text.equals("|:")) { // begin repeat
                    addAllToCurrentVoice();
                    currentState.currentRepeat = new Repeat();
                    currentVoice.seeBeginRepeat();
                    break;
                }

                if (token.text.equals(":|")) {
                    // if only :| without |: --> repeat from beginning of voice
                    if (currentState.currentRepeat == null) {
                        // if no currentRepeat, create one
                        if (!currentVoice.hasBeginRepeat()) {
                            currentState.currentRepeat = new Repeat();
                            for (Bar b : currentState.barList) {
                                currentState.currentRepeat.addNormalBar(b);
                            }
                            currentState.barList.clear();
                        } else {
                            throw new ParseException(
                                    "ERROR: Invalid repeat token.");
                        }
                    }
                    currentVoice.add(currentState.currentRepeat);
                    if (currentState.altRepeat == 0) {
                        currentState.currentRepeat = null;
                    }
                }

                // double bars - marker for begin repeat if no |: barline
                if (token.text.equals("||") || token.text.equals("|]")) {
                    currentState.currentRepeat = null;
                    addAllToCurrentVoice();
                }
                break;

            case NTH_REPEAT:
                currentState.altRepeat = Integer.parseInt(token.text
                        .substring(1));
                if (currentState.altRepeat == 1) {
                    if (currentState.currentRepeat == null) {
                        currentState.currentRepeat = new Repeat();
                        for (Bar ms : currentState.barList) {
                            currentState.currentRepeat.addNormalBar(ms);
                        }
                        currentState.barList.clear();
                    }
                } else if (currentState.altRepeat == 2) {
                    // All good.
                } else {
                    throw new ParseException(
                            "ERROR: Higher-order (>2) Nth repeat encountered!");
                }
            }
        }

        formNote(); // form note with modifier, push to appropriate object
        if (currentState.astStack.peek().getMusicalType() != MusicalType.BAR) {
            throw new ParseException("ERROR: Invalid barline position.");
        }
        if (currentState.currentRepeat != null) {
            // unclosed repeat - really shouldn't happen, but we will extend the
            // repeat to the end
            switch (currentState.altRepeat) {
            case 0:
                currentState.currentRepeat
                        .addNormalBar((Bar) currentState.astStack.pop());
                break;
            case 1:
                currentState.currentRepeat
                        .addToFirstEnding((Bar) currentState.astStack.pop());
                break;
            case 2:
                currentState.currentRepeat
                        .addToSecondEnding((Bar) currentState.astStack.pop());
                break;
            default:
                throw new ParseException("ERROR: Invalid n-th repeat state");
            }
            currentVoice.add(currentState.currentRepeat);
        } else {
            Bar top = (Bar) currentState.astStack.pop();
            if (top.getElements().size() > 0) {
                currentVoice.add(top);
            }
        }
        addAllToCurrentVoice();
        music.addVoice(currentVoice.getName(), currentVoice);
        return this.music;
    }

    private void addAllToCurrentVoice() {
        for (Bar b : currentState.barList) {
            if (b.getElements().size() == 0) {
                continue;
            }
            currentVoice.add(b);
        }
        currentState.barList.clear();
    }

    /**
     * takes current ParsingState and modifies the note with accidentals.
     * Inserts note into top of stack whether it be chord, tuplet, or bar
     * 
     * @throws ParseException
     */
    private void formNote() throws ParseException {
        if (currentState.currentNote != null) {
            if (currentState.hasAccidental) {
                if (currentState.currentNote.getPitch() == null) {
                    throw new ParseException(
                            "ERROR: Rests cannot have accidental modifiers.");
                }
                Stack<MusicalElementContainer> mecStack = new Stack<MusicalElementContainer>();
                while (currentState.astStack.peek().getMusicalType() != MusicalType.BAR
                        && currentState.astStack.peek().getMusicalType() != MusicalType.TUPLET) {
                    mecStack.push(currentState.astStack.pop());
                }
                currentState.astStack.peek().add(
                        new Accidental(currentState.currentNote.getPitch(),
                                currentState.currentAccidental));
                while (!mecStack.empty()) {
                    currentState.astStack.push(mecStack.pop());
                }
            }
            if (currentState.currentNote.isRest()) {
                currentState.astStack.peek().add(
                        new Rest(currentState.currentNote.getLength()));
            } else {
                currentState.astStack.peek().add(currentState.currentNote);
            }
            currentState.currentNote = null;
            currentState.currentAccidental = 0;
            currentState.hasAccidental = false;
            currentState.fractionBarProcessed = false;
        }

        if (currentState.astStack.size() > 0
                && currentState.astStack.peek().getMusicalType() == MusicalType.TUPLET) {
            Tuplet tuplet = (Tuplet) currentState.astStack.peek();
            if (tuplet.getCurrentNumNotes() == tuplet.getSize()) {
                currentState.astStack.pop();
                currentState.astStack.peek().add(tuplet);
            }
        }
    }
}
