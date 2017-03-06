package org.jembi.bsis.service;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TransfusionReactionTypeCRUDService {

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;;

  public TransfusionReactionType createTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    transfusionReactionTypeRepository.save(transfusionReactionType);
    return transfusionReactionType;
  }
  
  public TransfusionReactionType updateTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    TransfusionReactionType existingTransfusionReactionType =
        transfusionReactionTypeRepository.findById(transfusionReactionType.getId());

    existingTransfusionReactionType.setName(transfusionReactionType.getName());
    existingTransfusionReactionType.setDescription(transfusionReactionType.getDescription());
    existingTransfusionReactionType.setIsDeleted(transfusionReactionType.getIsDeleted());
    return transfusionReactionTypeRepository.update(existingTransfusionReactionType);
  }
}
