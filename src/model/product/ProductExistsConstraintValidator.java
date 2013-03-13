package model.product;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.ProductRepository;

@Component
public class ProductExistsConstraintValidator implements
    ConstraintValidator<ProductExists, Product> {

  @Autowired
  private ProductRepository productRepository;

  public ProductExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(ProductExists constraint) {
  }

  public boolean isValid(Product target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {

      Product product = null;

      if (target.getId() != null) {
        product = productRepository.findProductById(target.getId());
      }
      if (product != null) {
        return true;
      }
    } catch (Exception e) {
       e.printStackTrace();
    }
    return false;
  }

  public void setProductRepository(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }
}