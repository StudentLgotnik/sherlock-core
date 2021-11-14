package com.mkaza.sherlock.util;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        writeToExcel("AvarageEpsilon", averageDistances);

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

        writeToExcel("NormalEpsilon", averageDistances.stream().mapToDouble(Double::doubleValue).toArray());

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

    private static void writeToExcel(String fileName, double[] data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Java Books");

//        Object[][] bookData = {
//                {"Head First Java", "Kathy Serria", 79},
//                {"Effective Java", "Joshua Bloch", 36},
//                {"Clean Code", "Robert martin", 42},
//                {"Thinking in Java", "Bruce Eckel", 35},
//        };

//        int rowCount = 0;
//
//        for (Object[] aBook : bookData) {
//            Row row = sheet.createRow(++rowCount);
//
//            int columnCount = 0;
//
//            for (Object field : aBook) {
//                Cell cell = row.createCell(++columnCount);
//                if (field instanceof String) {
//                    cell.setCellValue((String) field);
//                } else if (field instanceof Integer) {
//                    cell.setCellValue((Integer) field);
//                }
//            }
//
//        }

        Row headers = sheet.createRow(1);
        Cell a = headers.createCell(1);
        a.setCellValue("A");
        Cell b = headers.createCell(2);
        b.setCellValue("B");

        for (int i = 0; i < data.length; i++) {

            Row row = sheet.createRow(2 + i);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(data[i]);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(i);

        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
