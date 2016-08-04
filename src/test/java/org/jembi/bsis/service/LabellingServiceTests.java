package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.argThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LabellingServiceTests extends UnitTestSuite {

  @InjectMocks
  LabellingService labellingService;
  
  @Mock
  private ComponentCRUDService componentCRUDService;
  
  @Mock
  private LabellingConstraintChecker labellingConstraintChecker;
  
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  
  @Test
  public void testPrintDiscardLabel_shouldReturnZPLContainingText() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    String donationIdentificationNumber = "1234567";
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(true);
    
    // run test
    String label = labellingService.printDiscardLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
  }
  
  @Test(expected = java.lang.IllegalArgumentException.class)
  public void testPrintDiscardLabel_throwsException() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    String donationIdentificationNumber = "1234567";
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(false);
    
    // run test
    labellingService.printDiscardLabel(componentId);
  }
  
  @Test
  public void testPrintPackLabelWithPositiveRhBlood_shouldReturnZPLContainingText() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1);
    String bloodAbo = "A";
    String bloodRh = "+";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(donationId)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withComponentTypeName(componentTypeName).build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(new Date())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(componentCRUDService.updateComponent(component)).thenReturn(component);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn("dd/MM/yyyy");
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn("dd/MM/yyyy hh:mm:ss a");
    
    // run test
    String label = labellingService.printPackLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
    assertThat(label, label.contains(bloodAbo));
    assertThat(label, label.contains("POSITIVE"));
    assertThat(label, label.contains(new SimpleDateFormat("dd/MM/yyyy").format(donationDate)));
    assertThat(label, label.contains(componentTypeName));
  }
  
  @Test
  public void testPrintPackLabelWithNegativeRhBlood_shouldReturnZPLContainingText() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1);
    String bloodAbo = "A";
    String bloodRh = "-";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(donationId)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withComponentTypeName(componentTypeName).build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(new Date())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(componentCRUDService.updateComponent(component)).thenReturn(component);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn("dd/MM/yyyy");
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn("dd/MM/yyyy hh:mm:ss a");
    
    // run test
    String label = labellingService.printPackLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
    assertThat(label, label.contains(bloodAbo));
    assertThat(label, label.contains("NEGATIVE"));
    assertThat(label, label.contains(new SimpleDateFormat("dd/MM/yyyy").format(donationDate)));
    assertThat(label, label.contains(componentTypeName));
  }
  
  @Test
  public void testPrintPackLabelWithNotInStockComponent_shouldMarkAsInStock() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1);
    String bloodAbo = "A";
    String bloodRh = "-";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(donationId)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withComponentTypeName(componentTypeName).build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Date expiresOn = new Date();
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    Component expectedComponent = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(component.getLocation())
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(componentCRUDService.updateComponent(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn("dd/MM/yyyy");
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn("dd/MM/yyyy hh:mm:ss a");
    
    // run test
    labellingService.printPackLabel(componentId);
    
    // check outcome
    verify(componentCRUDService).updateComponent(argThat(hasSameStateAsComponent(expectedComponent)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPrintPackLabel_throwsException() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    String donationIdentificationNumber = "1234567";
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(false);
    
    // run test
    labellingService.printPackLabel(componentId);
  }
}
