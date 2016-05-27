package service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.component.Component;
import model.util.BloodGroup;
import repository.InventoryRepository;

@Transactional
@Service
public class InventoryCRUDService {

  @Autowired
  private InventoryRepository inventoryRepository;

  public List<Component> findComponentsInStock(Long locationId, Long componentTypeId, Date dueToExpireBy, String bloodGroup) {
    BloodGroup bloodGroupObj = new BloodGroup(bloodGroup);
    return inventoryRepository.findComponentsInStock(locationId, componentTypeId, dueToExpireBy, bloodGroupObj.getBloodAbo(), bloodGroupObj.getBloodRh());
  }

}
