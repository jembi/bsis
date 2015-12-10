package model.componenttype;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Audited
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class ComponentTypeCombination {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 300)
  private String combinationName;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ComponentType> componentTypes;

  @NotAudited
  @ManyToMany(mappedBy = "producedComponentTypeCombinations", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private Set<ComponentType> sourceComponentTypes;

  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<ComponentType> getComponentTypes() {
    return componentTypes;
  }

  public void setComponentTypes(List<ComponentType> componentTypes) {
    this.componentTypes = componentTypes;
  }

  public Set<ComponentType> getSourceComponentTypes() {
    return sourceComponentTypes;
  }

  public void setSourceComponentTypes(Set<ComponentType> sourceComponentTypes) {
    this.sourceComponentTypes = sourceComponentTypes;
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

  public void copy(ComponentTypeCombination componentTypeCombination) {
    this.componentTypes = componentTypeCombination.getComponentTypes();
    this.sourceComponentTypes = componentTypeCombination.getSourceComponentTypes();
    this.combinationName = componentTypeCombination.getCombinationName();
    this.isDeleted = componentTypeCombination.getIsDeleted();
  }

}
