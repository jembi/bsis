package model.donorcodes;



import java.util.List;
import java.util.Set;

import javax.persistence.*;



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
    
   @OneToMany(mappedBy = "donorCodeGroup",cascade = CascadeType.ALL)
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

    
   
}
