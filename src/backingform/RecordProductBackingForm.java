package backingform;

import java.util.List;

public class RecordProductBackingForm {
	  private String searchBy;
	  private String productNumber;
	  private String collectionNumber;
	  private List<String> productTypes;
	  private List<String> status;
	  private String dateExpiresFrom;
	  private String dateExpiresTo;
	  private int noOfUnits;
	  private int collectedSampleID;
	  private long productID;
	  
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
		public int getNoOfUnits() {
			return noOfUnits;
		}
		public void setNoOfUnits(int noOfUnits) {
			this.noOfUnits = noOfUnits;
		}
		public int getCollectedSampleID() {
			return collectedSampleID;
		}
		public void setCollectedSampleID(int collectedSampleID) {
			this.collectedSampleID = collectedSampleID;
		}
		public long getProductID() {
			return productID;
		}
		public void setProductID(long productID) {
			this.productID = productID;
		}
		
		
	  
}
