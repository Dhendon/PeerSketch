package data;

import android.util.Log;

import gatech.adam.peersketch.R;

/**
 * Created by davidhendon on 11/20/14.
 */
public class Util {
    public static final int[] DEFAULT_SAMPLE_IDS = {R.raw.y07_wah_guitar,
            R.raw.hiphop_basssub_001,
            R.raw.hiphop_dustybassline_001,
            R.raw.hiphop_funkbass_001,
            R.raw.hiphop_funknass_002,
            R.raw.hiphop_solomooglead_001,
            R.raw.hiphop_synthplucklead_001,
            R.raw.house_deep_sinepad_001,
            R.raw.techno_clublead_001,
            R.raw.techno_polylead_001,
            R.raw.techno_polylead_007,
            R.raw.dubstep_pad_004,
            R.raw.hiphop_dustygroove_003,
            R.raw.hiphop_stomp_beat_part_001,
            R.raw.hiphop_traphop_beat_002,
            R.raw.hiphop_traphop_beat_004,
            R.raw.hiphop_traphop_beat_006,
            R.raw.y58_clap_1,
            R.raw.techno_snareroll_004,
            R.raw.y58_drumpad_1,
            R.raw.hiphop_hihat_roll_003,
            R.raw.hiphop_hihat_roll_004,
            R.raw.hiphop_hihat_roll_006,
            R.raw.y58_percussion_1,
            R.raw.y58_percussion_2,
            R.raw.y07_strings,
            R.raw.y07_horns};
    public static final String[] DEFAULT_BEATS = {"––––0+++–––0+++", "0+++0+++0+0+0+",
            "0+0+––––0+0+–0++", "0++0++0+–––0++0+", "1–001–00–0–01–00"};
    public static final String[] DEFAULT_SAMPLES = {
            "Wah Guitar",
            "Sub Bass",
            "Hip-Hop Bass",
            "Funk Bass 1",
            "Funk Bass 2",
            "Synth Lead 1",
            "Synth Lead 2",
            "Deep Synth Pad",
            "Techno Synth 1",
            "Techno Synth 2",
            "Techno Synth 3",
            "Dubstep Pad",
            "Hip-Hop Drum Groove",
            "Stomp Beat",
            "Trap Beat 1",
            "Trap Beat 2",
            "Trap Beat 3",
            "Clap",
            "Snare Roll",
            "Drum Pad",
            "Hi-Hat Roll 1",
            "Hi-Hat Roll 2",
            "Hi-Hat Roll 3",
            "Percussion 1",
            "Percussion 2",
            "Strings",
            "Horns"};
    public static final int DEFAULT_TEMPO_BPM = 120;
    public static final int DEFAULT_PHRASE_LENGTH = 8;
    public static final int DROP_LOCATION = 0; // TODO: Remove this once drag and drop implemented
    public static final boolean DEBUG_MODE = true;
    public static final String[] CONDITIONALS = {"<", "<=", "=", "!=", ">=", ">"};
    public static final int BEATS_IN_MEASURE = 16;
    //TODO: determine which are feasible to implement
    public static final String[] EFFECT_NAMES = {"REVERB", "VOLUME", "EQ3BAND", "PITCHSHIFT"};
    public static final String[][] EFFECT_PARAMETERS = {{"SHIFT", "BYPASS"}, {"GAIN", "BYPASS"},
            {"LOWGAIN", "LOWFREQ", "MIDGAIN", "MIDFREQ", "HIGHGAIN", "HIGHFREQ"}};
    public static final String OPERANDS[] = {"+", "-", "*", "/", "%", "none"};
    public final static String ES_LOGIN_BASE_URL = "http://earsketch.gatech.edu/EarSketchWS";
    // TODO: Update these samples to the actual ones we'll be using.
    private static String TAG = "util";

