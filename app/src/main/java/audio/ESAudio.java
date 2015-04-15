package audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.EnvironmentalReverb;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import data.ContainerObject;
import data.ESFitMedia;
import data.ESMakeBeat;
import data.ESSetEffect;
import data.ForLoop;
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
    private static final int TICK_MS = 20;
    private static final double BEAT_LENGTH = 1 / 16.0;
    private static List<CountDownTimer> playTimers = new ArrayList<CountDownTimer>();

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
                    Log.i(TAG, "Starting MediaPlayer at:" + System.currentTimeMillis());
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
        playTimers.add(playTimer);
        return true;
    }

    public static CountDownTimer getReadyMediaPlayerForPlay(ESFitMedia fitMedia, Context context, int tempoBPM,
                                                            int phraseLength) {
        if (fitMedia == null) {
            Log.i(TAG, "play - fitMedia is null");
            fitMedia = new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0.5, 1);
            //TODO: make this less jank
            //return false;
        }
        if (context == null) {
            Log.i(TAG, "play - context is null");
            return null;
        }
        if (!fitMedia.isValid()) {
            Log.i(TAG, "play - fitMedia is invalid, fitmedia=" + fitMedia.toString());
            return null;
        }

        int sampleId = Util.getSampleIDFromName(fitMedia.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + fitMedia.getSampleName());
            return null;
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
                    Log.i(TAG, "Starting MediaPlayer at:" + System.currentTimeMillis());
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
        };

        return playTimer;
    }

    public static boolean play(Section section, Context context) {
        if (!section.isValid()) {
            Log.i(TAG, "Attempted to play invalid section, name:" + section.getName());
            return false;
        }
        List<ESFitMedia> allFitMedias = new ArrayList<ESFitMedia>();
        List<ESMakeBeat> allMakeBeats = new ArrayList<ESMakeBeat>();
        for (ContainerObject object : section.getOrderedObjects()) {
            if (object.getClass() == ESMakeBeat.class) {
                ESMakeBeat makeBeat = (ESMakeBeat) object;
                //play(makeBeat, context, section.getTempoBPM(),section.getPhraseLengthMeasures());
                allMakeBeats.add(makeBeat);
            } else if (object.getClass() == ESFitMedia.class) {
                ESFitMedia fitMedia = (ESFitMedia) object;
                //play(fitMedia, context, section.getTempoBPM(), section.getPhraseLengthMeasures());
                allFitMedias.add(fitMedia);
            } else if (object.getClass() == ESSetEffect.class) {
                // TODO: Implement this.
            } else if (object.getClass() == ForLoop.class) {
                ForLoop forLoop = (ForLoop) object;
                for (ContainerObject contained : forLoop.getOrderedObjects()) {
                    if (contained.getClass() == ESFitMedia.class) {
                        ESFitMedia fitMedia = (ESFitMedia) contained;
                        List<ESFitMedia> fitMedias = getFitMediaListFromForLoop(fitMedia, forLoop);
                        allFitMedias.addAll(fitMedias);
                    } else if (contained.getClass() == ESMakeBeat.class) {
                        ESMakeBeat makeBeat = (ESMakeBeat) contained;
                        List<ESMakeBeat> makeBeats = getMakeBeatListFromForLoop(makeBeat, forLoop);
                        allMakeBeats.addAll(makeBeats);
                    } else if (contained.getClass() == ESSetEffect.class) {
                        // TODO: check for this
                    } else if (contained.getClass() == ForLoop.class) {
                        Log.i(TAG, "Loop again!");
                    } else if (contained.getClass() == IfStatement.class) {
                        Log.i(TAG, "Need to implement if statement logic here");
                    }
                }
            }
        }

        List<CountDownTimer> timers = new ArrayList<CountDownTimer>();
        // TODO: Add setEffect
        for (ESFitMedia fitMedia : allFitMedias) {
            timers.add(getReadyMediaPlayerForPlay(fitMedia, context, section.getTempoBPM(),
                    section.getPhraseLengthMeasures()));
        }
        for (ESMakeBeat makeBeat : allMakeBeats) {
            timers.add(getReadyMediaPlayerForPlay(makeBeat, context,
                    section.getTempoBPM(), section.getPhraseLengthMeasures()));
        }

        for (CountDownTimer timer : timers) {
            Log.i(TAG, "Queuing timer at:" + System.currentTimeMillis());
            timer.start();
        }
        return true;
    }

    // Assumes no nested forLoops
    public static void play(ForLoop forLoop, Context context, int tempoBPM, int phraseLength) {
        if (forLoop.getOrderedObjects().isEmpty() || forLoop.getIterValues().isEmpty()) {
            return;
        }

        List<ESFitMedia> allFitMedias = new ArrayList<ESFitMedia>();
        List<ESMakeBeat> allMakeBeats = new ArrayList<ESMakeBeat>();

        for (ContainerObject contained : forLoop.getOrderedObjects()) {
            if (contained.getClass() == ESFitMedia.class) {
                ESFitMedia fitMedia = (ESFitMedia) contained;
                List<ESFitMedia> fitMedias = getFitMediaListFromForLoop(fitMedia, forLoop);
                allFitMedias.addAll(fitMedias);
            } else if (contained.getClass() == ESMakeBeat.class) {
                ESMakeBeat makeBeat = (ESMakeBeat) contained;
                List<ESMakeBeat> makeBeats = getMakeBeatListFromForLoop(makeBeat, forLoop);
                allMakeBeats.addAll(makeBeats);
            } else if (contained.getClass() == ESSetEffect.class) {
                // TODO: check for this
            } else if (contained.getClass() == ForLoop.class) {
                Log.i(TAG, "Loop again!");
            } else if (contained.getClass() == IfStatement.class) {
                Log.i(TAG, "Need to implement if statement logic here");
            }
        }

        List<CountDownTimer> timers = new ArrayList<CountDownTimer>();
        // TODO: Add setEffect
        for (ESFitMedia fitMedia : allFitMedias) {
            timers.add(getReadyMediaPlayerForPlay(fitMedia, context, tempoBPM,
                    phraseLength));
        }
        for (ESMakeBeat makeBeat : allMakeBeats) {
            timers.add(getReadyMediaPlayerForPlay(makeBeat, context,
                    tempoBPM, phraseLength));
        }

        for (CountDownTimer timer : timers) {
            Log.i(TAG, "Queuing timer at:" + System.currentTimeMillis());
            timer.start();
        }
    }

    /**
     * Calculates a list of when a for loop needs to play a sample
     *
     * @param forLoop
     * @return
     */
    public static List<Pair<Double, Double>> singleFitMediaPlayFromForLoop(ForLoop forLoop) {
        char operand = forLoop.getOperand();
        double amount = forLoop.getStepSize();
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
        for (ContainerObject contained : forLoop.getOrderedObjects()) {
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

    public static boolean play(ESFitMedia fitMedia, ESSetEffect setEffect,
                               Context context, int tempoBPM, int phraseLength) {
        if (fitMedia == null) {
            Log.i(TAG, "play - fitMedia is null");
            fitMedia = new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0.5, 1);
            //TODO: make this less jank
            //return false;
        }
        if (setEffect == null) {
            Log.i(TAG, "play - setEffect is null");
            //TODO: make this less jank too.
            return false;
        }
        if (context == null) {
            Log.i(TAG, "play - context is null");
            return false;
        }
        if (!fitMedia.isValid()) {
            Log.i(TAG, "play - fitMedia is invalid, fitmedia=" + fitMedia.toString());
            return false;
        }

        if (!setEffect.isValid()) {
            Log.i(TAG, "play - setEffect is invalid, seteffect=" + fitMedia.toString());
            return false;
        }
        int sampleId = Util.getSampleIDFromName(fitMedia.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + fitMedia.getSampleName());
            return false;
        }


        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        //final MediaPlayer mediaPlayer = attachESEffect(context, sampleId, setEffect);
        if (setEffect.getEffectIndex() == Util.Effects.REVERB) {
            int decayTime = 1000;
            short density = 500;
            short diffusion = 500;
            short roomLevel = 0;
            short reverbLevel = 500;
            short reflectionsDelay = 100;
            short reflectionsLevel = 100;
            short reverbDelay = 0;

            /*EnvironmentalReverb reverb = new EnvironmentalReverb(0, mediaPlayer.getAudioSessionId());
            reverb.setDecayTime(decayTime);
            reverb.setDensity(density);
            reverb.setDiffusion(diffusion);
            reverb.setReverbLevel(reverbLevel);
            reverb.setRoomLevel(roomLevel);
            reverb.setReflectionsDelay(reflectionsDelay);
            reverb.setReflectionsLevel(reflectionsLevel);
            reverb.setReverbDelay(reverbDelay);
            reverb.setEnabled(true);*/
            //EnvironmentalReverb overdrive = createOverdriveEffect();
            //mediaPlayer.attachAuxEffect(reverb.getId());
            //mediaPlayer.setAuxEffectSendLevel(1.0f);
            mediaPlayer.attachAuxEffect(EnvironmentalReverb.PARAM_DECAY_TIME);
        }

        // TODO: Handle effects in a robust way instead of encoding them
        Log.i(TAG, "Created MediaPlayer, duration: " + mediaPlayer.getDuration());

        // Schedule when it needs to be played
        final int startTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getStartLocation(),
                tempoBPM, phraseLength);
        final int endTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getEndLocation(),
                tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;

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
                    Log.i(TAG, "Starting MediaPlayer at:" + System.currentTimeMillis());
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
        playTimers.add(playTimer);
        return true;
    }

    public static CountDownTimer getReadyMediaPlayerForPlay(ESMakeBeat makeBeat, Context context, int tempoBPM,
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
            return null;
        }
        if (!makeBeat.isValid()) {
            Log.i(TAG, "play - makeBeat is invalid, makeBeat=" + makeBeat.toString());
            return null;
        }

        int sampleId = Util.getSampleIDFromName(makeBeat.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + makeBeat.getSampleName());
            return null;
        }
        //final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        //Log.i(TAG, "Created MediaPlayer, duration: " + mediaPlayer.getDuration());

        // Schedule when it needs to be played
        final int startTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation(),
                tempoBPM, phraseLength);
        // TODO: Add in the end time
        final int endTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation() +
                makeBeat.getBeatPattern().length() * BEAT_LENGTH, tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;

        final List<List<Integer>> playTimeMS = calcPlayTimesMSFromMakeBeat(makeBeat, tempoBPM,
                phraseLength);

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
                long currentTimeMS = endTimeMilliseconds - millisUntilFinished;
                //Pair<Integer, Integer> currentPairIndex;
                // This assumes a well constructed playTimeMS list.
                for (int i = 0; i < playTimeMS.size(); i++) {
                    // This assumes that the latency of this search isn't enough to ruin the timing.
                    if (currentTimeMS < playTimeMS.get(i).get(MakeBeatIndices.PLAY_START) + TICK_MS &&
                            currentTimeMS > playTimeMS.get(i).get(MakeBeatIndices.PLAY_START) - TICK_MS) {
                        //currentPairIndex = new Pair<Integer, Integer>(i, MakeBeatIndices.PLAY_START);
                        if (millisUntilFinished <= durationMilliseconds) {
                            mediaPlayer.pause();
                            if (startTimeMilliseconds > 0) {
                                mediaPlayer.seekTo(0);
                            }
                            mediaPlayer.start();
                            Log.i(TAG, "MediaPlayer starting at " + currentTimeMS + "ms");
                        }
                        break;
                    } else if (currentTimeMS < playTimeMS.get(i).get(MakeBeatIndices.REST_START)
                            + TICK_MS && currentTimeMS
                            > playTimeMS.get(i).get(MakeBeatIndices.REST_START) - TICK_MS) {
                        //currentPairIndex = new Pair<Integer, Integer>(i, MakeBeatIndices.REST_START);
                        if (millisUntilFinished <= durationMilliseconds) {
                            if (startTimeMilliseconds > 0) {
                                mediaPlayer.seekTo(0);
                            }
                            mediaPlayer.start();
                            Log.i(TAG, "MediaPlayer starting at " +
                                    (endTimeMilliseconds - millisUntilFinished) + "ms");
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.reset();
                mediaPlayer.release();
                Log.i(TAG, "MediaPlayer finished after " + durationMilliseconds + "ms");
            }
        };

        return playTimer;
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
            Log.i(TAG, "play - makeBeat is invalid, makeBeat=" + makeBeat.toString());
            return false;
        }

        int sampleId = Util.getSampleIDFromName(makeBeat.getSampleName());
        if (sampleId == -1) {
            Log.i(TAG, "play - sampleID is invalid, name=" + makeBeat.getSampleName());
            return false;
        }
        //final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        //Log.i(TAG, "Created MediaPlayer, duration: " + mediaPlayer.getDuration());

        // Schedule when it needs to be played
        final int startTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation(),
                tempoBPM, phraseLength);
        // TODO: Add in the end time
        final int endTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation() +
                makeBeat.getBeatPattern().length() * BEAT_LENGTH, tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;

        final List<List<Integer>> playTimeMS = calcPlayTimesMSFromMakeBeat(makeBeat, tempoBPM,
                phraseLength);

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
                long currentTimeMS = endTimeMilliseconds - millisUntilFinished;
                //Pair<Integer, Integer> currentPairIndex;
                // This assumes a well constructed playTimeMS list.
                for (int i = 0; i < playTimeMS.size(); i++) {
                    // This assumes that the latency of this search isn't enough to ruin the timing.
                    if (currentTimeMS < playTimeMS.get(i).get(MakeBeatIndices.PLAY_START) + TICK_MS &&
                            currentTimeMS > playTimeMS.get(i).get(MakeBeatIndices.PLAY_START) - TICK_MS) {
                        //currentPairIndex = new Pair<Integer, Integer>(i, MakeBeatIndices.PLAY_START);
                        if (millisUntilFinished <= durationMilliseconds) {
                            mediaPlayer.pause();
                            if (startTimeMilliseconds > 0) {
                                mediaPlayer.seekTo(0);
                            }
                            mediaPlayer.start();
                            Log.i(TAG, "MediaPlayer starting at " + currentTimeMS + "ms");
                        }
                        break;
                    } else if (currentTimeMS < playTimeMS.get(i).get(MakeBeatIndices.REST_START)
                            + TICK_MS && currentTimeMS
                            > playTimeMS.get(i).get(MakeBeatIndices.REST_START) - TICK_MS) {
                        //currentPairIndex = new Pair<Integer, Integer>(i, MakeBeatIndices.REST_START);
                        if (millisUntilFinished <= durationMilliseconds) {
                            if (startTimeMilliseconds > 0) {
                                mediaPlayer.seekTo(0);
                            }
                            Log.i(TAG, "Starting MediaPlayer at:" + System.currentTimeMillis());
                            mediaPlayer.start();
                            Log.i(TAG, "MediaPlayer starting at " +
                                    (endTimeMilliseconds - millisUntilFinished) + "ms");
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.reset();
                mediaPlayer.release();
                Log.i(TAG, "MediaPlayer finished after " + durationMilliseconds + "ms");
            }
        }.start();
        playTimers.add(playTimer);
        return true;
    }

    /**
     * Turns a makeBeat into a list of played measures by processing the beatPattern
     *
     * @param makeBeat
     * @return
     */
    // TODO: Find a more clever way to account for rests.
    public static List<List<Double>> measurePairsFromMakeBeat(ESMakeBeat makeBeat) {
        String beatPattern = makeBeat.getBeatPattern();
        char[] beats = beatPattern.toCharArray();
        List<List<Double>> playMeasures = new ArrayList<List<Double>>();
        int playIndex = 0;
        // i also tracks the position of the current beat in the sample
        for (int i = 0; i < beats.length; i++) {
            // TODO: Deal with more than one sample
            // Starts a new set of beats
            if (beats[i] == '0') {
                playMeasures.add(playIndex, createMakeBeatPair(i, BEAT_LENGTH));
                playIndex++;
            } else if (beats[i] == '+') {
                List<Double> original = playMeasures.remove(playIndex);
                playMeasures.add(playIndex, addPlusToMakeBeatPair(original, BEAT_LENGTH));
            } else if (beats[i] == '-') {
                List<Double> original = playMeasures.remove(playIndex);
                playMeasures.add(playIndex, addRestToMakeBeatPair(original, BEAT_LENGTH));
            } else {
                Log.i(TAG, "Unrecognized character in beatPattern: " + beats[i]);
            }
        }
        return playMeasures;
    }

    private static List<Double> createMakeBeatPair(int beatIndex, double beatLengthMeasures) {
        List<Double> created = new ArrayList<Double>();
        created.add(MakeBeatIndices.PLAY_START, beatIndex * beatLengthMeasures);
        created.add(MakeBeatIndices.PLAY_END, (beatIndex + 1) * beatLengthMeasures);
        created.add(MakeBeatIndices.REST_START, beatIndex * beatLengthMeasures);
        created.add(MakeBeatIndices.REST_END, beatIndex * beatLengthMeasures);
        return created;
    }

    private static List<Double> addPlusToMakeBeatPair(List<Double> originalPair,
                                                      double beatLengthMeasures) {
        Double playEnd = originalPair.remove(MakeBeatIndices.PLAY_END);
        playEnd += beatLengthMeasures;
        originalPair.add(MakeBeatIndices.PLAY_END, playEnd);
        return originalPair;
    }

    private static List<Double> addRestToMakeBeatPair(List<Double> originalPair,
                                                      double beatLengthMeasures) {
        Double restEnd = originalPair.remove(MakeBeatIndices.REST_END);
        restEnd += beatLengthMeasures;
        originalPair.add(MakeBeatIndices.REST_END, restEnd);
        return originalPair;
    }

    private static List<List<Integer>> calcPlayTimesMSFromMakeBeat(ESMakeBeat makeBeat,
                                                                   int tempoBPM, int phraseLength) {
        List<List<Double>> measures = measurePairsFromMakeBeat(makeBeat);
        int startTimeMilliseconds = Util.getLocationTimeMS(makeBeat.getStartLocation(),
                tempoBPM, phraseLength);
        List<List<Integer>> playTimesMS = new ArrayList<List<Integer>>();
        for (int i = 0; i < measures.size(); i++) {
            List<Integer> times = new ArrayList<Integer>();
            Double playStart = measures.get(i).get(MakeBeatIndices.PLAY_START);
            Double playEnd = measures.get(i).get(MakeBeatIndices.PLAY_END);
            Double restStart = measures.get(i).get(MakeBeatIndices.REST_START);
            Double restEnd = measures.get(i).get(MakeBeatIndices.REST_END);
            times.add(MakeBeatIndices.PLAY_START, startTimeMilliseconds
                    + Util.getLocationTimeMS(playStart, tempoBPM, phraseLength));
            times.add(MakeBeatIndices.PLAY_END, startTimeMilliseconds
                    + Util.getLocationTimeMS(playEnd, tempoBPM, phraseLength));
            times.add(MakeBeatIndices.REST_START, startTimeMilliseconds
                    + Util.getLocationTimeMS(restStart, tempoBPM, phraseLength));
            times.add(MakeBeatIndices.REST_END, startTimeMilliseconds
                    + Util.getLocationTimeMS(restEnd, tempoBPM, phraseLength));
            playTimesMS.add(times);
        }
        return playTimesMS;
    }

    private static MediaPlayer attachESEffect(Context context, int sampleID, ESSetEffect setEffect) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleID);
        if (setEffect != null) {
            if (setEffect.getEffectIndex() == Util.Effects.REVERB) {
                int decayTime = 5000;
                short density = 500;
                short diffusion = 500;
                short roomLevel = 0;
                short reverbLevel = 500;
                short reflectionsDelay = 100;
                short reflectionsLevel = 100;
                short reverbDelay = 0;

                EnvironmentalReverb reverb = new EnvironmentalReverb(0, 0);
                reverb.setDecayTime(decayTime);
                reverb.setDensity(density);
                reverb.setDiffusion(diffusion);
                reverb.setReverbLevel(reverbLevel);
                reverb.setRoomLevel(roomLevel);
                reverb.setReflectionsDelay(reflectionsDelay);
                reverb.setReflectionsLevel(reflectionsLevel);
                reverb.setReverbDelay(reverbDelay);
                //EnvironmentalReverb overdrive = createOverdriveEffect();
                mediaPlayer.attachAuxEffect(reverb.getId());
                mediaPlayer.setAuxEffectSendLevel(100.0f);
            }
        }
        return mediaPlayer;
    }

    public static void playForLoopFitMedia(ESFitMedia contained, ForLoop container, Context context,
                                           int tempoBPM, int phraseLength) {
        List<ESFitMedia> fitMediasToPlay =
                ESAudio.getFitMediaListFromForLoop(contained, container);
        List<CountDownTimer> fitMediaTimers = new ArrayList<CountDownTimer>();
        for (ESFitMedia fitMedia : fitMediasToPlay) {
            fitMediaTimers.add(ESAudio.getReadyMediaPlayerForPlay(fitMedia,
                    context, tempoBPM, phraseLength));
            //Log.i(TAG, "Attempting to play fitMedia:" + fitMedia.toString());
            //ESAudio.play(fitMedia, context, tempoBPM, phraseLength);
        }
        for (CountDownTimer timer : fitMediaTimers) {
            Log.i(TAG, "Starting forLoopFitMedia timer");
            timer.start();
        }
    }

    public static void playForLoopMakeBeat(ESMakeBeat contained, ForLoop container, Context context,
                                           int tempoBPM, int phraseLength) {
        List<ESMakeBeat> makeBeatsToPlay =
                ESAudio.getMakeBeatListFromForLoop(contained, container);
        List<CountDownTimer> makeBeatTimers = new ArrayList<CountDownTimer>();
        for (ESMakeBeat makeBeat : makeBeatsToPlay) {
            makeBeatTimers.add(ESAudio.getReadyMediaPlayerForPlay(makeBeat, context, tempoBPM,
                    phraseLength));
        }
        for (CountDownTimer timer : makeBeatTimers) {
            Log.i(TAG, "Starting forLoopMakeBeat timer");
            timer.start();
        }
    }

    private static List<ESFitMedia> getFitMediaListFromForLoop(ESFitMedia fitMedia, ForLoop forLoop) {
        List<ESFitMedia> fitMedias = new ArrayList<ESFitMedia>();
        String variable = forLoop.getVariable();
        List<Double> startLocations = new ArrayList<Double>();
        List<Double> endLocations = new ArrayList<Double>();
        double startAmount = fitMedia.getStartAmount();
        double endAmount = fitMedia.getEndAmount();
        Log.i(TAG, "Got here.. fitMediaList");
        // TODO: Handle case where two for loops are used.
        if (fitMedia.getStartVariable().equals(variable)
                && fitMedia.getEndVariable().equals(variable)) {
            Log.i(TAG, "ForLoop values" + forLoop.getIterValues().toString());
            // Start values.
            if (fitMedia.getStartOperand() == '+') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value + startAmount);
                }
            } else if (fitMedia.getStartOperand() == '-') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value - startAmount);
                }
            } else if (fitMedia.getStartOperand() == '*') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value * startAmount);
                }
            } else if (fitMedia.getStartOperand() == '/') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value / startAmount);
                }
            } else if (fitMedia.getStartOperand() == '%') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value % startAmount);
                }
            } else {
                startLocations = forLoop.getIterValues();
            }

            // End values.
            if (fitMedia.getEndOperand() == '+') {
                for (Double value : forLoop.getIterValues()) {
                    endLocations.add(value + endAmount);
                }
            } else if (fitMedia.getEndOperand() == '-') {
                for (Double value : forLoop.getIterValues()) {
                    endLocations.add(value - endAmount);
                }
            } else if (fitMedia.getEndOperand() == '*') {
                for (Double value : forLoop.getIterValues()) {
                    endLocations.add(value * endAmount);
                }
            } else if (fitMedia.getEndOperand() == '/') {
                for (Double value : forLoop.getIterValues()) {
                    endLocations.add(value / endAmount);
                }
            } else if (fitMedia.getEndOperand() == '%') {
                for (Double value : forLoop.getIterValues()) {
                    endLocations.add(value % endAmount);
                }
            } else {
                endLocations = forLoop.getIterValues();
            }
        }

        Log.i(TAG, "FitMedia startLocations size=" + startLocations.size());
        // Create fitMedias
        for (int i = 0; i < startLocations.size(); i++) {
            fitMedias.add(new ESFitMedia(fitMedia.getSampleName(), fitMedia.getSectionNumber(),
                    startLocations.get(i), endLocations.get(i)));
        }

        // TODO: isValid checks
        return fitMedias;
    }

    private static List<ESMakeBeat> getMakeBeatListFromForLoop(ESMakeBeat makeBeat, ForLoop forLoop) {
        List<ESMakeBeat> makeBeats = new ArrayList<ESMakeBeat>();
        String variable = forLoop.getVariable();
        List<Double> startLocations = new ArrayList<Double>();
        Double startAmount = makeBeat.getStartAmount();
        if (makeBeat.getStartVariable().equals(variable)) {
            if (makeBeat.getStartOperand() == '+') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value + startAmount);
                }
            } else if (makeBeat.getStartOperand() == '-') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value - startAmount);
                }
            } else if (makeBeat.getStartOperand() == '*') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value * startAmount);
                }
            } else if (makeBeat.getStartOperand() == '/') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value / startAmount);
                }
            } else if (makeBeat.getStartOperand() == '%') {
                for (Double value : forLoop.getIterValues()) {
                    startLocations.add(value % startAmount);
                }
            } else if (makeBeat.getStartOperand() == 'n') {
                startLocations = forLoop.getIterValues();
            }
        }

        // Create makeBeats
        for (int i = 0; i < startLocations.size(); i++) {
            makeBeats.add(new ESMakeBeat(makeBeat.getSampleName(), -1,
                    startLocations.get(i), makeBeat.getBeatPattern()));
        }
        return makeBeats;
    }

    public static void killAllMediaPlayers() {
        for (CountDownTimer timer : playTimers) {
            timer.cancel();
            timer.onFinish();
        }
    }

    /*
        code from:
        https://github.com/paranoidfrog/paranoid-frog-mobile-app/blob/893034c1a99dfbffa81806e1a2429338cd863099/src/com/example/paranoid_effects/Overdrive.java
     */
    private static EnvironmentalReverb createOverdriveEffect() {

        int decayTime = 5000;
        short density = 500;
        short diffusion = 500;
        short roomLevel = 0;
        short reverbLevel = 500;
        short reflectionsDelay = 100;
        short reflectionsLevel = 100;
        short reverbDelay = 0;

        EnvironmentalReverb reverb = new EnvironmentalReverb(0, 0);
        reverb.setDecayTime(decayTime);
        reverb.setDensity(density);
        reverb.setDiffusion(diffusion);
        reverb.setReverbLevel(reverbLevel);
        reverb.setRoomLevel(roomLevel);
        reverb.setReflectionsDelay(reflectionsDelay);
        reverb.setReflectionsLevel(reflectionsLevel);
        reverb.setReverbDelay(reverbDelay);
        return reverb;
    }

    private interface MakeBeatIndices {
        public static final int PLAY_START = 0;
        public static final int PLAY_END = 1;
        public static final int REST_START = 2;
        public static final int REST_END = 3;
    }
}


