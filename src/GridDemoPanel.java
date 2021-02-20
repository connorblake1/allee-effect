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
//    final private JFrame dvFrame;
    private ArrayList<Food> snacks;
    private ArrayList<Blob> blobs;
    private ArrayList<double []> genStats;
//    private Food f;
//    private Blob b, b1, b2;

    public GridDemoPanel(GridDemoFrame parent)
    {
        super();

//        dvFrame = new JFrame("Mole Fraction");
//        DistroVisualizer dvPanel = new DistroVisualizer();
//        dvFrame.add(dvPanel);
//        dvFrame.setSize(AVACROSS,AVDOWN+20);
//        dvFrame.setVisible(true);
//        dvFrame.setLocation(060,445);
        svFrame = new JFrame("Characteristic Distribution");
        CharDist svPanel = new CharDist();
        svFrame.add(svPanel);
        svFrame.setSize(AVACROSS,AVDOWN+20);
        svFrame.setVisible(true);
        svFrame.setLocation(660,445);
       // setBackground(Color.BLACK);

//        b = new Blob(buildRandomDNA());
//        f = new Food(200,200,20);
//        b.setTracker(f);
        genStats = new ArrayList<double[]>();
        blobs = new ArrayList<Blob>();
        for (int i = 0; i < POPULATION; i++) {
            blobs.add(new Blob(buildRandomDNA()));
            //blobs.get(i).setTracker(f);
        }

        snacks = new ArrayList<Food>();
        for (int i = 0; i < SNACK; i++) {
            snacks.add(new Food());}
        genStats.add(addNewAvg());
        svFrame.repaint();
    }

    public void paintComponent(Graphics g) {
        setBackground(Color.GRAY);
        for (int i = 0; i < blobs.size(); i++) {
            if (blobs.get(i).isAlive()) {
                blobs.get(i).display(g);}}
        for (int i = 0; i < SNACK; i++) {
            if (!snacks.get(i).isGone()) {
                snacks.get(i).display(g);
            }
        }
//        b.display(g);
//        f.display(g);
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
        for (int i = 0; i < POPULATION; i++) {
            Blob b = blobs.get(i);
            if (b.isAlive()) {
                if (b.getX() < 0 || b.getX() > ACROSS-SIZE/2 || b.getY() < 0 || b.getY() > DOWN -SIZE/2) {
                    b.setState(false);
                    continue;}
                if (b.getX() < b.getWallBounce()) {
                    b.modSteering(false,0,b.getY());}
                if (b.getX() > ACROSS - b.getWallBounce()) {
                    b.modSteering(false,ACROSS,b.getY());}
                if (b.getY() < b.getWallBounce()) {
                    b.modSteering(false,b.getX(),0);}
                if (b.getY() > DOWN - b.getWallBounce()) {
                    b.modSteering(false,b.getX(),DOWN);}
                boolean breakCheck = false;
//                for (int j = 0; j < POPULATION; j++) {
//                    if (i!=j) {
////                        if (b.crashCheck(blobs.get(j))) {
////                            System.out.println("Crash");
////                            b.setState(false);
////                            blobs.get(j).setState(false);
////                            breakCheck = true;
////                            break;
////                        }
//                        if (b.visionCheck(blobs.get(j))) {
//                            b.modSteering(false,blobs.get(j).getX(),blobs.get(j).getY());}}}
                if (breakCheck) {continue;}
                if (b.getTracked() != -1 && !snacks.get(b.getTracked()).isGone()) {
                    b.modSteering(true,snacks.get(b.getTracked()).getX(),snacks.get(b.getTracked()).getY());
                    if (b.eatCheck(snacks.get(b.getTracked()))) {
                        b.modHealth(snacks.get(b.getTracked()).getHealth());
                        snacks.get(b.getTracked()).setGone(true);
                        b.setActiveTracking(-1);}}
                else {
                    for (int j = 0; j < SNACK; j++) {
                        if (!snacks.get(j).isGone()) {
                            if (b.visionCheck(snacks.get(j))) {
                                b.setActiveTracking(j);
                                break;}}}}
            }
        }
        for (int i = 0; i < blobs.size(); i++) {
            if (blobs.get(i).isAlive()) {
                blobs.get(i).move();}}
        repaint();
        boolean genBreaker = true;
        for (int i = 0; i < SNACK; i++) {
            if (!snacks.get(i).isGone()) {
                genBreaker = false;
                break;}}
        boolean genBreaker2 = true;
        for (int i = 0; i < POPULATION; i++) {
            if (blobs.get(i).isAlive()) {
                genBreaker2 = false;
                break;}}
        if (genBreaker || genBreaker2) {
            System.out.println("EVOLUTION");
            ArrayList<Blob> nextGen;
            nextGen = new ArrayList<Blob>();
            while (blobs.size()>0) { //shitty bubble sort
                double max = 0;
                int index = -1;
                for (int i = 0; i < blobs.size(); i++) {
                    if (blobs.get(i).getHealth() > max) {
                        max = blobs.get(i).getHealth();
                        index = i;}}
                nextGen.add(blobs.get(index));
                blobs.remove(index);}
            blobs = new ArrayList<Blob>();
            for (int i = 0; i < POPULATION/8;i++) { //couple num
                for (int j = 0; j < 4; j++) {
                    double [] zygote = new double[TRAITS];
                    for (int k = 0; k < TRAITS; k++) {
                        int n = (int)(2*Math.random()); //50% chance
                        int m = (int)(10*Math.random());
                        double wack = 0; //mutation
                        if (m == 1) {wack = Math.random()*100-50;}
                        zygote[k] = wack+nextGen.get(2*i+n).getDNA()[k];}
                    blobs.add(new Blob(zygote));
                    blobs.add(new Blob(zygote));}}
            snacks = new ArrayList<Food>();
            for (int i = 0; i < SNACK; i++) {
                snacks.add(new Food());}
            genStats.add(addNewAvg());
            svFrame.repaint();
        }
        //svFrame.repaint();
        //dvFrame.repaint();
    }
    public double[] addNewAvg() {
        double [] returner = new double[TRAITS];
        for (int i = 0; i < TRAITS; i++) {
            double sum = 0;
            for (int j = 0; j < POPULATION; j++) {
                sum += blobs.get(j).getDNA()[i];}
            returner[i] = sum/POPULATION;}
        return returner;
    }
    public double[] buildRandomDNA() {
        double [] dna = new double[TRAITS];
        for (int i = 0; i < TRAITS; i++) {
            dna[i] = (Math.random()*100);}
        return dna;}

    //Auxiliary drawing panels
    //TODO show by species
    public class CharDist extends JPanel
    {
        public CharDist()
        {super();}
        public void paintComponent(Graphics g)
        {
            int gens = 75;
            g.setColor(Color.WHITE);
            g.drawRect(0,0,AVACROSS,AVDOWN);
            for (int i =0; i < genStats.size(); i++) {
                g.drawLine((int)(i/gens*AVACROSS),0,(int)(i/gens*AVACROSS),AVDOWN);}
            g.setColor(Color.RED);
            for (int i = 0; i < genStats.size(); i++) {
                for (int j = 0; j < TRAITS; j++) {
                    g.fillRect((int)(AVACROSS/gens*(i+j/TRAITS)),(int)(AVDOWN-genStats.get(i)[j]),(int)(AVACROSS/gens*j/TRAITS),(int)(genStats.get(i)[j]));} }

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
