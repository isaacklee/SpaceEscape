package SE;
import javafx.scene.shape.Rectangle;

public class SpaceShip extends Rectangle {
	private double velX=0;
	private double velY=0;
	double x = getX();
	double y = getY();
	
	public SpaceShip() {
		 super( 10, 300, 40, 30 );
	}
	public void tick() {
		x+=velX;
		y+=velY;
	}
	public void setVelX(double  velX) {
		this.velX=velX;
		tick();
		
	}
	public void setVelY(double velY) {
		this.velY=velY;
		tick();
		
	}
	
	public void check()
	{
		if(getX()<0||getX()>840) {
			x=10;
			setX(x);
			setY(y);
		}
		else if(getY()<50) {
			y=55;
			setY(y);
		}
		else if(getY()>550) {
			y=546;
			setY(y);
		}
		
	}
	 
	public void move(double d, double u)
	{
		
		if(getX()<0||getX()>840) {
			x=10;
			setX(x);
			setY(y);
		}
		else if(getY()<50) {
			y=55;
			setY(y);
		}
		else if(getY()>550) {
			y=546;
			setY(y);
		}
		else {
			x += d;
			setX(x);
			y+=u;
			setY(y);
		}
	}
	
}
