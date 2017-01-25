package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonationTypeViewModelBuilder.aDonationTypeViewModel;
import static org.jembi.bsis.helpers.matchers.DonationTypeViewModelMatcher.hasSameStateAsDonationTypeViewModel;

import java.util.Arrays;
import java.util.List;

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
    DonationType donationType = aDonationType()
        .withId(1L)
        .withName("Voluntary")
        .thatIsNotDeleted()
        .build();

    DonationTypeViewModel donationTypeViewModel = aDonationTypeViewModel()
        .withId(1L)
        .withType("Voluntary")
        .thatIsNotDeleted()
        .build();
    
    // Exercise SUT
    DonationTypeViewModel returnedViewModel = donationTypeFactory.createDonationTypeViewModel(donationType);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsDonationTypeViewModel(donationTypeViewModel));
  }

  
  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<DonationType> donationTypes = Arrays.asList(
        aDonationType().build(),
        aDonationType().build(),
        aDonationType().build());
    
    List<DonationTypeViewModel> returnedViewModels = donationTypeFactory.createDonationTypeViewModels(donationTypes);
    
    assertThat(returnedViewModels.size(), is(3));
  }
}