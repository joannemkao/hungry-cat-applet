import java.applet.*; 
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.util.*;
import java.awt.event.*;   // Provides ActionEvent, ActionListener 

//By implementing runnable, applet can be threaded and thus gain access to run() method
public class Game extends Applet implements Runnable
{
	//sets dimensions of the applet
    public final int WIDTH = 500;
    public final int HEIGHT = 500;
    Dimension screenSize = new Dimension(WIDTH, HEIGHT);
	
	/* double buffering - to eliminate flicker 
	 * bufferGraphics - holds graphics content draw on offScreen later
	 * offScreen - holds image of what to draw on the next screen
	 */
    Graphics bufferGraphics;
    Image offScreen;
   
    private Font aFont;
	private Image playerImg;
    Image background;
    Image titleScreen;
    Image howToScreen;
    Image gameOverScreen;
    Image scorebar;
    Player myPlayer;
    Dimension appletSize;
    
    boolean leftKey = false;
    boolean rightKey = false;
    boolean isRunning = true;
    
    //create a thread - run() is called when a thread is created
    Thread t;
    
    //generates random y-coord for food and random type (egg, ice cream, etc)
	Random randomGenerator = new Random();
    int randomPosInt;
    int randomTypeInt;
    
    //create a 2d array with objects and images;
    LinkedList<Food> foodList = new LinkedList<Food>();
    LinkedList<Image> foodImgList = new LinkedList<Image>();
    
    //timer to generate a new flying object given an amount of delay
    int delay;   
    int period;  
    Timer timer = new Timer();
    
    //projectile variables
    LinkedList<Projectile> projList = new LinkedList<Projectile>();
    LinkedList<Image> projImgList = new LinkedList<Image>();
    
    //keep score and time
    int score;
    private static long start;
    public static int timeLeft;
    
    //keeps track of which screen to paint
    static int screenTrack;

    
    // init - method is called when applet is loaded into browser
    public void init(){
    	
		//set font
		aFont = new Font("Verdana", Font.BOLD, 14);
		setFont(aFont);
	  
    	//set dimensions of applet
        this.setSize(screenSize);
        appletSize = getSize();

        background = getImage(getDocumentBase(), "background.png");
        scorebar = getImage(getDocumentBase(), "scorebar.png");
        titleScreen = getImage(getDocumentBase(), "title.png");
        howToScreen = getImage(getDocumentBase(), "instructions.png");
        gameOverScreen = getImage(getDocumentBase(), "gameover.png");
        
        myPlayer = new Player(0,350,5, 58);
        playerImg = getImage(getDocumentBase(),"hcat.png");
        
        //enable movement of player using arrow keys
        addKeyListener(new MyKeyListener());
        
        ///double buffering
        //create an off-screen image to draw on
        offScreen = createImage(appletSize.width,appletSize.height);
        //get graphics content of offScreen image
        bufferGraphics = offScreen.getGraphics();
        
        //listen for mouse clicks
        addMouseListener(new MouseListener());
        
        //for threading
        t = new Thread(this);
        t.start();
        
        score = 0;
        start = 0;
        timeLeft = 30; //30 seconds
        screenTrack = 0;
        
        delay = 1000;   // delay for 1 sec before task is executed
        period = 1000;  // repeat every 2.5 sec.
    }
    
    /* TODO: start - automatically called after the init() method 
     * and whenever user returns after looking at other pages
     */
    public void start() {
    	
    } 
    
    // TODO: stop - method is called if you leave the site with the applet 
    public void stop() {
    	
    } 

    // TODO: destroy method is called if you close the browser 
    public void destroy() {
    	//stop looping in run()
    	isRunning = false;
    	//destroy thread
    	t = null;
    } 

