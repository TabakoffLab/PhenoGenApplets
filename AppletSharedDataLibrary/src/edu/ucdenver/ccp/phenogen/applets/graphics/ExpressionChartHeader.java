/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author smahaffey
 */
public class ExpressionChartHeader extends JPanel {
    String[] strains=null;
    String[] rStrains=null;
    String tissue=null;
    int xOffset=55;
    int fontSize=14;
    int margin=0;

    
    
    public ExpressionChartHeader(){
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100,100));
        this.setSize(100, 100);
    }
    
    public void setLabels(String[] strains, String tissue){
        this.strains=strains;
        this.tissue=tissue;
        rStrains=new String[strains.length];
        for(int i=0;i<strains.length;i++){
            rStrains[i]=reverse(strains[i]);
        }
    }
    
    public void setWide(boolean wide){
        if(wide){
            xOffset=60;
            fontSize=14;
            margin=0;
        }else{
            xOffset=50;
            fontSize=14;
            margin=0;
        }
        
    }
    
    protected void paintComponent(Graphics g) {
        String os=System.getProperty("os.name");
        System.out.println("OS:"+os);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setFont(new Font(g2.getFont().getName(), g2.getFont().getStyle(), fontSize));
        int width=this.getWidth();
        int mid=width/2;
        Rectangle2D namerect=g.getFont().getStringBounds(tissue, g.getFontMetrics().getFontRenderContext());
        double numlen=namerect.getMaxX()-namerect.getMinX();
        g2.drawString(tissue, mid-(int)(numlen/2), 15);
        double indivWidth=(width-xOffset-2)/strains.length;
        int height=this.getHeight();
        if(strains!=null){
            Font prevFont=g2.getFont();
            g2.setFont(g2.getFont().deriveFont(9));
            AffineTransform fontAT = new AffineTransform();
            Font theFont = g2.getFont();
            fontAT.rotate(270 * java.lang.Math.PI/180 );
            Font theDerivedFont = theFont.deriveFont(fontAT);
            g2.setFont(theDerivedFont);
            if(os.contains("Mac")){
                for(int i=0;i<strains.length;i++){
                    namerect=g.getFont().getStringBounds(rStrains[i], g.getFontMetrics().getFontRenderContext());
                    numlen=namerect.getMaxX()-namerect.getMinX();
                    //double numHeight=namerect.getMaxY()-namerect.getMinY();
                    //int halfW=indivWidth/2+(int)(numHeight/2);
                    int y =(int) (height-2-numlen);
                    int x=(int)((i+1)*indivWidth)+xOffset;
                    g2.drawString(rStrains[i], x, y); 
                    g2.setColor(Color.black);
                }
            }else{
                System.out.println("Alt OS:"+os);
                for(int i=0;i<strains.length;i++){
                    namerect=g.getFont().getStringBounds(strains[i], g.getFontMetrics().getFontRenderContext());
                    numlen=namerect.getMaxX()-namerect.getMinX();
                    //double numHeight=namerect.getMaxY()-namerect.getMinY();
                    //int halfW=(int)(numHeight/2)+indivWidth/2;
                    //int y =(int) (height-2-numlen);
                    //int x=i*indivWidth+halfW+xOffset;
                    int y=height-2;
                    int x=(int)((i+1)*indivWidth)+xOffset;
                    g2.drawString(strains[i], x, y); 
                    g2.setColor(Color.black);
                }
            }
            g2.setFont(prevFont);
        }
        
    }
    private String reverse(String s){
        String ret="";
        for(int i=s.length()-1;i>=0;i--){
            ret=ret+s.charAt(i);
        }
        return ret;
    }
}
