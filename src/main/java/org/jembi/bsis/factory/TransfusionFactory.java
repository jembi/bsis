package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransfusionFactory {

  @Autowired
  private PatientFactory patientFactory;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  public Transfusion createEntity(TransfusionBackingForm form) {
    Transfusion transfusion = new Transfusion();
    transfusion.setId(form.getId());
    transfusion.setDonationIdentificationNumber(form.getDonationIdentificationNumber());
    // note: currently we always create a new patient entity because we only support creating new Transfusion data
    // and we don't attempt patient lookups.
    transfusion.setPatient(patientFactory.createEntity(form.getPatient()));
    if (form.getComponentCode() != null) {
      // the user scanned a component code - we need to use that
      transfusion.setComponentType(componentTypeRepository.findComponentTypeByCode(form.getComponentCode()));
    } else {
      // they selected the component type
      ComponentTypeBackingForm selectedComponentType = form.getComponentType();
      transfusion.setComponentType(componentTypeRepository.findComponentTypeByCode(selectedComponentType.getComponentTypeCode()));
    }
    transfusion.setUsageSite(locationRepository.getLocation(form.getReceivedFrom().getId()));
    if (form.getTransfusionReactionType() != null) {
      transfusion.setTransfusionReactionType(transfusionReactionTypeRepository.findById(form.getTransfusionReactionType().getId()));
    }
    transfusion.setTransfusionOutcome(form.getTransfusionOutcome());
    transfusion.setDateTransfused(form.getDateTransfused());
    // FIXME: waiting for this to be added: 
    // transfusion.setNotes(form.getNotes());
    transfusion.setIsDeleted(form.getIsDeleted()); // note: the validator must ensure this is not null
    return transfusion;
  }
}
