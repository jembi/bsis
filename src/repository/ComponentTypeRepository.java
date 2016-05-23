package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ComponentType> getAllComponentTypes() {
    TypedQuery<ComponentType> query;
    query = em.createQuery("SELECT ct from ComponentType ct where ct.isDeleted=:isDeleted", ComponentType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<ComponentType> getAllComponentTypesIncludeDeleted() {
    TypedQuery<ComponentType> query;
    query = em.createQuery("SELECT ct from ComponentType ct", ComponentType.class);
    return query.getResultList();
  }

  public boolean verifyComponentTypeExists(Long id) {
    return em.createNamedQuery(ComponentTypeQueryConstants.NAME_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS, Boolean.class)
        .setParameter("id", id)
        .setParameter("deleted", false)
        .getSingleResult();
  }

  public ComponentType getComponentTypeById(Long id) throws NoResultException, NonUniqueResultException {
    TypedQuery<ComponentType> query;
    query = em.createQuery("SELECT ct from ComponentType ct " +
        "where ct.id=:id", ComponentType.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void deactivateComponentType(Long componentTypeId) {
    ComponentType componentType = getComponentTypeById(componentTypeId);
    componentType.setIsDeleted(true);
    em.merge(componentType);
  }

  public void activateComponentType(Long componentTypeId) {
    ComponentType componentType = getComponentTypeById(componentTypeId);
    componentType.setIsDeleted(false);
    em.merge(componentType);
  }

  public List<ComponentTypeCombination> getAllComponentTypeCombinations() {

    String queryStr = "SELECT ct from ComponentTypeCombination ct WHERE " +
        "ct.isDeleted=:isDeleted";
    TypedQuery<ComponentTypeCombination> query = em.createQuery(queryStr, ComponentTypeCombination.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<ComponentTypeCombination> getAllComponentTypeCombinationsIncludeDeleted() {

    String queryStr = "SELECT ct from ComponentTypeCombination ct";
    TypedQuery<ComponentTypeCombination> query = em.createQuery(queryStr, ComponentTypeCombination.class);
    return query.getResultList();
  }

  public ComponentTypeCombination getComponentTypeCombinationById(Long id)
      throws NoResultException, NonUniqueResultException {
    TypedQuery<ComponentTypeCombination> query;
    query = em.createQuery("SELECT ct from ComponentTypeCombination ct " +
        "where ct.id=:id", ComponentTypeCombination.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public void saveComponentTypeCombination(
      ComponentTypeCombination componentTypeCombination) {

    String combinationName = componentTypeCombination.getCombinationName();
    List<ComponentType> componentTypes = new ArrayList<ComponentType>();
    List<String> combinationNameList = new ArrayList<String>();

    for (ComponentType componentType : componentTypeCombination.getComponentTypes()) {
      componentTypes.add(componentType);
      combinationNameList.add(componentType.getComponentTypeNameShort());
    }

    if (StringUtils.isBlank(combinationName)) {
      combinationName = StringUtils.join(combinationNameList, ",");
    }

    componentTypeCombination.setCombinationName(combinationName);
    componentTypeCombination.setComponentTypes(componentTypes);

    componentTypeCombination.setIsDeleted(false);
    em.persist(componentTypeCombination);
  }

  public ComponentType getComponentTypeByName(String componentTypeName) throws NoResultException, NonUniqueResultException {
    TypedQuery<ComponentType> query;
    query = em.createQuery("SELECT ct from ComponentType ct " +
        "where ct.componentType=:componentTypeName", ComponentType.class);
    query.setParameter("componentTypeName", componentTypeName);
    ComponentType componentType = null;
    componentType = query.getSingleResult();
    return componentType;
  }

  public List<ComponentType> getAllParentComponentTypes() {
    TypedQuery<ComponentType> query;
    List<ComponentType> componentTypes = new ArrayList<ComponentType>();
    query = em.createQuery("SELECT ct from ComponentType ct where ct.isDeleted=:isDeleted AND pediComponentType_id != null AND ct.id!= 1", ComponentType.class);
    query.setParameter("isDeleted", false);
    componentTypes = query.getResultList();
    componentTypes.add(getComponentTypeByIdList(1l).get(0));
    return componentTypes;
  }

  public List<ComponentType> getComponentTypeByIdList(Long id) {
    TypedQuery<ComponentType> query;
    query = em.createQuery("SELECT ct from ComponentType ct where ct.id IN (SELECT ct1.pediComponentType from ComponentType ct1 WHERE ct1.id=:id) ", ComponentType.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getResultList();
  }

  public ComponentType saveComponentType(ComponentType componentType) {
    em.persist(componentType);
    return componentType;
  }

  public ComponentType updateComponentType(ComponentType componentType) {
    ComponentType existingComponentType = getComponentTypeById(componentType.getId());
    existingComponentType.copy(componentType);
    return em.merge(existingComponentType);
  }


  public ComponentTypeCombination updateComponentTypeCombination(
      ComponentTypeCombination componentTypeCombination) throws IllegalArgumentException {
    ComponentTypeCombination existingComponentTypeCombination = getComponentTypeCombinationById(componentTypeCombination.getId());
    existingComponentTypeCombination.copy(componentTypeCombination);
    return em.merge(existingComponentTypeCombination);
  }


  public void deactivateComponentTypeCombination(Long componentTypeCombinationId) {
    ComponentTypeCombination componentTypeCombination = getComponentTypeCombinationById(componentTypeCombinationId);
    componentTypeCombination.setIsDeleted(true);
    em.merge(componentTypeCombination);
  }

  public void activateComponentTypeCombination(Long componentTypeCombinationId) {
    ComponentTypeCombination componentTypeCombination = getComponentTypeCombinationById(componentTypeCombinationId);
    componentTypeCombination.setIsDeleted(false);
    em.merge(componentTypeCombination);
  }

}
