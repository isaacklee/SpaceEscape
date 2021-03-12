package SE;

import javafx.scene.shape.Circle;

public class Door extends Circle {

     public Door(int x, int y) {
    	 super(850,x,30);
     }
     public Point getExit() {
    	 Point point = new Point(getCenterX(),getCenterY());
    	 return point;
     }
}
