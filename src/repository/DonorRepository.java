package repository;

import controller.UtilController;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.address.AddressType;
import model.collectedsample.CollectedSample;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donorcodes.DonorCode;
import model.donorcodes.DonorCodeGroup;
import model.donorcodes.DonorDonorCode;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.idtype.IdType;
import model.preferredlanguage.PreferredLanguage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import utils.CustomDateFormatter;
import utils.DonorUtils;

@Repository
@Transactional
public class DonorRepository {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(DonorRepository.class);
    public static final int ID_LENGTH = 12;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UtilController utilController;

    public void saveDonor(Donor donor) {
        em.persist(donor);
        em.flush();
    }

    public Donor deleteDonor(Long donorId){
        Donor existingDonor = findDonorById(donorId);
        existingDonor.setIsDeleted(Boolean.TRUE);
        em.merge(existingDonor);
        em.flush();
        return existingDonor;

    }

    public Donor findDonorById(Long donorId) throws NoResultException{
            String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.collectedSamples  WHERE d.id = :donorId and d.isDeleted = :isDeleted";
            TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
            query.setParameter("isDeleted", Boolean.FALSE);
            return query.setParameter("donorId", donorId).getSingleResult();
    
    }

    public Donor findDonorById(String donorId) {
        if (StringUtils.isBlank(donorId)) {
            return null;
        }
        return findDonorById(Long.parseLong(donorId));
    }

    public List<Donor> findAnyDonor(String donorNumber, String firstName,
            String lastName, Map<String, Object> pagingParams, Boolean usePhraseMatch, String donationIdentificationNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
        Root<Donor> root = cq.from(Donor.class);
        Predicate donorNumberExp = cb.equal(root.<String>get("donorNumber"), donorNumber);
        Predicate firstNameExp, lastNameExp;
        if (!usePhraseMatch) {
            firstNameExp = cb.equal(root.<String>get("firstName"), firstName);
            lastNameExp = cb.equal(root.<String>get("lastName"), lastName);
        } else {
            if (firstName.trim().equals("")) {
                firstNameExp = cb.disjunction();
            } else {
                firstNameExp = cb.like(root.<String>get("firstName"), "%" + firstName + "%");
            }

            if (lastName.trim().equals("")) {
                lastNameExp = cb.disjunction();
            } else {
                lastNameExp = cb.like(root.<String>get("lastName"), "%" + lastName + "%");
            }
        }

        Expression<Boolean> exp2 = cb.conjunction();

        if (!StringUtils.isBlank(donorNumber)) {
            exp2 = cb.and(exp2, donorNumberExp);
        }

        if (!StringUtils.isBlank(firstName)) {
            exp2 = cb.and(exp2, firstNameExp);
        }

        if (!StringUtils.isBlank(lastName)) {
            exp2 = cb.and(exp2, lastNameExp);
        }

        Predicate notDeleted = cb.equal(root.<String>get("isDeleted"), false);
        cq.where(cb.and(notDeleted, exp2));

        int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
        int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

        if (pagingParams.containsKey("sortColumn") && pagingParams.containsKey("sortDirection")) {
            List<Order> order = new ArrayList<Order>();
            if (pagingParams.get("sortDirection").equals("asc")) {
                order.add(cb.asc(root.<String>get((String) pagingParams.get("sortColumn"))));
            } else {
                order.add(cb.desc(root.<String>get((String) pagingParams.get("sortColumn"))));
            }
            cq.orderBy(order);
        }

        TypedQuery<Donor> query = em.createQuery(cq);
        query.setFirstResult(start);
        query.setMaxResults(length);

        CriteriaQuery<Long> countCriteriaQuery = cb.createQuery(Long.class);
        Root<Donor> countRoot = countCriteriaQuery.from(Donor.class);
        countCriteriaQuery.where(cb.and(notDeleted, exp2));
        countCriteriaQuery.select(cb.countDistinct(countRoot));

        TypedQuery<Long> countQuery = em.createQuery(countCriteriaQuery);
        Long totalResults = countQuery.getSingleResult().longValue();
        List<Donor> donorResults = query.getResultList();
        boolean looped = false;
        if (!StringUtils.isBlank(donationIdentificationNumber)) {
            List<Donor> uniqueResult = new ArrayList<Donor>();
            looped = true;
            for (Donor donor : donorResults) {
                for (CollectedSample collectedSample : donor.getCollectedSamples()) {
                    if (collectedSample.getCollectionNumber().equals(donationIdentificationNumber)) {
                        uniqueResult.add(donor);
                        return uniqueResult;
                    }
                }
            }
        }
        if (looped == true) {
            return null;
        }
        //return Arrays.asList(donorResults, totalResults);
        return donorResults;

    }

