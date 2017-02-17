package org.jembi.bsis.constraintvalidator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;

@org.springframework.stereotype.Component
public class ComponentStatusIsConsistentConstraintValidator implements ConstraintValidator<ComponentStatusIsConsistent, Component> {

  private static final Logger LOGGER = Logger.getLogger(ComponentStatusIsConsistentConstraintValidator.class);

  private static final List<ComponentStatus> IN_STOCK_COMPATIBLE_STATUSES = Arrays.asList(
      ComponentStatus.AVAILABLE,
      ComponentStatus.EXPIRED,
      ComponentStatus.UNSAFE);

  private static final List<ComponentStatus> NOT_IN_STOCK_COMPATIBLE_STATUSES = Arrays.asList(
      ComponentStatus.AVAILABLE,
      ComponentStatus.EXPIRED,
      ComponentStatus.UNSAFE,
      ComponentStatus.DISCARDED,
      ComponentStatus.PROCESSED,
      ComponentStatus.QUARANTINED);

  private static final List<ComponentStatus> REMOVED_COMPATIBLE_STATUSES = Arrays.asList(
      ComponentStatus.PROCESSED,
      ComponentStatus.ISSUED,
      ComponentStatus.DISCARDED,
      ComponentStatus.TRANSFUSED,
      ComponentStatus.AVAILABLE);

  @Override
  public void initialize(ComponentStatusIsConsistent constraint) {
  }

  @Override
  public boolean isValid(Component target, ConstraintValidatorContext context) {

    if (target == null) {
      return false;
    }

    if (target.getInventoryStatus().equals(InventoryStatus.IN_STOCK)
        && IN_STOCK_COMPATIBLE_STATUSES.contains(target.getStatus())) {
      return true;
    }

    if (target.getInventoryStatus().equals(InventoryStatus.NOT_IN_STOCK)
        && NOT_IN_STOCK_COMPATIBLE_STATUSES.contains(target.getStatus())) {
      return true;
    }

    if (target.getInventoryStatus().equals(InventoryStatus.REMOVED)
        && REMOVED_COMPATIBLE_STATUSES.contains(target.getStatus())) {
      return true;
    }

    LOGGER.warn("Component status '" + target.getStatus() + "' and inventory status '" + target.getInventoryStatus()
        + "' are not consistent for Component with id: " + target.getId());

    return false;
  }

}