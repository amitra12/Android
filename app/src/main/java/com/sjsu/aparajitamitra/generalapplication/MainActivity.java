package com.sjsu.aparajitamitra.generalapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    Button map,service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button launchWebsite = (Button) findViewById(R.id.runWebsite);

        //map= (Button) findViewById(R.id.map);
        btnListener();

        final EditText searchText = (EditText) findViewById(R.id.searchText);
        launchWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                String strURL = "https://www.google.com/#q=" + searchText.getText().toString();

                Intent implicit = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(strURL));
                startActivity(implicit);

                // Case True : : Validate if URL is valid URL using URLUtil class isValidUrl method.
                if (URLUtil.isValidUrl(strURL)) {

//                    Intent implicit = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(strURL));
//                    startActivity(implicit);
                }


                // Case false : : For all invalid URL's use cases intimate user
                else {
                    searchText.setText("Enter valid URL");
                }

            }
        });
    }

    public void btnListener() {

        final Context context = this;

        ImageView sensor = (ImageView) findViewById(R.id.button);
        Button map = (Button) findViewById(R.id.map);
        service = (Button) findViewById(R.id.service);

        sensor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //open Sensor Activity
                Intent intent = new Intent(context, Sensors.class);
                startActivityFromChild((Activity) context, intent, 1);


            }
        });

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //open Google Map Activity
                Intent intent = new Intent(context, MapsActivity.class);
                startActivityFromChild((Activity) context, intent, 1);


            }
        });


        service.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //open Google Map Activity
                Intent intent = new Intent(context, DownloadActivity.class);
                startActivityFromChild((Activity) context, intent, 1);


            }
        });
    }
}
