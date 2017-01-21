package edu.brown.cs.jj.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest  {
  @Test
  public void testNew() {
    //TODO: Test not null:
    Dictionary d = new Dictionary();
  }

  @Test
  public void testContains() {
    Dictionary d = new Dictionary("src/main/resources/ospd4.txt");
    assertTrue(d.contains("dog"));
    assertFalse(d.contains("voltron"));
    assertTrue(d.contains("wubba lubba dub dub"));
  }

  @Test
  public void testPrefix() {
    Dictionary d = new Dictionary("src/main/resources/ospd4.txt");
    assertTrue(d.containsPrefix("do"));
    assertFalse(d.containsPrefix("dogew"));
    assertTrue(d.contains("dub dub"));
  }
}
