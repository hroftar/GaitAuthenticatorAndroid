package com.example.alex.gait_authenticator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.example.alex.gait_authenticator.Waves.Transform;
import com.example.alex.gait_authenticator.Waves.FastWaveletTransform;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies6;

/**
 * Created by Alex on 18/04/2018.
 */

public class Calculations {

    public Calculations(){

    }

    //extract data from hashmap
    //pname - group name, amount - required number of entries
    //revType: 0 - amaxa, 1 - amina, 2 - abs dif, 3 - ssd, 4 - rms,
    //5 - wave, 6 - period, 7 - freq
    //check for group: 0.0 - accept, 1.0 - reject
    public ArrayList<Double> get_data(HashMap<String, ArrayList<ArrayList<Double>>> di, String pname, int recType, int amount){
        ArrayList<Double> retSet = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> particData = di.get(pname);
        ArrayList<Double> typeData = particData.get(recType);
        for (int x = 0; x < amount; x++){
            retSet.add(typeData.get(x));
        }
        if (pname.equals("accept")) {
            retSet.add(0.0);
        }
        else {
            retSet.add(1.0);
        }
        return retSet;
    }

    //prep train data
    public ArrayList<ArrayList<Double>> prep(HashMap<String, ArrayList<ArrayList<Double>>> fset, int ttype, int amount){
        ArrayList<ArrayList<Double>> train = new ArrayList<ArrayList<Double>>();
        train.add(get_data(fset, "accept", ttype, amount));
        train.add(get_data(fset, "reject", ttype, amount));
        return train;
    }

    //prep test data
    public ArrayList<ArrayList<Double>> prep2(HashMap<String, ArrayList<ArrayList<Double>>> fset, int ttype){
        ArrayList<ArrayList<Double>> test = new ArrayList<ArrayList<Double>>();
        test.add(get_data(fset, "test", ttype, 1));
        return test;
    }

    //maximum value of array
    public double max(ArrayList<Double> a){
        double max = a.get(0);
        for (int i = 0; i < a.size(); i++){
            if(a.get(i) > max){
                max = a.get(i);
            }
        }
        return max;
    }

    //minimum value of array
    public double min(ArrayList<Double> a){
        double min = a.get(0);
        for (int i = 0; i < a.size(); i++){
            if(a.get(i) < min){
                min = a.get(i);
            }
        }
        return min;
    }

    //mean
    public double mean(ArrayList<Double> ar){
        double sum = 0;
        for (int i = 0; i < ar.size(); i++){
            sum = sum + ar.get(i);
        }
        double mean = sum/ar.size();
        return mean;
    }

    //sum of square devations
    public double _ss(ArrayList<Double> ar){
        double c = mean(ar);
        double ss = 0;
        for (int x = 0; x < ar.size(); x++){
            ss = ss + Math.pow((ar.get(x) - c),2);
        }
        return ss;
    }

    //standard deviation. par=0 for SD, 1 for SSD
    public double sd(ArrayList<Double> ar, int par){
        double ss = _ss(ar);
        double sd = ss/(ar.size() - par);
        return Math.pow(sd, 0.5);
    }

    //average maximum acceleration
    public double a_max_acc(ArrayList<ArrayList<Double>> gc){
        ArrayList<Double> max_acc = new ArrayList<Double>();
        for (ArrayList<Double> x : gc){
            max_acc.add(max(x));
        }
        double amaxa = mean(max_acc);
        return amaxa;
    }

    //average minimum acceleration
    public double a_min_acc(ArrayList<ArrayList<Double>> gc){
        ArrayList<Double> min_acc = new ArrayList<Double>();
        for (ArrayList<Double> x : gc){
            min_acc.add(min(x));
        }
        double amina = mean(min_acc);
        return amina;
    }

    //average absolute difference
    public double a_abs_dif(ArrayList<Double> signal){
        double abs_dif = 0;
        for (double x : signal){
            abs_dif = abs_dif + Math.abs(x - mean(signal));
        }
        return abs_dif;
    }

    //root mean square
    public double rms(ArrayList<Double> signal){
        double rms = 0;
        for (double x : signal){
            rms = rms + Math.pow(x, 2);
        }
        return rms/signal.size();
    }

    //wavelength
    public double wave(ArrayList<Double> signal){
        double wavesum = 0;
        for (int x = 0; x < signal.size()-1; x++){
            wavesum = wavesum + Math.abs(signal.get(x+1) - signal.get(x));
        }
        return wavesum;
    }

    //cadence period
    public double cad_per(ArrayList<ArrayList<Double>> gc){
        double cadsum = 0;
        for (ArrayList<Double> x : gc){
            cadsum = cadsum + x.size();
        }
        return cadsum/gc.size();
    }

