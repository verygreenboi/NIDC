package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.interfaces.FragmentIdentity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    @InjectView(R.id.webview)
    protected WebView mWebView;
    private FragmentIdentity handler;
    private String mURL;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(Bundle b) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle b = getArguments();
            String title;
            int id;

            switch (b.getInt("step")) {
                case 3:
                    title = "Prohibition List";
                    id = 3;
                    break;
                case 4:
                    title = "CEMA Laws";
                    id = 4;
                    break;
                default:
                    title = "WebView";
                    id = -1;
                    break;
            }

            handler.getTitle(title);
            handler.getId(id);

            mURL = b.getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.inject(this, v);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mURL);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handler = (FragmentIdentity) getActivity();
    }
}
