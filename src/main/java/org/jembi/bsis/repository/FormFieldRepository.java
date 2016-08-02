package org.jembi.bsis.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.admin.FormField;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FormFieldRepository {
  
  private static final Logger LOGGER = Logger.getLogger(FormFieldRepository.class);
      
  @PersistenceContext
  private EntityManager em;

  public void saveFormField(FormField formField) {
    em.persist(formField);
    em.flush();
  }

  public FormField findFormFieldById(Long formFieldId) {
    try {
      String queryString = "SELECT f FROM FormField f WHERE f.id = :formFieldId";
      TypedQuery<FormField> query = em.createQuery(queryString, FormField.class);
      return query.setParameter("formFieldId", formFieldId).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public FormField updateFormField(FormField formField) {
    FormField existingFormField = findFormFieldById(formField.getId());
    if (existingFormField == null) {
      return null;
    }
    existingFormField.copy(formField);
    em.merge(existingFormField);
    em.flush();
    return existingFormField;
  }

  public FormField getFormField(String formName, String fieldName) {
    try {
      String queryString = "SELECT f FROM FormField f where form=:formName AND field=:fieldName";
      TypedQuery<FormField> query = em.createQuery(queryString, FormField.class);
      query.setParameter("formName", formName);
      query.setParameter("fieldName", fieldName);
      return query.getSingleResult();
    } catch (NoResultException ex) {
      LOGGER.warn("Could not find FormField for Form '" + formName + "' and field '" + fieldName + "'");
      return null;
    }
  }

  public List<FormField> getFormFields(String formName) {
    try {
      String queryString = "SELECT f FROM FormField f where form=:formName";
      TypedQuery<FormField> query = em.createQuery(queryString, FormField.class);
      query.setParameter("formName", formName);
      return query.getResultList();
    } catch (NoResultException ex) {
      LOGGER.warn("Could not find FormFields for Form '" + formName + "'");
      return Arrays.asList(new FormField[0]);
    }
  }

  public List<String> getRequiredFormFields(String formName) {
    String queryString = "SELECT f.field FROM FormField f where form=:formName and isRequired=:isRequired and hidden=:hidden";
    TypedQuery<String> query = em.createQuery(queryString, String.class);
    query.setParameter("formName", formName);
    query.setParameter("isRequired", true);
    query.setParameter("hidden", false);
    return query.getResultList();
  }

  public Map<String, Integer> getFieldMaxLengths(String formName) {
    String queryString = "SELECT f.field, f.maxLength FROM FormField f where form=:formName";
    TypedQuery<Object[]> query = em.createQuery(queryString, Object[].class);
    query.setParameter("formName", formName);
    Map<String, Integer> maxLengths = new HashMap<String, Integer>();
    for (Object[] obj : query.getResultList()) {
      maxLengths.put((String) obj[0], (Integer) obj[1]);
    }
    return maxLengths;
  }

}
