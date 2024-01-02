package gui.application.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import gui.application.Classes.ComplexNumber;

public class ReadCSV {
  public ArrayList<ArrayList<Double>> getCoordinates(String path, double offsetx, double offsety) {

    String[] token;

    ArrayList<ArrayList<Double>> coordinates = new ArrayList<>();
    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();

    try {
      File file = new File(path);
      Scanner scanner = new Scanner(file);

      while (scanner.hasNext()) {
        String str;
        str = scanner.next();

        // Split to x and y
        token = str.split(",");

        // Converts string  array to double array
        Double[] tokenToDouble = Arrays.stream(token).map(Double::valueOf).toArray(Double[]::new);

        // Add x and y components
        x.add(tokenToDouble[0] - offsetx);
        y.add(tokenToDouble[1]- offsety);
      }

      scanner.close();

    } catch (FileNotFoundException err) {
      System.out.println("File not found: " + err);
    }

    // Add X and Y component to coordinates
    coordinates.add(x);
    coordinates.add(y);

    return coordinates;
  }

  public ArrayList<ComplexNumber> getComplexCoordinates(String path, double offsetx, double offsety) {
    ArrayList<ArrayList<Double>> coordinates = getCoordinates(path, offsetx, offsety);
    ArrayList<ComplexNumber> complex = new ArrayList<>();

    for (int i = 0; i < coordinates.get(0).size(); i++) {
      complex.add(new ComplexNumber(coordinates.get(0).get(i), coordinates.get(1).get(i)));
    }
    return complex;
  }
}
