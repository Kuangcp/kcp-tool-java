package com.github.kuangcp.tuple;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author kuangcp on 2/13/19-11:37 AM
 */
public class Tuple3Test {

  @Test
  public void testUse() {
    Tuple3<Character, String, Integer> triplet = new Tuple3<>('%', "d", 3);
    Tuple3<Character, String, Integer> of = Tuple3.of('%', "d", 3);

    assertThat(triplet, equalTo(of));
  }
}
