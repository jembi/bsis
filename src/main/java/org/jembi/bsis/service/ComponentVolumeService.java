package org.jembi.bsis.service;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.component.Component;
import org.springframework.stereotype.Service;

@Service
public class ComponentVolumeService {

  private static final Logger LOGGER = Logger.getLogger(ComponentVolumeService.class);
  
  /**
   * Calculate the volume of the specified Component using the weight and the gravity of the corresponding ComponentType
   *
   * @param component Component for which to calculate volume
   * @return Integer volume (rounded up), can return null if weight or gravity is not specified
   */
  public Integer calculateVolume(Component component) {
    String message = getWarningMessage(component);
    if (StringUtils.isNotBlank(message)) {
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Component with id '" + component.getId() + "' has the following properties not configured correctly: " + message);
      }
      return null;
    }
    return BigDecimal.valueOf((Double.valueOf(component.getWeight()) / component.getComponentType().getGravity()))
        .round(new MathContext(2, RoundingMode.HALF_UP)).intValue();
  }
  
  private String getWarningMessage(Component component) {
    String warningMessage = "";
    if (component.getWeight() == null) {
      warningMessage +="weight not set";
    }
    if (component.getComponentType().getGravity() == null) {
      String gravityWarningMessage = "gravity not set";
      warningMessage += (StringUtils.isBlank(warningMessage) ? gravityWarningMessage : " and " + gravityWarningMessage);
    }
    return warningMessage;
  }
}