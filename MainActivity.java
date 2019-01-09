package com.example.alex.gait_authenticator;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    DataOperations DO;
    KNN knn;
    Calculations calc;
    TextView tv;
    DataRecorder DR;
    Button rec;
    Button pred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DO = new DataOperations();
        knn = new KNN();
        calc = new Calculations();
        tv = findViewById(R.id.textView);
        DR = new DataRecorder((SensorManager) getSystemService((Context.SENSOR_SERVICE)), this);
        rec = findViewById(R.id.button);
        //rec.setEnabled(true);
        pred = findViewById(R.id.button2);
        pred.setEnabled(false);
        View mv = findViewById(android.R.id.content);
       //mv.setBackgroundColor(Color.BLACK);

    }

    public void collect(View view ){
        tv.setText("KEEP WALKING!!!");
        rec.setEnabled(false);
        DR.start60();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtons();
            }
        }, 60200);
    }

    public void setView(String msg){
        tv.setText(msg);
    }

    public void setButtons(){
        pred.setEnabled(true);
        setView("DONE! PRESS PREDICT!");
        //(ViewGroup) findViewById(R.id.)
    }

    public void predict(View view){
        pred.setEnabled(false);
        //TRAIN STAGE
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetX = new HashMap<String, ArrayList<ArrayList<Double>>>();
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetY = new HashMap<String, ArrayList<ArrayList<Double>>>();
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetZ = new HashMap<String, ArrayList<ArrayList<Double>>>();

        DO.load_set("reject", signalSetX, signalSetY, signalSetZ);
        DO.load_set("accept", signalSetX, signalSetY, signalSetZ);

        //produce smoothed signals with Daubechies wavelets transform
        HashMap<String, ArrayList<ArrayList<Double>>> smoothX = calc.smooth(signalSetX);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothY = calc.smooth(signalSetY);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothZ = calc.smooth(signalSetZ);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothC = calc.combine(smoothX, smoothY, smoothZ);

        //detect picks and true picks of smoothed signals
        HashMap<String, ArrayList<ArrayList<Double>>> tpy = calc.picks_det(smoothY);
        HashMap<String, ArrayList<ArrayList<Double>>> tpz = calc.picks_det(smoothZ);
        HashMap<String, ArrayList<ArrayList<Double>>> tpc = calc.picks_det(smoothC);

        //extract GC from sets of true picks
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcy = calc.egc_di(smoothY, tpy);
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcz = calc.egc_di(smoothZ, tpz);
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcc = calc.egc_di(smoothC, tpc);

        //extract sets of features
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetY = calc.features(smoothY, gcy);
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetZ = calc.features(smoothZ, gcz);
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetC = calc.features(smoothC, gcc);

        //TEST PREP
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetXtest = new HashMap<String, ArrayList<ArrayList<Double>>>();
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetYtest = new HashMap<String, ArrayList<ArrayList<Double>>>();
        HashMap<String, ArrayList<ArrayList<Double>>> signalSetZtest = new HashMap<String, ArrayList<ArrayList<Double>>>();

        DO.load_set("test", signalSetXtest, signalSetYtest, signalSetZtest);

        //produce smoothed signals with Daubechies wavelets transform
        HashMap<String, ArrayList<ArrayList<Double>>> smoothXtest = calc.smooth(signalSetXtest);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothYtest = calc.smooth(signalSetYtest);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothZtest = calc.smooth(signalSetZtest);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothCtest = calc.combine(smoothXtest, smoothYtest, smoothZtest);

        System.out.println(smoothYtest.get("test"));

        //detect picks and true picks of smoothed signals
        HashMap<String, ArrayList<ArrayList<Double>>> tpytest = calc.picks_det(smoothYtest);
        HashMap<String, ArrayList<ArrayList<Double>>> tpztest = calc.picks_det(smoothZtest);
        HashMap<String, ArrayList<ArrayList<Double>>> tpctest = calc.picks_det(smoothCtest);

        //extract GC from sets of true picks
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcytest = calc.egc_di(smoothYtest, tpytest);
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcztest = calc.egc_di(smoothZtest, tpztest);
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gcctest = calc.egc_di(smoothCtest, tpctest);

        //extract sets of features
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetYtest = calc.features(smoothYtest, gcytest);
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetZtest = calc.features(smoothZtest, gcztest);
        HashMap<String, ArrayList<ArrayList<Double>>> featureSetCtest = calc.features(smoothCtest, gcctest);

        //PREDICITON STAGE
        String outDec = "";
        int decision = 0;
        for (int ttype = 0; ttype < 8; ttype++) {
            ArrayList<ArrayList<ArrayList<Double>>> p1 = new ArrayList<ArrayList<ArrayList<Double>>>();
            ArrayList<ArrayList<ArrayList<Double>>> p2 = new ArrayList<ArrayList<ArrayList<Double>>>();
            ArrayList<ArrayList<ArrayList<Double>>> p3 = new ArrayList<ArrayList<ArrayList<Double>>>();
            p1.add(calc.prep(featureSetY, ttype, 32));
            p1.add(calc.prep2(featureSetYtest, ttype));
            p2.add(calc.prep(featureSetZ, ttype, 32));
            p2.add(calc.prep2(featureSetZtest, ttype));
            p3.add(calc.prep(featureSetC, ttype, 32));
            p3.add(calc.prep2(featureSetCtest, ttype));
            int inst_dec = knn.pred_res(p1, p2, p3);
            decision = decision + inst_dec;
        }
        if (decision >= 3){
            outDec = "Accepted! U R OWNER!";
        }
        else {
            outDec = "Rejected! U R SOME RANDOM DUDE :)";
        }
        tv.setText("RESULT:" + outDec);
    }
}
