package model.util;

import java.util.HashMap;
import java.util.Map;

public class BloodGroup {

  private String abo;
  private String rh;

  static final private Map<String, BloodGroup> bloodgroups = new HashMap<String, BloodGroup>();
  
  static {
    bloodgroups.put("a+", new BloodGroup("A", "+"));
    bloodgroups.put("b+", new BloodGroup("B", "+"));
    bloodgroups.put("ab+", new BloodGroup("AB", "+"));
    bloodgroups.put("o+", new BloodGroup("O", "+"));
    bloodgroups.put("a-", new BloodGroup("A", "-"));
    bloodgroups.put("b-", new BloodGroup("B", "-"));
    bloodgroups.put("ab-", new BloodGroup("AB", "-"));
    bloodgroups.put("o-", new BloodGroup("O", "-"));
  }

  public BloodGroup() {
    abo = "";
    rh = "";
  }

  public BloodGroup(String bloodGroup) {
    BloodGroup bg = bloodgroups.get(bloodGroup.toLowerCase());
    if (bg == null) {
      abo = "";
      rh = "";
    }
    else {
      abo = bg.abo;
      rh = bg.rh;
    }
  }

  public BloodGroup(String abo, String rh) {
    if (abo == null || rh == null) {
      this.abo = null;
      this.rh = null;
      return;
    }
    this.abo = abo;
    this.rh = rh;
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
      if (rh.equals("+"))
        strBuilder.append("+");
      else if (rh.equals("-"))
        strBuilder.append("-");
      else {
        strBuilder = new StringBuilder();
        strBuilder.append("Unknown");
      }
    }
    return strBuilder.toString();
  }

  public String getBloodAbo() {
    return abo.toString();
  }

  public String getBloodRh() {
    return rh.toString();
  }
}
