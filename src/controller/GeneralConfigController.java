/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import backingform.GeneralConfigBackingForm;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.admin.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

   @RequestMapping(value = "/updateGeneralConfigProps", method = RequestMethod.POST)
    public void updateGeneralConfig(@ModelAttribute("generalConfigForm") GeneralConfigBackingForm form,
            HttpServletRequest request,HttpServletResponse response) throws IOException{
       
        List<String> values = form.getValues();
        List<GeneralConfig> configs = new ArrayList<GeneralConfig>();
        int count=0;
        for(GeneralConfig generalConfig : configRepository.getAll()){
            generalConfig.setValue(values.get(count++));
            configs.add(generalConfig);
        }
       configRepository.updateAll(configs);
       response.sendRedirect("viewGeneralConfig.html");
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
