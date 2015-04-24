// Navigation drawer
// Select option from list view, then open corresponding fragment
package gatech.adam.peersketch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Drawer_Navigation extends Fragment {
    // Remember the position of the selected item.
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    // Per the design guidelines, you should show the drawer on launch until the user manually expands it. This shared preference tracks this.
    private static final String PREF_USER_LEARNED_DRAWER_NAVIGATION = "navigation_drawer_learned";

    // A pointer to the current callbacks instance (the Activity).
    private NavigationDrawerCallbacks mCallbacks;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER_NAVIGATION, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        // selectItem(mCurrentSelectedPosition);
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

        // Inflate navigation drawer list view, attach to activity_main/container
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_drawer_navigation, container, false);

        // Set list item click listener
        mDrawerListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectItem(position);
                }
            }
        );

        // Set list view adapter
        mDrawerListView.setAdapter(
            new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item,
                R.id.list_item_textview,
                new String[]{
                        getString(R.string.title_section0),
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }
            )
        );

        return mDrawerListView;
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
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
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
    public static interface NavigationDrawerCallbacks {
        // Called when an item in the navigation drawer is selected.
        void onNavigationDrawerItemSelected(int position);
    }
}
