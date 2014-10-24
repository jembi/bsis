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
import viewmodel.ProductTypeCombinationViewModel;
import viewmodel.ProductTypeViewModel;

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
  public  List<ProductTypeViewModel> configureProductTypes() {

    List<ProductType> productTypes = productTypeRepository.getAllProductTypesIncludeDeleted();
    return getProductTypeViewModels(productTypes);
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public  Map<String, Object> getProductTypeSummary(@PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(id);
    map.put("productType", new ProductTypeViewModel(productType));
    return map;
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity saveComponentTypeByID(@Valid @RequestBody ComponentTypeBackingForm dataObject) {

      ProductType componentType = dataObject.getProductType();
      productTypeRepository.saveComponentType(componentType);
      return new ResponseEntity(HttpStatus.CREATED);
  }
  
  @RequestMapping(value="{id}", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity updatedComponentTypeByID(@Valid @RequestBody ComponentTypeBackingForm dataObject,
  @PathVariable Integer id) {

      Map<String, Object> map = new HashMap<String, Object>();
      ProductType componentType = dataObject.getProductType();
      componentType.setId(id);
      componentType = productTypeRepository.updateComponentType(componentType);
      map.put("componentType", componentType);
      return new ResponseEntity(map, HttpStatus.CREATED);
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
  public List<ProductTypeCombinationViewModel> configureProductTypeCombinations() {
    List<ProductTypeCombination> allProductTypeCombinationsIncludeDeleted = productTypeRepository.getAllProductTypeCombinationsIncludeDeleted();
    return getProductTypeCombinationViewModels(allProductTypeCombinationsIncludeDeleted);
  }
  
  @RequestMapping(value="Combinations/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> getProductTypeCombinationSummary(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(id);
    map.put("productTypeCombination", new ProductTypeCombinationViewModel(productTypeCombination));
    return map;
  }

    @RequestMapping(value = "combinations", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public
            ResponseEntity saveProductTypeCombination(@RequestBody ProductTypeCombinationBackingForm productTypeCombinationBackingForm) {
        ProductTypeCombination productTypeCombination
                = productTypeCombinationBackingForm.getProductTypeCombination();

        productTypeRepository.saveComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "combinations/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    ResponseEntity updateProductTypeCombination(HttpServletResponse response,
            @RequestBody ProductTypeCombinationBackingForm productTypeCombinationBackingForm
            , @PathVariable Integer id) {
       ProductTypeCombination productTypeCombination = 
              productTypeCombinationBackingForm.getProductTypeCombination();
        productTypeCombination.setId(id);
        productTypeRepository.saveComponentTypeCombination(productTypeCombination);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
  
  @RequestMapping(value="/combinations/{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public ResponseEntity deactivateProductTypeCombination(
      @PathVariable Integer id) {

    productTypeRepository.deactivateProductTypeCombination(id);
   return new ResponseEntity(HttpStatus.CREATED);
  }

  @RequestMapping(value="/combinations/{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  ResponseEntity activateProductTypeCombination(
      @PathVariable Integer id) {

     productTypeRepository.activateProductTypeCombination(id);
     return new ResponseEntity(HttpStatus.CREATED);
  }
  
  public  List<ProductTypeViewModel> getProductTypeViewModels(List<ProductType> productTypes){
      
      List<ProductTypeViewModel> productTypeViewModels = new ArrayList<ProductTypeViewModel> ();
      for(ProductType productType : productTypes)
          productTypeViewModels.add(new ProductTypeViewModel(productType));
          
      return productTypeViewModels;
      
  }
  
  
  public  List<ProductTypeCombinationViewModel> 
        getProductTypeCombinationViewModels(List<ProductTypeCombination> productTypeConminations){
      
      List<ProductTypeCombinationViewModel> productTypeCombinationViewModels
              = new ArrayList<ProductTypeCombinationViewModel> ();
      for(ProductTypeCombination productTypeCombination : productTypeConminations)
          productTypeCombinationViewModels.add(new ProductTypeCombinationViewModel(productTypeCombination));
          
      return productTypeCombinationViewModels;
      
  }

}
