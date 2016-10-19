package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
public class CalculatorFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

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
    InputMethodManager manager;
    private boolean checked;
    private boolean isDollars;
    private FragmentIdentity handler;
    private doCalculate calc;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public static CalculatorFragment newInstance (Bundle b) {
        CalculatorFragment fragment = new CalculatorFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        handler = (FragmentIdentity) getActivity();
        calc = (doCalculate) getActivity();
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick (View v) {

        if (v.getId() == R.id.naira || v.getId() == R.id.dollars) {
            onRadioButtonClicked(v);
        } else if (v.getId() == mCalcBtn.getId()) {
            assert manager != null;
            manager.hideSoftInputFromWindow(mVAT.getWindowToken(), 0);
            doCalcSubmit();
        }
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

    private double getXr () {
        double xr = 1;
        if (!mExchange.getText().toString().trim().isEmpty()) {
            xr = Double.parseDouble(mExchange.getText().toString().trim());
        }
        return xr;
    }

    private String getFob () {
        String fob = String.valueOf(0);
        if (!mFOB.getText().toString().trim().isEmpty()) {
            fob = SanitizedText(mFOB.getText().toString().trim());
        }
        return fob;
    }

    private String getCIF () {
        String cif = String.valueOf(0);
        if (!mCIF.getText().toString().isEmpty()) {
            cif = SanitizedText(mCIF.getText().toString());
        }
        return cif;
    }

    private int getDuty() {
        int duty = 0;
        if (!mDuty.getText().toString().trim().isEmpty()) {
            duty = Integer.parseInt(mDuty.getText().toString().trim());
        }
        return duty;
    }

    private int getVAT () {
        int vat = 0;
        if (!mVAT.getText().toString().trim().isEmpty()) {
            vat = Integer.parseInt(mVAT.getText().toString().trim());
        }
        return vat;
    }

    private int getLevy () {
        int levy = 0;
        if (!mLevy.getText().toString().trim().isEmpty()) {
            levy = Integer.parseInt(mLevy.getText().toString().trim());
        }
        return levy;
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

    @Override
    public void onFocusChange (View view, boolean b) {
        int id = view.getId();
        if (id == mFOB.getId()) {
            mFOB.setHint(getAltHint(mFOB, b));
        } else if (id == mCIF.getId()) {
            mCIF.setHint(getAltHint(mCIF, b));
        }
    }

    private String getAltHint (EditText et, boolean hasFocus) {
        String hint = null;
        if (et.getId() == mFOB.getId()) {
            if (hasFocus) {
                hint = "Freight on board";
            } else {
                hint = "FOB";
            }
        } else if (et.getId() == mCIF.getId()) {
            if (hasFocus) {
                hint = "Cost Insurance Freight";
            } else {
                hint = "CIF";
            }
        }
        return hint;
    }

    public interface doCalculate {
        void doCalc (Bundle b);
    }
}
