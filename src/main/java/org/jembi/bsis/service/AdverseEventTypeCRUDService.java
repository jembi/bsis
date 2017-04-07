package org.jembi.bsis.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AdverseEventTypeCRUDService {

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  public AdverseEventType createAdverseEventType(AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = new AdverseEventType();
    adverseEventType.setName(backingForm.getName());
    adverseEventType.setDescription(backingForm.getDescription());
    adverseEventType.setDeleted(backingForm.getIsDeleted());
    adverseEventTypeRepository.save(adverseEventType);
    return adverseEventType;
  }

  public AdverseEventType updateAdverseEventType(UUID id, AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = adverseEventTypeRepository.findById(id);
    adverseEventType.setName(backingForm.getName());
    adverseEventType.setDescription(backingForm.getDescription());
    adverseEventType.setDeleted(backingForm.getIsDeleted());
    return adverseEventTypeRepository.update(adverseEventType);
  }
}
