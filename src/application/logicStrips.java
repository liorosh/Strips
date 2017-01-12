package application;

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
	 public final int maxCol = 20;
	 public final int maxRow = 12;

	public void rightrotateFurniture(Pane[][] logicBoard, Furniture furniture)
	{
		handleFurniture(logicBoard,furniture,null);
		int centerX = (furniture.upperLeft.x + furniture.bottomRight.x) / 2;
		int centerY = (furniture.upperLeft.y + furniture.bottomRight.y) / 2;
		Coordinates newUpLeft=new Coordinates(centerX + 1 + (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX + 1 + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
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
		Coordinates newUpLeft=new Coordinates(centerX -1+ (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX -1 + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
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

		return (upleft.x<=this.maxCol && upleft.x>=0 ) && (downright.x<=this.maxCol && downright.x>=0) &&
				(upleft.y<=this.maxRow && upleft.y>=0) &&(downright.y<=this.maxRow && downright.y>=0);
	}

	public void handleFurniture(Pane[][] logicBoard, Furniture furniture, String color)
	{
		for(int i = furniture.upperLeft.x; i < furniture.bottomRight.x ;i++){
    		for(int j = furniture.upperLeft.y; j < furniture.bottomRight.y; j++){
    			logicBoard[i][j].setStyle(color);
    		}
    	}
	}
}
