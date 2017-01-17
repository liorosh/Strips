package application;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

class Coordinates
{
	int x;
	int y;
	Coordinates(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
class Room
{
	Coordinates upLeftCoord;
	Coordinates downRightCoord;
	Door door1;
	Door door2;
	Room (Coordinates upLeft, Coordinates downRight, Door door1, Door door2){
		this.upLeftCoord = upLeft;
		this.downRightCoord = downRight;
		this.door1 = door1;
		this.door2 = door2;
	}
	
}
class Furniture
{
	double disFromEdge;
	Coordinates upperLeft;
	Coordinates bottomRight;
	Coordinates finalUpperLeft;
	Coordinates finalBottomRight;
	Coordinates diff;
	boolean needRotate;
	Furniture (Coordinates upleft, Coordinates botright ,Coordinates fupleft, Coordinates fbotright)
	{
		this.upperLeft = upleft;
		this.bottomRight = botright;
		this.finalUpperLeft = fupleft;
		this.finalBottomRight = fbotright;
		/*this.diff.x = fupleft.x - upleft.x;
		this.diff.y = fbotright.y - botright.y;*/
	}
}
class Wall
{
	Coordinates firstPos;
	Coordinates secondPos;
	Wall(Coordinates first, Coordinates second)
	{
		this.firstPos = first;
		this.secondPos = second;
	}
}
class Door
{
	Coordinates upperLeftPos;
	Coordinates downRightPos;
	Door(Coordinates upperLeft, Coordinates downRight)
	{
		this.upperLeftPos = upperLeft;
		this.downRightPos = downRight;
	}
}
public class logicStrips
{
	ArrayList <Wall> walls= new ArrayList<Wall>();
	ArrayList <Furniture> furnitures= new ArrayList<Furniture>();
	 public final int maxCol = 20;
	 public final int maxRow = 12;
	 public Room room1 = new Room (new Coordinates(0,0), new Coordinates(7,4), new Door (new Coordinates(7,1), new Coordinates(8,3)),  new Door (new Coordinates(2,4), new Coordinates(5,5)));
	 public Room room2 = new Room (new Coordinates(0,5), new Coordinates(7,11), new Door (new Coordinates(2,4), new Coordinates(5,5)),  new Door (new Coordinates(7,5), new Coordinates(8,10)));
	 public Room room3 = new Room (new Coordinates(8,0), new Coordinates(19,11), new Door (new Coordinates(7,1), new Coordinates(8,3)),  new Door (new Coordinates(7,5), new Coordinates(8,10)));
	public void rightrotateFurniture(Pane[][] logicBoard, Furniture furniture)
	{
		handleFurniture(logicBoard,furniture,null);
		int centerX = (furniture.upperLeft.x + furniture.bottomRight.x) / 2;
		int centerY = (furniture.upperLeft.y + furniture.bottomRight.y) / 2;
		Coordinates newUpLeft=new Coordinates(centerX  + (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX  + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
		if ((furniture.bottomRight.x - furniture.upperLeft.x) > (furniture.bottomRight.y - furniture.upperLeft.y)){
			newUpLeft.x++;
			newBotRight.x++;
		}
		if (checkValidity(newUpLeft,newBotRight))
		{
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
			System.out.println(furniture.upperLeft.x + " " + furniture.upperLeft.y);
		}
		handleFurniture(logicBoard,furniture,"-fx-background-color:#dae753;");
	}

	public void leftrotateFurniture(Pane[][] logicBoard, Furniture furniture)
	{
    	handleFurniture(logicBoard,furniture,null);
		int centerX = ((furniture.upperLeft.x + furniture.bottomRight.x)) / 2;
		int centerY = ((furniture.upperLeft.y + furniture.bottomRight.y)) / 2;
		Coordinates newUpLeft=new Coordinates(centerX + (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX  + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
		if ((furniture.bottomRight.x - furniture.upperLeft.x) < (furniture.bottomRight.y - furniture.upperLeft.y)){
			newUpLeft.x--;
			newBotRight.x--;
		}
		if (checkValidity(newUpLeft,newBotRight))
		{
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
			System.out.println(centerX + " " + centerY);
		}
		handleFurniture(logicBoard,furniture,"-fx-background-color:#dae753;");
	}
	public void move(Pane[][] logicBoard, Furniture furniture,int upAndDown,int leftAndRight)
	{
		handleFurniture(logicBoard,furniture,null);
		Coordinates newUpLeft=new Coordinates(furniture.upperLeft.x+leftAndRight,furniture.upperLeft.y+upAndDown);
		Coordinates newBotRight=new Coordinates(furniture.bottomRight.x+leftAndRight,furniture.bottomRight.y+upAndDown);
		if (checkValidity(newUpLeft,newBotRight))
		{
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
		}
		handleFurniture(logicBoard,furniture,"-fx-background-color:#dae753;");
	}
	public boolean checkValidity(Coordinates upleft,Coordinates downright)
	{

		return (upleft.x<this.maxCol && upleft.x>=0 ) && (downright.x<this.maxCol && downright.x>=0) &&
				(upleft.y<this.maxRow && upleft.y>=0) &&(downright.y<this.maxRow && downright.y>=0) &&
				checkForWalls(upleft,downright,walls) /*&&checkForFurniture(upleft,downright,obstacles*/;
	}

	public void handleFurniture(Pane[][] logicBoard, Furniture furniture, String color)
	{
		for(int i = furniture.upperLeft.x; i <= furniture.bottomRight.x ;i++)
		{
    		for(int j = furniture.upperLeft.y; j <= furniture.bottomRight.y; j++)
    		{
    			logicBoard[i][j].setStyle(color);
    		}
    	}
	}

	public void setWalls()
	{
		walls.add(new Wall(new Coordinates(7,0), new Coordinates(8,0)));
    	walls.add(new Wall(new Coordinates(7,4), new Coordinates(8,4)));
    	walls.add(new Wall(new Coordinates(7,5), new Coordinates(8,5)));
    	walls.add(new Wall(new Coordinates(7,11), new Coordinates(8,11)));
    	walls.add(new Wall(new Coordinates(0,4), new Coordinates(0,5)));
    	walls.add(new Wall(new Coordinates(1,4), new Coordinates(1,5)));
    	walls.add(new Wall(new Coordinates(6,4), new Coordinates(6,5)));
    	walls.add(new Wall(new Coordinates(7,4), new Coordinates(7,5)));
	}
	public boolean checkForWalls(Coordinates upleft,Coordinates downright,ArrayList<Wall> walls)
	{
		for(Wall wall: walls){
			if((wall.firstPos.x >= upleft.x && wall.firstPos.y >= upleft.y ) && (wall.secondPos.x <= downright.x && wall.secondPos.y <= downright.y) &&
				(wall.firstPos.x <= downright.x && wall.firstPos.y <= downright.y ) && (wall.secondPos.x >= upleft.x && wall.secondPos.y >= upleft.y))
				return false ;
		}
		return true;
	}
	public boolean checkForFurniture(Coordinates upleft,Coordinates downright, ArrayList<Furniture> obstacles){

		return true;
	}
	
	public boolean at(Furniture fur ,Coordinates upperL, Coordinates bottomR ){
		return ((fur.upperLeft==upperL) && fur.bottomRight==bottomR);
	}
	
	public Room whichRoom(Coordinates upperL, Coordinates bottomR){
		if(upperL.x <= room1.downRightCoord.x && upperL.x >=room1.upLeftCoord.x && bottomR.x <= room1.downRightCoord.x && bottomR.x >=room1.upLeftCoord.x && upperL.y <= room1.downRightCoord.y && upperL.y >=room1.upLeftCoord.y && bottomR.y <= room1.downRightCoord.y && bottomR.y >=room1.upLeftCoord.y ){
			return room1;
		}
		else if (upperL.x <= room2.downRightCoord.x && upperL.x >=room2.upLeftCoord.x && bottomR.x <= room2.downRightCoord.x && bottomR.x >=room2.upLeftCoord.x && upperL.y <= room2.downRightCoord.y && upperL.y >=room2.upLeftCoord.y && bottomR.y <= room2.downRightCoord.y && bottomR.y >=room2.upLeftCoord.y ){
			return room2;
		}
		else if (upperL.x <= room3.downRightCoord.x && upperL.x >=room3.upLeftCoord.x && bottomR.x <= room3.downRightCoord.x && bottomR.x >=room3.upLeftCoord.x && upperL.y <= room3.downRightCoord.y && upperL.y >=room3.upLeftCoord.y && bottomR.y <= room3.downRightCoord.y && bottomR.y >=room3.upLeftCoord.y ){
			return room3;
		}
		else 
			return null;
	}
    public double findDistance (Coordinates coord1, Coordinates coord2){
    	double dis = Math.sqrt((Math.pow((double)(coord2.y - coord1.y),2)) + ( Math.pow((double)(coord2.x - coord1.x),2)));
    	return dis;
    }
}
