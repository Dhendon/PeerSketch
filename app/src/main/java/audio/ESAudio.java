package audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import data.ESFitMedia;
import data.ESMakeBeat;
import data.ESSetEffect;
import data.ForLoop;
import data.GroupObject;
import data.IfStatement;
import data.Section;
import data.Util;

/**
 * Created by davidhendon on 11/16/14.
 */
public class ESAudio extends Thread {
    // TODO: recreate ES Audio functionality using MediaPlayer
    public static final String TAG = "esaudio";
    // This is in response to the imperfect scheduler timing.
    // TODO: replace this with noisy tick readings
    private static final int TICK_MS = 10;

    /**
     * @param fitMedia
     * @return Returns true when the section can be successfully played, false otherwise.
     */
    public static boolean play(ESFitMedia fitMedia, Context context, int tempoBPM,
                               int phraseLength) {
        if (fitMedia == null) {
            Log.i(TAG, "play - fitMedia is null");
            fitMedia = new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0.5, 1);
            //TODO: make this less jank
            //return false;
        }
        if (context == null) {
            Log.i(TAG, "play - context is null");
            return false;
        }
        if (!fitMedia.isValid()) {
            Log.i(TAG, "play - fitMedia is invalid, fitmedia=" + fitMedia.toString());
            return false;
        }

