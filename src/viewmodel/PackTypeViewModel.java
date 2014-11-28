

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
    
    public ProductTypeViewModel getProductType(){
        if (packType.getProductType() != null) {
            return new ProductTypeViewModel(packType.getProductType());
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
    
    public Boolean get(){
        return packType.getIsDeleted();
    }
    
    public Boolean isCountAsDonation(){
        return packType.isCountAsDonation();
    }
    
    public Integer getPeriodBetweenDonations(){
        return packType.getPeriodBetweenDonations();
    }
    
}
