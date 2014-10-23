

package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import controller.ProductTypeController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;

/**
 *
 * @author srikanth
 */
public class ProductTypeCombinationViewModel {

    public ProductTypeCombinationViewModel(ProductTypeCombination productTypeCombination) {
        this.productTypeCombination = productTypeCombination;
    }
    
    @JsonIgnore
    private ProductTypeCombination productTypeCombination;

    public ProductTypeCombination getProductTypeCombination() {
        return productTypeCombination;
    }

    public void setProductTypeCombination(ProductTypeCombination productTypeCombination) {
        this.productTypeCombination = productTypeCombination;
    }
    
    public Integer getId(){
        return productTypeCombination.getId();
    }
    
    public String getCombinationName(){
        return productTypeCombination.getCombinationName();
    }
    
    public List<ProductTypeViewModel> getProductTypeViewModels(){
        return getProductTypeViewModels(productTypeCombination.getProductTypes());
    }

    private List<ProductTypeViewModel> getProductTypeViewModels(Set<ProductType> productTypes) {
      List<ProductTypeViewModel> productTypeViewModels = new ArrayList<ProductTypeViewModel> ();
      for(ProductType productType : productTypes)
          productTypeViewModels.add(new ProductTypeViewModel(productType));
      return productTypeViewModels;
    }
    
}
