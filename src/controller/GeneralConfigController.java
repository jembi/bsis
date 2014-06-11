/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import backingform.validator.GeneralConfigFormValidator;
import backingform.GeneralConfigBackingForm;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.admin.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new GeneralConfigFormValidator(binder.getValidator(),configRepository));
  }

    

   @RequestMapping(value = "/updateGeneralConfigProps", method = RequestMethod.POST)
    public ModelAndView updateGeneralConfig(@ModelAttribute("generalConfigForm") @Valid GeneralConfigBackingForm form,
            BindingResult result, HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
       
        if(result.hasErrors()){
             response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }else{
             configRepository.updateAll(form.getConfigs());
        }
        ModelAndView modelAndView = new ModelAndView("config/viewGeneralConfig");
        modelAndView.addObject("donorFields", utilController.getFormFieldsForForm("GeneralConfig"));
        modelAndView.addObject("config", form.getConfigs());
        model.addAttribute("generalConfigForm", form);
        return modelAndView;
    }
    
    
    @RequestMapping(value = "/viewGeneralConfig" , method = RequestMethod.GET)
    public ModelAndView viewConfiguration(Model model){
        ModelAndView modelAndView = new ModelAndView("config/viewGeneralConfig");
        modelAndView.addObject("donorFields", utilController.getFormFieldsForForm("GeneralConfig"));
        modelAndView.addObject("config", configRepository.getAll());
        model.addAttribute("generalConfigForm", new GeneralConfigBackingForm());
        return modelAndView;
    }
    
}
