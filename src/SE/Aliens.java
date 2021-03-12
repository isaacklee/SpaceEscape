package SE;

import java.util.Random;

import javafx.scene.shape.Circle;


public class Aliens extends Circle
{
	
	double vx = 0; // pix per second 
	double vy = 0;
	int speed = 300;
	protected final static int hw = 20; // size of ball
	public static int getHW() { return hw; }
	Random r;
	protected static int spaceW=800; // dimensions of the space
	protected static int spaceH=700; // to bounce in ( 0,0 to W,H )
	public static void setSpaceW( int w ) { spaceW = w; }
	public static void setSpaceH( int h ) { spaceH = h; }
	
	int x=0;
	int y=0;
	
    public Aliens( Random r1, int x, int y, double vx, double vy )
    {
    	super(x,y,hw);
    	r=r1;
    	this.x = x;
    	this.y = y;
    	setCenterX(x);
    	setCenterY(y); 
    	//set random speed
    	this.vx = vx;
    	this.vy = vy;
    	
    }
    
    public double getvx() {
    	return vx;
    }
      
    public void move( double deltat )
    {
    	
    	double x = getCenterX();
    	x += vx * deltat;
    	setCenterX(x);
    	double y = getCenterY();
    	y += vy * deltat;
    	setCenterY(y);
    	
    	if ( y>spaceH -hw-100 ) { vy = -(r.nextInt(179) + 181); }
		if ( x>spaceW -hw-50 ) { vx = -(r.nextInt(179) + 181); }
		if ( x<hw+100 ) { vx = (r.nextInt(179) + 181); }
		if ( y<hw ) { vy = (r.nextInt(179) + 181); }
    
    }
    
    
    public void Hit() {
    	vx=-vx;
    }
}