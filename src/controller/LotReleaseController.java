package controller;

import backingform.validator.CollectedSampleBackingFormValidator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.bloodtesting.TTIStatus;
import model.collectedsample.CollectedSample;
import model.collectedsample.LotReleaseConstant;
import model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.CollectedSampleRepository;
import repository.bloodtesting.BloodTypingStatus;
import utils.PermissionConstants;

@RestController
@RequestMapping("lotreleases")
public class LotReleaseController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;


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
  /*
  *Not Required to expose dummy method which returns nothing
  *
  @RequestMapping(value = "/lotReleaseFormGenerator", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  Map<String, Object> lotReleaseFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new  HashMap<String, Object>();
    return map;
  }
  */
  @RequestMapping(value = "/find/{dinNumber}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DISCARDS+"')")
  public ResponseEntity<Map<String, Object>> findlotRelease(HttpServletRequest request,
          @PathVariable String dinNumber)  {
    Map<String, Object> map = new  HashMap<String, Object>();
    boolean success = true;
    boolean discard = false;
    List<CollectedSample> collectedSample = collectedSampleRepository.findLotReleseById(dinNumber);
    if(dinNumber.isEmpty()){
    	map.put("errorMessage", "Please Enter the Donation Identification Number.");
    	success=false;
    	map.put("success", success);
    	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
    }
    
    if(collectedSample == null || collectedSample.size() == 0){
    	map.put("errorMessage", "Donation Identification Number does not exist.");
    	success=false;
    	map.put("success", success);
    	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
    }
   
    discard = checkCollectionForDiscard(map,collectedSample);

    if(!discard){
    
	    success = checkCollectionNumber(map, collectedSample);
	    
	    if(!success){
	    	map.put("errorMessage", "Cannot print pack label for DIN "+dinNumber);
	    }
    }
    else{
    	success=false;
    }
    
    map.put("dinNumber", dinNumber);
    map.put("success", success);
    map.put("discard", discard);
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/printLabel/{dinNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  ResponseEntity<Map<String, Object>> printLabel( @PathVariable String dinNumber) {
	  
	 Map<String, Object> map = new  HashMap<String, Object>();
	
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(dinNumber);
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
        String bloodABO = collectedSample.getBloodAbo();
        String inverse = "";
        String bloodRh = "";
        if (collectedSample.getBloodRh().contains("+")){
        	bloodRh = "Positive";
        }
        else if (collectedSample.getBloodRh().contains("-")){	
        	bloodRh = "Negative";
        	inverse = "^LRY^FO562,175^GB204,0,171^FS^LRN";
        }
        String collectionDate = df.format(collectedSample.getCollectedOn());        	

        // TODO: improve calculation of expiry date according to final processed components expiry dates 
        // i.e. expiryDate = df.format(collectedSample.getProducts().get(i).getExpiresOn());
        // For now, generates expiry date as Collection Date + 35 Days.
        String expiryDate = "";
    	Calendar c = Calendar.getInstance();
    	c.setTime(collectedSample.getCollectedOn());
    	c.add(Calendar.DATE,35);
    	expiryDate = df.format(c.getTime());

    	
    	    map.put("labelZPL",
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
    			         "^FT244,606^A@N,28,31,TT0003M_^FH\\^CI17^F8^FDAffix compatibility label^FS^CI0" +
    	    			 "^BY2,3,42^FT285,111^BCN,,Y,N"+
    	    			 "^FD>:" + dinNumber + "^FS"+
    	    			 inverse +
    			         "^PQ1,0,1,Y^XZ"
    			         );
    
    	
    	/*
    // Generate ZPL label
    map.addObject("labelZPL",		
    		"${^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ"+
    				"^XA"+
    				"^MMT"+
    				"^PW799"+
    				"^LL0799"+
    				"^LS0"+
    				"^FT308,356^A0N,226,240^FH\\^FD" + bloodABO + "^FS"+
    				"^FT603,302^A0N,39,31^FH\\^FD" + bloodRh + "^FS"+
    				"^FT626,119^A0N,28,28^FH\\^FD" + expiryDate + "^FS"+
    				"^FT49,118^A0N,28,28^FH\\^FD" + collectionDate + "^FS"+
    				"^FT631,37^A0N,18,43^FH\\^FDExpiry ^FS"+
    				"^FT631,59^A0N,18,43^FH\\^FD  Date^FS"+
    				"^FT15,37^A0N,18,43^FH\\^FDCollection^FS"+
    				"^FT15,59^A0N,18,43^FH\\^FD    Date^FS"+
    				"^FT237,46^A0N,18,43^FH\\^FDDonation Number^FS"+
    				"^FO575,11^GB0,150,8^FS"+
    				"^FO201,10^GB0,148,10^FS"+
    				"^FO16,439^GB747,0,6^FS"+
    				"^FO15,157^GB748,0,5^FS"+
    				"^FT19,254^A0N,16,19^FH\\^FDPrepared from 450ml of whole ^FS"+
    				"^FT19,274^A0N,16,19^FH\\^FDblood \\F1 10% in CPD anti-^FS"+
    				"^FT19,294^A0N,16,19^FH\\^FDcoagulant solution. Final ^FS"+
    				"^FT19,314^A0N,16,19^FH\\^FDhaematocrit is 0.4 \\F1 litre/litre. ^FS"+
    				"^FT19,334^A0N,16,19^FH\\^FDStore at 1\\F8C to 6\\F8C. ^FS"+
    				"^FT19,354^A0N,16,19^FH\\^FDTransport at 2\\F8C to 10\\F8 C      ^FS"+
    				"^FT19,374^A0N,16,19^FH\\^FD            Low Titre Donor^FS"+
    				"^FO15,379^GB750,0,6^FS"+
    				"^FT28,194^A0N,29,64^FH\\^FDWHOLE ^FS"+
    				"^FT28,230^A0N,29,64^FH\\^FDBLOOD^FS"+
    				"^FT98,410^A0N,18,19^FH\\^FDVolunteer donor blood collected and processed by the National Blood Service ^FS"+
    				"^FT98,432^A0N,18,19^FH\\^FD                      This product may transmit infectious agents^FS"+
    				"^FT599,237^A0N,39,31^FH\\^FD  Rh (D) ^FS"+
    				"^FT106,752^A0N,23,33^FH\\^FDPROPERLY IDENTIFY INTENDED RECIPIENT^FS"+
    				"^FT244,606^A@N,28,31,TT0003M_^FH\\^CI17^F8^FDAffix compatibility label^FS^CI0"+
    				"^BY2,3,42^FT285,111^BCN,,Y,N"+
    				"^FD>:" + dinNumber + "^FS"+
    				//inverse +
    				"^PQ1,0,1,Y^XZ}$"
    		);
    		*/
    
    
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/printDiscard/{dinNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  ResponseEntity<Map<String, Object>> printDiscard(@PathVariable String dinNumber) {
	  
	 Map<String, Object> map = new  HashMap<String, Object>();
	
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(dinNumber);
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
        
        String collectionDate = df.format(collectedSample.getCollectedOn());        	

    // Generate ZPL label
    map.put("labelZPL",	
    		"^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ"+
    		"~DG000.GRF,12800,040,"+
    		",:::::::gV0E0J01F,gT03FE0J01FF8,gS07FFE0J01FHF80,gR01FHFC0K07FFE0,gR0IF80M07FFE,gQ07FFC0O07FFC0,gQ0IFQ01FHF0,gP07FF80Q07FFC,gO03FFC0S0IF,gO07FFC0S07FFC0,gN01FFE0T01FHF0,gN07FF80U07FFC,gN0IFW03FFE,gM03FFC0W0IF80,gM07FFC0W07FF80,gL01FHFY01FFE0,gL03FFE0Y0IF8,gL07FFC0Y0IFC,gK01FHFgG03FFE,gK03FFE0g01FHF80,gK07FFE0gG0IFC0,gJ01FHF80gG07FFE0,gJ03FHFgI03FHF0,gJ03FHFgI01FHF8,gJ0IFE0gI0IFC,gI01FHFC0gI07FFE,gI01FHF80gI07FHF,gI07FHF80gI03FHF80,gI0IFE0gJ01FHFC0,gI0IFE0gJ01FHFE0,gH03FHFC0gK0JF0,gH03FHFC0gK07FHF8,gH07FHF80gK03FHF8,gH0JFgM03FHFE,gG01FIFgM01FIF,gG01FHFE0gL01FIF,gG03FHFC0gM0JF80,gG07FHFC0gM07FHFC0,gG0JFC0gM07FHFC0,gG0JF80gM07FHFE0,g01FIF80gM07FHFE0,g01FIF80gM03FIF0,g03FIFgO01FIF8,g07FIFgO01FIF8,g07FHFE0gN01FIFC,g0JFE0gO0JFC,g0JFE0gO0JFE,Y01FIFC0gO0KF,Y03FIFC0gO0KF,Y03FIFC0gO07FIF,Y03FIFC0gO07FIF80,Y07FIFC0gO03FIF80,Y07FIF80gO03FIFC0,Y07FIF80gO03FIFE0,Y0KF80gO03FIFE0,X01FJFgQ03FIFE0,X01FJFgQ03FJF0,:X03FJFgQ03FJF0,X07FJFgQ01FJF8,:X07FJFgQ01FJFC,:X0LFgQ01FJFC,:X0LFgQ01FJFE,::W01FKFgQ01FKF,::W03FKFgQ03FKF,::W03FKFT07FF80R03FKF80,W03FKFS0KFE0Q03FKF80,W03FKF80O07FMF80O03FKF80,W07FKF80N0QFC0N03FKF80,W07FKF80M01FQFO03FKF80,W07FKFC0L01FSFN07FKF80,W07FKFC0L0UFE0L07FKF80,W07FKFC0K01FUFM07FKF80,W07FKFC0K0WFE0K0MF80,W07FKFE0J07FWF80J0MFC0,W07FKFE0J0YFE0J0MFC0,W07FKFE0I03FYFJ01FLFC0,W07FKFE0I0gGFC0H01FLFC0,W07FLFI01FgFE0H01FLFC0,W07FLFI07FgGF8001FLFC0,W07FLF800FgHFE003FLFC0,W07FLF801FgIFH07FLFC0,W07FLFC07FgIF807FLFC0,W07FLFC07FgIFC07FLFC0,W07FLFC07FgIF80FMFC0,W07FLFE03FgIF81FMFC0,W07FMF03FgIF01FMFC0,W07FMF01FNF80I03FNF03FMFC0,W07FMF80FMFM01FLFC03FMF80,W07FMF80FLFC0M07FKFC03FMF80,W07FMFC07FJFC0O0LF80FNF80,W07FMFE03FIFE0P01FJF80FNF80,W07FMFE01FIFC0Q07FIF01FNF80,W03FNFH0IFE0R01FHFE01FNF80,W03FNF80FHF80S03FFC03FNF80,W03FNF807FF0T01FFC07FNF80,V01FOFC03FC0U07F00FOFE0,V0QFE01F0V03E00FPFC,U01FQFH0F0V01E01FPFE,U07FQF80gG03FQFC0,T03FRFC0gG07FRF0,T07FRFE0gG0TF8,S01FTFgG01FSFE,S07FTF80Y03FTFC0,S0VFC0Y0VFC0,R01FUFE0X01FVF0,R07FVF80W03FVFC,R0XFC0W07FVFC,Q03FWFE0W0YF,Q03FXFW03FXF80,Q0gFC0U07FXFE0,P01FgFU01FgF0,P03FgF80S03FgF8,P07FgFE0R01FgGFC,O01FgHFE0Q07FgHF,O03FgIFQ01FgIF,O07FgIFE0O0gKF80,O0gLFE0M0gLFE0,O0gMF80K07FgKFE0,N01FgNF8007FgNF0,N03FgOF03FgOF8,N07FgOF03FgOFC,N0gQF03FgOFE,M01FgPF03FgOFE,M01FgPF03FgPF,M07FgPF03FgPF80,M07FgPF03FgPFC0,M0gRF03FgPFC0,M0gRF03FgPFE0,L03FOFC0I01FTFC007FTFK0QF0,L03FNFE0K03FSFI01FSF80J01FOF0,L07FMFE0M01FQFC0I0SFO0OF8,L07FLFE0O03FPFK03FPFQ0NFC,L07FLF80P0QFK01FOFC0P07FLFE,K01FLFE0Q01FNFC0K07FNFS0MFE,K01FLFL03FFC0I03FMF80K07FMF80I0IFL01FLF,K01FKFE0K03FHF80H01FMF80K03FMFJ07FHFL01FLF,K03FKF80K03FIFC0H07FLFM01FLFC0H0KFM03FKF80,K03FKFM03FIFE0H03FLFM01FLF8001FJFM01FKF80,K07FJFC0L03FIFE0I0LFE0M0LFE0H01FJFN07FJFC0,K0LFN03FIFE0I03FJFC0M0LF80H01FJF80L01FJFC0,K0KFE0M03FIFE0I03FJFC0M0LFK0KF80L01FJFC0,K0KF80M03FIFE0J0KFC0M07FIFE0J0KF80M07FIFE0,J01FJFO03FIFE0J07FIFC0M07FIF80J0KF80M03FJF0,J01FJFO03FIFE0J03FIFC0M07FIFL0KF80M01FJF0,J03FIFC0N03FIFE0K0JF80M07FHFE0K0KF80N07FIF8,J03FIF80N03FIFE0K07FHF80M07FHFC0K0KF80N03FIF8,J07FIFP03FIFE0K07FHF80M07FHF80K0KF80N01FIF8,J07FHFE0O03FIFE0K01FHF80M07FHFM0KF80O0JFC,J07FHFC0O03FIFE0L0IF80M07FFE0L0KF80O07FHFC,J0JFC0O03FIFE0L0IFC0M07FFC0L0KF80O07FHFC,J0JFQ03FIFE0L07FFC0M07FFC0L0KF80O03FHFC,I01FHFE0P03FIFE0L03FFC0M0IFN0KF80O01FHFE,I01FHFE0P03FIFE0L01FFC0M0IFM01FJFR0IFE,I01FHFC0P03FIFE0L01FF0N01FE0L01FJFR07FHF,I01FHF80P03FIFE0M07C0O0FC0L01FJFR03FHF,I03FHF80P03FIFE0M0780O03C0L01FJFR03FHF,I03FHFR03FJFN020P0180L01FJFR01FHF,I03FFE0Q01FJFX040O01FJFS0IF80,I03FFE0Q01FJFQ0C0K060O01FJFS0IF80,I07FFC0Q01FJF80N03E0J01F80N03FJFS07FFC0,I07FF80Q01FJF80N0HF80I03FE0N03FJFS07FFC0,I07FF80Q01FJF80M01FFC0I07FF0N03FJFS07FFC0,I0IF80R0KF80M03FHFI01FHF80M07FIFE0R03FFC0,I0IFT0KF80M03FHFC007FHF80M07FIFE0R01FFC0,I0HFE0S0KFC0M03FOFO07FIFC0R01FFC0,I0HFE0S0KFC0M01FOFO07FIFC0S0HFC0,I0HFE0S0KFC0M01FOFO0KFC0S0HFE0,I0HFC0S07FIFE0M01FNFE0N0KFC0S0HFE0,I0HFC0S07FIFE0N0OFE0M01FJFC0S07FE0,I0HFC0S07FJFO0OFE0M01FJF80S07FE0,I0HF80S07FJFO07FMFC0M03FJF80S03FE0,H01FF0T03FJF80M07FMF80M03FJF80S03FE0,:H01FF0T01FJFC0M03FMF80M07FJFU01FE0,H01FF0T01FJFC0M03FMF80M0KFE0T01FE0,H01FF0T01FJFE0M03FMFO0KFE0T01FE0,H01FE0U0KFE0M03FMFN01FJFE0T01FE0,H01FE0U0LFN01FMFN01FJFC0T01FE0,H01FE0U0LF80L01FMFN03FJFC0U0FE0,H01FE0U07FJF80L01FMFN07FJFC0U0FE0,H01FE0U03FJFC0M0NFN07FJF80U0FE0,H01FE0U03FJFC0M0MFE0M0LF80U07E0,H01FC0U03FJFE0M0MFE0M0LFW07E0,H01FC0U01FKFN0MFE0L03FKFW07E0,H01FC0U01FKF80L0MFE0L03FJFE0V07E0,H01FC0V0LFC0L0MFC0L07FJFE0V07E0,H01F80V07FJFE0L0MFC0L0LFC0V07E0,H01F80V07FJFE0L0MFC0K01FKF80V07E0,I0F80V03FKFM0MFC0K03FKF80V07E0,I0F80V03FKF80K0MFC0K03FKFX07E0,I0F80V01FKFC0K0MFC0K0MFX07E0,I0F80W0LFE0K0MFC0J01FKFE0W07E0,I0F80W0MF80J0MFC0J03FKFC0W07E0,I0F80W07FKFC0J0MFC0J07FKFC0W07C0,I0F80W03FLFK0MFC0I01FLF80W07C0,I0F80W01FLFK0MFC0I03FLF80W07C0,I07C0W01FLFC0I0MFC0I07FKFE0X07C0,I07C0X0NFJ0MFE0H01FLFC0X07C0,I07C0X0NF80H0MFE0H03FLFC0X07C0,I03C0X03FLFE0H0MFE0H0NF80X0780,I03C0X01FMFC00FLFE007FMFg0780,I03C0X01FMFE00FLFE01FMFE0Y0780,I03E0Y07FMF81FMF03FMFC0Y0F,I03E0Y03FMF81FMF03FMF80Y0F,I01E0Y03FMF81FMF03FMFgG0F,I01E0g0NF03FMF03FLFE0Y01F,I01E0g07FLF03FMF01FLFC0Y01E,I01F0g03FLF03FMF01FLF80Y01E,J0F0g01FLF03FMF81FLFgG01C,J070gG07FJFE03FMF81FKFC0g03C,J070gG07FJFE07FMF81FKF80g03C,J0780g01FJFE07FMF80FKFgH038,J070gH07FIFC07FMFC07FIFC0gG018,J020gH07FIFC07FMFE07FIF80gH08,gN01FIF80FNFE07FHFE0,gO0JF81FNFE03FHFE0,gO03FHF81FOF03FHF,gP07FF01FOF01FFC,gP07FE03FOF01FF8,gQ0FE03FOF80FE0,gQ01C07FOFC0F,gR0C07FOFC06,gT0QFC,gS01FPFE,:gS01FQF80,gS07FQF80,:gS0SFC0,gR01FRFE0,gR01FSF0,gR03FSF8,gR07FSFC,N080gH0UFC,M03C0gG01FTFE0gH0F80,M01E0gG03FUF80g01F,M01F0gG07FUF80g01E,N0F80g0WFC0g07C,N03E0Y01FWFgG0F8,N03F0Y03FWFg01F0,N01F80X07FWFC0X03F0,N01F80X0YFE0X07E0,O07E0W03FYFX01FC0,O03F80V07FYFC0V07F80,O03FC0V0gGFE0V07F,P0HFV03FgGF80T01FE,P07FC0T0gIFE0T07FC,P03FE0S01FgIFU0HF8,P01FF80R07FgIFC0R03FE0,Q0IFR03FQFEFRFR01FFC0,Q03FF80P0SFCFRFC0P07FF80,Q01FHF80N07FRF03FRF80N03FHF,R07FHF80L07FRFC00FSFC0L03FHFC,R07FHFE0K01FSFC007FSFM0JF8,R01FJFE001FUFI01FUFI0KFE0,S07FgHFC0I0gJFC0,S03FgHF80I03FgHF,T0gHFE0J01FgGFE,T03FgF80K07FgF0,U0gGFM03FYFE0,U03FXFC0M07FXF80,V0XFE0N01FWFC,V03FVFC0O07FVF8,W07FTFE0P01FUFC0,X0TFE0R01FSFE,X07FRFC0S07FRF8,Y03FPFC0U07FPF80,g0QFW01FOFC,gG03FLFC0Y0NF80,gI01FHF80gI03FHF,,:::::::::::::::::::::::::::::::^XA"+
    		"^MMT"+
    		"^PW799"+
    		"^LL0799"+
    		"^LS0"+
    		"^FT32,704^XG000.GRF,1,1^FS"+
    		"^FO298,31^GB0,127,9^FS"+
    		"^FO15,157^GB748,0,5^FS"+
    		"^FT424,610^A0N,39,38^FH\\^FDDISPOSAL ONLY^FS"+
    		"^FT458,523^A0N,39,38^FH\\^FDBIOHAZARD^FS"+
    		"^FT454,440^A0N,39,38^FH\\^FDDO NOT USE^FS"+
    		"^FT67,65^A0N,28,28^FH\\^FDCollection Date^FS"+
    		"^FT414,63^A0N,28,28^FH\\^FDDonation Number^FS"+
    		"^FT151,307^A0N,135,134^FH\\^FDDISCARD^FS"+
    		"^FO18,357^GB748,0,8^FS"+
    		"^FT52,749^A0N,28,28^FH\\^FDIf found contact the BTS immediately at (000) 000-0000^FS" +
    		"^BY2,3,52^FT408,135^BCN,,Y,N" +
    		"^FD>:" + dinNumber + "^FS" +
    		"^FT88,118^A0N,28,28^FH\\^FD2013/01/01^FS" +
    		"^PQ1,0,1,Y^XZ^XA^ID000.GRF^FS^XZ" 
    		);
   
    
     return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }


	private boolean checkCollectionNumber(Map map,
			List<CollectedSample> collectedSample) {
		boolean success=false;
		if(collectedSample != null){
    	success=true;
    	if(collectedSample.get(0).getTTIStatus().equals(TTIStatus.TTI_UNSAFE)
    			|| collectedSample.get(0).getTTIStatus().equals(TTIStatus.NOT_DONE)
    			){
    		success=false;
    	}else if(collectedSample.get(0).getDonor()!=null && collectedSample.get(0).getDonor().getDonorStatus().equals(LotReleaseConstant.POSITIVE_TTI)){
    		success=false;
    	}else if(collectedSample.get(0).getProducts()!=null && !collectedSample.get(0).getProducts().isEmpty() && 
    			(collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_DISCARDED) 
    			|| collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_EXPIRED)
    			|| collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_QUARANTINED) 
    			|| collectedSample.get(0).getProducts().get(0).getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_SPLIT))){   		
    		success=false;
    	}else if(collectedSample.get(0).getBloodTestResults()!=null 
    			&& !collectedSample.get(0).getBloodTestResults().isEmpty() 
    			&& !collectedSample.get(0).getBloodTestResults().get(0).getBloodTest().getPositiveResults().equals(LotReleaseConstant.POSITIVE_BLOOD)){
    		success=false;
    	}
    	else if(collectedSample.get(0).getBloodTypingStatus().equals(BloodTypingStatus.NOT_DONE) 
    			|| collectedSample.get(0).getBloodTypingStatus().equals(BloodTypingStatus.AMBIGUOUS)
    			|| collectedSample.get(0).getBloodTypingStatus().equals(BloodTypingStatus.NOT_DONE)
    			|| collectedSample.get(0).getBloodTypingStatus().equals(BloodTypingStatus.PENDING_TESTS)
    			){
    		success = false;
    	}
    	// TODO: improve deferrals check - should only flag donors that are CURRENTLY deferred 
    	/*else if(collectedSample.get(0).getDonor().getDeferrals()!=null 
    			&& ! collectedSample.get(0).getDonor().getDeferrals().isEmpty()){
    		success=false;
    	}
    	*/
    	
    }else{
    	success=false;
    }
		return success;
	}
	
	private boolean checkCollectionForDiscard(Map map, List<CollectedSample> collectedSample){
		boolean discard = false;
		
		if(collectedSample.get(0).getTTIStatus().equals(TTIStatus.TTI_UNSAFE)){
    		discard=true;
    	}
		for(Product product : collectedSample.get(0).getProducts()){ 
			if (product.getStatus().toString().equals(LotReleaseConstant.COLLECTION_FLAG_DISCARDED)){
				discard = true;
			}
		}
		
		return discard;
	}
  
}
