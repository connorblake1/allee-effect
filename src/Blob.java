import java.awt.Color;
import java.awt.Graphics;
public class Blob implements Constants {
    private float x,y,vr, vt;
    private int health;
    private String dna;
    private Color color;
    Blob() {

    }
    public void display(Graphics graphics) {
        graphics.fillOval((int)x,(int)y,SIZE,SIZE);
    }
    public double colorDist(Color c) {
        return Math.sqrt(Math.pow(c.getRed()-color.getRed(),2) + Math.pow(c.getGreen()-color.getGreen(),2) + Math.pow(c.getBlue()-color.getBlue(),2));
    }
}
