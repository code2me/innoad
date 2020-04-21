package com.example.gpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gpstracking.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements SensorEventListener  {

    private static final String TAG = "HomeActivity";
    Sensor accelerometer;
    TextView xValue, yValue, zValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_main);

        xValue = findViewById(R.id.xValue);
        yValue =  findViewById(R.id.yValue);
        zValue =  findViewById(R.id.zValue);



        Log.d(TAG, "onCreate : Initializing Sensor Services");
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(HomeActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listener");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged (final SensorEvent sensorEvent){
        Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
        xValue.setText((int) sensorEvent.values[0]);
        yValue.setText((int) sensorEvent.values[1]);
        zValue.setText((int) sensorEvent.values[2]);

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });


        if((sensorEvent.values[0] >= 27) || (sensorEvent.values[1] >= 27) || (sensorEvent.values[2] >= 27)){

            //send location to firebase
            Intent i = new Intent(HomeActivity.this, LocationService.class);
            startActivity(i);
        }

        /*catch(MalformedURLException e){}
        catch(IOException e){}*/
    }


}

