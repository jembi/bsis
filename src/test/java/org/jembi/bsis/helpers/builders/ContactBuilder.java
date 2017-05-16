package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.address.Contact;

public class ContactBuilder extends AbstractEntityBuilder<Contact> {

  private UUID id;
  private String mobileNumber;
  private String homeNumber;
  private String workNumber;
  private String email;

  public ContactBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ContactBuilder withMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  public ContactBuilder withHomeNumber(String homeNumber) {
    this.homeNumber = homeNumber;
    return this;
  }
  
  public ContactBuilder withWorkNumber(String workNumber) {
    this.workNumber = workNumber;
    return this;
  }
  
  public ContactBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  @Override
  public Contact build() {
    Contact contact = new Contact();
    contact.setId(id);
    contact.setEmail(email);
    contact.setWorkNumber(workNumber);
    contact.setHomeNumber(homeNumber);
    contact.setMobileNumber(mobileNumber);
    return contact;
  }
  
  public static ContactBuilder aContact() {
    return new ContactBuilder();
  }

}
