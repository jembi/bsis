package org.jembi.bsis.template.labelling;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateObjectFactory {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public DiscardLabelTemplateObject createDiscardLabelTemplateObject(Component component){
    DiscardLabelTemplateObject template = new DiscardLabelTemplateObject();

    template.component.componentCode = component.getComponentCode();
    String serviceInfoLine1 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1);
    String serviceInfoLine2 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2);

    template.config.serviceInfoLine1 = serviceInfoLine1;
    template.config.serviceInfoLine2 = serviceInfoLine2;

    return template;
  }

}
