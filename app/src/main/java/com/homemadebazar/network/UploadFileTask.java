package com.homemadebazar.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;


public class UploadFileTask extends AsyncTask<Void, Integer, String> implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public static final int BUFFER_SIZE = 10 * 1024;
    /**
     * The Constant lineEnd.
     */
    private static final String lineEnd = "\r\n";

    /**
     * The Constant twoHyphens.
     */
    private static final String twoHyphens = "--";

    /**
     * The Constant boundary.
     */
    private static final String boundary = "*****";
    private final String TAG = UploadFileTask.class.getSimpleName();
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog mDialog;

    private String mUrl;
    private String mFilePath;
    private Hashtable<String, String> mMultipartParams = new Hashtable<String, String>();
    private String filePartTagName = "";
    private FileUploadListener mListener;

    public UploadFileTask(Context context, String url, String filePath, Hashtable<String, String> multipartParams, String filePartTagName, FileUploadListener listener) {
        this.mContext = context;
        this.mUrl = url;
        this.mFilePath = filePath;
        this.mMultipartParams = multipartParams;
        this.filePartTagName = filePartTagName;
        this.mListener = listener;
    }

    /**
     * Gets the file name from path.
     *
     * @param filePath the file path
     * @return the file name from path
     */
    public static String getFileNameFromPath(String filePath) {
        String fileName = filePath;
        if (filePath != null) {
            int i = filePath.lastIndexOf(File.separatorChar);
            if (i != -1)
                fileName = filePath.substring(i + 1);
        }
        return fileName;
    }

    @Override
    protected final void onPreExecute() {
        super.onPreExecute();

        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Uploading File. Please wait...");
        mDialog.setIndeterminate(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
//            mDialog.setOnCancelListener(this);
//            mDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", this);
        mDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mDialog.setIndeterminate(false);
        mDialog.setMax(100);
        mDialog.setProgress(progress[0]);
    }

    @Override
    protected final String doInBackground(Void... params) {
        return uploadFile(mFilePath);
    }

    public String uploadFile(String filePath) {
        String retVal = null;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        FileInputStream fileInputStream = null;

        try {
            File file = new File(filePath);

//             if(file.exists())
//            {
            // UPLOAD IMAGE
            if (isCancelled())
                return null;

            String fileName = "";
            long fileSize = 0;
            if (!TextUtils.isEmpty(filePath)) {
                fileName = getFileNameFromPath(filePath);

                Log.d(TAG, "uploading file " + fileName);
                File temp_file = new File(filePath);
                fileSize = temp_file.length();
                Log.d(TAG, "file size " + fileSize);
                fileInputStream = new FileInputStream(temp_file);
            }
            // open a URL connection
            URL url = new URL(mUrl);

            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");
//                conn.setRequestProperty("decode", "2"); // 1=true,  2=false
//                conn.setRequestProperty("g_zip", "2"); // 1=true,  2=false
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //conn.setFixedLengthStreamingMode(contentLength);
            conn.setChunkedStreamingMode(1024);

            dos = new DataOutputStream(conn.getOutputStream());

            if (mMultipartParams != null) {
                Enumeration<String> keys = mMultipartParams.keys();
                while (keys.hasMoreElements()) {
                    String k = keys.nextElement();
                    String value = mMultipartParams.get(k);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + k + "\""
                            + lineEnd + lineEnd);
                    dos.writeBytes(value + lineEnd);
                }
            }

            // Send a binary file
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + filePartTagName + "\";filename=\""
                    + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);


            // read file and write it into form...
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            int totalSentBytes = 0;

            if (!TextUtils.isEmpty(filePath)) {
                while ((len = fileInputStream.read(buffer)) != -1) {
                    if (isCancelled()) {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Exception e) {
                                // Ignore
                            }
                        }
                        return null;
                    }
                    dos.write(buffer, 0, len);
                    totalSentBytes += len;
                    Log.d(TAG, fileName + " : SentBytes= " + totalSentBytes + " of " + fileSize);
                    if (fileSize > 0)
                        publishProgress((int) ((totalSentBytes * 100) / fileSize));
                }
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            int httpResCode = conn.getResponseCode();
            Log.d(TAG, "HTTP resCode= " + httpResCode);
            inStream = new DataInputStream(conn.getInputStream());


            // READ RESPONSE
            if (isCancelled())
                return null;
            String s = getResponseString(inStream);
            try {
                if (inStream != null)
                    inStream.close();
            } catch (Exception e) {

            }
            Log.d(TAG, "uploadFile Response: " + s + " for file= " + filePath);


            if (isCancelled())
                return null;

            // PARSE RESPONSE
            if (s != null) {
                return s;
            }
            if (isCancelled())
                return null;

//            }
        } catch (Exception e) {
            Log.e(TAG, "uploadFile ERROR " + e);
            e.printStackTrace();
            retVal = null;
        } finally {
            // close streams
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (Exception e) {
            }

            try {
                if (dos != null)
                    dos.close();
            } catch (Exception e) {
            }

            try {
                if (inStream != null)
                    inStream.close();
            } catch (Exception e) {
            }

            try {
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e2) {
            }
        }
        return retVal;
    }

    @Override
    protected final void onPostExecute(final String result) {
        mWakeLock.release();
        mDialog.dismiss();
        dispatchResponse((String) result);
    }

    private void dispatchResponse(String response) {
        if (mListener != null)
            mListener.onComplete(response);
    }

    @Override
    protected void onCancelled(String result) {
        mWakeLock.release();
        mDialog.dismiss();
        Toast.makeText(mContext, "File upload cancelled.", Toast.LENGTH_LONG).show();
        dispatchResponse(null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        cancel(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    /**
     * Gets the response string.
     *
     * @param is the is
     * @return the response string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String getResponseString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String sResponse;
        StringBuilder s = new StringBuilder();

        while ((sResponse = reader.readLine()) != null) {
            s = s.append(sResponse);
        }
        return s.toString();
    }

    public interface FileUploadListener {
        void onComplete(String response);
    }
}
