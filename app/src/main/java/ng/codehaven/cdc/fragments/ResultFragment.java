package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    private static final int ID = -2;
    private static final String TITLE = "RESULT";
    @InjectView(R.id.duty)
    protected TextView mDuty;
    @InjectView(R.id.surcharge)
    protected TextView mSurcharge;
    @InjectView(R.id.etls)
    protected TextView mETLS;
    @InjectView(R.id.ciss)
    protected TextView mCISS;
    @InjectView(R.id.levy)
    protected TextView mLevy;
    @InjectView(R.id.vat)
    protected TextView mVAT;
    @InjectView(R.id.total)
    protected TextView mTotal;


    String mDutyString, mSurchargeString, mETLSString, mCISSString, mLevyString, mVATString, mTotalString;


    public ResultFragment() {
        // Required empty public constructor
    }
    public static ResultFragment newInstance(Bundle b) {
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();

            Logger.m(b.toString());

            mDutyString = formatNumber(Double.parseDouble(b.getString("dutyResult")));
            mSurchargeString = formatNumber(Double.parseDouble(b.getString("surchargeResult")));
            mETLSString = formatNumber(Double.parseDouble(b.getString("etlsResult")));
            mCISSString = formatNumber(Double.parseDouble(b.getString("cissResult")));
            mLevyString = formatNumber(Double.parseDouble(b.getString("levyResult")));
            mVATString = formatNumber(Double.parseDouble(b.getString("vatResult")));

            mTotalString = formatNumber(Double.parseDouble(b.getString("totalResult")));

        } else {
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.inject(this, v);

        mDuty.setText(mDutyString);
        mSurcharge.setText(mSurchargeString);
        mETLS.setText(mETLSString);
        mCISS.setText(mCISSString);
        mLevy.setText(mLevyString);
        mVAT.setText(mVATString);

        mTotal.setText(mTotalString);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public String formatNumber(double n) {
        Locale nigeria = new Locale("en", "NG");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(nigeria);

        return formatter.format(n);
    }

}
