package gatech.adam.peersketch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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
import ui.CreateForLoopChoiceDialogFragment;
import ui.CreateForLoopDialogFragment;
import ui.CreateForLoopFitmediaDialogFragment;
import ui.CreateForLoopMakeBeatDialogFragment;
import ui.CreateMakeBeatDialogFragment;
import ui.CreateSetEffectDialogFragment;


public class SectionEditorActivity extends FragmentActivity
        implements CreateFitMediaDialogFragment.FitMediaDialogListener,
        CreateForLoopDialogFragment.ForLoopDialogListener,
        CreateMakeBeatDialogFragment.MakeBeatDialogListener,
        CreateSetEffectDialogFragment.SetEffectDialogListener,
        CreateForLoopFitmediaDialogFragment.ForLoopFitMediaDialogListener,
        CreateForLoopMakeBeatDialogFragment.ForLoopMakeBeatDialogListener,
        CreateForLoopChoiceDialogFragment.ForLoopChoiceDialogListener {

    public static String TAG = "section-editor";
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: trigger editing instead of just playing here.
                // TODO: replace this.. ASAP
                List<ForLoop> currentForLoops = currentSection.getForLoops();
                List<ESMakeBeat> currentMakeBeats = currentSection.getMakeBeats();
                List<ESFitMedia> currentFitMedias = currentSection.getFitMedias();
                if (position < currentForLoops.size()) {
                    int forLoopIndex = position;
                    selectedToAddGroup = currentForLoops.get(forLoopIndex);
                    promptForLoopChoiceDialog();
                } else if (position < currentFitMedias.size() + forLoops.size()) {
                    int fitMediaIndex = position - currentFitMedias.size();
                    // Clicked item is a fitMedia
                    ESFitMedia selectedFitMedia = currentFitMedias.get(fitMediaIndex);
                    if (selectedFitMedia.hasVariable()) {
                        // Matches forLoop to fitMedia by variable.
                        for (ForLoop forLoop : currentForLoops) {
                            if (forLoop.getVariable().equals(selectedFitMedia.getStartVariable())) {
                                Log.i(TAG, "Found forLoop with variable:" + forLoop.getVariable());
                                ESAudio.playForLoopFitMedia(selectedFitMedia, forLoop, context,
                                        currentSection.getTempoBPM(),
                                        currentSection.getPhraseLengthMeasures());
                            }
                        }
                        Log.i(TAG, "Completed search for fitMedia forLoop");
                    } else {
                        ESAudio.play(selectedFitMedia, context, songBPM, songPhraseLength);
                    }
                    Log.i(TAG, "Playing fitMedia at position: " + position);
                    selectedToAddGroup = null;
                } else if (position < currentFitMedias.size() + currentForLoops.size()
                        + currentMakeBeats.size()) {
                    int makeBeatIndex = position - currentFitMedias.size() - currentForLoops.size();
                    ESMakeBeat selectedMakeBeat = currentSection.getMakeBeats().get(makeBeatIndex);
                    if (selectedMakeBeat.hasVariable()) {
                        for (ForLoop forLoop : currentForLoops) {
                            if (forLoop.getVariable().equals(selectedMakeBeat.getStartVariable())) {
                                ESAudio.playForLoopMakeBeat(selectedMakeBeat, forLoop, context,
                                        currentSection.getTempoBPM(),
                                        currentSection.getPhraseLengthMeasures());
                            }
                        }
                        Log.i(TAG, "Completed search for fitMedia forLoop");
                    } else {
                        ESAudio.play(selectedMakeBeat, context, currentSection.getTempoBPM(),
                                currentSection.getPhraseLengthMeasures());
                    }
                    selectedToAddGroup = null;
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                List<ForLoop> currentForLoops = currentSection.getForLoops();
                List<ESMakeBeat> currentMakeBeats = currentSection.getMakeBeats();
                List<ESFitMedia> currentFitMedias = currentSection.getFitMedias();
                if (position < currentFitMedias.size()) {
                    int fitMediaIndex = position;
                    // Clicked item is a fitMedia
                    ESFitMedia removed = currentFitMedias.remove(fitMediaIndex);
                    Toast.makeText(context, "Removed FitMedia: " + removed.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Removed Section Item:" + removed.toString());
                } else if (position < currentFitMedias.size() + currentForLoops.size()) {
                    int forLoopIndex = position - currentFitMedias.size();
                    ForLoop forLoop = currentForLoops.get(forLoopIndex);
                    // TODO: Play forLoop here.
                    //ESAudio.executeForLoop(forLoop, currentSection, context);
                    Toast.makeText(context, "Executing for loop: " + forLoop.toString(),
                            Toast.LENGTH_SHORT).show();
                } else if (position < currentFitMedias.size() + currentForLoops.size()
                        + currentMakeBeats.size()) {
                    int makeBeatIndex = position - currentFitMedias.size() - currentForLoops.size();
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
        } else if (id == R.id.action_stop_all_players) {
            ESAudio.killAllMediaPlayers();
        }
        // TODO: Remove this after testing
        else if (id == R.id.action_test_seteffect) {
            ESSetEffect effect = new ESSetEffect(Util.Effects.REVERB, 0, 1, 1.0);
            String sampleName = Util.DEFAULT_SAMPLES[Util.DefaultSamples.ORGAN];
            final ESFitMedia fitMedia = new ESFitMedia(sampleName, 0, 0, 1);
            ESAudio.play(fitMedia, effect, context, 120, 8);
            Toast.makeText(context, "Testing setEffect!", Toast.LENGTH_SHORT).show();
            CountDownTimer timer = new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    ESAudio.play(fitMedia, context, 120, 8);
                }
            }.start();

        }

        return super.onOptionsItemSelected(item);
    }

    private void promptForLoopChoiceDialog() {
        DialogFragment newFragment = new CreateForLoopChoiceDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoopChoice");
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

    private void promptForLoopFitMediaDialogAndWrite() {
        DialogFragment newFragment = new CreateForLoopFitmediaDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoopFitMedia");
    }

    private void promptForLoopMakeBeatDialogAndWrite() {
        DialogFragment newFragment = new CreateForLoopMakeBeatDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoopMakeBeat");
    }

    // TODO: Replace with ArrayAdapter.add(...) version because this is slow.
    public void refreshListUI() {
        //mListView = (ListView) findViewById(R.id.listViewSectionItems_dumbUI);
        List<ForLoop> forLoops = currentSection.getForLoops();
        List<ESFitMedia> fitMedias = currentSection.getFitMedias();
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
        if (!value.hasVariable() ||
                (value.hasVariable() && currentSection.getVariables().containsKey(value.getStartVariable()))
                        && currentSection.getVariables().containsKey(value.getEndVariable())) {
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
        } else {
            String unknownVariable = "";
            if (!currentSection.getVariables().containsKey(value.getStartVariable())) {
                unknownVariable += value.getStartVariable();
            }
            if (!currentSection.getVariables().containsKey(value.getEndVariable())) {
                unknownVariable += " " + value.getEndVariable();
            }
            Toast.makeText(context, "Unrecognized variable(s)" + unknownVariable +
                    " check your spelling!", Toast.LENGTH_SHORT).show();
            promptForLoopFitMediaDialogAndWrite();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ForLoop value) {
        value.setSectionNumber(sectionNumber);
        if (!currentSection.addVariable(value.getVariable(), value.getIterValues())) {
            Toast.makeText(context, "Variable name:" + value.getVariable() + " already taken." +
                    " Please try again with a new name!", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedToAddGroup != null) {
                Log.i(TAG, "Added new ForLoop: " + value.toString() + " to "
                        + selectedToAddGroup.toString());
                updateGroups(value);
            } else {
                Log.i(TAG, "Added new ForLoop: " + value.toString());
                currentSection.add(value, Util.DROP_LOCATION);
                currentSection.addObject(value);
            }
            refreshListUI();
        }
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
    public void onDialogPositiveClick(DialogFragment dialog, int choiceID) {
        if (choiceID == RadioButtonIndices.FITMEDIA) {
            promptForLoopFitMediaDialogAndWrite();
        } else if (choiceID == RadioButtonIndices.MAKEBEAT) {
            promptForLoopMakeBeatDialogAndWrite();
        } else if (choiceID == RadioButtonIndices.FORLOOP) {
            // TODO: Deal with Nesting
            promptForLoopDialogAndWrite();
        } else if (choiceID != RadioButtonIndices.UNASSIGNED) {
            Toast.makeText(context,
                    "Unimplemented choice selected: " + choiceID,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESMakeBeat value) {
        if (!value.hasVariable() ||
                (value.hasVariable() && currentSection.getVariables().containsKey(value.getStartVariable()))) {
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
        } else {
            Toast.makeText(context, "Unrecognized variable, check your spelling!",
                    Toast.LENGTH_SHORT).show();
            promptForLoopMakeBeatDialogAndWrite();
        }
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

    public interface RadioButtonIndices {
        final int UNASSIGNED = -1;
        final int FITMEDIA = 0;
        final int MAKEBEAT = 1;
        final int FORLOOP = 2;
    }

}
