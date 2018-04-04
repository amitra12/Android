package com.sjsu.aparajitamitra.generalapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aparajitamitra on 4/26/17.
 */

public class Sensors extends AppCompatActivity implements SensorEventListener {

    public Vibrator v;
    SQLiteDatabase db;
    private float lastX, lastY, lastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float vibrateThreshold = 0;
    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        Toast.makeText(getApplicationContext(), "movement identified along x-axis", Toast.LENGTH_LONG);

        System.out.println("Test");
        db = openOrCreateDatabase("SensorDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS sensor(event_date VARCHAR,x VARCHAR,y VARCHAR,z VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS movement(event_date VARCHAR,mesaage VARCHAR,x VARCHAR,y VARCHAR,z VARCHAR);");


        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
            vibrateThreshold = accelerometer.getMaximumRange() / 20;
        } else {
            // fail we dont have an accelerometer!
        }

        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initializeViews() {


        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // insert values

        Toast.makeText(getApplicationContext(), "movement identified", Toast.LENGTH_SHORT);

        db.execSQL("INSERT INTO sensor VALUES('" + event.timestamp + "','" + event.values[0] +
                "','" + event.values[1] + "','" + event.values[2] + "');");
        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values
        displayMaxValues();


        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        int sleepBreakX,sleepBreakY,sleepBreakZ;
        sleepBreakX=0;
        sleepBreakY=0;
        sleepBreakZ=0;

        // if the change is below 0.5, it is just plain noise
        if (deltaX < 0.5)
            deltaX = 0;
        if (deltaY < 0.5)
            deltaY = 0;

        TextView tvALert = (TextView) findViewById(R.id.textView);

        tvALert.setVisibility(View.INVISIBLE);
        if (deltaX > vibrateThreshold) {

            sleepBreakX++;
            if (sleepBreakX > 5) {
                Toast.makeText(getApplicationContext(), "movement identified  : Re-position along x-axis", Toast.LENGTH_LONG).show();

                //Toast.makeText(getApplicationContext(), "movement identified along x-axis", Toast.LENGTH_SHORT);

                db.execSQL("INSERT INTO movement VALUES('" + event.timestamp +
                        "','" + "Re-position along x-axis" +
                        "','" + sleepBreakX +
                        "','" + sleepBreakY +
                        "','" + sleepBreakZ + "');");

                tvALert.setText("Re-position along x-axis");
                tvALert.setVisibility(View.VISIBLE);

            }
            }


        if (deltaY > vibrateThreshold)

        {
            sleepBreakY++;
            if (sleepBreakY > 5) {
                Toast.makeText(getApplicationContext(), "movement identified  : Re-position along y-axis", Toast.LENGTH_LONG).show();

                db.execSQL("INSERT INTO movement VALUES('" + event.timestamp +
                        "','" + "Re-position along y-axis" +
                        "','" + sleepBreakX +
                        "','" + sleepBreakY +
                        "','" + sleepBreakZ + "');");
                tvALert.setText("Re-position along y-axis");
                tvALert.setVisibility(View.VISIBLE);

            }

        }


        if (deltaZ > vibrateThreshold) {

            sleepBreakZ++;
            if (sleepBreakZ > 5)
            {
                Toast.makeText(getApplicationContext(), "movement identified : Re-position along z-axis", Toast.LENGTH_LONG).show();

                db.execSQL("INSERT INTO movement VALUES('" + event.timestamp +
                        "','" + "Re-position along z-axis" +
                        "','" + sleepBreakX +
                        "','" + sleepBreakY +
                        "','" + sleepBreakZ + "');");
                tvALert.setText("Re-position along z-axis");
                tvALert.setVisibility(View.VISIBLE);

            }
        }

        if(sleepBreakX>25 || sleepBreakY>25 || sleepBreakZ>25){
            // Get instance of Vibrator from current Context
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            tvALert.setText("Multiple rotations ");
            tvALert.setVisibility(View.VISIBLE);

            // Vibrate for 500 milliseconds
            v.vibrate(500);

        }


    }


    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));

    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }

    }


}

