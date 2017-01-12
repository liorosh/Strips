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
class Furniture
{
	Coordinates upperLeft;
	Coordinates bottomRight;
	Coordinates finalUpperLeft;
	Coordinates finalBottomRight;
	Coordinates diff;
	boolean needRotate;
	Furniture (Coordinates upleft, Coordinates botright/*,Coordinates fupleft, Coordinates fbotright*/)
	{
		this.upperLeft = upleft;
		this.bottomRight = botright;
		/*this.finalUpperLeft = fupleft;
		this.finalBottomRight = fbotright;
		this.diff.x = fupleft.x - upleft.x;
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
public class logicStrips
{
	ArrayList <Wall> walls= new ArrayList<Wall>();
	ArrayList <Furniture> furnitures= new ArrayList<Furniture>();
	 public final int maxCol = 20;
	 public final int maxRow = 12;

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

}
