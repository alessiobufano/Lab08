package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	this.txtResult.clear();
    	String text = this.distanzaMinima.getText();
    	Double minimumDistance = null;
    	
    	try {
    		minimumDistance = Double.parseDouble(text);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Error! The minimum distance must be a numeric value!!\n");
    		return;
    	}
    	
    	this.model.setGraph(minimumDistance);
    	this.txtResult.appendText("Graph created with "+this.model.getGraph().vertexSet().size()+" vertexs and "+this.model.getGraph().edgeSet().size()+" edges\n");
    	this.txtResult.appendText(this.model.getEdgeList(minimumDistance));

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
