package com.example.alex.gait_authenticator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 17/04/2018.
 */

public class DataRecorder extends AppCompatActivity implements SensorEventListener {

    private Timer timer;

    private int type = 10;

    MainActivity ma;

    private String path = Environment.getExternalStorageDirectory() + File.separator + "gauth/test/";
    private String dir10path = path + "__10/1";
    private String dir60path = path + "__60/1";
    private File dir10 = new File(dir10path);
    private File dir60 = new File(dir60path);
    private File gyrofile;
    private File accfile;
    private File MFfile;
    private File LAfile;
    private File grafile;
    private File gyrofile60;
    private File accfile60;
    private File MFfile60;
    private File LAfile60;
    private File grafile60;

    private SensorManager gyroM;

    private float xvalue;
    private float yvalue;
    private float zvalue;

    private boolean logging;

    private FileWriter gyroout;
    private BufferedWriter gyrowriter;
    private FileWriter accout;
    private BufferedWriter accwriter;
    private FileWriter MFout;
    private BufferedWriter MFwriter;
    private FileWriter LAout;
    private BufferedWriter LAwriter;
    private FileWriter graout;
    private BufferedWriter grawriter;
    private FileWriter gyroout60;
    private BufferedWriter gyrowriter60;
    private FileWriter accout60;
    private BufferedWriter accwriter60;
    private FileWriter MFout60;
    private BufferedWriter MFwriter60;
    private FileWriter LAout60;
    private BufferedWriter LAwriter60;
    private FileWriter graout60;
    private BufferedWriter grawriter60;

    public DataRecorder(SensorManager gyroo, MainActivity maa) {

        ma = maa;

        gyroM = gyroo;

        gyrofile = new File(dir10, "gyro_data");
        accfile = new File(dir10, "acc_data");
        MFfile = new File(dir10, "mag_field_data");
        LAfile = new File(dir10, "lin_acc_data");
        grafile = new File(dir10, "grav_data");

        gyrofile60 = new File(dir60, "gyro_data_60");
        accfile60 = new File(dir60, "acc_data_60");
        MFfile60 = new File(dir60, "mag_field_data_60");
        LAfile60 = new File(dir60, "lin_acc_data_60");
        grafile60 = new File(dir60, "grav_data_60");

        //delete files with previous data
        if (gyrofile.exists() || gyrofile60.exists()){
            gyrofile.delete();
            accfile.delete();
            MFfile.delete();
            LAfile.delete();
            grafile.delete();
            gyrofile60.delete();
            accfile60.delete();
            MFfile60.delete();
            LAfile60.delete();
            grafile60.delete();
        }

        //create files
        try{
            gyrofile.createNewFile();
            accfile.createNewFile();
            MFfile.createNewFile();
            LAfile.createNewFile();
            grafile.createNewFile();
            gyrofile60.createNewFile();
            accfile60.createNewFile();
            MFfile60.createNewFile();
            LAfile60.createNewFile();
            grafile60.createNewFile();
        }catch(Exception E){}

        //sensor managers for various types
        gyroM.registerListener(this, gyroM.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
        gyroM.registerListener(this, gyroM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        gyroM.registerListener(this, gyroM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
        gyroM.registerListener(this, gyroM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
        gyroM.registerListener(this, gyroM.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);

        logging = false;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (logging){
            try{
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    xvalue = sensorEvent.values[0];
                    yvalue = sensorEvent.values[1];
                    zvalue = sensorEvent.values[2];

                    if (type == 10 ){
                        accwriter.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                    else if (type == 60){
                        accwriter60.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                }
                else if (sensor.getType() == Sensor.TYPE_GYROSCOPE){
                    xvalue = sensorEvent.values[0];
                    yvalue = sensorEvent.values[1];
                    zvalue = sensorEvent.values[2];

                    if (type == 10 ){
                        gyrowriter.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                    else if (type == 60){
                        gyrowriter60.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                }
                else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                    xvalue = sensorEvent.values[0];
                    yvalue = sensorEvent.values[1];
                    zvalue = sensorEvent.values[2];

                    if (type == 10 ){
                        LAwriter.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                    else if (type == 60){
                        LAwriter60.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                }
                else if (sensor.getType() == Sensor.TYPE_GRAVITY){
                    xvalue = sensorEvent.values[0];
                    yvalue = sensorEvent.values[1];
                    zvalue = sensorEvent.values[2];

                    if (type == 10 ){
                        grawriter.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                    else if (type == 60){
                        grawriter60.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                }
                else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                    xvalue = sensorEvent.values[0];
                    yvalue = sensorEvent.values[1];
                    zvalue = sensorEvent.values[2];

                    if (type == 10 ){
                        MFwriter.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                    else if (type == 60){
                        MFwriter60.append(String.valueOf(xvalue) + ":" + String.valueOf(yvalue) + ":" + String.valueOf(zvalue) + ";");
                    }
                }
            }catch(Exception e){}
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start10(){
        //declare file writers
        try {
            //acc
            accout = new FileWriter(accfile, true);
            accwriter = new BufferedWriter(accout);

            //gyro
            gyroout = new FileWriter(gyrofile, true);
            gyrowriter = new BufferedWriter(gyroout);

            //LA
            LAout = new FileWriter(LAfile, true);
            LAwriter = new BufferedWriter(LAout);

            //gravity
            graout = new FileWriter(grafile, true);
            grawriter = new BufferedWriter(graout);

            //MF
            MFout = new FileWriter(MFfile, true);
            MFwriter = new BufferedWriter(MFout);
        }catch(Exception e){}

        type = 10;
        logging = true;
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logging = false;

                //close writers
                try{
                    //acc
                    accwriter.newLine();
                    accwriter.close();
                    accout.close();

                    //gyro
                    gyrowriter.newLine();
                    gyrowriter.close();
                    gyroout.close();

                    //LA
                    LAwriter.newLine();
                    LAwriter.close();
                    LAout.close();

                    //grav
                    grawriter.newLine();
                    grawriter.close();
                    graout.close();

                    //MF
                    MFwriter.newLine();
                    MFwriter.close();
                    MFout.close();
                    System.out.println("FINISHED!");
                }catch(Exception e){}
            }
        }, 10000);
    }

    public void start60(){
        //declare file writers
        try{
            //acc
            accout60 = new FileWriter(accfile60, true);
            accwriter60 = new BufferedWriter(accout60);

            //gyro
            gyroout60 = new FileWriter(gyrofile60, true);
            gyrowriter60 = new BufferedWriter(gyroout60);

            //LA
            LAout60 = new FileWriter(LAfile60, true);
            LAwriter60 = new BufferedWriter(LAout60);

            //gravity
            graout60 = new FileWriter(grafile60, true);
            grawriter60 = new BufferedWriter(graout60);

            //MF
            MFout60 = new FileWriter(MFfile60, true);
            MFwriter60 = new BufferedWriter(MFout60);
        }catch (Exception e){}

        type = 60;
        logging = true;
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logging = false;

                //close writers
                try{
                    //acc
                    accwriter60.newLine();
                    accwriter60.close();
                    accout60.close();

                    //gyro
                    gyrowriter60.newLine();
                    gyrowriter60.close();
                    gyroout60.close();

                    //LA
                    LAwriter60.newLine();
                    LAwriter60.close();
                    LAout60.close();

                    //grav
                    grawriter60.newLine();
                    grawriter60.close();
                    graout60.close();

                    //MF
                    MFwriter60.newLine();
                    MFwriter60.close();
                    MFout60.close();

                }catch(Exception e){}
            }
        }, 60000);
    }
}
