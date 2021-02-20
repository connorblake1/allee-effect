import java.awt.Color;
import java.awt.Graphics;
public class Food implements Constants {
    private double x,y, size, health;
    private boolean isGone;
    Food(double x,double y, double health) {
        this.size = FOOD_SIZE;
        this.x = x;
        this.y = y;
        this.health = health;
        this.isGone = false;
    }
    Food() {
        this.size = FOOD_SIZE;
        this.isGone = false;
        this.health = FOOD_HEALTH;
        this.x = WALL_OFFSET + Math.random()*(ACROSS-2*WALL_OFFSET);
        this.y = WALL_OFFSET + Math.random()*(DOWN-2*WALL_OFFSET);}
    public double getX() {return x;}
    public void setX(double x) {this.x = x;}
    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
    public double getHealth() {return health;}
    public void setHealth(double health) {this.health = health;}
    public boolean isGone() {return isGone;}
    public void setGone(boolean state) {isGone = state;}
    public void display(Graphics graphics) {
        graphics.setColor(Color.RED);
        if (!isGone) graphics.fillOval((int)x,(int)y,(int)size,(int)size);}
}
