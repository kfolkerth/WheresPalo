//represents the central compass that rotates based on current bearing
package wherespalo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import internetsources.RotatedIcon;

public class CompassPanel extends JPanel {
    private final int TIMER_DELAY = 1; 
    private double COMPASS_SWING = .05, TOLERANCE = 0.1;
    private boolean needleLeft = true;
    private ImageIcon compassImage;
    private Timer compassTimer;
    private JLabel bearingLabel;
    private Double displayedBearing, compassBearing;
    private RotatedIcon ri;
    
    public CompassPanel(double displayedBearing) {
        
        compassBearing = 360.0;
        this.displayedBearing = displayedBearing;
        compassImage = new ImageIcon("compass.png");
        compassTimer = new Timer(TIMER_DELAY, new CompassListener());
        ri = new RotatedIcon(compassImage, RotatedIcon.Rotate.ABOUT_CENTER);
        
        bearingLabel = new JLabel("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        add (bearingLabel);
        compassTimer.start();
    }
    
    @Override
    public void paintComponent (Graphics page) {
        super.paintComponent(page);

        ri.paintIcon(this, page, 250, 150);
        
    }
    public void setDisplayedBearing(double newBearing) {
        
        displayedBearing = newBearing;
        bearingLabel.setText("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        
        determineNeedleDirection();
        
        compassTimer.restart();
    }
    
    private class CompassListener implements ActionListener {
        
        @Override
        public void actionPerformed (ActionEvent event) {
            
            if (Math.abs(compassBearing - displayedBearing) < TOLERANCE)  {
                compassTimer.stop();
            }
            else
            {
                if (!needleLeft) {
                    compassBearing = (compassBearing + COMPASS_SWING) % 360;
                    ri = new RotatedIcon(compassImage, compassBearing);
                    System.out.println(compassBearing);
                }
                else
                {
                    if (compassBearing < 1.0) //ensures bearing jumps from 0 to 360
                        compassBearing  = 359.0;
                    compassBearing = Math.abs(compassBearing - COMPASS_SWING) % 360; //needle set only to travel right at this time
                    ri = new RotatedIcon(compassImage, compassBearing);
                    System.out.println(compassBearing);
                }
                repaint();
            }
        }
    }
    
    //logic in this method is still flawed and needs some work
    private void determineNeedleDirection() {
        if (needleLeft) {
            if (displayedBearing > 180)
                needleLeft = false;
        } else {
            if (Math.abs(displayedBearing - compassBearing) > 180)
                needleLeft = true;
        }
    }
}