    //cadence frequency
    public double cad_freq(ArrayList<ArrayList<Double>> gc){
        double fsum = 0;
        for (ArrayList<Double> x : gc){
            fsum = fsum + x.size();
        }
        return (double)(gc.size())/(double)(fsum);
    }

    //EXTRACT GAIT CYCLES BLOCK
    //support for egc
    public ArrayList<Double> _egc(ArrayList<Double> signal, double fp, double sp, double thp){
        int start = signal.indexOf(signal.get(0));
        int fin = 100;
        for (double x : signal){
            if (x == fp){
                break;
            }
            else if (x <= start){
                start = signal.indexOf(x);
            }
        }
        int spind = signal.indexOf(sp);
        for (double x : signal.subList(spind, signal.size()-1)){
            if (x == thp){
                break;
            }
            if (x <= fin){
                fin = signal.indexOf(x) + 1;
                break;
            }
        }
        return new ArrayList<Double>(signal.subList(start, fin));
    }

    //extract GC
    public ArrayList<ArrayList<Double>> egc(ArrayList<Double> signal, ArrayList<Double> tp){
        ArrayList<ArrayList<Double>> gs = new ArrayList<ArrayList<Double>>();
        if (tp.size() %2 != 0){
            tp.remove(tp.size()-1);
        }
        for (int x = 0; x < tp.size() - 2; x = x +2){
            gs.add(_egc(signal, tp.get(x), tp.get(x+1), tp.get(x+2)));
        }
        if (gs.size() != 0) {
            gs.add(new ArrayList<Double>(signal.subList(signal.indexOf(tp.get(tp.size() - 2)), signal.size() - 1)));
        }
        return gs;
    }

    //egc for hashmap
    public HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> egc_di(HashMap<String, ArrayList<ArrayList<Double>>> ds, HashMap<String, ArrayList<ArrayList<Double>>> tp){
        HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> egcSet = new HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>>();
        Set<String> keys = ds.keySet();
        for (String k : keys){
            ArrayList<ArrayList<ArrayList<Double>>> partic = new ArrayList<ArrayList<ArrayList<Double>>>();
            ArrayList<ArrayList<Double>> dsVal = ds.get(k);
            ArrayList<ArrayList<Double>> tpVal = tp.get(k);
            for (int x= 0; x < dsVal.size(); x++){
                partic.add(egc(dsVal.get(x), tpVal.get(x)));
            }
            egcSet.put(k, partic);
        }
        return egcSet;
    }

    //detect picks and true picks of the signal
    public HashMap<String, ArrayList<ArrayList<Double>>> picks_det(HashMap<String, ArrayList<ArrayList<Double>>> ds){
        HashMap<String, ArrayList<ArrayList<Double>>> tp_di = new HashMap<String, ArrayList<ArrayList<Double>>>(ds);
        Set<String> keys = ds.keySet();
        for (String k : keys){
            ArrayList<ArrayList<Double>> partic = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> dsVal = ds.get(k);
            for (int x = 0; x < dsVal.size(); x++){
                double ko = 2/3;
                ArrayList<Double> true_picks = new ArrayList<Double>();
                ArrayList<Double> picks = new ArrayList<Double>();
                double prev = 0;
                double cur = 0;
                double fol = 0;
                for (int c = 0; c < dsVal.get(x).size(); c++){
                    if (c == 0){
                        prev = dsVal.get(x).get(0);
                    }
                    else if (c == 1){
                        cur = dsVal.get(x).get(1);
                    }
                    else{
                        fol = dsVal.get(x).get(c);
                        if (prev < cur && cur > fol){
                            picks.add(cur);
                        }
                        prev = cur;
                        cur = fol;
                    }
                }
                double TVal = mean(picks) + sd(picks, 0) * ko;
                for (Double v : picks){
                    if (v > TVal){
                        true_picks.add(v);
                    }
                }
                partic.add(true_picks);
            }
            tp_di.put(k, partic);
        }
        return tp_di;
    };

