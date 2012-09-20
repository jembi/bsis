package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.ProductUsage;

public class UsageViewModel {
  private ProductUsage usage;

  public UsageViewModel(ProductUsage usage) {
    this.usage = usage;
  }

  public Long getUsageId() {
    return usage.getUsageId();
  }

  public String getProductNumber() {
    return usage.getProductNumber();
  }

  public String getDateUsed() {
    Date dateUsed = usage.getDateUsed();
    if (dateUsed != null) {
      DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
      return formatter.format(dateUsed);
    }
    return "";
  }

  public String getUseIndication() {
    return usage.getUseIndication();
  }

  public String getHospital() {
    return usage.getHospital();
  }

  public String getWard() {
    return usage.getWard();
  }

  public String getComments() {
    return usage.getComments();
  }

}
