 package controller;

import backingform.ComponentTypeBackingForm;
import backingform.ProductTypeCombinationBackingForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import backingform.validator.ComponentTypeBackingFormValidator;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.ProductTypeRepository;
import utils.PermissionConstants;
import viewmodel.ProductTypeCombinationViewModel;
import viewmodel.ProductTypeViewModel;

@RestController
@RequestMapping("componenttypes")
public class ProductTypeController {

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private UtilController utilController;
  
  public ProductTypeController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new ComponentTypeBackingFormValidator(binder.getValidator(), utilController, productTypeRepository));
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
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public  ResponseEntity<Map<String, Object>>  getComponentTypes() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<ProductType> productTypes = productTypeRepository.getAllProductTypesIncludeDeleted();
    map.put("componentTypes", getProductTypeViewModels(productTypes));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public ResponseEntity<Map<String, Object>> getComponentTypeById(@PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(id);
    map.put("productType", new ProductTypeViewModel(productType));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public  ResponseEntity saveComponentType(@Valid @RequestBody ComponentTypeBackingForm form) {
      
	  ProductType componentType = productTypeRepository.saveComponentType(form.getProductType());
      return new ResponseEntity(new ProductTypeViewModel(componentType), HttpStatus.CREATED);
  }
  
  @RequestMapping(value="{id}", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public  ResponseEntity updateComponentType(@Valid @RequestBody ComponentTypeBackingForm form,
  @PathVariable Integer id) {

      ProductType componentType = form.getProductType();
      componentType.setId(id);
      componentType = productTypeRepository.updateComponentType(componentType);
      return new ResponseEntity(new ProductTypeViewModel(componentType), HttpStatus.OK);
  }

  @RequestMapping(value="{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public  ResponseEntity deactivateComponentType(@PathVariable Integer id) {
   
    productTypeRepository.deactivateProductType(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_TYPES+"')")
  public  ResponseEntity activateComponentType(HttpServletRequest request,
      @PathVariable Integer id) {

    productTypeRepository.activateProductType(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value="/combinations", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public ResponseEntity<Map<String, Object>> getComponentTypeCombinations() {
    List<ProductTypeCombination> allProductTypeCombinationsIncludeDeleted = productTypeRepository.getAllProductTypeCombinationsIncludeDeleted();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("combinations",  getProductTypeCombinationViewModels(allProductTypeCombinationsIncludeDeleted));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value="/combinations/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> getComponentTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(id);
    map.put("productTypeCombination", new ProductTypeCombinationViewModel(productTypeCombination));
    return map;
  }

    @RequestMapping(value = "/combinations", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public ResponseEntity saveComponentTypeCombination(@RequestBody ProductTypeCombinationBackingForm productTypeCombinationBackingForm) {
        ProductTypeCombination productTypeCombination
                = productTypeCombinationBackingForm.getProductTypeCombination();

        productTypeRepository.saveComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/combinations/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    ResponseEntity updateComponentTypeCombination(HttpServletResponse response,
            @RequestBody ProductTypeCombinationBackingForm productTypeCombinationBackingForm
            , @PathVariable Integer id) {
       ProductTypeCombination productTypeCombination = 
              productTypeCombinationBackingForm.getProductTypeCombination();
        productTypeCombination.setId(id);
        productTypeRepository.updateComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.OK);
    }
  
  @RequestMapping(value="/combinations/{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public ResponseEntity deactivateComponentTypeCombination(
      @PathVariable Integer id) {

    productTypeRepository.deactivateProductTypeCombination(id);
   return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value="/combinations/{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity activateComponentTypeCombination(
      @PathVariable Integer id) {

     productTypeRepository.activateProductTypeCombination(id);
     return new ResponseEntity(HttpStatus.OK);
  }
  
  private List<ProductTypeViewModel> getProductTypeViewModels(List<ProductType> productTypes){
      
      List<ProductTypeViewModel> productTypeViewModels = new ArrayList<ProductTypeViewModel> ();
      for(ProductType productType : productTypes)
          productTypeViewModels.add(new ProductTypeViewModel(productType));
          
      return productTypeViewModels;
      
  }
  
  private List<ProductTypeCombinationViewModel> 
        getProductTypeCombinationViewModels(List<ProductTypeCombination> productTypeCombinations){
      
      List<ProductTypeCombinationViewModel> productTypeCombinationViewModels
              = new ArrayList<ProductTypeCombinationViewModel> ();
      for(ProductTypeCombination productTypeCombination : productTypeCombinations)
          productTypeCombinationViewModels.add(new ProductTypeCombinationViewModel(productTypeCombination));
          
      return productTypeCombinationViewModels;
      
  }

}
