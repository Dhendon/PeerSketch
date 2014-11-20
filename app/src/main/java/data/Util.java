package data;

/**
 * Created by davidhendon on 11/20/14.
 */
public class Util {
    //TODO: determine which are feasible to implement
    static final String[] EFFECTS = {"PITCHSHIFT", "VOLUME", "EQ3BAND"};
    static final String[][] EFFECT_PARAMETERS = {{"SHIFT", "BYPASS"}, {"GAIN", "BYPASS"},
            {"LOWGAIN", "LOWFREQ", "MIDGAIN", "MIDFREQ", "HIGHGAIN", "HIGHFREQ"}};
    static final String[] CONDITIONALS = {"<", "<=", "=", "!=", ">=", ">"};
    // TODO: Update these samples to the actual ones we'll be using.
    String[] DefaultSamples = {"DUBSTEP", "EIGHTBIT", "ELECTRO", "HIPHOP", "HOUSE", "TECHNO"};

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

    // TODO: Include default beat patterns?

}
