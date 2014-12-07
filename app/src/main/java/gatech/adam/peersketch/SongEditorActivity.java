package gatech.adam.peersketch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import audio.ESAudio;
import data.Section;
import data.Song;
import data.Util;


public class SongEditorActivity extends FragmentActivity {

    private static final String TAG = "song-editor";
    private static String sectionName;
    private static Song currentSong;
    private Button mButtonCreateSection;
    private ListView mListViewSongItems;
    private Context context = this;
    private ArrayAdapter<String> sampleAdapter;

    private static boolean sectionNameTaken(String name) {
        for (Section section : currentSong.getSections()) {
            if (section.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_editor);
        mListViewSongItems = (ListView) findViewById(R.id.listViewSections_dumbUI);
        if (currentSong == null) {
            currentSong = new Song();
        } else {
            List<Section> sections = currentSong.getSections();
            String[] sectionItems = new String[sections.size()];
            // Crappy parsing of strings
            for (int i = 0; i < sections.size(); i++) {
                sectionItems[i] = sections.get(i).toString();
            }
            ArrayAdapter<String> sampleAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, sectionItems);
            mListViewSongItems.setAdapter(sampleAdapter);
        }
        mButtonCreateSection = (Button) findViewById(R.id.buttonNewSection_dumbUI);
        // Set up the drawer.
        mButtonCreateSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionName = "";
                promptCreatedSectionName();
                // Add section to this song
                // TODO: use tap location data
                refreshListUI();
            }
        });
        mListViewSongItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sectionEditorIntent = new Intent(context,
                        SectionEditorActivity.class);
                sectionEditorIntent.putExtra(Util.BundleKeys.SECTION,
                        currentSong.getSections().get(position));
                startActivity(sectionEditorIntent);
            }
        });
        mListViewSongItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ESAudio.play(currentSong.getSections().get(position), context);
                return true;
            }
        });
    }

    // TODO: Check for sections of the same name...
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra(Util.BundleKeys.SECTION)) {
            Section editedSection = (Section) intent.getSerializableExtra(Util.BundleKeys.SECTION);
            int thisSectionIndex = editedSection.getSectionNumber();
            List<Section> sections = currentSong.getSections();
            Section expectedSection = sections.get(thisSectionIndex);
            if (expectedSection.getName().equals(editedSection.getName())) {
                // TODO: Remember if this just edits a local copy or not..
                currentSong.getSections().set(thisSectionIndex, editedSection);
            } else {
                Log.e(TAG, "Attempted to add mismatched edited Section name: "
                        + editedSection.getName() + " Index:" + thisSectionIndex);
                for (int i = 0; i < sections.size(); i++) {
                    if (sections.get(i).getName().equals(editedSection.getName())) {
                        Log.i(TAG, "Found section with same name at index:" + i
                                + " instead of index:" + thisSectionIndex);
                        editedSection.setSectionNumber(i);
                        currentSong.getSections().set(i, editedSection);
                    }
                }
                Section updatedSection = currentSong.getSections().get(editedSection.getSectionNumber());
                if (!updatedSection.equals(editedSection)) {
                    Log.e(TAG, "Sections were not updated successfully - unknown section: "
                            + editedSection.toString());
                }
            }
            refreshListUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_editor, menu);
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

    private void promptCreatedSectionName() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateSectionDialogFragment();
        newFragment.show(getFragmentManager(), "createSection");
    }

    // TODO: Replace with ArrayAdapter.add(...) because this is inefficient
    public void refreshListUI() {
        List<Section> sections = currentSong.getSections();
        String[] sectionItems = new String[sections.size()];
        // Crappy parsing of strings
        for (int i = 0; i < sections.size(); i++) {
            sectionItems[i] = sections.get(i).toString();
        }
        ArrayAdapter<String> sampleAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sectionItems);
        mListViewSongItems.setAdapter(sampleAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        Section recentSection = (Section) intent.getSerializableExtra(Util.BundleKeys.SECTION);
        // Replace old version of section with updated one
        List<Section> sections = currentSong.getSections();
        sections.remove(recentSection.getSectionNumber());
        sections.add(recentSection.getSectionNumber(), recentSection);
        refreshListUI();
    }

    public static class CreateSectionDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            EditText inputFake = new EditText(getActivity().getApplicationContext());
            inputFake.setTextColor(Color.BLACK);
            final EditText input = inputFake;
            builder.setMessage("What's the name of the section?")
                    .setPositiveButton("Create!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sectionName = input.getText().toString();
                            if (!sectionNameTaken(sectionName)) {
                            // TODO: fix this getting cleared.
                            int trackNumber = currentSong.getSections().size() - 1;
                            currentSong.addSection(new Section(sectionName), trackNumber);
                            Intent sectionEditorIntent = new Intent(getActivity().getApplicationContext(),
                                    SectionEditorActivity.class);
                            sectionEditorIntent.putExtra(Util.BundleKeys.SECTION_NAME, sectionName);
                            sectionEditorIntent.putExtra(Util.BundleKeys.SECTION_NUMBER, trackNumber);
                            startActivity(sectionEditorIntent);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Sorry! Section name taken, please try again with a new name",
                                        Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Unable to create new section with name: "
                                        + sectionName + " - Duplicate Name");
                            }

                        }
                    })
                    .setNegativeButton("Nevermind.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setView(input);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
