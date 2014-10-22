package viewmodel;

import model.collectedsample.CollectedSample;
import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.product.Product;

public class MatchingProductViewModel {

  private Product product;
  private ProductViewModel productViewModel;
  private CompatibilityTest compatibilityTest;

  public MatchingProductViewModel(Product product) {
    this.product = product;
    this.productViewModel = new ProductViewModel(this.product);
  }

  public MatchingProductViewModel(Product product,
      CompatibilityTest crossmatchTest) {
    this.product = product;
    this.productViewModel = new ProductViewModel(this.product);
    this.compatibilityTest = crossmatchTest;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Long getId() {
    return productViewModel.getId();
  }

  public CollectedSample getCollectedSample() {
    return productViewModel.getCollectedSample();
  }

  public ProductTypeViewModel getProductType() {
    return productViewModel.getProductType();
  }

  public String getLastUpdated() {
    return productViewModel.getLastUpdated();
  }

  public String getCreatedDate() {
    return productViewModel.getCreatedDate();
  }

  public String getCreatedBy() {
    return productViewModel.getCreatedBy();
  }

  public String getLastUpdatedBy() {
    return productViewModel.getLastUpdatedBy();
  }

  public String getNotes() {
    return productViewModel.getNotes();
  }

  public Boolean getIsDeleted() {
    return productViewModel.getIsDeleted();
  }

  public String getCreatedOn() {
    return productViewModel.getCreatedOn();
  }

  public String getExpiresOn() {
    return productViewModel.getExpiresOn();
  }

  public String getCollectionNumber() {
    return productViewModel.getCollectionNumber();
  }

  public String getAge() {
    return productViewModel.getAge();
  }

  public String getIsCompatible() {
    if (compatibilityTest == null)
      return CompatibilityResult.NOT_KNOWN.toString();
    return compatibilityTest.getCompatibilityResult().toString();
  }

  public String getBloodGroup() {
    return productViewModel.getBloodGroup();
  }

  public String getSubdivisionCode() {
    return productViewModel.getSubdivisionCode();
  }
}
