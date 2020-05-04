package it.polito.tdp.extflightdelays.db;

import java.sql.*;
import java.util.*;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.AirportCouples;
import it.polito.tdp.extflightdelays.model.Flight;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllAirports(Map<Integer, Airport> airportIdMap) {
		String sql = "SELECT * FROM airports";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!airportIdMap.containsKey(rs.getInt("ID")))
				{
					Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
							rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
							rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
					airportIdMap.put(airport.getAirportId(), airport);
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<AirportCouples> getAirportCouples(Map<Integer, Airport> airportIdMap) {
		String sqlMin = "SELECT ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID, AVG(DISTANCE) AS dist, COUNT(*) AS num " + 
				"FROM flights WHERE ORIGIN_AIRPORT_ID < DESTINATION_AIRPORT_ID " + 
				"GROUP BY ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID";
		String sqlMag = "SELECT ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID, AVG(DISTANCE) AS dist, COUNT(*) AS num " + 
				"FROM flights WHERE ORIGIN_AIRPORT_ID > DESTINATION_AIRPORT_ID " + 
				"GROUP BY ORIGIN_AIRPORT_ID, DESTINATION_AIRPORT_ID";
		List<AirportCouples> result = new LinkedList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			
			PreparedStatement stMin = conn.prepareStatement(sqlMin);
			ResultSet rsMin = stMin.executeQuery();

			while (rsMin.next()) {
				Airport origin = airportIdMap.get(rsMin.getInt("ORIGIN_AIRPORT_ID"));
				Airport destination = airportIdMap.get(rsMin.getInt("DESTINATION_AIRPORT_ID"));
				String coupleId = ""+origin.getAirportId()+"-"+destination.getAirportId();
				AirportCouples ac = new AirportCouples(origin, destination, coupleId, rsMin.getDouble("dist"), rsMin.getInt("num"));
				result.add(ac);
			}
			
			stMin.close();
			rsMin.close();
			
			PreparedStatement stMag = conn.prepareStatement(sqlMag);
			ResultSet rsMag = stMag.executeQuery();

			while (rsMag.next()) {
				Airport destination = airportIdMap.get(rsMag.getInt("ORIGIN_AIRPORT_ID"));
				Airport origin = airportIdMap.get(rsMag.getInt("DESTINATION_AIRPORT_ID"));
				String coupleId = ""+origin.getAirportId()+"-"+destination.getAirportId();
				AirportCouples ac2 = new AirportCouples(origin, destination, coupleId, rsMag.getDouble("dist"), rsMag.getInt("num"));
				
				boolean newCouple = true;
				for(AirportCouples ac : result)
				{
					if(ac.equals(ac2))
					{
						ac.updateDistance(ac2);
						newCouple = false;
					}	
				}
				if(newCouple)
					result.add(ac2);
			}
			
			stMag.close();
			rsMag.close();
			
			conn.close();
			
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
}
