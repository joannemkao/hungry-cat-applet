public class Player {
	//Player variables
    private int speed;
    private int width;
    private int posX;
    private int posY;
    private int height;
    
    //constructor
    public Player(int posX, int posY, int speed, int width)
    {
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;
        this.width = width;
        this.height = 0;
    }
    
    //get y-coord of player
    public int getposX(){
        return this.posX;
    }
    
    //get y-coord of player
    public int getposY(){
        return this.posY;
    }
    
    //get width of player
    public int getWidth(){
        return this.width;
    }

    //get height of player
    public int getHeight(){
    	return this.height;
    }
    
    //set height of player
    public void addHeight(int heightAmount){
    	this.height += heightAmount;
    }
    
    //movement methods - assure the player stays on screen.
    public void moveLeft(){
        if((posX - speed) > 0)
            posX -= speed;
        //else pos_x - speed <= 0, so make 0
        else if(posX > 0)
            posX = 0;
    }
    public void moveRight(){
    	//where width is the width of the player
        if((posX + width + speed) < 500)
            posX += speed;
        else if ((posX + width) < 500)
        	posX = 500 - width;
    }	
}