    public static int getLocationTimeMS(double location, int tempoBPM, int phraseLength) {
        // measures --> beats
        final int MINUTES_TO_MS = 60000;
        double numBeats = location * phraseLength;
        // BPM --> minutes --> milliseconds
        return (int) (numBeats / tempoBPM * MINUTES_TO_MS);
    }

    public static int getSampleIDFromName(String sampleName) {
        for (int i = 0; i < Util.DEFAULT_SAMPLES.length; i++) {
            if (Util.DEFAULT_SAMPLES[i].equals(sampleName)) {
                return Util.DEFAULT_SAMPLE_IDS[i];
            }
        }
        Log.i(TAG, "Attempted to play Unknown Sample:" + sampleName);
        return -1;
    }

    public interface DefaultSamples {
        public static final int SUB_BASS = 0;
        public static final int HIPHOP_BASS = 1;
        public static final int FUNK_BASS1 = 2;
        public static final int FUNK_BASS2 = 3;
        public static final int SYNTH_LEAD1 = 4;
        public static final int SYNTH_LEAD2 = 5;
        public static final int DEEP_SYNTH_PAD = 6;
        public static final int TECHNO_SYNTH1 = 7;
        public static final int TECHNO_SYNTH2 = 8;
        public static final int TECHNO_SYNTH3 = 9;
        public static final int DUBSTEP_PAD = 10;
        public static final int HIPHOP_DRUM_GROOVE = 11;
        public static final int STOMP_BEAT = 12;
        public static final int TRAP_BEAT1 = 13;
        public static final int TRAP_BEAT2 = 14;
        public static final int TRAP_BEAT3 = 15;
        public static final int CLAP = 16;
        public static final int SNARE_ROLL = 17;
        public static final int DRUM_PAD = 18;
        public static final int HIHAT_ROLL1 = 19;
        public static final int HIHAT_ROLL2 = 20;
        public static final int HIHAT_ROLL3 = 21;
        public static final int PERCUSSION1 = 22;
        public static final int PERCUSSION2 = 23;
        public static final int STRINGS = 24;
        public static final int HORNS = 25;
    }

    // TODO: Update these effect indexes with the EFFECTS array and add mapping to settings
    public interface Effects {
        public static final int REVERB = 0;
        public static final int VOLUME = 1;
        public static final int EQUALIZER = 2;
        public static final int PITCHSHIFT = 3;
    }

    public interface EffectParameters {
        // PITCHSHIFT
        public static final int PS_SHIFT = 0;
        public static final int PS_BYPASS = 1;
        // VOLUME
        public static final int V_GAIN = 0;
        public static final int V_BYPASS = 1;
        // EQ3BAND
        public static final int EQ_LOWGAIN = 0;
        public static final int EQ_LOWFREQ = 1;
        public static final int EQ_MIDGAIN = 2;
        public static final int EQ_MIDFREQ = 3;
        public static final int EQ_HIGHGAIN = 4;
        public static final int EQ_HIGHFREQ = 5;
    }

    public interface Conditionals {
        static final public int LESSTHAN = 0;
        static final public int LESSTHANOREQUAL = 1;
        static final public int EQUAL = 2;
        static final public int NOTEQUAL = 3;
        static final public int GREATERTHANOREQUAL = 4;
        static final public int GREATERTHAN = 5;
    }

    public interface BundleKeys {
        static final String FITMEDIA = "fitmedia";
        static final String MAKEBEAT = "makebeat";
        static final String SETEFFECT = "seteffect";
        static final String FORLOOP = "forloop";
        static final String GROUP = "group";
        static final String IFSTATEMENT = "ifstatement";
        static final String SECTION = "section";
        static final String SECTION_NAME = "section-name";
        static final String SECTION_NUMBER = "section-number";
        static final String SONG = "song";
        static final String SONGLIBRARY = "songlibrary";
    }    // TODO: Include default beat patterns?

    public interface ForLoopChoice {
        final int UNASSIGNED = -1;
        final int FITMEDIA = 0;
        final int MAKEBEAT = 1;
        final int FORLOOP = 2;
    }

}
