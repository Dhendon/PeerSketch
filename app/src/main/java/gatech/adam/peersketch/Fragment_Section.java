package gatech.adam.peersketch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import data.Section;

public class Fragment_Section extends Fragment {

    private static final String SECTION_NAME = "sectionName";
    private String mSectionName;
    private OnFragmentInteractionListener mListener;

    public static Fragment_Section newInstance(String sectionName) {
        Fragment_Section fragment = new Fragment_Section();
        Bundle args = new Bundle();
        args.putString(SECTION_NAME, sectionName);

        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Section() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionName = getArguments().getString(SECTION_NAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_section, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
