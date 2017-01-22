package application;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

public class stripsController {
	Pane[][] logicBoard=new Pane[20][12];
	logicStrips logic =  logicStrips.getInstance();
	CalculateStack calc;
	ObservableList<String> stackItems= FXCollections.observableArrayList();
	int counter = 0;
    @FXML
    private Button start;

    @FXML
    private TextField x1;

    @FXML
    private TextField y4;

    @FXML
    private TextField x3;

    @FXML
    private TextField x4;

    @FXML
    private TextField y3;

    @FXML
    private TextField y2;

    @FXML
    private TextField y1;

    @FXML
    private TextField x2;

    @FXML
    private ListView<String> stackCalc;

    @FXML
    private Button next;

	@FXML
    private Button up,down,rotateleft,rotateright,moveright,moveleft,addFurniture;

    @FXML
    private GridPane board;

    @FXML
    void left(ActionEvent event)
    {
		//logic.move(logicBoard,furniture,0,-1);
    }

    @FXML
    void right(ActionEvent event)
    {
		//logic.move(logicBoard, furniture,0,1);
    }

    @FXML
    void up(ActionEvent event)
    {
		//logic.move(logicBoard,furniture,-1,0);
    }

    @FXML
    void down(ActionEvent event)
    {
		//logic.move(logicBoard,furniture,1,0);
    }

    @FXML
    void rotateleft(ActionEvent event)
    {
		//logic.leftrotateFurniture(logicBoard,furniture);
    }

    @FXML
    void startFunction(ActionEvent event) {

    }

    @FXML
    void rotateright(ActionEvent event)
    {
		//logic.rightrotateFurniture(logicBoard,furniture);
    }

