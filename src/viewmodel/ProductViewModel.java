package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import repository.CollectionRepository;

import model.Collection;
import model.Product;

public class ProductViewModel {

	private Product product;

	@Autowired
	CollectionRepository collectionRepository;
	
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

  public String getIsIssued() {
    if (product == null || product.getIssued() == null)
      return null;
    return product.getIssued() ? "yes" : "no";
  }

  public String getAbo() {
    return product.getAbo();
  }

  public String getRh() {
    return product.getRhd();
  }

  public String getBloodType() {
    if (product.getAbo() == null || product.getRhd() == null)
        return "";
    String rh = product.getRhd().equals("positive") ? "+" : "-"; 
    return product.getAbo() + rh;
  }

  public String getDateCollected() {
    Date dateCollected = product.getDateCollected();
    if (dateCollected == null)
      return "";
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(dateCollected);
  }
}
