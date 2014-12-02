package data;

/**
 * Created by davidhendon on 11/20/14.
 */
public class Util {
    // TODO: Update these samples to the actual ones we'll be using.
    public static final String[] DEFAULT_SAMPLES = {"DUBSTEP", "EIGHTBIT", "ELECTRO", "HIPHOP",
            "HOUSE", "TECHNO"};
    public static final int DEFAULT_TEMPO_BPM = 120;
    public static final int DEFAULT_PHRASE_LENGTH = 8;
    //TODO: determine which are feasible to implement
    static final String[] EFFECTS = {"PITCHSHIFT", "VOLUME", "EQ3BAND"};
    static final String[][] EFFECT_PARAMETERS = {{"SHIFT", "BYPASS"}, {"GAIN", "BYPASS"},
            {"LOWGAIN", "LOWFREQ", "MIDGAIN", "MIDFREQ", "HIGHGAIN", "HIGHFREQ"}};
    static final String[] CONDITIONALS = {"<", "<=", "=", "!=", ">=", ">"};

    // TODO: Update these effect indexes with the EFFECTS array and add mapping to settings
    public interface Effects {
        public static final int PITCHSHIFT = 0;
        public static final int VOLUME = 1;
        public static final int EQUALIZER = 2;
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
}