    public List<Donor> getAllDonors() {
        TypedQuery<Donor> query = em.createQuery(
                "SELECT d FROM Donor d WHERE d.isDeleted = :isDeleted", Donor.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        return query.getResultList();
    }

    public Donor addDonor(Donor donor)throws PersistenceException{
        updateDonorAutomaticFields(donor);
        em.persist(donor);
        em.flush();
        return donor;
    }

    public Donor updateDonor(Donor donor) {
        Donor existingDonor = findDonorById(donor.getId());
        if (existingDonor == null) {
            return null;
        }
        donor.getAddress().setId(existingDonor.getAddress().getId());
        donor.getContact().setId(existingDonor.getContact().getId());
        existingDonor.copy(donor);
        existingDonor.setIsDeleted(false);
        em.merge(existingDonor);
        em.flush();
        return existingDonor;
    }

    public Donor findDonorByNumber(String donorNumber) throws NoResultException{
            String queryString = "SELECT d FROM Donor d WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
            TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
            query.setParameter("isDeleted", Boolean.FALSE);
            return query.setParameter("donorNumber", donorNumber).getSingleResult();
    }

    public List<Donor> findAnyDonorStartsWith(String term)throws NoResultException {

        term = term.trim();
        if (term.length() < 2) {
            return Arrays.asList(new Donor[0]);
        }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
            Root<Donor> root = cq.from(Donor.class);

            Predicate donorNumberExp = cb.like(root.<String>get("donorNumber"), term + "%");
            Predicate firstNameExp;
            if (term.equals("")) {
                firstNameExp = cb.disjunction();
            } else {
                firstNameExp = cb.like(root.<String>get("firstName"), term + "%");
            }

            Predicate lastNameExp;
            if (term.equals("")) {
                lastNameExp = cb.disjunction();
            } else {
                lastNameExp = cb.like(root.<String>get("lastName"), term + "%");
            }
            Expression<Boolean> exp = cb.or(donorNumberExp, firstNameExp, lastNameExp);

            Predicate notDeleted = cb.equal(root.<String>get("isDeleted"), false);
            cq.where(cb.and(notDeleted, exp));
            TypedQuery<Donor> query = em.createQuery(cq);
            List<Donor> donors = query.getResultList();
            if (donors != null && donors.size() > 0) {
                return donors;
            }
            return new ArrayList<Donor>();
   
    }

    public void addAllDonors(List<Donor> donors) {
        for (Donor donor : donors) {
            updateDonorAutomaticFields(donor);
            em.persist(donor);
        }
        em.flush();
    }

    private void updateDonorAutomaticFields(Donor donor) {
        if (donor.getDonorStatus() == null) {
            donor.setDonorStatus(DonorStatus.NORMAL);
        }
        if (donor.getBloodAbo() == null) {
            donor.setBloodAbo("");
        }
        if (donor.getBloodRh() == null) {
            donor.setBloodRh("");
        }
        donor.setDonorHash(DonorUtils.computeDonorHash(donor));
    }

    public Donor findDonorByDonorNumber(String donorNumber, boolean isDelete) {
        Donor donor = null;
        String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.collectedSamples  WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
        TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
        query.setParameter("isDeleted", isDelete);
        try{
        donor = query.setParameter("donorNumber", donorNumber).getSingleResult();
        }
        catch(NoResultException ex){
            return null;
        }
        catch(NonUniqueResultException ex){
            ex.printStackTrace();
        }
       return donor;
    }
    


    /*y
     public Donor findDonorByDonorNumberIncludeDeleted(String donorNumber) {
     String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.collectedSamples  WHERE d.donorNumber = :donorNumber";
     TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
     Donor donor = null;
     try {
     donor = query.setParameter("donorNumber", donorNumber).getSingleResult();
     } catch (NoResultException ex) {    	
     LOGGER.error("could not find record with donorNumber :" + donorNumber);
     LOGGER.error(ex.getMessage());
     }
     return donor;
     }
     */
    public List<PreferredLanguage> getAllLanguages() {
        TypedQuery<PreferredLanguage> query = em.createQuery(
                "SELECT l FROM PreferredLanguage l", PreferredLanguage.class);
        return query.getResultList();
    }

    public List<IdType> getAllIdTypes() {
        TypedQuery<IdType> query = em.createQuery(
                "SELECT id FROM IdType id", IdType.class);
        return query.getResultList();
    }

    public List<DeferralReason> getDeferralReasons() {
        String queryString = "SELECT d from DeferralReason d WHERE d.isDeleted=:isDeleted";
        TypedQuery<DeferralReason> query = em.createQuery(queryString, DeferralReason.class);
        query.setParameter("isDeleted", false);
        return query.getResultList();
    }
    
    public DonorDeferral deferDonor(DonorDeferral deferral)throws PersistenceException{
        em.persist(deferral);
        em.flush();
        return deferral;
    }

    public DonorDeferral updateDeferral(DonorDeferral deferral) {
    	DonorDeferral existingDeferral = findDeferralById(deferral.getId());
        if (existingDeferral == null) {
            return null;
        }
        existingDeferral.copy(deferral);
        em.merge(existingDeferral);
        em.flush();
        return existingDeferral;
    }
    
	public DonorDeferral findDeferralById(Long deferralId) throws NoResultException{
	        String queryString = "SELECT d from DonorDeferral d WHERE "
		            + " d.id = :deferralId AND d.isVoided=:isVoided";
		    TypedQuery<DonorDeferral> query = em.createQuery(queryString, DonorDeferral.class);
	        query.setParameter("deferralId", deferralId);
	        query.setParameter("isVoided", false);
	        return query.getSingleResult();
	}

    public void cancelDeferDonor(Long donorDeferralId) {
        DonorDeferral donorDeferral = getDonorDeferralsId(donorDeferralId);
        if (donorDeferral != null) {
            donorDeferral.setIsVoided(Boolean.TRUE);
            donorDeferral.setVoidedDate(new Date());
            donorDeferral.setVoidedBy(utilController.getCurrentUser());
        }
        em.persist(donorDeferral);
    }

    public DeferralReason findDeferralReasonById(String deferralReasonId) throws NoResultException{
            String queryString = "SELECT d FROM DeferralReason d WHERE "
                    + "d.id = :deferralReasonId AND d.isDeleted=:isDeleted";
            TypedQuery<DeferralReason> query = em.createQuery(queryString, DeferralReason.class);
            query.setParameter("deferralReasonId", Integer.parseInt(deferralReasonId));
            query.setParameter("isDeleted", false);
            return query.getSingleResult();
     
    }

    public List<DonorDeferral> getDonorDeferrals(Long donorId) throws NoResultException{
        String queryString = "SELECT d from DonorDeferral d WHERE "
                + " d.deferredDonor.id=:donorId AND d.isVoided=:isVoided";
        TypedQuery<DonorDeferral> query = em.createQuery(queryString, DonorDeferral.class);
        query.setParameter("donorId", donorId);
        query.setParameter("isVoided", Boolean.FALSE);
        return query.getResultList();
    }

    public boolean isCurrentlyDeferred(List<DonorDeferral> donorDeferrals) {

        if (donorDeferrals == null) {
            return false;
        }

        boolean currentlyDeferred = false;

        DateTime dt = new DateTime().toDateMidnight().toDateTime();
        Date today = new Date(dt.getMillis());

        for (DonorDeferral donorDeferral : donorDeferrals) {
            Date deferredOn = donorDeferral.getCreatedDate();
            Date deferredUntil = donorDeferral.getDeferredUntil();
            if (deferredOn == null || deferredUntil == null) {
                currentlyDeferred = true;
                break;
            }
            if (today.after(deferredOn) && today.before(deferredUntil)) {
                currentlyDeferred = true;
                break;
            }
            if (today.equals(deferredOn) || today.equals(deferredUntil)) {
                currentlyDeferred = true;
                break;
            }
        }

        return currentlyDeferred;
    }

    public boolean isCurrentlyDeferred(Donor donor) {
        List<DonorDeferral> donorDeferrals = getDonorDeferrals(donor.getId());
        return isCurrentlyDeferred(donorDeferrals);
    }

    public Date getLastDonorDeferralDate(Long donorId) {
        List<DonorDeferral> deferrals = getDonorDeferrals(donorId);

        if (deferrals == null) {
            return null;
        }

        Date lastDeferredUntil = deferrals.get(0).getDeferredUntil();
        for (DonorDeferral deferral : deferrals) {
            if (deferral.getDeferredUntil() != null && deferral.getDeferredUntil().after(lastDeferredUntil)) {
                lastDeferredUntil = deferral.getDeferredUntil();
            }
        }
        return lastDeferredUntil;
    }

    public DonorDeferral getDonorDeferralsId(Long donorDeferralsId) {
        String queryString = "SELECT d from DonorDeferral d WHERE "
                + " id=:donorDeferralsId";
        TypedQuery<DonorDeferral> query = em.createQuery(queryString, DonorDeferral.class);
        query.setParameter("donorDeferralsId", donorDeferralsId);
        if (query.getResultList().size() > 0) {
            return query.getSingleResult();
        }
        return null;
    }

  //Donor Code & Code Group Methods
    public void saveDonorCodeGroup(DonorCodeGroup donorCodeGroup) {
        em.persist(donorCodeGroup);
        em.flush();

    }

    public void saveDonorCode(DonorCode donorCode) {

        em.persist(donorCode);
        em.flush();
    }

    public void saveDonorDonorCode(DonorDonorCode donorDonorCode) {

        em.persist(donorDonorCode);
        em.flush();
    }

    public List<DonorCodeGroup> findDonorCodeGroupsByDonorId(Long donorId) {
        Donor donor = em.find(Donor.class, donorId);
        List<DonorCodeGroup> donorCodeGroups = new ArrayList<DonorCodeGroup>();
        List<DonorCode> donorCodes = donor.getDonorCodes();
        DonorCodeGroup donorCodeGroup = null;
        for (DonorCode donorCode : donorCodes) {
            donorCodeGroup = donorCode.getDonorCodeGroup();
            if (!donorCodeGroups.contains(donorCodeGroup)) {
                donorCodeGroups.add(donorCodeGroup);
            }

        }
        return donorCodeGroups;

    }

    public List<DonorCodeGroup> getAllDonorCodeGroups() {

        TypedQuery<DonorCodeGroup> query = em.createQuery(
                "SELECT dcg FROM DonorCodeGroup dcg", DonorCodeGroup.class);
        return query.getResultList();

    }

    public List<DonorCode> findDonorCodesbyDonorCodeGroupById(Long id) {

        DonorCodeGroup donorCodeGroup = em.find(DonorCodeGroup.class, id);
        em.flush();
        return donorCodeGroup.getDonorCodes();

    }

    public List<DonorDonorCode> findDonorDonorCodesOfDonorByDonorId(Long donorId) {

        TypedQuery<DonorDonorCode> query = em.createQuery(
                "SELECT dc FROM DonorDonorCode dc where donorId = :donorId", DonorDonorCode.class);
        query.setParameter("donorId", em.find(Donor.class, donorId));
        return query.getResultList();
    }

    public DonorCode findDonorCodeById(Long id) {

        DonorCode donorCode = em.find(DonorCode.class, id);
        em.flush();
        return donorCode;

    }

    public Donor deleteDonorCode(Long id) {
        DonorDonorCode donorDonorCode = em.find(DonorDonorCode.class, id);
        Donor donor = donorDonorCode.getDonor();
        em.remove(donorDonorCode);
        em.flush();
        return donor;
    }

    /**
     * To be used in adding multiple donor numbers
     * @param idNumber 
     */
   /* public void saveIdNumber(IdNumber idNumber){
        em.persist(idNumber);
    }*/
    
    public List<AddressType> getAllAddressTypes(){
        TypedQuery<AddressType> query = em.createQuery(
                "SELECT addressType FROM AddressType addressType", AddressType.class);
        return query.getResultList();

    }

}
