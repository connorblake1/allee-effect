import java.awt.Color;
import java.awt.Graphics;
public class Blob implements Constants {
    private static double[] scale;
    private static double[] bump;
    private double x,y,t, ax, ay, trackX, trackY, health;
    private boolean isAlive;
    private int activeTracking, wallBounce;
    private Color color;
    private double [] dna; // 0 speed (always runs at max), 1 tracking (steering force), 2 tracking (radius), 3 tracking (vision arc), 4 sensing (close 360 radius),5 wall bouncing
    Blob(double[] genes) {
        activeTracking = -1;
        isAlive = true;     // speed  force     track-r     track-t  close-r         wall
        scale = new double[] {  0.5,    1,      .5,         1,          .1,           .3};
        bump = new double[] {   20,     0,      SIZE,        20,        SIZE/2,          20};
        health = DEFAULT_HEALTH;
        dna = new double[TRAITS];
        //System.out.println("start");
        for (int i = 0; i < TRAITS; i++) {
            dna[i] = genes[i]*scale[i]+bump[i];
        }
        color = new Color(128,128,128);
        wallBounce = (int)dna[5];
        x = (int)(dna[5]+Math.random()*(ACROSS-dna[5]));
        y = (int)(dna[5]+Math.random()*(DOWN-dna[5]));
    }

    public void display(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        //TODO fix this
        graphics.drawLine((int)(x+SIZE/2+Math.cos(t+dna[3]/360*Math.PI)*dna[4]),(int)(y+SIZE/2+Math.sin(t+dna[3]/360*Math.PI)*dna[4]),(int)(x+SIZE/2+Math.cos(t+dna[3]/360*Math.PI)*dna[2]),(int)(y+SIZE/2+Math.sin(t+dna[3]/360*Math.PI)*dna[2]));
        graphics.drawLine((int)(x+SIZE/2+Math.cos(t-dna[3]/360*Math.PI)*dna[4]),(int)(y+SIZE/2+Math.sin(t-dna[3]/360*Math.PI)*dna[4]),(int)(x+SIZE/2+Math.cos(t-dna[3]/360*Math.PI)*dna[2]),(int)(y+SIZE/2+Math.sin(t-dna[3]/360*Math.PI)*dna[2]));
        graphics.setColor(color);
        graphics.fillOval((int)x,(int)y,SIZE,SIZE);
        graphics.setColor(Color.WHITE);
        graphics.drawOval((int)x,(int)y,SIZE,SIZE);
        graphics.drawLine((int)(x+SIZE/2),(int)(y+SIZE/2),(int)(x+(1+Math.cos(t))*SIZE/2),(int)(y+(1+Math.sin(t))*SIZE/2));
        //arc
        graphics.drawArc((int)(x+SIZE/2-dna[2]),(int)(y+SIZE/2-dna[2]),(int)(2*dna[2]),(int)(2*dna[2]),(int)(-t*180/Math.PI+dna[3]/2),(int)(-dna[3]));
        graphics.drawOval((int)(x+SIZE/2-dna[4]),(int)(y+SIZE/2-dna[4]),(int)(2*dna[4]),(int)(2*dna[4]));
    }
    public void modSteering(boolean to, double x1, double y1) {
        double mag = Math.sqrt(Math.pow(y1-y,2)+Math.pow(x1-x,2));
        int s;
        if (to) {s = 1;}
        else {s = -1;}
        ax += s*dna[1]*(x1-x)/mag-Math.cos(t);
        ay += s*dna[1]*(y1-y)/mag-Math.sin(t);
    }
    public double limit(double limiter, double input) {
        if (limiter < input) {
            return limiter*(input/Math.abs(input));}
        else {return input;}}
    public void move() {
        double vx = Math.cos(t)*dna[0];
        double vy = Math.sin(t)*dna[0];
        t = Math.atan2(vy+ay,vx+ax);
        x+=NOTCH*Math.cos(t)*dna[0];
        y+=NOTCH*Math.sin(t)*dna[0];
        ax = 0;
        ay = 0;}
    public double colorDist(Color c) {return Math.sqrt(Math.pow(c.getRed()-color.getRed(),2) + Math.pow(c.getGreen()-color.getGreen(),2) + Math.pow(c.getBlue()-color.getBlue(),2));}
    public double getX() {return x;}
    public void setX(double x) {this.x = x;}
    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
    public void setTracker(double x1,double y1) {trackX = x1; trackY = y1;}
    public void setTracker(Food f) {trackX = f.getX(); trackY = f.getY();}
    public void setTracker(Blob b) {trackX = b.getX(); trackY = b.getY();}
    public double getTrackX() {return trackX;}
    public void setTrackX(double trackX) {this.trackX = trackX;}
    public double getTrackY() {return trackY;}
    public void setTrackY(double trackY) {this.trackY = trackY;}
    public double getT() {return t;}
    public void setT(double t) {this.t = t;}
    public double getHealth() {return health;}
    public void setHealth(int health) {this.health = health;}
    public void modHealth(double health) {this.health+=health;}
    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}
    public double[] getDNA() {
        double [] genes = new double[TRAITS];
        for (int i = 0; i < TRAITS; i ++) {
            genes[i] = (dna[i]-bump[i])/scale[i];}
        return genes;}
    public void setDNA(double[] DNA) {this.dna = DNA;}
    public boolean isAlive() {return isAlive;}
    public void setState(boolean state) {isAlive = state;}
    public int getTracked() {return activeTracking;}
    public void setActiveTracking(int tr) {activeTracking = tr;}
    public int getWallBounce() {return wallBounce;}
    public boolean visionCheck(Food food) {
        double dx = food.getX()-this.x+SIZE/2;
        double dy = food.getY()-this.y+SIZE/2;
        double d = Math.sqrt(dx*dx+dy*dy);
        return ((d<= this.dna[2] && Math.atan2(dy,dx)-this.t > -this.dna[3]*Math.PI/360 && Math.atan2(dy,dx)-this.t < this.dna[3]*Math.PI/360)||d <= this.dna[4]);}
    public boolean visionCheck(Blob blob) {
        double dx = blob.getX()-this.x;
        double dy = blob.getY()-this.y;
        double d = Math.sqrt(dx*dx+dy*dy);
        return ((d<= this.dna[2] && Math.atan2(dy,dx)-this.t > -this.dna[3]/2 && Math.atan2(dy,dx)-this.t < this.dna[3]/2)||d <= this.dna[4]);}
    public boolean eatCheck(Food food) {
        double dx = food.getX()-this.x-SIZE/2+FOOD_SIZE/2;
        double dy = food.getY()-this.y-SIZE/2+FOOD_SIZE/2;
        double d = Math.sqrt(dx*dx+dy*dy);
        return (d<=SIZE);
    }
    public boolean crashCheck(Blob blob) {
        double dx = blob.getX()-this.x;
        double dy = blob.getY()-this.y;
        return (Math.sqrt(dx*dx+dy*dy) < 2*SIZE);
    }
}
