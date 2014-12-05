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
            fitMedia = new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0.1, 1);
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
        int sampleId = -1;
        for (int i = 0; i < Util.DEFAULT_SAMPLES.length; i++) {
            if (Util.DEFAULT_SAMPLES[i].equals(fitMedia.getSampleName())) {
                isDefaultSample = true;
                sampleId = Util.DEFAULT_SAMPLE_IDS[i];
                break;
            }
        }
        if (!isDefaultSample) {
            // TODO: handle user imported samples
            return false;
        }

        final MediaPlayer mediaPlayer = MediaPlayer.create(context, sampleId);
        Log.i(TAG, "Created MediaPlayer - " + mediaPlayer.toString());

        // Schedule when it needs to be played
        int startTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getStartLocation(),
                tempoBPM, phraseLength);
        int endTimeMilliseconds = Util.getLocationTimeMS(fitMedia.getEndLocation(),
                tempoBPM, phraseLength);
        final int durationMilliseconds = endTimeMilliseconds - startTimeMilliseconds;
        // TODO: Add in for loops / conditionals affecting the playing of the song.
        // Play it
        /*try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (startTimeMilliseconds > 0) {
            mediaPlayer.seekTo(startTimeMilliseconds);
        }
        Log.i(TAG, "MediaPlayer seeking to " + startTimeMilliseconds + "ms");
        mediaPlayer.start();
        // dear jesus this is hacky..
        CountDownTimer playTimer = new CountDownTimer(durationMilliseconds, durationMilliseconds) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                Log.i(TAG, "MediaPlayer finished after " + durationMilliseconds + "ms");
            }
        }.start();
        mediaPlayer.reset();
        mediaPlayer.release();
        return true;

        /* MEDIAPLAYER STANDARD
        if(mp!=null) {
        if(mp.isPlaying())
            mp.stop();
        mp.reset();
        mp.release();
        mp=null; }
         */
    }
}
