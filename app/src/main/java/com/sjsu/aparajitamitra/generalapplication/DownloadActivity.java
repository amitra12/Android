package com.sjsu.aparajitamitra.generalapplication;

/**
 * Created by aparajitamitra on 4/26/17.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class DownloadActivity extends BaseActivity {

    public DownloadDataService serviceBinder;
    private Button btnDownload;
    private Intent i;
    private boolean isBind;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            serviceBinder = ((DownloadDataService.MyBinder) service)
                    .getService();
            try {
                URL[] urls = new URL[]{
                        new URL( ((EditText)findViewById(R.id.etPdfLink1)).getText().toString())};


                serviceBinder.urls = urls;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            startService(i);
        }

        public void onServiceDisconnected(ComponentName className) {

            serviceBinder = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);


        btnDownload = (Button) findViewById(R.id.start_download);

        btnDownload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (ApplicationEx
                        .isConnectionAvailable(getApplicationContext())) {

                    //String strFolder= "GA_"+String.valueOf(( new Date().getTime()));
                    if (!ApplicationEx.isDirectoryPresent("PDFs")) {

                        if (ApplicationEx.selectedFlag == 0) {
                            ApplicationEx.dataType = "PDFs";
                            ApplicationEx.fileExtension = ".pdf";
                            isBind = true;
                            Toast.makeText(DownloadActivity.this,
                                    "Downloading PDF Files...",
                                    Toast.LENGTH_LONG).show();
                            i = new Intent(DownloadActivity.this,
                                    DownloadDataService.class);
                            boolean isbound = bindService(i, connection,
                                    Context.BIND_AUTO_CREATE);
                            System.out.println("****IsBound******" + isbound);
                        } else {
                            ApplicationEx.showDialog(DownloadActivity.this);
                        }
                    } else {
                        Toast.makeText(DownloadActivity.this,
                                "File already present", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(DownloadActivity.this,
                            "Network Error...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (isBind) {
            unbindService(connection);
        }
        super.onDestroy();
    }

}
