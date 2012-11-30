/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.ExCorHeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.HeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.ProbeSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;


/**
 *
 * @author smahaffey
 */
public class ExCorHeatMapGraphicsPanel extends JPanel{
    
    //HeatMapData hmd=new HeatMapData(HeatMapData.TWO_COLOR);
    ExCorHeatMapData eHmd=new ExCorHeatMapData(HeatMapData.TWO_COLOR);
    ArrayList<ProbeSet> probes;
    int currentDim=20;
    int cellwidth=0;
    int cellheight=0;
    int height=400;
    int width=400;
    int Dim=5;
    Rectangle2D.Double[][] heatMapRects=new Rectangle2D.Double[Dim][Dim];
    
    public ExCorHeatMapGraphicsPanel(){
        setup();
    }
    
    public void setCurrentSize(int height,int width){
        this.height=height;
        this.width=width;
        setup();
    }
    
    public void setup(){
        this.cellheight=currentDim;
        this.cellwidth=currentDim;
        //double cellheight=height/heatMapRects.length;
        //double cellwidth=width/heatMapRects[0].length;
        for(int i=0;i<heatMapRects.length;i++){
            for(int j=0;j<heatMapRects[i].length;j++){
                heatMapRects[i][j]=new Rectangle2D.Double(j*cellheight,i*cellwidth,cellheight,cellwidth);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        float fontSize=(float) (currentDim/2.0);
        if(fontSize>14){
            fontSize=14;
        }
        g2.setFont(g2.getFont().deriveFont(fontSize));
        
        for(int i=0;i<heatMapRects.length;i++){
            for(int j=0;j<heatMapRects[i].length;j++){
                //g.drawRect(i*5, j*5, (i+1)*5, (j+1)*5);
                g2.setColor(eHmd.getColor(i,j));
                g2.draw(heatMapRects[i][j]);
                g2.fill(heatMapRects[i][j]);
            }
            if(probes!=null&&probes.size()>0){
                g2.setColor(Color.BLACK);
                float x=currentDim*Dim+2;
                Rectangle2D namerect=g.getFont().getStringBounds(probes.get(i).getProbeSetID(), g.getFontMetrics().getFontRenderContext());
                double numlen=namerect.getMaxY()-namerect.getMinY();
                int halfW=(int)(numlen/2);
                float y=(float) ((currentDim*i)+(currentDim/2)+halfW);
                g2.setColor(findColor(i));
                if(probes.get(i).getOppositeStrand()){
                        //g2.setColor(Color.red);
                        g2.drawString("Rev"+probes.get(i).getProbeSetID(), x,y );
                }else{
                    g2.drawString(probes.get(i).getProbeSetID(), x,y );
                }
                g2.setColor(Color.black);
            }
        }
            
        if(probes!=null&&probes.size()>0){
            g2.setColor(Color.black);
            fontSize=(float) (currentDim/2.0);
            if(fontSize>14){
                fontSize=14;
            }
            //g2.setFont(g2.getFont().deriveFont(fontSize));
            /*if(fontSize>=8){
                g2.setFont(g2.getFont().deriveFont(fontSize));
                int height =(int)this.getPreferredSize().getHeight()-4;
                for(int i=0;i<probes.size();i++){
                    Rectangle2D namerect=g.getFont().getStringBounds(probes.get(i).getProbeSetID(), g.getFontMetrics().getFontRenderContext());
                    double numlen=namerect.getMaxX()-namerect.getMinX();
                    int halfW=(int)((cellwidth-numlen)/2);
                    if(probes.get(i).getOppositeStrand()){
                        g2.setColor(Color.red);
                    }
                    g2.drawString(probes.get(i).getProbeSetID(), i*cellwidth+halfW, height);
                    g2.setColor(Color.black);
                }
            }else{
                
                int sHeight =(int)this.getPreferredSize().getHeight()-4;
                for(int i=0;i<probes.size();i++){
                    Rectangle2D namerect=g.getFont().getStringBounds("0", g.getFontMetrics().getFontRenderContext());
                    double numlen=namerect.getMaxX()-namerect.getMinX();
                    int halfW=(int)(numlen/2);
                    int x=i*cellwidth+(cellwidth/2)-halfW;
                    String probeID=probes.get(i).getProbeSetID();
                    for(int j=0;j<probeID.length();j++){
                        int y=sHeight+(15*j);
                        if((j+1)==probeID.length()){
                            g2.drawString(probeID.substring(j), x, y);
                        }else{
                            g2.drawString(probeID.substring(j,j+1), x, y);
                        }
                    }
                }
            }*/
            
            //fontSize=14;
            Font prevFont=g2.getFont();
            g2.setFont(g2.getFont().deriveFont(fontSize));
            
            AffineTransform fontAT = new AffineTransform();
            Font theFont = g2.getFont();
            fontAT.rotate(270 * java.lang.Math.PI/180 );
            Font theDerivedFont = theFont.deriveFont(fontAT);
            g2.setFont(theDerivedFont);
            Rectangle2D namerect=g.getFont().getStringBounds("0", g.getFontMetrics().getFontRenderContext());
            double numlen=namerect.getMaxX()-namerect.getMinX();
            int halfW=(int)(numlen/2);
            int y =(int)currentDim*Dim+10;
            for(int i=0;i<probes.size();i++){
                int x=i*cellwidth+(cellwidth/2)+halfW;
                g2.setColor(findColor(i));
                String toDraw=probes.get(i).getProbeSetID();
                if(probes.get(i).getOppositeStrand()){
                        //g2.setColor(Color.red);
                    toDraw="R"+toDraw;
                }
                g2.drawString(reverse(toDraw), x, y); 
                g2.setColor(Color.black);
            }
            g2.setFont(prevFont);
        }else{
            g2.drawString("No Probesets to View. Please adjust filters if all exons are excluded.", 5, 15);
        }
        
    }
    
    private Color findColor(int index){
        Color ret=Color.BLACK;
        if(index>-1&&index<probes.size()){
            ProbeSet ps=(ProbeSet) probes.get(index);
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
    
    private String reverse(String s){
        String ret="";
        for(int i=s.length()-1;i>=0;i--){
            ret=ret+s.charAt(i);
        }
        return ret;
    }
    public void setCurDim(int currentDim) {
        this.currentDim=currentDim;
        int sizeMaxw=currentDim*(Dim+2);
        int sizeMaxh=currentDim*Dim+70;
        this.setPreferredSize(new Dimension(sizeMaxw,sizeMaxh));
        setup();
    }
    
    public int getProbesetCount(){
        return eHmd.getSize();
    }

    public void setHeatMapData(ExCorHeatMapData generateMapValues) {
        this.eHmd=generateMapValues;
        probes=eHmd.getIncludedProbes();
        Dim=eHmd.getSize();
        heatMapRects=new Rectangle2D.Double[Dim][Dim];
        setup();
    }

    public ExCorHeatMapData getHmd() {
        return eHmd;
    }
    

    public void setToolTipText(Point point) {
        int row=-1, col=-1;
        for(int i=0;i<this.heatMapRects.length&&row==-1;i++){
            for(int j=0;j<this.heatMapRects[i].length&&col==-1;j++){
                if(heatMapRects[i][j].contains(point)){
                    row=i;
                    col=j;
                }
            }
        }
        if(row>-1 && col>-1){
            String tooltip="<html><b>Value:"+eHmd.getValueAt(row,col)+"</b><BR>";
            tooltip=tooltip+probes.get(row).getProbeSetID()+" vs "+probes.get(col).getProbeSetID()+"</html>";
            this.setToolTipText(tooltip);
        }
    }

    
    
}
