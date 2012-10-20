package wherespalo;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;

//represents main content panel for the where's palo program
public class PaloPanel extends JPanel {
    
    private CompassPanel compass;
    private Location current, palo;
    private JButton update;
    private JSlider bearingSlider;
    private JLabel distance;
    private boolean imperial;
    private PaloUtilityPanelEast eastPanel;
    private NumberFormat format;
    private double currentBearing, currentDistance, displayedBearing;


    public PaloPanel() {
        current = new Location(37.275290, -107.880079); //default location of Durango, CO
        palo = new Location(33.407299, -111.9548224); //location of the Palo Verde Lounge
        
        format = new DecimalFormat("#####.###");
        
        imperial = true; //uses imperial units by default
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        
        eastPanel = new PaloUtilityPanelEast(current.getLat(), current.getLong());

        update = new JButton("Update");
        update.setMnemonic('d');
        update.setToolTipText("Recalculates your distance");
        update.addActionListener(new ButtonListener());
        
        bearingSlider = new JSlider(JSlider.VERTICAL,  0, 360, 0); //represents user's current bearing, for eventual use with compass
        bearingSlider.setMajorTickSpacing(60);
        bearingSlider.setMinorTickSpacing(30);
        bearingSlider.setPaintTicks(true);
        bearingSlider.setPaintLabels(true);
        bearingSlider.setToolTipText("Your current bearing.");
        currentBearing = bearingSlider.getValue();
        bearingSlider.addChangeListener(new SliderListener());
        
        currentDistance = Double.parseDouble(format.format(palo.calculateDistance(current, imperial)));
        distance = new JLabel("Your distance from the Palo is " + currentDistance + " miles.");
        distance.setHorizontalAlignment(JLabel.CENTER);
        
        displayedBearing = Double.parseDouble(format.format(current.calculateBearing(palo, currentBearing)));
        compass = new CompassPanel(displayedBearing);
                
        add (update, BorderLayout.SOUTH);
        add (bearingSlider, BorderLayout.WEST);
        add (distance, BorderLayout.NORTH);
        add (eastPanel, BorderLayout.EAST);
        add (compass, BorderLayout.CENTER);
    }
    
    //represents button listener for update button
    private class ButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed (ActionEvent event) {
            
            currentDistance = Double.parseDouble(format.format(palo.calculateDistance(current, imperial)));
            if (imperial) {
                distance.setText("Your distance from the Palo is " + currentDistance + " miles.");
            }
            else {
                distance.setText("Your distance from the Palo is " + currentDistance + " kilometers");
            }
            displayedBearing = Double.parseDouble(format.format(current.calculateBearing(palo, currentBearing)));
            compass.setDisplayedBearing(displayedBearing);
        }
    }
    
    //listener for current bearing slider
    private class SliderListener implements ChangeListener {
        
        @Override
        public void stateChanged(ChangeEvent event) {

            currentBearing = bearingSlider.getValue();
        }
    }    
    //panel consisting of a combo box and two text fields that represent user's choice of units along with
    //current latitude and longitude
    private class PaloUtilityPanelEast extends JPanel {
        private String[] unitOptions = new String[2];
        private JTextField latField, longField;
        private JComboBox units;
        private EastPanelListener eListener;

        public PaloUtilityPanelEast(double lat, double longit) {

            setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
            
            eListener = new EastPanelListener();
            
            unitOptions[0] = "Imperial";
            unitOptions[1] = "Metric";
            units = new JComboBox(unitOptions);
            units.addActionListener(eListener);

            latField = new JTextField(String.valueOf(lat));
            latField.setPreferredSize(new Dimension(100, 100));
            latField.setMaximumSize( latField.getPreferredSize());
            latField.setToolTipText("Your latitude in decimal notation");
            latField.addActionListener(eListener);
            
            longField = new JTextField(String.valueOf(longit));
            longField.setPreferredSize(new Dimension(100, 100));
            longField.setMaximumSize(longField.getPreferredSize());
            longField.setToolTipText("Your longitude in decimal notation");
            longField.addActionListener(eListener);
        
            add (units);
            add (Box.createVerticalGlue());
            add (latField);
            add (Box.createVerticalGlue());
            add (longField);
        
        }
        
        //represents listener for the three components of the GUI's east panel
        private class EastPanelListener implements ActionListener {
        
            @Override
            public void actionPerformed (ActionEvent event) {
            
                if (event.getSource() == units) {
                    if (units.getSelectedIndex() == 0) {
                        imperial = true;
                    }
                    else {
                        imperial = false;
                    }
                }
                else if (event.getSource() == latField) {
                    try {
                        double newLat = Double.parseDouble(latField.getText());
                        current.setLat(newLat);
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "You must enter a valid latitude in decimal format.", "You broke it.", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    try {
                        double newLong = Double.parseDouble(longField.getText());
                        current.setLong(newLong);
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "You must enter a valid longitude in decimal format.", "You broke it.", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}