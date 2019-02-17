package com.github.kuangcp.tuple;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author kuangcp on 2/13/19-10:23 AM
 */
public class Tuple2Test {

  @Test
  public void testUse() {
    Tuple2<String, Integer> pair = new Tuple2<>("a", 1);
    Tuple2<String, Integer> of = Tuple2.of("a", 1);

    assertThat(pair, equalTo(of));
  }
}