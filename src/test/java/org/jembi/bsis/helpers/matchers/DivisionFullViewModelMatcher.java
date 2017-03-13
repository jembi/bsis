package org.jembi.bsis.helpers.matchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DivisionFullViewModel;
import org.jembi.bsis.viewmodel.DivisionViewModel;

public class DivisionFullViewModelMatcher extends TypeSafeMatcher<DivisionFullViewModel> {

  private DivisionFullViewModel expected;

  public DivisionFullViewModelMatcher(DivisionFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A DivisionFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nLevel: ").appendValue(expected.getLevel())
        .appendText("\nParent: ").appendValue(expected.getParent())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  protected boolean matchesSafely(DivisionFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getLevel(), expected.getLevel())
        && matchParent(actual.getParent(), expected.getParent())
        && valuesEquals(actual.getPermissions(), expected.getPermissions());
  }
  
//This method was specifically created so that java will not use 
 // default equals method when comparing the object parent which 
 // will always return false as a new object is always created
 private boolean matchParent(DivisionViewModel actualParent, DivisionViewModel expectedParent) {
   if (actualParent == null && expectedParent == null) {
     return true;
   } else if ((actualParent == null && expectedParent != null) || (actualParent != null && expectedParent == null)) {
     return false;
   }
   return Objects.equals(actualParent.getId(), expectedParent.getId())
       && Objects.equals(actualParent.getName(), expectedParent.getName())
       && Objects.equals(actualParent.getLevel(), expectedParent.getLevel());
 }
 
 private <V extends Comparable<V>> boolean valuesEquals(Map<?,V> actualPermissionsMap, Map<?,V> expectedPermissionsMap) {
   List<V> actualPermissionsList = new ArrayList<V>(actualPermissionsMap.values());
   List<V> expectedPermissionsList = new ArrayList<V>(expectedPermissionsMap.values());
   Collections.sort(actualPermissionsList);
   Collections.sort(expectedPermissionsList);
   return actualPermissionsList.equals(expectedPermissionsList);
}
  
  public static DivisionFullViewModelMatcher hasSameStateAsDivisionFullViewModel(DivisionFullViewModel expected) {
    return new DivisionFullViewModelMatcher(expected);
  }

}
