package SE;

import java.util.Random;

import javafx.scene.shape.Circle;


public class star extends Circle
{
	
	double vx = 0; // pix per second 
	double vy = 0;
	int speed = 300;
	protected final static int hw = 10; // size of ball
	public static int getHW() { return hw; }
	Random r;
	protected static int spaceW=800; // dimensions of the space
	protected static int spaceH=700; // to bounce in ( 0,0 to W,H )
	public static void setSpaceW( int w ) { spaceW = w; }
	public static void setSpaceH( int h ) { spaceH = h; }
	
	int x=0;
	int y=0;
	
    public star( int x, int y)
    {
    	super(x,y,hw);
    	this.x = x;
    	this.y = y;
    	setCenterX(x);
    	setCenterY(y); 
    	//set random speed
    	
    }
}