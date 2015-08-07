package backingform;

import java.util.List;

import viewmodel.ProductTypeCombinationViewModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;

public class ComponentTypeBackingForm {
    
	@JsonIgnore
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
    
    public void setProductTypeName(String productTypeName){
        productType.setProductTypeName(productTypeName);
    }
    
    public void setProductTypeNameShort(String productTypeNameShort){
        productType.setProductTypeNameShort(productTypeNameShort);
    }
    
    public void setExpiresAfter(Integer expiresAfter){
        productType.setExpiresAfter(expiresAfter);
    }
    
    public void setExpiresAfterUnits(String productTypeTimeUnits){
        productType.setExpiresAfterUnits(ProductTypeTimeUnits.valueOf(productTypeTimeUnits));
    }
    
    public void setDescription(String description){
        productType.setDescription(description);
    }
    
    public void setHasBloodGroup(Boolean hasBloodGroup){
        productType.setHasBloodGroup(hasBloodGroup);
    }    
    
    public void setProductTypeCombinations(List<ProductTypeCombination> productTypeCombinations){
        productType.setProductTypeCombinations(productTypeCombinations);
    }
    
    public void setProducedProductTypeCombinations(List<ProductTypeCombination> producedProductTypeCombinations){
    	productType.setProducedProductTypeCombinations(producedProductTypeCombinations);
    }
    
    public void setHighStorageTemperature(Integer highStorageTemperature) {
        productType.setHighStorageTemperature(highStorageTemperature);
    }
    
    public void setLowStorageTemperature(Integer lowStorageTemperature) {
        productType.setLowStorageTemperature(lowStorageTemperature);
    }

    public void setLowTransportTemperature(Integer lowTransportTemperature) {
        productType.setLowTransportTemperature(lowTransportTemperature);
    }

    public void setHighTransportTemperature(Integer highTransportTemperature) {
        productType.setHighTransportTemperature(highTransportTemperature);
    }

    public void setPreparationInfo(String preparationInfo) {
        productType.setPreparationInfo(preparationInfo);
    }
        
}
