package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();

		
		model.setGraph(3000.0);
		
		//System.out.print(model.getGraph().edgeSet());
		System.out.print(model.getEdgeList(600.0));
		
	}

}
