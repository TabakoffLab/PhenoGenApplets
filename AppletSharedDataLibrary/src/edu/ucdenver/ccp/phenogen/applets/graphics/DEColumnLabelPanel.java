/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.Exon;
import edu.ucdenver.ccp.phenogen.applets.data.Transcript;
import edu.ucdenver.ccp.phenogen.applets.data.TranscriptElement;
import java.awt.*;
import javax.swing.JPanel;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/**
 *
 * @author smahaffey
 */
public class DEColumnLabelPanel extends JPanel{
    HashMap top,bottom;
    int currentDimX=20;
    int currentDimY=20;
    
    
    public DEColumnLabelPanel(){
        this.setBackground(Color.WHITE);
    }
   
    
    public void setLabels(HashMap tissues,HashMap strains){
        if(strains.size()==1){
            top=strains;
            bottom=tissues;
        }else{
            top=tissues;
            bottom=strains;
        }
       
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font(g2.getFont().getName(), g2.getFont().getStyle(), 16));
        if(top!=null){
            int twidth=bottom.size()*currentDimX;
            for(int i=0;i<top.size();i++){
                String tmpTissue=(String)top.get(i);
                Rectangle2D namerect=g.getFont().getStringBounds(tmpTissue, g.getFontMetrics().getFontRenderContext());
                double len=namerect.getMaxX()-namerect.getMinX();
                int posX=(twidth*i)+(twidth/2)-(int)len/2;
                if(bottom.size()>1){
                    g2.drawLine((twidth*i)+(currentDimX/2), 20, (twidth*i)+twidth-(currentDimX/2), 20);
                    g2.drawLine((twidth*i)+(currentDimX/2), 20,(twidth*i)+(currentDimX/2), 37);
                    g2.drawLine((twidth*i)+twidth-(currentDimX/2), 20,(twidth*i)+twidth-(currentDimX/2), 37);
                }
                g2.drawString(tmpTissue, posX, 15);
                for(int j=0;j<bottom.size();j++){
                    String tmpStrain=(String)bottom.get(j);
                    namerect=g.getFont().getStringBounds(tmpStrain, g.getFontMetrics().getFontRenderContext());
                    len=namerect.getMaxX()-namerect.getMinX();
                    posX=(twidth*i)+(j*currentDimX)+(currentDimX/2)-(int)len/2;
                    g2.drawString(tmpStrain, posX, 50);
                }
            }
        }
        
    }

    public void setCurDim(int curDimX,int curDimY) {
        this.currentDimX=curDimX;
        this.currentDimY=curDimY;
        int sizeMax=currentDimX*(top.size()*bottom.size());
        this.setPreferredSize(new Dimension(sizeMax,60));
        this.revalidate();
    }

    

   
    
}
