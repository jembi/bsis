package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import model.ReportConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.ReportConfigRepository;
import utils.ControllerUtil;
import utils.LoggerUtil;

@Controller
public class ReportConfigController {

    @Autowired
    private ReportConfigRepository reportConfigRepository;

    @RequestMapping("/admin-reportConfigLandingPage")
    public ModelAndView getReportConfigLandingPage(HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        ModelAndView modelAndView = new ModelAndView("reportConfigLandingPage");
        Map<String, Object> model = new HashMap<String, Object>();


        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-collectionsReportFieldsConfig")
    public ModelAndView getCollectionReportConfig(HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        ModelAndView modelAndView = new ModelAndView("collectionsReportConfig");
        Map<String, Object> model = new HashMap<String, Object>();

        ControllerUtil.addCollectionReportConfigFieldsToModel(model, reportConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveCollectionsReportConfig")
    public ModelAndView saveCollectionReportConfig(@RequestParam Map<String, String> params,HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        saveConfig(params, "collection");

        ModelAndView modelAndView = new ModelAndView("collectionsReportConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addCollectionReportConfigFieldsToModel(model, reportConfigRepository);

        model.put("configSaved", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-productsReportFieldsConfig")
    public ModelAndView getProductReportConfig(HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        ModelAndView modelAndView = new ModelAndView("productsReportConfig");
        Map<String, Object> model = new HashMap<String, Object>();

        ControllerUtil.addProductReportConfigFieldsToModel(model, reportConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveProductsReportConfig")
    public ModelAndView saveProductReportConfig(@RequestParam Map<String, String> params,HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        saveConfig(params, "product");

        ModelAndView modelAndView = new ModelAndView("productsReportConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addProductReportConfigFieldsToModel(model, reportConfigRepository);


        model.put("configSaved", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-testResultsReportFieldsConfig")
    public ModelAndView getTestResultsReportConfig(HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        ModelAndView modelAndView = new ModelAndView("testResultsReportConfig");
        Map<String, Object> model = new HashMap<String, Object>();

        ControllerUtil.addTestResultsReportConfigFieldsToModel(model, reportConfigRepository);

        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-saveTestResultsReportConfig")
    public ModelAndView saveTestResultsReportConfig(@RequestParam Map<String, String> params,HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        saveConfig(params, "testResults");

        ModelAndView modelAndView = new ModelAndView("testResultsReportConfig");

        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addTestResultsReportConfigFieldsToModel(model, reportConfigRepository);


        model.put("configSaved", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    private void saveConfig(Map<String, String> params, String reportType) {
        Set<String> fields = params.keySet();
        String fieldNames = StringUtils.collectionToCommaDelimitedString(fields);
        if (reportConfigRepository.getReportConfig(reportType) == null) {
            reportConfigRepository.saveReportConfig(new ReportConfig(reportType, fieldNames));
        } else {
            reportConfigRepository.updateReportConfig(reportType, fieldNames);
        }
    }


}
