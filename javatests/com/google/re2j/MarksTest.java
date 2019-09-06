// Copyright 2019 Google Inc. All Rights Reserved.

package com.google.re2j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Testing the marks functionality of the RE2/J Machine and Matcher
 *
 * @author jeffrey.wayne.evans@gmail.com (Jeff Evans)
 */
@RunWith(Parameterized.class)
public class MarksTest {

  @Parameterized.Parameters
  public static Object[][] matchTests() {
    return new Object[][] {
        // sanity check; marks BitSet should still be null
        {"foo", "foo", true, new int[]{}},
        // match, and mark 17 should be set
        {"(?M<17>foo)", "foo", true, new int[]{17}},
        // not match, but mark 24 should still be set
        {"(?M<24>foo)Bar", "fooBaz", false, new int[]{24}},
        // second mark should be set
        {"(?M<1>foo)|(?M<2>bar)", "bar", true, new int[]{2}},
        // all marks should be set
        {"(?M<10>b)(?M<11>a)(?M<12>z)", "baz", true, new int[]{10, 11, 12}},
        {"(?M<20>foo)|(?M<21>foobar)|(?M<22>foo2)|(?M<23>foo3)|(?M<24>foo4)", "foo", true, new int[]{20}},
    };
  }

  private final String pattern;
  private final String matchAgainst;
  private final boolean matches;
  private final int[] expectedMarks;

  public MarksTest(String pattern, String matchAgainst, boolean matches, int[] expectedMarks) {
    this.pattern = pattern;
    this.matchAgainst = matchAgainst;
    this.matches = matches;
    this.expectedMarks = expectedMarks;
  }

  @Test
  public void testMarksSet() {
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(matchAgainst);
    assertEquals(matches, m.matches());

    if (expectedMarks.length == 0) {
      assertNull(m.getMarks());
    } else {
      assertNotNull(m.getMarks());
      assertEquals(expectedMarks.length, m.getMarks().cardinality());
      for (int markNum : expectedMarks) {
        assertTrue(m.getMarks().get(markNum));
      }
    }
  }

}
