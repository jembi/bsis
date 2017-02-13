package org.jembi.bsis.controllerservice;

import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.helpers.builders.LocationBackingFormBuilder;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationControllerServiceIntegrationTests extends ContextDependentTestSuite {

  @Autowired
  LocationControllerService locationControllerService;

  @Test
  public void test() {
    Division divisionLevel1 = aDivision()
        .withName("South Africa").withLevel(1).buildAndPersist(entityManager);
    Division divisionLevel2 = aDivision()
        .withName("Western Cape").withLevel(2).withParent(divisionLevel1).buildAndPersist(entityManager);
    Division divisionLevel3 = aDivision()
        .withName("Cape Town").withLevel(3).withParent(divisionLevel2).buildAndPersist(entityManager);
    
    DivisionBackingForm divisionForm = aDivisionBackingForm()
        .withId(divisionLevel3.getId())
        .withLevel(3)
        .build();
    LocationBackingForm form = LocationBackingFormBuilder
        .aReferralSiteBackingForm()
        .withName("test")
        .withDivisionLevel3(divisionForm)
        .build();

    LocationFullViewModel location = locationControllerService.addLocation(form);

    Assert.assertNotNull(location.getId());
    Assert.assertEquals("test", location.getName());
    Assert.assertTrue(location.getIsReferralSite());
  }
}