    //called when a new thread is created - runs on infinite loop
	public void run () {
        
        Projectile currProj;
        Image currProjImg;
        
    	//loop forever until user leaves.
        while(isRunning){
        	
          //update timer if game is running
          if (screenTrack == 2)
        	  timeCountdown();
        	
          if(leftKey){ 
        	  myPlayer.moveLeft(); 
          }
          if(rightKey){ 
        	  myPlayer.moveRight(); 
          }
          
          //repaint to make changes appear on screen
          repaint();
          
          if (screenTrack == 2){
	          Food currFood;
	          Image currImg;
	          //loop through all the food in the list and either remove, fall, or continue flying
	          for (int i = 0; i < foodList.size(); i++)
	          {
	          	  currFood = foodList.get(i);
	          	  currImg = foodImgList.get(i);
	        	  
	        	  //if an object flies off screen, remove
			      if (currFood != null && currFood.move() == -100 && currFood.getFalling() == false){
			    	  foodImgList.remove(currImg);
			    	  foodList.remove(currFood);
			    	  currFood = null;
			    	  currImg = null;
			    	  i--;
			      }
			          
			      //if proj position is close to the food, make the food fall
			      //traverse through projectiles
			      int tempSize = projList.size();
			      for (int j = 0; j < tempSize; j++){
			    	  currProj = projList.get(j);
					  if (currProj != null && currFood != null){
						  if ((currFood.getPosX() <= (currProj.getPosX() + 30)) && (currFood.getPosX() >= (currProj.getPosX() - 30))){
							  if ((currFood.getPosY() <= (currProj.getPosY() + 30)) && (currFood.getPosY() >= (currProj.getPosY() - 30))){
							    	  	currFood.fall();
							    	  	currFood.setFalling(true);
							  }
						  }
					  }
			      }
				  
				  //if falling boolean is true, let it fall
				  if (currFood != null && currFood.getFalling() == true){
					  int fallPos = currFood.fall();
					  //eat food
					  if ((fallPos <= 375 + 20) && (fallPos >= 375 - 20)){
						  int foodXPos = currFood.getPosX();
						  int catXPos = myPlayer.getposX();
						  if ((foodXPos <= catXPos + 20) && (foodXPos >= catXPos - 20)){
							  //add to score
							  score += currFood.getScoreValue();
							  timeLeft += currFood.getTimeValue();
							  foodImgList.remove(currImg);
					    	  foodList.remove(currFood);
					    	  currFood = null;
					    	  currImg = null;
						  }
					  }
					  if (fallPos == 600){
						  foodImgList.remove(currImg);
				    	  foodList.remove(currFood);
				    	  currFood = null;
				    	  currImg = null;
				    	  i--;
					  }
					  
				  }  
	          }
	          
	          //projectile - clear projectiles that are offscreen
		      for (int j = 0; j < projList.size(); j++){
		    	  currProj = projList.get(j);
		    	  currProjImg = projImgList.get(j);
		          if (currProj != null && currProj.move() == -100){
		        	  projImgList.remove(currProjImg);
		        	  projList.remove(currProj);
		        	  currProj = null;
		        	  currProjImg = null;
		        	  //rewind counter
		        	  j--;  
		          }
		      }
          }
          
          try {
              Thread.sleep(20);
          }
          catch (InterruptedException ex){ }
        }
    } 
    
    private class MyKeyListener extends KeyAdapter{
        
        public void keyPressed(KeyEvent e){
        	Projectile newProj;
        	Image newProjImg;
        	int currKey = e.getKeyCode();
            if (currKey == KeyEvent.VK_LEFT)
                	leftKey = true;
            else if (currKey == KeyEvent.VK_RIGHT)
                	rightKey = true;
            else if (currKey == KeyEvent.VK_SPACE){
	                int x = myPlayer.getposX() + 5;
	                int y = myPlayer.getposY() - 35;
	                newProj = new Projectile(x, y, 10);
	                newProjImg = getImage(getDocumentBase(), "fireball.png");
	                projList.add(newProj);
	                projImgList.add(newProjImg);
            }
        }
        
        public void keyReleased(KeyEvent e){
        	int currKey = e.getKeyCode();
        	if (currKey == KeyEvent.VK_LEFT)
        		leftKey = false;
        	else if (currKey == KeyEvent.VK_RIGHT)
        		rightKey = false;
        }
    }

