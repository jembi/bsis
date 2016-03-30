package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.location.Location;

@Repository
@Transactional
public class LocationRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveLocation(Location location) {
    em.persist(location);
    em.flush();
  }

  public List<Location> getAllLocations() {
    TypedQuery<Location> query = em
        .createQuery("SELECT l FROM Location l", Location.class);

    return query.getResultList();
  }

  public List<Location> getAllUsageSites() {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l from Location l where l.isUsageSite=:isUsageSite and l.isDeleted=:isDeleted",
        Location.class);
    query.setParameter("isUsageSite", true);
    query.setParameter("isDeleted", false);
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

  public List<String> getAllUsageSitesAsString() {
    List<Location> locations = getAllUsageSites();
    List<String> locationNames = new ArrayList<String>();
    for (Location l : locations) {
      if (l.getIsUsageSite())
        locationNames.add(l.getName());
    }
    return locationNames;
  }

  public List<Location> getAllVenues() {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l from Location l where l.isVenue=:isVenue and l.isDeleted=:isDeleted",
        Location.class);
    query.setParameter("isVenue", true);
    query.setParameter("isDeleted", false);
    return query.getResultList();
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
}
