//represents the central compass that rotates based on current bearing
package wherespalo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import internetsources.RotatedIcon;

public class CompassPanel extends JPanel {
    private final int COMPASS_HALFWAY = 180, TIMER_DELAY = 1; 
    private double COMPASS_SWING = .05, TOLERANCE = 0.1;
    private boolean rotating = false, needleLeft = false;
    private ImageIcon compassImage;
    private Timer compassTimer;
    private JLabel bearingLabel;
    private Double displayedBearing, compassBearing;
    private Dimension labelDimension;
    private RotatedIcon ri;
    
    public CompassPanel(double displayedBearing) {
        
        compassBearing = 360.0;
        this.displayedBearing = displayedBearing;
        compassImage = new ImageIcon("compass.png");
        compassTimer = new Timer(TIMER_DELAY, new CompassListener());
        ri = new RotatedIcon(compassImage, RotatedIcon.Rotate.ABOUT_CENTER);
        
        bearingLabel = new JLabel("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        labelDimension = this.getSize();
        add (bearingLabel);
    }
    
    @Override
    public void paintComponent (Graphics page) {
        super.paintComponent(page);

        ri.paintIcon(this, page, 175, 150);
        
    }
    public void setDisplayedBearing(double newBearing) {
        
        displayedBearing = newBearing;
        bearingLabel.setText("Your current bearing towards the Palo is " + displayedBearing + " degrees.");
        if (displayedBearing - compassBearing > 0)
            needleLeft = true;
        else
            needleLeft = false;
        System.out.println(needleLeft);
        compassTimer.restart();
    }
    
    private class CompassListener implements ActionListener {
        
        @Override
        public void actionPerformed (ActionEvent event) {
            
            if (Math.abs(compassBearing - displayedBearing) < TOLERANCE)  {
                rotating = false;
                compassTimer.stop();
            }
            else
            {
                rotating = true;
                if (!needleLeft) {
                    compassBearing = (compassBearing + COMPASS_SWING) % 360;
                    ri = new RotatedIcon(compassImage, compassBearing);
                    System.out.println(compassBearing);
                }
                else
                {
                    if (compassBearing -360.0 <= TOLERANCE && compassBearing - 360.0 > 0)
                        compassBearing = 359.0;
                    else
                        compassBearing = Math.abs(compassBearing - COMPASS_SWING); //needle set only to travel right at this time
                    ri = new RotatedIcon(compassImage, compassBearing);
                    System.out.println(compassBearing);
                }
                repaint();
            }
        }
    }
}