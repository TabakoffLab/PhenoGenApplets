/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JPanel;


/**
 *
 * @author smahaffey
 */
public class HeatMapGraphicsPanel extends JPanel{
    
    HeatMapData hmd=new HeatMapData(HeatMapData.TWO_COLOR);
    ArrayList<ProbeSet> probes;
    int currentDimX=20;
    int currentDimY=20;
    int cellwidth=0;
    int cellheight=0;
    int height=400;
    int width=400;
    int DimR=5;
    int DimC=5;
    Rectangle2D.Double[][] heatMapRects=new Rectangle2D.Double[DimR][DimC];
    boolean displayNum=false;
    DecimalFormat df = new DecimalFormat("#############.##");
    
    public HeatMapGraphicsPanel(){
        setup();
    }
    
    public void setCurrentSize(int height,int width){
        this.height=height;
        this.width=width;
        setup();
    }
    
    public void setup(){
        this.cellheight=currentDimY;
        this.cellwidth=currentDimX;
        //double cellheight=height/heatMapRects.length;
        //double cellwidth=width/heatMapRects[0].length;
        for(int i=0;i<heatMapRects.length;i++){
            for(int j=0;j<heatMapRects[i].length;j++){
                heatMapRects[i][j]=new Rectangle2D.Double(j*cellwidth,i*cellheight,cellwidth,cellheight);
            }
        }
    }
    
    public void setDisplayNum(boolean disp){
        displayNum=disp;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        float fontSize=(float) (currentDimY/2.0);
        
        g2.setFont(g2.getFont().deriveFont(fontSize));
        
        for(int i=0;i<heatMapRects.length;i++){
            for(int j=0;j<heatMapRects[i].length;j++){
                //g.drawRect(i*5, j*5, (i+1)*5, (j+1)*5);
                g2.setColor(hmd.getColor(i,j));
                g2.draw(heatMapRects[i][j]);
                g2.fill(heatMapRects[i][j]);
                if(displayNum){
                    g2.setColor(Color.WHITE);
                    String toDraw=df.format(hmd.getValueAt(i, j));
                    if(toDraw.equals("0")){
                        toDraw="<0.01";
                    }else if(toDraw.equals("-0")){
                        toDraw=">-0.01";
                    }
                    Rectangle2D namerect=g.getFont().getStringBounds(toDraw, g.getFontMetrics().getFontRenderContext());
                    double len=namerect.getMaxX()-namerect.getMinX();
                    double height=namerect.getMaxY()-namerect.getMinY();
                    int xStart=(int)heatMapRects[i][j].getX();
                    int yStart=(int)heatMapRects[i][j].getY();
                    int x=xStart+(currentDimX/2-(int)len/2);
                    int y=yStart+(currentDimY/2+(int)height/2);
                    g2.drawString(toDraw,x ,y );
                }
            }
            
        }
            
        if(DimR>0){
            
        }else{
            g2.drawString("No Probesets to View. Please adjust filters if all exons are excluded.", 5, 15);
        }
        
    }
    private String reverse(String s){
        String ret="";
        for(int i=s.length()-1;i>=0;i--){
            ret=ret+s.charAt(i);
        }
        return ret;
    }
    public void setCurDim(int currentDimX,int currentDimY) {
        this.currentDimX=currentDimX;
        this.currentDimY=currentDimY;
        int sizeMaxw=currentDimX*DimC;
        int sizeMaxh=currentDimY*(DimR+1);
        this.setPreferredSize(new Dimension(sizeMaxw,sizeMaxh));
        setup();
    }
    
    public int getProbesetCount(){
        return hmd.getSize();
    }

    public void setHeatMapData(HeatMapData generateMapValues) {
        this.hmd=generateMapValues;
        probes=hmd.getIncludedProbes();
        DimR=hmd.getRows();
        DimC=hmd.getColumns();
        heatMapRects=new Rectangle2D.Double[DimR][DimC];
        int sizeMaxw=currentDimX*DimC;
        int sizeMaxh=currentDimY*(DimR+1);
        this.setPreferredSize(new Dimension(sizeMaxw,sizeMaxh));
        setup();
    }

    public HeatMapData getHmd() {
        return hmd;
    }

    /*public void setToolTipText(Point point) {
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
            String tooltip="<html><b>Value:"+hmd.getValueAt(row,col)+"</b><BR>";
            tooltip=tooltip+probes.get(row).getProbeSetID()+" vs "+probes.get(col).getProbeSetID()+"</html>";
            this.setToolTipText(tooltip);
        }
    }*/
    
}
