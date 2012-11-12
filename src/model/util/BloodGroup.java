package model.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;

public class BloodGroup {

  private BloodAbo abo;
  private BloodRhd rhd;

  static final private Map<String, BloodGroup> bloodgroups = new HashMap<String, BloodGroup>();
  
  static {
    bloodgroups.put("a+", new BloodGroup(BloodAbo.A, BloodRhd.POSITIVE));
    bloodgroups.put("b+", new BloodGroup(BloodAbo.B, BloodRhd.POSITIVE));
    bloodgroups.put("ab+", new BloodGroup(BloodAbo.AB, BloodRhd.POSITIVE));
    bloodgroups.put("o+", new BloodGroup(BloodAbo.O, BloodRhd.POSITIVE));
    bloodgroups.put("a-", new BloodGroup(BloodAbo.A, BloodRhd.NEGATIVE));
    bloodgroups.put("b-", new BloodGroup(BloodAbo.B, BloodRhd.NEGATIVE));
    bloodgroups.put("ab-", new BloodGroup(BloodAbo.AB, BloodRhd.NEGATIVE));
    bloodgroups.put("o-", new BloodGroup(BloodAbo.O, BloodRhd.NEGATIVE));
  }

  public BloodGroup() {
    abo = BloodAbo.Unknown;
    rhd = BloodRhd.Unknown;
  }

  public BloodGroup(String bloodGroup) {
    BloodGroup bg = bloodgroups.get(bloodGroup.toLowerCase());
    if (bg == null) {
      abo = BloodAbo.Unknown;
      rhd = BloodRhd.Unknown;
    }
    else {
      abo = bg.abo;
      rhd = bg.rhd;
    }
  }

  public BloodGroup(BloodAbo abo, BloodRhd rhd) {
    this.abo = abo;
    this.rhd = rhd;
  }

  public BloodGroup toBloodGroup(String bloodGroup) {
    return BloodGroup.bloodgroups.get(bloodGroup.toLowerCase());
  }

  @Override
  public String toString() {
    StringBuilder strBuilder = new StringBuilder();
    if (abo == null || abo.equals(BloodAbo.Unknown)) {
      strBuilder.append("Unknown");
    }
    else {
      strBuilder.append(abo.toString());
      switch (rhd) {
        case POSITIVE: strBuilder.append("+");
                       break;
        case NEGATIVE: strBuilder.append("-");
                       break;
        case Unknown:  strBuilder = new StringBuilder();
                       strBuilder.append("Unknown");
                       break;
      }
    }
    return strBuilder.toString();
  }

  public BloodAbo getBloodAbo() {
    return abo;
  }

  public BloodRhd getBloodRhd() {
    return rhd;
  }
}
