package com.homemadebazar.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.homemadebazar.util.Constants;

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

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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