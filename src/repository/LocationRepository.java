package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
		Query query = em
				.createQuery("SELECT l FROM Location l where l.isDeleted= :isDeleted");
		query.setParameter("isDeleted", Boolean.FALSE);
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
	  System.out.println("getLocation");
		TypedQuery<Location> query = em.createQuery(
			"SELECT l FROM Location l where l.id= :locationId and l.isDeleted= :isDeleted",
			Location.class);
		query.setParameter("isDeleted", Boolean.FALSE);
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

  public List<String> getAllCentersAsString() {
    List<Location> locations = getAllCenters();
    List<String> locationNames = new ArrayList<String>();
    for (Location l : locations) {
      if (l.getIsCenter())
        locationNames.add(l.getName());
    }
    return locationNames;
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

  public Location getCollectionSiteByName(String name) {
    List<Location> locations = getAllCollectionSites();
    for (Location l : locations) {
      if (l.getName().equals(name))
        return l;
    }
    return null;
  }

  public Location getCenterByName(String name) {
    List<Location> locations = getAllCenters();
    for (Location l : locations) {
      if (l.getName().equals(name))
        return l;
    }
    return null;
  }

  public Long getIDByName(String name) {
    List<Location> locations = getAllLocations();
    for (Location l : locations) {
      if (l.getName().equals(name))
        return l.getId();
    }
    return (long) -1;
  }

  public List<Location> getAllCollectionSites() {
    TypedQuery<Location> query = em.createQuery(
        "SELECT l from Location l where l.isCollectionSite=:isCollectionSite and l.isDeleted=:isDeleted",
        Location.class);
    query.setParameter("isCollectionSite", true);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<Location> getAllCenters() {
    TypedQuery<Location> query = em
        .createQuery("SELECT l FROM Location l where l.isCenter=:isCenter and l.isDeleted= :isDeleted", Location.class);
    query.setParameter("isCenter", true);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
}
