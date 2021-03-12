package SE;

import javafx.scene.input.MouseEvent;

public class Point
{
   private double x, y; 
    public Point( double x, double y )
    {
        this.x = x;
        this.y = y;
    }
    
    public Point( MouseEvent me )
    {
         this.x = me.getX();
         this.y = me.getY();
    }

    public double getX() { return x; }
    public double getY() { return y; }
    
    public double xdif( Point p ) { return Math.abs(p.x - x); }
    public double ydif( Point p ) { return Math.abs(p.y - y); }

    public String toString()
    {
        return "" + x + ", "+ y;
    }
}