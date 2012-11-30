/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.HeatMapData;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import javax.swing.JComponent;

/**
 *
 * @author smahaffey
 */
public class HeatMapLegend extends JComponent{
    
    HeatMapData hmd=null;
    
    boolean twoColor=false;
    boolean oneColor=false;
    
    Color colorMax=null;
    Color colorMid=null;
    Color colorMin=null;
    
    String minStr="";
    String midStr="";
    String maxStr="";
    String label="";
    
    public HeatMapLegend() {
        this.setPreferredSize(new Dimension(200,50));
    }
    
    public void setHeatMapData(HeatMapData hm){
        hmd=hm;
        DecimalFormat df = new DecimalFormat("#############.#");
        if(hmd.isTwoColor()){
            twoColor=true;
            oneColor=false;
            colorMax=hmd.getMaxColor();
            colorMin=hmd.getMinColor();
            colorMid=hmd.getMidColor();
            minStr=df.format(hmd.getMin());
            maxStr=df.format(hmd.getMax());
            midStr=df.format(hmd.getMid());
        }else{
            oneColor=true;
            twoColor=false;
            colorMax=hmd.getMaxColor();
            colorMin=hmd.getMinColor();
            minStr=df.format(hmd.getMin());
            maxStr=df.format(hmd.getMax());
        }
    }
    
    
    /**
     *
     * @param label
     */
    public void setHeatMapLabel(String label){
        this.label=label;
    }
    
    
    
    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        int width=this.getWidth();
        int height=this.getHeight();
        //System.out.println("width:"+width+"  Height:"+height);
        int xIndent=25;
        int yIndentTop=12;
        int yIndentBottom=15;
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.WHITE);
        //g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        int xleft=xIndent;
        int xmid=width/2;
        int xright=width-xIndent;
        int rectH=height-yIndentBottom-yIndentTop;
        int rectW=width/2-xIndent;
        
        if(twoColor){
            GradientPaint gp=new GradientPaint(xmid,0,colorMid,xright,height,colorMax,false);
            GradientPaint gp2=new GradientPaint(xleft,0,colorMin,xmid,height,colorMid,false);

            Rectangle2D.Double rect=new Rectangle2D.Double(xmid, yIndentTop, rectW, rectH);
            Rectangle2D.Double rect2=new Rectangle2D.Double(xleft, yIndentTop, rectW, rectH);
            g2.setPaint(gp);
            g2.fill(rect);
            g2.setPaint(gp2);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            Rectangle2D namerect=g.getFont().getStringBounds(maxStr, g.getFontMetrics().getFontRenderContext());
            int len=(int)(namerect.getMaxX()-namerect.getMinX());
            g2.drawString(maxStr, xright-len, height-3);
            namerect=g.getFont().getStringBounds(midStr, g.getFontMetrics().getFontRenderContext());
            len=(int)(namerect.getMaxX()-namerect.getMinX());
            g2.drawString(midStr,xmid-(len/2),height-3);
            g2.drawString(minStr,xleft-2,height-3);
        }else if(oneColor){
            GradientPaint gp=new GradientPaint(xleft,0,colorMin,(xright-xleft),height,colorMax,false);

            Rectangle2D.Double rect=new Rectangle2D.Double(xleft, yIndentTop, (xright-xleft), rectH);
            g2.setPaint(gp);
            g2.fill(rect);
            g2.setColor(Color.BLACK);
            
            Rectangle2D namerect=g.getFont().getStringBounds(maxStr, g.getFontMetrics().getFontRenderContext());
            int len=(int)(namerect.getMaxX()-namerect.getMinX());
            g2.drawString(maxStr, xright-len, height-3);
            g2.drawString(minStr,xleft-2,height-3);
        }
        if(label!=null&&!label.equals("")){
            int mid=width/2;
            Rectangle2D namerect=g.getFont().getStringBounds(label, g.getFontMetrics().getFontRenderContext());
            int len=(int)(namerect.getMaxX()-namerect.getMinX());
            int x=(mid-((int)(len/2)));
            g2.drawString(label, x,10);
        }
        //g2.drawString("test"+twoColor+oneColor,xmid-2,height-3);
    }
}
