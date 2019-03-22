package com.github.kuangcp.math.number;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/3/21
 * TODO 将浮点数转换成分数，并提供相关操作方法 貌似是不可行的
 * 最多是给定一个0.3（3）指定循环部分可以求出分数
 * 使用两个字符串分别表示分子和分母来计算,提供加减乘除以及化简的方法
 */
@Slf4j
public class Fraction extends Number implements Comparable<Fraction> {

  private static Pattern pattern = Pattern.compile("^-?[0-9]*\\.?[0-9]*$");


  private Integer numerator;
  private Integer denominator;

  /**
   * init a fraction by string  ag: -12.3434
   */
  public static Fraction valueOf(String num) {
    boolean matches = pattern.matcher(num).matches();
    if (!matches) {
      throw new RuntimeException("this num is infinity: " + num);
    }

    String[] numberArray = num.split("\\.");
    Fraction fraction = new Fraction();

    int numerator = Integer.parseInt(numberArray[0]);
    if (numberArray.length == 1) {
      fraction.changeToOne().setNumerator(numerator);
    } else {
      int decimalDigits = numberArray[1].length();
      int decimal = Integer.parseInt(numberArray[1]);
      if (numerator < 0) {
        decimal *= -1;
      }

      int denominator = (int) Math.pow(10.0, decimalDigits);
      fraction.setNumerator(numerator * denominator + decimal);
      fraction.setDenominator(denominator);
    }
    return fraction.simplify();
  }

  private Fraction() {
  }

  /**
   * init a new object from other fraction
   */
  public Fraction(Fraction fraction) {
    this.numerator = fraction.getNumerator();
    this.denominator = fraction.getDenominator();
  }

