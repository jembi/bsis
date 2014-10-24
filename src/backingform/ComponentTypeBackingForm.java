

package backingform;

import java.util.Set;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;

/**
 *
 * @author srikanth
 */
public class ComponentTypeBackingForm {
    
    private ProductType productType;
    
    public ComponentTypeBackingForm(){
        productType = new ProductType();
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
    
    public void setId(Integer id){
        productType.setId(id);
    }
    
    public void setproductTypeName(String productTypeName){
        productType.setProductType(productTypeName);
    }
    
    public void setproductTypeNameShort(String productTypeNameShort){
        productType.setProductTypeNameShort(productTypeNameShort);
    }
    
    public void setexpiresAfter(Integer expiresAfter){
        productType.setExpiresAfter(expiresAfter);
    }
    
    public void setdescription(String description){
        productType.setDescription(description);
    }
    
    public void sethasBloodGroup(Boolean hasBloodGroup){
        productType.setHasBloodGroup(hasBloodGroup);
    }
    
    public void setexpiresAfterUnits(ProductTypeTimeUnits productTypeTimeUnits){
        productType.setExpiresAfterUnits(productTypeTimeUnits);
    }
    
    public void setproductTypeCombinations(Set<ProductTypeCombination> productTypeCombinations){
        productType.setProductTypeCombinations(productTypeCombinations);
    }
}
