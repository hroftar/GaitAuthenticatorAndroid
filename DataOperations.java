package com.example.alex.gait_authenticator;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 17/04/2018.
 */

public class DataOperations {

    String prepath = Environment.getExternalStorageDirectory() + File.separator + "gauth/";

    public DataOperations(){
    }

    public void load_set(String folder, HashMap<String, ArrayList<ArrayList<Double>>> dsetX, HashMap<String, ArrayList<ArrayList<Double>>> dsetY, HashMap<String, ArrayList<ArrayList<Double>>> dsetZ){
        ArrayList<ArrayList<Double>> setX = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> setY = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> setZ = new ArrayList<ArrayList<Double>>();
        String folderPath = prepath + folder + "/__10/";
        File dir = new File(folderPath);
        if (dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                String filepath = folderPath + f.getName() + "/acc_data";
                ArrayList<Double> x = new ArrayList<Double>();
                ArrayList<Double> y = new ArrayList<Double>();
                ArrayList<Double> z = new ArrayList<Double>();
                String line = ";";
                try {
                    FileReader file = new FileReader(filepath);
                    BufferedReader br = new BufferedReader(file);
                    line = br.readLine();
                    br.close();
                    String[] readings = line.split(";");
                    for (String reading : readings) {
                        String[] s = reading.split(":");
                        if (s.length == 3) {
                            x.add(Double.parseDouble(s[0]));
                            y.add(Double.parseDouble(s[1]));
                            z.add(Double.parseDouble(s[2]));
                        }
                    }
                    setX.add(x);
                    setY.add(y);
                    setZ.add(z);
                } catch (Exception e) {
                }
            }
        }
        folderPath = prepath + folder + "/__60/";
        dir = new File(folderPath);
        if (dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                String filepath = folderPath + f.getName() + "/acc_data_60";
                ArrayList<Double> x = new ArrayList<Double>();
                ArrayList<Double> y = new ArrayList<Double>();
                ArrayList<Double> z = new ArrayList<Double>();
                String line = ";";
                try {
                    FileReader file = new FileReader(filepath);
                    BufferedReader br = new BufferedReader(file);
                    line = br.readLine();
                    br.close();
                    String[] readings = line.split(";");
                    for (String reading : readings) {
                        String[] s = reading.split(":");
                        if (s.length == 3) {
                            x.add(Double.parseDouble(s[0]));
                            y.add(Double.parseDouble(s[1]));
                            z.add(Double.parseDouble(s[2]));
                        }
                    }
                    int partLen = x.size() / 6;
                    for (int i = 1; i < 6; i++) {
                        setX.add(new java.util.ArrayList<Double>(x.subList((i - 1) * partLen, i * partLen)));
                        setY.add(new java.util.ArrayList<Double>(y.subList((i - 1) * partLen, i * partLen)));
                        setZ.add(new java.util.ArrayList<Double>(z.subList((i - 1) * partLen, i * partLen)));
                    }
                    setX.add(new java.util.ArrayList<Double>(x.subList(5 * partLen, x.size())));
                    setY.add(new java.util.ArrayList<Double>(y.subList(5 * partLen, y.size())));
                    setZ.add(new java.util.ArrayList<Double>(z.subList(5 * partLen, z.size())));
                } catch (Exception e) {
                }
            }
        }
        dsetX.put(folder, setX);
        dsetY.put(folder, setY);
        dsetZ.put(folder, setZ);
    };
}
