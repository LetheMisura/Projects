package Java.Aircraft_Game;

public class Bullet extends FlyingObject
{
    private int speed = 3;  //bullet steps only y axis is changing
	public Bullet(int x,int y)
    {
        //bullet accordinate change when aircraft change	
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
    public void step()
    {
		y -= speed;
	}
    public boolean outOfBounds()
    {
		return this.y <- this.height;
	}
}
