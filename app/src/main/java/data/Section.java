package data;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhendon on 10/28/14.
 */
public class Section {
    private List<ForLoop> forLoops;
    private List<IfStatement> ifStatements;
    private List<ESSetEffect> effects;
    private List<ESFitMedia> fitMedias;
    private List<ESMakeBeat> makeBeats;
    private List<Pair<Integer, Integer>> activeMeasures; // TODO: update this when parameters are changed
    private String sample; // TODO: replace with MediaPlayer file

    public Section(String sample) {
        this.sample = sample;
        forLoops = new ArrayList<ForLoop>();
        ifStatements = new ArrayList<IfStatement>();
        effects = new ArrayList<ESSetEffect>();
        fitMedias = new ArrayList<ESFitMedia>();
        makeBeats = new ArrayList<ESMakeBeat>();
        activeMeasures = new ArrayList<Pair<Integer, Integer>>();
    }


    // TODO: Update active measures according to newly added functions

    public void add(ForLoop forLoop, int location) {
        if (validLocation(location, forLoops)) {
            forLoops.add(location, forLoop);
        } else {
            forLoops.add(forLoop);
        }
    }

    /**
     * Adds an item to the section
     *
     * @param ifStatement newly user created if statement
     * @param location    this determines the order in which to show the object on the screen
     */
    public void add(IfStatement ifStatement, int location) {
        if (validLocation(location, ifStatements)) {
            ifStatements.add(location, ifStatement);
        } else {
            ifStatements.add(ifStatement);
        }
    }

    public void add(ESSetEffect effect, int location) {
        if (validLocation(location, effects)) {
            effects.add(location, effect);
        } else {
            effects.add(effect);
        }
    }

    public void add(ESFitMedia fitMedia, int location) {
        if (validLocation(location, fitMedias)) {
            fitMedias.add(location, fitMedia);
        } else {
            fitMedias.add(fitMedia);
        }
    }

    public void add(ESMakeBeat makeBeat, int location) {
        if (validLocation(location, makeBeats)) {
            makeBeats.add(location, makeBeat);
        } else {
            makeBeats.add(makeBeat);
        }
    }

    private boolean validLocation(int location, List<?> list) {
        return location <= list.size() && location >= 0;
    }

    // TODO: Implement this method
    public boolean isValid() {
        return true;
    }

    public List<ForLoop> getForLoops() {
        return forLoops;
    }

    public List<IfStatement> getIfStatements() {
        return ifStatements;
    }

    public List<ESSetEffect> getEffects() {
        return effects;
    }

    public List<ESFitMedia> getFitMedias() {
        return fitMedias;
    }

    public List<ESMakeBeat> getMakeBeats() {
        return makeBeats;
    }

    public List<Pair<Integer, Integer>> getActiveMeasures() {
        return activeMeasures;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

}
