// Main container
// Includes navigation drawer fragment. Select project from list view, then open song fragment

package gatech.adam.peersketch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;

import data.Group;
import data.Section;
import data.Song;
import data.SongLibrary;
import gatech.adam.peersketch.views.ExpandableList_Child;

public class Activity_Main
        extends Activity
        implements Fragment_Drawer_Pallet.PalletDrawerCallbacks, Fragment_Drawer_Navigation.NavigationDrawerCallbacks {

    private FragmentManager mFragmentManager = getFragmentManager(); // Fragment manager, used for switching fragments
    private DrawerLayout mDrawerContainer; // Get a pointer to the drawer layout
    private Fragment mProjects; // Get a pointer to the project list fragment

    public SongLibrary mSongLibrary = new SongLibrary();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment_Drawer_Navigation fragmentNavigationDrawer; // Declare a pointer to the navigation fragment
        Fragment_Drawer_Pallet fragmentPalletDrawer; // Declare a pointer to the pallet fragment

        initData(); // Initialize dummy data

        super.onCreate(savedInstanceState); // Restore saved instance state, if available
        setContentView(R.layout.activity_main); // Set root content view to XML layout file, activity_main

        //Get a pointer to drawer layout
        mDrawerContainer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Add project fragment to main_activity/container
        mProjects = new Fragment_SongLibrary();
        mFragmentManager.beginTransaction()
                .add(R.id.container, mProjects)
                .commit();

        // Set up pallet drawer and lock in closed position
        fragmentPalletDrawer = (Fragment_Drawer_Pallet) mFragmentManager.findFragmentById(R.id.pallet_drawer);
        fragmentPalletDrawer.setUp( // Set up the drawer
                R.id.pallet_drawer, // Root drawer view id, activity_main
                mDrawerContainer); // Drawer layout id, activity_main
        mDrawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);

        // Set up navigation drawer
        fragmentNavigationDrawer = (Fragment_Drawer_Navigation) mFragmentManager.findFragmentById(R.id.navigation_drawer);
        fragmentNavigationDrawer.setUp( // Set up the drawer
                R.id.navigation_drawer, // Root drawer view id, activity_main
                mDrawerContainer); // Drawer layout id, activity_main
    }

    // Initialize dummy data
    private void initData() {
        // Creating dummy song data
        Song songDummy1 = new Song("My Awesome Song"); // Song

        songDummy1.addSection(new Section("Lead Synth 1"), 0); // Track 1
        songDummy1.addSection(new Section("Pad 1"), 0); // Track 2

        ArrayList<Section> group1List = new ArrayList<Section>();
        group1List.add(new Section("Clap 1")); // Track 3
        group1List.add(new Section("Snare 3")); // Track 4
        group1List.add(new Section("Snare 2")); // Track 5
        songDummy1.addGroup(new Group(group1List, "Rhythm Section"), 0); // Group 1

        mSongLibrary.addSong(songDummy1);

    }

    // Make pallet drawer available
    private void togglePalletDrawer(boolean setTo) {
        // Enable and disable pallet based on currently showing fragment, i.e. song and track fragments
        if (setTo) { mDrawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END);}
        else { mDrawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);}
    }

    // Close drawers
    public void closeDrawers() {
        mDrawerContainer.closeDrawers();
    }

    // Open pallet drawer
    public void openPalletDrawer() {
        mDrawerContainer.openDrawer(Gravity.RIGHT);
    }

    // Update the main content by replace fragments
    public void updateContainer(Fragment to, Boolean isPalletVisible) {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, to)
                .commit();

        togglePalletDrawer(isPalletVisible);
    }

    // Navigation drawer click listener
    public void onNavigationDrawerItemSelected(int position) {

        // Default fragment, landing page
        Fragment switchToFragment = mProjects;

        // Chose replacement fragment based on selection
        switch (position) {
            case 0:             // Projects
                switchToFragment = mProjects;
                break;
            case 1:             // Collab Cloud, Friend Zone
                // switchToFragment = Fragment_Projects.newInstance();
                break;
            case 2:             // Games
                // switchToFragment = Fragment_Projects.newInstance();
                break;
            case 3:             // Settings
                // switchToFragment = Fragment_Projects.newInstance();
                break;
        }

        // Replacing fragment and hiding pallet drawer, if activity_main has already been inflated
        if (mDrawerContainer != null) {
            updateContainer(switchToFragment, false);
        }

    }

    // Pallet drawer click listener
    public void onPalletDrawerItemSelected(int position) {
        // TODO: Play clip functionality
    }
    public void onPalletDrawerItemDrag(ExpandableList_Child item, View view) {
        // TODO: Drag and drop functionality
        //Toast.makeText(getApplicationContext(), "Drag start: " + item.getName(), Toast.LENGTH_SHORT).show();

        // Saving data to be trasferred to drop target, i.e. song fragment
        ClipData data = ClipData.newPlainText(item.getName(), item.getTag());

        // Creating shadow builder and show for dragging
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, item, 0);
    }
}
