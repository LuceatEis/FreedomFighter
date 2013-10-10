package freedomFighter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Note that this gamedriver has been enhanced and modified 
public abstract class GameDriver extends Canvas implements KeyListener, Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2100197421924079147L;
	protected static boolean[] keys;
	// protected boolean[] keysTyped;
	protected BufferedImage back;
	protected int timer = 6;

	public GameDriver()
	{
		//set up all variables related to the game

		// number of key possibilities
		keys = new boolean[25];
		// keysTyped = new boolean[16];


    	setBackground(Color.WHITE);
		setVisible(true);

		new Thread(this).start();
		addKeyListener(this);		//starts the key thread to log key strokes
		setFocusable(true);
	}

   public void update(Graphics window){
	   paint(window);
   }

   public void setTimer(int value) {
   		timer = value;
   }
   public void paint(Graphics window)
   {
		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));
		Graphics2D graphToBack = (Graphics2D) back.createGraphics();

		draw(graphToBack);

		Graphics2D win2D = (Graphics2D) window;
		win2D.drawImage(back, null, 0, 0);

	}

	public abstract void draw(Graphics2D win);

	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_W : keys[0]=true; break;
			case KeyEvent.VK_S : keys[1]=true; break;
			case KeyEvent.VK_A : keys[2]=true; break;
			case KeyEvent.VK_D : keys[3]=true; break;
			case KeyEvent.VK_F : keys[4]=true; break;

			case KeyEvent.VK_8 : keys[5]=true; break;
			case KeyEvent.VK_5 : keys[6]=true; break;
			case KeyEvent.VK_4 : keys[7]=true; break;
			case KeyEvent.VK_6 : keys[8]=true; break;
			case KeyEvent.VK_PLUS : keys[9]=true; break;
			case KeyEvent.VK_ENTER : keys[10]=true;break;
			case KeyEvent.VK_SPACE : keys[11]=true;break;
			case KeyEvent.VK_UP : keys[12]=true;break;
			case KeyEvent.VK_DOWN : keys[13]=true;break;
			case KeyEvent.VK_LEFT : keys[14]=true;break;
			case KeyEvent.VK_RIGHT : keys[15]=true;break;
			
			case KeyEvent.VK_Y : keys[16]=true;break; // Damage
			case KeyEvent.VK_U : keys[17]=true;break; // Rate of fire (1/pof)
			case KeyEvent.VK_I : keys[18]=true;break; // Health
			case KeyEvent.VK_O : keys[19]=true;break; // Armor
			case KeyEvent.VK_H : keys[20]=true;break; // Shield
			case KeyEvent.VK_J : keys[21]=true;break; // Shield Recharge Rate
			case KeyEvent.VK_K : keys[22]=true;break; // Shield Recharge Reset Time
			
			case KeyEvent.VK_OPEN_BRACKET : keys[23]=true;break; // save
			case KeyEvent.VK_CLOSE_BRACKET : keys[24]=true;break; // load
		}

	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_W : keys[0]=false; break;
			case KeyEvent.VK_S : keys[1]=false; break;
			case KeyEvent.VK_A : keys[2]=false; break;
			case KeyEvent.VK_D : keys[3]=false; break;
			case KeyEvent.VK_F : keys[4]=false; break;

			case KeyEvent.VK_8 : keys[5]=false; break;
			case KeyEvent.VK_5 : keys[6]=false; break;
			case KeyEvent.VK_4 : keys[7]=false; break;
			case KeyEvent.VK_6 : keys[8]=false; break;
			case KeyEvent.VK_PLUS : keys[9]=false; break;
			case KeyEvent.VK_ENTER : keys[10]=false;break;
			case KeyEvent.VK_SPACE : keys[11]=false;break;
			case KeyEvent.VK_UP : keys[12]=false;break;
			case KeyEvent.VK_DOWN : keys[13]=false;break;
			case KeyEvent.VK_LEFT : keys[14]=false;break;
			case KeyEvent.VK_RIGHT : keys[15]=false;break;
			
			case KeyEvent.VK_Y : keys[16]=false;break;
			case KeyEvent.VK_U : keys[17]=false;break;
			case KeyEvent.VK_I : keys[18]=false;break;
			case KeyEvent.VK_O : keys[19]=false;break;
			case KeyEvent.VK_H : keys[20]=false;break;
			case KeyEvent.VK_J : keys[21]=false;break;
			case KeyEvent.VK_K : keys[22]=false;break;
			
			case KeyEvent.VK_OPEN_BRACKET : keys[23]=false;break; 
			case KeyEvent.VK_CLOSE_BRACKET : keys[24]=false;break; 
		}
	}

	public void keyTyped(KeyEvent e){ /*
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W : keysTyped[0]=true; break;
			case KeyEvent.VK_S : keysTyped[1]=true; break;
			case KeyEvent.VK_A : keysTyped[2]=true; break;
			case KeyEvent.VK_D : keysTyped[3]=true; break;
			case KeyEvent.VK_F : keysTyped[4]=true; break;
	
			case KeyEvent.VK_8 : keysTyped[5]=true; break;
			case KeyEvent.VK_5 : keysTyped[6]=true; break;
			case KeyEvent.VK_4 : keysTyped[7]=true; break;
			case KeyEvent.VK_6 : keysTyped[8]=true; break;
			case KeyEvent.VK_PLUS : keysTyped[9]=true; break;
			case KeyEvent.VK_ENTER : keysTyped[10]=true;break;
			case KeyEvent.VK_SPACE : keysTyped[11]=true;break;
			case KeyEvent.VK_UP : keysTyped[12]=true;break;
			case KeyEvent.VK_DOWN : keysTyped[13]=true;break;
			case KeyEvent.VK_LEFT : keysTyped[14]=true;break;
			case KeyEvent.VK_RIGHT : keysTyped[15]=true;break;
		} */
		
	}

   public void run()
   {
   	try
   	{
   		while(true)
   		{
   		   Thread.currentThread().sleep(timer);
            repaint();
         }
      }catch(Exception e)
      {
      }
  	}

  	public static BufferedImage addImage(String name) {

  		BufferedImage img = null;

  			try {
				img = ImageIO.read(new File(name));

			} catch (IOException e) {
				System.out.println(e);
		}

  		return img;



  	}
  	
  	public static boolean[] getKeys() {
  		return keys;
  	}
  	public static boolean getKey(int i) {
  		return keys[i];
  	}

}