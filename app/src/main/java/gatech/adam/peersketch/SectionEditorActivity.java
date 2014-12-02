package gatech.adam.peersketch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.Section;
import data.Util;


public class SectionEditorActivity extends FragmentActivity {

    public static String TAG = "section-editor";
    // TODO change this to use callbacks from the DialogFragment
    private static ESFitMedia recentFitMedia;
    private Section currentSection;
    private ListView mListView;
    private Button mFitMediaButton;
    private Button mPlayButton;
    // TODO: make these do something
    private int songBPM = Util.DEFAULT_TEMPO_BPM;
    private int songPhraseLength = Util.DEFAULT_PHRASE_LENGTH;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_editor);
        Intent intent = getIntent();
        if (intent.hasExtra(Util.BundleKeys.SECTION_NAME)) {
            String sectionName = intent.getStringExtra(Util.BundleKeys.SECTION_NAME);
            currentSection = new Section(sectionName);
            setTitle(sectionName);
        } else {
            currentSection = new Section("Section Name Goes Here");
        }
        // Sets up UI
        setTitle(currentSection.getName());
        mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        String[] sectionItems = new String[fitMedias.size()];
        // Crappy parsing of strings
        for (int i = 0; i < fitMedias.size(); i++) {
            sectionItems[i] = fitMedias.get(i).toString();
        }
        ArrayAdapter<String> sectionItemsAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sectionItems);
        mListView.setAdapter(sectionItemsAdapter);
        mFitMediaButton = (Button) findViewById(R.id.buttonFitMedia_dumbUI);
        mPlayButton = (Button) findViewById(R.id.buttonPlaySection_dumbUI);
        mFitMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptCreateFitMedia();
                currentSection.add(recentFitMedia, 0);
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSection.getFitMedias().isEmpty()) {
                    // TODO: Remove this after demo
                    ESAudio.play(new ESFitMedia(Util.DEFAULT_SAMPLES[1], 0, 0, 1), context, 120, 8);
                } else {
                    ESAudio.play(currentSection.getFitMedias().get(0),
                            context, songBPM, songPhraseLength);
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: trigger editing instead of just playing here.
                ESFitMedia selectedFitMedia = currentSection.getFitMedias().get(position);
                ESAudio.play(selectedFitMedia, context, songBPM, songPhraseLength);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptCreateFitMedia() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateFitMediaDialogFragment();
        newFragment.show(getFragmentManager(), "createFitMedia");
        refreshUI();
    }

    public void refreshUI() {
        mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        if (fitMedias.isEmpty()) {
            Log.i(TAG, "fitMedias empty in refreshUI()");
            return;
        }
        String[] sectionItems = new String[fitMedias.size()];
        // Crappy parsing of strings
        for (int i = 0; i < fitMedias.size(); i++) {
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

    public static class CreateFitMediaDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View prompt = inflater.inflate(R.layout.fragment_fitmedia_dialog, null);
            builder.setTitle("Create New FitMedia")
                    .setPositiveButton("Make it so!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Spinner samplesSpinner = (Spinner) prompt.findViewById(
                                    R.id.spinnerSamples);
                            EditText startEditText = (EditText) prompt.findViewById(
                                    R.id.editTextStartLocation);
                            EditText endEditText = (EditText) prompt.findViewById(
                                    R.id.editTextEndLocation);
                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                    android.R.layout.simple_spinner_item, Util.DEFAULT_SAMPLES);
                            samplesSpinner.setAdapter(adapter);
                            String rawStartLocation = startEditText.getText().toString();
                            String rawEndLocation = endEditText.getText().toString();
                            double start = Double.parseDouble(rawStartLocation);
                            double end = Double.parseDouble(rawEndLocation);

                            String sampleName = samplesSpinner.getSelectedItem().toString();
                            if (sampleName.equals("")) {
                                sampleName = "Default Sample";
                            }
                            Toast.makeText(getActivity().getApplicationContext(), "Made FitMedia!",
                                    Toast.LENGTH_SHORT);
                            recentFitMedia = new ESFitMedia(sampleName, 0, start, end);
                            Log.i(TAG, "recentFitMedia set:" + recentFitMedia.toString());
                        }
                    })
                    .setNegativeButton("Forget it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setView(prompt);

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
