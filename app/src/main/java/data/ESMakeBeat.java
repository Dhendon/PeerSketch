package data;

/**
 * Created by davidhendon on 11/19/14.
 */
public class ESMakeBeat {
    private String sampleName;
    private int track;
    private double startMeasure;
    private String beatPattern;

    public ESMakeBeat(String sampleName, int track, double startMeasure, String beatPattern) {
        this.sampleName = sampleName;
        this.track = track;
        this.startMeasure = startMeasure;
        this.beatPattern = beatPattern;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public double getStartMeasure() {
        return startMeasure;
    }

    public void setStartMeasure(double startMeasure) {
        this.startMeasure = startMeasure;
    }

    public String getBeatPattern() {
        return beatPattern;
    }

    public void setBeatPattern(String beatPattern) {
        this.beatPattern = beatPattern;
    }
}
