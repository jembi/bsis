package viewmodel;

import model.Product;

public class ProductViewModel {

	private Product product;

	public ProductViewModel() {
	  product = new Product();
	}
	
	public ProductViewModel(Product product) {
		this.product = product;
	}

  public void copy(Product product) {
    product.copy(product);
  }

  public boolean equals(Object obj) {
    return product.equals(obj);
  }

  public String getCollectionNumber() {
    return product.getCollectionNumber();
  }

  public String getComments() {
    return product.getComments();
  }

  public String getProductNumber() {
    return product.getProductNumber();
  }

  public String getType() {
    return product.getType();
  }

  public String getisIssued() {
    return product.getIssued() ? "yes" : "no";
  }

}
