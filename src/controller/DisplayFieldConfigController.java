package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import model.RecordFieldsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.RecordFieldsConfigRepository;
import utils.ControllerUtil;

@Controller
public class DisplayFieldConfigController {

    @Autowired
    private RecordFieldsConfigRepository recordFieldsConfigRepository;

    public DisplayFieldConfigController() {
    }

    @RequestMapping("/admin-displayFieldsConfigLandingPage.html")
    public ModelAndView displayLandingPage(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("displayFieldsConfigLandingPage");

        Map<String, Object> model = new HashMap<String, Object>();

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-collectionsDisplayFieldsConfig.html")
    public ModelAndView display(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("collectionsDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("hasCollection", true);

        ControllerUtil.addFieldsToDisplay("collection", model, recordFieldsConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveCollectionsDisplayFieldsConfig")
    public ModelAndView saveReportConfig(@RequestParam Map<String, String> params,HttpServletRequest request) {

        Set<String> fields = params.keySet();
        String fieldNames = StringUtils.collectionToCommaDelimitedString(fields);
        if (recordFieldsConfigRepository.getRecordFieldsConfig("collection") == null) {
            recordFieldsConfigRepository.saveRecordFieldsConfig(new RecordFieldsConfig("collection", fieldNames));
        } else {
            recordFieldsConfigRepository.updateRecordFieldsConfig("collection", fieldNames);
        }

        ModelAndView modelAndView = new ModelAndView("collectionsDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addFieldsToDisplay("collection", model, recordFieldsConfigRepository);

        model.put("configSaved", true);
        model.put("hasCollectionDetails", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-donorsDisplayFieldsConfig.html")
    public ModelAndView displayDonorConfig(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("donorDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("hasNames", true);

        ControllerUtil.addFieldsToDisplay("donor", model, recordFieldsConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveDonorsDisplayFieldsConfig")
    public ModelAndView saveDonorConfig(@RequestParam Map<String, String> params,HttpServletRequest request) {

        Set<String> fields = params.keySet();
        String fieldNames = StringUtils.collectionToCommaDelimitedString(fields);
        if (recordFieldsConfigRepository.getRecordFieldsConfig("donor") == null) {
            recordFieldsConfigRepository.saveRecordFieldsConfig(new RecordFieldsConfig("donor", fieldNames));
        } else {
            recordFieldsConfigRepository.updateRecordFieldsConfig("donor", fieldNames);
        }

        ModelAndView modelAndView = new ModelAndView("donorDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addFieldsToDisplay("donor", model, recordFieldsConfigRepository);

        model.put("configSaved", true);
        model.put("hasDetails", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-requestsDisplayFieldsConfig.html")
    public ModelAndView displayRequestConfig(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("requestDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("hasNames", true);

        ControllerUtil.addFieldsToDisplay("request", model, recordFieldsConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveRequestsDisplayFieldsConfig")
    public ModelAndView saveRequestConfig(@RequestParam Map<String, String> params,HttpServletRequest request) {

        Set<String> fields = params.keySet();
        String fieldNames = StringUtils.collectionToCommaDelimitedString(fields);
        if (recordFieldsConfigRepository.getRecordFieldsConfig("request") == null) {
            recordFieldsConfigRepository.saveRecordFieldsConfig(new RecordFieldsConfig("request", fieldNames));
        } else {
            recordFieldsConfigRepository.updateRecordFieldsConfig("request", fieldNames);
        }

        ModelAndView modelAndView = new ModelAndView("requestDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addFieldsToDisplay("request", model, recordFieldsConfigRepository);

        model.put("configSaved", true);
        model.put("hasDetails", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }
    @RequestMapping("/admin-usageDisplayFieldsConfig.html")
    public ModelAndView displayUsageConfig(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("usageDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("hasNames", true);

        ControllerUtil.addFieldsToDisplay("usage", model, recordFieldsConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveUsageDisplayFieldsConfig")
    public ModelAndView saveUsageConfig(@RequestParam Map<String, String> params,HttpServletRequest request) {

        Set<String> fields = params.keySet();
        String fieldNames = StringUtils.collectionToCommaDelimitedString(fields);
        if (recordFieldsConfigRepository.getRecordFieldsConfig("usage") == null) {
            recordFieldsConfigRepository.saveRecordFieldsConfig(new RecordFieldsConfig("usage", fieldNames));
        } else {
            recordFieldsConfigRepository.updateRecordFieldsConfig("usage", fieldNames);
        }

        ModelAndView modelAndView = new ModelAndView("usageDisplayFieldsConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addFieldsToDisplay("usage", model, recordFieldsConfigRepository);

        model.put("configSaved", true);
        model.put("hasDetails", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }
}
