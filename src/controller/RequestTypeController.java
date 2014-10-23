/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.requesttype.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.RequestTypeRepository;
import utils.PermissionConstants;

/**
 *
 * @author srikanth
 */
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

        RequestType requestType = requestTypesRepository.getRequestTypeById(id);
        return new ResponseEntity(requestType, HttpStatus.OK);
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

        requestType.setId(id);
        requestTypesRepository.saveRequestType(requestType);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
