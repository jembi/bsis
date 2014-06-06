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
    
    @RequestMapping("/configuregeneralProps")
    public ModelAndView generalConfigFormGenerator(){
        ModelAndView modelAndView = new ModelAndView("configureGeneralProps");
        modelAndView.addObject("generalCOnfigs", configRepository.getAll());
        return modelAndView;
    }
    
    @RequestMapping(value = "/updateGeneralConfigProps", method = RequestMethod.POST)
    public ModelAndView updateGeneralConfig(){
        return null;
    }
    
}
