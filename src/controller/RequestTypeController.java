package controller;

import model.requesttype.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.RequestTypeRepository;
import utils.PermissionConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("requesttypes")
public class RequestTypeController {

  @Autowired
  RequestTypeRepository requestTypesRepository;

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_REQUESTS + "')")
  public List<RequestType> getAllRequestTypes() {

    return requestTypesRepository.getAllRequestTypes();
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_REQUESTS + "')")
  public ResponseEntity getRequestTypeById(Integer id) {

    Map<String, Object> map = new HashMap<>();
    RequestType requestType = requestTypesRepository.getRequestTypeById(id);
    map.put("requestType", requestType);
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_REQUESTS + "')")
  public ResponseEntity saveRequestTypes(
          @RequestBody RequestType requestType) {

    requestTypesRepository.saveRequestType(requestType);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_REQUESTS + "')")
  public ResponseEntity updateRequestType(
          @RequestBody RequestType requestType, @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<>();
    requestType.setId(id);
    requestType = requestTypesRepository.updateRequestType(requestType);
    map.put("requestType", requestType);
    return new ResponseEntity(map, HttpStatus.OK);
  }

}
