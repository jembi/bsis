package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.collectedsample.CollectedSample;
import model.collectedsample.LotReleaseConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    if(dinNumber.isEmpty()){
    	mv.addObject("errorMessage", "Please Enter the Donation Identification Number.");
    	success=false;
    	mv.addObject("success", success);
    	return mv;
    }
    
    if(collectedSample == null || collectedSample.size() == 0){
    	mv.addObject("errorMessage", "Donation Identification Number does not exist.");
    	success=false;
    	mv.addObject("success", success);
    	return mv;
    }
    
    success = checkCollectionNumber(mv, collectedSample);
    
    if(!success){
    	mv.addObject("errorMessage", "Collection Discarded. Cannot print pack label for DIN "+dinNumber);
    }
    
    mv.addObject("dinNumber", dinNumber);
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("success", success);
    return mv;
  }
  
  @RequestMapping(value = "/printLabel", method = RequestMethod.GET)
  public ModelAndView printLabel(HttpServletRequest request, Model model,
		  @RequestParam(value="dinNumber") String dinNumber) {
	  
	ModelAndView mv = new ModelAndView("zplLabel");
	
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(dinNumber);
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    try{
        
        String bloodABO = collectedSample.getBloodAbo();
        
        String bloodRh = "";
        if (collectedSample.getBloodRh().contains("+"))
        	bloodRh = "Positive";
        else if (collectedSample.getBloodRh().contains("-"))
        	bloodRh = "Negative";
        
        String collectionDate = df.format(collectedSample.getCollectedOn());        	

        // TODO: improve calculation of expiry date according to final processed components expiry dates 
        // i.e. expiryDate = df.format(collectedSample.getProducts().get(i).getExpiresOn());
        // For now, generates expiry date as Collection Date + 35 Days.
        String expiryDate = "";
    	Calendar c = Calendar.getInstance();
    	c.setTime(collectedSample.getCollectedOn());
    	c.add(Calendar.DATE,35);
    	expiryDate = df.format(c.getTime());

    // Generate ZPL label
    mv.addObject("labelZPL",
    		"^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD15^JUS^LRN^CI0^XZ" +
    		"^XA" +
    		"^MMT" +
    		"^PW799" +
    		"^LL0799" +
    		"^LS0" +
    		"^FT301,348^A0N,243,240^FH\\^FD" + bloodABO + "^FS" +
    		"^FT603,302^A0N,39,31^FH\\^FD" + bloodRh + "^FS" +
    		"^FT626,119^A0N,28,28^FH\\^FD" + expiryDate + "^FS" +
    		"^FT49,118^A0N,28,28^FH\\^FD" + collectionDate + "^FS" +
    		"^FT631,37^A0N,18,43^FH\\^FDExpiry ^FS" +
    		"^FT631,59^A0N,18,43^FH\\^FD  Date^FS" +
    		"^FT15,37^A0N,18,43^FH\\^FDCollection^FS" +
    		"^FT15,59^A0N,18,43^FH\\^FD    Date^FS" +
    		"^FT320,42^A0N,18,43^FH\\^FDPack ID^FS" +
    		"^FO575,11^GB0,150,8^FS" +
    		"^FO201,10^GB0,148,10^FS" +
    		"^FO16,439^GB747,0,6^FS" +
    		"^FO15,157^GB748,0,5^FS" +
    		"^FT19,254^A0N,16,19^FH\\^FDPrepared from 450ml of whole ^FS" +
    		"^FT19,274^A0N,16,19^FH\\^FDblood \\F1 10% in CPD anti-^FS" +
    		"^FT19,294^A0N,16,19^FH\\^FDcoagulant solution. Final ^FS" +
    		"^FT19,314^A0N,16,19^FH\\^FDhaematocrit is 0.4 \\F1 litre/litre. ^FS" +
    		"^FT19,334^A0N,16,19^FH\\^FDStore at 1\\F8C to 6\\F8C. ^FS" +
    		"^FT19,354^A0N,16,19^FH\\^FDTransport at 2\\F8C to 10\\F8 C      ^FS" +
    		"^FT19,374^A0N,16,19^FH\\^FD            Low Titre Donor^FS" +
    		"^FO15,379^GB750,0,6^FS" +
    		"^FT28,194^A0N,29,64^FH\\^FDWHOLE ^FS" +
    		"^FT28,230^A0N,29,64^FH\\^FDBLOOD^FS" +
    		"^FT98,410^A0N,18,19^FH\\^FDVolunteer donor blood collected and processed by the National Blood Service ^FS" +
    		"^FT98,432^A0N,18,19^FH\\^FD                      This product may transmit infectious agents^FS" +
    		"^FT599,237^A0N,39,31^FH\\^FD  Rh (D) ^FS" +
    		"^FT106,752^A0N,23,33^FH\\^FDPROPERLY IDENTIFY INTENDED RECIPIENT^FS" +
    		"^FT299,119^A@N,28,29,TT0003M_^FH\\^CI17^F8^FDAffix Pack ID^FS^CI0" +
    		"^FT244,606^A@N,28,31,TT0003M_^FH\\^CI17^F8^FDAffix compatibility label^FS^CI0" +
    		"^PQ1,0,1,Y^XZ"
    		);
    }
    catch (Exception e){
    	e.printStackTrace();
    }
    
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
    	}else if(collectedSample.get(0).getProducts()!=null && !collectedSample.get(0).getProducts().isEmpty() && (collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_DISCARDED) || collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_EXPIRED)
    			|| collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_QUARANTINED) || collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_SPLIT))){   		
    		success=false;
    	}else if(collectedSample.get(0).getBloodTestResults()!=null && !collectedSample.get(0).getBloodTestResults().isEmpty() && !collectedSample.get(0).getBloodTestResults().get(0).getBloodTest().getPositiveResults().equals(LotReleaseConstant.POSITIVE_BLOOD)){
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
