package com.github.kuangcp.tuple;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.github.kuangcp.tuple.Pair;
import org.junit.Test;

/**
 * @author kuangcp on 2/13/19-10:23 AM
 */
public class PairTest {

  @Test
  public void testUse() {
    Pair<String, Integer> pair = new Pair<>("a", 1);
    Pair<String, Integer> of = Pair.of("a", 1);

    assertThat(pair, equalTo(of));
  }
}