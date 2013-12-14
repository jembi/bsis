/**
 * 
 */
package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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

import repository.bloodtesting.BloodTestingRepository;
import controller.bloodtesting.TTIController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class TTIControllerTest {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockMvc mockMvc;

	// Use @Mock annotation to specify Mock objects

	// Controller that is being tested.
	@InjectMocks
	private TTIController ttiController;

	@Autowired
	BloodTestingRepository bloodTestingRepository;

	@SuppressWarnings("unchecked")
	@Test
	public void saveTTIResultsOnPlateTest() {
		String ttiResults = "{\"1\"" + ":{\"1\"" + ":{\"contents\""
				+ ":\"sample\"," + "\"welltype\"" + ":\"1\","
				+ "\"collectionNumber\"" + ":\"C1213000001\","
				+ "\"testResult\"" + ":\"-\"," + "\"machineReading\""
				+ ":\"-\"" + "}}}";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> ttiResultsMap;
		try {
			ttiResultsMap = mapper.readValue(ttiResults, HashMap.class);
			bloodTestingRepository.saveTTIResultsOnPlate(ttiResultsMap, 17L);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}