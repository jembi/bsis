package helpers.builders;

import java.util.List;

import model.componenttype.ComponentTypeCombination;
import backingform.ComponentTypeBackingForm;

public class ComponentTypeBackingFormBuilder {

    private String componentTypeName;
    private String componentTypeNameShort;
    private Integer expiresAfter;
    private String expiresAfterUnits;
    private String description;
    private Boolean bloodGroup;
    private List<ComponentTypeCombination> producedComponentTypeCombinations;

    public ComponentTypeBackingFormBuilder withComponentTypeName(String componentTypeName) {
        this.componentTypeName = componentTypeName;
        return this;
    }

    public ComponentTypeBackingFormBuilder withComponentTypeNameShort(String componentTypeNameShort) {
        this.componentTypeNameShort = componentTypeNameShort;
        return this;
    }

    public ComponentTypeBackingFormBuilder withExpiresAfter(Integer expiresAfter) {
        this.expiresAfter = expiresAfter;
        return this;
    }

    public ComponentTypeBackingFormBuilder withExpiresAfterUnits(String expiresAfterUnits) {
        this.expiresAfterUnits = expiresAfterUnits;
        return this;
    }

    public ComponentTypeBackingFormBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ComponentTypeBackingFormBuilder thatHasBloodGroup() {
        bloodGroup = true;
        return this;
    }

    public ComponentTypeBackingFormBuilder withProducedComponentTypeCombinations(
            List<ComponentTypeCombination> componentTypeCombinations) {
        producedComponentTypeCombinations = componentTypeCombinations;
        return this;
    }

    public ComponentTypeBackingForm build() {
        ComponentTypeBackingForm backingForm = new ComponentTypeBackingForm();
        backingForm.setComponentTypeName(componentTypeName);
        backingForm.setComponentTypeNameShort(componentTypeNameShort);
        backingForm.setExpiresAfter(expiresAfter);
        if (expiresAfterUnits != null) {
            backingForm.setExpiresAfterUnits(expiresAfterUnits);
        }
        backingForm.setDescription(description);
        backingForm.setHasBloodGroup(bloodGroup);
        backingForm.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
        return backingForm;
    }

    public static ComponentTypeBackingFormBuilder aComponentTypeBackingForm() {
        return new ComponentTypeBackingFormBuilder();
    }

}
