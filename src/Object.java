
public abstract class Object {

    private int speed;
    private int posX;
    private int posY;
    
    //constructors
    public Object(int posX, int posY, int speed) {
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;

    }
    
    //get x-coord
    public int getPosX(){
        return this.posX;
    }
    
    //get y-coord
    public int getPosY(){
        return this.posY;
    }
    
    //get speed
    public int getSpeed(){
        return this.speed;
    }
    
    //set x-coord
    public void setPosX(int posX){
        this.posX = posX;
    }
    
    //set y-coord of player
    public void setPosY(int posY){
        this.posY = posY;
    }
    
    //set speed
    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    //movement method - implemented by Projectile or Food classes
    public abstract int move();
}
