package controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.ProductTypeRepository;
import utils.PermissionConstants;

@RestController
@RequestMapping("producttype")
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

  public  Map<String, Object> getProductTypeSummary(HttpServletRequest request,
      @RequestParam(value="productTypeId") Integer productTypeId) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(productTypeId);
    map.put("productType", productType);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    @RequestMapping(method = RequestMethod.POST)
    public 
    Map<String, Object>  saveNewProductType(HttpServletResponse response,
             @RequestBody Map<String, Object> productType) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean success = false;
        productTypeRepository.saveNewProductTypeCombination(productType);
        success = true;
        if (!success) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        map.put("succcess", true);
        return map;
    }
    

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    public 
    Map<String, Object> updateProductType(HttpServletResponse response,
            @RequestBody Map<String, Object> productType) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean success = false;
        productTypeRepository.saveNewProductTypeCombination(productType);
        success = true;
        if (!success) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        map.put("succcess", true);
        return map;
    }

  @RequestMapping(value="{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> deactivateProductType(@PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    productTypeRepository.deactivateProductType(id);
    return m;
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> activateProductType(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    productTypeRepository.activateProductType(id);
    return m;
  }

  @RequestMapping(value="Combination/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> getProductTypeCombinationSummary(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(id);
    map.put("productTypeCombination", productTypeCombination);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

    @RequestMapping(value = "Combination", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public 
    Map<String, Object> saveNewProductTypeCombination(HttpServletResponse response,
            @RequestBody Map<String, Object> productType) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean success = false;
        productTypeRepository.saveNewProductTypeCombination(productType);
        success = true;
        if (!success) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        map.put("succcess", true);
        return map;
    }
  
  @RequestMapping(value="/Combination/{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public Map<String, Object> deactivateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object>();
    productTypeRepository.deactivateProductTypeCombination(id);
    return map;
  }

  @RequestMapping(value="/Combination/{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> activateProductTypeCombination(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object>();
    productTypeRepository.activateProductTypeCombination(id);
    return map;
  }


}
