package backingform;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import model.producttype.ProductType;

public class RecordProductBackingForm {
	
	@NotBlank
    private String parentComponentId;
	
	@NotNull
    private ProductType childComponentType;
	
	@NotNull
    private Integer numUnits;
	
	public String getParentComponentId() {
        return parentComponentId;
    }
	
	public void setParentComponentId(String parentComponentId){
		this.parentComponentId = parentComponentId;
	}
	
	public ProductType getChildComponentType() {
        return childComponentType;
    }
	
	public void setChildComponentType(ProductType childComponentType){
		this.childComponentType = childComponentType;
	}
	
	public Integer getNumUnits() {
        return numUnits;
    }
	
	public void setNumUnits(Integer numUnits){
		this.numUnits = numUnits;
	}
	
/*
    private String searchBy;

    @NotBlank
    private String productNumber;

    @NotBlank
    private String collectionNumber;

    @NotNull
    private List<String> productTypes;

    @NotNull
    private List<String> status;

    @NotBlank
    private String dateExpiresFrom;

    @NotBlank
    private String dateExpiresTo;

    @NotNull
    private Integer noOfUnits;

    @NotNull
    private Integer collectedSampleID;

    @NotNull
    private Long productID;

    public String getSearchBy() {
        return searchBy;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public List<String> getProductTypes() {
        return productTypes;
    }

    public String getDateExpiresFrom() {
        return dateExpiresFrom;
    }

    public String getDateExpiresTo() {
        return dateExpiresTo;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public void setProductTypes(List<String> productTypes) {
        this.productTypes = productTypes;
    }

    public void setDateExpiresFrom(String dateExpiresFrom) {
        this.dateExpiresFrom = dateExpiresFrom;
    }

    public void setDateExpiresTo(String dateExpiresTo) {
        this.dateExpiresTo = dateExpiresTo;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Integer getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Integer noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public Integer getCollectedSampleID() {
        return collectedSampleID;
    }

    public void setCollectedSampleID(Integer collectedSampleID) {
        this.collectedSampleID = collectedSampleID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }
*/
}
