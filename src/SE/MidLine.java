package SE;

import javafx.scene.shape.Rectangle;

public class MidLine extends Rectangle {

    double a, b, c, d; 
     Rectangle rect;
	
    public MidLine() {
    	 super(870,0,50,600);
    	 
     }

	
	public void sety(double y) {
		rect.setY(y);
	}
	public Rectangle rect() {
		return rect;
	}
     
     
}
