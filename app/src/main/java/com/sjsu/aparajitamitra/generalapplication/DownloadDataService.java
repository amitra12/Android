package com.sjsu.aparajitamitra.generalapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by aparajitamitra on 4/26/17.
 */

public class DownloadDataService extends Service {
    static final int UPDATE_INTERVAL = 1000;
    private final IBinder binder = new MyBinder();
    public int counter = 0;
    public URL[] urls;
    private File file;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new DoBackgroundTask().execute(urls);

        return START_STICKY;
    }

    private int DownloadFile(URL url, int count, File folder) {
        int fileSize = 0;

        file = new File(folder, ApplicationEx.getFileType() + " " + count
                + ApplicationEx.fileExtension);
        //file = new File(String.valueOf(( new Date().getTime())));

        try {

            file.createNewFile();
            String activityName = null;

            if (ApplicationEx.fileExtension.equalsIgnoreCase(".pdf"))
                activityName = "DownloadActivity";

            String outputLog = activityName + " : Downloading " + file.getName();
            ApplicationEx.writeLogsToFile(outputLog);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {

            FileOutputStream fOutStream = new FileOutputStream(file);
            InputStream in = null;

            if (ApplicationEx.fileExtension.equalsIgnoreCase(".txt")) {
                URLConnection conn = url.openConnection();
                conn.connect();
                fileSize = conn.getContentLength();
                in = conn.getInputStream();
            } else {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.connect();
                fileSize = conn.getContentLength();
                in = conn.getInputStream();
            }

            byte[] buffer = new byte[4096];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                fOutStream.write(buffer, 0, len1);
            }
            fOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSize;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public DownloadDataService getService() {
            return DownloadDataService.this;
        }
    }

    private class DoBackgroundTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalBytesDownloaded = 0;
            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, ApplicationEx.dataType);
            folder.mkdir();
            for (int i = 0; i < count; i++) {
                totalBytesDownloaded += DownloadFile(urls[i], i + 1, folder);

                publishProgress((int) (((i + 1) / (float) count) * 100));
            }
            return totalBytesDownloaded;
        }

        protected void onProgressUpdate(Integer... progress) {

            Log.d("Downloading files", String.valueOf(progress[0])
                    + "% downloaded");

            Toast.makeText(getBaseContext(),
                    String.valueOf(progress[0]) + "% downloaded",
                    Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(Long result) {

            Toast.makeText(getBaseContext(), "Downloaded " + result + " bytes",
                    Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }
}
