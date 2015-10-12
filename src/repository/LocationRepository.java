package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.location.Location;

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

  public void deleteAllLocations() {
    Query query = em.createQuery("DELETE FROM Location l");
    query.executeUpdate();
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

  public Long getIDByName(String name) {
    List<Location> locations = getAllLocations();
    for (Location l : locations) {
      if (l.getName().equals(name))
        return l.getId();
    }
    return (long) -1;
  }

  public void saveAllLocations(List<Location> locations) {
    for (Location location : locations) {
      if (location.getId() == null) {
        location.setIsDeleted(false);
        em.persist(location);
      }
      else {
        Location existingLocation = em.find(Location.class, location.getId());
        if (existingLocation != null) {
          existingLocation.setIsDeleted(false);
          existingLocation.copy(location);
          em.merge(existingLocation);
        }
      }
    }
    em.flush();
  }

  public List<Location> getAllVenues() {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l from Location l where l.isVenue=:isVenue and l.isDeleted=:isDeleted",
        Location.class);
    query.setParameter("isVenue", true);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public Location findLocationByName(String locationName) throws NoResultException, NonUniqueResultException{
    TypedQuery<Location> query = em.createQuery(
        "SELECT l FROM Location l where l.name= :locationName and l.isDeleted= :isDeleted",
        Location.class);
      query.setParameter("isDeleted", false);
      query.setParameter("locationName", locationName);
      Location location = null;
      location = query.getSingleResult();
      return location;
  }
}
