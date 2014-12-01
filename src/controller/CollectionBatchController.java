package controller;

import backingform.CollectionBatchBackingForm;
import backingform.validator.CollectionBatchBackingFormValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import model.collectionbatch.CollectionBatch;
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
import repository.CollectionBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.CollectionBatchViewModel;

@RestController
@RequestMapping("/donationbatches")
public class CollectionBatchController {

  @Autowired
  private CollectionBatchRepository collectionBatchRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;

  public CollectionBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CollectionBatchBackingFormValidator(binder.getValidator(),
                        utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  
  /**
   * #209 - COmmented as not used anywhere 
   * @param request
   * @return 
   

  @RequestMapping(value = "/findform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public  Map<String, Object> findCollectionFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    return map;
  }
*/
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public  ResponseEntity findCollectionBatch(HttpServletRequest request,
          @RequestParam(value = "isClosed", required = false) Boolean isClosed,
          @RequestParam(value = "donorPanels", required = false) List<Long> donorPanels) {

	if(donorPanels == null){
		donorPanels = new ArrayList<Long>();
	}

    List<CollectionBatch> collectionBatches =
        collectionBatchRepository.findCollectionBatches(isClosed, donorPanels);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationBatches", getCollectionBatchViewModels(collectionBatches));

    return new ResponseEntity(map, HttpStatus.OK);
  }

  
  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')")
  public   Map<String, Object> addCollectionBatchFormGenerator(HttpServletRequest request) {

    CollectionBatchBackingForm form = new CollectionBatchBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addCollectionBatchForm", form);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionbatch");
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("collectionBatchFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')") 
  public  ResponseEntity addCollectionBatch(
      @RequestBody @Valid CollectionBatchBackingForm form) {
        CollectionBatch collectionBatch = form.getCollectionBatch();
        collectionBatch.setIsDeleted(false);
        collectionBatchRepository.addCollectionBatch(collectionBatch);
        return new ResponseEntity(new CollectionBatchViewModel(collectionBatch), HttpStatus.CREATED);
  }
  
  
  @RequestMapping(value = "{id}",method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONATION_BATCH+"')")
  public ResponseEntity updateCollectionBatch(@PathVariable Long id,
          @RequestBody @Valid CollectionBatchBackingForm form){
      
      CollectionBatch collectionBatch = collectionBatchRepository.updateCollectionBatch(form.getCollectionBatch());
      return new ResponseEntity(new CollectionBatchViewModel(collectionBatch), HttpStatus.OK);
  }
  

  @RequestMapping(value = "{id}" ,method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public  ResponseEntity<Map<String, Object>> collectionBatchSummaryGenerator(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object>();
    CollectionBatch  collectionBatch = collectionBatchRepository.findCollectionBatchById(id);
    CollectionBatchViewModel collectionBatchViewModel = getCollectionBatchViewModel(collectionBatch);
    map.put("collectionBatch", collectionBatchViewModel);


    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("donorPanels", locationRepository.getAllDonorPanels());
  }

  private CollectionBatchViewModel getCollectionBatchViewModel(CollectionBatch collectionBatch) {
    CollectionBatchViewModel collectionBatchViewModel = new CollectionBatchViewModel(collectionBatch);
    return collectionBatchViewModel;
  }

  public static List<CollectionBatchViewModel> getCollectionBatchViewModels(
      List<CollectionBatch> collectionBatches) {
    if (collectionBatches == null)
      return Arrays.asList(new CollectionBatchViewModel[0]);
    List<CollectionBatchViewModel> collectionBatchViewModels = new ArrayList<CollectionBatchViewModel>();
    for (CollectionBatch collectionBatch : collectionBatches) {
      collectionBatchViewModels.add(new CollectionBatchViewModel(collectionBatch));
    }
    return collectionBatchViewModels;
  }
  
}
