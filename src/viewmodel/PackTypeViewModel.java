package viewmodel;

import model.bloodbagtype.BloodBagType;

public class PackTypeViewModel {
    
    private BloodBagType packType;

    public PackTypeViewModel(BloodBagType packType) {
        this.packType = packType;
    }
    
    public Integer getId(){
        return packType.getId();
    }
    
    public String getBloodBagType(){
        return packType.getBloodBagType();
    }
    
    public ComponentTypeViewModel getComponentType(){
        if (packType.getComponentType() != null) {
            return new ComponentTypeViewModel(packType.getComponentType());
        } else {
            return null;
        }
    }
    
    public Boolean getCanPool(){
        return packType.getCanPool();
    }
    
    public Boolean getCanSplit(){
        return packType.getCanSplit();
    }
    
    public Boolean getIsDeleted(){
        return packType.getIsDeleted();
    }
    
    public Boolean getCountAsDonation(){
        return packType.getCountAsDonation();
    }
    
    public Integer getPeriodBetweenDonations(){
        return packType.getPeriodBetweenDonations();
    }

    @Override
    public String toString() {
        return packType.toString();
    }
    
}
