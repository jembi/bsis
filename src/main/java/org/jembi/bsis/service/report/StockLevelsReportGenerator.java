package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.StockLevelDTO;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockLevelsReportGenerator {

  @Autowired
  private InventoryRepository inventoryRepository;

  public Report generateStockLevelsForLocationReport(UUID locationId, InventoryStatus inventoryStatus) {
    Report report = new Report();

    List<StockLevelDTO> dtos = new ArrayList<>();

    if (locationId == null) {
      dtos = inventoryRepository.findStockLevels(inventoryStatus);
    } else {
      dtos = inventoryRepository.findStockLevelsForLocation(locationId, inventoryStatus);
    }

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (StockLevelDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setValue(dto.getCount());

      Cohort componentTypeCohort = new Cohort();
      componentTypeCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      componentTypeCohort.setComparator(Comparator.EQUALS);
      componentTypeCohort.setOption(dto.getComponentType().getComponentTypeName());
      dataValue.addCohort(componentTypeCohort);

      Cohort bloodTypeCohort = new Cohort();
      bloodTypeCohort.setCategory(CohortConstants.BLOOD_TYPE_CATEGORY);
      bloodTypeCohort.setComparator(Comparator.EQUALS);
      bloodTypeCohort.setOption(dto.getBloodAbo() + dto.getBloodRh());
      dataValue.addCohort(bloodTypeCohort);

      dataValues.add(dataValue);
    }

    report.setDataValues(dataValues);

    return report;
  }

}
