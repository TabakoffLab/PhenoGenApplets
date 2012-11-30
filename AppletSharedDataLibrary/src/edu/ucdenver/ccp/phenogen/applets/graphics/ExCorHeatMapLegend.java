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
public class ExCorHeatMapLegend extends JComponent{
    
    
    
    public ExCorHeatMapLegend() {
        
    }
    
    protected void paintComponent(Graphics g) {
        int width=this.getWidth();
        int height=this.getHeight();
        //System.out.println("width:"+width+"  Height:"+height);
        int xIndent=25;
        int yIndentTop=35;
        int yIndentBottom=20;
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        int xleft=xIndent;
        int xmid=width/2;
        int xright=width-xIndent;
        int rectH=height-yIndentBottom-yIndentTop;
        int rectW=width/2-xIndent;
        GradientPaint gp=new GradientPaint(xmid,0,Color.BLACK,xright,height,Color.RED,false);
        GradientPaint gp2=new GradientPaint(xleft,0,Color.GREEN,xmid,height,Color.BLACK,false);
        
        Rectangle2D.Double rect=new Rectangle2D.Double(xmid, yIndentTop, rectW, rectH);
        Rectangle2D.Double rect2=new Rectangle2D.Double(xleft, yIndentTop, rectW, rectH);
        g2.setPaint(gp);
        g2.fill(rect);
        g2.setPaint(gp2);
        g2.fill(rect2);
        g2.setColor(Color.BLACK);
        g2.drawString("1", xright-2, height-3);
        g2.drawString("0",xmid-2,height-3);
        g2.drawString("-1",xleft-2,height-3);
    }
}
