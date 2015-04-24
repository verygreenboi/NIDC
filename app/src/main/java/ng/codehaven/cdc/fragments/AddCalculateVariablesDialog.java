package ng.codehaven.cdc.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.utils.Logger;
import ng.codehaven.cdc.utils.NumberTextWatcher;

public class AddCalculateVariablesDialog extends DialogFragment implements View.OnClickListener {

    static Boolean isTouched = false;

    @InjectView(R.id.rateSwitch)
    protected SwitchCompat mSwitch;
    @InjectView(R.id.rate)
    protected EditText mRate;
    @InjectView(R.id.cif)
    protected EditText mCIF;
    @InjectView(R.id.fob)
    protected EditText mFOB;
    @InjectView(R.id.calculate)
    protected Button mCalculate;
    @InjectView(R.id.cancel)
    protected Button mCancel;
    @InjectView(R.id.exchangeCurrencyLabel) protected TextView mCurTextView;

    int cif, fob, rate;
    boolean isDollars;
    DialogActions handler;

    public AddCalculateVariablesDialog() {

    }

    public static AddCalculateVariablesDialog newInstance(String title) {
        AddCalculateVariablesDialog frag = new AddCalculateVariablesDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_calculate, container);
        ButterKnife.inject(this, v);

        String title = getArguments().getString("title");

        getDialog().setTitle(title);

        mCIF.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mRate.addTextChangedListener(new NumberTextWatcher(mRate));
        mCIF.addTextChangedListener(new NumberTextWatcher(mCIF));
        mFOB.addTextChangedListener(new NumberTextWatcher(mFOB));

        mCancel.setOnClickListener(this);
        mCalculate.setOnClickListener(this);

        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        mRate.setVisibility(View.VISIBLE);
                        mRate.requestFocus();
                        mCurTextView.setText("Exchange in Naira?");
                    } else {
                        mRate.setVisibility(View.GONE);
                        mCIF.requestFocus();
                        mCurTextView.setText("Exchange in Dollars?");
                    }
                }
            }
        });

        return v;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            handler.onCancel();
        } else if (v.getId() == R.id.calculate) {
            // Do calculate
            if (!mCIF.getText().toString().trim().isEmpty()) {
                cif = sanitizedInt(mCIF.getText().toString().trim());
            } else {
                cif = 0;
            }
            if (!mFOB.getText().toString().trim().isEmpty()) {
                fob = sanitizedInt(mFOB.getText().toString().trim());
            } else {
                fob = 0;
            }
            if (!mRate.getText().toString().trim().isEmpty()) {
                rate = sanitizedInt(mRate.getText().toString().trim());
            } else {
                rate = 1;
            }

            isDollars = mSwitch.isChecked();

            handler.onCalculate(cif, fob, rate, isDollars);
        }
    }

    private int sanitizedInt(String number){

        int n = 0;

        if (!number.isEmpty()){
            n = Integer.parseInt(number.replaceAll(",", ""));
        }

        return n;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            handler = (DialogActions) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface DialogActions {
        void onCancel();

        void onCalculate(int cif, int fob, int rate, boolean isDollars);
    }

}
