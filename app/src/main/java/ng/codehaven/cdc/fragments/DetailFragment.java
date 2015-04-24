package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_ID = "objectId";
    public static final String ARG_CET = "cet";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_IMPORT_DUTY = "import_duty";
    public static final String ARG_VAT = "vat";
    public static final String ARG_LEVY = "levy";
    public static final String ARG_FAV = "favorite";

    private String mId;
    private String mCet;
    private String mDesc;
    private int mImportDuty;
    private int mVat;
    private int mLevy;

    private Bundle mItem;

    private doCalculate handler;

    @InjectView(R.id.importDuty) protected TextView mImportDutyView;
    @InjectView(R.id.vat) protected TextView mVATView;
    @InjectView(R.id.levy) protected TextView mLevyView;
    @InjectView(R.id.calculate) protected Button mCalBtn;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param b Item in a bundle
     *
     * @return A new instance of fragment DetailFragnent.
     */
    public static DetailFragment newInstance(Bundle b) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = getArguments();
            mId = getArguments().getString(ARG_ID);
            mCet = getArguments().getString(ARG_CET);
            mDesc = getArguments().getString(ARG_DESCRIPTION);
            mImportDuty = getArguments().getInt(ARG_IMPORT_DUTY);
            mVat = getArguments().getInt(ARG_VAT);
            mLevy = getArguments().getInt(ARG_LEVY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_fragnent, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImportDutyView.setText(String.valueOf(mImportDuty));
        mVATView.setText(String.valueOf(mVat));
        mLevyView.setText(String.valueOf(mLevy));
        mCalBtn.setOnClickListener(this);
    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity Attached activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        handler = (doCalculate) getActivity();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.calculate){
            handler.handleCalculateClick(getArguments());
        }
    }

    public interface doCalculate{
        void handleCalculateClick(Bundle item);
    }


}
