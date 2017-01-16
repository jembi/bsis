package org.jembi.bsis.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.log4j.Logger;
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
    if (component.getWeight() == null || component.getComponentType().getGravity() == null) {
      logWarningMessage(component);
      return null;
    }
    return BigDecimal.valueOf((Double.valueOf(component.getWeight()) / component.getComponentType().getGravity()))
        .round(new MathContext(2, RoundingMode.HALF_UP)).intValue();
  }
  
  private void logWarningMessage(Component component) {
    if (component.getWeight() == null) {
      LOGGER.warn("The weight of component with id '" + component.getId() + "' is not set.");
    }
    if (component.getComponentType().getGravity() == null) {
      LOGGER.warn("The gravity of the component type with name '" + component.getComponentType().getComponentTypeName()
          + "' is not configured.");
    }
  }
}