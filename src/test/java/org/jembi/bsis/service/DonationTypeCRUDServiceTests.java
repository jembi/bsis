package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.DonationTypeMatcher.hasSameStateAsDonationType;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

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
    UUID userId = UUID.randomUUID();
    User user = aUser().withId(userId).build();
    Date createdDate = new Date();
    Date lastUpdated = new Date();
    UUID donationTypeId = UUID.randomUUID();
    
    DonationType donationType = aDonationType()
        .withId(donationTypeId)
        .withName("New Name")
        .thatIsNotDeleted()
        .build();

    DonationType existingDonationType = aDonationType()
        .withId(donationTypeId)
        .withName("Name")
        .withCreatedBy(user)
        .withCreatedDate(createdDate)
        .withLastUpdated(lastUpdated)
        .withLastUpdatedBy(user)
        .thatIsNotDeleted()
        .build();

    DonationType expectedDonationType = aDonationType()
        .withId(donationTypeId)
        .withName("New Name")
        .withCreatedBy(user)
        .withCreatedDate(createdDate)
        .withLastUpdated(lastUpdated)
        .withLastUpdatedBy(user)
        .thatIsNotDeleted()
        .build();

    when(donationTypeRepository.getDonationTypeById(donationTypeId)).thenReturn(existingDonationType);
    when(donationTypeRepository.update(argThat(hasSameStateAsDonationType(expectedDonationType)))).thenReturn(
        expectedDonationType);

    donationTypeCRUDService.updateDonationType(donationType);
    
    verify(donationTypeRepository).update(argThat(hasSameStateAsDonationType(expectedDonationType)));
  }
}
