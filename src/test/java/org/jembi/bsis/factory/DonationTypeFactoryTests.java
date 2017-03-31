package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonationTypeViewModelBuilder.aDonationTypeViewModel;
import static org.jembi.bsis.helpers.builders.DonationTypeBackingFormBuilder.aDonationTypeBackingForm;
import static org.jembi.bsis.helpers.matchers.DonationTypeViewModelMatcher.hasSameStateAsDonationTypeViewModel;
import static org.jembi.bsis.helpers.matchers.DonationTypeMatcher.hasSameStateAsDonationType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class DonationTypeFactoryTests extends UnitTestSuite {

  @InjectMocks
  private DonationTypeFactory donationTypeFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID donationTypeId = UUID.randomUUID();

    DonationType donationType = aDonationType()
        .withId(donationTypeId)
        .withName("Voluntary")
        .thatIsNotDeleted()
        .build();

    DonationTypeViewModel donationTypeViewModel = aDonationTypeViewModel()
        .withId(donationTypeId)
        .withType("Voluntary")
        .thatIsNotDeleted()
        .build();
    
    // Exercise SUT
    DonationTypeViewModel returnedViewModel = donationTypeFactory.createViewModel(donationType);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDonationTypeViewModel(donationTypeViewModel));
  }

  @Test
  public void testCreateViewModelsWithNull_shouldReturnEmptyList() {
    List<DonationTypeViewModel> returnedViewModels = donationTypeFactory.createViewModels(null);
    assertThat(returnedViewModels, is(notNullValue()));
    assertThat(returnedViewModels.size(), is(0));
  }

  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<DonationType> donationTypes = Arrays.asList(
        aDonationType().build(),
        aDonationType().build(),
        aDonationType().build());
    
    List<DonationTypeViewModel> returnedViewModels = donationTypeFactory.createViewModels(donationTypes);
    
    assertThat(returnedViewModels.size(), is(3));
  }

  @Test
  public void testCreateEntity_shouldReturnEntity() {
    // Set up fixtures
    UUID donationTypeId = UUID.randomUUID();

    DonationTypeBackingForm form = aDonationTypeBackingForm()
        .withId(donationTypeId)
        .withDonationType("type")
        .thatIsNotDeleted()
        .build();

    DonationType expectedDonationType = aDonationType()
        .withId(donationTypeId)
        .withName("type")
        .thatIsNotDeleted()
        .build();

    // Exercise SUT
    DonationType returnedDonationType = donationTypeFactory.createEntity(form);

    // Verify
    assertThat(returnedDonationType, hasSameStateAsDonationType(expectedDonationType));
  }
}