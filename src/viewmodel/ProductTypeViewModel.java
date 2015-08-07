package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import viewmodel.ProductTypeCombinationViewModel;
import model.producttype.ProductTypeTimeUnits;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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

    public Integer getLowStorageTemperature(){
        return productType.getLowStorageTemperature();
    }
    
    public Integer getHighStorageTemperature(){
        return productType.getHighStorageTemperature();
    }
    
    public Integer getLowTransportTemperature(){
        return productType.getLowTransportTemperature();
    }
    
    public Integer getHighTransportTemperature(){
        return productType.getHighTransportTemperature();
    }
    
    public String getPreparationInfo(){
        return productType.getPreparationInfo();
    }

    public List<ProductTypeCombinationViewModel> getProducedProductTypeCombinations(){
        return getProductTypeCombinationViewModels(productType.getProducedProductTypeCombinations());
    }
    
    public List<ProductTypeCombinationViewModel> 
        getProductTypeCombinationViewModels(List<ProductTypeCombination> productTypeCombinations){
      
      List<ProductTypeCombinationViewModel> productTypeCombinationViewModels
              = new ArrayList<ProductTypeCombinationViewModel> ();
      for(ProductTypeCombination productTypeCombination : productTypeCombinations)
          productTypeCombinationViewModels.add(new ProductTypeCombinationViewModel(productTypeCombination));
          
      return productTypeCombinationViewModels;
      
    }

   
}
