package backingform;

import java.text.ParseException;
import java.util.Date;
import javax.validation.Valid;
import model.donationbatch.DonationBatch;
import model.location.Location;
import org.apache.commons.lang3.StringUtils;
import utils.CustomDateFormatter;

public class DonationBatchBackingForm {

  @Valid
  private DonationBatch donationBatch;
  
  public DonationBatchBackingForm() {
    donationBatch = new DonationBatch();
  }

  public DonationBatch getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
  }

  public Integer getId() {
    return donationBatch.getId();
  }

  public void setId(Integer id) {
    donationBatch.setId(id);
  }

  public String getBatchNumber() {
    return donationBatch.getBatchNumber();
  }

  public void setBatchNumber(String batchNumber) {
    donationBatch.setBatchNumber(batchNumber);
  }

  public String getDonationCenter() {
    Location center = donationBatch.getDonationCenter();
    if (center == null || center.getId() == null)
      return null;
    return center.getId().toString();
  }

  public String getDonationSite() {
    Location site = donationBatch.getDonationSite();
    if (site == null || site.getId() == null)
      return null;
    return site.getId().toString();
  }

  public void setDonationCenter(String center) {
    if (StringUtils.isBlank(center)) {
      donationBatch.setDonationCenter(null);
    }
    else {
      Location l = new Location();
      try {
        l.setId(Long.parseLong(center));
        donationBatch.setDonationCenter(l);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        donationBatch.setDonationCenter(null);
      }
    }
  }

  public void setDonationSite(String collectionSite) {
    if (StringUtils.isBlank(collectionSite)) {
      donationBatch.setDonationSite(null);
    }
    else {
      Location l = new Location();
      try {
        l.setId(Long.parseLong(collectionSite));
        donationBatch.setDonationSite(l);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        donationBatch.setDonationSite(null);
      }
    }
  }

    public String getBatchOpenedOn() {
        if(donationBatch.getBatchOpenedOn() == null)
            return CustomDateFormatter.getDateTimeString(new Date());
        else
            return CustomDateFormatter.getDateTimeString(donationBatch.getBatchOpenedOn());
    }

    public void setBatchOpenedOn(String batchOpenedOn) {
        try {
            donationBatch.setBatchOpenedOn(CustomDateFormatter.getDateTimeFromString(batchOpenedOn));
        } catch (ParseException ex) {
            ex.printStackTrace();
            donationBatch.setBatchOpenedOn(null);
        }
    }

    public String getBatchClosedOn() {
         if(donationBatch.getBatchClosedOn() == null)
            return "";
        else
            return CustomDateFormatter.getDateTimeString(donationBatch.getBatchClosedOn());
    }

    public void setBatchClosedOn(String batchClosedOn) {
        try {
            donationBatch.setBatchClosedOn(CustomDateFormatter.getDateTimeFromString(batchClosedOn));
        } catch (ParseException ex) {
            ex.printStackTrace();
            donationBatch.setBatchClosedOn(null);
        }
    }
    
    

  public String getNotes() {
    return donationBatch.getNotes();
  }

  public void setNotes(String notes) {
    donationBatch.setNotes(notes);
  }
}
