package ng.codehaven.cdc.fragments;


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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param b Item Result Bundle.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
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

            mDutyString = formatNumber(b.getInt("dutyResult"));
            mSurchargeString = formatNumber(b.getInt("surchargeResult"));
            mETLSString = formatNumber(b.getInt("etlsResult"));
            mCISSString = formatNumber(b.getInt("cissResult"));
            mLevyString = formatNumber(b.getInt("levyResult"));
            mVATString = formatNumber(b.getInt("vatResult"));

            mTotalString = formatNumber(b.getInt("totalResult"));

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

    public String formatNumber(int n) {
        Locale nigeria = new Locale("en", "NG");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(nigeria);

        return formatter.format(n);
    }

}
