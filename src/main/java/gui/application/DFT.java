package gui.application;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class DFT {
  double realComp;
  double imaginaryComp;

  public DFT() {
    // Custructor
  }

  public ArrayList<Hashtable<String, Double>> calculateDFT(ArrayList<Double> values) {
    final int N = values.size();
    // ArrayList<ArrayList<Double>> X = new ArrayList<>();
    ArrayList<Hashtable<String, Double>> X = new ArrayList<>();
    Dictionary<String, Double> dictionary = new Hashtable<>();

    for (int k = 0; k < N; k++) {

      // Real and imaginary component of fourier transform
      realComp = 0;
      imaginaryComp = 0;

      for (int n = 0; n < N; n++) {
        double theta = (2 * Math.PI * k * n) / N;
        realComp += values.get(n) * Math.cos(theta);
        imaginaryComp -= values.get(n) * Math.sin(theta);
      }

      // Average contribution by N
      realComp = realComp / N;
      imaginaryComp = imaginaryComp / N;

      double frequency = k;
      double amp = Math.sqrt(realComp * realComp + imaginaryComp * imaginaryComp);
      double phase = Math.atan2(imaginaryComp, realComp);

      X.add(
          k,
          new Hashtable<String, Double>() {
            {
              put("real", realComp);
              put("imgaginary", imaginaryComp);
              put("frequency", frequency);
              put("amplitude", amp);
              put("phase", phase);
            }
          });
    }

    return X;
  }
}