    @FXML
    void addFurniture(ActionEvent event) {

    	int ix1 = Integer.valueOf(x1.getText());
    	int ix2 = Integer.valueOf(x2.getText());
    	int iy1 = Integer.valueOf(y1.getText());
    	int iy2 = Integer.valueOf(y2.getText());
    	int ix3 = Integer.valueOf(x3.getText());
    	int ix4 = Integer.valueOf(x4.getText());
    	int iy3 = Integer.valueOf(y3.getText());
    	int iy4 = Integer.valueOf(y4.getText());
    	int width1 = Math.abs(ix2-ix1);
    	int hight1 = Math.abs(iy2-iy1);
    	int width2 = Math.abs(ix4-ix3);
    	int hight2 = Math.abs(iy4-iy3);
    	if ((width1==width2 && hight1 == hight2)||(width1 == hight2 && width2 == hight1)){
    	Furniture fur = new Furniture(logic.IDCount, new Coordinates(ix1,iy1),new Coordinates(ix2,iy2),new Coordinates(ix3,iy3),new Coordinates(ix4,iy4));
    	fur.width = width1;
    	fur.hight = hight1;
    	logic.IDCount++;
    	logic.furnitures.add(fur);
    	x1.setText("");
    	x2.setText("");
    	x3.setText("");
    	y4.setText("");
    	x4.setText("");
    	y1.setText("");
    	y2.setText("");
    	y3.setText("");

     	String colorInHex=String.format("#%02x%02x%02x",fur.color.getRed(),fur.color.getGreen(),fur.color.getBlue());
    	logic.handleFurniture(logicBoard,fur, colorInHex);
    	Color borderColor=fur.color.brighter();
    	colorInHex=String.format("#%02x%02x%02x",borderColor.getRed(),borderColor.getGreen(),borderColor.getBlue());
    	logic.handleDestination(logicBoard,ix3,iy3,ix4,iy4,colorInHex);
    	}else{
    		System.out.println("not same size");
    	}
     
    	/*Furniture fur = new Furniture(logic.IDCount, new Coordinates(15,1),new Coordinates(18,3),new Coordinates(1,8),new Coordinates(4,10));
    	int width1 = Math.abs(18-15);
    	int hight1 = Math.abs(3-1);
    	int width2 = Math.abs(4-1);
    	int hight2 = Math.abs(10-8);
    	if ((width1==width2 && hight1 == hight2)||(width1 == hight2 && width2 == hight1)){
    	fur.width = width1;
    	fur.hight = hight1;
    	logic.IDCount++;
    	logic.furnitures.add(fur);
    	logic.handleFurniture(logicBoard,fur, "-fx-background-color:#2E64FE;");
    	}else{
    		System.out.println("not same size");
    	}
    	Furniture fur1 = new Furniture(logic.IDCount, new Coordinates(15,1),new Coordinates(18,3),new Coordinates(1,8),new Coordinates(4,10));
    	int width1 = Math.abs(18-15);
    	int hight1 = Math.abs(3-1);
    	int width2 = Math.abs(4-1);
    	int hight2 = Math.abs(10-8);
    	if ((width1==width2 && hight1 == hight2)||(width1 == hight2 && width2 == hight1)){
    	fur.width = width1;
    	fur.hight = hight1;
    	logic.IDCount++;
    	logic.furnitures.add(fur);
    	logic.handleFurniture(logicBoard,fur, "-fx-background-color:#2E64FE;");
    	}else{
    		System.out.println("not same size");
    	}
    	Furniture fur = new Furniture(logic.IDCount, new Coordinates(15,1),new Coordinates(18,3),new Coordinates(1,8),new Coordinates(4,10));
    	int width1 = Math.abs(18-15);
    	int hight1 = Math.abs(3-1);
    	int width2 = Math.abs(4-1);
    	int hight2 = Math.abs(10-8);
    	if ((width1==width2 && hight1 == hight2)||(width1 == hight2 && width2 == hight1)){
    	fur.width = width1;
    	fur.hight = hight1;
    	logic.IDCount++;
    	logic.furnitures.add(fur);
    	logic.handleFurniture(logicBoard,fur, "-fx-background-color:#2E64FE;");
    	}else{
    		System.out.println("not same size");
    	}Furniture fur = new Furniture(logic.IDCount, new Coordinates(15,1),new Coordinates(18,3),new Coordinates(1,8),new Coordinates(4,10));
    	int width1 = Math.abs(18-15);
    	int hight1 = Math.abs(3-1);
    	int width2 = Math.abs(4-1);
    	int hight2 = Math.abs(10-8);
    	if ((width1==width2 && hight1 == hight2)||(width1 == hight2 && width2 == hight1)){
    	fur.width = width1;
    	fur.hight = hight1;
    	logic.IDCount++;
    	logic.furnitures.add(fur);
    	logic.handleFurniture(logicBoard,fur, "-fx-background-color:#2E64FE;");
    	}else{
    		System.out.println("not same size");
    	}*/

  

    	//System.out.println(String.valueOf(logic.findDistance(new Coordinates(4,8), new Coordinates(7,10))));
    	//Popup pop=new Popup();
    	//pop.is
    	/*Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText("Look, an Information Dialog");
    	alert.setContentText("I have a great meage for you!");

    	alert.showAndWait();*/
    }


    @FXML
    void initialize()
    {
    	for (int i = 0; i < 20;i++)
    	{
    		for (int j = 0;j < 12;j++)
    		{
    			Pane guiItem = new Pane ();
    			logicBoard[i][j] = guiItem;
    			board.add(guiItem, i, j);

    		}
    	}
    	logic.setWalls();

    	//stackCalc = new ListView<String>();
    }



    @FXML
    void Start(ActionEvent event) {
    	calc= new CalculateStack(stackItems, stackCalc,logicBoard);


    	/*stackItems.add("c");
    	stackItems.add("a");
    	stackItems.add("b");
    	stackItems.add
    	System.out.println("fgfgh");
    	stackCalc.setItems(stackItems);*/

    }
    @FXML
    void next(ActionEvent event) {
    	counter=0;
    	calc.startCalculations(counter);
    }

}
