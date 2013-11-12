package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.collectedsample.CollectedSample;
import model.collectedsample.LotReleaseConstant;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.BloodBagTypeRepository;
import repository.CollectedSampleRepository;
import repository.DonationTypeRepository;
import repository.GenericConfigRepository;
import repository.LocationRepository;
import backingform.validator.CollectedSampleBackingFormValidator;

@Controller
public class LotReleaseController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;

  @Autowired
  private DonationTypeRepository donorTypeRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private UtilController utilController;

  public LotReleaseController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CollectedSampleBackingFormValidator(binder.getValidator(),
                        utilController));
  }
  
  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  
  @RequestMapping(value = "/lotReleaseFormGenerator", method=RequestMethod.GET)
  public ModelAndView lotReleaseFormGenerator(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("lotRelease/lotReleaseForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }
  
  @RequestMapping(value = "/findlotRelease", method=RequestMethod.GET)
  public ModelAndView findlotRelease(HttpServletRequest request,HttpServletResponse response,
      @RequestParam(value="dinNumber") String dinNumber)  {
   ModelAndView mv = new ModelAndView("lotRelease/lotReleaseForm");
    boolean success = true;
    List<CollectedSample> collectedSample = collectedSampleRepository.findLotReleseById(dinNumber);
    success = checkCollectionNumber(mv, collectedSample);
    
    if(!success){
    	mv.addObject("errorMessage", "Collection Discarded. Cannot print pack label.");
    }
    
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("success", success);
    return mv;
  }

	private boolean checkCollectionNumber(ModelAndView mv,
			List<CollectedSample> collectedSample) {
		boolean success=false;
		if(collectedSample != null){
    	success=true;
    	if(collectedSample.get(0).getTTIStatus().equals(LotReleaseConstant.TTI_UNSAFE)){
    		success=false;
    	}else if(collectedSample.get(0).getDonor()!=null && collectedSample.get(0).getDonor().getDonorStatus().equals(LotReleaseConstant.POSITIVE_TTI)){
    		success=false;
    	}else if(collectedSample.get(0).getProducts()!=null && (collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_DISCARDED) || collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_EXPIRED)
    			|| collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_QUARANTINED) || collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_SPLIT))){   		
    		success=false;
    	}else if(collectedSample.get(0).getBloodTestResults()!=null && !collectedSample.get(0).getBloodTestResults().get(0).getBloodTest().getPositiveResults().equals(LotReleaseConstant.POSITIVE_BLOOD)){
    		success=false;
    	}else if(collectedSample.get(0).getDonor().getDeferrals()!=null && ! collectedSample.get(0).getDonor().getDeferrals().isEmpty()){
    		success=false;
    	}
    }else{
    	success=false;
    }
		return success;
	}
  
}
