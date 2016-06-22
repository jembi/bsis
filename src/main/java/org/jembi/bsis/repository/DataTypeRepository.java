package org.jembi.bsis.repository;

import org.jembi.bsis.model.admin.DataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

@Repository
@Transactional
public class DataTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<DataType> getAll() {
    TypedQuery<DataType> query = em.createQuery("select dt from DataType dt", DataType.class);
    return query.getResultList();
  }

  public DataType getDataTypeByid(Long id) {
    TypedQuery<DataType> query = em.createQuery("SELECT dt FROM DataType dt WHERE dt.id = :id ", DataType.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public DataType getDataTypeByName(String dataType) {
    TypedQuery<DataType> query = em.createQuery("SELECT dt FROM DataType dt WHERE dt.datatype = :datatype ", DataType.class);
    query.setParameter("datatype", dataType);
    DataType result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
    }
    return result;
  }
}
