package gatech.adam.peersketch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.ForLoop;
import data.Section;
import data.Util;


public class SectionEditorActivity extends FragmentActivity
        implements CreateFitMediaDialogFragment.FitMediaDialogListener,
        CreateForLoopDialogFragment.ForLoopDialogListener {

    public static String TAG = "section-editor";
    // TODO change this to use callbacks from the DialogFragment
    private Section currentSection;
    private ListView mListView;
    private Button mFitMediaButton;
    private Button mPlayButton;
    private Button mSetEffectButton;
    private Button mForLoopButton;
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
        mSetEffectButton = (Button) findViewById(R.id.buttonSetEffect_dumbUI);
        mForLoopButton = (Button) findViewById(R.id.buttonForLoop_dumbUI);

        // Sets up click interactions with UI elements
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
        // TODO(hendon): replace these to include for loops - Container Class?
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
        mSetEffectButton.setVisibility(View.INVISIBLE);
        mSetEffectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not yet implemented!", Toast.LENGTH_SHORT).show();
            }
        });
        mForLoopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForLoopDialogAndWrite();
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

    private void promptForLoopDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateForLoopDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoop");
    }
    // TODO: Replace with ArrayAdapter.add(...) version because this is slow.
    public void refreshListUI() {
        //mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        List<ForLoop> forLoops = currentSection.getForLoops();
        if (fitMedias.isEmpty() && forLoops.isEmpty()) {
            Log.i(TAG, "fitMedias or forLoops empty in refreshListUI()");
            mListView.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, new String[0]));
            return;
        }
        String[] sectionItems = new String[fitMedias.size() + forLoops.size()];
        // Crappy parsing of strings -- right now, just has for loops first.
        for (int i = 0; i < forLoops.size(); i++) {
            Log.i(TAG, "Assigned Item " + i + " to " + forLoops.get(i).toString());
            sectionItems[i] = forLoops.get(i).toString();
        }
        // Adds FitMedias to list.
        for (int i = 0; i < fitMedias.size(); i++) {
            Log.i(TAG, "Assigned Item " + i + " to " + fitMedias.get(i).toString());
            sectionItems[i + forLoops.size()] = fitMedias.get(i).toString();
        }
        // TODO(hendon): set the ordering to be correct with the for loops and fitMedias
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESFitMedia value) {
        value.setSectionNumber(sectionNumber);
        // TODO(hendon)
        currentSection.add(value, Util.DROP_LOCATION);
        currentSection.addObject(value);
        //mSectionItemsAdapter.add(value.toString());
        //mSectionItemsAdapter.notifyDataSetChanged();
        Log.i(TAG, "Added new FitMedia: " + value.toString());
        refreshListUI();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ForLoop value) {
        value.setSectionNumber(sectionNumber);
        currentSection.add(value, Util.DROP_LOCATION);
        currentSection.addObject(value);
        Log.i(TAG, "Added new ForLoop: " + value.toString());
        // TODO add variable to song & update drawer
        String variableName = value.getVariable();
        if (!currentSection.getParentSong().addVariable(variableName, value.getAmount() + "")) {
            Log.e(TAG, "Unable to set variable:" + variableName + " to " + value.getAmount());
        }
        refreshListUI();
    }
}
