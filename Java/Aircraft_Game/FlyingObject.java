package Java.Aircraft_Game;

import java.awt.image.BufferedImage;

public abstract class FlyingObject 
{
    protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	//airplane steps
	public abstract void step();
	//out of bound way
	public abstract boolean outOfBounds();
	
	//when enemy is hit
	public boolean shootBy(Bullet bullet)
    {
		//this  = enemy       
        //other = bullet
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		int x = bullet.x;
		int y = bullet.y;
		return x > x1 && x < x2
				&&
				y > y1 && y < y2;
	}    
}
