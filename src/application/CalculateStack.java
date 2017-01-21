package application;

import java.awt.EventQueue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

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
			PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
			pushAndDisp(prec);
		}
		startCalculations(0);
	}
	

	public void startCalculations(int counter){
		while (!opStack.isEmpty() && (counter==0)){
			counter =1;
			if (opStack.peek() instanceof  PrecondAT){
				if (((PrecondAT)opStack.peek()).isSatisfied()){
					opStack.pop();
					popAndDisp();
					continue;
				}
				else{
					Action move = new Action(opStack.peek().fur, ((PrecondAT)opStack.peek()).location, logicBoard, ((PrecondAT)opStack.peek()).from);
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
			if (opStack.peek() instanceof  IsValidPlace){
				if (((IsValidPlace)opStack.peek()).isSatisfied()){
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
		direction oposite = null;
		loc.upLeft = new Coordinates();
		loc.btRight = new Coordinates();
		if (move.moveDirection == direction.RIGHT){
			loc.upLeft.x=move.destination.upLeft.x-1;
			loc.btRight.x=move.destination.btRight.x-1;
			loc.upLeft.y=move.destination.upLeft.y;
			loc.btRight.y=move.destination.btRight.y;
			oposite = direction.LEFT;
		}
		else if (move.moveDirection == direction.LEFT){
			loc.upLeft.x=move.destination.upLeft.x+1;
			loc.btRight.x=move.destination.btRight.x+1;
			loc.upLeft.y=move.destination.upLeft.y;
			loc.btRight.y=move.destination.btRight.y;
			oposite = direction.RIGHT;
		}
		else if (move.moveDirection == direction.DOWN){
			loc.upLeft.x=move.destination.upLeft.x;
			loc.btRight.x=move.destination.btRight.x;
			loc.upLeft.y=move.destination.upLeft.y-1;
			loc.btRight.y=move.destination.btRight.y-1;
			oposite = direction.UP;
			
		}
		else if (move.moveDirection == direction.UP){
			loc.upLeft.x=move.destination.upLeft.x;
			loc.btRight.x=move.destination.btRight.x;
			loc.upLeft.y=move.destination.upLeft.y+1;
			loc.btRight.y=move.destination.btRight.y+1;
			oposite = direction.DOWN;
		}
		else if (move.moveDirection == direction.ROTATERIGHT ){
			int centerX = ((move.destination.upLeft.x + move.destination.btRight.x)) / 2;
			int centerY = ((move.destination.upLeft.y + move.destination.btRight.y)) / 2;
			loc.upLeft=new Coordinates(centerX + (move.destination.upLeft.y - centerY),centerY - (centerX - move.destination.upLeft.x));
			loc.btRight=new Coordinates(centerX  + (move.destination.btRight.y - centerY), centerY + (move.destination.btRight.x - centerX));
			if ((move.destination.btRight.x - move.destination.upLeft.x) < (move.destination.btRight.y - move.destination.upLeft.y)){
				loc.upLeft.x--;
				loc.btRight.x--;
			}
			oposite = direction.ROTATELEFT;
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
			oposite = direction.ROTATERIGHT;
		}
		
		PrecondAT precond = new PrecondAT(move.fur, loc, oposite);
		int last;
		if ((last=opStack.search(precond))>0){
			System.out.println("gfgfh");
			/*for (int i = opStack.size(); i>=last; i--){
				opStack.pop();
				popAndDisp();
			}*/
			while (!(opStack.peek()instanceof Action)){
				opStack.pop();
				popAndDisp();
			}
			((Action)opStack.peek()).getNextDir();
			pushPreConds(((Action)opStack.peek()));
			return;
		}
		pushAndDisp(precond);
		pushAndDisp(new PrecondCanMoveWall(move.fur,move.moveDirection, move.destination));
		pushAndDisp(new IsValidPlace(loc));
		
	}
	
	public void pushAndDisp(StackItem Item){
		opStack.push(Item);
		this.stackItems.add(0, Item.toString());
		this.dispStack.setItems(this.stackItems);
		//this.dispStack.getItems().remove(0,this.dispStack.getItems().size());
		//this.dispStack.setItems(this.stackItems);
		
		
		/*try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
	
		}*/
		
	}
	public void popAndDisp(){
		
		this.stackItems.remove(0);
		this.dispStack.setItems(this.stackItems);
		/*this.dispStack.getItems().remove(0);
	
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
		
		
		}*/
	}
}
