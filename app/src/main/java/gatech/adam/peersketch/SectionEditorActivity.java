package gatech.adam.peersketch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.Section;
import data.Util;


public class SectionEditorActivity extends FragmentActivity
        implements CreateFitMediaDialogFragment.FitMediaDialogListener {

    public static String TAG = "section-editor";
    // TODO change this to use callbacks from the DialogFragment
    private Section currentSection;
    private ListView mListView;
    private Button mFitMediaButton;
    private Button mPlayButton;
    // TODO: Figure out why this adding to this is throwing an UnsupportedOperationException
    private ArrayAdapter<String> mSectionItemsAdapter;
    // TODO: make these do something
    private int songBPM = Util.DEFAULT_TEMPO_BPM;
    private int songPhraseLength = Util.DEFAULT_PHRASE_LENGTH;
    private Context context = this;
    private int sectionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_editor);
        Intent intent = getIntent();
        if (intent.hasExtra(Util.BundleKeys.SECTION_NAME)) {
            String sectionName = intent.getStringExtra(Util.BundleKeys.SECTION_NAME);
            currentSection = new Section(sectionName);
            setTitle(sectionName);
        } else if (intent.hasExtra(Util.BundleKeys.SECTION)) {
            currentSection = (Section) intent.getSerializableExtra(Util.BundleKeys.SECTION);
        } else {
            currentSection = new Section("Section Name Goes Here");
            currentSection.setSectionNumber(0);
        }
        sectionNumber = currentSection.getSectionNumber();

        // Sets up UI
        setTitle(currentSection.getName());
        mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        final String[] sectionItems = new String[fitMedias.size()];
        // Crappy parsing of strings
        for (int i = 0; i < fitMedias.size(); i++) {
            sectionItems[i] = fitMedias.get(i).toString();
        }
        mSectionItemsAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sectionItems);
        // TODO: call refreshListUI() here
        mListView.setAdapter(mSectionItemsAdapter);
        mFitMediaButton = (Button) findViewById(R.id.buttonFitMedia_dumbUI);
        mPlayButton = (Button) findViewById(R.id.buttonPlaySection_dumbUI);
        mFitMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptFitMediaDialogAndWrite();
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSection.getFitMedias().isEmpty()) {
                    // TODO: Replace text once we add in makeBeat
                    Toast.makeText(context, "No Samples: Try making a FitMedia," +
                            " then hit play again", Toast.LENGTH_LONG).show();
                } else {
                    ESAudio.play(currentSection, context);
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: trigger editing instead of just playing here.
                ESFitMedia selectedFitMedia = currentSection.getFitMedias().get(position);
                //localPlay(selectedFitMedia, songBPM, songPhraseLength);
                ESAudio.play(selectedFitMedia, context, songBPM, songPhraseLength);
                Log.i(TAG, "Playing fitMedia at position: " + position);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.RED);
                ESFitMedia removed = currentSection.getFitMedias().remove(position);
                Toast.makeText(context, "Removed FitMedia: " + removed.toString(),
                        Toast.LENGTH_SHORT).show();
                // TODO Undo button for removal?
                Log.i(TAG, "Removed Section Item:" + removed.toString());
                refreshListUI();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_section_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_clear_all) {
            currentSection.clearAll();
            Log.i(TAG, "Cleared all items in current section");
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptFitMediaDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateFitMediaDialogFragment();
        newFragment.show(getFragmentManager(), "createFitMedia");
    }

    // TODO: Replace with ArrayAdapter.add(...) version because this is slow.
    public void refreshListUI() {
        //mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        if (fitMedias.isEmpty()) {
            Log.i(TAG, "fitMedias empty in refreshListUI()");
            mListView.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, new String[0]));
            return;
        }
        String[] sectionItems = new String[fitMedias.size()];
        // Crappy parsing of strings
        for (int i = 0; i < sectionItems.length; i++) {
            Log.i(TAG, "Assigned Item " + i + " to " + fitMedias.get(i).toString());
            sectionItems[i] = fitMedias.get(i).toString();
        }
        ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sectionItems);
        mListView.setAdapter(sectionItemsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent songIntent = new Intent(context, SongEditorActivity.class);
        songIntent.putExtra(Util.BundleKeys.SECTION, currentSection);
        startActivity(songIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListUI();
    }

    /**
     * DEPRECATED  -- REPLACE WITH ESAudio.play(...) immediately
     *
     * @param fitMedia
     * @param tempoBPM
     * @param phraseLength
     * @return
     */
    private boolean localPlay(ESFitMedia fitMedia, int tempoBPM, int phraseLength) {
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
        Log.i(TAG, "Created MediaPlayer with sample: " + fitMedia.getSampleName());
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESFitMedia value) {
        value.setSectionNumber(sectionNumber);
        currentSection.add(value, Util.DROP_LOCATION);
        //mSectionItemsAdapter.add(value.toString());
        //mSectionItemsAdapter.notifyDataSetChanged();
        Log.i(TAG, "Added new FitMedia: " + value.toString());
        refreshListUI();
    }


    private List<String> getItemsAsStringList() {
        ArrayList<String> allItems = new ArrayList<String>();
        if (currentSection.getFitMedias().isEmpty()) {
            Log.i(TAG, "No FitMedias in current section");
        }
        for (ESFitMedia fitMedia : currentSection.getFitMedias()) {
            if (fitMedia != null)
                allItems.add(fitMedia.toString());
            else
                Log.i(TAG, "Null fitMedia!");
        }
        // TODO: Splice together all items, instead of just fitMedias
        return allItems;
    }
}
