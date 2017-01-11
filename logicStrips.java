package application;

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
	 public final int maxCol=20;
	 public final int maxRow=12;


	public void rightrotateFurniture(Furniture furniture)
	{
		int centerX = (furniture.upperLeft.x + furniture.bottomRight.x) / 2;
		int centerY = (furniture.upperLeft.y + furniture.bottomRight.y) / 2;
		Coordinates newUpLeft=new Coordinates(centerX + 1 + (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX + 1 + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
		if (checkValidity(newUpLeft,newBotRight)){
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
			System.out.println(furniture.upperLeft.x + " " + furniture.upperLeft.y);
		}
	}

	public void leftrotateFurniture(Furniture furniture)
	{
		int centerX = ((furniture.upperLeft.x + furniture.bottomRight.x)) / 2;
		int centerY = ((furniture.upperLeft.y + furniture.bottomRight.y)) / 2;
		Coordinates newUpLeft=new Coordinates(centerX -1+ (furniture.upperLeft.y - centerY),centerY - (centerX - furniture.upperLeft.x));
		Coordinates newBotRight=new Coordinates(centerX -1 + (furniture.bottomRight.y - centerY), centerY + (furniture.bottomRight.x - centerX));
		if (checkValidity(newUpLeft,newBotRight)){
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
			System.out.println(centerX + " " + centerY);
		}
	}
	public void moveUpandDown(Furniture furniture,int x)
	{
		Coordinates newUpLeft=new Coordinates(furniture.upperLeft.x,furniture.upperLeft.y+x);
		Coordinates newBotRight=new Coordinates(furniture.bottomRight.x,furniture.bottomRight.y+x);
		if (checkValidity(newUpLeft,newBotRight)){
			furniture.upperLeft = newUpLeft;
			furniture.bottomRight = newBotRight;
		}

	}
	public boolean checkValidity(Coordinates upleft,Coordinates downright){

		return (upleft.x<=this.maxCol && upleft.x>=0 ) && (downright.x<=this.maxCol && downright.x>=0) &&
				(upleft.y<=this.maxRow && upleft.y>=0) &&(downright.y<=this.maxRow && downright.y>=0);

	}

}
