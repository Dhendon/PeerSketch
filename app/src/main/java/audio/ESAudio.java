package audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;

import data.ESFitMedia;
import data.Util;
import gatech.adam.peersketch.R;

/**
 * Created by davidhendon on 11/16/14.
 */
public class ESAudio {
    // TODO: recreate ES Audio functionality using MediaPlayer
    public static final String TAG = "esaudio";

    /**
     * @param fitMedia
     * @return Returns true when the section can be successfully played, false otherwise.
     */
    public static boolean play(ESFitMedia fitMedia, Context context, int tempoBPM, int phraseLength) {
        if (fitMedia == null) {
            Log.i(TAG, "play - fitMedia is null");
            fitMedia = new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0, 1);
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

        // Get Sample
        boolean isDefaultSample = false;
        for (String sampleName : Util.DEFAULT_SAMPLES) {
            if (sampleName.equals(fitMedia.getSampleName())) {
                isDefaultSample = true;
                break;
            }
        }

        int sampleId = -1;
        if (isDefaultSample) {
            // Locate the file
            //String filePath = "/samples/" + fitMedia.getSampleName();
            // TODO: There has to be a better way to do this.
            // TODO: Replace this with selected samples.
            if (fitMedia.getSampleName().equals(Util.DEFAULT_SAMPLES[1])) {
                sampleId = R.raw.test_cbr;
            } else if (fitMedia.getSampleName().equals(Util.DEFAULT_SAMPLES[2])) {
                sampleId = R.raw.sample2;
            } else if (fitMedia.getSampleName().equals(Util.DEFAULT_SAMPLES[3])) {
                sampleId = R.raw.sample3;
            } else {
                sampleId = R.raw.sample4;
            }
        } else {
            // TODO: handle user imported samples
            return false;
        }

        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);

        // Schedule when it needs to be played
        int startTimeMilliseconds = getLocationTimeMS(fitMedia.getStartLocation(),
                tempoBPM, phraseLength);
        int endTimeMilliseconds = getLocationTimeMS(fitMedia.getEndLocation(),
                tempoBPM, phraseLength);
        int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;
        // TODO: Add in for loops / conditionals affecting the playing of the song.
        // Play it
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.seekTo(startTimeMilliseconds);
        mediaPlayer.start();
        // dear jesus this is hacky..
        CountDownTimer playTimer = new CountDownTimer(durationMilliseconds, durationMilliseconds) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
            }
        }.start();
        return true;
    }

    private static int getLocationTimeMS(double location, int tempoBPM, int phraseLength) {
        // measures --> beats
        final int MINUTES_TO_MS = 60000;
        double numBeats = location * phraseLength;
        // BPM --> minutes --> milliseconds
        return (int) (numBeats / tempoBPM * MINUTES_TO_MS);
    }
}
