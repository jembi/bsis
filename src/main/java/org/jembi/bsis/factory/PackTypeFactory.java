package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.jembi.bsis.viewmodel.PackTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackTypeFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  
  public PackType createEntity(PackTypeBackingForm backingForm) {
    PackType packType = new PackType();
    packType.setId(backingForm.getId());
    packType.setPackType(backingForm.getPackType());
    if (backingForm.getComponentType() != null) {
      packType.setComponentType(componentTypeFactory.createEntity(backingForm.getComponentType()));
    }
    packType.setCanPool(backingForm.getCanPool());
    packType.setCanSplit(backingForm.getCanSplit());
    packType.setIsDeleted(backingForm.getIsDeleted());
    packType.setCountAsDonation(backingForm.getCountAsDonation());
    packType.setTestSampleProduced(backingForm.getTestSampleProduced());
    packType.setPeriodBetweenDonations(backingForm.getPeriodBetweenDonations());
    packType.setMinWeight(backingForm.getMinWeight());
    packType.setMaxWeight(backingForm.getMaxWeight());
    packType.setLowVolumeWeight(backingForm.getLowVolumeWeight());
    return packType;
  }

  public PackTypeViewModel createViewModel(PackType packType) {
    PackTypeViewModel viewModel = new PackTypeViewModel();
    populateViewModel(packType, viewModel);
    return viewModel;
  }

  public PackTypeFullViewModel createFullViewModel(PackType packType) {
    PackTypeFullViewModel viewModel = new PackTypeFullViewModel();
    populateFullViewModel(packType, viewModel);
    return viewModel;
  }

  public List<PackTypeFullViewModel> createFullViewModels(List<PackType> packTypes) {

    List<PackTypeFullViewModel> viewModels = new ArrayList<PackTypeFullViewModel>();
    for (PackType packType : packTypes) {
      viewModels.add(createFullViewModel(packType));
    }
    return viewModels;
  }

  private void populateViewModel(PackType packType, PackTypeViewModel viewModel) {
    viewModel.setId(packType.getId());
    viewModel.setPackType(packType.getPackType());
  }

  private void populateFullViewModel(PackType packType, PackTypeFullViewModel viewModel) {
    populateViewModel(packType, viewModel);
    viewModel.setCanPool(packType.getCanPool());
    viewModel.setCanSplit(packType.getCanSplit());
    if (packType.getComponentType() != null) {
      viewModel.setComponentType(componentTypeFactory.createViewModel(packType.getComponentType()));
    }
    viewModel.setCountAsDonation(packType.getCountAsDonation());
    viewModel.setIsDeleted(packType.getIsDeleted());
    viewModel.setPeriodBetweenDonations(packType.getPeriodBetweenDonations());
    viewModel.setTestSampleProduced(packType.getTestSampleProduced());
    viewModel.setMinWeight(packType.getMinWeight());
    viewModel.setMaxWeight(packType.getMaxWeight());
    viewModel.setLowVolumeWeight(packType.getLowVolumeWeight());
  }
}
