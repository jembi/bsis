
package backingform;

import javax.validation.Valid;
import model.location.Location;

/**
 *
 * @author srikanth
 */
public class LocationBackingForm {

    
    

    
    @Valid
    private Location location;

    public LocationBackingForm() {
       location = new Location();
    }
    
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getId(){
        return location.getId();
    }
    
    public void setId(Long id){
        location.setId(id);
    }
    
    public String getName() {
        return location.getName();
    }

    public void setName(String name) {
        location.setName(name);
    }

    public Boolean isIsDonationCenter() {
        return location.getIsCollectionCenter();
    }

    public void setIsDonationCenter(Boolean isCenter) {
        location.setIsCollectionCenter(isCenter);
    }

    public Boolean isIsDonationSite() {
        return location.getIsCollectionSite();
    }

    public void setIsDonationSite(Boolean isCollectionSite) {
        location.setIsCollectionSite(isCollectionSite);
    }

    public Boolean isIsUsageSite() {
        return location.getIsUsageSite();
    }

    public void setIsUsageSite(Boolean isUsageSite) {
        location.setIsUsageSite(isUsageSite);
    }
    
    public Boolean isIsMobilesite(){
        return location.getIsMobileSite();
    }
    
    public void setIsMobileSite(Boolean isMobileSite){
        location.setIsMobileSite(isMobileSite);
    }
    
    public Boolean isIsDonorPanel(){
        return location.getIsDonorPanel();
    }
    
    public void setIsDonorPanel(Boolean isDonorPanel){
        location.setIsDonorPanel(isDonorPanel);
    }
    
    public String getNotes(){
        return location.getNotes();
    }
    
    public void setNotes(String notes){
        location.setNotes(notes);
    }
}
