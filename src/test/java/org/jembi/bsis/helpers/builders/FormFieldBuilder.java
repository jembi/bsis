package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.admin.FormField;

public class FormFieldBuilder extends AbstractEntityBuilder<FormField> {

  private Long id;

  private String defaultDisplayName;
  private String displayName;
  private String defaultValue;

  private Boolean autoGenerate;
  private Boolean isAutoGeneratable;

  private Boolean useCurrentTime;
  private Boolean isTimeField;

  private Boolean hidden;

  private String form;
  private String field;
  private Integer maxLength;

  private Boolean isRequired;

  public FormFieldBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public FormFieldBuilder withForm(String form) {
    this.form = form;
    return this;
  }

  public FormFieldBuilder withField(String field) {
    this.field = field;
    return this;
  }

  public FormFieldBuilder withMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public FormFieldBuilder withDefaultDisplayName(String defaultDisplayName) {
    this.defaultDisplayName = defaultDisplayName;
    return this;
  }

  public FormFieldBuilder withDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public FormFieldBuilder withDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public FormFieldBuilder thatIsAutoGeneratable(Boolean isAutoGeneratable) {
    this.isAutoGeneratable = isAutoGeneratable;
    return this;
  }

  public FormFieldBuilder withAutoGenerate(Boolean autoGenerate) {
    this.autoGenerate = autoGenerate;
    return this;
  }

  public FormFieldBuilder thatIsTimeField(Boolean isTimeField) {
    this.isTimeField = isTimeField;
    return this;
  }

  public FormFieldBuilder withUseCurrentTime(Boolean useCurrentTime) {
    this.useCurrentTime = useCurrentTime;
    return this;
  }

  public FormFieldBuilder thatIsHidden(Boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  public FormFieldBuilder thatIsRequired(Boolean required) {
    this.isRequired = required;
    return this;
  }

  @Override
  public FormField build() {
    FormField formField = new FormField();

    formField.setId(id);
    formField.setDefaultDisplayName(defaultDisplayName);
    formField.setDisplayName(displayName);
    formField.setDefaultValue(defaultValue);
    formField.setField(field);
    formField.setForm(form);
    formField.setMaxLength(maxLength);
    formField.setHidden(hidden);
    formField.setAutoGenerate(autoGenerate);
    formField.setIsAutoGeneratable(isAutoGeneratable);
    formField.setIsTimeField(isTimeField);
    formField.setUseCurrentTime(useCurrentTime);
    formField.setIsRequired(isRequired);

    return formField;
  }

  public static FormFieldBuilder aFormField() {
    return new FormFieldBuilder();
  }

}