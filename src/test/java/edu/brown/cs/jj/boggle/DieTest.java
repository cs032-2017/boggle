package edu.brown.cs.jj.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class DieTest {
  @Test
  public void testNew() {
    // TODO: Test not null
    Die d = new Die("abcdef");
  }

  @Test
  public void testPick() {
    Die a = new Die("qqqqqq");
    assertEquals(a.pick(), 'q');
    assertEquals(a.pick(), 'q');
    assertEquals(a.pick(), 'p');
    assertEquals(a.pick(), 'q');


    Die ab = new Die("ababab");
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
  }
  
 
}
