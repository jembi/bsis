package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import factory.ComponentTypeFactory;
import factory.InventoryFactory;
import factory.LocationViewModelFactory;
import model.component.Component;
import model.componenttype.ComponentType;
import model.location.Location;
import repository.ComponentTypeRepository;
import repository.LocationRepository;
import service.InventoryCRUDService;
import utils.PermissionConstants;

@RestController
@RequestMapping("inventories")
public class InventoryController {

  @Autowired
  private InventoryCRUDService inventoryCRUDService;
  
  @Autowired
  private InventoryFactory inventoryFactory;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  
  @RequestMapping(method = RequestMethod.GET, value = "/search/form")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormForm() {
    List<ComponentType> componentTypes = componentTypeRepository.getAllComponentTypes();
    List<Location> distributionSites = locationRepository.getDistributionSites();
    Map<String, Object> map = new HashMap<>();
    map.put("distributionSites", locationViewModelFactory.createLocationViewModels(distributionSites));
    map.put("componentTypes", componentTypeFactory.createViewModels(componentTypes));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public ResponseEntity<Map<String, Object>> findComponentBatches(
      @RequestParam(value = "donationIdentificationNumber", required = false) String donationIdentificationNumber,
      @RequestParam(value = "componentCode", required = false) String componentCode,
      @RequestParam(value = "locationId", required = false) Long locationId,
      @RequestParam(value = "componentTypeId", required = false) Long componentTypeId,
      @RequestParam(value = "dueToExpireBy", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dueToExpireBy,
      @RequestParam(value = "bloodGroup", required = false) String bloodGroup) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<Component> components =
        inventoryCRUDService.findComponentsInStock(donationIdentificationNumber, componentCode, locationId, componentTypeId, dueToExpireBy, bloodGroup);
    map.put("inventories", inventoryFactory.createInventoryViewModels(components));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

  }

}
