package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DiscardLabelTemplateObjectBuilder.aDiscardLabelTemplateObject;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodAbo;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.template.TemplateEngine;
import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;
import org.jembi.bsis.template.labelling.TemplateObjectFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class LabellingServiceTests extends UnitTestSuite {
  
  private static final String DATE_FORMAT = "dd/MM/yyyy";
  private static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";
  private static final String PREPARATION_INFO = "Prepared from 450 ±50ml";
  private static final String STORAGE_INFO = "Store below -30°C";
  private static final String TRANSPORT_INFO = "Transport below -25°C";
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");

  @Spy
  @InjectMocks
  LabellingService labellingService;
  
  @Mock
  private ComponentCRUDService componentCRUDService;
  
  @Mock
  private LabellingConstraintChecker labellingConstraintChecker;
  
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Mock
  private CheckCharacterService checkCharacterService;

  @Mock
  private ComponentVolumeService componentVolumeService;

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
        .withId(DONATION_ID)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentType(ComponentTypeBuilder.aComponentType().withComponentTypeCode("3001").build())
        .build();
    DiscardLabelTemplateObject discardLabelTemplateObject = aDiscardLabelTemplateObject()
        .withServiceInfoLine1("Line 1")
        .withServiceInfoLine2("Line 2")
        .build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withName(GeneralConfigConstants.DISCARD_LABEL_ZPL)
        .withValue("CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD{{donation.DIN}}")
        .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(true);
    when(templateObjectFactory.createDiscardLabelTemplateObject(argThat(hasSameStateAsComponent(component))))
      .thenReturn(discardLabelTemplateObject);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.DISCARD_LABEL_ZPL))
    .thenReturn(generalConfig);
    when(templateEngine.execute("DISCARD_LABEL_TEMPLATE_NAME", generalConfig.getValue(), discardLabelTemplateObject))
    .thenReturn("CT~~CD,~CC^~CT~^XA^FX DIN barcode^BY3,3,77^FT75,140^BCN,,Y,N^FD" + component.getDonationIdentificationNumber());

    // run test
    String label = labellingService.printDiscardLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber));
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
  public void testPrintPackLabelWithPositiveRhBlood_shouldReturnZPLContainingText() throws Exception {
    // set up data
    String bloodAbo = "A";
    String bloodRh = "+";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withFlagCharacters("28")
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();

    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();

    Component componentRemoved = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.removeComponentFromStock(argThat(hasSameStateAsComponent(component)))).thenReturn(componentRemoved);

    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);
    
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
    String bloodAbo = "A";
    String bloodRh = "-";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withFlagCharacters("32")
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();

    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();

    Component componentRemoved = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.removeComponentFromStock(argThat(hasSameStateAsComponent(component)))).thenReturn(componentRemoved);
    
    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);
    
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
  public void testPrintPackLabelWithInStockComponent_shouldMarkAsNotInStock() throws Exception {
    // set up data
    String bloodAbo = "A";
    String bloodRh = "-";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withFlagCharacters("17")
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();
    
    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    Component labelledComponent = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(component.getLocation())
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.removeComponentFromStock(argThat(hasSameStateAsComponent(component)))).thenReturn(labelledComponent);

    // run test
    labellingService.printPackLabel(COMPONENT_ID);
    
    // check outcome
    verify(componentCRUDService).removeComponentFromStock(argThat(hasSameStateAsComponent(component)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPrintPackLabel_throwsException() throws Exception {
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
  public void testPrintPackLabelThatShouldContainHighTitre_shouldReturnZPLWithHighTitreText() {
    // set up data
    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withBloodRh("+")
        .withDonationIdentificationNumber("1234567")
        .withFlagCharacters("17")
        .build();
    ComponentType componentType = aComponentType().withComponentTypeName("name").build();
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(labellingService.shouldLabelIncludeHighTitre(component)).thenReturn(true);
    
    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, label.contains("HIGH TITRE"));
  }
  
  @Test
  public void testPrintPackLabelThatShouldntContainHighTitre_shouldntReturnZPLWithHighTitreText() {
    // set up data
    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withBloodRh("+")
        .withDonationIdentificationNumber("1234567")
        .withFlagCharacters("17")
        .build();
    ComponentType componentType = aComponentType().withComponentTypeName("name").build();
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(labellingService.shouldLabelIncludeHighTitre(component)).thenReturn(false);
    
    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, !label.contains("HIGH TITRE"));
  }

  @Test
  public void testPrintPackLabelThatShouldContainVolume_shouldReturnZPLWithVolumeText() {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("1234567")
        .withBloodRh("+")
        .withDonationDate(new Date())
        .build();
    ComponentType componentType = aComponentType()
        .withGravity(1.03)
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(new Date())
        .withWeight(100)
        .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(labellingService.shouldLabelIncludeHighTitre(component)).thenReturn(true);
    when(componentVolumeService.calculateVolume(component)).thenReturn(327);

    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);

    // check outcome
    assertThat(label, label.contains("Volume: 327ml"));
  }

  @Test
  public void testPrintPackLabelForComponentWithoutWeight_shouldReturnZPLWithVolumeText() {
    // set up data
    Donation donation = aDonation()
        .withDonationIdentificationNumber("1234567")
        .withBloodRh("+")
        .withDonationDate(new Date())
        .build();
    ComponentType componentType = aComponentType()
        .withGravity(1.03)
        .build();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(new Date())
        .withWeight(null)
        .build();

    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(labellingService.shouldLabelIncludeHighTitre(component)).thenReturn(true);
    when(componentVolumeService.calculateVolume(component)).thenReturn(null);

    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);

    // check outcome
    assertThat(label.contains("Volume"), is(false));
  }

  @Test
  public void testShouldLabelIncludeHighTitre_shouldReturnTrue() {
    // Set up
    Donation donation = aDonation().withTitre(Titre.HIGH).withBloodAbo(BloodAbo.O.name()).build();
    Component component =
        aComponent().withDonation(donation).withComponentType(aComponentType().thatContainsPlasma().build()).build();

    // Exercise SUT
    boolean includeHighTitre = labellingService.shouldLabelIncludeHighTitre(component);

    // Verify
    assertThat(includeHighTitre, is(true));
  }

  @Test
  public void testShouldLabelIncludeHighTitreWithLowTitre_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().withTitre(Titre.LOW).withBloodAbo(BloodAbo.O.name()).build();
    Component component =
        aComponent().withDonation(donation).withComponentType(aComponentType().thatContainsPlasma().build()).build();

    // Exercise SUT
    boolean includeHighTitre = labellingService.shouldLabelIncludeHighTitre(component);

    // Verify
    assertThat(includeHighTitre, is(false));
  }

  @Test
  public void testShouldLabelIncludeHighTitreWithNoTitre_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().withTitre(null).withBloodAbo(BloodAbo.O.name()).build();
    Component component =
        aComponent().withDonation(donation).withComponentType(aComponentType().thatContainsPlasma().build()).build();

    // Exercise SUT
    boolean includeHighTitre = labellingService.shouldLabelIncludeHighTitre(component);

    // Verify
    assertThat(includeHighTitre, is(false));
  }

  @Test
  public void testShouldLabelIncludeHighTitreWithNoOAbo_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().withTitre(Titre.HIGH).withBloodAbo(BloodAbo.A.name()).build();
    Component component =
        aComponent().withDonation(donation).withComponentType(aComponentType().thatContainsPlasma().build()).build();

    // Exercise SUT
    boolean includeHighTitre = labellingService.shouldLabelIncludeHighTitre(component);

    // Verify
    assertThat(includeHighTitre, is(false));
  }

  @Test
  public void testShouldLabelIncludeHighTitreWithNoPlasma_shouldReturnFalse() {
    // Set up
    Donation donation = aDonation().withTitre(Titre.HIGH).withBloodAbo(BloodAbo.O.name()).build();
    Component component = aComponent().withDonation(donation)
        .withComponentType(aComponentType().thatDoesntContainsPlasma().build()).build();

    // Exercise SUT
    boolean includeHighTitre = labellingService.shouldLabelIncludeHighTitre(component);

    // Verify
    assertThat(includeHighTitre, is(false));
  }

  @Test
  public void testPrintPackLabelWithComponentLinkedToDonationWithoutFlagCharacters_ShouldAddFlagcharactersBeforePrinting() throws Exception {
    // set up data
    String bloodAbo = "B";
    String bloodRh = "-";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationIdentificationNumber("3000505")
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

    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();    
    Component labelledComponent = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(labelledComponent);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(labelledComponent)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");
    
    // run test
    labellingService.printPackLabel(labelledComponent.getId());
    
    // check outcome
    verify(checkCharacterService).calculateFlagCharacters(donation.getDonationIdentificationNumber());
    assertThat("This donation now has flag characters", donation.getFlagCharacters().equals("11"));
  }

  @Test
  public void testPrintPackLabelWithFlagCharacters34AndCheckCharacterY_shouldReturnZPLContainingText() throws Exception {
    // set up data
    String bloodAbo = "A";
    String bloodRh = "+";
    String donationIdentificationNumber = "123456789102345678";
    String flagCharacters = "34";
    Date donationDate = new Date();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withFlagCharacters(flagCharacters)
        .build();
    String componentTypeName = "blood";
    ComponentType componentType = aComponentType()
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(PREPARATION_INFO)
        .withStorageInfo(STORAGE_INFO)
        .withTransportInfo(TRANSPORT_INFO)
        .build();

    String componentCode = "123";
    Date expiresOn = new DateTime().plusDays(90).toDate();
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentCode(componentCode)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();
    
    // set up mocks
    when(componentCRUDService.findComponentById(COMPONENT_ID)).thenReturn(component);
    when(labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component)).thenReturn(true);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_FORMAT)).thenReturn(DATE_FORMAT);
    when(generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.DATE_TIME_FORMAT)).thenReturn(DATE_TIME_FORMAT);
    when(componentCRUDService.putComponentInStock(component)).thenReturn(component);
    when(checkCharacterService.calculateCheckCharacter("34")).thenReturn("Y");
    // run test
    String label = labellingService.printPackLabel(COMPONENT_ID);
    
    // check outcome
    assertThat(label, label.contains(donationIdentificationNumber + flagCharacters));
    assertThat(label, label.contains("^FD34^FS"));  // assert that Label contains Flag Characters
    assertThat(label, label.contains("^FDY^FS"));   // assert that Label contains Check Character
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
        null, null, null,
        false);
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
