package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;

public class ProductTypeCombinationBackingForm {
    @JsonIgnore
    private ProductTypeCombination productTypeCombination;
    
    public ProductTypeCombinationBackingForm() {
        productTypeCombination = new ProductTypeCombination();
    }

    public ProductTypeCombination getProductTypeCombination() {
        return productTypeCombination;
    }

    public void setProductTypeCombination(ProductTypeCombination productTypeCombination) {
        this.productTypeCombination = productTypeCombination;
    }
     
    public Integer getId() {
        return productTypeCombination.getId();
    }
    
    public void setId(Integer id) {
        productTypeCombination.setId(id);
    }
    
    public List<ProductType> getProductTypes() {
        return productTypeCombination.getProductTypes();
    }
    
    public void setProductTypes(List<Integer> productTypeIds) {
        
        List<ProductType> productTypes = new ArrayList<ProductType>();
        ProductType productType = new ProductType();
        for (Integer productTypeId : productTypeIds) {
            productType.setId(productTypeId);
            productTypes.add(productType);
        }
        productTypeCombination.setProductTypes(productTypes);
    }
    
    public String getCombinationName() {
        return productTypeCombination.getCombinationName();
    }
    
    public void setCombinationName(String combinationName) {
        productTypeCombination.setCombinationName(combinationName);
    }
    
    public Boolean getIsDeleted() {
        return productTypeCombination.getIsDeleted();
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        productTypeCombination.setIsDeleted(isDeleted);
    }
    
}
