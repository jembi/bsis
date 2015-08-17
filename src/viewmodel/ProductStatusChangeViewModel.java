package viewmodel;

import utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.request.Request;
import model.component.Component;
import model.component.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeType;
import model.productmovement.ProductStatusChangeReason;
import model.producttype.ProductType;
import model.user.User;

public class ProductStatusChangeViewModel {
	
	public ProductStatusChangeViewModel() {
	}
	
	public ProductStatusChangeViewModel(ProductStatusChange productStatusChange) {
        this.productStatusChange = productStatusChange;
    }
    
	@JsonIgnore
	private ProductStatusChange productStatusChange;
	
	public ProductStatusChange getProductStatusChange() {
        return productStatusChange;
    }

    public void setProductStatusChange(ProductStatusChange productStatusChange) {
        this.productStatusChange = productStatusChange;
    }
	
    public Long getId() {
	    return productStatusChange.getId();
	}
	
    @JsonIgnore
    public Component getComponent() {
	    return productStatusChange.getComponent();
	}
	
    public String getStatusChangedOn() {
	    return  CustomDateFormatter.getDateTimeString(productStatusChange.getStatusChangedOn());
	}
    
    public ProductStatus getNewStatus() {
	    return productStatusChange.getNewStatus();
	}

    public Request getIssuedTo() {
	    return productStatusChange.getIssuedTo();
	}
    
    public String getChangedBy() {
    	User user = productStatusChange.getChangedBy();
        if (user == null || user.getUsername() == null)
          return "";
        return user.getUsername();
	}
    
    public String getStatusChangeReasonText() {
	    return productStatusChange.getStatusChangeReasonText();
	}
    
    public String getIssuedBy() {
    	User user = productStatusChange.getIssuedBy();
        if (user == null || user.getUsername() == null)
          return "";
        return user.getUsername();
	}
    
    public String getIssuedOn() {
	    return CustomDateFormatter.getDateTimeString(productStatusChange.getIssuedOn());
	}

    public ProductStatusChangeReason getStatusChangeReason() {
	    return productStatusChange.getStatusChangeReason();
	}
    
    public ProductStatusChangeType getStatusChangeType() {
	    return productStatusChange.getStatusChangeType();
	}

}
