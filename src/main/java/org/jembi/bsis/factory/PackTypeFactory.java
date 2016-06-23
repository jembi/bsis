package org.jembi.bsis.factory;

import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;
import org.jembi.bsis.viewmodel.PackTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackTypeFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  public PackTypeViewModel createViewModel(PackType packType) {
    PackTypeViewModel viewModel = new PackTypeViewModel();
    populateViewModel(packType, viewModel);
    return viewModel;
  }

  public PackTypeViewFullModel createFullViewModel(PackType packType) {
    PackTypeViewFullModel viewModel = new PackTypeViewFullModel();
    populateFullViewModel(packType, viewModel);
    return viewModel;
  }

  private void populateViewModel(PackType packType, PackTypeViewModel viewModel) {
    viewModel.setId(packType.getId());
    viewModel.setPackType(packType.getPackType());
  }

  private void populateFullViewModel(PackType packType, PackTypeViewFullModel viewModel) {
    populateViewModel(packType, viewModel);
    viewModel.setCanPool(packType.getCanPool());
    viewModel.setCanSplit(packType.getCanSplit());
    viewModel.setComponentType(componentTypeFactory.createViewModel(packType.getComponentType()));
    viewModel.setCountAsDonation(packType.getCountAsDonation());
    viewModel.setIsDeleted(packType.getIsDeleted());
    viewModel.setPeriodBetweenDonations(packType.getPeriodBetweenDonations());
    viewModel.setTestSampleProduced(packType.getTestSampleProduced());

    System.out.println(viewModel.getComponentType());
  }

}
