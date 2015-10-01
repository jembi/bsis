package repository;

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
import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import utils.DonorUtils;
import viewmodel.DonorSummaryViewModel;
import controller.UtilController;

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

    public Donor findDonorById(Long donorId) throws NoResultException{
            String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations  WHERE d.id = :donorId and d.isDeleted = :isDeleted";
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

        String donorSearchMode = utilController.getGeneralConfigValueByName("donor.searchMode");

        if (!usePhraseMatch) {
            firstNameExp = cb.equal(root.<String>get("firstName"), firstName);
            lastNameExp = cb.equal(root.<String>get("lastName"), lastName);
        } else {
            if (firstName.trim().equals("")) {
                firstNameExp = cb.disjunction();
            } else {
                if ("start".equals(donorSearchMode))
                    firstNameExp = cb.like(root.<String>get("firstName"), firstName + "%");
                else if ("end".equals(donorSearchMode))
                    firstNameExp = cb.like(root.<String>get("firstName"), "%" + firstName);
                else
                    firstNameExp = cb.like(root.<String>get("firstName"), "%" + firstName + "%");
            }

            if (lastName.trim().equals("")) {
                lastNameExp = cb.disjunction();
            } else {
                if ("start".equals(donorSearchMode))
                    lastNameExp = cb.like(root.<String>get("lastName"), lastName + "%");
                else if ("end".equals(donorSearchMode))
                    lastNameExp = cb.like(root.<String>get("lastName"), "%" + lastName);
                else
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
                for (Donation donation : donor.getDonations()) {
                    if (donation.getDonationIdentificationNumber().equals(donationIdentificationNumber)) {
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

    public Donor updateDonorDetails(Donor donor) {
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
    
    @Transactional(propagation = Propagation.MANDATORY)
    public Donor updateDonor(Donor donor) {
        return em.merge(donor);
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
        String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations  WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
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
     String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations  WHERE d.donorNumber = :donorNumber";
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

        DateTime dt = new DateTime().toDateMidnight().toDateTime();
        Date today = new Date(dt.getMillis());

        for (DonorDeferral donorDeferral : donorDeferrals) {
            Date deferredUntil = donorDeferral.getDeferredUntil();
            if(deferredUntil.equals(today) || deferredUntil.after(today) && donorDeferral.getIsVoided() != true){
            	return true;
            }            
        }

        return false;
    }

    public boolean isCurrentlyDeferred(Donor donor) {
        List<DonorDeferral> donorDeferrals = getDonorDeferrals(donor.getId());
        return isCurrentlyDeferred(donorDeferrals);
    }

    public Date getLastDonorDeferralDate(Long donorId) {
        List<DonorDeferral> deferrals = getDonorDeferrals(donorId);

        if (deferrals == null || deferrals.isEmpty()) {
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

    public List<DonorCodeGroup> getAllDonorCodeGroups(){

        TypedQuery<DonorCodeGroup> query = em.createQuery(
                "SELECT dcg FROM DonorCodeGroup dcg", DonorCodeGroup.class);
        return query.getResultList();

    }

    public List<DonorCode> findDonorCodesbyDonorCodeGroupById(Long id) throws IllegalArgumentException{

        DonorCodeGroup donorCodeGroup = em.find(DonorCodeGroup.class, id);
        em.flush();
        return donorCodeGroup.getDonorCodes();

    }

    public List<DonorDonorCode> findDonorDonorCodesOfDonorByDonorId(Long donorId){

        TypedQuery<DonorDonorCode> query = em.createQuery(
                "SELECT dc FROM DonorDonorCode dc where donorId = :donorId", DonorDonorCode.class);
        query.setParameter("donorId", em.find(Donor.class, donorId));
        return query.getResultList();
    }

    public DonorCode findDonorCodeById(Long id) throws IllegalArgumentException{

        DonorCode donorCode = em.find(DonorCode.class, id);
        em.flush();
        return donorCode;

    }

    public Donor deleteDonorCode(Long id) throws IllegalArgumentException {
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
    
    public DonorSummaryViewModel findDonorSummaryByDonorNumber(String donorNumber) throws NoResultException {
        return em.createQuery(
                "SELECT NEW viewmodel.DonorSummaryViewModel(d.firstName, d.lastName, d.gender, d.birthDate) " +
                "FROM Donor d " +
                "WHERE d.donorNumber = :donorNumber " +
                "AND d.isDeleted = FALSE ",
                DonorSummaryViewModel.class)
                .setParameter("donorNumber", donorNumber)
                .getSingleResult();
    }

    public List<Donor> findDonorsByNumbers(List<String> donorNumbers) {
    	if (donorNumbers == null || donorNumbers.size() == 0) {
    		return new ArrayList<Donor>();
    	}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
		Root<Donor> donor = cq.from(Donor.class);
		cq.select(donor).where(donor.get("donorNumber").in(donorNumbers));
    	return em.createQuery(cq).getResultList();
    }

	public Donor addMergedDonor(Donor newDonor, List<Donor> mergedDonors, List<DuplicateDonorBackup> backupLogs) {
		for (DuplicateDonorBackup backupLog : backupLogs) {
			em.persist(backupLog);
		}
		for (Donor donor : mergedDonors) {
			em.persist(donor);
		}
		em.persist(newDonor);
		em.flush();
		return newDonor;
	}
}
