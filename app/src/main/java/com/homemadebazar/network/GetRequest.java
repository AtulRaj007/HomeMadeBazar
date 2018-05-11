package com.homemadebazar.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.homemadebazar.R;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by atulraj on 9/12/17.
 */

public class GetRequest extends AsyncTask<String, Void, String> {

    private String server_response;
    private Context context;
    private ApiCompleteListener apiCompleteListener;
    private Handler handler = new Handler();

    public GetRequest(Context context, ApiCompleteListener apiCompleteListener) {
        this.context = context;
        this.apiCompleteListener = apiCompleteListener;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            System.out.println(Constants.ServiceTAG.URL + strings[0]);
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(true);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();

            if (!Utils.isNetworkAvailable(context)) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.showAlert(context, context.getResources().getString(R.string.alert_no_network));
                    }
                }, 100);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.showAlert(context, e.getMessage());
                    }
                }, 100);
            }
        }

        return server_response;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println(Constants.ServiceTAG.RESPONSE + s);
        if (apiCompleteListener != null) {
            apiCompleteListener.onApiCompleteListener(s);
        }
    }


// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public interface ApiCompleteListener {
        void onApiCompleteListener(String response);
    }
}