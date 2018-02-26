package com.homemadebazar.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Atul on 8/16/17.
 */

public class DownloadFile extends AsyncTask<String, String, Void> {
    boolean isDownloaded = false;
    String filePath = "";
    String name = "";
    private Context mContext;

    public DownloadFile(Context mContext) {
        this.mContext = mContext;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    protected Void doInBackground(String... URL) {
        int count;
        try {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/HMB");

            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            java.net.URL url = new URL(URL[0]);
            String type = getMimeType(url.toString());
            Log.d("Type ", type + "");

            long time = System.currentTimeMillis();
            if (!TextUtils.isEmpty(type)) {
                if (type.contains("jpeg")) {
                    name = time + ".jpg";
                    myDir = new File(myDir, name);
                } else if (type.contains("mp4")) {
                    name = time + ".mp4";
                    myDir = new File(myDir, name);
                } else if (type.contains("png")) {
                    name = time + ".png";
                    myDir = new File(myDir, name);
                } else if (type.contains("3gpp")) {
                    name = time + ".3gp";
                    myDir = new File(myDir, name);
                }
            } else {
                name = time + ".jpg";
                myDir = new File(myDir, name);
            }
            downloadFile(url, myDir);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            isDownloaded = false;
        }
        return null;
    }

    void downloadFile(URL url, File outputFile) {
        HttpURLConnection con = null;
        FileOutputStream fos = null;
        InputStream is = null;
        filePath = outputFile.getAbsolutePath();
        try {
            con = (HttpURLConnection) url.openConnection();
            con.connect();

            fos = new FileOutputStream(outputFile);

            int status = con.getResponseCode();//my doctory
            Log.d("RespondCode", "status " + status);

            if (status != HttpURLConnection.HTTP_OK) {
                isDownloaded = false;
                Log.d("Connection", "Error in url connection");
            } else {
                isDownloaded = true;
                is = con.getInputStream();
                //  InputStream is = con.getInputStream();
                int fileLength = con.getContentLength();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                int total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                        /*if (isCancelled()) {
                            is.close();
                            return null;
                        }*/
                    total += len1;
                    if (fileLength > 0) {
                        publishProgress(String.valueOf((total * 100) / fileLength));
                    }
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
                con.disconnect();
            }


        } catch (Exception e) {
            isDownloaded = false;
            Log.d("error", "sss " + e);
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
            } catch (IOException ignored) {
            }

            if (con != null)
                con.disconnect();
        }
    }

    @Override
    protected void onPreExecute() {
        isDownloaded = false;
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("Downloading", values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isDownloaded) {
            Toast.makeText(mContext, "File Downloaded...", Toast.LENGTH_SHORT).show();
            addImageToGallery(mContext.getContentResolver(), "image/jpg;image/jpeg", filePath);
        } else {
            Toast.makeText(mContext, "File Download Error...", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri addImageToGallery(ContentResolver cr, String mimeType, String filepath) {
        ContentValues values = new ContentValues();
        if (mimeType.contains("3gp")) {
            values.put(MediaStore.Video.Media.TITLE, "player");
            values.put(MediaStore.Video.Media.DISPLAY_NAME, "player");
            values.put(MediaStore.Video.Media.DESCRIPTION, "");
            values.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.DATA, filepath);
            return cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        } else {
            values.put(MediaStore.Images.Media.TITLE, "player");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "player");
            values.put(MediaStore.Images.Media.DESCRIPTION, "");
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATA, filepath);
            return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

    }
}
