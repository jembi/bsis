package model.donorcodes;



import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;



/**
 * Entity implementation class for Entity: DonorCodeGroup
 *
 */
@Entity
public class DonorCodeGroup  {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false, updatable=false, insertable=false)
	private Long id;
    
    
    private String donorCodeGroup ;
    
   @OneToMany(mappedBy = "donorCodeGroup",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
   @Fetch(value = FetchMode.SUBSELECT)
    private List<DonorCode> donorCodes;
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDonorCodeGroup() {
		return donorCodeGroup;
	}

	public void setDonorCodeGroup(String donorCodeGroup) {
		this.donorCodeGroup = donorCodeGroup;
	}

	public List<DonorCode> getDonorCodes() {
	return donorCodes;
    }

   public void setDonorCodes(List<DonorCode> donorCodes) {
	this.donorCodes = donorCodes;
    }

    @Override
	    public boolean equals(Object object)
	    {
	        boolean isEqual = false;

	        if (object != null && object instanceof DonorCodeGroup )
	        {
	        	isEqual = this.id == ((DonorCodeGroup) object).id;
	        }

	        return isEqual;
	    }
	
		@Override
	    public int hashCode()
	    {
	       return this.id.intValue();
	    }
   
}
