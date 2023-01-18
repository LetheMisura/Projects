package Java.Aircraft_Game;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject
{
    private int life;                 
	private int doubleFire;         
	private BufferedImage[] images;
	private int index;
	
	public Hero()
	{
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		life = 3;
		doubleFire = 0;
		images = new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		index = 0;
	}
    	
	public void step()
    {
		//every 100ms one switch
		image = images[index++ / 10 % images.length];		
		/*
		index++;
		int a = index / 10;
		int b = a % 2;
		image = images[b];
		*/
	}
	
	public Bullet[] shoot()
    {
		int xStep = this.width / 4;
		if(doubleFire > 0)
        {   
			// double shot
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(this.x + 1 * xStep, this.y - 20);
			bullets[1] = new Bullet(this.x + 3 * xStep, this.y - 20);
			doubleFire -= 2;      //shooting double fire every time decrease by 2 which is double fire time left
			return bullets;
		}
		else
		{                        
			//single fire
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(this.x + 2 * xStep,this.y - 20);
			return bullets;
		}
	}
    
	public void moveTo(int x, int y)
	{
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}

	public boolean outOfBounds()
    {
		return false;
	}

    public void addLife()
    {
    	life++;
    }

    public int getLife()
    {
    	return life;
    }
    
    public void addDoubleFire()
    {
    	doubleFire += 40;
    }

    // clear double fire time 
    public void setDoubleFire(int doubleFire)
    {
    	this.doubleFire = doubleFire;
    }
    
    // when hero hits enemy
    public boolean hit(FlyingObject other)
    {
    	int x1 = other.x - this.width/2;
    	int x2 = other.x + other.width + this.width/2;
    	int y1 = other.y - this.height/2;
    	int y2 = other.y + other.height + this.height/2;
    	int hx = this.x + this.width/2;
    	int hy = this.y + this.height/2;
    	return hx > x1 && hx < x2
    			&&
    		   hy > y1 && hy < y2;
    }

    public void subtractLife()
	{
    	life--;
    }    
}
