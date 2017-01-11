package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.sun.prism.paint.Color;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class stripsController {
	Pane[][] logicBoard=new Pane[20][12];
	Furniture furniture= new Furniture(new Coordinates(3,3),new Coordinates(6,7));
    @FXML
    private Button up;

    @FXML
    private Button down;

    @FXML
    private Button left;

    @FXML
    private Button right;

    @FXML
    void up(ActionEvent event) {
    	for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[j][i].setStyle(null);
    		}
    	}
		logicStrips logic = new logicStrips();
		logic.moveUpandDown(furniture,-1);
		for(int i = furniture.upperLeft.x; i<furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j<furniture.bottomRight.y; j++){
    			logicBoard[j][i].setStyle("-fx-background-color:#dae753;");
    		}
    	}
    }

    @FXML
    void down(ActionEvent event) {
    	for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle(null);
    		}
    	}
		logicStrips logic = new logicStrips();
		logic.moveUpandDown(furniture,1);
		for(int i = furniture.upperLeft.x; i<furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j<furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle("-fx-background-color:#dae753;");
    		}
    	}
    }

    @FXML
    void left(ActionEvent event) {
    	for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle(null);
    		}
    	}
		logicStrips logic = new logicStrips();
		logic.leftrotateFurniture(furniture);
		for(int i = furniture.upperLeft.x; i<furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j<furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle("-fx-background-color:#dae753;");
    		}
    	}
    }

    @FXML
    private Button button;

    @FXML
    void right(ActionEvent event) {
    	for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle(null);
    		}
    	}
		logicStrips logic = new logicStrips();
		logic.rightrotateFurniture(furniture);
		for(int i= furniture.upperLeft.x; i<furniture.bottomRight.x ;i++){
    		for(int j=furniture.upperLeft.y; j<furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle("-fx-background-color:#dae753;");
    		}
    	}
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane board;
    @FXML
    void initialize()
    {
    	for (int i = 0; i < 20;i++) {
    		for (int j = 0;j < 12;j++) {
    			Pane guiItem = new Pane ();
    			logicBoard[i][j] = guiItem;
    			board.add(guiItem, i, j);
    		}
    	}

    	for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle("-fx-background-color:#dae753;");
    		}
    	}


    }
}
