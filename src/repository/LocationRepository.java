package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Location;

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

	public List<Location> getAllCenters() {
		Query query = em
				.createQuery("SELECT l FROM Location l where l.isCenter=true and l.isDeleted= :isDeleted");
		query.setParameter("isDeleted", Boolean.FALSE);
		return query.getResultList();
	}

	public List<Location> getAllCollectionSites() {
		Query query = em
				.createQuery("SELECT l FROM Location l where l.isCollectionSite=true and l.isDeleted= :isDeleted");
		query.setParameter("isDeleted", Boolean.FALSE);
		return query.getResultList();
	}

	public List<Location> getAllUsageSites() {
		Query query = em
				.createQuery("SELECT l FROM Location l where l.isUsageSite=true and l.isDeleted= :isDeleted");
		query.setParameter("isDeleted", Boolean.FALSE);
		return query.getResultList();
	}

	public Location getLocation(Long selectedLocationId) {
		try {
			TypedQuery<Location> query = em
					.createQuery(
							"SELECT l FROM Location l where l.locationId= :locationId and l.isDeleted= :isDeleted",
							Location.class);
			query.setParameter("isDeleted", Boolean.FALSE);
			query.setParameter("locationId", selectedLocationId);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
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
      locationNames.add(l.getName());
    }
    return locationNames;
  }
}
