package model.producttype;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Set;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Audited
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class ProductTypeCombination {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=300)
  private String combinationName;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch=FetchType.EAGER)
  private List<ProductType> productTypes;
  
  @NotAudited
  @ManyToMany(mappedBy="producedProductTypeCombinations", fetch = FetchType.EAGER)
  private List<ProductType> sourceProductTypes;
  
  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<ProductType> getProductTypes() {
    return productTypes;
  }

  public void setProductTypes(List<ProductType> productTypes) {
    this.productTypes = productTypes;
  }
  
  public List<ProductType> getSourceProductTypes() {
    return sourceProductTypes;
  }

  public void setSourceProductTypes(List<ProductType> sourceProductTypes) {
    this.sourceProductTypes = sourceProductTypes;
  }

  public String getCombinationName() {
    return combinationName;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