    //produce a complete set of features for a data set
    //map{name: [[amaxa],[amina],[abs_dif],[ssd],[rms],[wave],[period],[freq]]}
    public HashMap<String, ArrayList<ArrayList<Double>>> features(HashMap<String, ArrayList<ArrayList<Double>>> ds, HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> gc){
        HashMap<String, ArrayList<ArrayList<Double>>> featuresSet = new HashMap<String, ArrayList<ArrayList<Double>>>();
        Set<String> keys = ds.keySet();
        for (String k : keys){
            ArrayList<ArrayList<Double>> dsVal = ds.get(k);
            ArrayList<ArrayList<ArrayList<Double>>> gcVal = gc.get(k);
            ArrayList<ArrayList<Double>> partic = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> amaxaSet = new ArrayList<Double>();
            ArrayList<Double> aminaSet = new ArrayList<Double>();
            ArrayList<Double> absSet = new ArrayList<Double>();
            ArrayList<Double> ssdSet = new ArrayList<Double>();
            ArrayList<Double> rmsSet = new ArrayList<Double>();
            ArrayList<Double> waveSet = new ArrayList<Double>();
            ArrayList<Double> periodSet = new ArrayList<Double>();
            ArrayList<Double> freqSet = new ArrayList<Double>();
            for (int x = 0; x < dsVal.size(); x++){
                ArrayList<Double> signal = dsVal.get(x);
                ArrayList<ArrayList<Double>> signalGC = gcVal.get(x);
                amaxaSet.add(a_max_acc(signalGC));
                aminaSet.add(a_min_acc(signalGC));
                absSet.add(a_abs_dif(signal));
                ssdSet.add(sd(signal, 1));
                rmsSet.add(rms(signal));
                waveSet.add(wave(signal));
                periodSet.add(cad_per(signalGC));
                freqSet.add(cad_freq(signalGC));
            }
            partic.add(amaxaSet);
            partic.add(aminaSet);
            partic.add(absSet);
            partic.add(ssdSet);
            partic.add(rmsSet);
            partic.add(waveSet);
            partic.add(periodSet);
            partic.add(freqSet);
            featuresSet.put(k, partic);
        }
        return featuresSet;
    }

    //smooth signals with Daubechies wavelets transform db6
    public HashMap<String, ArrayList<ArrayList<Double>>> smooth(HashMap<String, ArrayList<ArrayList<Double>>> ds){

        Daubechies6 db = new Daubechies6();
        FastWaveletTransform fwt = new FastWaveletTransform(db);
        Transform t = new Transform(fwt);
        HashMap<String, ArrayList<ArrayList<Double>>> smoothed_di = new HashMap<String, ArrayList<ArrayList<Double>>>();
        Set<String> keys = ds.keySet();
        for (String k : keys){
            ArrayList<ArrayList<Double>> partic = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> dsVal = ds.get(k);
            int size = 2;
            if (4 <= dsVal.size() && dsVal.size() < 8){
                size = 4;
            }
            else if (8 <= dsVal.size() && dsVal.size() < 16){
                size = 8;
            }
            else if (16 <= dsVal.size() && dsVal.size() < 32){
                size = 16;
            }
            else if (32 <= dsVal.size() && dsVal.size() < 64){
                size = 32;
            }
            else if (dsVal.size() >= 64){
                size = 64;
            }
            for (int x = 0; x < /*dsVal.size()*/size; x++){
                double [] dar = new double[/*dsVal.size()*/size];
                for (Double d : dsVal.get(x)){
                    int co = 0;
                    dar[co] = d;
                    co++;
                }
                double[] smoothed = t.forward(dar);
                ArrayList<Double> smoothedA = new ArrayList<Double>();
                for (double d : smoothed){
                    if (new Double(d) != null) {
                        smoothedA.add(new Double(d));
                    }
                }
                partic.add(smoothedA);
            }
            smoothed_di.put(k, partic);
        }
        return smoothed_di;
    }

    //produce combined signal
    public HashMap<String, ArrayList<ArrayList<Double>>> combine(HashMap<String, ArrayList<ArrayList<Double>>> ds1, HashMap<String, ArrayList<ArrayList<Double>>> ds2, HashMap<String, ArrayList<ArrayList<Double>>> ds3){
        HashMap<String, ArrayList<ArrayList<Double>>> combDi = new HashMap<String, ArrayList<ArrayList<Double>>>();
        Set<String> keys = ds1.keySet();
        for (String k : keys){
            ArrayList<ArrayList<Double>> partic = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> ds1Val = ds1.get(k);
            ArrayList<ArrayList<Double>> ds2Val = ds2.get(k);
            ArrayList<ArrayList<Double>> ds3Val = ds3.get(k);
            for (int x = 0; x < ds1Val.size(); x++){
                ArrayList<Double> combSig = new ArrayList<Double>();
                for (int v = 0; v < ds1Val.get(x).size(); v++){
                    Double combP = (Math.pow((ds1Val.get(x).get(v)), 2) + Math.pow((ds2Val.get(x).get(v)), 2) + Math.pow((ds3Val.get(x).get(v)), 2));
                    combSig.add(Math.pow(combP, 0.5));
                }
                partic.add(combSig);
            }
            combDi.put(k, partic);
        }
        return combDi;
    }
}
