package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.location.LocationType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LocationRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveLocation(Location location) {
    em.persist(location);
    em.flush();
  }
  
  public List<Location> getVenues() {
    return em.createNamedQuery(LocationNamedQueryConstants.NAME_FIND_VENUES, Location.class)
        .setParameter("isVenue", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public List<Location> getProcessingSites() {
    return em.createNamedQuery(LocationNamedQueryConstants.NAME_FIND_PROCESSING_SITES, Location.class)
        .setParameter("isProcessingSite", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public List<Location> getUsageSites() {
    return em.createNamedQuery(LocationNamedQueryConstants.NAME_FIND_USAGE_SITES, Location.class)
        .setParameter("isUsageSite", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }

  public List<Location> getDistributionSites() {
    return em.createNamedQuery(LocationNamedQueryConstants.NAME_FIND_DISTRIBUTION_SITES, Location.class)
        .setParameter("isDistributionSite", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }

  public List<Location> getAllLocations() {
    TypedQuery<Location> query =
        em.createNamedQuery(LocationNamedQueryConstants.NAME_GET_ALL_LOCATIONS, Location.class);
    return query.getResultList();
  }

  public Location getLocation(Long selectedLocationId) {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l FROM Location l where l.id= :locationId",
        Location.class);
    query.setParameter("locationId", selectedLocationId);
    return query.getSingleResult();
  }

  public Location updateLocation(Long locationId, Location location) {
    Location existingLocation = em.find(Location.class, locationId);
    existingLocation.copy(location);
    em.merge(existingLocation);
    em.flush();
    return existingLocation;
  }

  public void deleteLocation(Long locationId) {
    Location existingLocation = em.find(Location.class, locationId);
    existingLocation.setIsDeleted(Boolean.TRUE);
    em.merge(existingLocation);
    em.flush();
  }

  public Location findLocationByName(String locationName) throws NoResultException, NonUniqueResultException {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l FROM Location l where l.name= :locationName",
        Location.class);
    query.setParameter("locationName", locationName);
    Location location = null;
    try {
      location = query.getSingleResult();
    } catch (NoResultException ex) {
    }
    return location;
  }
  
  public boolean verifyLocationExists(Long id) {
    Long count = em.createNamedQuery(LocationNamedQueryConstants.NAME_COUNT_LOCATION_WITH_ID, Long.class)
        .setParameter("id", id)
        .getSingleResult();
    if (count == 1) {
      return true;
    }
    return false;
  }
  
  public List<Location> findLocations(String name, boolean includeSimilarResults, LocationType locationType) {
    // build up Query string
    String queryString = "SELECT l FROM Location l WHERE l.isDeleted = :isDeleted ";
    if (!StringUtils.isBlank(name)) {
      if (includeSimilarResults) {
        queryString = queryString + "AND l.name LIKE :name ";
      } else {
        queryString = queryString + "AND l.name = :name ";
      }
    }
    if (locationType != null) {
      switch(locationType) {
        case VENUE:
          queryString = queryString + "AND l.isVenue = :isVenue ";
          break;
        case PROCESSING_SITE:
          queryString = queryString + "AND l.isProcessingSite = :isProcessingSite ";
          break;
        case DISTRIBUTION_SITE:
          queryString = queryString + "AND l.isDistributionSite = :isDistributionSite ";
          break;
        case TESTING_SITE:
          queryString = queryString + "AND l.isTestingSite = :isTestingSite ";
          break;
        case USAGE_SITE:
          queryString = queryString + "AND l.isUsageSite = :isUsageSite ";
          break;
      }
    }
    queryString = queryString + "ORDER BY l.name ASC";

    // create Query and set parameters
    TypedQuery<Location> query = em.createQuery(queryString, Location.class);
    query.setParameter("isDeleted", false);
    
    if (!StringUtils.isBlank(name)) {
      if (includeSimilarResults) {
        query.setParameter("name", "%"+name+"%");
      } else {
        query.setParameter("name", name);
      }
    }
    
    if (locationType != null) {
      switch(locationType) {
        case VENUE:
          query.setParameter("isVenue", true);
          break;
        case PROCESSING_SITE:
          query.setParameter("isProcessingSite", true);
          break;
        case DISTRIBUTION_SITE:
          query.setParameter("isDistributionSite", true);
          break;
        case TESTING_SITE:
          query.setParameter("isTestingSite", true);
          break;
        case USAGE_SITE:
          query.setParameter("isUsageSite", true);
          break;
      }
    }

    // execute Query
    return query.getResultList();
  }
}
