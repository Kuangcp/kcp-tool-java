package com.github.kuangcp.tuple;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.github.kuangcp.tuple.Triplet;
import org.junit.Test;

/**
 * @author kuangcp on 2/13/19-11:37 AM
 */
public class TripletTest {

  @Test
  public void testUse() {
    Triplet<Character, String, Integer> triplet = new Triplet<>('%', "d", 3);
    Triplet<Character, String, Integer> of = Triplet.of('%', "d", 3);

    assertThat(triplet, equalTo(of));
  }
}
