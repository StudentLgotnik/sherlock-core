package com.mkaza.sherlock.util;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.*;
import java.util.stream.Collectors;

public class DbscanUtil {

    public static int calcMinPts(int datasetSize) {
        return Math.toIntExact(Math.round(Math.log(datasetSize)));
    }

    //Predicts more accurately
    public static double calcAverageEpsilon(List<? extends Clusterable> dataRows, int neighborsCount) {
        //Measure to calculate distance between vectors
        DistanceMeasure dm = new EuclideanDistance();

        //Average data rows distances to nearest n neighbors
        double[] averageDistances = new double[dataRows.size()];

        for (int i = 0; i < dataRows.size(); i++) {

            Clusterable center = dataRows.get(i);

            //All distances to neighbors
            List<Double> distanceToNeighbors = new ArrayList<>();

            //Calculate distance from center to neighbor
            for (int j = 0; j < dataRows.size(); j++) {
                if (i != j) {
                    Clusterable neighbor = dataRows.get(j);
                    distanceToNeighbors.add(dm.compute(center.getPoint(), neighbor.getPoint()));
                }
            }

            //Get n nearest neighbors
            DoubleSummaryStatistics stats = distanceToNeighbors.stream()
                    .sorted()
                    .limit(neighborsCount)
                    .collect(Collectors.summarizingDouble(Double::doubleValue));

            //Get average
            averageDistances[i] = stats.getAverage();

        }

        Arrays.sort(averageDistances);

        //Find the elbow of function
        double maxDif = 0;
        int potentialEpsilonIndex = 0;
        for (int i = 0; i < averageDistances.length - 1; i++) {
            double currentDif = averageDistances[i+1] - averageDistances[i];
            if (currentDif > maxDif) {
                maxDif = currentDif;
                potentialEpsilonIndex = i;
            }
        }

        return averageDistances[potentialEpsilonIndex];
    }

    public static double calcEpsilon(List<? extends Clusterable> dataRows, int neighborsCount) {
        //Measure to calculate distance between vectors
        DistanceMeasure dm = new EuclideanDistance();

        //Average data rows distances to nearest n neighbors
        List<Double> averageDistances = new ArrayList<>();

        for (int i = 0; i < dataRows.size(); i++) {

            Clusterable center = dataRows.get(i);

            //All distances to neighbors
            List<Double> distanceToNeighbors = new ArrayList<>();

            //Calculate distance from center to neighbor
            for (int j = 0; j < dataRows.size(); j++) {
                if (i != j) {
                    Clusterable neighbor = dataRows.get(j);
                    distanceToNeighbors.add(dm.compute(center.getPoint(), neighbor.getPoint()));
                }
            }

            //Get n nearest neighbors
            List<Double> stats = distanceToNeighbors.stream()
                    .sorted()
                    .limit(neighborsCount)
                    .collect(Collectors.toList());

            averageDistances.addAll(stats);
        }

        Collections.sort(averageDistances);

        //Find the elbow of function
        double maxDif = 0;
        int potentialEpsilonIndex = 0;
        for (int i = 0; i < averageDistances.size() - 1; i++) {
            double currentDif = averageDistances.get(i+1) - averageDistances.get(i);
            if (currentDif > maxDif) {
                maxDif = currentDif;
                potentialEpsilonIndex = i;
            }
        }

        return averageDistances.get(potentialEpsilonIndex);
    }

}
