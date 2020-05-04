package it.polito.tdp.extflightdelays.model;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> airportIdMap;
	private Graph<Airport, DefaultEdge> graph;
	private List<AirportCouples> airportCouples;
	
	public Model() {
		
		this.dao = new ExtFlightDelaysDAO();
		this.airportIdMap = new HashMap<>();
		this.dao.loadAllAirports(airportIdMap);
		this.airportCouples = new LinkedList<>(this.dao.getAirportCouples(airportIdMap));
	}
	
	public void setGraph(Double minimumDistance) {
		
		this.graph = new SimpleWeightedGraph(DefaultWeightedEdge.class);
		
		for(Airport a : this.airportIdMap.values())
			this.graph.addVertex(a);
		
		for(AirportCouples ac : this.airportCouples)
			if(ac.getDistance()>=minimumDistance)
				Graphs.addEdge(this.graph, ac.getOrigin(), ac.getDestination(), ac.getDistance());
	}

	public Graph<Airport, DefaultEdge> getGraph() {
		return graph;
	}

	public String getEdgeList(Double minimumDistance) {
		String list="There are no fligths with an average distance longer than "+minimumDistance+" miles\n";
		if(this.graph.edgeSet().size()>0)
		{
			list="The list of flights with an average distance longer than "+minimumDistance+" miles is: \n";
			for(AirportCouples ac : this.airportCouples)
			{
				if(this.graph.containsEdge(ac.getOrigin(), ac.getDestination()))
				list += ""+"from "+ac.getOrigin().getAirportName()+" to "+ac.getDestination().getAirportName()+", average distance: "+ac.getDistance()+";\n";
			}
		}
		return list;
	}

}
