/*
* - commented on issue #209 [Adapt BSIS to Expose Rest Services]


package backingform.validator;

import java.util.Arrays;
import java.util.List;

import model.location.Location;
import model.util.BloodGroup;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.UtilController;

public class DonorCommunicationsBackingFormValidator implements Validator {

	private Validator validator;

	public DonorCommunicationsBackingFormValidator(Validator validator,
			UtilController utilController) {
		super();
		this.validator = validator;
	}

	public DonorCommunicationsBackingFormValidator() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class<?> clazz) {
		return Arrays.asList(DonorCommunicationsBackingForm.class).contains(
				clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		if (obj == null || validator == null)
			return;
		ValidationUtils.invokeValidator(validator, obj, errors);

		DonorCommunicationsBackingForm form = (DonorCommunicationsBackingForm) obj;

		List<Location> venues = form.getVenues();
		List<BloodGroup> bloodGroups = form.getBloodGroups();

		if (venues == null || venues.isEmpty()) {
			errors.rejectValue("venueErrorMessage","venues.empty",
					"Select 1 or more Donor Panel(s).");
		}

		if (bloodGroups == null || bloodGroups.isEmpty()) {
			errors.rejectValue("bloodGroupErrorMessage","bloodGroups.empty",
					" Select 1 or more Blood Group(s).");
		}
	}
}
*/