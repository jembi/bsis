

package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.rules.BloodTestSubCategory;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.CollectionField;


public class BloodTestingRuleBackingForm {

   @JsonIgnore
    private BloodTestingRule typingRule;
    
    public BloodTestingRuleBackingForm() {
        typingRule = new BloodTestingRule();
    }

    public BloodTestingRule getTypingRule() {
        return typingRule;
    }

    public void setTypingRule(BloodTestingRule typingRule) {
        this.typingRule = typingRule;
    }
    
    public void setId(Integer id){
        typingRule.setId(id);
    }
    
    public void setPendingTestsIds(String pendingTestsIds){
        typingRule.setPendingTestsIds(pendingTestsIds);
    }
    
    
    public void setCategory(String category){
        typingRule.setCategory(BloodTestCategory.valueOf(category));
    }
    
    
    public void setCollectionFieldChanged(String collectionField){
        typingRule.setCollectionFieldChanged(CollectionField.valueOf(collectionField));
    }
    
    public void setPattern(String pattern){
        typingRule.setPattern(pattern);
    }
    
    public void setContext(String context){
        typingRule.setContext(BloodTestContext.valueOf(context));
    }
    
    public void setExtraInformation(String extraInformation){
        typingRule.setExtraInformation(extraInformation);
    }
    
    public void setSubCategory(String subCategory){
        typingRule.setSubCategory(BloodTestSubCategory.valueOf(subCategory));
    }
    
    public void setisActive(Boolean isActive){
        typingRule.setIsActive(isActive);
    }
    
    public void setMarkSampleAsUnsafe(Boolean MarkSampleAsUnsafe){
        typingRule.setMarkSampleAsUnsafe(MarkSampleAsUnsafe);
    }
    
    public void setNewInformation(String newInformation){
        typingRule.setNewInformation(newInformation);
    }
    
    public void setBloodTestsIds(String bloodTestIds){
        typingRule.setBloodTestsIds(bloodTestIds);
    }
    

}
