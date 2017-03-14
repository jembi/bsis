package org.jembi.bsis.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
    if (bloodGroup != null) {
      BloodGroup bg = bloodgroups.get(bloodGroup.toLowerCase());
      if (bg == null) {
        abo = "";
        rh = "";
      } else {
        abo = bg.abo;
        rh = bg.rh;
      }
    } else {
      abo = "";
      rh = "";
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

  public static List<BloodGroup> toBloodGroups(List<String> bloodGroups) {
    if (bloodGroups == null) {
      return null;
    }
    List<BloodGroup> bloodGroupsList = new ArrayList<BloodGroup>();
    for (String bloodGroup : bloodGroups) {
      BloodGroup bg = new BloodGroup(bloodGroup);
      if (StringUtils.isEmpty(bg.getBloodAbo())) {
        throw new IllegalArgumentException("Invalid bloodGroup.");
      }
      bloodGroupsList.add(bg);
    }
    return bloodGroupsList;
  }

  @Override
  public String toString() {
    StringBuilder strBuilder = new StringBuilder();
    if (abo == null) {
      strBuilder.append("Unknown");
    } else {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((abo == null) ? 0 : abo.hashCode());
    result = prime * result + ((rh == null) ? 0 : rh.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BloodGroup other = (BloodGroup) obj;
    if (abo == null) {
      if (other.abo != null)
        return false;
    } else if (!abo.equals(other.abo))
      return false;
    if (rh == null) {
      if (other.rh != null)
        return false;
    } else if (!rh.equals(other.rh))
      return false;
    return true;
  }


}
