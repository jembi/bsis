package model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class FormField {

  public static final String FIELD = "field";
  public static final String DISPLAY_NAME = "displayName";
  public static final String DEFAULT_DISPLAY_NAME = "defaultDisplayName";
  public static final String DEFAULT_VALUE = "defaultValue";
  public static final String HIDDEN = "hidden";
  public static final String DERIVED = "derived";
  public static final String SOURCE_FIELD = "sourceField";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @NotBlank
  @Column(length=30, nullable=false)
  private String form;

  @NotBlank
  @Column(length=30, nullable=false)
  private String field;

  @Column(length=30)
  private String displayName;

  @NotBlank
  @Column(length=30, nullable=false)
  private String defaultDisplayName;

  @Lob
  private String defaultValue;
  
  private Boolean hidden = Boolean.FALSE;

  private Boolean derived = Boolean.FALSE;

  // copy value from another field
  @Column(length=30, nullable=false)
  private String sourceField;

  public Long getId() {
    return id;
  }

  public String getForm() {
    return form;
  }

  public String getField() {
    return field;
  }

  public String getDisplayName() {
    if (displayName == null || displayName.trim().equals(""))
      return getDefaultDisplayName();
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

  public Boolean getDerived() {
    return derived;
  }

  public String getSourceField() {
    return sourceField;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setDerived(Boolean derived) {
    this.derived = derived;
  }

  public void setSourceField(String sourceField) {
    this.sourceField = sourceField;
  }

  public void copy(FormField formField) {
    this.displayName = formField.displayName;
    this.defaultValue = formField.defaultValue;
    this.hidden = formField.hidden;
    this.derived = formField.derived;
    this.sourceField = formField.sourceField;
  }

}
