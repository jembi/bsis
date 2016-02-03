package validator;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import model.donation.Donation;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import repository.DonationRepository;
import backingform.DonationBackingForm;
import backingform.validator.AdverseEventBackingFormValidator;
import backingform.validator.DonationBackingFormValidator;

/**
 * Test using DBUnit to test the Donation BackingForm Validator
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonationBackingFormValidatorTest {

  @Autowired
  DonationRepository donationRepository;

  @Autowired
  private AdverseEventBackingFormValidator adverseEventBackingFormValidator;

  private BindException errors;

  @Autowired
  private DonationBackingFormValidator donationBackingFormValidator;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/DonationRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    return connection;
  }

  @Before
  public void init() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  @AfterTransaction
  public void after() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  @Test
  public void testAddDonationDonorAssessmentValuesWithinRange() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(1L);
    newDonation.setId(existingDonation.getId());
    newDonation.copy(existingDonation);
    newDonation.setId(null); // don't want to override, just save time with the copy
    newDonation.setDonationIdentificationNumber("JUNITDA123");
    Calendar today = Calendar.getInstance();
    newDonation.setCreatedDate(today.getTime());
    newDonation.setBleedEndTime(today.getTime());
    today.add(Calendar.MINUTE, -15);
    newDonation.setBleedStartTime(today.getTime());
    newDonation.setBloodPressureSystolic(100);
    newDonation.setBloodPressureDiastolic(80);
    newDonation.setDonorPulse(60);
    newDonation.setDonorWeight(new BigDecimal(80.0));
    newDonation.setHaemoglobinCount(new BigDecimal(15.0));
    DonationBackingForm donationBackingForm = new DonationBackingForm(newDonation);
    donationBackingForm.setDonorNumber(newDonation.getDonorNumber());

    errors = new BindException(donationBackingForm, "donationBackingForm");
    donationBackingFormValidator.validate(donationBackingForm, errors);
    Assert.assertEquals("Donor assessment values should all be within range", 0, errors.getErrorCount());
  }

  @Test
  public void testAddDonationDonorAssessmentValuesBelowRange() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(1L);
    newDonation.setId(existingDonation.getId());
    newDonation.copy(existingDonation);
    newDonation.setId(null); // don't want to override, just save time with the copy
    newDonation.setDonationIdentificationNumber("JUNITDA124");
    Calendar today = Calendar.getInstance();
    newDonation.setCreatedDate(today.getTime());
    newDonation.setBleedEndTime(today.getTime());
    today.add(Calendar.MINUTE, -15);
    newDonation.setBleedStartTime(today.getTime());
    newDonation.setBloodPressureSystolic(60);
    newDonation.setBloodPressureDiastolic(30);
    newDonation.setDonorPulse(20);
    newDonation.setDonorWeight(new BigDecimal(20.0));
    newDonation.setHaemoglobinCount(new BigDecimal(0.8));
    DonationBackingForm donationBackingForm = new DonationBackingForm(newDonation);
    donationBackingForm.setDonorNumber(newDonation.getDonorNumber());

    errors = new BindException(donationBackingForm, "donationBackingForm");
    donationBackingFormValidator.validate(donationBackingForm, errors);
    Assert.assertEquals("Donor assessment values should be included in the validation errors", 5, errors.getErrorCount());
    Assert.assertNotNull("donation.bloodPressureSystolic value should be below accepted range", errors.getFieldError("donation.bloodPressureSystolic"));
    Assert.assertNotNull("donation.bloodPressureDiastolic value should be below accepted range", errors.getFieldError("donation.bloodPressureDiastolic"));
    Assert.assertNotNull("donation.donorWeight value should be below accepted range", errors.getFieldError("donation.donorWeight"));
    Assert.assertNotNull("donation.donorPulse value should be below accepted range", errors.getFieldError("donation.donorPulse"));
    Assert.assertNotNull("donation.haemoglobinCount value should be below accepted range", errors.getFieldError("donation.haemoglobinCount"));
  }

  @Test
  public void testAddDonationDonorAssessmentValuesAboveRange() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(1L);
    newDonation.setId(existingDonation.getId());
    newDonation.copy(existingDonation);
    newDonation.setId(null); // don't want to override, just save time with the copy
    newDonation.setDonationIdentificationNumber("JUNITDA125");
    Calendar today = Calendar.getInstance();
    newDonation.setCreatedDate(today.getTime());
    newDonation.setBleedEndTime(today.getTime());
    today.add(Calendar.MINUTE, -15);
    newDonation.setBleedStartTime(today.getTime());
    newDonation.setBloodPressureSystolic(200);
    newDonation.setBloodPressureDiastolic(150);
    newDonation.setDonorPulse(250);
    newDonation.setDonorWeight(new BigDecimal(400.0));
    newDonation.setHaemoglobinCount(new BigDecimal(30.0));
    DonationBackingForm donationBackingForm = new DonationBackingForm(newDonation);
    donationBackingForm.setDonorNumber(newDonation.getDonorNumber());

    errors = new BindException(donationBackingForm, "donationBackingForm");
    donationBackingFormValidator.validate(donationBackingForm, errors);
    Assert.assertEquals("Donor assessment values should be included in the validation errors", 5, errors.getErrorCount());
    Assert.assertNotNull("donation.bloodPressureSystolic value should be above accepted range", errors.getFieldError("donation.bloodPressureSystolic"));
    Assert.assertNotNull("donation.bloodPressureDiastolic value should be above accepted range", errors.getFieldError("donation.bloodPressureDiastolic"));
    Assert.assertNotNull("donation.donorWeight value should be above accepted range", errors.getFieldError("donation.donorWeight"));
    Assert.assertNotNull("donation.donorPulse value should be above accepted range", errors.getFieldError("donation.donorPulse"));
    Assert.assertNotNull("donation.haemoglobinCount value should be above accepted range", errors.getFieldError("donation.haemoglobinCount"));
  }

  @Test
  public void testAddDonationUnknownDonor() throws Exception {
    DonationBackingForm donationBackingForm = new DonationBackingForm();
    donationBackingForm.setDonorNumber("TEST123");

    errors = new BindException(donationBackingForm, "donationBackingForm");
    donationBackingFormValidator.validate(donationBackingForm, errors);
    Assert.assertNotNull("donation.donor", errors.getFieldError("donation.donor"));
  }

  @Test
  public void testAddDonationUnknownDonationBatch() throws Exception {
    DonationBackingForm donationBackingForm = new DonationBackingForm();
    donationBackingForm.setDonationBatchNumber("TEST123");

    errors = new BindException(donationBackingForm, "donationBackingForm");
    donationBackingFormValidator.validate(donationBackingForm, errors);
    Assert.assertNotNull("donation.donationBatch", errors.getFieldError("donation.donationBatch"));
  }
}
