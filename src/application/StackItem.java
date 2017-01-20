package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.scene.layout.Pane;


public abstract class StackItem {
	Furniture fur;
	logicStrips logic =  logicStrips.getInstance();
	StackItem(Furniture fur){
		this.fur = fur;
	}
	public Furniture getFur(){
		return fur;
		 
	}
	public abstract String toString();
	
}
class Action extends StackItem
{
	direction moveDirection;
	Location destination;
	List<direction> dirList = new ArrayList<direction>();
	int upAndDown;
	int leftAndRight;
	Pane[][] logicBoard;
	Action(Furniture fur , Location loc, Pane[][] logicBoard)
	{
		super(fur);
		this.logicBoard = logicBoard;
		this.destination = loc;
		fillStack();
	}
	public void fillStack(){
		/*for (int i = direction.values().length - 1; i >= 0; i--) {
			dirStack.push(direction.values()[i]); 
		}*/
		int xDiff = destination.upLeft.x- fur.upperLeft.x;
		int yDiff = destination.upLeft.y- fur.upperLeft.y;
		boolean needRotate = (destination.upLeft.x- destination.btRight.x)!=(fur.upperLeft.x- fur.bottomRight.x);
		if (((xDiff>0) && (yDiff>0)) || ((xDiff==0) && (yDiff>0)) || ((xDiff>0) && (yDiff==0))){
				if (xDiff>yDiff){
					if (!needRotate){
						dirList.add(direction.LEFT);
						dirList.add(direction.UP);
						dirList.add(direction.ROTATELEFT);
						dirList.add(direction.ROTATERIGHT);
						dirList.add(direction.DOWN);
						dirList.add(direction.RIGHT);
					}else{
						dirList.add(direction.LEFT);
						dirList.add(direction.UP);
						dirList.add(direction.ROTATELEFT);
						dirList.add(direction.DOWN);
						dirList.add(direction.RIGHT);
						dirList.add(direction.ROTATERIGHT);
					}
				}else{
					if (!needRotate){
							dirList.add(direction.UP);
							dirList.add(direction.LEFT);
							dirList.add(direction.ROTATERIGHT);
							dirList.add(direction.ROTATELEFT);
							dirList.add(direction.RIGHT);
							dirList.add(direction.DOWN);
					}
					else{
						dirList.add(direction.UP);
						dirList.add(direction.LEFT);
						dirList.add(direction.ROTATELEFT);
						dirList.add(direction.ROTATERIGHT);
						dirList.add(direction.RIGHT);
						dirList.add(direction.DOWN);
						dirList.add(direction.ROTATERIGHT);
					}
			}
		}
		else if ((xDiff<0) && (yDiff<0)){
			if (xDiff<yDiff){
				if (!needRotate){
					dirList.add(direction.RIGHT);
					dirList.add(direction.DOWN);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.ROTATELEFT);
					dirList.add(direction.UP);
					dirList.add(direction.LEFT);
				}else{
					dirList.add(direction.RIGHT);
					dirList.add(direction.DOWN);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.UP);
					dirList.add(direction.LEFT);
					dirList.add(direction.ROTATELEFT);
				}
			}else{
				if (!needRotate){
					dirList.add(direction.DOWN);
					dirList.add(direction.RIGHT);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.ROTATELEFT);
					dirList.add(direction.LEFT);
					dirList.add(direction.UP);
					
				}
				else{
					dirList.add(direction.DOWN);
					dirList.add(direction.RIGHT);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.LEFT);
					dirList.add(direction.UP);
					dirList.add(direction.ROTATELEFT);
				}
		}
	}
		else if ((xDiff<0) && (yDiff>0)){
			if (Math.abs(xDiff)>yDiff){
				if (!needRotate){
				dirList.add(direction.RIGHT);
				dirList.add(direction.UP);
				dirList.add(direction.ROTATERIGHT);
				dirList.add(direction.ROTATELEFT);
				dirList.add(direction.DOWN);
				dirList.add(direction.LEFT);
				}else{
				
						dirList.add(direction.RIGHT);
						dirList.add(direction.UP);
						dirList.add(direction.ROTATERIGHT);
						dirList.add(direction.DOWN);
						dirList.add(direction.LEFT);
						dirList.add(direction.ROTATELEFT);
				}
			}else{
				if (!needRotate){
				dirList.add(direction.UP);
				dirList.add(direction.RIGHT);
				dirList.add(direction.ROTATERIGHT);
				dirList.add(direction.ROTATELEFT);
				dirList.add(direction.LEFT);
				dirList.add(direction.DOWN);
				}else{
					dirList.add(direction.UP);
					dirList.add(direction.RIGHT);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.LEFT);
					dirList.add(direction.DOWN);
					dirList.add(direction.ROTATELEFT);
				}
				
			}
			
		}
		else if(((xDiff>0) && (yDiff<0))){
			
			if (Math.abs(yDiff)<xDiff){
				if (!needRotate){
				dirList.add(direction.RIGHT);
				dirList.add(direction.UP);
				dirList.add(direction.ROTATERIGHT);
				dirList.add(direction.ROTATELEFT);
				dirList.add(direction.DOWN);
				dirList.add(direction.LEFT);
				}else{
				
						dirList.add(direction.RIGHT);
						dirList.add(direction.UP);
						dirList.add(direction.ROTATERIGHT);
						dirList.add(direction.DOWN);
						dirList.add(direction.LEFT);
						dirList.add(direction.ROTATELEFT);
				}
			}else{
				if (!needRotate){
				dirList.add(direction.RIGHT);
				dirList.add(direction.UP);
				dirList.add(direction.ROTATERIGHT);
				dirList.add(direction.ROTATELEFT);
				dirList.add(direction.DOWN);
				dirList.add(direction.UP);
				}else{
					dirList.add(direction.RIGHT);
					dirList.add(direction.UP);
					dirList.add(direction.ROTATERIGHT);
					dirList.add(direction.DOWN);
					dirList.add(direction.LEFT);
					dirList.add(direction.ROTATELEFT);
				}
				
			}
		}
	}
		
	
	public void getNextDir(){
		moveDirection = dirList.remove(dirList.size()-1);
	}
	
	public String toString(){
		return "move(" +Integer.toString(fur.ID) + ", "+ moveDirection.toString() + ", " + destination.toString() + ")";
	}
	
	public void makeMove(){
		upAndDown=0;
		leftAndRight=0;
		if (moveDirection == direction.LEFT){
			leftAndRight=-1;
			move();
		}
		else if (moveDirection == direction.RIGHT){
			leftAndRight=1;
			move();
		}
		else if (moveDirection == direction.UP){
			upAndDown=-1;
			move();
		}
		else if (moveDirection == direction.DOWN){
			upAndDown=1;
			move();
		}
		else if (moveDirection == direction.ROTATELEFT){
			leftrotateFurniture();
		}
		else if (moveDirection == direction.ROTATERIGHT){
			rightrotateFurniture();
		}
	}
	
	public void move()
	{
		logic.handleFurniture(logicBoard,fur,null);
		Coordinates newUpLeft=new Coordinates(fur.upperLeft.x+leftAndRight,fur.upperLeft.y+upAndDown);
		Coordinates newBotRight=new Coordinates(fur.bottomRight.x+leftAndRight,fur.bottomRight.y+upAndDown);
		fur.upperLeft = newUpLeft;
		fur.bottomRight = newBotRight;
		logic.handleFurniture(logicBoard,fur,"-fx-background-color:#dae753;");
	}
	public void rightrotateFurniture()
	{
		logic.handleFurniture(logicBoard,fur,null);
		int centerX = (fur.upperLeft.x + fur.bottomRight.x) / 2;
		int centerY = (fur.upperLeft.y + fur.bottomRight.y) / 2;
		Coordinates newUpLeft=new Coordinates(centerX  + (fur.upperLeft.y - centerY),centerY - (centerX - fur.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX  + (fur.bottomRight.y - centerY), centerY + (fur.bottomRight.x - centerX));
		if ((fur.bottomRight.x - fur.upperLeft.x) > (fur.bottomRight.y - fur.upperLeft.y)){
			newUpLeft.x++;
			newBotRight.x++;
		}
		fur.upperLeft = newUpLeft;
		fur.bottomRight = newBotRight;
		System.out.println(fur.upperLeft.x + " " + fur.upperLeft.y);
		
		logic.handleFurniture(logicBoard,fur,"-fx-background-color:#dae753;");
	}

	public void leftrotateFurniture()
	{
		logic.handleFurniture(logic.logicBoard,fur,null);
		int centerX = ((fur.upperLeft.x + fur.bottomRight.x)) / 2;
		int centerY = ((fur.upperLeft.y + fur.bottomRight.y)) / 2;
		Coordinates newUpLeft=new Coordinates(centerX + (fur.upperLeft.y - centerY),centerY - (centerX - fur.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX  + (fur.bottomRight.y - centerY), centerY + (fur.bottomRight.x - centerX));
		if ((fur.bottomRight.x - fur.upperLeft.x) < (fur.bottomRight.y - fur.upperLeft.y)){
			newUpLeft.x--;
			newBotRight.x--;
		}
	
		fur.upperLeft = newUpLeft;
		fur.bottomRight = newBotRight;
		System.out.println(centerX + " " + centerY);
		
		logic.handleFurniture(logic.logicBoard,fur,"-fx-background-color:#dae753;");
	}
}

class PrecondCanMoveWall extends StackItem{
	//boolean isSatisfied;
	direction moveDirection;
	//Coordinates newupleft = new Coordinates() ;
	//Coordinates newdownright = new Coordinates();
	Location dest;
	PrecondCanMoveWall(Furniture fur, direction dir , Location dest) {
		super(fur);
		//isSatisfied = false;
		this.moveDirection = dir;
		this.dest = dest;
		//isSatisfied=checkValidity(dest.upLeft, dest.btRight);
		
	}
	public String toString(){
		return "canMove(" + Integer.toString(fur.ID) + "," + moveDirection.toString()+")";
	}
	
	public boolean isSatisfied(){
		return checkValidity(dest.upLeft, dest.btRight);
	}
	
	/*public void canMove(){
		if (moveDirection == direction.RIGHT){
			newupleft.x=fur.upperLeft.x+1;
			newdownright.x=fur.bottomRight.x+1;
			newupleft.y=fur.upperLeft.y;
			newdownright.y=fur.bottomRight.y;
		}
		else if (moveDirection == direction.LEFT){
			newupleft.x=fur.upperLeft.x-1;
			newdownright.y=fur.bottomRight.x-1;
			newupleft.x=fur.upperLeft.y;
			newdownright.y=fur.bottomRight.y;
		}
		else if (moveDirection == direction.UP){
			newupleft.x=fur.upperLeft.x;
			newdownright.y=fur.bottomRight.x;
			newupleft.x=fur.upperLeft.y-1;
			newdownright.y=fur.bottomRight.y-1;
		}
		else if (moveDirection == direction.DOWN){
			newupleft.x=fur.upperLeft.x;
			newdownright.y=fur.bottomRight.x;
			newupleft.x=fur.upperLeft.y+1;
			newdownright.y=fur.bottomRight.y+1;
		}
		else if (moveDirection == direction.ROTATELEFT){
			int centerX = ((fur.upperLeft.x + fur.bottomRight.x)) / 2;
			int centerY = ((fur.upperLeft.y + fur.bottomRight.y)) / 2;
			Coordinates newupleft=new Coordinates(centerX + (fur.upperLeft.y - centerY),centerY - (centerX - fur.upperLeft.x));
			Coordinates newdownright=new Coordinates(centerX  + (fur.bottomRight.y - centerY), centerY + (fur.bottomRight.x - centerX));
			if ((fur.bottomRight.x - fur.upperLeft.x) < (fur.bottomRight.y - fur.upperLeft.y)){
				newupleft.x--;
				newdownright.x--;
			}
		}
		else if (moveDirection == direction.ROTATERIGHT){
			int centerX = (fur.upperLeft.x + fur.bottomRight.x) / 2;
			int centerY = (fur.upperLeft.y + fur.bottomRight.y) / 2;
			Coordinates newupleft=new Coordinates(centerX  + (fur.upperLeft.y - centerY),centerY - (centerX - fur.upperLeft.x));
			Coordinates newdownright=new Coordinates(centerX  + (fur.bottomRight.y - centerY), centerY + (fur.bottomRight.x - centerX));
			if ((fur.bottomRight.x - fur.upperLeft.x) > (fur.bottomRight.y - fur.upperLeft.y)){
				newupleft.x++;
				newdownright.x++;
			}
		}
		
		
	}*/
	
	
	public boolean checkValidity(Coordinates newupleft, Coordinates newdownright)
	{

		return (newupleft.x<logic.maxCol && newupleft.x>=0 ) && (newdownright.x<logic.maxCol && newdownright.x>=0) &&
				(newupleft.y<logic.maxRow && newupleft.y>=0) &&(newdownright.y<logic.maxRow && newdownright.y>=0) &&
				logic.checkForWalls(newupleft,newdownright,logic.walls) /*&&checkForFurniture(upleft,downright,obstacles*/;
	}
	
}

class PrecondAT extends StackItem{
	
	Location location;
	PrecondAT(Furniture fur, Location loc) {
		super(fur);
		this.location = loc;
		

	}
	public boolean isSatisfied(){
		return ((fur.upperLeft.equals(location.upLeft) )&&(fur.bottomRight.equals(location.btRight)));
	}
	
	
	public String toString(){
		return "isAt(" + Integer.toString(fur.ID) + "," + location.toString()+")";
	}
	
	
}
