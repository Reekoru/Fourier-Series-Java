package gui.application;

import java.util.ArrayList;
import java.util.Arrays;

public class DFT {

  public DFT() {
    // Custructor
  }

  public Double[][] calculateDFT(ArrayList<Double> values) {
    final int N = values.size();

    // Stores the fourier transformed of the signal values
    Double[][] X = new Double[N][];
    double realComp;
    double imaginaryComp;

    for (int k = 0; k < N; k++) {

      // Real and imaginary component of fourier transform
      realComp = 0;
      imaginaryComp = 0;

      for (int n = 0; n < N; n++) {
        double theta = (2 * Math.PI * k * n) / N;

        realComp += values.get(n) * Math.cos(theta);
        imaginaryComp -= values.get(n) * Math.sin(theta);
      }

      // Get average
      realComp = realComp / N;
      imaginaryComp = imaginaryComp / N;

      // Real and imaginary can be expressed in cartesian form
      double frequency = k;
      double amp = Math.sqrt(realComp * realComp + imaginaryComp * imaginaryComp);
      double phase = Math.atan2(imaginaryComp, realComp);

      /* Stores all components of fourier transfored points
       * INDEX:
       * 0: Real component
       * 1: Imaginary component
       * 2: Frequency
       * 3: Amplitude
       * 4: Phase
       */
      X[k] = new Double[] {realComp, imaginaryComp, frequency, amp, phase};
    }

    // Sorts the array based on amplitude
    Arrays.sort(X, (a, b) -> Double.compare(b[3], a[3]));

    return X;
  }
}
