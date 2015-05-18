package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.codehaven.cdc.R;
import ng.codehaven.cdc.interfaces.FragmentIdentity;
import ng.codehaven.cdc.utils.Calculator;
import ng.codehaven.cdc.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalcResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalcResultFragment extends Fragment {


    public static final int ID = -1;
    public static final String TITLE = "DUTIES";
    Bundle result;
    private FragmentIdentity handler;

    public CalcResultFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CalcResultFragment newInstance(Bundle b) {
        CalcResultFragment fragment = new CalcResultFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            result = Calculator.calDuties(getArguments());
            Logger.m(result.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calc_result, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.getId(ID);
        handler.getTitle(TITLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handler = (FragmentIdentity) getActivity();
    }
}
