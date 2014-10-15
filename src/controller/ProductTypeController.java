 package controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.ProductTypeRepository;
import utils.PermissionConstants;

@RestController
@RequestMapping("componenttypes")
public class ProductTypeController {

  @Autowired
  private ProductTypeRepository productTypeRepository;
  
  public ProductTypeController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  
  @RequestMapping( method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> configureProductTypes() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productTypes", productTypeRepository.getAllProductTypesIncludeDeleted());
    return map;
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public  Map<String, Object> getProductTypeSummary(@PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(id);
    map.put("productType", productType);
    return map;
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity saveComponentTypeByID(@RequestBody ProductType componentType) {

      productTypeRepository.saveComponentType(componentType);
      return new ResponseEntity(HttpStatus.CREATED);
  }
  
  @RequestMapping(value="{id}", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity updatedComponentTypeByID(@RequestBody ProductType componentType,
  @PathVariable Integer id) {

      componentType.setId(id);
      productTypeRepository.updateComponentType(componentType);
      return new ResponseEntity(HttpStatus.CREATED);
  }

  @RequestMapping(value="{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity deactivateProductType(@PathVariable Integer id) {
   
    productTypeRepository.deactivateProductType(id);
     return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity activateProductType(HttpServletRequest request,
      @PathVariable Integer id) {

    productTypeRepository.activateProductType(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value="/combinations", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> configureProductTypeCombinations() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productTypeCombinations", productTypeRepository.getAllProductTypeCombinationsIncludeDeleted());
    map.put("productTypes", productTypeRepository.getAllProductTypes());
    return map;
  }
  @RequestMapping(value="Combinations/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> getProductTypeCombinationSummary(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(id);
    map.put("productTypeCombination", productTypeCombination);
    return map;
  }

    @RequestMapping(value = "combinations", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    ResponseEntity saveProductTypeCombination(HttpServletResponse response,
            @RequestBody ProductTypeCombination productTypeCombination) {

        productTypeRepository.saveComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "combinations/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    ResponseEntity updateProductTypeCombination(HttpServletResponse response,
            @RequestBody ProductTypeCombination productTypeCombination, @PathVariable Integer id) {
        productTypeCombination.setId(id);
        productTypeRepository.saveComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.CREATED);
    }
  
  @RequestMapping(value="/combinations/{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public ResponseEntity deactivateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

    productTypeRepository.deactivateProductTypeCombination(id);
   return new ResponseEntity(HttpStatus.CREATED);
  }

  @RequestMapping(value="/combinations/{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity activateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

     productTypeRepository.activateProductTypeCombination(id);
     return new ResponseEntity(HttpStatus.CREATED);
  }

}
