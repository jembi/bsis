package org.jembi.bsis.helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractTypeSafeMatcher<T> extends TypeSafeMatcher<T> {

  public T expected;

  public AbstractTypeSafeMatcher(T expected) {
    this.expected = expected;
  }

  public abstract void appendDescription(Description description, T t);

  @Override
  public void describeTo(Description description) {
    appendDescription("a " + expected.getClass().getSimpleName() + " with the following state:", description, expected);
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    appendDescription("was a " + item.getClass().getSimpleName() + " with the following state:", 
        mismatchDescription, item);
  }

  private void appendDescription(String initialMessage, Description description, T t) {
    description.appendText(initialMessage);
    appendDescription(description, t);
  }
}
