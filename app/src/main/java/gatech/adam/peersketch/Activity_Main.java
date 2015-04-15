// Main container
// Includes navigation drawer fragment. Select project from list view, then open song fragment

package gatech.adam.peersketch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import audio.ESAudio;
import data.ESFitMedia;
import data.Section;
import data.Song;
import data.SongLibrary;
import data.Util;
import gatech.adam.peersketch.views.ExpandableList_Child;

public class Activity_Main
        extends Activity
        implements Drawer_Pallet.PalletDrawerCallbacks, Drawer_Navigation.NavigationDrawerCallbacks {

    // Fragments
    private FragmentManager mFragmentManager; // Fragment manager, used for switching fragments
    public DrawerLayout mDrawerContainer; // Drawer container
    public Drawer_Navigation mNavigationDrawer; // Navigation drawer
    public Drawer_Pallet mPalletDrawer; // Pallet drawer

    public Fragment mSongLibraryFragment; // Song library
    public Fragment_SongEdit mSongEditorFragment; // Song editor
    public Fragment_SectionEdit mSectionEditorFragment; // Section editor
    public Fragment_Fab mFabFragment; // Floating action button

    // Data
    private Song mSong;
    private Section mSection;
    public SongLibrary mSongLibrary = new SongLibrary();
    public enum ExpandableListAdapterMode {SONG_EDITOR, PALLET_DRAWER};

    // Mode
    public enum Mode {SONG_LIBRARY, SONG_EDITOR, SECTION_EDITOR};
    public Mode mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initializing dummy data
        initData();

        // Restoring saved instance state, if available
        super.onCreate(savedInstanceState);
        // Setting root content
        setContentView(R.layout.activity_main);

        // Getting fragment manager
        mFragmentManager = getFragmentManager();

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
        Section electro = new Section("Funk Guitar 1");
        electro.add(new ESFitMedia(Util.DEFAULT_SAMPLES[Util.DefaultSamples.FUNK_GUITAR1], 0, 2, 3), 0); // Adding fitMedia
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
        return mSong;
    }

    public void setSong(Song to) {
        this.mSong = to;
    }

    public Section getSection() {
        return mSection;
    }

    public void setSection(Section to) {
        this.mSection = to;
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
                switchTo = Fragment_SongEdit.newInstance(mSong);
                isPalletVisible = true;
                isFabVisible = true;
                break;
            case SECTION_EDITOR:
                switchTo = Fragment_SectionEdit.newInstance(mSection);
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
        mFragmentManager.beginTransaction()
                .replace(R.id.container, switchTo)
                .commit();

        // Configure pallet drawer
        if(isPalletVisible) { setPalletDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); }
        else { setPalletDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); }

        // Update mMode
        mMode = to;
    }



    // Navigation drawer click listener
    public void onNavigationDrawerItemSelected(int position) {
        // Choosing replacement fragment
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
}
