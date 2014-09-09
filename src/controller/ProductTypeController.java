package controller;

import com.wordnik.swagger.annotations.Api;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.ProductTypeRepository;
import utils.PermissionConstants;

@Controller
@RequestMapping
@Api(value = "Account operations")
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

  public @ResponseBody Map<String, Object> getProductTypeSummary(HttpServletRequest request,
      @RequestParam(value="productTypeId") Integer productTypeId) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductType productType = productTypeRepository.getProductTypeById(productTypeId);
    map.put("productType", productType);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    @RequestMapping(value = "saveNewProductType", method = RequestMethod.POST)
    public @ResponseBody
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
    

    @RequestMapping(value = "updateProductType", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
    public @ResponseBody
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

  @RequestMapping(value="deactivateProductType", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public @ResponseBody Map<String, Object> deactivateProductType(HttpServletRequest request,
      @RequestParam(value="productTypeId") Integer productTypeId) {

    Map<String, Object> m = new HashMap<String, Object>();
    productTypeRepository.deactivateProductType(productTypeId);
    return m;
  }

  @RequestMapping(value="activateProductType", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public @ResponseBody Map<String, Object> activateProductType(HttpServletRequest request,
      @RequestParam(value="productTypeId") Integer productTypeId) {

    Map<String, Object> m = new HashMap<String, Object>();
    productTypeRepository.activateProductType(productTypeId);
    return m;
  }

  @RequestMapping(value="productTypeCombinationSummary", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public @ResponseBody Map<String, Object> getProductTypeCombinationSummary(HttpServletRequest request,
      @RequestParam(value="productTypeCombinationId") Integer productTypeCombinationId) {

    Map<String, Object> map = new HashMap<String, Object> ();
    ProductTypeCombination productTypeCombination = productTypeRepository.getProductTypeCombinationById(productTypeCombinationId);
    map.put("productTypeCombination", productTypeCombination);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

    @RequestMapping(value = "saveNewProductTypeCombination", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
    public @ResponseBody
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
  
  @RequestMapping(value="deactivateProductTypeCombination", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public @ResponseBody Map<String, Object> deactivateProductTypeCombination(HttpServletRequest request,
      @RequestParam(value="productTypeCombinationId") Integer productTypeCombinationId) {

    Map<String, Object> map = new HashMap<String, Object>();
    productTypeRepository.deactivateProductTypeCombination(productTypeCombinationId);
    return map;
  }

  @RequestMapping(value="activateProductTypeCombination", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public @ResponseBody Map<String, Object> activateProductTypeCombination(HttpServletRequest request,
      @RequestParam(value="productTypeCombinationId") Integer productTypeCombinationId) {

    Map<String, Object> map = new HashMap<String, Object>();
    productTypeRepository.activateProductTypeCombination(productTypeCombinationId);
    return map;
  }


}
