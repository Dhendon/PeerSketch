package data;

import java.io.Serializable;

/**
 * Created by davidhendon on 11/19/14.
 */
public class ESMakeBeat implements Serializable, GroupObject {
    private static final long serialVersionUID = 1L;
    private String sampleName;
    private int sectionNumber;
    private double startLocation;
    private String beatPattern;

    public ESMakeBeat(String sampleName, int sectionNumber, double startLocation, String beatPattern) {
        this.sampleName = sampleName;
        this.sectionNumber = sectionNumber;
        this.startLocation = startLocation;
        this.beatPattern = beatPattern;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public double getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(double startLocation) {
        this.startLocation = startLocation;
    }

    public String getBeatPattern() {
        return beatPattern;
    }

    public void setBeatPattern(String beatPattern) {
        this.beatPattern = beatPattern;
    }

    public boolean isValid() {
        return startLocation > 0 && beatpatternIsValid(beatPattern);
    }

    public boolean beatpatternIsValid(String beatPattern) {
        String validRegexp = "(0*-*)*"; //TODO: Add in the plus sign
        return beatPattern.length() == Util.BEATS_IN_MEASURE && beatPattern.matches(validRegexp);

    }
}
