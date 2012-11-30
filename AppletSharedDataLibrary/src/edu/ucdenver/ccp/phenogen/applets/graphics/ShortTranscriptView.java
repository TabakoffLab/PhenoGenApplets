/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.Exon;
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
import javax.swing.JPanel;

/**
 *
 * @author smahaffey
 */
public class ShortTranscriptView extends JPanel{
    Transcript trans=null;
    ArrayList<TranscriptElement> elementList=new ArrayList<TranscriptElement>();
    ArrayList<RoundRectangle2D.Float> elementRects=new ArrayList<RoundRectangle2D.Float>();
    ArrayList<Line2D> elementLines=new ArrayList<Line2D>();
    private int currentDim=5;
    private int numProbesets=0;
    private int horPosIncld=20;
    private boolean displayIntrons=false;
    
    public ShortTranscriptView(){
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(45,200));
    }
   
    
    public void setTranscript(Transcript t,boolean displayIntrons){
        this.trans=t;
        this.displayIntrons=displayIntrons;
        this.numProbesets=this.trans.getProbeSetCount(displayIntrons);
        //System.out.println("Selected Transcript:"+trans.getID()+":"+trans.getExonLength());
        this.setupGraphics();
    }
    
    private void setupGraphics(){
        elementRects=new ArrayList<RoundRectangle2D.Float>();
        elementLines=new ArrayList<Line2D>();
        /*if(!displayIntrons){
            if(trans!=null){
                int offset = (int) ((int) currentDim * 0.2);
                if (offset < 1) {
                    offset = 1;
                }
                int mappedInd=0;
                int curStartPosY=0;
                ArrayList<Exon> ex = trans.getExons();
                for (int i = 0; i < ex.size(); i++) {
                    //System.out.println("Short:Exon:"+ex.get(i).getID()+":"+ex.get(i).isExclude());
                    if (!ex.get(i).isExclude()) {
                        exonlist.add(ex.get(i));
                        int vertPos = curStartPosY + offset;
                        int horPos=this.horPosIncld;
                        int length=(currentDim *ex.get(i).getIncludedProbeSetCount(false)) - (offset * 2);
                    exonRects.add(new RoundRectangle2D.Float(horPos, vertPos,20, length, 4, 4));
                    if(i>0 && !ex.get(i-1).isExclude()){
                            int y1=(int) ((int)exonRects.get(mappedInd).getCenterY()-(exonRects.get(mappedInd).getHeight()/2.0));
                            int x1=(int)(exonRects.get(mappedInd).getCenterX());
                            int y2=(int) ((int)exonRects.get(mappedInd-1).getCenterY()+(exonRects.get(mappedInd-1).getHeight()/2.0));
                            int x2=(int)(exonRects.get(mappedInd-1).getCenterX());
                            intronLines.add(new Line2D.Float(x1,y1,x2,y2));
                    }
                    mappedInd++;
                    curStartPosY=curStartPosY+(currentDim *ex.get(i).getIncludedProbeSetCount(false));
                    }
                }
            }
        }else{*/
            if(trans!=null){
                int offset = (int) ((int) currentDim * 0.2);
                if (offset < 1) {
                    offset = 1;
                }
                int mappedInd=0;
                int curStartPosY=0;
                ArrayList<TranscriptElement> elem = trans.getIncludedTranscriptElements(displayIntrons);
                for (int i = 0; i < elem.size(); i++) {
                    //System.out.println("Short:Exon:"+ex.get(i).getID()+":"+ex.get(i).isExclude());
                    TranscriptElement curElem=elem.get(i);
                    if (!curElem.isExclude()) {
                        elementList.add(curElem);
                        int vertPos = curStartPosY + offset;
                        int width=20;
                        int horPos = this.horPosIncld;
                        int length = (currentDim * curElem.getIncludedProbeSetCount(displayIntrons)) - (offset * 2);
                        if(curElem.getType().equals("intron")){
                            horPos=horPos+width/2;
                            width=2;
                        }
                        elementRects.add(new RoundRectangle2D.Float(horPos, vertPos, width, length, 4, 4));
                        if (i > 0 && !elem.get(i - 1).isExclude()) {
                            int y1 = (int) ((int) elementRects.get(mappedInd).getCenterY() - (elementRects.get(mappedInd).getHeight() / 2.0));
                            int x1 = (int) (elementRects.get(mappedInd).getCenterX());
                            int y2 = (int) ((int) elementRects.get(mappedInd - 1).getCenterY() + (elementRects.get(mappedInd - 1).getHeight() / 2.0));
                            int x2 = (int) (elementRects.get(mappedInd - 1).getCenterX());
                            elementLines.add(new Line2D.Float(x1, y1, x2, y2));
                        }
                        mappedInd++;
                        curStartPosY = curStartPosY + (currentDim * curElem.getIncludedProbeSetCount(displayIntrons));
                    }
                }
            }
        //}
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        int height=(int)this.getSize().getHeight();
        int width=(int)this.getSize().getWidth();
        g2.setColor(Color.BLUE);
        g2.drawString("5'", 2, 15);
        g2.drawString("3'", 2,height-5);
        for(int i=0;i<elementRects.size();i++){
                g2.setColor(Color.LIGHT_GRAY);
                g2.fill(elementRects.get(i));
                if(elementList.get(i).getType().equals("exon")){
                    Exon tmpEx=(Exon)elementList.get(i);
                    if(!tmpEx.isFullCoding()){
                        int tmpheight=10;
                        if(tmpEx.getPercentNonCoding()>0.1){
                            tmpheight=(int)(tmpEx.getPercentNonCoding()*elementRects.get(i).getHeight());
                        }else{
                            tmpheight=(int)(0.1*elementRects.get(i).getHeight());
                        }
                        RoundRectangle2D.Float temp= new RoundRectangle2D.Float((int)elementRects.get(i).getX(), (int)elementRects.get(i).getY(),20,tmpheight, 4, 4);
                        g2.setColor(Color.WHITE);
                        g2.fill(temp);
                    }
                }
                g2.setColor(Color.DARK_GRAY);
                g2.draw(elementRects.get(i));
                
        }
        g2.setColor(Color.BLACK);
        for(int i=0;i<elementLines.size();i++){
            g2.draw(elementLines.get(i));
        }
    }

    public void setCurDim(int currentDim) {
        this.currentDim=currentDim;
        int sizeMax=currentDim*numProbesets+70;
        this.setPreferredSize(new Dimension(45,sizeMax));
        setupGraphics();
        this.revalidate();
    }

    /*public void setNumberOfProbeSets(int probesetCount) {
        this.numProbesets=probesetCount;
        setCurDim(currentDim);
    }*/

    public TranscriptElement getElementMouseIsOver(Point p){
        TranscriptElement ret=null;
        for(int i=0;i<elementRects.size()&&ret==null;i++){
            if(elementRects.get(i).contains(p)){
                ret=elementList.get(i);
            }
        }
        return ret;
    }
}
