package Java.Aircraft_Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * ShootGame
 * Chengmin Zheng
 */
public class ShootGame extends JPanel
{
    public static void main(String[] args) 
    {	
		JFrame frame = new JFrame("Aircraft Game");
		ShootGame game = new ShootGame(); 
		frame.add(game);
    	//BufferedImage image;
		/*
		try 
		{
			image = ImageIO.read(ShootGame.class.getResource("icon.jpg"));
			frame.setIconImage(image);
		} 
		catch (FileNotFoundException e) 
		{
		} 
		catch (IOException e) 
		{
		}  
		*/
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true); // always top
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLocationRelativeTo(null); // inital application location in middle
		frame.setVisible(true); // application can be see

		game.action();
	}

	private static final long serialVersionUID = 9158805858745581422L;
	public static final int WIDTH = 400; // application's width
	public static final int HEIGHT = 654; // application's height
	//no motion
	public static BufferedImage background; 
	//public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	//public static AudioClip music;

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = 0; // current status

	private Hero hero = new Hero();
	private Bullet[] bullets = {};
	private FlyingObject[] flyings = {}; //enemy

	private Timer timer;
	private int intervel = 10; //in ms

	// no motion part
	static 
    {
		try 
        {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			//start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			
			//URL musicPath = ShootGame.class.getResource("game_music.wav");
			//music = Applet.newAudioClip(musicPath);
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
	}

	public static FlyingObject nextOne() 
    {
		Random rand = new Random();
		int type = rand.nextInt(20);
        //if 0, return to bee, else to enemy
		if (type == 0) 
        {
			return new Bee();
		} 
        else 
        {
			return new Airplane();
		}
	}

	int flyEnteredIndex = 0;

	//enemy spawn
	public void enterAction() 
    {
		flyEnteredIndex++; // every 10ms incrase by 1
		if (flyEnteredIndex % 40 == 0) 
        {
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj; // give enemy flying array to last element
		}
	}

	public void stepAction() 
    {
		hero.step(); // hero 1 step
		int num = time1 / 15;
		for (int i = 0; i < flyings.length; i++) 
        {
			for(int j = 0; j <= num; j++) 
            {
				flyings[i].step(); // enemy 1 step
			}
		}
		for (int i = 0; i < bullets.length; i++) 
        {
			for(int j = 0; j <= num/2; j++) 
            {
				bullets[i].step(); // bullet 1 step
			}
		}
	}

	int shootIndex = 0;

	public void shootAction() 
    {
		shootIndex++; // every 10 ms increase by 1
        // 300ms one bullet
		if (shootIndex % 30 == 0) 
        {
			Bullet[] bs = hero.shoot(); // obtain bullet target
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
		}
	}

	// Delete out of bound things
	public void outOfBoundsAction() 
    {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) 
            {
				flyingLives[index] = f;// 不越界，将其装入flyingLives[]数组中
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);

		index = 0;
		Bullet[] bulletsLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) 
        {
			Bullet bs = bullets[i];
			if (!bs.outOfBounds())
            {
				bulletsLives[index] = bs;// 不越界，将其装入flyingLives[]数组中
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletsLives, index);

	}

	int score = 0;
	static double time = 0.00; 
	static int time1 = 0;

	// all bullet to all enemy
	public void bangAction() 
    {
		for (int i = 0; i < bullets.length; i++) 
        {
			bang(bullets[i]);
		}
	}

	// one bullet to all enemy
	public void bang(Bullet b) 
    {
		int index = -1;// 被撞敌人的索引
		for (int i = 0; i < flyings.length; i++) 
        {// 遍历所有的敌人
			if (flyings[i].shootBy(b)) 
            {// Determine if hits
				index = i; // 记录被撞敌人的索引
				break;
			}
		}
		if (index != -1) 
        {// hits
			FlyingObject one = flyings[index];
			if (one instanceof Enemy) 
            {
				Enemy e = (Enemy) one;
				score += e.getScore();
			}
			if (one instanceof Award) 
            {
				Award a = (Award) one;
				int type = a.getType();
				switch (type) {
				case Award.DOUBLE_FIRE: // award fire
					hero.addDoubleFire(); // award upgrade
					break;
				case Award.LIFE: // award life
					hero.addLife(); // award hero life
					break;
				}
			}
            // switch between enemy hits and last element in flying array
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
            // delete random element which is target will be hit
			flyings = Arrays.copyOf(flyings, flyings.length - 1);
		}
	}

	public void checkGameOverAction() 
    {
		if (isGameOver()) 
        {
			state = GAME_OVER;
			time1 = 0;
			time = 0;
		}
	}

	public boolean isGameOver() 
    {
		for (int i = 0; i < flyings.length; i++) 
        { // aircraft crash
			if (hero.hit(flyings[i])) 
            {
				hero.subtractLife(); // life decrease by 1
				hero.setDoubleFire(0); // fire rate decrease

				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}
		return hero.getLife() <= 0; // hero life <= 0, game over
	}

	// initial application
	public void action() 
    {
		MouseAdapter l = new MouseAdapter() 
        {
			public void mouseMoved(MouseEvent e) 
            {
				if (state == RUNNING) // while running 
                { 
					int x = e.getX(); // mouse X axis
					int y = e.getY(); // mouse Y axis
					hero.moveTo(x, y); // hero follow mouse while moving
				}
			}
			// mouse click action
			public void mouseClicked(MouseEvent e) 
            {
				switch (state) {
				case PAUSE:
					state = RUNNING;
					//music.stop();
					//music.loop();
					break;
				case RUNNING:
					state = PAUSE;
					//music.stop();
					break;
				case START:
					state = RUNNING;
					//music.stop();
					//music.loop();
					break;
				case GAME_OVER:
					hero = new Hero();// 清理现场
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					score = 0;
					state = START;
					//music.stop();
					break;
				}
			}

			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}

			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
		};
		this.addMouseListener(l); // Deal mouse action
		this.addMouseMotionListener(l);// Deal mouse moving

		timer = new Timer(); 
		timer.schedule(new TimerTask() {
			public void run() 
            { 
				if (state == RUNNING) // while running
                {
					enterAction();
					stepAction();// plane action
					shootAction();// bullet spawn
					outOfBoundsAction();// delete out bound things
					bangAction(); // bullet hits enemy
					time = time + 0.01;
					time1 = (int)time;
					checkGameOverAction();
				}
				repaint();
			}
		}, intervel, intervel);
	}

	// g: = pen
	public void paint(Graphics g) 
    {
		g.drawImage(background, 0, 0, null); // backgound picture
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintScore(g);
		paintState(g);
	}

	// draw status
	public void paintState(Graphics g) 
    {
		switch (state) 
        {
			/*
		case START: // start picture
			g.drawImage(start, 0, 0, null);
			break;
			*/
		case PAUSE: // pause picture
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER: // end picture
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public void paintHero(Graphics g) 
    {
		g.drawImage(hero.image, hero.x, hero.y, null); // draw hero target
	}

	public void paintFlyingObjects(Graphics g) 
    {
		for (int i = 0; i < flyings.length; i++) 
        {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}

	public void paintBullets(Graphics g) 
    {
		for (int i = 0; i < bullets.length; i++) 
        {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}

	}

	public void paintScore(Graphics g) // draw score and life
    {
		g.setColor(new Color(0xFF0000));
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g.drawString("SCORE: " + score, 20, 25);
		g.drawString("LIFE: " + hero.getLife(), 20, 45);
		g.drawString("TIME:" +time1,20,65);
	}
}