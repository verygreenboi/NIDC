package ng.codehaven.cdc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.codehaven.cdc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {

    public static CalculatorFragment newInstance(Bundle b){
        CalculatorFragment fragment = new CalculatorFragment();
        fragment.setArguments(b);
        return fragment;
    }


    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Calculator");
    }
}
