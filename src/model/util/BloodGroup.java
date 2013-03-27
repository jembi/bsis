package model.util;

import java.util.HashMap;
import java.util.Map;

public class BloodGroup {

  private BloodAbo abo;
  private BloodRh rhd;

  static final private Map<String, BloodGroup> bloodgroups = new HashMap<String, BloodGroup>();
  
  static {
    bloodgroups.put("a+", new BloodGroup(BloodAbo.A, BloodRh.POSITIVE));
    bloodgroups.put("b+", new BloodGroup(BloodAbo.B, BloodRh.POSITIVE));
    bloodgroups.put("ab+", new BloodGroup(BloodAbo.AB, BloodRh.POSITIVE));
    bloodgroups.put("o+", new BloodGroup(BloodAbo.O, BloodRh.POSITIVE));
    bloodgroups.put("a-", new BloodGroup(BloodAbo.A, BloodRh.NEGATIVE));
    bloodgroups.put("b-", new BloodGroup(BloodAbo.B, BloodRh.NEGATIVE));
    bloodgroups.put("ab-", new BloodGroup(BloodAbo.AB, BloodRh.NEGATIVE));
    bloodgroups.put("o-", new BloodGroup(BloodAbo.O, BloodRh.NEGATIVE));
  }

  public BloodGroup() {
    abo = BloodAbo.Unknown;
    rhd = BloodRh.Unknown;
  }

  public BloodGroup(String bloodGroup) {
    BloodGroup bg = bloodgroups.get(bloodGroup.toLowerCase());
    if (bg == null) {
      abo = BloodAbo.Unknown;
      rhd = BloodRh.Unknown;
    }
    else {
      abo = bg.abo;
      rhd = bg.rhd;
    }
  }

  public BloodGroup(BloodAbo abo, BloodRh rhd) {
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

  public BloodRh getBloodRhd() {
    return rhd;
  }
}
