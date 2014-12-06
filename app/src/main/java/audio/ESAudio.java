package audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import data.ESFitMedia;
import data.Util;

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
        int startTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getStartLocation(),
                tempoBPM, phraseLength);
        int endTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getEndLocation(),
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
        if (startTimeMilliseconds > 0) {
            mediaPlayer.seekTo(startTimeMilliseconds);
            Log.i(TAG, "MediaPlayer seeking to " + startTimeMilliseconds + "ms");
        }

        mediaPlayer.start();
        Log.i(TAG, "MediaPlayer starting at " + startTimeMilliseconds + "ms");
        CountDownTimer playTimer = new CountDownTimer(durationMilliseconds, durationMilliseconds) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.i(TAG, "MediaPlayer still playing after " +
                //        (durationMilliseconds - millisUntilFinished) + "ms");
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
}
