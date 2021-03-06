package gatech.adam.peersketch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import audio.ESAudio;
import data.ESFitMedia;
import data.Util;
import gatech.adam.peersketch.views.ExpandableList_Adapter;
import gatech.adam.peersketch.views.ExpandableList_Child;
import gatech.adam.peersketch.views.ExpandableList_Group;


public class Drawer_Pallet extends Fragment {
    // Remember the position of the selected item.
    private static final String STATE_SELECTED_POSITION = "selected_pallet_drawer_position";

    // Per the design guidelines, you should show the drawer on launch until the user manually expands it. This shared preference tracks this.
    private static final String PREF_USER_LEARNED_DRAWER_PALLET = "pallet_drawer_learned";

    // A pointer to the current callbacks instance (the Activity).
    private PalletDrawerCallbacks mCallbacks;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinearLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private Button mStopAudioButton;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER_PALLET, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate pallet drawer linear layout, attach to activity_main/container
        mDrawerLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_drawer_pallet, container, false);

        // Inflate navigation drawer list view, attach to activity_main/container
        mDrawerListView = (ExpandableListView) mDrawerLinearLayout.findViewById(R.id.listView_pallet);

        // Set list item long click listener
        mDrawerListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        // If item is a child, proceed to drag operation
                        if (mDrawerListView.getItemAtPosition(position) instanceof ExpandableList_Child) {
                            ExpandableList_Child item = (ExpandableList_Child) mDrawerListView.getItemAtPosition(position);
                            mCallbacks.onPalletDrawerItemDrag(item, view);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );

        mStopAudioButton = (Button) mDrawerLinearLayout.findViewById(R.id.buttonStopAudio);
        mStopAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESAudio.killAllMediaPlayers();
            }
        });

        // Set expandable list view adapter
        // Track Groups
        ArrayList<ExpandableList_Child> children1 = new ArrayList<>();
        for (String name : Util.DEFAULT_SAMPLES) {
            children1.add(new ExpandableList_Child(name));
        }

        // Populating groups...
        ArrayList<ExpandableList_Group> groups = new ArrayList<>();
        groups.add(new ExpandableList_Group("Demo Samples", children1));

        // Initiating track list adapter
        ExpandableList_Adapter pallet_adapter = new ExpandableList_Adapter(
                (Activity_Main) getActivity(),
                groups,
                Activity_Main.ExpandableListAdapterMode.PALLET_DRAWER
        );

        // Getting pointer to expandable list view in root, fragment_song_editt.xml.xml, and setting adapter
        final ExpandableListView expandableListView =  (ExpandableListView) mDrawerListView.findViewById(R.id.listView_pallet);
        expandableListView.setAdapter(pallet_adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // Getting child item
                String sampleName = Util.DEFAULT_SAMPLES[childPosition];

                // Playing sample
                ESAudio.play(new ESFitMedia(sampleName, 0, 0, 2), getActivity(), 120, 4);

                // Animating view
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
                v.startAnimation(anim);
                return true;
            }
        });

        return mDrawerLinearLayout;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    // Set up the drawer's list with items and click listeners
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        /**
         * Users of this fragment must call this method to set up the navigation drawer interactions.
         *
         * @param fragmentId   The android:id of this fragment in its activity's layout.
         * @param drawerLayout The DrawerLayout containing this fragment's UI.
         */

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer

        boolean isLocked = (mDrawerLayout.getDrawerLockMode(Gravity.END) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (!mUserLearnedDrawer && !mFromSavedInstanceState && isLocked) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
    }

    private void selectItem(int position) {
        Log.i("drawer-pallet", "Is this even called? pos=" + position);
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            //mCallbacks.onPalletDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (PalletDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    // Callbacks interface that all activities using this fragment must implement.
    public static interface PalletDrawerCallbacks {
       // Called when an item is dragged
        void onPalletDrawerItemDrag(ExpandableList_Child item, View view);
    }
}
