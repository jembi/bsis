package org.jembi.bsis.backingform;

import java.util.LinkedHashSet;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestContext;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBackingForm {

  private Long id;

  private BloodTest bloodTest;

  private String pattern;

  private DonationField donationFieldChanged;

  private String newInformation;

  private String extraInformation;

  private LinkedHashSet<String> pendingTestsIds;

  private BloodTestCategory category;

  private BloodTestSubCategory subCategory;

  private BloodTestContext context;

  private Boolean markSampleAsUnsafe;

  private boolean isDeleted = false;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public BloodTest getBloodTest () {
    return bloodTest;
  }

  public void setBloodTest (BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public String getPattern () {
    return pattern;
  }

  public void setPattern (String pattern) {
    this.pattern = pattern;
  }

  public DonationField getDonationFieldChanged () {
    return donationFieldChanged;
  }

  public void setDonationFieldChanged (DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }

  public String getNewInformation () {
    return newInformation;
  }

  public void setNewInformation (String newInformation) {
    this.newInformation = newInformation;
  }

  public String getExtraInformation () {
    return extraInformation;
  }

  public void setExtraInformation (String extraInformation) {
    this.extraInformation = extraInformation;
  }

  public LinkedHashSet<String> getPendingTestsIds () {
    return pendingTestsIds;
  }

  public void setPendingTestsIds (LinkedHashSet<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }

  public BloodTestCategory getCategory () {
    return category;
  }

  public void setCategory (BloodTestCategory category) {
    this.category = category;
  }

  public BloodTestSubCategory getSubCategory () {
    return subCategory;
  }

  public void setSubCategory (BloodTestSubCategory subCategory) {
    this.subCategory = subCategory;
  }

  public BloodTestContext getContext () {
    return context;
  }

  public void setContext (BloodTestContext context) {
    this.context = context;
  }

  public Boolean getMarkSampleAsUnsafe () {
    return markSampleAsUnsafe;
  }

  public void setMarkSampleAsUnsafe (Boolean markSampleAsUnsafe) {
    this.markSampleAsUnsafe = markSampleAsUnsafe;
  }

  public boolean isDeleted () {
    return isDeleted;
  }

  public void setDeleted (boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
