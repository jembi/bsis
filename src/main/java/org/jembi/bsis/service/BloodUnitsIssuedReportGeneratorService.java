package org.jembi.bsis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.OrderFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodUnitsIssuedReportGeneratorService {
  
  @Autowired
  private OrderFormRepository orderFormRepository;
  
  public Report generateUnitsIssuedReport(Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);
    
    List<DataValue> dataValues = new ArrayList<DataValue>();
    dataValues.addAll(getUnitsOrderedDataValues(startDate, endDate));
    dataValues.addAll(getUnitsIssuedDataValues(startDate, endDate));

    report.setDataValues(dataValues);
    return report;
  }

  private List<DataValue> getUnitsOrderedDataValues(Date startDate, Date endDate) {
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (BloodUnitsOrderDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setId("unitsOrdered");
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setValue(dto.getCount());

      Cohort bloodTestCohort = new Cohort();
      bloodTestCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      bloodTestCohort.setComparator(Comparator.EQUALS);
      bloodTestCohort.setOption(dto.getComponentType().getComponentTypeName());
      dataValue.addCohort(bloodTestCohort);
      dataValues.add(dataValue);
    }

    return dataValues;
  }

  private List<DataValue> getUnitsIssuedDataValues(Date startDate, Date endDate) {
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsIssued(startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (BloodUnitsOrderDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setId("unitsIssued");
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setValue(dto.getCount());

      Cohort bloodTestCohort = new Cohort();
      bloodTestCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      bloodTestCohort.setComparator(Comparator.EQUALS);
      bloodTestCohort.setOption(dto.getComponentType().getComponentTypeName());
      dataValue.addCohort(bloodTestCohort);
      dataValues.add(dataValue);
    }

    return dataValues;
  }

}
