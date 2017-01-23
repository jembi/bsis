package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.DonationTypeMatcher.hasSameStateAsDonationType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationTypeCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  DonationTypeCRUDService donationTypeCRUDService;

  @Mock
  DonationTypeRepository donationTypeRepository;

  @Test
  public void testUpdateDonationType_callsRepositoryWithCorrectEntity() throws Exception {
    User user = aUser().withId(1L).build();
    Date createdDate = new Date();
    Date lastUpdated = new Date();
    
    DonationType donationType = aDonationType()
        .withId(1L)
        .withName("New Name")
        .thatIsNotDeleted()
        .build();

    DonationType existingDonationType = aDonationType()
        .withId(1L)
        .withName("Name")
        .withCreatedBy(user)
        .withCreatedDate(createdDate)
        .withLastUpdated(lastUpdated)
        .withLastUpdatedBy(user)
        .thatIsNotDeleted()
        .build();

    DonationType expectedDonationType = aDonationType()
        .withId(1L)
        .withName("New Name")
        .withCreatedBy(user)
        .withCreatedDate(createdDate)
        .withLastUpdated(lastUpdated)
        .withLastUpdatedBy(user)
        .thatIsNotDeleted()
        .build();

    when(donationTypeRepository.getDonationTypeById(1L)).thenReturn(existingDonationType);
    when(donationTypeRepository.update(argThat(hasSameStateAsDonationType(expectedDonationType)))).thenReturn(
        expectedDonationType);

    donationTypeCRUDService.updateDonationType(donationType);
    
    verify(donationTypeRepository).update(argThat(hasSameStateAsDonationType(expectedDonationType)));
  }
}
