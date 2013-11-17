package model.bloodtesting;

import java.math.BigDecimal;

import java.util.Date;

public class TSVFileHeaderName {

	private String SID;
	
	private Integer assayNumber;

	private BigDecimal result;

	private String interpretation;

	private Date completed;

	private Integer operatorID;

	private String reagentLotNumber;

	/**
	 * @return the sID
	 */
	public String getSID() {
		return SID;
	}

	/**
	 * @param sID
	 *            the sID to set
	 */
	public void setSID(String sID) {
		SID = sID;
	}

	/**
	 * @return the assayNumber
	 */
	public Integer getAssayNumber() {
		return assayNumber;
	}

	/**
	 * @param assayNumber
	 *            the assayNumber to set
	 */
	public void setAssayNumber(Integer assayNumber) {
		this.assayNumber = assayNumber;
	}

	/**
	 * @return the result
	 */
	public BigDecimal getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(BigDecimal result) {
		this.result = result;
	}

	/**
	 * @return the interpretation
	 */
	public String getInterpretation() {
		return interpretation;
	}

	/**
	 * @param interpretation
	 *            the interpretation to set
	 */
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	/**
	 * @return the completed
	 */
	public Date getCompleted() {
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	/**
	 * @return the operatorID
	 */
	public Integer getOperatorID() {
		return operatorID;
	}

	/**
	 * @param operatorID
	 *            the operatorID to set
	 */
	public void setOperatorID(Integer operatorID) {
		this.operatorID = operatorID;
	}

	/**
	 * @return the reagentLotNumber
	 */
	public String getReagentLotNumber() {
		return reagentLotNumber;
	}

	/**
	 * @param reagentLotNumber
	 *            the reagentLotNumber to set
	 */
	public void setReagentLotNumber(String reagentLotNumber) {
		this.reagentLotNumber = reagentLotNumber;
	}

}
