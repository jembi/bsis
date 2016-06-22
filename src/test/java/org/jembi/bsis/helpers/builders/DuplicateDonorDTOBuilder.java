package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.model.util.Gender;

public class DuplicateDonorDTOBuilder extends AbstractBuilder<DuplicateDonorDTO> {

  private String groupKey;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private long count;

  public DuplicateDonorDTOBuilder withGroupKey(String groupKey) {
    this.groupKey = groupKey;
    return this;
  }

  public DuplicateDonorDTOBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public DuplicateDonorDTOBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public DuplicateDonorDTOBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DuplicateDonorDTOBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public DuplicateDonorDTOBuilder withCount(Long count) {
    this.count = count;
    return this;
  }

  @Override
  public DuplicateDonorDTO build() {
    DuplicateDonorDTO duplicateDonorDTO = new DuplicateDonorDTO();
    duplicateDonorDTO.setBirthDate(birthDate);
    duplicateDonorDTO.setCount(count);
    duplicateDonorDTO.setFirstName(firstName);
    duplicateDonorDTO.setLastName(lastName);
    duplicateDonorDTO.setGender(gender);
    duplicateDonorDTO.setGroupKey(groupKey);
    return duplicateDonorDTO;
  }

  public static DuplicateDonorDTOBuilder aDuplicateDonorDTO() {
    return new DuplicateDonorDTOBuilder();
  }

}
