package edu.brown.cs.jj.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest  {

  @Test
  public void testRandomConstruction() {
    Board b = new Board();
    assertNotNull(b);
  }

  @Test
  public void testSpecificConstruction() {
    //TODO: Fix test:
    Board b = new Board("rick,quit,done,here");
    assertNotNull(b);
    assertEquals(b.get(0,0), "r");
    assertEquals(b.get(1,0), "q");
  }

  @Test
  public void testToString() {
    Board b = new Board("rick,isnt,done,here");
    b.toString();
    b.toString(',');
    //TODO: Test with assertEquals
  }


  @Test
  public void testPlay() {
    //TODO: Test play with example board
    Board b = new Board("rick,xxx,xxx,xxxx");
  }

  @Test
  public void testScore() {
    //TODO: Test score method 
  }
}
