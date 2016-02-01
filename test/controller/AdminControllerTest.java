/**
 * 
 */
package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

public class AdminControllerTest {
	
	private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockMvc mockMvc;
    
    // Use @Mock annotation to specify Mock objects

    //Controller that is being tested.
    @InjectMocks
    private AdminController adminController;
    
	@Ignore @Test
	public void exampleTest() {
    	assertEquals("10 x 5 must be 50", 50, 10*5);
	}

}
