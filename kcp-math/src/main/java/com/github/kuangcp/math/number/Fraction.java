package com.github.kuangcp.math.number;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/3/21
 * TODO 将浮点数转换成分数，并提供相关操作方法 貌似是不可行的
 * 最多是给定一个0.3（3）指定循环部分可以求出分数
 * 使用两个字符串分别表示分子和分母来计算,提供加减乘除以及化简的方法
 *
 * http://commons.apache.org/proper/commons-math/userguide/fraction.html
 * https://codereview.stackexchange.com/questions/43084/java-fraction-calculator
 * https://stackoverflow.com/questions/474535/best-way-to-represent-a-fraction-in-java
 */
@Slf4j
@Getter
@Setter
public class Fraction extends Number implements Comparable<Fraction> {

  private static Pattern pattern = Pattern.compile("^-?[0-9]*\\.?[0-9]*$");

  public static final Fraction INFINITY = new Fraction().toInfinity();

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
      fraction.toOne().setNumerator(numerator);
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

  private static Fraction reversed(Fraction target) {
    return new Fraction(target.getDenominator(), target.getNumerator());
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

  public Fraction(Integer numerator) {
    this.numerator = numerator;
    this.denominator = 1;
  }

  public Fraction add(Fraction other) {
    this.simplify();
    other.simplify();
    Fraction result;

    if (other.isInfinity() || this.isInfinity()) {
      return new Fraction().toInfinity();
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
      return result.toZero();
    }

    if (this.getDenominator() == 0 || other.getDenominator() == 0) {
      return result.toInfinity();
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

  public Fraction subtract(Integer other) {
    return subtract(new Fraction(other));
  }

  public Fraction divide(Fraction other) {
    Fraction result = new Fraction(other);
    if (this.isZero()) {
      return result.toZero();
    }

    if (this.isInfinity() || other.isInfinity() || other.isZero()) {
      return result.toInfinity();
    }
    result = reversed(other);
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
      return this.toOne();
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
    return doubleValue() == 0.0;
  }

  public boolean isOne() {
    return doubleValue() == 1.0;
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

  public Fraction toZero() {
    return setValue(0, 1);
  }

  public Fraction toOne() {
    return setValue(1, 1);
  }

  public Fraction toInfinity() {
    return setValue(0, 0);
  }

  @Override
  public String toString() {
    if (this.isInteger() || isZero()) {
      return numerator + " ";
    } else if (this.isInfinity()) {
      return "Infinity";
    } else {
      return numerator + "/" + denominator + " ";
    }
  }

  @Override
  public boolean equals(Object target) {
    if (this == target) {
      return true;
    }
    if (target == null) {
      return false;
    }

    if (getClass() == target.getClass()) {
      Fraction fraction = (Fraction) target;
      if (this.isInfinity()) {
        return fraction.isInfinity();
      } else if (this.isZero()) {
        return fraction.isZero();
      } else {
        return this.compareTo(fraction) == 0;
      }
    }

    if (target instanceof Number) {
      Number number = (Number) target;
      return number.doubleValue() == this.doubleValue();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = numerator.hashCode();
    result = 31 * result + denominator.hashCode();
    return result;
  }

  @Override
  public byte byteValue() {
    return (byte) this.doubleValue();
  }

  @Override
  public double doubleValue() {
    return ((double) numerator) / ((double) denominator);
  }

  @Override
  public float floatValue() {
    return (float) this.doubleValue();
  }

  @Override
  public int intValue() {
    return (int) this.doubleValue();
  }

  @Override
  public long longValue() {
    return (long) this.doubleValue();
  }

  @Override
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

  private Fraction setValue(Integer numerator, Integer denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
    return this;
  }
}
