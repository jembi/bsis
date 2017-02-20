package org.jembi.bsis.backingform;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionBackingForm {

  private Long id;

  private String donationIdentificationNumber;
  
  private PatientBackingForm patient;
  
  private String componentCode;
  
  private ComponentTypeBackingForm componentType;
  
  private LocationBackingForm usageSite;
  
  private TransfusionReactionTypeBackingForm transfusionReactionType;
  
  private TransfusionOutcome transfusionOutcome;
  
  private Date dateTransfused;
  
  private Boolean isDeleted;

  
}
