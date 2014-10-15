

package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
}
