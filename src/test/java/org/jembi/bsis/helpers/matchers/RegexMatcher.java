package org.jembi.bsis.helpers.matchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class RegexMatcher extends TypeSafeMatcher<String> {
  
  private String pattern;

  public RegexMatcher(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A string that matches the following pattern: ").appendValue(pattern);
  }

  @Override
  public boolean matchesSafely(String actual) {
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(actual);
    return m.matches();
  }

  public static RegexMatcher containsPattern(String pattern) {
    return new RegexMatcher(pattern);
  }





}
