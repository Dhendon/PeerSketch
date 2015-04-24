// Main container
// Includes navigation drawer fragment. Select project from list view, then open song fragment

package gatech.adam.peersketch;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import data.ESFitMedia;
import data.ESMakeBeat;
import data.ESSetEffect;
import data.ForLoop;
import data.Group;
import data.GroupObject;
import data.IfStatement;
import data.Section;
import data.Song;
import data.SongLibrary;
import data.Util;
import gatech.adam.peersketch.views.ExpandableList_Child;
import ui.CreateFitMediaDialogFragment;
import ui.CreateForLoopChoiceDialogFragment;
import ui.CreateForLoopDialogFragment;
import ui.CreateForLoopFitmediaDialogFragment;
import ui.CreateForLoopMakeBeatDialogFragment;
import ui.CreateMakeBeatDialogFragment;
import ui.CreateSectionDialogFragment;
import ui.CreateSetEffectDialogFragment;

public class Activity_Main
        extends FragmentActivity
        implements Drawer_Pallet.PalletDrawerCallbacks, Drawer_Navigation.NavigationDrawerCallbacks,
        CreateFitMediaDialogFragment.FitMediaDialogListener,
        CreateForLoopDialogFragment.ForLoopDialogListener,
        CreateMakeBeatDialogFragment.MakeBeatDialogListener,
        CreateSetEffectDialogFragment.SetEffectDialogListener,
        CreateForLoopFitmediaDialogFragment.ForLoopFitMediaDialogListener,
        CreateForLoopMakeBeatDialogFragment.ForLoopMakeBeatDialogListener,
        CreateForLoopChoiceDialogFragment.ForLoopChoiceDialogListener,
        CreateSectionDialogFragment.SectionDialogListener,
        Section.OnSectionChangeProvider,
        Song.OnSongChangeProvider {

    // Data
    private static Song mCurrentSong;
    // App
    private static String TAG = "main-activity";
    public DrawerLayout mDrawerContainer; // Drawer container
    public Drawer_Navigation mNavigationDrawer; // Navigation drawer
    public Drawer_Pallet mPalletDrawer; // Pallet drawer
    public Fragment mSongLibraryFragment; // Song library
    public Fragment_SongEdit mSongEditorFragment; // Song editor
    public Fragment_SectionEdit mSectionEditorFragment; // Section editor
    public Fragment_Fab mFabFragment; // Floating action button
    public SongLibrary mSongLibrary = new SongLibrary();
    public Mode mMode;
    // Fragments
    private FragmentManager mFragmentManager; // Fragment manager, used for switching fragments
    private Section mCurrentSection;
    private int sectionNumber;
    private Group selectedToAddGroup;
    private Context context = this;
    private boolean refreshUI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initializing dummy data
        initData();

        if (mCurrentSong == null) {
            mCurrentSong = new Song(120);
        } else if (!mCurrentSong.getSections().isEmpty()) {
            mCurrentSection = mCurrentSong.getSections().get(0);
            sectionNumber = 0;
        } else {
            mCurrentSection = new Section("New Section");
            mCurrentSong.addSection(mCurrentSection, 0);
        }

        // Restoring saved instance state, if available
        super.onCreate(savedInstanceState);
        // Setting root content
        setContentView(R.layout.activity_main);

        // Getting fragment manager
        mFragmentManager = getSupportFragmentManager();

        // Adding song library to root container
        mSongLibraryFragment = Fragment_SongLibrary.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.container, mSongLibraryFragment)
                .commit();

        //Get a pointer to drawer layout
        mDrawerContainer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setting up pallet drawer on the right
        mPalletDrawer = (Drawer_Pallet) mFragmentManager.findFragmentById(R.id.pallet_drawer);
        mPalletDrawer.setUp( // Set up the drawer
                R.id.pallet_drawer, // Root drawer view id, activity_main
                mDrawerContainer); // Drawer layout id, activity_main

        // Locking pallet drawer
        mDrawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);

        // Setting up navigation drawer on the left
        mNavigationDrawer = (Drawer_Navigation) mFragmentManager.findFragmentById(R.id.navigation_drawer);
        mNavigationDrawer.setUp( // Set up the drawer
                R.id.navigation_drawer, // Root drawer view id, activity_main
                mDrawerContainer); // Drawer layout id, activity_main
    }

    // Initialize dummy data
    private void initData() {


        // Creating dummy song data
        Song song = new Song();
        song.setDescription("My Song");

        // Creating mSections
        // Lead synth
        Section synthHarp = new Section("Funk Bass");
        synthHarp.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.FUNK_BASS1], 0, 2, 4), 0); // Adding fitMedia
        song.addSection(synthHarp, 0);

        // Organ
        Section organ = new Section("Hi-Hat Roll");
        organ.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.HIHAT_ROLL1], 0, 1, 3), 0); // Adding fitMedia
        //organ.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.ELECTRO3], 1, 4, 5), 1); // Adding fitMedia
        song.addSection(organ, 1);

        // Electro
        Section electro = new Section("Dubstep Pad 1");
        electro.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.DUBSTEP_PAD], 0, 2, 3), 0); // Adding fitMedia
        song.addSection(electro, 2);

        // Electro
        Section electro2 = new Section("Funk Bass 1");
        electro2.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.FUNK_BASS1], 0, 1, 3), 0); // Adding fitMedia
        song.addSection(electro2, 3);

        // Electro
        Section electro3 = new Section("Techno Synth");
        electro3.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.TECHNO_SYNTH1], 0, 3, 5), 0); // Adding fitMedia
        song.addSection(electro3, 4);

        // Electro
        Section electro4 = new Section("Clap");
        electro4.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.CLAP], 0, 3, 5), 0); // Adding fitMedia
        song.addSection(electro4, 5);

        // Adding song to song library
        mSongLibrary.addSong(song);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESFitMedia value) {
        if (!value.hasVariable() ||
                (value.hasVariable() && mCurrentSection.getVariables().containsKey(value.getStartVariable()))
                        && mCurrentSection.getVariables().containsKey(value.getEndVariable())) {
            value.setSectionNumber(sectionNumber);
            // TODO(hendon)
            mCurrentSection.add(value, Util.DROP_LOCATION);
            mCurrentSection.addObject(value);
            //mSectionItemsAdapter.add(value.toString());
            //mSectionItemsAdapter.notifyDataSetChanged();
            if (selectedToAddGroup != null) {
                Log.i(TAG, "Added new FitMedia: " + value.toString() + " to "
                        + selectedToAddGroup.toString());
                updateGroups(value);
            } else {
                Log.i(TAG, "Added new FitMedia: " + value.toString());
            }
        } else {
            String unknownVariable = "";
            if (!mCurrentSection.getVariables().containsKey(value.getStartVariable())) {
                unknownVariable += value.getStartVariable();
            }
            if (!mCurrentSection.getVariables().containsKey(value.getEndVariable())) {
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
        if (!mCurrentSection.addVariable(value.getVariable(), value.getIterValues())) {
            Toast.makeText(context, "Variable name:" + value.getVariable() + " already taken." +
                    " Please try again with a new name!", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedToAddGroup != null) {
                Log.i(TAG, "Added new ForLoop: " + value.toString() + " to "
                        + selectedToAddGroup.toString());
                updateGroups(value);
            } else {
                Log.i(TAG, "Added new ForLoop: " + value.toString());
                mCurrentSection.add(value, Util.DROP_LOCATION);
                mCurrentSection.addObject(value);
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESSetEffect value) {
        value.setSectionNumber(sectionNumber);
        // TODO(hendon)
        mCurrentSection.add(value, Util.DROP_LOCATION);
        mCurrentSection.addObject(value);
        //mSectionItemsAdapter.add(value.toString());
        //mSectionItemsAdapter.notifyDataSetChanged();
        if (selectedToAddGroup != null) {
            Log.i(TAG, "Added new SetEffect: " + value.toString() + " to "
                    + selectedToAddGroup.toString());
            updateGroups(value);
        } else {
            Log.i(TAG, "Added new SetEffect: " + value.toString());
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int choiceID) {
        if (choiceID == Util.ForLoopChoice.FITMEDIA) {
            promptForLoopFitMediaDialogAndWrite();
        } else if (choiceID == Util.ForLoopChoice.MAKEBEAT) {
            promptForLoopMakeBeatDialogAndWrite();
        } else if (choiceID == Util.ForLoopChoice.FORLOOP) {
            // TODO: Deal with Nesting
            promptForLoopDialogAndWrite();
        } else if (choiceID != Util.ForLoopChoice.UNASSIGNED) {
            Toast.makeText(context,
                    "Unimplemented choice selected: " + choiceID,
                    Toast.LENGTH_LONG).show();
        }
    }

        @Override
    public void onDialogPositiveClick(DialogFragment dialog, ESMakeBeat value) {
        if (!value.hasVariable() ||
                (value.hasVariable() && mCurrentSection.getVariables().containsKey(value.getStartVariable()))) {
            value.setSectionNumber(sectionNumber);
            mCurrentSection.add(value, Util.DROP_LOCATION);
            mCurrentSection.addObject(value);
            if (selectedToAddGroup != null) {
                Log.i(TAG, "Added new MakeBeat: " + value.toString() + " to "
                        + selectedToAddGroup.toString());
                updateGroups(value);
            } else {
                Log.i(TAG, "Added new MakeBeat: " + value.toString());
            }
        } else {
            Toast.makeText(context, "Unrecognized variable, check your spelling!",
                    Toast.LENGTH_SHORT).show();
            promptForLoopMakeBeatDialogAndWrite();
        }
    }

    @Override
    public void onBackPressed() {
        switch (mMode) {
            case SECTION_EDITOR:
                setMode(Mode.SONG_EDITOR);
                break;
            case SONG_EDITOR:
                setMode(Mode.SONG_LIBRARY);
                break;
            default:
            case SONG_LIBRARY:
                super.onBackPressed();
                break;
        }
    }

    public Song getSong() {
        return mCurrentSong;
    }

    public void setSong(Song to) {
        this.mCurrentSong = to;
    }

    public Section getmCurrentSection() {
        return mCurrentSection;
    }

    public void setmCurrentSection(Section to) {
        this.mCurrentSection = to;
    }

    // Lock or unlock the pallet drawer
    private void setPalletDrawerLockMode(int to) {
        if(mDrawerContainer != null) { mDrawerContainer.setDrawerLockMode(to, Gravity.END); }
    }

    // Close both drawers
    public void closeDrawers() {
        mDrawerContainer.closeDrawers();
    }

    // Open pallet drawer
    public void openPalletDrawer() {
        mDrawerContainer.openDrawer(Gravity.RIGHT);
    }

    // Set new mode: song library, song editor, section editor
    public void setMode(Mode to) {

        // Fragment settings
        boolean isPalletVisible; // True for SONG_EDITOR and SECTION_EDITOR
        boolean isFabVisible; // True for SONG_EDITOR and SECTION_EDITOR
        Fragment switchTo;

        switch (to) {
            case SONG_EDITOR:
                switchTo = Fragment_SongEdit.newInstance(mCurrentSong);
                isPalletVisible = true;
                isFabVisible = true;
                break;
            case SECTION_EDITOR:
                switchTo = Fragment_SectionEdit.newInstance(mCurrentSection);
                isPalletVisible = true;
                isFabVisible = true;
                break;
            default:
            case SONG_LIBRARY:
                switchTo = Fragment_SongLibrary.newInstance();
                isPalletVisible = false;
                isFabVisible = false;
                break;
        }

        // Replace current fragment with new fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container, switchTo);
        transaction.commit();

        refreshUI = false;
        // Configure pallet drawer
        if(isPalletVisible) { setPalletDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); }
        else { setPalletDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); }

        // Update mMode
        mMode = to;
    }

    // Navigation drawer click listener
    public void onNavigationDrawerItemSelected(int position) {
        // Choosing replacement fragment
        // TODO: Update when collab cloud gets updated.
        Mode to;
        switch (position) {
            case 1: // Collab Cloud, Friend Zone
                to = Mode.SONG_LIBRARY;
                break;
            case 2: // Games
                to = Mode.SONG_LIBRARY;
                break;
            case 3: // Settings
                to = Mode.SONG_LIBRARY;
                break;

            default:
            case 0: // Song Library
                to = Mode.SONG_LIBRARY;
                break;
        }

        // Replacing fragment
        Log.v("Activity_Main", to.toString());
        setMode(to);
    }

    public void onPalletDrawerItemDrag(ExpandableList_Child item, View view) {

        // Saving data to be transferred to drop target, i.e. song fragment
        ClipData data = ClipData.newPlainText(item.getName(), item.getTag());

        // Creating shadow builder and show for dragging
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, item, 0);
    }

    public void promptForLoopChoiceDialog() {
        DialogFragment newFragment = new CreateForLoopChoiceDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoopChoice");
    }

    public void promptFitMediaDialogAndWrite() {
        DialogFragment newFragment = new CreateFitMediaDialogFragment();
        newFragment.show(getFragmentManager(), "createFitMedia");
    }

    public void promptMakeBeatDialogAndWrite() {
        DialogFragment newFragment = new CreateMakeBeatDialogFragment();
        newFragment.show(getFragmentManager(), "createMakeBeat");
    }

    public void promptForLoopDialogAndWrite() {
        DialogFragment newFragment = new CreateForLoopDialogFragment();
        newFragment.show(getFragmentManager(), "createForLoop");
    }

    public void promptSetEffectDialogAndWrite() {
        DialogFragment newFragment = new CreateSetEffectDialogFragment();
        newFragment.show(getFragmentManager(), "createSetEffect");
    }

    public void promptForLoopFitMediaDialogAndWrite() {
        DialogFragment newFragment = CreateForLoopFitmediaDialogFragment.newInstance(mCurrentSection);
        newFragment.show(getFragmentManager(), "createForLoopFitMedia");
    }

    public void promptForLoopMakeBeatDialogAndWrite() {
        DialogFragment newFragment = CreateForLoopMakeBeatDialogFragment.newInstance(mCurrentSection);
        newFragment.show(getFragmentManager(), "createForLoopMakeBeat");
    }

    public void promptCreateSection() {
        DialogFragment newFragment = CreateSectionDialogFragment.newInstance(mCurrentSong);
        newFragment.show(getFragmentManager(), "createSection");
    }

    public void updateGroups(GroupObject value) {
        for (Group group : mCurrentSection.getSubgroups()) {
            if (selectedToAddGroup.equals(group)) {
                if (group.getClass().equals(ForLoop.class)) {
                    List<ForLoop> forLoops = mCurrentSection.getForLoops();
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

    @Override
    public void setOnSectionChangeListener(Section.OnSectionChangeListener listener) {
        mCurrentSection.setListener(listener);
    }

    @Override
    public void setOnSongChangeListener(Song.OnSongChangeListener listener) {
        mCurrentSong.setListener(listener);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String sectionName) {
        // This doesn't really do anything..
    }

    public enum ExpandableListAdapterMode {SONG_EDITOR, PALLET_DRAWER}

// Mode
    public enum Mode {SONG_LIBRARY, SONG_EDITOR, SECTION_EDITOR}

}
