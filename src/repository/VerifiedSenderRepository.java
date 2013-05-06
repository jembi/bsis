package repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.sms.VerifiedSender;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class VerifiedSenderRepository {

  @PersistenceContext
  private EntityManager em;
  
  public boolean isVerifiedSender(String senderName) {
    String queryStr = "SELECT s from VerifiedSender WHERE s.senderName=:senderName AND " +
    		"s.isDeleted=:isDeleted";
    TypedQuery<VerifiedSender> query = em.createQuery(queryStr, VerifiedSender.class);

    boolean senderExists = false;
    
    try {
      VerifiedSender sender = query.getSingleResult();
      if (sender != null)
        senderExists = true;
    } catch (NoResultException ex) {
      ex.printStackTrace();
    } catch (NonUniqueResultException ex) {
      ex.printStackTrace();
    }
    return senderExists;
  }
}
