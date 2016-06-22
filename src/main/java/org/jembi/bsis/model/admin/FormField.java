package org.jembi.bsis.model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.jembi.bsis.model.BaseEntity;

@Entity
public class FormField extends BaseEntity {
  private static final long serialVersionUID = 1L;

  public static final String FIELD = "field";
  public static final String DISPLAY_NAME = "displayName";
  public static final String SHORT_DISPLAY_NAME = "shortDisplayName";
  public static final String DEFAULT_DISPLAY_NAME = "defaultDisplayName";
  public static final String DEFAULT_VALUE = "defaultValue";

  public static final String ALLOW_CHANGE_HIDDEN = "allowChangeHidden";
  public static final String HIDDEN = "hidden";

  public static final String ALLOW_CHANGE_REQUIRED = "allowChangeRequired";
  public static final String REQUIRED = "required";

  public static final String IS_AUTO_GENERATABLE = "isAutoGeneratable";
  public static final String AUTO_GENERATE = "autoGenerate";
  public static final String IS_TIME_FIELD = "isTimeField";
  public static final String USE_CURRENT_TIME = "useCurrentTime";

  @Column(length = 30)
  private String form;

  @Column(length = 30)
  private String field;

  /**
   * User can override default display name with display name.
   */
  @Column(length = 60)
  private String displayName;

  @Column(length = 60)
  private String defaultDisplayName;

  @Column
  private Integer maxLength;

  @Lob
  private String defaultValue;

  /**
   * Fields can be hidden by setting this parameter to false. We could also have a shown field
   * instead of hidden field which would store true for any field that is visible on the form. But
   * that would not have made any difference. Right now we check if the field value is not true. In
   * this case I prefer hidden field instead of shown field because by some programming mistake if
   * we ended up storing NULL in the hidden field then the field would still show up in the UI (a
   * somewhat tolerable mistake from a user's point of view) rather than not show up at all (very
   * annoying for users). Of course in the first place such an error should be allowed in the code.
   */
  private Boolean hidden;

  private Boolean isHidable;

  private Boolean isRequired;

  private Boolean canBeOptional;

  private Boolean autoGenerate;

  private Boolean isAutoGeneratable;

  private Boolean useCurrentTime;

  private Boolean isTimeField;

  public String getForm() {
    return form;
  }

  public String getField() {
    return field;
  }

  public String getDisplayName() {
    return getDefaultDisplayName();
  }

  public String getShortDisplayName() {
    return displayName;
  }

  public String getDefaultDisplayName() {
    return defaultDisplayName;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setForm(String form) {
    this.form = form;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setDefaultDisplayName(String defaultDisplayName) {
    this.defaultDisplayName = defaultDisplayName;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public void copy(FormField formField) {
    this.displayName = formField.displayName;
    this.defaultValue = formField.defaultValue;
    if (this.isHidable)
      this.hidden = formField.hidden;
    if (this.canBeOptional)
      this.isRequired = formField.isRequired;
    if (this.isAutoGeneratable)
      this.autoGenerate = formField.autoGenerate;
    if (this.isTimeField)
      this.useCurrentTime = formField.useCurrentTime;
    this.maxLength = formField.maxLength;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  public Boolean getAutoGenerate() {
    return autoGenerate;
  }

  public void setAutoGenerate(Boolean autoGenerate) {
    this.autoGenerate = autoGenerate;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public Boolean getIsAutoGeneratable() {
    return isAutoGeneratable;
  }

  public void setIsAutoGeneratable(Boolean isAutoGeneratable) {
    this.isAutoGeneratable = isAutoGeneratable;
  }

  public Boolean getIsHidable() {
    return isHidable;
  }

  public void setIsHidable(Boolean isHidable) {
    this.isHidable = isHidable;
  }

  public Boolean getCanBeOptional() {
    return canBeOptional;
  }

  public void setCanBeOptional(Boolean canBeOptional) {
    this.canBeOptional = canBeOptional;
  }

  public Boolean getUseCurrentTime() {
    return useCurrentTime;
  }

  public void setUseCurrentTime(Boolean useCurrentTime) {
    this.useCurrentTime = useCurrentTime;
  }

  public Boolean getIsTimeField() {
    return isTimeField;
  }

  public void setIsTimeField(Boolean isTimeField) {
    this.isTimeField = isTimeField;
  }
}
