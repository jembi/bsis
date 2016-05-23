/**
 *
 */
package controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityExistsException;

import model.component.Component;
import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import repository.ComponentRepository;
import repository.ComponentTypeRepository;
import repository.DonationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/bsis-servlet.xml")
@WebAppConfiguration
@Transactional
public class ComponentControllerTest {

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  // Test case for record new componentComponents
  @Ignore
  @Test
  public void recordNewComponentsTest() {

    ComponentType componentType2 = componentRepository.findComponentTypeBySelectedComponentType(1l);
    String donationIdentificationNumber = "D0001";
    String status = "QUARANTINED";
    long componentId = 1L;

    if (donationIdentificationNumber.contains("-")) {
      donationIdentificationNumber = donationIdentificationNumber.split("-")[0];
    }
    String componentTypeCode = componentType2.getComponentTypeCode();
    int noOfUnits = 3;
    long donationId = 1;

    // Add New component
    if (!status.equalsIgnoreCase("PROCESSED")) {
      if (noOfUnits > 0) {

        for (int i = 1; i <= noOfUnits; i++) {
          try {
            Component component = new Component();
            component.setIsDeleted(false);
            component.setComponentCode(componentTypeCode + "-" + i);
            Calendar c = new GregorianCalendar();
            System.out.println("after :" + componentTypeRepository.getComponentTypeById(1l).getExpiryIntervalMinutes());
            c.add(Calendar.MINUTE, componentTypeRepository.getComponentTypeById(1l).getExpiryIntervalMinutes());
            Date expiredate = c.getTime();


            component.setCreatedOn(new Date());
            component.setExpiresOn(expiredate);
            ComponentType componentType = new ComponentType();
            componentType.setComponentTypeName(componentType2.getComponentTypeName());
            componentType.setId(componentType2.getId());
            component.setComponentType(componentType);
            Donation donation = new Donation();
            donation.setId(donationId);
            component.setDonation(donation);
            component.setStatus(ComponentStatus.QUARANTINED);
            componentRepository.addComponent(component);

            // Once component save successfully update selected component status with processed
            componentRepository.setComponentStatusToProcessed(componentId);

          } catch (EntityExistsException ex) {
            ex.printStackTrace();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      } else {

        try {
          Component component = new Component();
          component.setIsDeleted(false);
          component.setComponentCode(componentTypeCode);

          Calendar c = new GregorianCalendar();
          System.out.println("after :" + componentTypeRepository.getComponentTypeById(1l).getExpiryIntervalMinutes());
          c.add(Calendar.MINUTE, componentTypeRepository.getComponentTypeById(1l).getExpiryIntervalMinutes());
          Date expiredate = c.getTime();

          component.setCreatedOn(new Date());
          component.setExpiresOn(expiredate);
          ComponentType componentType = new ComponentType();
          componentType.setComponentTypeName(componentType2.getComponentTypeName());
          componentType.setId(componentType2.getId());
          component.setComponentType(componentType);
          Donation donation = new Donation();
          donation.setId(donationId);
          component.setDonation(donation);
          component.setStatus(ComponentStatus.QUARANTINED);
          componentRepository.addComponent(component);
          componentRepository.setComponentStatusToProcessed(componentId);

        } catch (EntityExistsException ex) {
          ex.printStackTrace();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

}


