/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.Exon;
import edu.ucdenver.ccp.phenogen.applets.data.ProbeSet;
import edu.ucdenver.ccp.phenogen.applets.data.Transcript;
import edu.ucdenver.ccp.phenogen.applets.data.TranscriptElement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author smahaffey
 */
public class DERowLabelPanel extends JPanel{
    private int currentDimY=20;
    private int currentDimX=20;
    private int numProbesets=0;
    ArrayList<String> probesetID=new ArrayList<String>();
    HashMap probesetList=null;
    
    public DERowLabelPanel(){
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(65,1000));
    }
   
    public void setProbeset(ArrayList<String> probesetIDs,HashMap m){
        this.probesetID=probesetIDs;
        this.probesetList=m;
        int numProbesets=probesetID.size();
        int sizeMaxY=currentDimY*(numProbesets+1);
        int sizeMaxX=65;
        if(currentDimY>20){
            //sizeMaxX=currentDim*2+15;
        }
        this.setPreferredSize(new Dimension(sizeMaxX,sizeMaxY));
    }
    
    
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        int fontSize=currentDimY/2;
        g2.setFont(g2.getFont().deriveFont(fontSize));
        int curYHalf=currentDimY/2;
        for(int i=0;i<this.probesetID.size();i++){
              Rectangle2D namerect=g.getFont().getStringBounds(probesetID.get(i), g.getFontMetrics().getFontRenderContext());
                int h=(int)(namerect.getMaxY()-namerect.getMinY());
              g2.setColor(findColor(probesetID.get(i)));
              g2.drawString(probesetID.get(i), 2, (curYHalf+currentDimY*i)+h/2);
        }
    }

    public void setCurDim(int currentDimX,int currentDimY) {
        this.currentDimX=currentDimX;
        this.currentDimY=currentDimY;
        int sizeMaxY=currentDimY*(numProbesets+1);
        int sizeMaxX=65;
        if(currentDimY>20){
            //sizeMaxX=currentDim*2+15;
        }
        this.setPreferredSize(new Dimension(sizeMaxX,sizeMaxY));
    }
    
    private Color findColor(String id){
        Color ret=Color.BLACK;
        if(this.probesetList.containsKey(id)){
            ProbeSet ps=(ProbeSet) probesetList.get(id);
            if(ps.getAnnotation().equals("full")){
                ret=new Color(0,100,0);
            }else if(ps.getAnnotation().equals("extended")){
                ret=new Color(0,0,255);
            }else if(ps.getAnnotation().equals("core")){
                ret=new Color(255,0,0);
            }
        }
        return ret;
    }

}
