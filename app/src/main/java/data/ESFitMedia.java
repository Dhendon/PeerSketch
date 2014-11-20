package data;

/**
 * Created by davidhendon on 11/19/14.
 */
public class ESFitMedia {
    private String sampleName;
    private int trackNumber;
    private double startLocation;
    private double endLocation;

    public ESFitMedia(String sampleName, int trackNumber, double startLocation, double endLocation) {
        this.sampleName = sampleName;
        this.trackNumber = trackNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    // TODO: Implement this method
    public boolean isValid() {
        // Check sample name is correct / exists
        // make sure track exists
        if (startLocation > endLocation) {
            return false;
        } else if (startLocation < 0 || endLocation < 0) {
            return false;
        } else { // TODO: Add max song length.
            return true;
        }
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public double getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(double startLocation) {
        this.startLocation = startLocation;
    }

    public double getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(double endLocation) {
        this.endLocation = endLocation;
    }

}
