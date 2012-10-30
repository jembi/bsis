package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Location;
import model.Request;

public class RequestViewModel {
	private Request request;
	private List<Location> sites;

	public RequestViewModel(Request request, List<Location> sites) {
		this.request = request;
		this.sites = sites;
	}

	public RequestViewModel(Request request) {
	  this.request = request;
  }

  public Long getRequestId() {
		return request.getRequestId();
	}

	public String getRequestNumber() {
		return request.getRequestNumber();
	}

	public String getDateRequested() {
		Date dateRequested = request.getDateRequested();
		if (dateRequested != null) {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format(dateRequested);
		}
		return "";
	}

	public String getDateRequired() {
		Date dateRequired = request.getDateRequired();
		if (dateRequired != null) {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format(dateRequired);
		}
		return "";
	}

	public String getSiteId() {
		return getStringValue(request.getSiteId());
	}

	public String getSiteName() {
		Long siteId = request.getSiteId();
		if (sites != null && siteId != null) {
			for (Location site : sites) {
				if (site.getLocationId().equals(siteId)) {
					return site.getName();
				}
			}
		}
		return "";
	}

	public String getProductType() {
		return request.getProductType();
	}

	public String getAbo() {
		return request.getAbo();
	}

	public String getRhd() {
		return request.getRhd();
	}

	public String getQuantity() {
		return getStringValue(request.getQuantity().longValue());
	}

	public String getComments() {
		return request.getComments();
	}

	public String getStatus() {
		return request.getStatus();
	}

	public Boolean getUnfulfilled() {
		if (!request.getStatus().equals("fulfilled")) {
			return true;
		}
		return false;
	}

	private String getStringValue(Long value) {
		return value == null ? "" : value.toString();
	}

	public String getBloodType() {
	  if (request.getAbo() == null || request.getRhd() == null)
	      return "";
	  String rh = request.getRhd().equals("positive") ? "+" : "-"; 
	  return request.getAbo() + rh;
	}

	public String getPatientName() {
    return request.getPatientName();
  }
}
