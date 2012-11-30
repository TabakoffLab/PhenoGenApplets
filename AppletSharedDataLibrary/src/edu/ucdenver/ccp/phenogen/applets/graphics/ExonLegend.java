/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

    
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 *
 * @author smahaffey
 */
public class ExonLegend extends JComponent{
    public ExonLegend() {
    }
    
    protected void paintComponent(Graphics g) {
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.black);
        //g2.fillRect(0, 0, getWidth(), getHeight());
        g2.drawString("Herit", 2, 20);
        
        g2.drawString("DABG",75,20);
        g2.drawString("Annotation",150,20);
        g2.drawString("Strand",250,20);
        g2.drawString("Not Found",350,20);
        
        g2.setColor(Color.black);
        g2.draw3DRect(40, 2, 23, 23, true);
        g2.fill3DRect(40, 2, 23, 23, true);
    }
}

