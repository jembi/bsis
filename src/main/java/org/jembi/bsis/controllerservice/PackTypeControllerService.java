package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PackTypeControllerService {
  
  @Autowired
  private PackTypeRepository packTypeRepository;
  @Autowired
  private PackTypeFactory packTypeFactory;
  
  public List<PackTypeFullViewModel> getAllPackTypes() {
    return packTypeFactory.createFullViewModels(packTypeRepository.getAllPackTypes());
  }
  
  public PackTypeFullViewModel getPackTypeById(UUID id) {
    PackType packType = packTypeRepository.getPackTypeById(id);
    return packTypeFactory.createFullViewModel(packType);
  }
  
  public PackTypeFullViewModel createPackType(PackTypeBackingForm backingForm) {
    PackType packType = packTypeFactory.createEntity(backingForm);
    // TODO: Use pack type CRUD service
    packType = packTypeRepository.savePackType(packType);
    return packTypeFactory.createFullViewModel(packType);
  }
  
  public PackTypeFullViewModel updatePackType(PackTypeBackingForm backingForm) {
    PackType packType = packTypeFactory.createEntity(backingForm);
    // TODO: Use pack type CRUD service
    packType = packTypeRepository.updatePackType(packType);
    return packTypeFactory.createFullViewModel(packType);
}

}
