package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DiscardLabelTemplateObjectBuilder.aDiscardLabelTemplateObject;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.builders.PackLabelTemplateObjectBuilder.aPackLabelTemplateObject;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.template.TemplateEngine;
import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;
import org.jembi.bsis.template.labelling.PackLabelTemplateObject;
import org.jembi.bsis.template.labelling.TemplateObjectFactory;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class LabellingServiceTests extends UnitTestSuite {

  @Spy
  @InjectMocks
  LabellingService labellingService;
  
  @Mock
  private ComponentCRUDService componentCRUDService;
  
  @Mock
  private LabellingConstraintChecker labellingConstraintChecker;

  @Mock
  private CheckCharacterService checkCharacterService;

  @Mock
  private TemplateObjectFactory templateObjectFactory;

  @Mock
  private ComponentRepository componentRepository;
  
  @Mock
  private GeneralConfigRepository generalConfigRepository;
  
  @Mock
  private TemplateEngine templateEngine;
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  @Test
  public void testVerifyPackLabelWithValidInputs_shouldReturnTrue() {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("3000505")
        .withFlagCharacters("11")
        .build();
    Component component = aComponent()
      .withId(COMPONENT_ID)
      .withDonation(donation)
      .withStatus(ComponentStatus.AVAILABLE)
      .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
      .build();
    Component componentInStock = aComponent()
      .withId(COMPONENT_ID)
      .withDonation(donation)
      .withStatus(ComponentStatus.AVAILABLE)
      .withInventoryStatus(InventoryStatus.IN_STOCK)
      .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
      .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(componentCRUDService.putComponentInStock(component)).thenReturn(componentInStock);

    // run test
    boolean dinMatches = labellingService.verifyPackLabel(component.getId(), "3000505", "300050511");

    // check outcome
    assertThat(dinMatches, is(true));
    verify(componentCRUDService).putComponentInStock(argThat(hasSameStateAsComponent(component)));
  }

  @Test
  public void testVerifyPackLabelWithComponentStatusNotAvailable_shouldReturnFalse () {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("3000505")
        .withFlagCharacters("11")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.PROCESSED)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();
  
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
  
    // run test
    boolean dinMatches = labellingService.verifyPackLabel(component.getId(), "3000505", "300050511");
  
    // check outcome
    assertThat(dinMatches, is(false));
    verify(componentCRUDService, never()).putComponentInStock(any(Component.class));
  }  

  @Test
  public void testVerifyPackLabelWithInvalidPrePrintedDIN_shouldReturnFalse () {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("3000505")
        .withFlagCharacters("11")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);

    // run test
    boolean dinMatches = labellingService.verifyPackLabel(component.getId(), "1234567", "300050511");

    // check outcome
    assertThat(dinMatches, is(false));
    verify(componentCRUDService, never()).putComponentInStock(any(Component.class));
  }

  @Test
  public void testVerifyPackLabelWithInvalidPackLabelDIN_shouldReturnFalse() {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("3000505")
        .withFlagCharacters("11")
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);

    // run test
    boolean dinMatches = labellingService.verifyPackLabel(component.getId(), "3000505", "123456789");

    // check outcome
    assertThat(dinMatches, is(false));
    verify(componentCRUDService, never()).putComponentInStock(any(Component.class));
  }

  @Test
  public void testPrintDiscardLabel_shouldReturnZPLContainingDin() throws Exception {
    // set up data
    String donationIdentificationNumber = "1234567";
    Donation donation = aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();
    DiscardLabelTemplateObject discardLabelTemplateObject = aDiscardLabelTemplateObject()
        .withDIN(donationIdentificationNumber)
        .build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withName(GeneralConfigConstants.DISCARD_LABEL_ZPL)
        .withValue("CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD{{donation.DIN}}")
        .build();
    String discardLabelZpl = "CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD" + component.getDonationIdentificationNumber();
    String expectedDiscardLabelZpl = LabellingService.DATA_LINK_ESCAPE + discardLabelZpl;

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(true);
    when(templateObjectFactory.createDiscardLabelTemplateObject(argThat(hasSameStateAsComponent(component))))
      .thenReturn(discardLabelTemplateObject);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.DISCARD_LABEL_ZPL))
      .thenReturn(generalConfig);
    when(templateEngine.execute(generalConfig.getName(), generalConfig.getValue(), discardLabelTemplateObject))
      .thenReturn(discardLabelZpl);

    // run test
    String label = labellingService.printDiscardLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, is(expectedDiscardLabelZpl));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPrintDiscardLabel_throwsIllegalArgumentException() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.AVAILABLE)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(false);
    
    // run test
    labellingService.printDiscardLabel(COMPONENT_ID);
  }

  @Test
  public void testPrintPackLabelWithInStockComponent_shouldMarkAsRemoved() throws Exception {
    // set up data
    String donationIdentificationNumber = "1234567";
    Donation donation = aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withFlagCharacters("17")
        .build();
    String componentCode = "123";
    Component component = aComponent()
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .build();
    
    Component labelledComponent = aComponent()
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(donation)
        .withLocation(component.getLocation())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(componentCRUDService.removeComponentFromStock(argThat(hasSameStateAsComponent(component)))).thenReturn(labelledComponent);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PACK_LABEL_ZPL)).thenReturn(aGeneralConfig().build());

    // run test
    labellingService.printPackLabel(COMPONENT_ID);
    
    // check outcome
    verify(componentCRUDService).removeComponentFromStock(argThat(hasSameStateAsComponent(component)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPrintPackLabel_throwsIllegalArgumentException() throws Exception {
    // set up data
    String donationIdentificationNumber = "1234567";
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(false);
    
    // run test
    labellingService.printPackLabel(COMPONENT_ID);
  }
  
  @Test
  public void testPrintPackLabel_shouldReturnZPL() throws IOException {
    // set up data
    Donation donation = aDonation().withDonationIdentificationNumber("1234567").withFlagCharacters("00").build();
    ComponentType componentType = aComponentType().withComponentTypeName("name").build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentType(componentType)
        .build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withName(GeneralConfigConstants.PACK_LABEL_ZPL)
        .withValue("CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD{{donation.DIN}}")
        .build();
    PackLabelTemplateObject packLabelTemplateObject = aPackLabelTemplateObject()
        .withDIN(donation.getDonationIdentificationNumber())
        .build();
    String packLabelZpl =
        "CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD" + component.getDonationIdentificationNumber();
    String expectedPackLabelZpl = LabellingService.DATA_LINK_ESCAPE + packLabelZpl;
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PACK_LABEL_ZPL)).thenReturn(generalConfig);
    when(templateObjectFactory.createPackLabelTemplateObject(component)).thenReturn(packLabelTemplateObject);
    when(templateEngine.execute(generalConfig.getName(), generalConfig.getValue(), packLabelTemplateObject)).thenReturn(packLabelZpl);
    
    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, is(expectedPackLabelZpl));
  }

  @Test
  public void testPrintPackLabelWithComponentLinkedToDonationWithoutFlagCharacters_shouldAddFlagcharactersBeforePrinting() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("3000505")
        .build();   
    Component labelledComponent = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode("123")
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withDonation(donation)
        .build();
    
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(labelledComponent);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(labelledComponent)).thenReturn(true);
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PACK_LABEL_ZPL)).thenReturn(aGeneralConfig().build());
    
    // run test
    labellingService.printPackLabel(labelledComponent.getId());
    
    // check outcome
    verify(checkCharacterService).calculateFlagCharacters(donation.getDonationIdentificationNumber());
    assertThat("This donation now has flag characters", donation.getFlagCharacters().equals("11"));
  }

  @Test
  public void testFindSafeComponentWithNullDin_shouldDoFindSafeComponentsSearch() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("a+");
    bloodGroups.add("a-");
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    // set up mocks
    when(componentRepository.findSafeComponents(componentTypeId, locationId, BloodGroup.toBloodGroups(bloodGroups),
        null, null, null, false))
        .thenReturn(null);
    // run test
    labellingService.findSafeComponentsToLabel(null, null, componentTypeId, locationId, bloodGroups, null, null, null);
    // verify
    verify(componentRepository).findSafeComponents(componentTypeId, locationId, BloodGroup.toBloodGroups(bloodGroups),
        null, null, null, false);
  }

  @Test
  public void testFindSafeComponentWithNullDinAndWithInventoryStatusNotInStock_shouldDoFindSafeComponentsSearch() {
    // set up mocks
    when(componentRepository.findSafeComponents(null, null, null, null, null, 
        Arrays.asList(InventoryStatus.NOT_IN_STOCK, InventoryStatus.REMOVED), false))
        .thenReturn(null);
    // run test
    labellingService.findSafeComponentsToLabel(null, null, null, null, null, null, null, InventoryStatus.NOT_IN_STOCK);
    // verify
    verify(componentRepository).findSafeComponents(null, null, null, null, null, 
        Arrays.asList(InventoryStatus.NOT_IN_STOCK, InventoryStatus.REMOVED), false);
  }

  @Test
  public void testFindSafeComponentWithNullDinAndWithInventoryStatusInStock_shouldDoFindSafeComponentsSearch() {
    // set up mocks
    when(componentRepository.findSafeComponents(null, null, null, null, null, 
        Arrays.asList(InventoryStatus.IN_STOCK), false))
        .thenReturn(null);
    // run test
    labellingService.findSafeComponentsToLabel(null, null, null, null, null, null, null, InventoryStatus.IN_STOCK);
    // verify
    verify(componentRepository).findSafeComponents(null, null, null, null, null, 
        Arrays.asList(InventoryStatus.IN_STOCK), false);
  }

  @Test
  public void testFindSafeComponentWithDinAndComponentCode_shouldDoFindComponentsByDINAndComponentCodeAndStatus() {
    String din = "123467";
    String componentCode = "1234";
    // set up mocks
    when(componentRepository.findComponentsByDINAndComponentCodeAndStatus(din, componentCode, ComponentStatus.AVAILABLE,
        false)).thenReturn(null);
    // run test
    labellingService.findSafeComponentsToLabel(din, componentCode, null, null, null, null, null, null);
    // verify
    verify(componentRepository).findComponentsByDINAndComponentCodeAndStatus(din, componentCode,
        ComponentStatus.AVAILABLE, false);
  }

}
