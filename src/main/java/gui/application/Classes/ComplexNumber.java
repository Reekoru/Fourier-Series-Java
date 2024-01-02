package gui.application.Classes;

public class ComplexNumber {
  public Double real;
  public Double imaginary;

  public ComplexNumber(Double real, Double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
  }

  public ComplexNumber add(ComplexNumber other) {
    this.real += other.real;
    this.imaginary += other.imaginary;
    return new ComplexNumber(this.real, this.imaginary);
  }

  public ComplexNumber multiply(ComplexNumber other) {
    Double complexReal = this.real * other.real - this.imaginary * other.imaginary;
    Double complexImag = this.real * other.imaginary + this.imaginary * other.real;
    return new ComplexNumber(complexReal, complexImag);
  }

  public Double getAmplitude() {
    return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
  }

  public Double getPhase() {
    return Math.atan2(this.imaginary, this.real);
  }
}
