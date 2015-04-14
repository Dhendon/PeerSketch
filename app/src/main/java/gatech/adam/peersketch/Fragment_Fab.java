package gatech.adam.peersketch;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import data.Section;

public class Fragment_Fab extends Fragment {
    private Activity_Main mActivity;
    private FloatingActionButton mFabRoot, mFabSection, mFabGroup, mFabIf, mFabFor;
    private ArrayList<FloatingActionButton> mFabList = new ArrayList<FloatingActionButton>();
    private boolean isHidden = true;

    // Returns new instance of song fragment
    public static Fragment_Fab newInstance() {
        return new Fragment_Fab();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting handle to main activity
        mActivity = (Activity_Main) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflating rootView
        View rootView = inflater.inflate(R.layout.fragment_fab, container, false);

        // Getting fab root handle, setting color
        mFabRoot = (FloatingActionButton) rootView.findViewById(R.id.fabRoot);
        mFabRoot.setColorNormal(getResources().getColor(R.color.red));

        // Getting other fab handles, setting color, adding to fab list
        mFabFor = (FloatingActionButton) rootView.findViewById(R.id.fabFor);
        mFabFor.setColorNormal(getResources().getColor(R.color.blue));
        mFabList.add(mFabFor);

        mFabIf = (FloatingActionButton) rootView.findViewById(R.id.fabIf);
        mFabIf.setColorNormal(getResources().getColor(R.color.blue));
        mFabList.add(mFabIf);

        mFabGroup = (FloatingActionButton) rootView.findViewById(R.id.fabGroup);
        mFabGroup.setColorNormal(getResources().getColor(R.color.red));
        mFabList.add(mFabGroup);

        mFabSection = (FloatingActionButton) rootView.findViewById(R.id.fabSection);
        mFabSection.setColorNormal(getResources().getColor(R.color.red));
        mFabList.add(mFabSection);

        // Setting fab root listener
        mFabRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility();
            }
        });

        // Setting fab section listener
        mFabSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSection(new Section("New Section"));
            }
        });

        // Collapsing fabs
        hide();

        return rootView;
    }

    public void addSection(Section section) {
        // TODO:
    }

    public void toggleVisibility() {
        if (isHidden) { show(); }
        else { hide(); }
    }

    public void hide() {
        // Shrinking fabs
        for (FloatingActionButton fab : mFabList) {
            // Inflating animator
            AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                    R.animator.animator_scale_down);

            // Playing animation
            anim.setTarget(fab);
            anim.start();
        }

        // Rotating fabRoot
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_to_0);
        mFabRoot.startAnimation(anim);

        // Updating current state
        isHidden = true;
    }

    public void show() {
        // Growing fabs
        for (FloatingActionButton fab : mFabList) {
            // Inflating animator
            AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                    R.animator.animator_scale_up);

            // Playing animation
            anim.setTarget(fab);
            anim.start();
        }

        // Rotating fabRoot
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_to_45);
        anim.setFillAfter(true);
        mFabRoot.startAnimation(anim);

        // Updating current state
        isHidden = false;
    }





}
