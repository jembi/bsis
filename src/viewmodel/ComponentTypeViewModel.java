package viewmodel;

import java.util.ArrayList;
import java.util.List;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentTypeViewModel {

    public ComponentTypeViewModel(ComponentType componentType) {
        this.componentType = componentType;
    }  
    
    @JsonIgnore
    private ComponentType componentType;

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }
    
    public Integer getId(){
        return componentType.getId();
    }
    
    public String getComponentTypeName(){
        return componentType.getComponentTypeName();
    }

    public String getComponentTypeNameShort(){
        return componentType.getComponentTypeNameShort();
    }
    
    public boolean getHasBloodGroup(){
        return componentType.getHasBloodGroup();
    }
    
    public Integer getExpiresAfter(){
        return componentType.getExpiresAfter();
    }
    
    public ComponentTypeTimeUnits getExpiresAfterUnits(){
        return componentType.getExpiresAfterUnits();
    }
    
    public String getDescription(){
        return componentType.getDescription();
    }

    public Integer getLowStorageTemperature(){
        return componentType.getLowStorageTemperature();
    }
    
    public Integer getHighStorageTemperature(){
        return componentType.getHighStorageTemperature();
    }
    
    public Integer getLowTransportTemperature(){
        return componentType.getLowTransportTemperature();
    }
    
    public Integer getHighTransportTemperature(){
        return componentType.getHighTransportTemperature();
    }
    
    public String getPreparationInfo(){
        return componentType.getPreparationInfo();
    }

    public List<ComponentTypeCombinationViewModel> getProducedComponentTypeCombinations(){
        return getComponentTypeCombinationViewModels(componentType.getProducedComponentTypeCombinations());
    }
    
    @JsonIgnore
    public List<ComponentTypeCombinationViewModel> 
        getComponentTypeCombinationViewModels(List<ComponentTypeCombination> componentTypeCombinations){
      
      List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels
              = new ArrayList<ComponentTypeCombinationViewModel> ();
      for(ComponentTypeCombination componentTypeCombination : componentTypeCombinations)
          componentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(componentTypeCombination));
          
      return componentTypeCombinationViewModels;
      
    }

   
}
