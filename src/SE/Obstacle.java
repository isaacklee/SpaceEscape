package SE;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Obstacle extends Rectangle
{
	
	double vx = 0; // pix per second 
	double vy = 0;
	int speed = 300;
	protected final static int hw = 50; // size of ball
	public static int getHW() { return hw; }
	Random r;
	protected static int spaceW; // dimensions of the space
	protected static int spaceH; // to bounce in ( 0,0 to W,H )
	public static void setSpaceW( int w ) { spaceW = w; }
	public static void setSpaceH( int h ) { spaceH = h; }
	
	int x=0;
	int y=0;
	
    public Obstacle( Random r1, int x, int y, double vx, double vy )
    {
    	super(x,y,hw,hw);
    	r=r1;
    	this.x = x;
    	this.y = y;
    	setX(x);
    	setY(y); 
    	//set random speed
    	this.vx = vx;
    	this.vy = vy;
    }
    
    public double getvx() {
    	return vx;
    }
      
    public void move( double deltat )
    {
    	
    	/*double x = getX();
    	x += vx * deltat;
    	setX(x);
    	double y = getY();*/
    	y += vy * deltat;
    	setY(y);
    	
    	if ( y>spaceH -hw ) { vy = -(r.nextInt(300) + 200); }
		if ( x>spaceW -hw ) { vx = -(r.nextInt(300) + 200); }
		if ( x<hw ) { vx = (r.nextInt(300) + 200); }
		if ( y<hw-50 ) { vy = (r.nextInt(300) + 200); }
    
    }
    public void rmove( double deltat )
    {	
    	double x = getX();
    	x += vx * deltat;
    	setX(x);
    	double y = getY();
    	y += vy * deltat;
    	setY(y);
    	
    	if ( y>spaceH -hw ) { vy = -(r.nextInt(179) + 181); }
		if ( x>spaceW -hw ) { vx = -(r.nextInt(179) + 181); }
		if ( x<hw ) { vx = (r.nextInt(179) + 181); }
		if ( y<hw ) { vy = (r.nextInt(179) + 181); }
    }
    
    public void Hit() {
    	vx=-vx;
    }
    //When the door is open go through the door
	public void goThrough(double deltat) {
		double x = getX();
    	setX(x);
    	
    	double y = getY();
    	setY(y);
    	
	}
	

}



