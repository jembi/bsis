package model.collectionbatch;

import java.util.Arrays;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.CollectionBatchViewModel;
import controller.UtilController;

public class CollectionBatchBackingFormValidator implements Validator {

  private Validator validator;

  private UtilController utilController;

  public CollectionBatchBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(CollectionBatchBackingForm.class,
        FindCollectionBatchBackingForm.class, CollectionBatch.class,
        CollectionBatchViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CollectionBatchBackingForm form = (CollectionBatchBackingForm) obj;
    utilController.commonFieldChecks(form, "collectionBatch", errors);
  }
}
