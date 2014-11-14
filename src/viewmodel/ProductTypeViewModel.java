

package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.producttype.ProductType;

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
        return productType.getProductType();
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
    
    public String getDescription(){
        return productType.getDescription();
    }
    
    public String getStorageInformation(){
        final String DEGREECELSIUS  = "\u2103";
        return "store at " + productType.getLowStorageTemperature() + DEGREECELSIUS+ " to "
                + productType.getHighStorageTemperature() + DEGREECELSIUS; 
                
    }
   
}
