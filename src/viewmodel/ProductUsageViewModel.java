package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import utils.CustomDateFormatter;
import model.usage.ProductUsage;

public class ProductUsageViewModel {

  private ProductUsage productUsage;

  public ProductUsageViewModel(ProductUsage productUsage) {
    this.productUsage = productUsage;
  }

  public Long getId() {
    return productUsage.getId();
  }

  @JsonIgnore
  public String getDonationIdentificationNumber() {
    return productUsage.getComponent().getDonationIdentificationNumber();
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
    return CustomDateFormatter.getDateTimeString(productUsage.getUsageDate());
  }

  public String getNotes() {
    return productUsage.getNotes();
  }

  public String getUseIndication() {
    return productUsage.getUseIndication();
  }
}
