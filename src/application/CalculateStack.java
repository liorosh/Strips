package application;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Random;
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
		while (!opStack.isEmpty()/* && (counter==0)*/){
			counter =1;
			if (opStack.peek() instanceof  PrecondAT){
				PrecondAT precond = ((PrecondAT)opStack.peek());
				if (precond.isSatisfied()){
					/*if (precond.fur.isInPlace()&& (opStack.size() <= logic.furnitures.size())){
						logic.furnitures.remove(precond.fur);
						logic.furnitures.add(0, precond.fur);
						opStack.clear();
						for(Furniture fur: logic.furnitures){
							PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
							pushAndDisp(prec);
						}
						
					}else{*/
					opStack.pop();
					popAndDisp();
					continue;
					//}
				}
				else{
					Action move = new Action(opStack.peek().fur, ((PrecondAT)opStack.peek()).location, logicBoard, ((PrecondAT)opStack.peek()).from);
					move.getNextDir();
					pushAndDisp(move);
					pushPreConds(move);
					continue;
				}
			}
			/*if (opStack.peek() instanceof  PrecondCanMoveFur){
				PrecondCanMoveFur precond = (PrecondCanMoveFur)opStack.peek();
				Furniture furn = (precond.fur);
				Furniture fur = ((PrecondCanMoveFur)opStack.peek()).isOverlapFur();
				
				if (fur!=null){
					fur.timesBuped++;
					if (fur.timesBuped<10){
						popToNextMove();
						//opStack.pop();
						//popAndDisp();
						//popToNextMove();
						return;
					}else{
						fur.timesBuped = 0;
					}
					Location loc = findNextFurLoc(fur , ((PrecondCanMoveFur)opStack.peek()).dir, furn);
					while (!logic.checkValidity(loc.upLeft, loc.btRight)){
						loc = findNextFurLoc(fur , ((PrecondCanMoveFur)opStack.peek()).dir ,furn);
						if (loc == null){
							while (!(opStack.peek()instanceof Action)){
								opStack.pop();
								popAndDisp();
							}
							((Action)opStack.peek()).getNextDir();
							pushPreConds((Action)opStack.peek());
							return;
						}
					}
					if (loc == null){
						popToNextMove();
					}else{
						fur.timesBuped = 0;
						Coordinates coaard1 = new Coordinates(furn.finalUpperLeft.x, furn.finalUpperLeft.y); 
						Coordinates coaard2 = new Coordinates(furn.finalBottomRight.x, furn.finalBottomRight.y);
						Location location  = new Location(coaard1, coaard2);
						//go back times
						PrecondAT precondition = new PrecondAT(furn, location, null);
						int last;
						if ((last=opStack.search(precondition))>0){
							for (int i =opStack.size(); i>last; i--)
							{
								opStack.pop();
								popAndDisp();
							}
						}
						PrecondAT prec = new PrecondAT(fur, loc,null);
						pushAndDisp(prec);
						if (opStack.size()>((Math.max(fur.hight, fur.width)+ Math.max(furn.hight, furn.width)+logic.furnitures.size())*2)){
							while (!(opStack.peek()instanceof Action)){
								opStack.pop();
								popAndDisp();
							}
						for ( int i = 0 ; i<Math.max(fur.hight, fur.width)+ Math.max(furn.hight, furn.width); i++){ 
							opStack.pop();
							popAndDisp();
							
							while (!(opStack.peek()instanceof Action)){
								opStack.pop();
								popAndDisp();
							}
							
						}
						
						pushPreConds((Action)opStack.peek());
						
						}else{
							popToNextMove();
						}
						return;
					}
				}else{
					opStack.pop();
					popAndDisp();
					continue;
				}
			}*/
			
			/*if (opStack.peek() instanceof  PrecondCanMoveFur){
				PrecondCanMoveFur precond = (PrecondCanMoveFur)opStack.peek();
				Furniture furn = (precond.fur);
				Furniture fur = ((PrecondCanMoveFur)opStack.peek()).isOverlapFur();
				if (fur==null){
					opStack.pop();
					popAndDisp();
				}else{
				}
			}*/
			if (opStack.peek() instanceof  PrecondCanMoveFur){
				if (((PrecondCanMoveFur)opStack.peek()).isOverlapFur()==null){
					opStack.pop();
					popAndDisp();
				}else{
					popToNextMove();
				}
			}
			
			if (opStack.peek() instanceof  PrecondCanMoveWall){
				if (((PrecondCanMoveWall)opStack.peek()).isSatisfied()){
					opStack.pop();
					popAndDisp();
				}else{
					popToNextMove();
				}
			}
			if (opStack.peek() instanceof  IsValidPlace){
				if (((IsValidPlace)opStack.peek()).isSatisfied()){
					opStack.pop();
					popAndDisp();
				}else{
					popToNextMove();
				}
				
			}
			if (opStack.peek() instanceof  Action){
				((Action)opStack.pop()).makeMove();
				popAndDisp();
				continue;
			}
			
		}
		if (opStack.isEmpty()){
			for(Furniture fur: logic.furnitures){
				if (!fur.isInPlace()){
					PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
					pushAndDisp(prec);
					logic.furNotInPlace++;
				}
			
			}
			if (logic.furNotInPlace == 0){
				System.out.println("Yaaayyyy");
				return;
			}
			startCalculations(0);
			
		}
		System.out.println("finished");
	}
	
	
	public Location findNextFurLoc(Furniture fur , direction dir, Furniture furToMove){
		Location loc= new Location();
		loc.upLeft = new Coordinates();
		loc.btRight = new Coordinates();
		loc.upLeft.x = fur.upperLeft.x;
		loc.upLeft.y = fur.upperLeft.y;
		loc.btRight.x = fur.bottomRight.x;
		loc.btRight.y = fur.bottomRight.y;
		fur.unplannedMove++;
		int amount=0;
		ArrayList<Integer> directions = new ArrayList<Integer>();
		directions.add(1);
		directions.add(2);
		directions.add(3);
		directions.add(4);
		if (dir == direction.LEFT || dir == direction.ROTATELEFT){
			if ((fur.unplannedMove%4)==0){
			loc.upLeft.x-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
			if ((fur.unplannedMove%4)==1){
			loc.upLeft.y+= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			if ((fur.unplannedMove%4)==2){
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			loc.upLeft.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			}
			if ((fur.unplannedMove%4)==3){
			logic.leftRotate(loc);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			return null;
			
			
		}
		if (dir == direction.RIGHT || dir == direction.ROTATERIGHT){
			if ((fur.unplannedMove%4)==0){
			loc.upLeft.x+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
			if ((fur.unplannedMove%4)==1){
			loc.upLeft.y+= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			if ((fur.unplannedMove%4)==2){
			loc.upLeft.y-= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			if ((fur.unplannedMove%4)==3){
			logic.RightRotate(loc);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			
			return null;
		}
		if (dir == direction.UP){
			if ((fur.unplannedMove%4)==0){
			loc.upLeft.y-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
			if ((fur.unplannedMove%4)==1){
			loc.upLeft.x+= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			if ((fur.unplannedMove%4)==2){
			loc.upLeft.x-= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
	
			if ((fur.unplannedMove%4)==3){
				logic.RightRotate(loc);
				if (logic.checkValidity(loc.upLeft, loc.btRight))
					return loc;	
			}
			
			return null;
		}
		if (dir == direction.DOWN || dir == direction.DOWN){
			if ((fur.unplannedMove%4)==0){
			loc.upLeft.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.y+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
			if ((fur.unplannedMove%4)==1){
			loc.upLeft.x+= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x+=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;	
			}
			if ((fur.unplannedMove%4)==2){
			loc.upLeft.x-= Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			loc.btRight.x-=Math.max(fur.hight, fur.width)+ Math.max(furToMove.hight, furToMove.width);
			if (logic.checkValidity(loc.upLeft, loc.btRight))
				return loc;
			}
			if ((fur.unplannedMove%4)==3){
				logic.RightRotate(loc);
				if (logic.checkValidity(loc.upLeft, loc.btRight))
					return loc;	
			}
			
			return null;
		}
		return loc;
		//if (((fur.finalUpperLeft.x - fur.upperLeft.x==0))&&((fur.finalUpperLeft.y - fur.upperLeft.y)==0)){
		/*	if (fur.unplannedMove>2)
				return null;
			int n = directions.get(fur.unplannedMove);
			if (n ==1){
				loc.upLeft.x=loc.upLeft.x+amount;
				loc.btRight.x=loc.btRight.x+amount;
			}
			if (n ==2){
				loc.upLeft.x=loc.upLeft.x-amount;
				loc.btRight.x=loc.btRight.x-amount;
			}
			if (n ==3){
				loc.upLeft.y=loc.upLeft.y-amount;
				loc.btRight.y=loc.btRight.y-amount;
			}
			if (n ==4){
				loc.upLeft.y=loc.upLeft.y+amount;
				loc.btRight.y=loc.btRight.y+amount;
			}
			fur.unplannedMove++;
			return loc;*/
		//}
		/*loc.upLeft.x = (fur.upperLeft.x+fur.finalUpperLeft.x)/2;
		loc.upLeft.y = (fur.upperLeft.y+fur.finalUpperLeft.y)/2;
		loc.btRight.x = (fur.bottomRight.x+fur.finalBottomRight.x)/2;
		loc.btRight.y = (fur.bottomRight.y+fur.finalBottomRight.y)/2;
		/*if ((fur.finalUpperLeft.x - fur.upperLeft.x)<0){
			loc.upLeft.x-=fur.unplannedMove+1;
			loc.btRight.x-=fur.unplannedMove+1;
		}
		else{
			loc.upLeft.x+=fur.unplannedMove+1;
			loc.btRight.x+=fur.unplannedMove+1;
		}
		if((fur.finalUpperLeft.y - fur.upperLeft.y)<0){
			loc.upLeft.y-=fur.unplannedMove+1;
			loc.btRight.y-=fur.unplannedMove+1;
		}else{
			loc.upLeft.y+=fur.unplannedMove+1;
			loc.btRight.y+=fur.unplannedMove+1;
		}
		fur.unplannedMove++;
		*/
		//return loc;
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
			/*if ((move.destination.btRight.x - move.destination.upLeft.x) > (move.destination.btRight.y - move.destination.upLeft.y)){
				loc.upLeft.x--;
				loc.btRight.x--;
			}*/
			oposite = direction.ROTATELEFT;
		}
		else if (move.moveDirection == direction.ROTATELEFT){
			int centerX = (move.destination.upLeft.x + move.destination.btRight.x) / 2;
			int centerY = (move.destination.upLeft.y + move.destination.btRight.y) / 2;
			loc.upLeft = new Coordinates(centerX  + (move.destination.upLeft.y - centerY),centerY - (centerX - move.destination.upLeft.x));
			loc.btRight = new Coordinates(centerX  + (move.destination.btRight.y - centerY), centerY + (move.destination.btRight.x - centerX));
			/*if ((move.destination.btRight.x - move.destination.upLeft.x) < (move.destination.btRight.y - move.destination.upLeft.y)){
				loc.upLeft.x++;
				loc.btRight.x++;
			}*/
			oposite = direction.ROTATERIGHT;
		}
		
		PrecondAT precond = new PrecondAT(move.fur, loc, oposite);
		int last;
		if ((last=opStack.search(precond))>0){
			System.out.println("gfgfh");
			popToNextMove();
			/*for (int i = opStack.size(); i>=last; i--){
				opStack.pop();
				popAndDisp();
			}*/
			/*while (!(opStack.peek()instanceof Action)){
				opStack.pop();
				popAndDisp();
			}
			((Action)opStack.peek()).getNextDir();
			pushPreConds(((Action)opStack.peek()));*/
			return;
		}
		
		pushAndDisp(precond);
		pushAndDisp(new PrecondCanMoveFur(move.fur,loc , move.moveDirection));
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
	
	public void popToNextMove(){
		while (!(opStack.peek()instanceof Action)){
			opStack.pop();
			popAndDisp();
		}
		Action act = ((Action)opStack.peek());
		while (!act.getNextDir()){
			opStack.pop();
			popAndDisp();
			/*if (opStack.size()<= logic.furnitures.size()){
				logic.furnitures.add(0, logic.furnitures.get(logic.furnitures.size()-1));
				logic.furnitures.remove(logic.furnitures.size()-1);
				for(Furniture fur: logic.furnitures){
					PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
					pushAndDisp(prec);
				}
				return;
			}*/
			while (!(opStack.peek()instanceof Action)){
				opStack.pop();
				popAndDisp();
			}
			act = ((Action)opStack.peek());
		}
		opStack.pop();
		popAndDisp();
		pushAndDisp(act);
		pushPreConds(act);
	}
}
