package application;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
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
	boolean stopFlag = true;
	CalculateStack(ObservableList<String> stackItems , ListView<String> dispStack, Pane[][] logicBoard){
		this.stackItems = stackItems;
		this.dispStack = dispStack;
		this.logicBoard = logicBoard;
		for(Furniture fur: logic.furnitures){
			PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
			pushAndDisp(prec);
		}
		new Timer().schedule(
			    new TimerTask() {
			        @Override
			        public void run() {
	        		//calc.startCalculations(0);
			        	Platform.runLater(new Runnable()
						{
							public void run()
							{
								if(stopFlag){
			        	if(!startCalculations(0))
			        	{
			        	 cancel();
			             return;
			             }
							}
							}});
			        }
			    }, 0, 100);
	}
	

	public boolean startCalculations(int counter){
		while (!opStack.isEmpty() && (counter==0)){
			counter =1;
			if (opStack.peek() instanceof  PrecondAT){
				PrecondAT precond = ((PrecondAT)opStack.peek());
				if (precond.isSatisfied()){
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
			if (opStack.peek() instanceof  PrecondCanMoveFur){
				PrecondCanMoveFur precond = (PrecondCanMoveFur)opStack.peek();
				Furniture furnThatMoves = (precond.fur);
				Furniture furnToMove = ((PrecondCanMoveFur)opStack.peek()).isOverlapFur();
	
				if (furnToMove!=null){
					if (!findNextFurLoc(furnToMove , ((PrecondCanMoveFur)opStack.peek()).dir, precond.destination)){
						popToNextMove();
					}
				}else{
					opStack.pop();
					popAndDisp();
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
			logic.furNotInPlace=0;
			for(Furniture fur: logic.furnitures){
				if (!fur.isInPlace()){
					PrecondAT prec = new PrecondAT(fur, new Location(fur.finalUpperLeft, fur.finalBottomRight),null);
					pushAndDisp(prec);
					logic.furNotInPlace++;
				}
			
			}
			if (logic.furNotInPlace == 0){
				System.out.println("Yaaayyyy");
				return false;
			}
			//startCalculations(0);
			
		}
		System.out.println("finished");
		return true;
	}
	
	
	public boolean findNextFurLoc(Furniture furToMove , direction dir, Location furThatMoves){
		if ((dir == direction.DOWN) || (dir == direction.UP)){
			Location locRight= new Location();
			locRight.upLeft = new Coordinates(furToMove.upperLeft.x+(furThatMoves.btRight.x-furToMove.upperLeft.x+1),furToMove.upperLeft.y );
			locRight.btRight = new Coordinates(furToMove.bottomRight.x+(furThatMoves.btRight.x-furToMove.upperLeft.x+1),furToMove.bottomRight.y );
			Location locLeft= new Location();
			locLeft.upLeft = new Coordinates(furToMove.upperLeft.x-(furToMove.bottomRight.x - furThatMoves.upLeft.x+1),furToMove.upperLeft.y );
			locLeft.btRight = new Coordinates(furToMove.bottomRight.x-(furToMove.bottomRight.x - furThatMoves.upLeft.x+1),furToMove.bottomRight.y );
			if (dir == direction.DOWN){
				Location locDown= new Location();
				locDown.upLeft = new Coordinates(furToMove.upperLeft.x, furToMove.upperLeft.y+1);
				locDown.btRight = new Coordinates(furToMove.bottomRight.x, furToMove.bottomRight.y+1);
				List<Location> locationList = new ArrayList<Location>();
				
				if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
					//locationList.add(locDown);
				}
				if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
					locationList.add(locLeft);
				}
				if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
					locationList.add(locRight);
				}
				if (!locationList.isEmpty()){
					PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
					prec.isMoveFur = true;
					prec.locationList=locationList;
					pushAndDisp(prec);
					return true;
				}
				return false;
			
			}
			if (dir == direction.UP){
				Location locUp= new Location();
				locUp.upLeft = new Coordinates(furToMove.upperLeft.x, furToMove.upperLeft.y-1);
				locUp.btRight = new Coordinates(furToMove.bottomRight.x, furToMove.bottomRight.y-1);
				List<Location> locationList = new ArrayList<Location>();
				
				if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
					//locationList.add(locUp);
				}
				if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
					locationList.add(locLeft);
				}
				if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
					locationList.add(locRight);
				}
				if (!locationList.isEmpty()){
					PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
					prec.isMoveFur = true;
					prec.locationList=locationList;
					pushAndDisp(prec);
					return true;
				}
				return false;
			}
		}
		if ((dir == direction.LEFT) || (dir == direction.RIGHT)){
			Location locUp= new Location();
			locUp.upLeft = new Coordinates(furToMove.upperLeft.x,furToMove.upperLeft.y-(furToMove.bottomRight.y - furThatMoves.upLeft.y+1));
			locUp.btRight = new Coordinates(furToMove.bottomRight.x,furToMove.bottomRight.y -(furToMove.bottomRight.y - furThatMoves.upLeft.y+1));
			Location locDown= new Location();
			locDown.upLeft = new Coordinates(furToMove.upperLeft.x,furToMove.upperLeft.y +(furThatMoves.btRight.y -furToMove.upperLeft.y+1 ));
			locDown.btRight = new Coordinates(furToMove.bottomRight.x,furToMove.bottomRight.y + (furThatMoves.btRight.y -furToMove.upperLeft.y +1));
			if (dir == direction.LEFT){
				Location locLeft= new Location();
				locLeft.upLeft = new Coordinates(furToMove.upperLeft.x-1, furToMove.upperLeft.y);
				locLeft.btRight = new Coordinates( furToMove.bottomRight.x-1, furToMove.bottomRight.y);
				List<Location> locationList = new ArrayList<Location>();
				
				if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
					//locationList.add(locLeft);
				}
				if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
					locationList.add(locDown);
				}
				if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
					locationList.add(locUp);
				}
				if (!locationList.isEmpty()){
					PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
					prec.isMoveFur = true;
					prec.locationList=locationList;
					pushAndDisp(prec);
					return true;
				}
				return false;
			}
			if (dir == direction.RIGHT){
				Location locRight= new Location();
				locRight.upLeft = new Coordinates(furToMove.upperLeft.x+1, furToMove.upperLeft.y );
				locRight.btRight = new Coordinates(furToMove.bottomRight.x+1, furToMove.bottomRight.y);
				List<Location> locationList = new ArrayList<Location>();
				
				if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
					//locationList.add(locRight);
				}
				if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
					locationList.add(locDown);
				}
				if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
					locationList.add(locUp);
				}
				if (!locationList.isEmpty()){
					PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
					prec.isMoveFur = true;
					prec.locationList=locationList;
					pushAndDisp(prec);
					return true;
				}
				return false;
			}
		
		}
		
		if ((dir == direction.ROTATELEFT) || (dir == direction.ROTATERIGHT)){
			
			int centerX = ((furThatMoves.upLeft.x + furThatMoves.btRight.x)) / 2;
			int centerY = ((furThatMoves.upLeft.y + furThatMoves.btRight.y)) / 2;
			Coordinates newUpLeft=new Coordinates(centerX + (furThatMoves.upLeft.y - centerY),centerY - (centerX - furThatMoves.upLeft.x));
			Coordinates newBotRight=new Coordinates(centerX  + (furThatMoves.btRight.y - centerY), centerY + (furThatMoves.btRight.x - centerX));
		
			if ((furThatMoves.btRight.x< furToMove.upperLeft.x) || (furThatMoves.upLeft.x> furToMove.bottomRight.x)){
				Location locUp= new Location();
				locUp.upLeft = new Coordinates(furToMove.upperLeft.x,furToMove.upperLeft.y-(furToMove.bottomRight.y - newUpLeft.y+1));
				locUp.btRight = new Coordinates(furToMove.bottomRight.x,furToMove.bottomRight.y -(furToMove.bottomRight.y - newUpLeft.y+1));
				Location locDown= new Location();
				locDown.upLeft = new Coordinates(furToMove.upperLeft.x,furToMove.upperLeft.y +(newBotRight.y -furToMove.upperLeft.y +1));
				locDown.btRight = new Coordinates(furToMove.bottomRight.x,furToMove.bottomRight.y + (newBotRight.y -furToMove.upperLeft.y +1));
				if (furThatMoves.upLeft.x> furToMove.bottomRight.x){
					Location locLeft= new Location();
					locLeft.upLeft = new Coordinates(furToMove.upperLeft.x-centerY-1, furToMove.upperLeft.y);
					locLeft.btRight = new Coordinates( furToMove.bottomRight.x-centerY-1, furToMove.bottomRight.y);
					List<Location> locationList = new ArrayList<Location>();
					
					if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
						//locationList.add(locLeft);
					}
					if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
						locationList.add(locDown);
					}
					if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
						locationList.add(locUp);
					}
					if (!locationList.isEmpty()){
						PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
						prec.isMoveFur = true;
						prec.locationList=locationList;
						pushAndDisp(prec);
						return true;
					}
					return false;
					
				}
				if (furThatMoves.btRight.x< furToMove.upperLeft.x){
					Location locRight= new Location();
					locRight.upLeft = new Coordinates(furToMove.upperLeft.x+centerY+1, furToMove.upperLeft.y );
					locRight.btRight = new Coordinates(furToMove.bottomRight.x+centerY+1, furToMove.bottomRight.y);
					List<Location> locationList = new ArrayList<Location>();
					
					if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
						//locationList.add(locRight);
					}
					if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
						locationList.add(locDown);
					}
					if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
						locationList.add(locUp);
					}
					if (!locationList.isEmpty()){
						PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
						prec.isMoveFur = true;
						prec.locationList=locationList;
						pushAndDisp(prec);
						return true;
					}
					return false;
				}
			}
			if ((furThatMoves.btRight.y< furToMove.upperLeft.y) || (furThatMoves.upLeft.y> furToMove.bottomRight.y)){
				Location locRight= new Location();
				locRight.upLeft = new Coordinates(furToMove.upperLeft.x+(newBotRight.x-furToMove.upperLeft.x),furToMove.upperLeft.y +1);
				locRight.btRight = new Coordinates(furToMove.bottomRight.x+(newBotRight.x-furToMove.upperLeft.x),furToMove.bottomRight.y +1);
				Location locLeft= new Location();
				locLeft.upLeft = new Coordinates(furToMove.upperLeft.x-(furToMove.bottomRight.x - newUpLeft.x),furToMove.upperLeft.y+1 );
				locLeft.btRight = new Coordinates(furToMove.bottomRight.x-(furToMove.bottomRight.x - newUpLeft.x),furToMove.bottomRight.y +1);
				if (dir == direction.DOWN){
					Location locDown= new Location();
					locDown.upLeft = new Coordinates(furToMove.upperLeft.x, furToMove.upperLeft.y+centerX+1);
					locDown.btRight = new Coordinates(furToMove.bottomRight.x, furToMove.bottomRight.y+centerX+1);
					List<Location> locationList = new ArrayList<Location>();
					
					if (logic.checkValidity(locDown.upLeft, locDown.btRight)){
						//locationList.add(locDown);
					}
					if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
						locationList.add(locLeft);
					}
					if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
						locationList.add(locRight);
					}
					if (!locationList.isEmpty()){
						PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
						prec.isMoveFur = true;
						prec.locationList=locationList;
						pushAndDisp(prec);
						return true;
					}
					return false;
				}
				if (dir == direction.UP){
					Location locUp= new Location();
					locUp.upLeft = new Coordinates(furToMove.upperLeft.x, furToMove.upperLeft.y-centerX-1);
					locUp.btRight = new Coordinates(furToMove.bottomRight.x, furToMove.bottomRight.y-centerX-1);
					List<Location> locationList = new ArrayList<Location>();
					
					if (logic.checkValidity(locUp.upLeft, locUp.btRight)){
						//locationList.add(locUp);
					}
					if (logic.checkValidity(locLeft.upLeft, locLeft.btRight)){
						locationList.add(locLeft);
					}
					if (logic.checkValidity(locRight.upLeft, locRight.btRight)){
						locationList.add(locRight);
					}
					if (!locationList.isEmpty()){
						PrecondAT prec = new PrecondAT(furToMove, locationList.remove(locationList.size()-1),null);
						prec.isMoveFur = true;
						prec.locationList=locationList;
						pushAndDisp(prec);
						return true;
					}
					return false;
				}
		
			}
		
		}
		return false;
		
		
		
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
			return;
		}
		pushAndDisp(precond);
		pushAndDisp(new PrecondCanMoveFur(move.fur,move.destination , move.moveDirection));
		pushAndDisp(precond);
		pushAndDisp(new PrecondCanMoveFur(move.fur,loc , move.moveDirection));
		pushAndDisp(new PrecondCanMoveWall(move.fur,move.moveDirection, move.destination));
		pushAndDisp(new IsValidPlace(loc));
		
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
	
	public void popToNextMove(){
		while (!(opStack.peek()instanceof Action)){
			opStack.pop();
			popAndDisp();
		}
		Action act = ((Action)opStack.peek());
		while (!act.getNextDir()){
			opStack.pop();
			popAndDisp();
			while (!(opStack.peek()instanceof Action)){
				opStack.pop();
				popAndDisp();
				/*if (opStack.peek() instanceof PrecondAT){
					PrecondAT prec = (PrecondAT)opStack.peek();
					if (prec.isMoveFur){
						if (!prec.locationList.isEmpty()){
							prec.location = prec.locationList.remove(prec.locationList.size()-1);
							return;
						}
					}
				}*/
			}
			act = ((Action)opStack.peek());
		}
		opStack.pop();
		popAndDisp();
		pushAndDisp(act);
		pushPreConds(act);
	}
}
