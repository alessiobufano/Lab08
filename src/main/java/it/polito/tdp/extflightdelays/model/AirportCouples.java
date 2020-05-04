package it.polito.tdp.extflightdelays.model;

public class AirportCouples {
	
	private Airport origin;
	private Airport destination;
	private String coupleId;
	private Double distance;
	private int numberOfCouples;
	
	public AirportCouples(Airport origin, Airport destination, String coupleId, Double distance, int numberOfCouples) {
		this.origin = origin;
		this.destination = destination;
		this.coupleId = coupleId;
		this.distance = distance;
		this.numberOfCouples = numberOfCouples;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int getNumberOfCouples() {
		return numberOfCouples;
	}

	public void setNumberOfCouples(int numberOfCouples) {
		this.numberOfCouples = numberOfCouples;
	}
	
	public String getCoupleId() {
		return coupleId;
	}

	public void setCoupleId(String coupleId) {
		this.coupleId = coupleId;
	}

	public void updateDistance(AirportCouples ac2) {
		this.distance = (this.distance*this.numberOfCouples + ac2.distance*ac2.numberOfCouples)/(this.numberOfCouples + ac2.numberOfCouples);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coupleId == null) ? 0 : coupleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AirportCouples other = (AirportCouples) obj;
		if (coupleId == null) {
			if (other.coupleId != null)
				return false;
		} else if (!coupleId.equals(other.coupleId))
			return false;
		return true;
	}
	
	
	
	
}
