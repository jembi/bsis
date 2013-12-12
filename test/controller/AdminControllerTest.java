/**
 * 
 */
package controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import repository.ProductStatusChangeReasonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration

public class AdminControllerTest {
	
	private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockMvc mockMvc;
    
    // Use @Mock annotation to specify Mock objects

    //Controller that is being tested.
    @InjectMocks
    private AdminController adminController;
    
    @Autowired
    ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
    
	@Test
	public void exampleTest() {
    	assertEquals("10 x 5 must be 50", 50, 10*5);
	}
	
	@Test
	public void configureProductStatusChangeReasonsTest()
	{
		List<ProductStatusChangeReason> allProductStatusChangeReason = new ArrayList<ProductStatusChangeReason>();
		try {
		      String[] paramsStrArray = {"5~Expired Update~RETURNED~true","Final Update~Final Update~RETURNED~true"};
		      for(String paramsStr : paramsStrArray){
		      String splitArr[] = paramsStr.split("~");
		      ProductStatusChangeReason productStatusChangeReasonObj = new ProductStatusChangeReason();
		      try {
		        productStatusChangeReasonObj.setId(Integer.parseInt(splitArr[0]));
		       } catch (NumberFormatException ex) {
		          productStatusChangeReasonObj.setId(null);
		        }
		        productStatusChangeReasonObj.setStatusChangeReason((String)splitArr[1]);
		        productStatusChangeReasonObj.setCategory(ProductStatusChangeReasonCategory.valueOf(splitArr[2].toString()));
		        if(Boolean.valueOf(splitArr[3].toString()) == true)
		        	productStatusChangeReasonObj.setIsDeleted(false);
		        else
		        	productStatusChangeReasonObj.setIsDeleted(true);
		        allProductStatusChangeReason.add(productStatusChangeReasonObj);
		      }
		      
		      productStatusChangeReasonRepository.saveAllProductStatusChangeReason(allProductStatusChangeReason);
		     
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    }
	}
}
