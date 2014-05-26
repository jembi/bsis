	package model.donorcodes;
	
	import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
	
	@Audited
	@Entity
	public class DonorCode {
		
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
		@Column(nullable=false, updatable=false, insertable=false)
		private Long id;
	
		private String donorCode;
		
		@NotAudited
		@ManyToOne(cascade =CascadeType.ALL,fetch = FetchType.EAGER )
		@JoinColumn(nullable = false,name = "donorCodeGroupId")
		private DonorCodeGroup donorCodeGroup;
	
		public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
		public String getDonorCode() {
			return donorCode;
		}
	
		public void setDonorCode(String donorCode) {
			this.donorCode = donorCode;
		}
	
		public DonorCodeGroup getDonorCodeGroup() {
			return donorCodeGroup;
		}
	
		public void setDonorCodeGroup(DonorCodeGroup donorCodeGroup) {
			this.donorCodeGroup = donorCodeGroup;
		}
		
		@Override
	    public boolean equals(Object object)
	    {
	        boolean isEqual = false;

	        if (object != null && object instanceof DonorCode )
	        {
	        	isEqual = this.id == ((DonorCode) object).id;
	        }

	        return isEqual;
	    }
	
		@Override
	    public int hashCode()
	    {
	       return this.id.intValue();
	    }

		
	}
