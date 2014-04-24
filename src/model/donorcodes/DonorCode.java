	package model.donorcodes;
	
	import javax.persistence.CascadeType;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;
	
	
	@Entity
	public class DonorCode {
		
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
		@Column(nullable=false, updatable=false, insertable=false)
		private Long id;
	
		private String donorCode;
		
		@ManyToOne(cascade =CascadeType.ALL )
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
		
	
	}
