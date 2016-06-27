package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;
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
  
  public List<PackTypeViewFullModel> getAllPackTypes() {
    return packTypeFactory.createFullViewModels(packTypeRepository.getAllPackTypes());
  }
  
  public PackTypeViewFullModel getPackTypeById(long id) {
    PackType packType = packTypeRepository.getPackTypeById(id);
    return packTypeFactory.createFullViewModel(packType);
  }
  
  public PackTypeViewFullModel createPackType(PackTypeBackingForm backingForm) {
    PackType packType = packTypeFactory.createEntity(backingForm);
    // TODO: Use pack type CRUD service
    packType = packTypeRepository.savePackType(packType);
    return packTypeFactory.createFullViewModel(packType);
  }
  
  public PackTypeViewFullModel updatePackType(PackTypeBackingForm backingForm) {
    PackType packType = packTypeFactory.createEntity(backingForm);
    // TODO: Use pack type CRUD service
    packType = packTypeRepository.updatePackType(packType);
    return packTypeFactory.createFullViewModel(packType);
}

}
