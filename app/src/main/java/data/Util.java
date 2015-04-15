package data;

import android.util.Log;

import gatech.adam.peersketch.R;

/**
 * Created by davidhendon on 11/20/14.
 */
public class Util {
    public static final int[] DEFAULT_SAMPLE_IDS = {R.raw.synth_harp_1, R.raw.clap_1,
            R.raw.drumpad_1, R.raw.electro_1, R.raw.electro_2, R.raw.electro_3,
            R.raw.hi_hats_1, R.raw.percussion_1, R.raw.percussion_2, R.raw.organ_1};
    public static final String[] DEFAULT_BEATS = {"––––0+++–––0+++", "0+++0+++0+0+0+",
            "0+0+––––0+0+–0++", "0++0++0+–––0++0+", "1–001–00–0–01–00"};
    public static final String[] DEFAULT_SAMPLES = {"Synth Harp", "Clap", "Drumpad", "Electro 1",
            "Electro 2", "Electro 3", "Hi Hats", "Percussion 1", "Percussion 2", "Organ"};
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
        public static final int SYNTH_HARP = 0;
        public static final int CLAP = 1;
        public static final int DRUMPAD = 2;
        public static final int ELECTRO1 = 3;
        public static final int ELECTRO2 = 4;
        public static final int ELECTRO3 = 5;
        public static final int HI_HATS = 6;
        public static final int PERCUSSION1 = 7;
        public static final int PERCUSSION2 = 8;
        public static final int ORGAN = 9;
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
