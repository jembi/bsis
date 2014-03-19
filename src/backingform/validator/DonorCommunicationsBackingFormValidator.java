package backingform.validator;

import java.util.Arrays;
import java.util.List;

import model.location.Location;
import model.util.BloodGroup;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import backingform.DonorCommunicationBackingForm;
import controller.UtilController;

public class DonorCommunicationsBackingFormValidator implements Validator {

	private Validator validator;

	public DonorCommunicationsBackingFormValidator(Validator validator,
			UtilController utilController) {
		super();
		this.validator = validator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class<?> clazz) {
		return Arrays.asList(DonorCommunicationBackingForm.class).contains(
				clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		if (obj == null || validator == null)
			return;
		ValidationUtils.invokeValidator(validator, obj, errors);

		DonorCommunicationBackingForm form = (DonorCommunicationBackingForm) obj;

		List<Location> donorPanel = form.getDonorPanels();
		List<BloodGroup> bloodGroups = form.getBloodGroups();

		if (donorPanel == null || donorPanel.isEmpty()) {
			errors.rejectValue("donorPanelErrorMessage","donorPanelErrorMessage",
					"Select 1 or more Donor Panel(s).");
		}

		if (bloodGroups == null || bloodGroups.isEmpty()) {
			errors.rejectValue("donorBloodGrpErrorMessage","donorBloodGrpErrorMessage",
					" Select 1 or more Blood Group(s).");
		}
	}
}
