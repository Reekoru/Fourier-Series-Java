package gui.application.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadCSV {
  public ArrayList<ArrayList<Double>> getCoordinates(String path) {

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
        x.add(tokenToDouble[0]);
        y.add(tokenToDouble[1]);
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
}
