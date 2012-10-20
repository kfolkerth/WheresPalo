//represents the central compass that rotates based on current bearing
package wherespalo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CompassPanel extends JPanel {
    private final int COMPASS_HALFWAY = 180, TIMER_DELAY = 1; 
    private double COMPASS_SWING = .1, TOLERANCE = 0.1;
    private boolean rotating = false, needleLeft = false;
    private ImageIcon compassImage;
    private Timer compassTimer;
    private JLabel bearingLabel;
    private Double displayedBearing, compassBearing;
    
    public CompassPanel(double displayedBearing) {
        
        compassBearing = 0.0;
        this.displayedBearing = displayedBearing;
        compassImage = new ImageIcon("compass.png");
        compassTimer = new Timer(TIMER_DELAY, new CompassListener());
        
        bearingLabel = new JLabel("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        add (bearingLabel);
    }
    
    @Override
    public void paintComponent (Graphics page) {
        super.paintComponent(page);
        Graphics2D g2 = (Graphics2D) page;
        
        if (!rotating) {
            compassImage.paintIcon(this, page, 175, 150);
        } else {
            g2.rotate(Math.toRadians(compassBearing), 485, 345);
            compassImage.paintIcon(this, page, 175, 150);
        }
    }
    public void setDisplayedBearing(double newBearing) {
        
        displayedBearing = newBearing;
        bearingLabel.setText("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        if (COMPASS_HALFWAY - displayedBearing > 0)
            needleLeft = false;
        else
            needleLeft = true;
        compassTimer.restart();
    }
    
    private class CompassListener implements ActionListener {
        
        @Override
        public void actionPerformed (ActionEvent event) {
            
            if (Math.abs(compassBearing - displayedBearing) < TOLERANCE) {
                rotating = false;
                compassTimer.stop();
            }
            else
            {
                rotating = true;
                if (!needleLeft) {
                    compassBearing = (compassBearing + COMPASS_SWING) % 360;
                    System.out.println(compassBearing);
                }
                else
                {
                    compassBearing = Math.abs((compassBearing - COMPASS_SWING) % 360);
                    System.out.println(compassBearing);
                }
                repaint();
            }
        }
    }
}