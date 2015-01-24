

package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;
import java.util.Set;

/**
 *
 * @author srikanth
 */
public class ProductTypeViewModel {

    public ProductTypeViewModel(ProductType productType) {
        this.productType = productType;
    }
    
    
    @JsonIgnore
    private ProductType productType;

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
    
    public Integer getId(){
        return productType.getId();
    }
    
    public String getProductTypeName(){
        return productType.getProductTypeName();
    }

    public String getProductTypeNameShort(){
        return productType.getProductTypeNameShort();
    }
    
    public boolean getHasBloodGroup(){
        return productType.getHasBloodGroup();
    }
    
    public Integer getExpiresAfter(){
        return productType.getExpiresAfter();
    }
    
    public ProductTypeTimeUnits getExpiresAfterUnits(){
        return productType.getExpiresAfterUnits();
    }
    
    public String getDescription(){
        return productType.getDescription();
    }
    
    public Set<ProductTypeCombination> getProductTypeCombinations(){
    	return productType.getProductTypeCombinations();
    }
    
    public Set<ProductTypeCombination> getProducedProductTypeCombinations(){
    	return productType.getProducedProductTypeCombinations();
    }

   
}
