package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LabellingServiceTests extends UnitTestSuite {
  
  private static final String DATE_FORMAT = "dd/MM/yyyy";
  private static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";
  private static final String PREPARATION_INFO = "Prepared from 450 ±50ml";
  private static final String STORAGE_INFO = "Store below -30°C";
  private static final String TRANSPORT_INFO = "Transport below -25°C";

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
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.SERVICE_INFO_LINE_1)).thenReturn("Line 1");
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.SERVICE_INFO_LINE_2)).thenReturn("Line 2");

    // run test
    String label = labellingService.printDiscardLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains("3001"));
    assertThat(label, label.contains("Line 1"));
    assertThat(label, label.contains("Line 2"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPrintDiscardLabel_throwsException() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.AVAILABLE)
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
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.putComponentInStock(component)).thenReturn(component);

    // run test
    String label = labellingService.printPackLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
    assertThat(label, label.contains(bloodAbo));
    assertThat(label, label.contains("RhD POSITIVE"));
    assertThat(label, label.contains(new SimpleDateFormat(DATE_FORMAT).format(donationDate)));
    assertThat(label, label.contains(new SimpleDateFormat(DATE_TIME_FORMAT).format(expiresOn)));
    assertThat(label, label.contains(componentTypeName));
    assertThat(label, label.contains(PREPARATION_INFO));
    assertThat(label, label.contains(STORAGE_INFO));
    assertThat(label, label.contains(TRANSPORT_INFO));
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
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(componentId)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.putComponentInStock(component)).thenReturn(component);
    
    // run test
    String label = labellingService.printPackLabel(componentId);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
    assertThat(label, label.contains(bloodAbo));
    assertThat(label, label.contains("RhD NEGATIVE"));
    assertThat(label, label.contains(new SimpleDateFormat(DATE_FORMAT).format(donationDate)));
    assertThat(label, label.contains(new SimpleDateFormat(DATE_TIME_FORMAT).format(expiresOn)));
    assertThat(label, label.contains(componentTypeName));
    assertThat(label, label.contains(PREPARATION_INFO));
    assertThat(label, label.contains(STORAGE_INFO));
    assertThat(label, label.contains(TRANSPORT_INFO));
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
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();
    Long componentId = Long.valueOf(1);
    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    Component labelledComponent = aComponent()
        .withId(componentId)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(component.getLocation())
        .withExpiresOn(expiresOn)
        .build();
    
    Component finalComponent = aComponent()
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
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.putComponentInStock(argThat(hasSameStateAsComponent(labelledComponent)))).thenReturn(finalComponent);
    
    // run test
    labellingService.printPackLabel(componentId);
    
    // check outcome
    verify(componentCRUDService).putComponentInStock(argThat(hasSameStateAsComponent(labelledComponent)));
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
