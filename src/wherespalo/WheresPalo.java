package wherespalo;

import javax.swing.*;

//driver for where's palo application
public class WheresPalo {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Where's Palo?");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().add(new PaloPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}