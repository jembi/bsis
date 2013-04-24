package model.microtiterplate;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import model.bloodtesting.BloodTestResult;
import model.bloodtesting.WellType;

@Entity
public class MachineReading {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false)
  private Long id;

  @ManyToOne
  private WellType wellType;

  @Column(precision=7, scale=3)
  private BigDecimal machineReading;

  @OneToOne(mappedBy="machineReading")
  private BloodTestResult bloodTestResult;

  @Column(columnDefinition="SMALLINT")
  private Integer rowNumber;

  @Column(columnDefinition="SMALLINT")
  private Integer columnNumber;

  @ManyToOne
  private PlateSession plateSession;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public WellType getWellType() {
    return wellType;
  }

  public void setWellType(WellType wellType) {
    this.wellType = wellType;
  }

  public BigDecimal getMachineReading() {
    return machineReading;
  }

  public void setMachineReading(BigDecimal machineReading) {
    this.machineReading = machineReading;
  }

  public BloodTestResult getBloodTestResult() {
    return bloodTestResult;
  }

  public void setBloodTestResult(BloodTestResult bloodTestResult) {
    this.bloodTestResult = bloodTestResult;
  }

  public PlateSession getPlateSession() {
    return plateSession;
  }

  public void setPlateSession(PlateSession plateSession) {
    this.plateSession = plateSession;
  }

  public Integer getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(Integer rowNumber) {
    this.rowNumber = rowNumber;
  }

  public Integer getColumnNumber() {
    return columnNumber;
  }

  public void setColumnNumber(Integer columnNumber) {
    this.columnNumber = columnNumber;
  }
}
