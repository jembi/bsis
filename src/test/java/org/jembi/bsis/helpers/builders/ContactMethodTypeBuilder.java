package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.address.ContactMethodType;

public class ContactMethodTypeBuilder extends AbstractEntityBuilder<ContactMethodType> {

  private UUID id;
  private String contactMethod;

  public ContactMethodTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ContactMethodTypeBuilder withContactMethodType(String contactMethodType) {
    this.contactMethod = contactMethodType;
    return this;
  }

  @Override
  public ContactMethodType build() {
    ContactMethodType contactMethodType = new ContactMethodType();
    contactMethodType.setId(id);
    contactMethodType.setContactMethodType(contactMethod);
    return contactMethodType;
  }
  
  public static ContactMethodTypeBuilder aContactMethodType() {
    return new ContactMethodTypeBuilder();
  }

  
  public static ContactMethodTypeBuilder anEmailContactMethodType() {
    return new ContactMethodTypeBuilder().withContactMethodType("Email");
  }

}
