package model.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;

public class BloodGroup {

  private String abo;
  private String rh;

  static final private Map<String, BloodGroup> bloodgroups = new LinkedHashMap<String, BloodGroup>();
  
  static {
		bloodgroups.put("o+", new BloodGroup("O", "+"));
		bloodgroups.put("o-", new BloodGroup("O", "-"));
		bloodgroups.put("a+", new BloodGroup("A", "+"));
		bloodgroups.put("a-", new BloodGroup("A", "-"));
		bloodgroups.put("b+", new BloodGroup("B", "+"));
		bloodgroups.put("b-", new BloodGroup("B", "-"));  
		bloodgroups.put("ab+", new BloodGroup("AB", "+"));
		bloodgroups.put("ab-", new BloodGroup("AB", "-"));   
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
    if (abo == null) {
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

  public static Map<String, BloodGroup> getBloodgroups() {
	return bloodgroups;
  }
  
  public static Collection<BloodGroup> getAllBloodGroups() {
	return bloodgroups.values();
  }
}
