package repository;

import model.usage.ComponentUsage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class UsageRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveUsage(ComponentUsage componentUsage) {
    em.persist(componentUsage);
    em.flush();
  }

  public ComponentUsage findComponentUsage(String componentIdentificationNumber) throws NoResultException, NonUniqueResultException {
    String queryString = "SELECT u FROM ComponentUsage u WHERE u.component.componentIdentificationNumber = :componentIdentificationNumber and u.isDeleted= :isDeleted";
    TypedQuery<ComponentUsage> query = em.createQuery(queryString,
            ComponentUsage.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("componentIdentificationNumber",
            componentIdentificationNumber).getSingleResult();
  }

  public void deleteAllUsages() {
    Query query = em.createQuery("DELETE FROM ComponentUsage u");
    query.executeUpdate();
  }

  public List<ComponentUsage> findAnyUsageMatching(String componentIdentificationNumber,
                                                   String dateUsedFrom, String dateUsedTo, List<String> useIndications) throws ParseException {

    TypedQuery<ComponentUsage> query = em.createQuery(
            "SELECT u FROM ComponentUsage u WHERE "
                    + "(u.component.componentIdentificationNumber = :componentIdentificationNumber OR "
                    + "u.useIndication IN (:useIndications)) AND "
                    + "(u.dateUsed BETWEEN :dateUsedFrom AND " + ":dateUsedTo) AND "
                    + "(u.isDeleted= :isDeleted)", ComponentUsage.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("componentIdentificationNumber", componentIdentificationNumber == null ? ""
            : componentIdentificationNumber);
    query.setParameter("useIndications", useIndications);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from = (dateUsedFrom == null || dateUsedFrom.equals("")) ? dateFormat
            .parse("31/12/1970") : dateFormat.parse(dateUsedFrom);

    query.setParameter("dateUsedFrom", from);

    Date to = (dateUsedTo == null || dateUsedTo.equals("")) ? dateFormat
            .parse(dateFormat.format(new Date())) : dateFormat.parse(dateUsedTo);
    query.setParameter("dateUsedTo", to);


    return query.getResultList();
  }

  public ComponentUsage findUsageByComponentIdentificationNumber(String componentIdentificationNumber)
          throws NoResultException, NonUniqueResultException {
    TypedQuery<ComponentUsage> query = em
            .createQuery(
                    "SELECT u FROM ComponentUsage u WHERE u.component.componentIdentificationNumber = :componentIdentificationNumber and u.isDeleted= :isDeleted",
                    ComponentUsage.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("componentIdentificationNumber", componentIdentificationNumber);
    return query.getSingleResult();
  }

  public void deleteUsage(String componentIdentificationNumber) {
    ComponentUsage existingUsage = findUsageByComponentIdentificationNumber(componentIdentificationNumber);
    existingUsage.setIsDeleted(Boolean.TRUE);
    em.merge(existingUsage);
    em.flush();
  }

  public ComponentUsage findUsageById(Long usageId) throws IllegalArgumentException {
    return em.find(ComponentUsage.class, usageId);
  }

  public ComponentUsage addUsage(ComponentUsage usage) {
    em.persist(usage);
    em.flush();
    em.refresh(usage);
    return usage;
  }
}
