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
  public void testAdd(){
    Fraction one = Fraction.valueOf("3.2");
    Fraction two = Fraction.valueOf("-3.5");

    assertThat(one.add(two), equalTo(new Fraction(-3, 10)));
    log.info("{} {} {}", one.simplify(), two.simplify(), one.add(two).simplify());
  }
}