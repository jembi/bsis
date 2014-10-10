 package controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public  Map<String, Object> getProductTypeSummary(@PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(id);
    map.put("productType", productType);
    return map;
  }

    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    @RequestMapping(method = RequestMethod.POST)
    public 
    HttpStatus saveNewProductType(
             @RequestBody Map<String, Object> productType) {
       
        productTypeRepository.saveNewProductTypeCombination(productType);
        return HttpStatus.NO_CONTENT;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    public HttpStatus updateProductType(
            @RequestBody Map<String, Object> productType) {
        productTypeRepository.saveNewProductTypeCombination(productType);
        return HttpStatus.NO_CONTENT;
    }

  @RequestMapping(value="{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  HttpStatus deactivateProductType(@PathVariable Integer id) {
   
    productTypeRepository.deactivateProductType(id);
    return HttpStatus.NO_CONTENT;
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  HttpStatus activateProductType(HttpServletRequest request,
      @PathVariable Integer id) {

    productTypeRepository.activateProductType(id);
    return HttpStatus.NO_CONTENT;
  }

  @RequestMapping(value="Combination/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> getProductTypeCombinationSummary(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(id);
    map.put("productTypeCombination", productTypeCombination);
    return map;
  }

    @RequestMapping(value = "Combination", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    HttpStatus saveNewProductTypeCombination(HttpServletResponse response,
            @RequestBody Map<String, Object> productType) {

        productTypeRepository.saveNewProductTypeCombination(productType);
        return HttpStatus.NO_CONTENT;
    }
  
  @RequestMapping(value="/Combination/{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public HttpStatus deactivateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

    productTypeRepository.deactivateProductTypeCombination(id);
    return HttpStatus.NO_CONTENT;
  }

  @RequestMapping(value="/Combination/{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  HttpStatus activateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

     productTypeRepository.activateProductTypeCombination(id);
     return HttpStatus.NO_CONTENT;
  }


}
