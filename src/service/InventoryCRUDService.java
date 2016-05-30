package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.component.Component;
import model.util.BloodGroup;
import repository.InventoryRepository;

@Transactional
@Service
public class InventoryCRUDService {

  @Autowired
  private InventoryRepository inventoryRepository;

  /**
   * Find components in stock. If donationIdentificationNumber or componentCode are present, both
   * need to be present, and only one component will be returned in the List. The rest of the
   * parameters will be ignored.
   * 
   * If they are not present, a search by the rest of the parameters will be executed.
   *
   * @param donationIdentificationNumber the donation identification number
   * @param componentCode the component code
   * @param locationId the location id
   * @param componentTypeId the component type id
   * @param dueToExpireBy the due to expire by
   * @param bloodGroup the blood group
   * @return the list
   */
  public List<Component> findComponentsInStock(String donationIdentificationNumber, String componentCode, Long locationId, Long componentTypeId, Date dueToExpireBy, String bloodGroup) {

    BloodGroup bloodGroupObj = null;
    
    // Check that if donationIdentificationNumber or componentCode are present, both are present
    boolean searchByCodeAndDIN = false;
    if (StringUtils.isNotEmpty(donationIdentificationNumber) || StringUtils.isNotEmpty(componentCode)) {
      if (StringUtils.isNotEmpty(donationIdentificationNumber) && StringUtils.isNotEmpty(componentCode)) {
        searchByCodeAndDIN = true;
      } else {
        throw new IllegalArgumentException("Both donationIdentificationNumber and componentCode are required if one of them is present.");
      }
    } else if (StringUtils.isNotEmpty(bloodGroup)) {
      // Check that bloodGroup is valid
      bloodGroupObj = new BloodGroup(bloodGroup);
      if (StringUtils.isEmpty(bloodGroupObj.getBloodAbo())) {
        throw new IllegalArgumentException("Invalid bloodGroup.");
      }
    }

    List<Component> components = new ArrayList<>();
    if (searchByCodeAndDIN) {
      Component component = inventoryRepository.findComponentByCodeAndDINInStock(componentCode, donationIdentificationNumber);
      components.add(component);
    } else {
      components = inventoryRepository.findComponentsInStock(locationId, componentTypeId, dueToExpireBy, bloodGroupObj.getBloodAbo(), bloodGroupObj.getBloodRh());
    }
    return components;
  }

}
