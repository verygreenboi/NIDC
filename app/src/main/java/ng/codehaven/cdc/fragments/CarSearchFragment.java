package ng.codehaven.cdc.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.interfaces.OnCarFragmentInteractionListener;
import ng.codehaven.cdc.models.Car;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCarFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarSearchFragment#newInstanceWithBundle} factory method to
 * create an instance of this fragment.
 */
public class CarSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final String YEAR = "year";
    private static final String USED = "used";

    @InjectView(R.id.spinner_brand)
    Spinner mBrandSpinner;
    @InjectView(R.id.txt_model)
    TextView mModelLabel;
    @InjectView(R.id.spinner_model)
    Spinner mModelSpinner;
    @InjectView(R.id.txt_year)
    TextView mYearLabel;
    @InjectView(R.id.spinner_year)
    Spinner mYearSpinner;
    @InjectView(R.id.fab)
    FloatingActionButton mFab;
    @InjectView(R.id.rateSwitch)
    SwitchCompat mUsedSwitch;
    @InjectView(R.id.txt_used)
    TextView mUsedLabel;
    List<Car> brandList, modelList;
    private String mBrand;
    private String mModel;
    private String mYear;
    private boolean mUsed;
    private boolean hasBundle;
    private OnCarFragmentInteractionListener mListener;
    private Realm realm;
    private int step;
    private boolean isTouched;
    private boolean isUsed;

    public CarSearchFragment () {
        // Required empty public constructor
    }

    public static CarSearchFragment newInstanceWithBundle (String brand, String model, String year, boolean used) {
        CarSearchFragment fragment = new CarSearchFragment();
        Bundle args = new Bundle();
        args.putString(BRAND, brand);
        args.putString(MODEL, model);
        args.putString(YEAR, year);
        args.putBoolean(USED, used);
        fragment.setArguments(args);
        return fragment;
    }

    public static CarSearchFragment newInstance () {
        return new CarSearchFragment();
    }

    public void onButtonPressed (Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCarFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getInstance(getActivity());

        if (getArguments() != null) {
            hasBundle = true;
            mBrand = getArguments().getString(BRAND);
            mYear = getArguments().getString(YEAR);
            mModel = getArguments().getString(MODEL);
            mUsed = getArguments().getBoolean(USED);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_search, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBrandSpinner();
        mUsedSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });
        mUsedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        mUsedLabel.setText("Used?");
                        isUsed = false;
                    } else {
                        mUsedLabel.setText("New?");
                        isUsed = true;
                    }
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                switch (step) {
                    case 0:
                        mListener.carListInteraction(brandList, isUsed);
                        break;
                    case 1:
                        mListener.carListInteraction(modelList, isUsed);
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    private void setupBrandSpinner () {
        brandList = realm.where(Car.class).findAll();
        List<String> brandStringList = new ArrayList<>();
        for (Car c : brandList) {
            brandStringList.add(c.getBrandName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, brandStringList);
        mBrandSpinner.setAdapter(spinnerAdapter);
        mBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                mBrand = tv.getText().toString();
                setUpModelSpinner(tv.getText());
                step = 0;
                mFab.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setStartDelay(400)
                        .start();
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
    }

    private void setUpModelSpinner (CharSequence text) {
        mModelLabel.animate().scaleX(1).scaleY(1).setStartDelay(400).start();
        mModelSpinner.animate().scaleX(1).scaleY(1).setStartDelay(600).start();

        brandList = realm.where(Car.class).equalTo("brandName", text.toString()).findAll();

        List<String> modelStringList = new ArrayList<>();

        for (Car c : brandList) {
            modelStringList.add(c.getModel());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, modelStringList);

        mModelSpinner.setAdapter(adapter);
        mModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                mModel = tv.getText().toString();
                step = 1;
                setupYear(mBrand, mModel);
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
    }

    private void setupYear (String brand, String model) {
        mYearLabel.animate().scaleX(1).scaleY(1).setStartDelay(400).start();
        mYearSpinner.animate().scaleX(1).scaleY(1).setStartDelay(600).start();

        modelList = realm.where(Car.class).equalTo("brandName", brand).equalTo("model", model).findAll();
        List<String> yearStringList = new ArrayList<>();

        for (Car c : modelList) {
            yearStringList.add(c.getYear());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, yearStringList);
        mYearSpinner.setAdapter(adapter);
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                mYear = tv.getText().toString();

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onDetach () {
        super.onDetach();
        mListener = null;
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected (AdapterView<?> parent) {

    }
}
