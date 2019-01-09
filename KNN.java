package com.example.alex.gait_authenticator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alex on 17/04/2018.
 */

public class KNN {

    public KNN(){

    };

    public double euclideanDistance(ArrayList<Double> i1, ArrayList<Double> i2, int length){
        int distance = 0;
        for (int x = 0; x < i1.size(); x++){
            distance += java.lang.Math.pow((i1.get(x) - i2.get(x)), 2);
        }
        return java.lang.Math.sqrt(distance);
    };

    public ArrayList<ArrayList<Double>> getNeighbors(ArrayList<ArrayList<Double>> trainingSet, ArrayList<Double> testInstance, int k){
        ArrayList<Double> distances = new ArrayList<Double>();
        int length = testInstance.size() - 1;
        ArrayList<Double> finlist = new ArrayList<Double>();
        double findist = 1000;
        for (int x = 0; x < trainingSet.size(); x++){
            double dist = euclideanDistance(testInstance, trainingSet.get(x), length);
            if (dist < findist){
                findist = dist;
                finlist = trainingSet.get(x);
            }
        }
        ArrayList<ArrayList<Double>> nighbours = new ArrayList<ArrayList<Double>>();
        nighbours.add(finlist);
        return nighbours;
    };

    public double getAccuracy(ArrayList<ArrayList<Double>> testSet, ArrayList<Double> predictions){
        int correct = 0;
        for (int x = 0; x < testSet.size(); x++){
            if (testSet.get(x).get(testSet.get(x).size()-1) == predictions.get(x)){
                correct += 1;
            }
        }
        return (correct/(double)(testSet.size())) * 100.0;
    };

    public double getResponse(ArrayList<ArrayList<Double>> neighbors){
        HashMap<Double, Integer> classVotes = new HashMap<Double, Integer>();
        for (int x = 0; x < neighbors.size(); x++){
            double response = neighbors.get(x).get(neighbors.size()-1);
            if (classVotes.containsKey(response)){
                classVotes.put(response, classVotes.get(response) + 1);
            }
            else {
                classVotes.put(response, 1);
            }
        }
        return classVotes.get(classVotes.keySet().iterator().next());
    };

    //predictions
    public double predict(ArrayList<ArrayList<Double>> train, ArrayList<ArrayList<Double>> test){
        ArrayList<Double> predictions = new ArrayList<Double>();
        int k = 1;
        for (int x = 0; x < test.size(); x++){
            ArrayList<ArrayList<Double>> neighbors = getNeighbors(train, test.get(x), k);
            double result = getResponse(neighbors);
            predictions.add(result);
        }
        double accuracy = getAccuracy(test, predictions);
        return accuracy;
    };

    //result of predictions (if point is accepted at least for 1 signal - accept)
    public int pred_res(ArrayList<ArrayList<ArrayList<Double>>> prepset1, ArrayList<ArrayList<ArrayList<Double>>> prepset2, ArrayList<ArrayList<ArrayList<Double>>> prepset3){
      int accval = 0;
      double acc1 = predict(prepset1.get(0), prepset1.get(1));
      double acc2 = predict(prepset2.get(0), prepset2.get(1));
      double acc3 = predict(prepset3.get(0), prepset3.get(1));
      if (acc1 == 100.0 || acc2 == 100.0 || acc3 == 100.0){
          accval = 1;
      }
      return accval;
    };
}
