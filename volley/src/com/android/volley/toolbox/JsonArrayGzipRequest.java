/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * A request for retrieving a {@link JSONArray} response body at a given URL.
 */
public class JsonArrayGzipRequest extends JsonRequest<JSONArray> {

    /**
     * Creates a new request.
     * @param url URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public JsonArrayGzipRequest(String url, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        String output = "";
        try {
            //GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
            InflaterInputStream gStream = new InflaterInputStream(new ByteArrayInputStream(response.data));
            InputStreamReader reader = new InputStreamReader(gStream);
            BufferedReader in = new BufferedReader(reader);
            String read;
            while ((read = in.readLine()) != null) {
                output += read;
            }
            reader.close();
            in.close();
            gStream.close();
        } catch (IOException e) {
            //return Response.error(new ParseError());
            try {
                output = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e1) {
                return Response.error(new ParseError(e1));
            }
        }

        try {
            /*String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));*/
            return Response.success(new JSONArray(output),
                    HttpHeaderParser.parseCacheHeaders(response));
        } /*catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }*/ catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
