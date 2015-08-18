package backingform;

import javax.validation.Valid;

import model.componenttype.ComponentType;
import model.packtype.PackType;

public class PackTypeBackingForm {
	
	@Valid
    private PackType packType;

    public PackTypeBackingForm() {
    	packType = new PackType();
    }
    
    public PackType getPackType() {
        return packType;
    }

    public void setPackType(PackType packType) {
        this.packType = packType;
    }
    
    public void setId(Integer id){
        packType.setId(id);
    }
    
    public void setPackTypeName(String packTypeStr){
        packType.setPackType(packTypeStr);
    }
    
    public String getPackTypeName(){
        return packType.getPackType();
    }
    
    public void setComponentType(ComponentType componentType){
    	packType.setComponentType(componentType);
    }
    
    public void setCanPool(Boolean canPool){
        packType.setCanPool(canPool);
    }
    
    public void setCanSplit(Boolean canSplit){
        packType.setCanSplit(canSplit);
    }
    
    public void setIsDeleted(Boolean isDeleted){
        packType.setIsDeleted(isDeleted);
    }
    
    public void setCountAsDonation(Boolean countAsDonation){
        packType.setCountAsDonation(countAsDonation);
    }
    
    public void setPeriodBetweenDonations(Integer periodBetweenDonations){
        packType.setPeriodBetweenDonations(periodBetweenDonations);
    }

}
