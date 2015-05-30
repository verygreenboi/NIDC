package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.interfaces.FragmentIdentity;
import ng.codehaven.cdc.utils.NumberTextWatcher;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {

    public static final int ID = 0;
    public static final String TITLE = "Calculator";
    @InjectView(R.id.naira)
    protected RadioButton mNairaBtn;
    @InjectView(R.id.dollars)
    protected RadioButton mDollarBtn;
    @InjectView(R.id.exchangeRate)
    protected EditText mExchange;
    @InjectView(R.id.fob)
    protected EditText mFOB;
    @InjectView(R.id.cif)
    protected EditText mCIF;
    @InjectView(R.id.duty)
    protected EditText mDuty;
    @InjectView(R.id.levy)
    protected EditText mLevy;
    @InjectView(R.id.vat)
    protected EditText mVAT;
    @InjectView(R.id.btn_calculate)
    protected Button mCalcBtn;
    private boolean checked;
    private boolean isDollars;
    private FragmentIdentity handler;
    private doCalculate calc;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public static CalculatorFragment newInstance(Bundle b){
        CalculatorFragment fragment = new CalculatorFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.inject(this, v);

        mFOB.addTextChangedListener(new NumberTextWatcher(mFOB));
        mCIF.addTextChangedListener(new NumberTextWatcher(mCIF));

        mNairaBtn.setOnClickListener(this);
        mDollarBtn.setOnClickListener(this);
        mCalcBtn.setOnClickListener(this);

        return v;
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
        calc = (doCalculate) getActivity();
    }

    public void onRadioButtonClicked(View v) {
        checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.naira:
                // Do Naira
                if (checked) {
                    isDollars = false;
                    if (mExchange.getVisibility() == View.VISIBLE) {
                        mExchange.setVisibility(View.GONE);
                        mFOB.requestFocus();
                    }
                }
                break;
            case R.id.dollars:
                //Do dollars
                if (checked) {
                    isDollars = true;
                    if (mExchange.getVisibility() == View.GONE) {
                        mExchange.setVisibility(View.VISIBLE);
                        mExchange.requestFocus();
                    }
                }
                break;
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.naira || v.getId() == R.id.dollars) {
            onRadioButtonClicked(v);
        } else if (v.getId() == mCalcBtn.getId()) {
            doCalcSubmit();
        }
    }

    private void doCalcSubmit() {
        Bundle b = getBundle();
        calc.doCalc(b);
    }

    private Bundle getBundle() {
        Bundle b = new Bundle();
        String xr;

        xr = String.valueOf(getXr());

        b.putString("xr", xr);

        String fob;

        fob = getFob();

        b.putString("fob", fob);

        String cif;

        cif = getCIF();

        b.putString("cif", cif);

        int duty;

        duty = getDuty();

        b.putInt("duty", duty);

        int vat;

        vat = getVAT();

        b.putInt("vat", vat);

        int levy;

        levy = getLevy();

        b.putInt("levy", levy);

        b.putBoolean("isDollars", isDollars);

        return b;
    }

    private int getLevy() {
        int levy = 0;
        if (!mLevy.getText().toString().trim().isEmpty()) {
            levy = Integer.parseInt(mLevy.getText().toString().trim());
        }
        return levy;
    }

    private int getVAT() {
        int vat = 0;
        if (!mVAT.getText().toString().trim().isEmpty()) {
            vat = Integer.parseInt(mVAT.getText().toString().trim());
        }
        return vat;
    }

    private int getDuty() {
        int duty = 0;
        if (!mDuty.getText().toString().trim().isEmpty()) {
            duty = Integer.parseInt(mDuty.getText().toString().trim());
        }
        return duty;
    }

    private String getCIF() {
        String cif = String.valueOf(0);
        if (!mCIF.getText().toString().isEmpty()) {
            cif = SanitizedText(mCIF.getText().toString());
        }
        return cif;
    }

    private String getFob() {
        String fob = String.valueOf(0);
        if (!mFOB.getText().toString().trim().isEmpty()) {
            fob = SanitizedText(mFOB.getText().toString().trim());
        }
        return fob;
    }

    private String SanitizedText(String trim) {
        String t;
        if (!trim.isEmpty()) {
            t = trim.replaceAll(",", "");
        } else {
            t = "0";
        }
        return t;
    }

    private double getXr() {
        double xr = 1;
        if (!mExchange.getText().toString().trim().isEmpty()) {
            xr = Double.parseDouble(mExchange.getText().toString().trim());
        }
        return xr;
    }

    public interface doCalculate {
        public void doCalc(Bundle b);
    }
}
