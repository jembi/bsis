package viewmodel;

import model.CustomDateFormatter;
import model.producttype.ProductType;
import model.usage.ProductUsage;

public class ProductUsageViewModel {

  private ProductUsage productUsage;

  public ProductUsageViewModel(ProductUsage productUsage) {
    this.productUsage = productUsage;
  }

  public Long getId() {
    return productUsage.getId();
  }

  public String getCollectionNumber() {
    return productUsage.getProduct().getCollectionNumber();
  }

  public ProductType getProductType() {
    return productUsage.getProductType();
  }

  public String getHospital() {
    return productUsage.getHospital();
  }

  public String getWard() {
    return productUsage.getWard();
  }

  public String getPatientName() {
    return productUsage.getPatientName();
  }

  public String getUsedBy() {
    return productUsage.getUsedBy();
  }

  public String getUsageDate() {
    return CustomDateFormatter.getDateString(productUsage.getUsageDate());
  }

  public String getNotes() {
    return productUsage.getNotes();
  }

  public String getUseIndication() {
    return productUsage.getUseIndication();
  }
}
