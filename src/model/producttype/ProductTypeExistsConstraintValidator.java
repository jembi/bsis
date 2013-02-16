package model.producttype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.ProductTypeRepository;

@Component
public class ProductTypeExistsConstraintValidator implements
    ConstraintValidator<ProductTypeExists, ProductType> {

  @Autowired
  private ProductTypeRepository productTypeRepository;

  public ProductTypeExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(ProductTypeExists constraint) {
  }

  public boolean isValid(ProductType target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {
     if (productTypeRepository.getProductTypeById(target.getId()) != null)
      return true;
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void setProductTypeRepository(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }
}