        int sampleId = Util.getSampleIDFromName(fitMedia.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + fitMedia.getSampleName());
            return false;
        }

        //final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        Log.i(TAG, "Created MediaPlayer, duration: " + mediaPlayer.getDuration());

        // Schedule when it needs to be played
        final int startTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getStartLocation(),
                tempoBPM, phraseLength);
        final int endTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getEndLocation(),
                tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;
        // TODO: Add in for loops / conditionals affecting the playing of the song.
        // Play it

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Log.i(TAG, "MediaPlayer is playing, stopping it");
        }
            /*
             Ignore the error message: Should have subtitle controller already set
             it's an artifact of MediaPlayer's programming: http://goo.gl/Gmb1l4 for more info
             */
        CountDownTimer playTimer = new CountDownTimer(endTimeMilliseconds,
                TICK_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!mediaPlayer.isPlaying() && millisUntilFinished <= durationMilliseconds) {
                    if (startTimeMilliseconds > 0) {
                        mediaPlayer.seekTo(startTimeMilliseconds);
                        Log.i(TAG, "MediaPlayer seeking to " + startTimeMilliseconds + "ms");
                    }
                    mediaPlayer.start();
                    Log.i(TAG, "MediaPlayer starting at " +
                            (endTimeMilliseconds - millisUntilFinished) + "ms");
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.reset();
                mediaPlayer.release();
                Log.i(TAG, "MediaPlayer finished after " + durationMilliseconds + "ms");
            }
        }.start();
        return true;
    }

    public static boolean play(Section section, Context context) {
        if (!section.isValid()) {
            Log.i(TAG, "Attempted to play invalid section, name:" + section.getName());
            return false;
        }

        for (GroupObject object : section.getOrderedObjects()) {
            if (object.getClass() == ForLoop.class) {
                ForLoop forLoop = (ForLoop) object;
                for (GroupObject contained : forLoop.getOrderedObjects()) {
                    if (contained.getClass() == ESFitMedia.class) {
                        ESFitMedia fitMedia = (ESFitMedia) contained;
                        List<Pair<Double, Double>> playTimes =
                                singleFitMediaPlayFromForLoop(forLoop);
                        playThenPauseThenSeekAllPairs(playTimes, context, fitMedia,
                                section.getTempoBPM(), section.getPhraseLengthMeasures());
                    } else if (contained.getClass() == ESMakeBeat.class) {
                        Log.i(TAG, "Attempted to play makeBeat -- need to implement!");
                    } else if (contained.getClass() == ESSetEffect.class) {
                        Log.i(TAG, "Attempted to play with setEffect -- need to implement!");
                    } else if (contained.getClass() == ForLoop.class) {
                        Log.i(TAG, "Loop again!");
                    } else if (contained.getClass() == IfStatement.class) {
                        Log.i(TAG, "Need to implement if statement logic here");
                    }
                }
            }
        }
        // TODO: Add in logic for ifStatements, forLoops, makeBeats and setEffects
        for (ESFitMedia fitMedia : section.getFitMedias()) {
            //localPlay(fitMedia, songBPM, songPhraseLength);
            ESAudio.play(fitMedia, context, section.getTempoBPM(),
                    section.getPhraseLengthMeasures());
        }

        return true;
    }

    /**
     * Calculates a list of when a for loop needs to play a sample
     *
     * @param forLoop
     * @return
     */
    public static List<Pair<Double, Double>> singleFitMediaPlayFromForLoop(ForLoop forLoop) {
        char operand = forLoop.getOperand();
        double amount = forLoop.getAmount();
        // for var in range() --> fitMedia(var, var (operand) amount)
        if (operand != '*' && operand != '/' && operand != '+' && operand != '%') {
            Log.i(TAG, "Invalid Operand: " + operand);
            return null;
        }
        if (operand == '/' && amount > 1) {
            Log.i(TAG, "Attempted to divide by too small a number:" + amount);
            return null;
        }
        List<Pair<Double, Double>> measures = new ArrayList<Pair<Double, Double>>();
        // Each case corresponds to a different operand and generates a corresponding set of pairs.
        if (operand == '+') {
            for (double i = forLoop.getStart(); i < forLoop.getEnd(); i += forLoop.getStepSize()) {
                measures.add(new Pair<Double, Double>(i, i + amount));
            }
        } else if (operand == '*') {
            for (double i = forLoop.getStart(); i < forLoop.getEnd(); i += forLoop.getStepSize()) {
                measures.add(new Pair<Double, Double>(i, i * amount));
            }
        } else if (operand == '/') {
            for (double i = forLoop.getStart(); i < forLoop.getEnd(); i += forLoop.getStepSize()) {
                measures.add(new Pair<Double, Double>(i, i / amount));
            }
        } else if (operand == '%') {
            for (double i = forLoop.getStart(); i < forLoop.getEnd(); i += forLoop.getStepSize()) {
                measures.add(new Pair<Double, Double>(i, i % amount));
            }
        }

        // TODO(hendon): Allow this to handle more cases.
        // TODO(hendon): Incorporate out of bounds error checking.
        return measures;
    }

    public static void playThenPauseThenSeekAllPairs(List<Pair<Double, Double>> measurePairs,
                                                     final Context context, final ESFitMedia fitMedia,
                                                     final int tempoBPM, final int phraseLength) {
        double previousStartMeasure = 0;
        for (Pair<Double, Double> pair : measurePairs) {
            double startMeasure = pair.first;
            double endMeasure = pair.second;
            fitMedia.setStartLocation(startMeasure);
            fitMedia.setEndLocation(endMeasure);
            long endTimeMilliseconds = Util.getLocationTimeMS(startMeasure, tempoBPM, phraseLength)
                    - Util.getLocationTimeMS(previousStartMeasure, tempoBPM, phraseLength);
            // Waits the difference between the two start times to begin playing.
            CountDownTimer playTimer = new CountDownTimer(endTimeMilliseconds,
                    TICK_MS) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    play(fitMedia, context, tempoBPM, phraseLength);
                }
            }.start();
        }
    }

    public static void executeForLoop(ForLoop forLoop, Section section, Context context) {
        for (GroupObject contained : forLoop.getOrderedObjects()) {
            if (contained.getClass() == ESFitMedia.class) {
                ESFitMedia fitMedia = (ESFitMedia) contained;
                List<Pair<Double, Double>> playTimes =
                        singleFitMediaPlayFromForLoop(forLoop);
                playThenPauseThenSeekAllPairs(playTimes, context, fitMedia,
                        section.getTempoBPM(), section.getPhraseLengthMeasures());
            } else if (contained.getClass() == ESMakeBeat.class) {
                Log.i(TAG, "Attempted to play makeBeat -- need to implement!");
            } else if (contained.getClass() == ESSetEffect.class) {
                Log.i(TAG, "Attempted to play with setEffect -- need to implement!");
            } else if (contained.getClass() == ForLoop.class) {
                executeForLoop((ForLoop) contained, section, context);
            } else if (contained.getClass() == IfStatement.class) {
                Log.i(TAG, "Need to implement if statement logic here");
            }
        }
    }

    public static boolean play(ESMakeBeat makeBeat, Context context, int tempoBPM,
                               int phraseLength) {
        if (makeBeat == null) {
            Log.i(TAG, "play - makeBeat is null");
            makeBeat = new ESMakeBeat("", 0, 0, Util.DEFAULT_BEATS[0]);
            //makeBeat = new ESMakeBeat(Util.DEFAULT_SAMPLES[1], 0, 0.5, 1);
            //TODO: make this less jank
            //return false;
        }
        if (context == null) {
            Log.i(TAG, "play - context is null");
            return false;
        }
        if (!makeBeat.isValid()) {
            Log.i(TAG, "play - makeBeat is invalid, fitmedia=" + makeBeat.toString());
            return false;
        }

        int sampleId = Util.getSampleIDFromName(makeBeat.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + makeBeat.getSampleName());
            return false;
        }
        Pair<List<Integer>, List<Pair<Double, Double>>> restsAndMeasures =
                restsAndMeasuresFromMakeBeat(makeBeat);
        List<Integer> rests = restsAndMeasures.first;
        List<Pair<Double, Double>> measures = restsAndMeasures.second;
        //final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        Log.i(TAG, "Created MediaPlayer, duration: " + mediaPlayer.getDuration());

        // Schedule when it needs to be played
        final int startTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation(),
                tempoBPM, phraseLength);
        // TODO: Add in the end time
        final int endTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation() + 16 * TICK_MS,
                tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;
        // TODO: Add in for loops / conditionals affecting the playing of the song.
        // Play it

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Log.i(TAG, "MediaPlayer is playing, stopping it");
        }
            /*
             Ignore the error message: Should have subtitle controller already set
             it's an artifact of MediaPlayer's programming: http://goo.gl/Gmb1l4 for more info
             */
        CountDownTimer playTimer = new CountDownTimer(endTimeMilliseconds,
                TICK_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!mediaPlayer.isPlaying() && millisUntilFinished <= durationMilliseconds) {
                    if (startTimeMilliseconds > 0) {
                        mediaPlayer.seekTo(startTimeMilliseconds);
                        Log.i(TAG, "MediaPlayer seeking to " + startTimeMilliseconds + "ms");
                    }
                    mediaPlayer.start();
                    Log.i(TAG, "MediaPlayer starting at " +
                            (endTimeMilliseconds - millisUntilFinished) + "ms");
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.reset();
                mediaPlayer.release();
                Log.i(TAG, "MediaPlayer finished after " + durationMilliseconds + "ms");
            }
        }.start();
        return true;
    }

    /**
     * Turns a makeBeat into a list of played measures by processing the beatPattern
     *
     * @param makeBeat
     * @return
     */
    // TODO: Find a more clever way to account for rests.
    public static Pair<List<Integer>, List<Pair<Double, Double>>> restsAndMeasuresFromMakeBeat(
            ESMakeBeat makeBeat) {
        List<Pair<Double, Double>> measures = new ArrayList<Pair<Double, Double>>();
        List<Integer> rests = new ArrayList<Integer>();
        String beatPattern = makeBeat.getBeatPattern();
        char[] beats = beatPattern.toCharArray();
        int beatCount = 0;
        int restCount = 0;
        for (char beat : beats) {
            if (beat == '0') {
                rests.add(restCount);
                // Play first 1/16th note
                measures.add(new Pair<Double, Double>(0.0, beatCount + 0.0));
                // Reset counts
                beatCount = 0;
                restCount = 0;
            } else if (beat == '-') {
                beatCount = 0;
                restCount++;
            } else if (beat == '+') {
                restCount = 0;
                beatCount++;
            } else {
                Log.i(TAG, "Unrecognized character in beatPattern: " + beat);
            }
        }
        return new Pair<List<Integer>, List<Pair<Double, Double>>>(rests, measures);
    }
}


