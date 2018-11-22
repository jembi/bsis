package org.jembi.bsis.template.labelling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DiscardLabelTemplateObjectBuilder.aDiscardLabelTemplateObject;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackLabelTemplateObjectBuilder.aPackLabelTemplateObject;
import static org.jembi.bsis.helpers.matchers.DiscardLabelTemplateObjectMatcher.hasSameStateAsDiscardLabelTemplateObject;
import static org.jembi.bsis.helpers.matchers.PackLabelTemplateObjectMatcher.hasSameStateAsPackLabelTemplateObject;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.service.CheckCharacterService;
import org.jembi.bsis.service.ComponentVolumeService;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class TemplateObjectFactoryTests extends UnitTestSuite {

  @Spy
  @InjectMocks
  private TemplateObjectFactory templateObjectFactory;

  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Mock
  private ComponentVolumeService componentVolumeService;

  @Mock
  private CheckCharacterService checkCharacterService;

  @Test
  public void testCreateDiscardLabelTemplateObject_shouldReturnADiscardLabelTemplateObject() {
    // Set up fixture
    String componentCode = "1001-01";
    String componentTypeCode = "1001";
    String serviceInfoLine1 = "service info line 1";
    String serviceInfoLine2 = "service info line 2";
    String DIN = "3000600";

    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .build();

    ComponentType componentType = aComponentType()
        .withComponentTypeCode(componentTypeCode)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withComponentType(componentType)
        .withDonation(donation)
        .build();

    DiscardLabelTemplateObject expectedResult = aDiscardLabelTemplateObject()
        .withComponentCode(componentCode)
        .withComponentTypeCode(componentTypeCode)
        .withDIN(DIN)
        .withServiceInfoLine1(serviceInfoLine1)
        .withServiceInfoLine2(serviceInfoLine2)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1))
      .thenReturn(serviceInfoLine1);

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2))
      .thenReturn(serviceInfoLine2);

    // Exercise SUT
    DiscardLabelTemplateObject actualResult = templateObjectFactory.createDiscardLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsDiscardLabelTemplateObject(expectedResult));
  }

  @Test
  public void testCreatePackLabelTemplateObject_shouldReturnAPackLabelTemplateObject() throws ParseException {
    // Set up fixture
    int flagCharPos = 123;
    int boxPos = 153;
    int checkCharPos = 162;

    String serviceInfoLine1 = "service info line 1";
    String serviceInfoLine2 = "service info line 2";

    String DIN = "12345";
    String flagCharacters = "21";
    String checkCharacter = "A";
    String bloodABO = "A";
    String bloodRh = "+";
    String donationDate = "2017/05/29";
    String donationDateISO = "2017-05-29";

    String componentCode = "1001";
    String expiresOn = "2017/05/29 00:30:00";
    String expiresOnISO = "2017-05-29";
    Integer volume = 40;

    String componentTypeName = "Fresh Frozen Fish";
    String preparationInfo = "Shaken not stirred";
    String storageInfo = "Cold Storage";
    String transportInfo = "Rocket Launch";

    Date expiresOnDate = null;
    Date donationDateDate = null;

    expiresOnDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expiresOn);
    donationDateDate = new SimpleDateFormat("yyyy/MM/dd").parse(donationDate);


    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .withFlagCharacters(flagCharacters)
        .withBloodAbo(bloodABO)
        .withBloodRh(bloodRh)
        .withDonationDate(donationDateDate)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOnDate)
        .withDonation(donation)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .withPreparationInfo(preparationInfo)
            .withStorageInfo(storageInfo)
            .withTransportInfo(transportInfo)
            .build())
        .build();

    PackLabelTemplateObject expectedResult = aPackLabelTemplateObject()
        .withServiceInfoLine1(serviceInfoLine1)
        .withServiceInfoLine2(serviceInfoLine2)
        .withFlagCharPos(flagCharPos)
        .withBoxPos(boxPos)
        .withCheckCharPos(checkCharPos)
        .withDIN(DIN)
        .withFlagCharacters(flagCharacters)
        .withCheckCharacter(checkCharacter)
        .withBloodABO(bloodABO)
        .withBloodRh(bloodRh)
        .thatIsBloodRhPositive()
        .thatIsNotBloodRhNegative()
        .thatIsNotBloodHighTitre()
        .withDonationDate(donationDate)
        .withDonationDateISO(donationDateISO)
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOn)
        .withExpiresOnISO(expiresOnISO)
        .withVolume(volume)
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(preparationInfo)
        .withStorageInfo(storageInfo)
        .withTransportInfo(transportInfo)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1))
      .thenReturn(serviceInfoLine1);

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2))
      .thenReturn(serviceInfoLine2);

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateFormat")).thenReturn("yyyy/MM/dd");

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat")).thenReturn("yyyy/MM/dd HH:mm:ss");

    when(componentVolumeService.calculateVolume(component)).thenReturn(volume);

    when(checkCharacterService.calculateCheckCharacter(flagCharacters)).thenReturn(checkCharacter);

    // Exercise SUT
    PackLabelTemplateObject actualResult = templateObjectFactory.createPackLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsPackLabelTemplateObject(expectedResult));
  }

  @Test
  public void testCreatePackLabelTemplateObjectWithHighTitre_shouldReturnAPackLabelTemplateObject()
      throws ParseException {
    // Set up fixture
    int flagCharPos = 123;
    int boxPos = 153;
    int checkCharPos = 162;

    String serviceInfoLine1 = "service info line 1";
    String serviceInfoLine2 = "service info line 2";

    String DIN = "12345";
    String flagCharacters = "21";
    String checkCharacter = "A";
    String bloodABO = "O";
    String bloodRh = "-";
    String donationDate = "2017/05/29";
    String donationDateISO = "2017-05-29";

    String componentCode = "1001";
    String expiresOn = "2017/05/29 00:30:00";
    String expiresOnISO = "2017-05-29";
    Integer volume = 40;

    String componentTypeName = "Fresh Frozen Fish";
    String preparationInfo = "Shaken not stirred";
    String storageInfo = "Cold Storage";
    String transportInfo = "Rocket Launch";

    Date expiresOnDate = null;
    Date donationDateDate = null;

    expiresOnDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expiresOn);
    donationDateDate = new SimpleDateFormat("yyyy/MM/dd").parse(donationDate);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .withFlagCharacters(flagCharacters)
        .withBloodAbo(bloodABO)
        .withBloodRh(bloodRh)
        .withDonationDate(donationDateDate)
        .withTitre(Titre.HIGH)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOnDate)
        .withDonation(donation)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .withPreparationInfo(preparationInfo)
            .withStorageInfo(storageInfo)
            .withTransportInfo(transportInfo)
            .thatContainsPlasma()
            .build())
        .build();

    PackLabelTemplateObject expectedResult = aPackLabelTemplateObject()
        .withServiceInfoLine1(serviceInfoLine1)
        .withServiceInfoLine2(serviceInfoLine2)
        .withFlagCharPos(flagCharPos)
        .withBoxPos(boxPos)
        .withCheckCharPos(checkCharPos)
        .withDIN(DIN)
        .withFlagCharacters(flagCharacters)
        .withCheckCharacter(checkCharacter)
        .withBloodABO(bloodABO)
        .withBloodRh(bloodRh)
        .thatIsNotBloodRhPositive()
        .thatIsBloodRhNegative()
        .thatIsBloodHighTitre()
        .withDonationDate(donationDate)
        .withDonationDateISO(donationDateISO)
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOn)
        .withExpiresOnISO(expiresOnISO)
        .withVolume(volume)
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(preparationInfo)
        .withStorageInfo(storageInfo)
        .withTransportInfo(transportInfo)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1))
      .thenReturn(serviceInfoLine1);

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2))
      .thenReturn(serviceInfoLine2);

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateFormat")).thenReturn("yyyy/MM/dd");

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat")).thenReturn("yyyy/MM/dd HH:mm:ss");

    when(componentVolumeService.calculateVolume(component)).thenReturn(volume);

    when(checkCharacterService.calculateCheckCharacter(flagCharacters)).thenReturn(checkCharacter);

    // Exercise SUT
    PackLabelTemplateObject actualResult = templateObjectFactory.createPackLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsPackLabelTemplateObject(expectedResult));
  }

  @Test
  public void testCreatePackLabelTemplateObjectWithLowBloodTitreAndONegBlood_shouldReturnAPackLabelTemplateObject()
      throws ParseException {
    // Set up fixture
    int flagCharPos = 123;
    int boxPos = 153;
    int checkCharPos = 162;

    String serviceInfoLine1 = "service info line 1";
    String serviceInfoLine2 = "service info line 2";

    String DIN = "12345";
    String flagCharacters = "21";
    String checkCharacter = "A";
    String bloodABO = "O";
    String bloodRh = "-";
    String donationDate = "2017/05/29";
    String donationDateISO = "2017-05-29";

    String componentCode = "1001";
    String expiresOn = "2017/05/29 00:30:00";
    String expiresOnISO = "2017-05-29";
    Integer volume = 40;

    String componentTypeName = "Fresh Frozen Fish";
    String preparationInfo = "Shaken not stirred";
    String storageInfo = "Cold Storage";
    String transportInfo = "Rocket Launch";

    Date expiresOnDate = null;
    Date donationDateDate = null;

    expiresOnDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expiresOn);
    donationDateDate = new SimpleDateFormat("yyyy/MM/dd").parse(donationDate);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .withFlagCharacters(flagCharacters)
        .withBloodAbo(bloodABO)
        .withBloodRh(bloodRh)
        .withDonationDate(donationDateDate)
        .withTitre(Titre.LOW)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOnDate)
        .withDonation(donation)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .withPreparationInfo(preparationInfo)
            .withStorageInfo(storageInfo)
            .withTransportInfo(transportInfo)
            .thatContainsPlasma()
            .build())
        .build();

    PackLabelTemplateObject expectedResult = aPackLabelTemplateObject()
        .withServiceInfoLine1(serviceInfoLine1)
        .withServiceInfoLine2(serviceInfoLine2)
        .withFlagCharPos(flagCharPos)
        .withBoxPos(boxPos)
        .withCheckCharPos(checkCharPos)
        .withDIN(DIN)
        .withFlagCharacters(flagCharacters)
        .withCheckCharacter(checkCharacter)
        .withBloodABO(bloodABO)
        .withBloodRh(bloodRh)
        .thatIsNotBloodRhPositive()
        .thatIsBloodRhNegative()
        .thatIsNotBloodHighTitre()
        .withDonationDate(donationDate)
        .withDonationDateISO(donationDateISO)
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOn)
        .withExpiresOnISO(expiresOnISO)
        .withVolume(volume)
        .withComponentTypeName(componentTypeName)
        .withPreparationInfo(preparationInfo)
        .withStorageInfo(storageInfo)
        .withTransportInfo(transportInfo)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1))
      .thenReturn(serviceInfoLine1);

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2))
      .thenReturn(serviceInfoLine2);

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateFormat")).thenReturn("yyyy/MM/dd");

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat")).thenReturn("yyyy/MM/dd HH:mm:ss");

    when(componentVolumeService.calculateVolume(component)).thenReturn(volume);

    when(checkCharacterService.calculateCheckCharacter(flagCharacters)).thenReturn(checkCharacter);

    // Exercise SUT
    PackLabelTemplateObject actualResult = templateObjectFactory.createPackLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsPackLabelTemplateObject(expectedResult));
  }

  @Test
  public void testCreatePackLabelTemplateObjectWithHighBloodTitreButNotPlasma_shouldReturnAPackLabelTemplateObject()
      throws ParseException {
    // Set up fixture
    int flagCharPos = 123;
    int boxPos = 153;
    int checkCharPos = 162;
    String DIN = "12345";
    String bloodABO = "O";
    String bloodRh = "-";
    String donationDate = "2017/05/29";
    String donationDateISO = "2017-05-29";
    String componentCode = "1001";
    String expiresOn = "2017/05/29 00:30:00";
    String expiresOnISO = "2017-05-29";
    String componentTypeName = "Fresh Frozen Fish";
    Date expiresOnDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expiresOn);
    Date donationDateDate = new SimpleDateFormat("yyyy/MM/dd").parse(donationDate);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .withBloodAbo(bloodABO)
        .withBloodRh(bloodRh)
        .withDonationDate(donationDateDate)
        .withTitre(Titre.HIGH)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOnDate)
        .withDonation(donation)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .thatDoesntContainsPlasma()
            .build())
        .build();

    PackLabelTemplateObject expectedResult = aPackLabelTemplateObject()
        .withFlagCharPos(flagCharPos)
        .withBoxPos(boxPos)
        .withCheckCharPos(checkCharPos)
        .withDIN(DIN)
        .withBloodABO(bloodABO)
        .withBloodRh(bloodRh)
        .thatIsNotBloodRhPositive()
        .thatIsBloodRhNegative()
        .thatIsNotBloodHighTitre()
        .withDonationDate(donationDate)
        .withDonationDateISO(donationDateISO)
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOn)
        .withExpiresOnISO(expiresOnISO)
        .withComponentTypeName(componentTypeName)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateFormat")).thenReturn("yyyy/MM/dd");
    when(generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat")).thenReturn("yyyy/MM/dd HH:mm:ss");
    when(componentVolumeService.calculateVolume(component)).thenReturn(null);

    // Exercise SUT
    PackLabelTemplateObject actualResult = templateObjectFactory.createPackLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsPackLabelTemplateObject(expectedResult));
  }

  @Test
  public void testCreatePackLabelTemplateObjectWithHighBloodTitreButNotOBlood_shouldReturnAPackLabelTemplateObject() throws ParseException {
    // Set up fixture
    int flagCharPos = 123;
    int boxPos = 153;
    int checkCharPos = 162;
    String DIN = "12345";
    String bloodABO = "A";
    String bloodRh = "-";
    String donationDate = "2017/05/29";
    String donationDateISO = "2017-05-29";
    String componentCode = "1001";
    String componentTypeName = "Fresh Frozen Fish";
    String expiresOn = "2017/05/29 00:30:00";
    String expiresOnISO = "2017-05-29";

    Date expiresOnDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expiresOn);
    Date donationDateDate = new SimpleDateFormat("yyyy/MM/dd").parse(donationDate);

    Donation donation = aDonation()
        .withDonationIdentificationNumber(DIN)
        .withBloodAbo(bloodABO)
        .withBloodRh(bloodRh)
        .withDonationDate(donationDateDate)
        .withTitre(Titre.HIGH)
        .build();

    Component component = aComponent()
        .withComponentCode(componentCode)
        .withDonation(donation)
        .withExpiresOn(expiresOnDate)
        .withComponentType(aComponentType()
            .withComponentTypeName(componentTypeName)
            .thatContainsPlasma()
            .build())
        .build();

    PackLabelTemplateObject expectedResult = aPackLabelTemplateObject()
        .withFlagCharPos(flagCharPos)
        .withBoxPos(boxPos)
        .withCheckCharPos(checkCharPos)
        .withDIN(DIN)
        .withBloodABO(bloodABO)
        .withBloodRh(bloodRh)
        .thatIsNotBloodRhPositive()
        .thatIsBloodRhNegative()
        .thatIsNotBloodHighTitre()
        .withDonationDate(donationDate)
        .withDonationDateISO(donationDateISO)
        .withComponentCode(componentCode)
        .withExpiresOn(expiresOn)
        .withExpiresOnISO(expiresOnISO)
        .withComponentTypeName(componentTypeName)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName("dateFormat")).thenReturn("yyyy/MM/dd");
    when(generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat")).thenReturn("yyyy/MM/dd HH:mm:ss");
    when(componentVolumeService.calculateVolume(component)).thenReturn(null);

    // Exercise SUT
    PackLabelTemplateObject actualResult = templateObjectFactory.createPackLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsPackLabelTemplateObject(expectedResult));
  }
}
