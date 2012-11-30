/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author smahaffey
 */
public class URLImagePanel extends JPanel{
    URL url=null;
    BufferedImage img=null;
    String name="";
    
    public URLImagePanel(){
        
    }
    
    public URLImagePanel(String url,String name){
        this.setURL(url,name);
        this.name=name;
    }
    
    public void setURL(String url,String name){
        this.name=name;
        try {
            this.url=new URL(url);
            try {
                img = ImageIO.read(this.url);
                Dimension dim=new Dimension(img.getWidth(), img.getHeight()+30);
                this.setSize(dim);
                this.setPreferredSize(dim);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                Logger.getLogger(URLImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(URLImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void paintComponent(Graphics g) {
        int width=this.getWidth();
        g.setFont(new Font(g.getFont().getName(),g.getFont().getStyle(),16));
        Rectangle2D namerect=g.getFont().getStringBounds(name, g.getFontMetrics().getFontRenderContext());
        double length=namerect.getMaxX()-namerect.getMinX();
        int half=(int)(length/2);
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawString(name, (width/2)-half, 23);
        g.drawImage(img, 0, 30, null);
    }
    
    
}
