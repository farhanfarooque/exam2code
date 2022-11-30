package sim;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.Timer;
import java.nio.file.*;
import java.io.IOException;
import java.io.BufferedWriter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;


/**
 * Write a description of class ControllerView here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SimController
{
    // instance variables
    private Simulator sim;
    private JFrame frame;
    private JButton oneButton;
    private JButton zeroButton;
    private Timer simTimer;
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

    public SimController(){
        makeFrame();
        sim = new Simulator();
        simTimer = new Timer(100, e -> doNothing());

    }

    private void doNothing(){
        System.out.println("This does nothing");
    }

    /**
     * Create the Swing frame and its content.
     */
    private void makeFrame()
    {
        frame = new JFrame("Sim Control");
        JPanel contentPane = (JPanel) frame.getContentPane();

        makeMenuBar(frame);

        // Specify the layout manager
        contentPane.setLayout(new FlowLayout());

        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel();

        oneButton = new JButton("1");
        zeroButton = new JButton("0");

        toolbar.add(oneButton);
        toolbar.add(zeroButton);

        // Add toolbar into panel with flow layout for spacing
        JPanel flow = new JPanel();
        flow.add(toolbar);

        contentPane.add(flow );

        frame.pack();

        // place the frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }

    /**
     * Create the main frame's menu bar.
     * 
     * @param frame   The frame that the menu bar should be added to.
     */
    private void makeMenuBar(JFrame frame)
    {

        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu menu;
        JMenuItem item;

        // create the File menu
        menu = new JMenu("File");
        menubar.add(menu);

        item = new JMenuItem("Item 1");

        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Quit - not working");
   
        menu.add(item);

    }

    public void quit(){
        // SHUT DOWN YOUR OWN FRAME AND TIMER HERE
        simTimer.stop();
        frame.dispose();
        sim.endSimulation();
        frame.setVisible(false);
        frame.dispose();
        // System.exit(0);
    }

    public Simulator getSimulator(){ 
        return sim;
    }

}

