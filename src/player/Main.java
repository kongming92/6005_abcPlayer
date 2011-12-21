package player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import player.Parser.ParseException;
import sound.SequencePlayer;

import music.ast.Music;
import music.visitor.MusicalWellFormednessVisitor;
import music.visitor.OptimalTicksPerQuarterNoteVisitor;
import music.visitor.TranslateToSequenceVisitor;

/**
 * Main entry point of your application.
 */
public class Main {

    /**
     * Plays the input file using Java MIDI API and displays header information
     * to the standard output stream.
     * 
     * <p>
     * Your code <b>should not</b> exit the application abnormally using
     * System.exit()
     * </p>
     * 
     * @param file
     *            the name of input abc file
     */
    public static void play(String file) {
        // Load in the file
        StringBuffer data = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            int read = 0;
            char[] buffer = new char[1024];
            while ((read = in.read(buffer)) != -1) {
                data.append(String.valueOf(buffer, 0, read));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + file + "\" not found");
            return;
        } catch (IOException ioe) {
            System.err.println("Something horrible happened while loading \""
                    + file + "\"");
            return;
        }
        // Now, create the parser and lexer
        Lexer lexer = new Lexer(data.toString());
        Parser parser = new Parser(lexer.iterator());
        Music music = null;
        // Get the Music AST
        try {
            music = parser.getMusic();
        } catch (ParseException e) {
            System.err.println("Error: Could not parse file \"" + file + "\"");
            System.err.println(e.getMessage());
            return;
        }

        // Print some useful information
        System.out.println("Index Number: " + music.getIndexNumber());
        System.out.println("Title: " + music.getTitle());
        System.out.println("Composer: " + music.getComposer());
        System.out.println("Default Length: " + music.getDefaultLength());
        System.out.println("Meter: " + music.getMeter());
        System.out.println("Tempo: " + music.getTempo());
        System.out.println("Key: " + music.getKeySignature());
        // See how well-formed this piece is.
        System.out.println("Checking musical piece for well-formedness....");
        MusicalWellFormednessVisitor mwfv = new MusicalWellFormednessVisitor();
        music.accept(mwfv);
        String errors = mwfv.getErrors();
        if (errors.length() > 0) {
            System.out.print(errors);
        }
        System.out.println("Going to try and play, even if errors occured...");
        
        // Run a Visitor on the Music to find the LCM, so we can set ticks per
        System.out.println("Computing ticks per quarter note...");
        OptimalTicksPerQuarterNoteVisitor otpqnv = new OptimalTicksPerQuarterNoteVisitor();
        int ticksPerQuarterNote = music.accept(otpqnv);
        // Now that we have the actual Music, run the Visitor on it to populate
        // a SequencePlayer
        SequencePlayer sp = null;
        try {
            sp = new SequencePlayer(music.getTempo(), ticksPerQuarterNote);
        } catch (Exception e) {
            System.err
                    .println("Could not initialize MIDI subsystem. Perhaps there is a problem with your computer?");
        }

        TranslateToSequenceVisitor ttsv = new TranslateToSequenceVisitor(sp,
                ticksPerQuarterNote);
        try {
            music.accept(ttsv);
        } catch (Throwable t) {
            System.err.println("Could not convert ABC to MIDI. Check the file for impossible pitches?");
            System.err.println("Technical details:");
            System.err.println(t.getMessage());
            return;
        }

        // Finally, play!
        try {
            System.out.print("Playing...");
            sp.play();
            System.out.println("done!");
        } catch (MidiUnavailableException e) {
            System.err
                    .println("Failed to play MIDI sounds. Perhaps there is a problem with your computer?");
        }
    }
}
