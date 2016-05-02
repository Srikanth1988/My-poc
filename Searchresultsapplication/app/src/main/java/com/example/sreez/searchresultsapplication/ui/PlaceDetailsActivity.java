package com.example.sreez.searchresultsapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sreez.searchresultsapplication.R;
import com.example.sreez.searchresultsapplication.model.SearchDetailsResult;
import com.example.sreez.searchresultsapplication.model.utility.Utility;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sreez on 4/30/2016.
 */
public class PlaceDetailsActivity extends Activity {


    private static final String TAG = PlaceDetailsActivity.class.getSimpleName();
    String selected_placeid;
    SearchDetailsResult detailsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);
        selected_placeid = getIntent().getStringExtra("selected");

        if (Utility.getInstance().checkStringIsNullOrEmpty(selected_placeid)) {
            prepareRequest(selected_placeid);
        }
    }

    private void prepareRequest(String place_id) {
        if (isNetworkAvailable()) {

            if (Utility.getInstance().checkStringIsNullOrEmpty(place_id)) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Utility.getInstance().getSearchDetailsUrl(place_id))
                        .build();
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        alertUserAboutError(getString(R.string.service_error_text));
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {
                            String jsonData = response.body().string();
                            Log.v(TAG, jsonData);
                            if (response.isSuccessful()) {

                                if (jsonData != null && jsonData.length() > 0) {

                                    detailsResult = new Gson().fromJson(jsonData, SearchDetailsResult.class);

                                    if (detailsResult != null) {
                                        runOnUiThread(runnable);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        else
        {
            Toast.makeText(this, R.string.network_unavailable_text,Toast.LENGTH_LONG).show();
        }
    }

    private void ShowDetails() {
        TextView textView = (TextView) findViewById(R.id.selectedItem);

        String details;

        details = (Utility.getInstance().checkStringIsNullOrEmpty(detailsResult.getResult().getFormatted_address())) ? detailsResult.getResult().getFormatted_address() : "";
        details = "\n" + details + ((Utility.getInstance().checkStringIsNullOrEmpty(detailsResult.getResult().getVicinity())) ? detailsResult.getResult().getVicinity() : "");
        details = "\n" + details + ((Utility.getInstance().checkStringIsNullOrEmpty(detailsResult.getResult().getFormatted_phone_number())) ? detailsResult.getResult().getFormatted_phone_number() : "");
        //details = "\n" + details + ((Utility.checkStringIsNullOrEmpty(detailsResult.getResult().getUrl())) ? detailsResult.getResult().getUrl() : "");

        textView.setText(details);

        if(Utility.getInstance().checkStringIsNullOrEmpty(detailsResult.getResult().getUrl()))
        {
            TextView urlTextView = (TextView) findViewById(R.id.url);
            urlTextView.setText(detailsResult.getResult().getUrl());
            Linkify.addLinks(urlTextView, Linkify.ALL);
        }

        if(Utility.getInstance().checkStringIsNullOrEmpty(detailsResult.getResult().getWebsite()))
        {
            TextView websiteTextView = (TextView) findViewById(R.id.site);
            websiteTextView.setText(detailsResult.getResult().getWebsite());
            Linkify.addLinks(websiteTextView,Linkify.ALL);
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ShowDetails();
        }
    };

    private void alertUserAboutError(String msg) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), msg);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!=null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }

}
