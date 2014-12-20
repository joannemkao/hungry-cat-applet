
public class Food extends Object{
  
	private boolean isFalling = false;
	private Type type;
	
	public enum Type {
        ICE_CREAM(0,50,3), CLOCK(10,0,3), EGG(0,10,2);
        private int timeValue;
        private int scoreValue;
        private int speed;
        private Type(int timeValue, int scoreValue, int speed) {
                this.timeValue = timeValue;
                this.scoreValue = scoreValue;
                this.speed = speed;
        }
	};   

	
	//use object constructor
	public Food(int posX, int posY, Type type){
		super(posX, posY, type.speed);
		this.type = type;
	}
	
	public boolean getFalling(){
		return isFalling;
	}
	
	public int getTimeValue(){
		if (type == null) return 0;
		return type.timeValue;
				
	}
	
	public int getScoreValue(){
		if (type == null) return 0;
		return type.scoreValue;
	}

	public void setFalling(boolean b){
		this.isFalling = b;
	}
	
	public void setType(Type type){
		this.type = type;
	}
	
	//movement method - once the food moves off the left screen, remove
    public int move(){
    	//don't move the x position if it is falling
    	if (!isFalling){
	    	if (getPosX() >= -10)
	    		setPosX(getPosX() - getSpeed());
	    	else
	    		setPosX(-100);
    	}
    	return getPosX();
    }
    
    public int fall(){ 	
        if (getPosY() < 475)
        	setPosY(getPosY() + 3);
        else
        	setPosY(600);
        return getPosY();
    }
}