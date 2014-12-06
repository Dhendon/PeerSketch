package audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import data.ESFitMedia;
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

        // mediaPlayer.start();
        if (Util.DEBUG_MODE) {
            Toast.makeText(context, "Starting Timer now!", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "Theoretically playing from (" + startTimeMilliseconds
                + ", " + endTimeMilliseconds + ")");
        // Set to 100ms tick right now, because it doesn't tick exact enough to work correctly.
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
}
