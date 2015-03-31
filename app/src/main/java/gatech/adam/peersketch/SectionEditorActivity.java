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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import audio.ESAudio;
import data.ESFitMedia;
import data.ESMakeBeat;
import data.ESSetEffect;
import data.ForLoop;
import data.Group;
import data.GroupObject;
import data.IfStatement;
import data.Section;
import data.Util;
import ui.CreateFitMediaDialogFragment;
import ui.CreateForLoopDialogFragment;
import ui.CreateMakeBeatDialogFragment;
import ui.CreateSetEffectDialogFragment;


public class SectionEditorActivity extends FragmentActivity
        implements CreateFitMediaDialogFragment.FitMediaDialogListener,
        CreateForLoopDialogFragment.ForLoopDialogListener,
        CreateMakeBeatDialogFragment.MakeBeatDialogListener,
        CreateSetEffectDialogFragment.SetEffectDialogListener {

    public static String TAG = "section-editor";
    private static int selectedRadioButtonIndex = RadioButtonIndices.UNASSIGNED;
    // TODO change this to use callbacks from the DialogFragment
    private Section currentSection;
    private Group selectedToAddGroup;
    private ListView mListView;
    private Button mFitMediaButton;
    private Button mPlayButton;
    //private Button mSetEffectButton;
    private Button mForLoopButton;
    private Button mMakeBeatButton;
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
        final List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        final List<ForLoop> forLoops = currentSection.getForLoops();
        final List<ESMakeBeat> makeBeats = currentSection.getMakeBeats();
        final String[] sectionItems = new String[fitMedias.size() + forLoops.size()
                + makeBeats.size()];
        // Crappy parsing of strings -- right now, just has for loops first.
        for (int i = 0; i < forLoops.size(); i++) {
            Log.i(TAG, "Reassigned Item " + i + " to " + forLoops.get(i).toString());
            sectionItems[i] = forLoops.get(i).toString();
        }
        // Adds FitMedias to list.
        for (int i = 0; i < fitMedias.size(); i++) {
            Log.i(TAG, "Reassigned Item " + i + " to " + fitMedias.get(i).toString());
            sectionItems[i + forLoops.size()] = fitMedias.get(i).toString();
        }
        // Adds MakeBeats to list
        for (int i = 0; i < makeBeats.size(); i++) {
            Log.i(TAG, "Reassigned Item " + i + " to " + makeBeats.get(i).toString());
            sectionItems[i + forLoops.size() + fitMedias.size()] = makeBeats.get(i).toString();
        }
        mSectionItemsAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sectionItems);
        // TODO: call refreshListUI() here
        mListView.setAdapter(mSectionItemsAdapter);
        mFitMediaButton = (Button) findViewById(R.id.buttonFitMedia_dumbUI);
        mPlayButton = (Button) findViewById(R.id.buttonPlaySection_dumbUI);
        //mSetEffectButton = (Button) findViewById(R.id.buttonSetEffect_dumbUI);
        mMakeBeatButton = (Button) findViewById(R.id.buttonMakeBeat_dumbUI);
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
                if (currentSection.getFitMedias().isEmpty()
                        && currentSection.getForLoops().isEmpty()
                        && currentSection.getMakeBeats().isEmpty()) {
                    // TODO: Replace text once we add in makeBeat
                    Toast.makeText(context, "No Samples: Try making a FitMedia or MakeBeat" +
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
                // TODO: replace this.. ASAP
                if (position < fitMedias.size()) {
                    int fitMediaIndex = position;
                    // Clicked item is a fitMedia
                    ESFitMedia selectedFitMedia = currentSection.getFitMedias().get(fitMediaIndex);
                    ESAudio.play(selectedFitMedia, context, songBPM, songPhraseLength);
                    Log.i(TAG, "Playing fitMedia at position: " + position);
                    selectedToAddGroup = null;
                } else if (position < fitMedias.size() + forLoops.size()) {
                    int forLoopIndex = position - fitMedias.size();
                    promptAddChoiceToGroupDialog();
                    selectedToAddGroup = forLoops.get(forLoopIndex);
                    if (selectedRadioButtonIndex == RadioButtonIndices.FITMEDIA) {
                        promptFitMediaDialogAndWrite();
                    } else if (selectedRadioButtonIndex == RadioButtonIndices.MAKEBEAT) {
                        promptMakeBeatDialogAndWrite();
                    } else if (selectedRadioButtonIndex == RadioButtonIndices.FORLOOP) {
                        promptForLoopDialogAndWrite();
                    } else if (selectedRadioButtonIndex != RadioButtonIndices.UNASSIGNED) {
                        Toast.makeText(context,
                                "Unimplemented choice selected: " + selectedRadioButtonIndex,
                                Toast.LENGTH_LONG).show();
                    }

                } else if (position < fitMedias.size() + forLoops.size() + makeBeats.size()) {
                    int makeBeatIndex = position - fitMedias.size() - forLoops.size();
                    ESMakeBeat makeBeat = currentSection.getMakeBeats().get(makeBeatIndex);
                    ESAudio.play(makeBeat, context, currentSection.getTempoBPM(),
                            currentSection.getPhraseLengthMeasures());
                    selectedToAddGroup = null;
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < fitMedias.size()) {
                    int fitMediaIndex = position;
                    // Clicked item is a fitMedia
                    ESFitMedia removed = currentSection.getFitMedias().remove(fitMediaIndex);
                    Toast.makeText(context, "Removed FitMedia: " + removed.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Removed Section Item:" + removed.toString());
                } else if (position < fitMedias.size() + forLoops.size()) {
                    int forLoopIndex = position - fitMedias.size();
                    ForLoop forLoop = forLoops.get(forLoopIndex);
                    ESAudio.executeForLoop(forLoop, currentSection, context);
                    Toast.makeText(context, "Executing for loop: " + forLoop.toString(),
                            Toast.LENGTH_SHORT).show();
                } else if (position < fitMedias.size() + forLoops.size() + makeBeats.size()) {
                    int makeBeatIndex = position - fitMedias.size() - forLoops.size();
                    ESMakeBeat removed = currentSection.getMakeBeats().remove(makeBeatIndex);
                    Toast.makeText(context, "Removed FitMedia: " + removed.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Removed Section Item:" + removed.toString());
                }
                refreshListUI();
                return true;
            }
        });
        mForLoopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForLoopDialogAndWrite();
            }
        });
        mMakeBeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptMakeBeatDialogAndWrite();
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
        if (id == R.id.action_add_fitmedia) {
            // FITMEDIA
            promptFitMediaDialogAndWrite();
            return true;
        } else if (id == R.id.action_add_makebeat) {
            promptMakeBeatDialogAndWrite();
        } else if (id == R.id.action_add_seteffect) {
            promptSetEffectDialogAndWrite();
        } else if (id == R.id.action_add_forloop) {
            promptForLoopDialogAndWrite();
        } else if (id == R.id.action_clear_all) {
            currentSection.clearAll();
            Log.i(TAG, "Cleared all items in current section");
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptAddChoiceToGroupDialog() {
        DialogFragment newFragment = new AddChoiceToGroupDialogFragment();
        newFragment.show(getFragmentManager(), "addChoiceToGroup");
    }

    private void promptFitMediaDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateFitMediaDialogFragment();
        newFragment.show(getFragmentManager(), "createFitMedia");
    }

    private void promptMakeBeatDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateMakeBeatDialogFragment();
        newFragment.show(getFragmentManager(), "createMakeBeat");
    }

    private void promptForLoopDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateForLoopDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoop");
    }

    private void promptSetEffectDialogAndWrite() {
        // TODO: write toStrings for all the methods
        DialogFragment newFragment = new CreateSetEffectDialogFragment();
        newFragment.show(getFragmentManager(), "createSetEffect");
    }

    // TODO: Replace with ArrayAdapter.add(...) version because this is slow.
    public void refreshListUI() {
        //mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
        List<ForLoop> forLoops = currentSection.getForLoops();
        List<ESMakeBeat> makeBeats = currentSection.getMakeBeats();
        if (fitMedias.isEmpty() && forLoops.isEmpty() && makeBeats.isEmpty()) {
            Log.i(TAG, "fitMedias, forLoops, and makeBeats empty in refreshListUI()");
            mListView.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, new String[0]));
            return;
        }
        String[] sectionItems = new String[fitMedias.size() + forLoops.size() + makeBeats.size()];
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
        // Adds MakeBeats to list
        for (int i = 0; i < makeBeats.size(); i++) {
            Log.i(TAG, "Assigned Item " + i + " to " + makeBeats.get(i).toString());
            sectionItems[i + forLoops.size() + fitMedias.size()] = makeBeats.get(i).toString();
        }
        // TODO(hendon): Use GroupObject list instead for ordering.
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
        if (selectedToAddGroup != null) {
            Log.i(TAG, "Added new FitMedia: " + value.toString() + " to "
                    + selectedToAddGroup.toString());
            updateGroups(value);
        } else {
            Log.i(TAG, "Added new FitMedia: " + value.toString());
        }
        refreshListUI();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ForLoop value) {
        value.setSectionNumber(sectionNumber);
        if (selectedToAddGroup != null) {
            Log.i(TAG, "Added new ForLoop: " + value.toString() + " to "
                    + selectedToAddGroup.toString());
            updateGroups(value);
        } else {
            Log.i(TAG, "Added new ForLoop: " + value.toString());
            currentSection.add(value, Util.DROP_LOCATION);
            currentSection.addObject(value);
        }
        // TODO add variable to song & update drawer
        String variableName = value.getVariable();
        //if (!currentSection.getParentSong().addVariable(variableName, value.getAmount() + "")) {
        //    Log.e(TAG, "Unable to set variable:" + variableName + " to " + value.getAmount());
        // }
        refreshListUI();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESSetEffect value) {
        value.setSectionNumber(sectionNumber);
        // TODO(hendon)
        currentSection.add(value, Util.DROP_LOCATION);
        currentSection.addObject(value);
        //mSectionItemsAdapter.add(value.toString());
        //mSectionItemsAdapter.notifyDataSetChanged();
        if (selectedToAddGroup != null) {
            Log.i(TAG, "Added new SetEffect: " + value.toString() + " to "
                    + selectedToAddGroup.toString());
            updateGroups(value);
        } else {
            Log.i(TAG, "Added new SetEffect: " + value.toString());
        }
        refreshListUI();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESMakeBeat value) {
        value.setSectionNumber(sectionNumber);
        currentSection.add(value, Util.DROP_LOCATION);
        currentSection.addObject(value);
        if (selectedToAddGroup != null) {
            Log.i(TAG, "Added new MakeBeat: " + value.toString() + " to "
                    + selectedToAddGroup.toString());
            updateGroups(value);
        } else {
            Log.i(TAG, "Added new MakeBeat: " + value.toString());
        }
        refreshListUI();
    }

    public void updateGroups(GroupObject value) {
        for (Group group : currentSection.getSubgroups()) {
            if (selectedToAddGroup.equals(group)) {
                if (group.getClass().equals(ForLoop.class)) {
                    List<ForLoop> forLoops = currentSection.getForLoops();
                    ForLoop containerForLoop = (ForLoop) group;
                    for (int i = 0; i < forLoops.size(); i++) {
                        if (forLoops.get(i).equals(containerForLoop)) {
                            forLoops.get(i).addObject(value);
                            if (value.getClass().equals(ESFitMedia.class)) {
                                ESFitMedia fitMedia = (ESFitMedia) value;
                                forLoops.get(i).add(fitMedia);
                            } else if (value.getClass().equals(ESMakeBeat.class)) {
                                ESMakeBeat makeBeat = (ESMakeBeat) value;
                                forLoops.get(i).add(makeBeat);
                            } else if (value.getClass().equals(ForLoop.class)) {
                                ForLoop forLoop = (ForLoop) value;
                                forLoops.get(i).add(forLoop);
                            } else {
                                Log.e(TAG, "Attempted to add unknown / unimplemented " +
                                        "class to forLoop");
                            }
                            Log.i(TAG, "Found for loop for adding at forLoops[" + i + "].");
                            break;
                        }
                    }
                } else if (group.getClass().equals(IfStatement.class)) {
                    // TODO when if statements are added
                    Toast.makeText(context, "TODO: implement option in createForLoop!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Attempted to add group of unknown type: "
                            + group.getClass().getName());
                }
                selectedToAddGroup = null;
                break;
            }
        }
    }

    private interface RadioButtonIndices {
        final int UNASSIGNED = -1;
        final int FITMEDIA = 0;
        final int MAKEBEAT = 1;
        final int FORLOOP = 2;
    }

    public static class AddChoiceToGroupDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final RadioButton[] choices = new RadioButton[3];
            RadioGroup choiceFake = new RadioGroup(getActivity().getApplicationContext());
            choiceFake.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
            // Adds a fitMedia choice to the RadioGroup
            RadioButton fitMediaChoice = new RadioButton(getActivity().getApplicationContext());
            choiceFake.addView(fitMediaChoice);
            fitMediaChoice.setTextColor(Color.BLACK);
            fitMediaChoice.setText("fitMedia");
            // Adds a makeBeat choice to the RadioGroup
            RadioButton makeBeatChoice = new RadioButton(getActivity().getApplicationContext());
            choiceFake.addView(makeBeatChoice);
            makeBeatChoice.setTextColor(Color.BLACK);
            makeBeatChoice.setText("makeBeat");
            // Adds a forLoop choice to the RadioGroup
            RadioButton forLoopChoice = new RadioButton(getActivity().getApplicationContext());
            choiceFake.addView(forLoopChoice);
            forLoopChoice.setTextColor(Color.BLACK);
            forLoopChoice.setText("for loop");
            // Adds a ifStatement choice to the RadioGroup
            //RadioButton ifStatementChoice = new RadioButton(getActivity().getApplicationContext());
            //choiceFake.addView(ifStatementChoice);
            //ifStatementChoice.setTextColor(Color.BLACK);
            //ifStatementChoice.setText("if statement");
            final RadioGroup input = choiceFake;
            builder.setMessage("What would you like to add?")
                    .setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            selectedRadioButtonIndex = input.getCheckedRadioButtonId();
                        }
                    })
                    .setNegativeButton("Maybe later.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            selectedRadioButtonIndex = -1;
                            // User cancelled the dialog
                        }
                    })
                    .setView(input);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
