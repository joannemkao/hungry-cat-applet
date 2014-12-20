public class Projectile extends Object {
	 
	//use object constructor
	public Projectile(int posX, int posY, int speed){
		super(posX, posY, speed);
	}
	
    //movement method once object moves off the top of the screen, set ycoord to -100
    public int move(){    	
    	if (getPosY() >= -10)
    		setPosY(getPosY() - getSpeed());
    	else
    		setPosY(-100);
    	return getPosY();
    }
}

