/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author smahaffey
 */
public class ExonProbePanel extends JPanel{
   
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        int height=(int)this.getSize().getHeight();
        int width=(int)this.getSize().getWidth();
        g2.setColor(Color.BLUE);
        g2.drawString("5'", 2, 15);
        g2.drawString("3'", width-20,15);
        
    }
}
