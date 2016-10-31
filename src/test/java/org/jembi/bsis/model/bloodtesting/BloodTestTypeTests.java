package org.jembi.bsis.model.bloodtesting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

public class BloodTestTypeTests {
  
  @Test
  public void testGetBloodTestTypeForCategoryTTI_shouldReturnCorrectTypes() {
    List<BloodTestType> types = BloodTestType.getBloodTestTypeForCategory(BloodTestCategory.TTI);
    assertThat("Types are returned", types.size(), is(3));
    assertThat("BASIC_TTI is in types", types.contains(BloodTestType.BASIC_TTI));
    assertThat("REPEAT_TTI is in types", types.contains(BloodTestType.REPEAT_TTI));
    assertThat("CONFIRMATORY_TTI is in types", types.contains(BloodTestType.CONFIRMATORY_TTI));
  }

  @Test
  public void testGetBloodTestTypeForCategoryBLOODTYPING_shouldReturnCorrectTypes() {
    List<BloodTestType> types = BloodTestType.getBloodTestTypeForCategory(BloodTestCategory.BLOODTYPING);
    assertThat("Types are returned", types.size(), is(2));
    assertThat("BASIC_BLOODTYPING is in types", types.contains(BloodTestType.BASIC_BLOODTYPING));
    assertThat("REPEAT_BLOODTYPING is in types", types.contains(BloodTestType.REPEAT_BLOODTYPING));
  }
}