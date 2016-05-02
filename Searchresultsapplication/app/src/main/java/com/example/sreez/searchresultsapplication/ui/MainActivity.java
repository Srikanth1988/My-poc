package com.example.sreez.searchresultsapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sreez.searchresultsapplication.R;
import com.example.sreez.searchresultsapplication.model.RawSearchResultResponse;
import com.example.sreez.searchresultsapplication.model.SearchResult;
import com.example.sreez.searchresultsapplication.model.utility.Utility;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.spdy.FrameReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    RawSearchResultResponse rawSearchResult;
    Handler handler;
    OneShotTask oneShotTask;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        oneShotTask = new OneShotTask();

        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(query!=null && !query.isEmpty())
                            callSearch(query);
                        return false;
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    handler.removeCallbacks(oneShotTask);
                    if (newText != null && !newText.isEmpty()) {
                        oneShotTask.setVal(newText);
                        handler.postDelayed(oneShotTask, 3000);
                    } else {
                       if(listView!=null) {
                            CustomPlaceAdapter adapter = (CustomPlaceAdapter) listView.getAdapter();
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.e(TAG, "Exception occured", ex);
                }
                return false;
            }



        });
    }

    public void callSearch(String query) {

        processRequest(query);
    }

    private void processRequest(String query) {
        if(isNetworkAvailable()) {

            if(Utility.getInstance().checkStringIsNullOrEmpty(query)) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Utility.getInstance().getSearchPlacesUrl(query))
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
                                //JSONObject searchResult = new JSONObject(jsonData);
                                //JSONArray objArray = searchResult.getJSONArray("predictions");

                                if (jsonData != null && jsonData.length() > 0 && (new JSONObject(jsonData).getString("status").equals("OK"))) {
                                    rawSearchResult = new Gson().fromJson(jsonData, RawSearchResultResponse.class);

                                    if (rawSearchResult != null) {
                                        runOnUiThread(runnable);
                                    }
                                }
                                else
                                {
                                    alertUserAboutError(getString(R.string.no_results_text));
                                }

                                Log.v(TAG, jsonData);
                            } else {
                                alertUserAboutError(getString(R.string.service_error_text));
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Exception occured", e);
                        } catch (JSONException ex) {
                            Log.e(TAG, "Json Exception occured", ex);
                        }

                    }
                });
            }
            else
            {
                alertUserAboutError("Enter proper search query");
            }
        }
        else
        {
            Toast.makeText(this, R.string.network_unavailable_text,Toast.LENGTH_LONG).show();
        }
    }


    private void alertUserAboutError(String msg) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),msg);
    }

    public void PrepareListView()
    {
        if(rawSearchResult.getPredictions().isEmpty())
        {
            alertUserAboutError(getString(R.string.no_items_found_text));
        }
        listView = (ListView) findViewById(R.id.search_list_view);
        CustomPlaceAdapter adapter = new CustomPlaceAdapter(MainActivity.this, R.layout.search_result_template, rawSearchResult.getPredictions());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, PlaceDetailsActivity.class);
                intent.putExtra("selected",rawSearchResult.getPredictions().get(position).getPlaceID());
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            PrepareListView();
        }
    };



    class OneShotTask implements Runnable {
        String newText;
        OneShotTask(){}
        public void setVal(String s){
            newText=s;
        }
        public void run() {
            if(Utility.getInstance().checkStringIsNullOrEmpty(newText)) {
                callSearch(newText);
            }
        }
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
