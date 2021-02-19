import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GridDemoPanel extends JPanel implements MouseListener, KeyListener, Constants
{
    final private JFrame svFrame;
    final private JFrame dvFrame;

    public GridDemoPanel(GridDemoFrame parent)
    {
        super();

        dvFrame = new JFrame("Mole Fraction");
        DistroVisualizer dvPanel = new DistroVisualizer();
        dvFrame.add(dvPanel);
        dvFrame.setSize(AVACROSS,AVDOWN+20);
        dvFrame.setVisible(true);
        dvFrame.setLocation(060,445);
        svFrame = new JFrame("Speed Distribution");
        SpeedVisualizer svPanel = new SpeedVisualizer();
        svFrame.add(svPanel);
        svFrame.setSize(AVACROSS,AVDOWN+20);
        svFrame.setVisible(true);
        svFrame.setLocation(660,445);
        setBackground(Color.BLACK);


    }

    public void paintComponent(Graphics g) {

    }

    /**
     * the mouse listener has detected a click, and it has happened on the cell in theGrid at row, col
     * @param row
     * @param col
     */
    public void userClickedCell(int row, int col) {}

    //============================ Mouse Listener Overrides ==========================
    @Override
    // mouse was just released within about 1 pixel of where it was pressed.
    public void mouseClicked(MouseEvent e) {
        // mouse location is at e.getX() , e.getY().
        // if you wish to convert to the rows and columns, you can integer-divide by the cell size.

    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    // mouse just entered this window
    public void mouseEntered(MouseEvent e){}
    @Override
    // mouse just left this window
    public void mouseExited(MouseEvent e){}
    //============================ Key Listener Overrides ==========================
    @Override
    /**
     * user just pressed and released a key. (May also be triggered by autorepeat, if key is held down?
     * @param e
     */
    public void keyTyped(KeyEvent e) {}
    @Override
    //not active in final program - remnant of when the program was a playable game of snake
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    // ============================= animation stuff ======================================
    /**
     * if you wish to have animation, you need to call this method from the GridDemoFrame AFTER you set the window visibility.
     */
    public void initiateAnimationLoop() {
        Thread aniThread = new Thread( new AnimationThread(0)); // the number here is the number of milliseconds between z.
        aniThread.start(); }

    /**
     * Modify this method to do what you want to have happen periodically.
     * This method will be called on a regular basis, determined by the delay set in the thread.
     * Note: By default, this will NOT get called unless you uncomment the code in the GridDemoFrame's constructor
     * that creates a thread.
     *
     */
    public void animationStep() {

        repaint();
        //svFrame.repaint();
        dvFrame.repaint();
    }

    //displays the full ANN connection and propagated value for a given Snakey with viperPit index as globalRepaintIndex
    //TODO show by species
    public class SpeedVisualizer extends JPanel
    {
        public SpeedVisualizer()
        {super();}
        public void paintComponent(Graphics g)
        {
                        }}

    public class DistroVisualizer extends JPanel
    {
        public DistroVisualizer()
        {super();}
        public void paintComponent(Graphics g)
        {

        }}

    // ------------------------------- animation thread - internal class -------------------
    public class AnimationThread implements Runnable
    {
        long start;
        long timestep;
        public AnimationThread(long t) {
            timestep = t;
            start = System.currentTimeMillis();}
        @Override
        public void run() {
            long difference;
            while (true) {
                difference = System.currentTimeMillis() - start;
                if (difference >= timestep) {
                    animationStep();
                    start = System.currentTimeMillis();}
                try {Thread.sleep(100);}
                catch (InterruptedException iExp) {
                    System.out.println(iExp.getMessage());
                    break;}}}}
}