    private class MouseListener extends MouseAdapter{
    	public void mouseClicked(MouseEvent e) {
    	       if (screenTrack == 0)
    	    	   screenTrack++;
    	       else if (screenTrack == 1)
    	       {
    	    	   screenTrack++;
    	    	   
    	    	   //set a timer to repeat every 2.5 seconds
    	    	   timer.scheduleAtFixedRate(new TimerTask() {
    	    		   public void run() {
    	    			   randomTypeInt = randomGenerator.nextInt(5);
    
    	    			   Food.Type type;
    	    			   Image foodImg;
    	    			   switch (randomTypeInt) {
    	    			   		case 1: type = Food.Type.ICE_CREAM;
    	    			   			foodImg = getImage(getDocumentBase(), "ice_cream.png");
    	    	                    break;
    	    			   		case 2: type = Food.Type.CLOCK;
    	    			   			foodImg = getImage(getDocumentBase(), "clock.png");
    	    			   			break;
    	    			   		default: type = Food.Type.EGG;
	    	    			   		foodImg = getImage(getDocumentBase(), "miniegg.png");
		    			   			break;
    	    			   }
    	    			   randomPosInt = randomGenerator.nextInt(260);
    	    			   Food currFood = new Food(450, 30 + randomPosInt, type);	    					
    	    			   foodImgList.add(foodImg);
    	    			   foodList.add(currFood);
	    		    	}
	    		    }, delay, period); //end timer method
    	       }
    	}
    }

    //timer
    public static void timeCountdown() {
        if (System.currentTimeMillis() - start >= 1000) { 
        	//every second (1000ms, deduct 1)
           timeLeft -= 1; 
           start = System.currentTimeMillis();
        }
        
        //end game if time runs out. 
        if (timeLeft == 0){
        	screenTrack = 3;
        }
     }
    
    /* paint(Graphics display) - called automatically following the start method and repaint()
     * parameters: Graphics display - provided by the system when paint is called      
     */
    public void paint(Graphics display){
    	
    	if (screenTrack == 0){
	        display.drawImage(titleScreen,0,0,this); 
    	}
    	
    	else if (screenTrack == 1){
	        display.drawImage(howToScreen,0,0,this);   
    	}
    	
    	else if (screenTrack == 2){	
    		//draw offscreen first (bufferGraphics)
	        bufferGraphics.drawImage(background, 0, 0, null);
	        bufferGraphics.drawImage(playerImg, myPlayer.getposX(), myPlayer.getposY(), null);        
	        bufferGraphics.drawImage(scorebar, 0, 0, null);
	        bufferGraphics.drawString( "" + score, 110, 19);
	        bufferGraphics.drawString("" + timeLeft, 415, 19);
	        
	        int foodSize = foodList.size();
	        Food currFood;
	        Image currfoodImg;
	        for (int j = 0; j < foodSize; j++){
	        	currFood = foodList.get(j);
	        	currfoodImg = foodImgList.get(j);
	            //draw object
	        	if (currFood != null && currfoodImg != null)
	        	        		bufferGraphics.drawImage(currfoodImg, currFood.getPosX(), currFood.getPosY(), null);
	        }
	        
	        //draw projectile(s)
	        int projSize = projList.size();
	        Projectile currProj;
	        Image projImg;
	        for (int j = 0; j < projSize; j++){
	        	currProj = projList.get(j);
	        	projImg = projImgList.get(j);
	            //draw object
	        	if (currProj != null)
	        	        		bufferGraphics.drawImage(projImg, currProj.getPosX(), currProj.getPosY(), null);
	        }
	        
	        //draw bufferGraphics (offscreen) image to the screen.
	        display.drawImage(offScreen,0,0,this);    
    	} //end if (screenTrack == 2)
    	
    	if (screenTrack == 3)
    	{
    		//draw offscreen first (bufferGraphics)
	        bufferGraphics.drawImage(gameOverScreen, 0, 0, null);
	        bufferGraphics.drawString( "final score: " + score, 190, 305);
	        //draw bufferGraphics (offscreen) image to the screen
	        display.drawImage(offScreen,0,0,this); 
    	}
    }
    
    /* "update" method is called when repaint() is called 
     * parameters: Graphics display - provided by system when update/repaint() is called
     * Override to prevent flickering
     * (typically update clears the screen then paints) */
    public void update(Graphics display){
        paint(display);
    }  
}