  public Fraction(Integer numerator, Integer denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  /**
   * init a integer
   */
  public Fraction(Integer numerator) {
    this.numerator = numerator;
    this.denominator = 1;
  }

  public Fraction add(Fraction other) {
    this.simplify();
    other.simplify();
    Fraction result;

    if (other.isInfinity() || this.isInfinity()) {
      return new Fraction().changeToInfinity();
    }

    if (this.getDenominator().equals(other.getDenominator())) {
      Integer numerator = this.getNumerator() + other.getNumerator();
      result = new Fraction(numerator, this.getDenominator());
    } else {
      Integer numerator = this.getNumerator() * other.getDenominator()
          + this.getDenominator() * other.getNumerator();
      result = new Fraction(numerator, this.getDenominator() * other.getDenominator());
    }
    return result.simplify();
  }

  public Fraction add(Integer other) {
    return add(new Fraction(other));
  }

  public Fraction multiply(Fraction other) {
    Fraction result = new Fraction();
    if (this.getNumerator() == 0 || other.getNumerator() == 0) {
      return result.changeToZero();
    }

    if (this.getDenominator() == 0 || other.getDenominator() == 0) {
      return result.changeToInfinity();
    }

    result.setNumerator(this.getNumerator() * other.getNumerator());
    result.setDenominator(this.getDenominator() * other.getDenominator());

    return result.simplify();
  }

  public Fraction multiply(Integer other) {
    Fraction temp = new Fraction(other);
    return multiply(temp);
  }

  public Fraction subtract(Fraction other) {
    Fraction fraction = new Fraction(other);
    if (fraction.isInfinity()) {
      return fraction;
    }
    fraction.setNumerator(-1 * fraction.getNumerator());
    return add(fraction);
  }

  public Fraction substract(Integer other) {
    return subtract(new Fraction(other));
  }

  public Fraction divide(Fraction other) {
    Fraction result = new Fraction(other);
    if (this.isZero()) {
      return result.changeToZero();
    }

    if (this.isInfinity() || other.isInfinity() || other.isZero()) {
      return result.changeToInfinity();
    }
    result = reverseNumeratorAndDenominator(other);
    return multiply(result);
  }

  public Fraction divide(Integer other) {
    return divide(new Fraction(other));
  }

  public boolean isGreaterThan(Fraction other) {
    return this.compareTo(other) > 0;
  }

  public boolean isGreaterThan(Integer other) {
    return isGreaterThan(new Fraction(other));
  }

  public Fraction simplify() {
    if (this.isInfinity() || this.isZero()) {
      return this;
    }
    if (this.isOne()) {
      return this.changeToOne();
    }

    Integer denominator = this.getDenominator();
    Integer numerator = this.getNumerator();

    Integer temp;
    while (denominator != 0) {
      temp = numerator % denominator;
      numerator = denominator;
      denominator = temp;
    }

    if (this.isPositive()) {
      this.setNumerator(Math.abs(this.getNumerator() / numerator));
    } else {
      this.setNumerator(-1 * Math.abs(this.getNumerator() / numerator));
    }
    this.setDenominator(Math.abs(this.getDenominator() / numerator));

    return this;
  }

  public boolean isPositive() {
    if (this.isInfinity() || this.isZero()) {
      return false;
    }
    return this.getNumerator() > 0 && this.getDenominator() > 0
        || this.getNumerator() < 0 && this.getDenominator() < 0;
  }

  public boolean isZero() {
    if (this.isInfinity()) {
      return false;
    }
    return this.getNumerator() == 0;
  }

  public boolean isOne() {
    if (this.isInfinity() || this.isZero()) {
      return false;
    }
    return this.getNumerator().equals(this.getDenominator());
  }

  public boolean isInteger() {
    if (this.isOne()) {
      return true;
    }
    return this.getDenominator() == 1;
  }

  public boolean isInfinity() {
    return this.getDenominator() == 0;
  }

  public Fraction changeToZero() {
    this.setNumerator(0);
    this.setDenominator(1);
    return this;
  }

  public Fraction changeToOne() {
    this.setNumerator(1);
    this.setDenominator(1);
    return this;
  }

  public Fraction changeToInfinity() {
    this.setNumerator(0);
    this.setDenominator(0);
    return this;
  }

  public Integer getNumerator() {
    return numerator;
  }

  public void setNumerator(Integer numerator) {
    this.numerator = numerator;
  }

  public Integer getDenominator() {
    return denominator;
  }

  public void setDenominator(Integer denominator) {
    this.denominator = denominator;
  }

  @Override
  public String toString() {
    if (this.isInteger()) {
      return numerator + " ";
    } else if (this.isInfinity()) {
      return "Infinity";
    } else {
      return "" + numerator + "/" + denominator + " ";
    }
  }

  private Fraction reverseNumeratorAndDenominator(Fraction target) {
    Fraction result = new Fraction();
    Integer temp = target.getDenominator();
    result.setDenominator(target.getNumerator());
    result.setNumerator(temp);
    return result;
  }

  @Override
  public boolean equals(Object target) {
    if (this == target) {
      return true;
    }
    if (target == null || getClass() != target.getClass()) {
      return false;
    }

    Fraction fraction = (Fraction) target;
    return this.compareTo(fraction) == 0;
  }

  @Override
  public int hashCode() {
    int result = numerator.hashCode();
    result = 31 * result + denominator.hashCode();
    return result;
  }

  public byte byteValue() {
    return (byte) this.doubleValue();
  }

  public double doubleValue() {
    return ((double) numerator) / ((double) denominator);
  }

  public float floatValue() {
    return (float) this.doubleValue();
  }

  public int intValue() {
    return (int) this.doubleValue();
  }

  public long longValue() {
    return (long) this.doubleValue();
  }

  public short shortValue() {
    return (short) this.doubleValue();
  }

  @Override
  public int compareTo(Fraction fraction) {
    long self = this.getNumerator() * fraction.getDenominator();
    long other = fraction.getNumerator() * this.getDenominator();

    if (self > other) {
      return 1;
    } else if (other > self) {
      return -1;
    }
    return 0;
  }

}
