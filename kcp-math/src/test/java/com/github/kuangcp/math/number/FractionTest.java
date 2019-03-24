package com.github.kuangcp.math.number;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/14/19-5:09 PM
 */
@Slf4j
public class FractionTest {

  @Test
  public void testInit() {
    Fraction fraction = new Fraction(1);
    assertThat(fraction, equalTo(new Fraction(1, 1)));
    assertThat(fraction.intValue(), equalTo(1));

    assertThat(new Fraction(2, 3).doubleValue(), equalTo(2 / 3.0));

    assertThat(Fraction.valueOf("3.1").doubleValue(), equalTo(31 / 10.0));
    assertThat(Fraction.valueOf("3.31342423").doubleValue(), equalTo(331342423 / 10000_0000.0));

  }

  @Test
  public void testAdd() {
    Fraction one = Fraction.valueOf("3.2");
    Fraction two = Fraction.valueOf("-3.5");

    assertThat(one.add(two), equalTo(new Fraction(-3, 10)));
    log.info("{} {} {}", one.simplify(), two.simplify(), one.add(two).simplify());

    assertThat(new Fraction(2, 3).add(new Fraction(4, 3)), equalTo(2));

    assertThat(new Fraction(2, 3).add(new Fraction(4, 3)), equalTo(new Fraction(6, 3)));

  }

  @Test
  public void testSimplify() {
    Fraction fraction = new Fraction(12, 3);

    assertThat(fraction, equalTo(4.0000f));

    assertThat(new Fraction(0, 1), equalTo(0));
    assertThat(new Fraction(0, 1).isZero(), equalTo(true));

    assertThat(new Fraction(1, 0).isInfinity(), equalTo(true));
    assertThat(new Fraction(1, 0), equalTo(Double.POSITIVE_INFINITY));
    assertThat(new Fraction(-1, 0), equalTo(Double.NEGATIVE_INFINITY));

    //    assertThat(1/0, equalTo(Double.POSITIVE_INFINITY));

  }

  @Test
  public void testSubtract() {
    assertThat(new Fraction(10, 3).subtract(new Fraction(4, 3)), equalTo(new Fraction(6, 3)));
  }

  @Test
  public void testDivide() {
    Fraction result = new Fraction(25, 1).divide(new Fraction(0, 3));
    assertThat(result, equalTo(Fraction.INFINITY));
    assertThat(result, equalTo(new Fraction(0, 3)));
    log.info("result: result={}", result);
  }

  @Test
  public void testMulti() {
    Fraction result = Fraction.valueOf("1.4").multiply(Fraction.valueOf("2.3"));
    assertThat(result, equalTo(new Fraction(14 * 23, 100)));
  }

  @Test
  public void testSimple() {
  }

  //测试比较函数
  @Test
  public void compare() {
  }

  @Test
  public void testIsZero() {
  }

  @Test
  public void testIsInfinity() {
  }

  @Test
  public void testIsPositive() {
  }
}