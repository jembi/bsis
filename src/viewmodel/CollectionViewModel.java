package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import model.Collection;
import model.Location;

public class CollectionViewModel {
	private Collection collection;
	private List<Location> allCollectionSites;
	private List<Location> allCenters;

	public CollectionViewModel(Collection collection) {
		this.collection = collection;
	}

	public CollectionViewModel(Collection collection,
			List<Location> allCollectionSites, List<Location> allCenters) {

		this.collection = collection;
		this.allCollectionSites = allCollectionSites;
		this.allCenters = allCenters;
	}

	public String getCollectionId() {
		return getStringValue(collection.getCollectionId());
	}

	public String getCenterId() {
		return getStringValue(collection.getCenterId());
	}

	public String getCenterName() {
		Long centerId = collection.getCenterId();
		if (allCenters != null && centerId != null) {
			for (Location center : allCenters) {
				if (center.getLocationId().equals(centerId)) {
					return center.getName();
				}
			}
		}
		return "";
	}

	public String getSiteId() {
		return getStringValue(collection.getSiteId());
	}

	public String getSiteName() {
		Long siteId = collection.getSiteId();
		if (allCollectionSites != null && siteId != null) {
			for (Location site : allCollectionSites) {
				if (site.getLocationId().equals(siteId)) {
					return site.getName();
				}
			}
		}
		return "";
	}

	public String getDateCollected() {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(collection.getDateCollected());
	}

	public String getSampleNumber() {
		return getStringValue(collection.getSampleNumber());
	}

	public String getShippingNumber() {
		return getStringValue(collection.getShippingNumber());
	}

	public String getDonorNumber() {
		return collection.getDonorNumber();

	}

	public String getComment() {
		return collection.getComments();
	}

	public String getDonorType() {
		return collection.getDonorType();
	}

	public String getCollectionNumber() {
		return collection.getCollectionNumber();
	}

	public String getAbo() {
		return collection.getAbo();
	}

	public String getRhd() {
		return collection.getRhd();
	}

	private String getStringValue(Long value) {
		return value == null ? "" : value.toString();
	}
}
