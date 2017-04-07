package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.address.AddressType;

public class AddressTypeBuilder extends AbstractEntityBuilder<AddressType> {

  private UUID id;
  private String preferredAddressType;

  public AddressTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public AddressTypeBuilder withAddressType(String preferredAddressType) {
    this.preferredAddressType = preferredAddressType;
    return this;
  }

  @Override
  public AddressType build() {
    AddressType addressType = new AddressType();
    addressType.setId(id);
    addressType.setPreferredAddressType(preferredAddressType);
    return addressType;
  }
  
  public static AddressTypeBuilder anAddressType() {
    return new AddressTypeBuilder();
  }
  
  public static AddressTypeBuilder aHomeAddressType() {
    return new AddressTypeBuilder().withAddressType("Home");
  }

}
