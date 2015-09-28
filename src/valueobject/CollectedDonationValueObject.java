package valueobject;

import model.donationtype.DonationType;
import model.location.Location;
import model.util.Gender;

public class CollectedDonationValueObject {
    
    private DonationType donationType;
    private Gender gender;
    private String bloodAbo;
    private String bloodRh;
    private long count;
    private Location venue;
    
    public CollectedDonationValueObject(DonationType donationType, Gender gender, String bloodAbo, String bloodRh,
            Location venue, long count) {
        this.donationType = donationType;
        this.gender = gender;
        this.bloodAbo = bloodAbo;
        this.bloodRh = bloodRh;
        this.venue = venue;
        this.count = count;
    }
    
    public DonationType getDonationType() {
        return donationType;
    }
    
    public void setDonationType(DonationType donationType) {
        this.donationType = donationType;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public String getBloodAbo() {
        return bloodAbo;
    }
    
    public void setBloodAbo(String bloodAbo) {
        this.bloodAbo = bloodAbo;
    }
    
    public String getBloodRh() {
        return bloodRh;
    }
    
    public void setBloodRh(String bloodRh) {
        this.bloodRh = bloodRh;
    }
    
    public Location getVenue() {
        return venue;
    }
    
    public void setVenue(Location venue) {
        this.venue = venue;
    }

    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }

}
