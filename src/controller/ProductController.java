package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.DonationRepository;
import repository.ProductRepository;
import repository.ProductStatusChangeReasonRepository;
import repository.ProductTypeRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import viewmodel.ProductStatusChangeViewModel;
import viewmodel.ProductTypeCombinationViewModel;
import viewmodel.ProductTypeViewModel;
import viewmodel.ProductViewModel;
import backingform.ProductCombinationBackingForm;
import backingform.RecordProductBackingForm;
import backingform.validator.ProductBackingFormValidator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("components")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;
  
  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
  
  @Autowired
  private ProductTypeRepository productTypeRepository;
  
  @Autowired
  private UtilController utilController;
  
  public ProductController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new ProductBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  public static String getNextPageUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findProduct.html", "search.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public   Map<String, Object> productSummaryGenerator(HttpServletRequest request, 
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Product product = productRepository.findProductById(id);
     
    ProductViewModel productViewModel = getProductViewModels(Arrays.asList(product)).get(0);
    addEditSelectorOptions(map);
    map.put("product", productViewModel);
    map.put("productStatusChangeReasons",
    productStatusChangeReasonRepository.getAllProductStatusChangeReasonsAsMap());
    return map;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT_INFORMATION+"')")
  public  Map<String, Object> findProductFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    List<ProductStatusChangeReason> statusChangeReasons =
    productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.RETURNED);
    map.put("returnReasons", statusChangeReasons);
    statusChangeReasons =
    productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.DISCARDED);
    map.put("discardReasons", statusChangeReasons);
    map.put("findProductByPackNumberForm",  new RecordProductBackingForm());
    return map;
  }

  @RequestMapping(value = "/combination/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> addProductCombinationFormGenerator() {

    ProductCombinationBackingForm form = new ProductCombinationBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addProductCombinationForm", form);

    addOptionsForAddProductCombinationForm(map);
    addEditSelectorOptions(map);

    return map;
  }

  @RequestMapping(value = "/combination", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public  ResponseEntity< Map<String, Object>> addProductCombination(
      @Valid @RequestBody ProductCombinationBackingForm form) throws ParseException {

    Map<String, Object> map = new HashMap<String, Object>();
    HttpStatus httpStatus = HttpStatus.CREATED;
    addEditSelectorOptions(map);
   
      List<Product> savedProducts = null;
      savedProducts = productRepository.addProductCombination(form);
      map.put("hasErrors", false);
      form = new ProductCombinationBackingForm();
   
      // at least one product should be created, all products should have the same collection number
      map.put("collectionNumber", savedProducts.get(0).getCollectionNumber());
      map.put("createdProducts", getProductViewModels(savedProducts));
      List<Product> allProductsForDonation = productRepository.findProductsByCollectionNumber(savedProducts.get(0).getCollectionNumber());
      map.put("allProductsForDonation", getProductViewModels(allProductsForDonation));
      map.put("addAnotherProductUrl", "addProductCombinationFormGenerator.html");
   
    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  private ProductViewModel getProductViewModel(Product product) {
    return new ProductViewModel(product);
  }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
    public Map<String, Object> findProductPagination(HttpServletRequest request,
            @RequestParam(value = "componentNumber", required=false, defaultValue ="") String productNumber,
            @RequestParam(value = "donationIdentificationNumber", required=false, defaultValue ="") String collectionNumber,
            @RequestParam(value = "componentTypes", required=false, defaultValue ="") List<Integer> componentTypeIds,
            @RequestParam(value = "status", required=false, defaultValue ="") List<String> status,
            @RequestParam(value = "donationDateFrom", required=false, defaultValue ="") String donationDateFrom,
            @RequestParam(value = "donationDateTo", required=false, defaultValue ="") String donationDateTo) throws ParseException {

    	
    	Map<String, Object> map = new HashMap<String, Object>();    	
    	
        Map<String, Object> pagingParams = new HashMap<String, Object>();
        pagingParams.put("sortColumn", "id");
        //pagingParams.put("start", "0");
        //pagingParams.put("length", "10");
        pagingParams.put("sortDirection", "asc");

        List<Product> results = new ArrayList<Product>();
        Date dateFrom = null;
        Date dateTo = null;
        
        if(!donationDateFrom.equals("")){
	        	dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);
        }
        if(!donationDateTo.equals("")){
	        	dateTo = CustomDateFormatter.getDateFromString(donationDateTo);
        }
        
        results = productRepository.findAnyProduct(
                collectionNumber, componentTypeIds, statusStringToProductStatus(status),
                dateFrom, dateTo, pagingParams);

        List<ProductViewModel> components = new ArrayList<ProductViewModel>();
        
        if (results != null){
    	    for(Product product : results){
    	    	ProductViewModel productViewModel = getProductViewModel(product);
    	    	components.add(productViewModel);
    	    }
        }

        map.put("components", components);
        return map;
    }
    
	public static List<ProductStatusChangeViewModel> getProductStatusChangeViewModels(List<ProductStatusChange> productStatusChanges) {
	    if (productStatusChanges == null)
	      return Arrays.asList(new ProductStatusChangeViewModel[0]);
	    List<ProductStatusChangeViewModel> productStatusChangeViewModels = new ArrayList<ProductStatusChangeViewModel>();
	    for (ProductStatusChange productStatusChange : productStatusChanges) {
	    	productStatusChangeViewModels.add(new ProductStatusChangeViewModel(productStatusChange));
	    }
	    return productStatusChangeViewModels;
	}

  public static List<ProductViewModel> getProductViewModels(
      List<Product> products) {
    if (products == null)
      return Arrays.asList(new ProductViewModel[0]);
    List<ProductViewModel> productViewModels = new ArrayList<ProductViewModel>();
    for (Product product : products) {
      productViewModels.add(new ProductViewModel(product));
    }
    return productViewModels;
  }
  
  public static List<ProductTypeViewModel> getProductTypeViewModels(
      List<ProductType> productTypes) {
    if (productTypes == null)
      return Arrays.asList(new ProductTypeViewModel[0]);
    List<ProductTypeViewModel> productTypeViewModels = new ArrayList<ProductTypeViewModel>();
    for (ProductType productType : productTypes) {
    	productTypeViewModels.add(new ProductTypeViewModel(productType));
    }
    return productTypeViewModels;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('"+PermissionConstants.VOID_COMPONENT+"')")
  public HttpStatus deleteProduct(
      @PathVariable Long id) {
 
      productRepository.deleteProduct(id);
      return HttpStatus.NO_CONTENT;
  }

  @RequestMapping(value = "{id}/discard", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public  ResponseEntity discardProduct(
      @PathVariable Long id,
      @RequestParam(value="discardReasonId") Integer discardReasonId,
      @RequestParam(value="discardReasonText", required = false) String discardReasonText) {

      ProductStatusChangeReason statusChangeReason = new ProductStatusChangeReason();
      statusChangeReason.setId(discardReasonId);
      Donation donation = productRepository.findProductById(id).getDonation();
      productRepository.discardProduct(id, statusChangeReason, discardReasonText);
      
      Map<String, Object> map = new HashMap<String, Object>();
	  Map<String, Object> pagingParams = new HashMap<String, Object>();
	  pagingParams.put("sortColumn", "id");
	  pagingParams.put("sortDirection", "asc");
	  List<Product> results = new ArrayList<Product>();
	  List<ProductStatus> statusList = Arrays.asList(ProductStatus.values());
	
	  results = productRepository.findProductByCollectionNumber(
	      donation.getCollectionNumber(), statusList,
	      pagingParams);
	
	  List<ProductViewModel> components = new ArrayList<ProductViewModel>();
	
	  if (results != null){
	    for(Product product : results){
	    	ProductViewModel productViewModel = getProductViewModel(product);
	    	components.add(productViewModel);
	    }
	  }
	
	  map.put("components", components);
	
	  return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}/split", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public  ResponseEntity discardProduct(
      @PathVariable Long id,
      @RequestParam("numProductsAfterSplitting") Integer numProductsAfterSplitting) {

      boolean success = true;
      success = productRepository.splitProduct(id, numProductsAfterSplitting);
      if(!success)
          return new ResponseEntity(HttpStatus.BAD_GATEWAY);
      
      return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "{id}/history", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> viewProductHistory(
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Product product = productRepository.findProductById(id);
    ProductViewModel productViewModel = getProductViewModel(product);
    map.put("product", productViewModel);
    List<ProductStatusChange> productStatusChangeList = productRepository.getProductStatusChanges(product);
    List<ProductStatusChangeViewModel> productStatusChanges = getProductStatusChangeViewModels(productStatusChangeList);
    
    map.put("productStatusChanges", productStatusChanges);
    return map;
  }

  @RequestMapping(value = "{id}/return", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public  HttpStatus returnProduct(
      @PathVariable  Long id,
      @RequestParam("returnReasonId") Integer returnReasonId,
      @RequestParam("returnReasonText") String returnReasonText) {

      ProductStatusChangeReason statusChangeReason = new ProductStatusChangeReason();
      statusChangeReason.setId(returnReasonId);
      productRepository.returnProduct(id, statusChangeReason, returnReasonText);

      return HttpStatus.NO_CONTENT;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/donations/{donationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public Map<String, Object> findProductByPackNumberPagination(HttpServletRequest request, @PathVariable String donationNumber) {

	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  List<Product> products = Arrays.asList(new Product[0]);

      Map<String, Object> pagingParams = new HashMap<String, Object>();
      pagingParams.put("sortColumn", "id");
      //pagingParams.put("start", "0");
      //pagingParams.put("length", "10");
      pagingParams.put("sortDirection", "asc");
      
    //Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");

    List<Product> results = new ArrayList<Product>();
    List<ProductStatus> status = Arrays.asList(ProductStatus.values());
    
      results = productRepository.findProductByCollectionNumber(
          donationNumber, status,
          pagingParams);
    
    List<ProductViewModel> components = new ArrayList<ProductViewModel>();
    
    if (results != null){
	    for(Product product : results){
	    	ProductViewModel productViewModel = getProductViewModel(product);
	    	components.add(productViewModel);
	    }
    }

    map.put("components", components);
    return map;
  }
  
  @RequestMapping(value="/combinations", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public Map<String, Object> getProductTypeCombinations() {
	Map<String, Object> map = new HashMap<String, Object>();
    List<ProductTypeCombination> allProductTypeCombinationsIncludeDeleted = productTypeRepository.getAllProductTypeCombinationsIncludeDeleted();
    map.put("combinations",getProductTypeCombinationViewModels(allProductTypeCombinationsIncludeDeleted));
    return map;
  }
  
  @RequestMapping(value = "/recordcombinations", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public  ResponseEntity<Map<String, Object>> recordNewProductCombinations(
       @RequestBody @Valid RecordProductBackingForm form) throws ParseException{

      Product parentComponent = productRepository.findProductById(Long.valueOf(form.getParentComponentId()));
      Donation donation = parentComponent.getDonation();
      String collectionNumber = donation.getCollectionNumber();
      ProductStatus status = parentComponent.getStatus();
      long productId = Long.valueOf(form.getParentComponentId());
      
      
      // map of new components, storing product type and num. of units 
      Map<ProductType, Integer> newComponents = new HashMap<ProductType, Integer>();
      
      // iterate over components in combination, adding them to the new components map, along with the num. of units of each component
      for(ProductType pt : form.getProductTypeCombination().getProductTypes()){    	  
    	  boolean check = false;
    	  for(ProductType ptm : newComponents.keySet()){  
    		  if(pt.getId() == ptm.getId()){
    	    		Integer count = newComponents.get(ptm) + 1;
    	    		newComponents.put(ptm,count); 
    	    		check = true;
    	    		break;
    		  }
          }
    	  if (!check){
    		  newComponents.put(pt,1);
    	  }
      }
      
      for(ProductType pt : newComponents.keySet()){
    	        	      
	      String componentTypeCode = pt.getProductTypeNameShort();
	      int noOfUnits = newComponents.get(pt);
	      String createdPackNumber = collectionNumber +"-"+componentTypeCode;
	      
	      // Add New product
	      if(!status.equals(ProductStatus.PROCESSED) && !status.equals(ProductStatus.DISCARDED)){
		      	
	      	   for (int i = 1; i <= noOfUnits; i++) {
	              Product product = new Product();
	              product.setIsDeleted(false);
	              
	              // if there is more than one unit of the component, append unit number suffix
	              if(noOfUnits > 1){
	            	  product.setComponentIdentificationNumber(createdPackNumber + "-0" + i);
	              }
	              else {
	            	  product.setComponentIdentificationNumber(createdPackNumber);
	              }
		          product.setProductType(pt);
		          product.setDonation(donation);
		          product.setParentProduct(parentComponent);
		          product.setStatus(ProductStatus.QUARANTINED);
		          product.setCreatedOn(donation.getDonationDate());
		          
			      Calendar cal = Calendar.getInstance();
			      Date createdOn = cal.getTime(); 
			      cal.setTime(product.getCreatedOn());
	                  
	                      //set product expiry date
	                      if(pt.getExpiresAfterUnits() == ProductTypeTimeUnits.DAYS)
	                          cal.add(Calendar.DAY_OF_YEAR, pt.getExpiresAfter());
	                      else
	                      if(pt.getExpiresAfterUnits() == ProductTypeTimeUnits.HOURS)
	                          cal.add(Calendar.HOUR, pt.getExpiresAfter());
	                      else
	                      if(pt.getExpiresAfterUnits() == ProductTypeTimeUnits.YEARS)
	                           cal.add(Calendar.YEAR, pt.getExpiresAfter());
	
			      Date expiresOn = cal.getTime();    
			      product.setCreatedOn(createdOn);
			      product.setExpiresOn(expiresOn);
		          
			      productRepository.addProduct(product);
	
			      // Set source component status to PROCESSED
			      productRepository.setProductStatusToProcessed(productId);
	      	   }
	      }
      }
      
	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Object> pagingParams = new HashMap<String, Object>();
	pagingParams.put("sortColumn", "id");
	pagingParams.put("sortDirection", "asc");
	List<Product> results = new ArrayList<Product>();
	List<ProductStatus> statusList = Arrays.asList(ProductStatus.values());
	
	results = productRepository.findProductByCollectionNumber(
	      donation.getCollectionNumber(), statusList,
	      pagingParams);
	
	List<ProductViewModel> components = new ArrayList<ProductViewModel>();
	
	if (results != null){
	    for(Product product : results){
	    	ProductViewModel productViewModel = getProductViewModel(product);
	    	components.add(productViewModel);
	    }
	}
	
	map.put("components", components);
	
	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }
  
  @RequestMapping(value = "/record/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public  Map<String, Object> getRecordNewProductComponents(HttpServletRequest request,
      @RequestParam(value = "componentTypeNames") List<String> productTypes,
      @RequestParam(value = "donationIdentificationNumber") String donationIdentificationNumber) {

  	ProductType productType = null;
  	if(productTypes!= null){
	  	String productTypeName = productTypes.get(productTypes.size()-1);
	  	productType = productRepository.findProductTypeByProductTypeName(productTypeName);
  	}
  	List<Product> products = Arrays.asList(new Product[0]);
  	
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allProducts", getProductViewModels(products));
    map.put("nextPageUrl", getNextPageUrlForNewRecordProduct(request,donationIdentificationNumber));
    
    if(productTypes != null){
    	addEditSelectorOptionsForNewRecordByList(map,productType);
  	}
  	else{
  		 addEditSelectorOptionsForNewRecord(map);
  	}
    
    return map;
  }
  
   private void addOptionsForAddProductCombinationForm(Map<String, Object> m) {
    m.put("componentTypes", productTypeRepository.getAllProductTypes());

    List<ProductTypeCombination> productTypeCombinations = productTypeRepository.getAllProductTypeCombinations();
    m.put("componentTypeCombinations", productTypeCombinations);

    ObjectMapper mapper = new ObjectMapper();
    Map<Integer, String> productTypeCombinationsMap = new HashMap<Integer, String>();
    for (ProductTypeCombination productTypeCombination : productTypeCombinations) {
      Map<String, String> productExpiryIntervals = new HashMap<String, String>();
      for (ProductType productType : productTypeCombination.getProductTypes()) {
        Integer expiryIntervalMinutes = productType.getExpiryIntervalMinutes();
        productExpiryIntervals.put(productType.getId().toString(), expiryIntervalMinutes.toString());
      }

      try {
        productTypeCombinationsMap.put(productTypeCombination.getId(), mapper.writeValueAsString(productExpiryIntervals));
      } catch (JsonGenerationException e) {
        e.printStackTrace();
      } catch (JsonMappingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    m.put("componentTypeCombinationsMap", productTypeCombinationsMap);
  }
  
  public static String getNextPageUrlForRecordProduct(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findProductByPackNumber.html", "findProductByPackNumberPagination.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("componentTypes", getProductTypeViewModels(productTypeRepository.getAllProductTypes()));
  }
  
  private void addEditSelectorOptionsForNewRecordByList(Map<String, Object> m, ProductType productType) {
    m.put("componentTypes", getProductTypeViewModels(productTypeRepository.getProductTypeByIdList(productType.getId())));
  }
  private void addEditSelectorOptionsForNewRecord(Map<String, Object> m) {
    m.put("componentTypes", getProductTypeViewModels(productTypeRepository.getAllParentProductTypes()));
  }
  
  public static String getUrlForNewProduct(HttpServletRequest req,String qString) {
    String reqUrl = req.getRequestURL().toString();
    String queryString[] = qString.split("-");   
    if (queryString != null) {
        reqUrl += "?collectionNumber="+queryString[0];
    }
    return reqUrl;
  }
  
  public static String getNextPageUrlForNewRecordProduct(HttpServletRequest req,String qString) {
  	String reqUrl ="";
  	if(req.getRequestURI().contains("recordnewcomponents")){
  		reqUrl = req.getRequestURL().toString().replaceFirst("recordNewProductComponents.html", "findProductByPackNumberPagination.html");
  	}
  	else{
  		reqUrl = req.getRequestURL().toString().replaceFirst("getRecordNewProductComponents.html", "findProductByPackNumberPagination.html");
  	}
    String queryString[] = qString.split("-"); 
    if (queryString != null) {
        reqUrl += "?collectionNumber="+queryString[0];
    }
    return reqUrl;
  }
  
   /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in recordProductTable.jsp.
   */
  private   Map<String, Object> generateRecordProductTablesMap(List<Product> products, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> productsMap = new HashMap<String, Object>();
    ArrayList<Object> productList = new ArrayList<Object>();
    for (ProductViewModel product : getProductViewModels(products)) {
      List<Object> row = new ArrayList<Object>();
      
      row.add(product.getId().toString());
      row.add(product.getDonation().getId());
      for (String property : Arrays.asList("productType", "donationIdentificationNumber", "createdOn", "expiresOn", "status", "createdBy")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(product, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            if (property.equals("productType") &&
                StringUtils.isNotBlank(product.getSubdivisionCode())) {
              propertyValue = propertyValue + " (" + product.getSubdivisionCode() + ")";
            }
            row.add(propertyValue.toString());
          }
        }
      }

      productList.add(row);
    }
    productsMap.put("aaData", productList);
    productsMap.put("iTotalRecords", totalRecords);
    productsMap.put("iTotalDisplayRecords", totalRecords);
    return productsMap;
  }
  
  private List<ProductStatus> statusStringToProductStatus(List<String> statusList) {
    List<ProductStatus> productStatusList = new ArrayList<ProductStatus>();
    if (statusList != null) {
      for (String status : statusList) {
        productStatusList.add(ProductStatus.lookup(status));
      }
    }
    return productStatusList;
  }
  
  public  List<ProductTypeCombinationViewModel> 
	  getProductTypeCombinationViewModels(List<ProductTypeCombination> productTypeCombinations){
	List<ProductTypeCombinationViewModel> productTypeCombinationViewModels
	        = new ArrayList<ProductTypeCombinationViewModel> ();
	for(ProductTypeCombination productTypeCombination : productTypeCombinations)
	    productTypeCombinationViewModels.add(new ProductTypeCombinationViewModel(productTypeCombination));
	    
	return productTypeCombinationViewModels;
	
  }
  
}
