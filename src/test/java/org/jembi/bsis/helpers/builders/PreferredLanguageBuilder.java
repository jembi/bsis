package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;

public class PreferredLanguageBuilder extends AbstractEntityBuilder<PreferredLanguage> {

  private UUID id;
  private String language;

  public PreferredLanguageBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PreferredLanguageBuilder withLanguage(String language) {
    this.language = language;
    return this;
  }

  @Override
  public PreferredLanguage build() {
    PreferredLanguage preferredLanguage = new PreferredLanguage();
    preferredLanguage.setId(id);
    preferredLanguage.setPreferredLanguage(language);
    return preferredLanguage;
  }

  public static PreferredLanguageBuilder aPreferredLanguage() {
    return new PreferredLanguageBuilder();
  }
  
  public static PreferredLanguageBuilder anEnglishPreferredLanguage() {
    return new PreferredLanguageBuilder().withLanguage("English");
  }

}
