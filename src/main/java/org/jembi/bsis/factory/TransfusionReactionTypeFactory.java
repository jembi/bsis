package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TransfusionReactionTypeFactory {

  public TransfusionReactionTypeManagementViewModel createTransfusionReactionTypeManagementViewModel(
      TransfusionReactionType transfusionReactionType) {
    TransfusionReactionTypeManagementViewModel viewModel = new TransfusionReactionTypeManagementViewModel();
    if (transfusionReactionType != null) {
      populateBasicFields(viewModel, transfusionReactionType);
      viewModel.setDescription(transfusionReactionType.getDescription());
    }
    return viewModel;
  }

  public TransfusionReactionTypeViewModel createTransfusionReactionTypeViewModel(TransfusionReactionType transfusionReactionType) {
    TransfusionReactionTypeViewModel viewModel = new TransfusionReactionTypeViewModel();
    if (transfusionReactionType != null) {
      populateBasicFields(viewModel, transfusionReactionType);
    }
    return viewModel;
  }

  private void populateBasicFields(TransfusionReactionTypeViewModel viewModel, TransfusionReactionType transfusionReactionType) {
    viewModel.setId(transfusionReactionType.getId());
    viewModel.setName(transfusionReactionType.getName());
    viewModel.setIsDeleted(transfusionReactionType.getIsDeleted());
  }

  public List<TransfusionReactionTypeViewModel> createTransfusionReactionTypeViewModels(
      List<TransfusionReactionType> transfusionReactionTypes) {
    List<TransfusionReactionTypeViewModel> viewModels = new ArrayList<>();
    for (TransfusionReactionType transfusionReactionType : transfusionReactionTypes) {
      viewModels.add(createTransfusionReactionTypeViewModel(transfusionReactionType));
    }
    return viewModels;
  }

  public List<TransfusionReactionTypeManagementViewModel> createTransfusionReactionTypeManagementViewModels(
      List<TransfusionReactionType> transfusionReactionTypes) {
    List<TransfusionReactionTypeManagementViewModel> viewModels = new ArrayList<>();
    for (TransfusionReactionType transfusionReactionType : transfusionReactionTypes) {
      viewModels.add(createTransfusionReactionTypeManagementViewModel(transfusionReactionType));
    }
    return viewModels;
  }

  public TransfusionReactionType createEntity(TransfusionReactionTypeBackingForm backingForm) {
    TransfusionReactionType transfusionReactionType = new TransfusionReactionType();
    transfusionReactionType.setId(backingForm.getId());
    transfusionReactionType.setName(backingForm.getName());
    transfusionReactionType.setDescription(backingForm.getDescription());
    transfusionReactionType.setIsDeleted(backingForm.getIsDeleted()); // note: validator needs to check that isDeleted is not null
    return transfusionReactionType;
  }
}