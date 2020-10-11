package com.example.ugd7_e_9590;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class NextActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Button btnPrev;

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    int cek = 0;

    int camBackId = Camera.CameraInfo.CAMERA_FACING_BACK;
    int camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        btnPrev = (Button)findViewById(R.id.imgClose);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(NextActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_PROXIMITY){
            if (sensorEvent.values[0] == 0){
                try {
                    mCamera.stopPreview();
                    mCamera.release();
                    cek = 1;
                    mCamera = Camera.open(camFrontId);
                }catch (Exception e){
                    Log.d("Error", "Failed to get Camera" + e.getMessage());
                }
                if (mCamera != null){
                    mCameraView = new CameraView(this, mCamera);
                    FrameLayout camera_viwe = (FrameLayout) findViewById(R.id.FLCamera);
                    camera_viwe.addView(mCameraView);
                    Toast.makeText(getApplicationContext(), "Kamera Depan", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                try {
                    if (cek == 1){
                        mCamera.stopPreview();
                        mCamera.release();
                        cek = 0;
                    }
                    mCamera = Camera.open(camBackId);
                }catch (Exception e){
                    Log.d("Error", "Failed to get Camera" + e.getMessage());
                }
                if (mCamera != null){
                    mCameraView = new CameraView(this, mCamera);
                    FrameLayout camera_viwe = (FrameLayout) findViewById(R.id.FLCamera);
                    camera_viwe.addView(mCameraView);
                    Toast.makeText(getApplicationContext(), "Kamera Belakang", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}