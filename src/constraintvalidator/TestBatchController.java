package constraintvalidator;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.TestBatchRepository;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

    @Autowired
    private TestBatchRepository testBatchRepository;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addTestBatch() {
        
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findTestBatchPagination() {

        Map<String, Object> map = new HashMap<String, Object>();
        return new ResponseEntity(map, HttpStatus.OK);

    }

}
