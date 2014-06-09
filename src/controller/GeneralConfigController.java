/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import repository.GeneralConfigRepository;

/**
 *
 * @author srikanth
 */
@Controller
public class GeneralConfigController {
    
    @Autowired
    private GeneralConfigRepository configRepository;
    
    @Autowired
    private UtilController utilController;

    @RequestMapping(value = "/updateGeneralConfigProps", method = RequestMethod.POST)
    public ModelAndView updateGeneralConfig(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(null, this);
        //jsp page 
        return null;
    }
    
    
    @RequestMapping(value = "/viewGeneralConfig" , method = RequestMethod.GET)
    public ModelAndView viewConfiguration(){
        ModelAndView modelAndView = new ModelAndView("config/viewGeneralConfig");
        modelAndView.addObject("donorFields", utilController.getFormFieldsForForm("GeneralConfig"));
        modelAndView.addObject("config", configRepository.getAll());
        return modelAndView;
    }
    
}
