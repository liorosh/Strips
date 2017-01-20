package application;

import java.util.Stack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class CalculateStack {
	Stack<StackItem> opStack = new Stack<StackItem>();
	logicStrips logic = logicStrips.getInstance();
	ObservableList<String> stackItems;
	ListView<String> dispStack;
	Pane[][] logicBoard;
	
	CalculateStack(ObservableList<String> stackItems , ListView<String> dispStack, Pane[][] logicBoard){
		this.stackItems = stackItems;
		this.dispStack = dispStack;
		this.logicBoard = logicBoard;
		for(Furniture fur: logic.furnitures){
			PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight));
			pushAndDisp(prec);
		}
		startCalculations();
	}
	

	public void startCalculations(){
		while (!opStack.isEmpty()){
			if (opStack.peek() instanceof  PrecondAT){
				if (((PrecondAT)opStack.peek()).isSatisfied()){
					opStack.pop();
					popAndDisp();
					continue;
				}
				else{
					Action move = new Action(opStack.peek().fur, ((PrecondAT)opStack.peek()).location, logicBoard);
					move.getNextDir();
					pushAndDisp(move);
					pushPreConds(move);
					continue;
				}
			}
			if (opStack.peek() instanceof  PrecondCanMoveWall){
				if (((PrecondCanMoveWall)opStack.peek()).isSatisfied()){
					opStack.pop();
					popAndDisp();
				}else{
					while (!(opStack.peek()instanceof Action)){
						opStack.pop();
						popAndDisp();
					}
					((Action)opStack.peek()).getNextDir();
					pushPreConds((Action)opStack.peek());
				}
			}
			if (opStack.peek() instanceof  Action){
				((Action)opStack.pop()).makeMove();
				popAndDisp();
				continue;
			}
			
		}
		System.out.println("finished");
	}
	
	public void pushPreConds(Action move){
		Location loc = new Location();
		loc.upLeft = new Coordinates();
		loc.btRight = new Coordinates();
		if (move.moveDirection == direction.RIGHT){
			loc.upLeft.x=move.destination.upLeft.x-1;
			loc.btRight.x=move.destination.btRight.x-1;
			loc.upLeft.y=move.destination.upLeft.y;
			loc.btRight.y=move.destination.btRight.y;
		}
		else if (move.moveDirection == direction.LEFT){
			loc.upLeft.x=move.destination.upLeft.x+1;
			loc.btRight.x=move.destination.btRight.x+1;
			loc.upLeft.y=move.destination.upLeft.y;
			loc.btRight.y=move.destination.btRight.y;
		}
		else if (move.moveDirection == direction.DOWN){
			loc.upLeft.x=move.destination.upLeft.x;
			loc.btRight.x=move.destination.btRight.x;
			loc.upLeft.y=move.destination.upLeft.y-1;
			loc.btRight.y=move.destination.btRight.y-1;
		}
		else if (move.moveDirection == direction.UP){
			loc.upLeft.x=move.destination.upLeft.x;
			loc.btRight.x=move.destination.btRight.x;
			loc.upLeft.y=move.destination.upLeft.y+1;
			loc.btRight.y=move.destination.btRight.y+1;
		}
		else if (move.moveDirection == direction.ROTATERIGHT ){
			int centerX = ((move.destination.upLeft.x + move.destination.btRight.x)) / 2;
			int centerY = ((move.destination.upLeft.y + move.destination.btRight.y)) / 2;
			loc.upLeft=new Coordinates(centerX + (move.destination.upLeft.y - centerY),centerY - (centerX - move.destination.upLeft.x));
			loc.btRight=new Coordinates(centerX  + (move.destination.upLeft.y - centerY), centerY + (move.destination.btRight.x - centerX));
			if ((move.destination.btRight.x - move.destination.upLeft.x) < (move.destination.btRight.y - move.destination.upLeft.y)){
				loc.upLeft.x--;
				loc.btRight.x--;
			}
		}
		else if (move.moveDirection == direction.ROTATELEFT){
			int centerX = (move.destination.upLeft.x + move.destination.btRight.x) / 2;
			int centerY = (move.destination.upLeft.y + move.destination.btRight.y) / 2;
			loc.upLeft = new Coordinates(centerX  + (move.destination.upLeft.y - centerY),centerY - (centerX - move.destination.upLeft.x));
			loc.btRight = new Coordinates(centerX  + (move.destination.btRight.y - centerY), centerY + (move.destination.btRight.x - centerX));
			if ((move.destination.btRight.x - move.destination.upLeft.x) > (move.destination.btRight.y - move.destination.upLeft.y)){
				loc.upLeft.x++;
				loc.btRight.x++;
			}
		}
		pushAndDisp(new PrecondCanMoveWall(move.fur,move.moveDirection, move.destination));
		pushAndDisp(new PrecondAT(move.fur, loc));
		
	}
	
	public void pushAndDisp(StackItem Item){
		opStack.push(Item);
		this.stackItems.add(0, Item.toString());
		this.dispStack.setItems(this.stackItems);
	}
	public void popAndDisp(){
		this.stackItems.remove(0);
		this.dispStack.setItems(this.stackItems);
	}
}
