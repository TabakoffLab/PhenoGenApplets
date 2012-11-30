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
/**
 *
 * @author smahaffey
 */
public class FullTranscriptView extends JPanel{
    Transcript trans=null;
    String colorBy="filter";
    
    RoundRectangle2D.Float[] elementRects=new RoundRectangle2D.Float[0];
    String[] elementNums=new String[0];
    Line2D[] elementLines=new Line2D[0];
    private int currentDim=5;
    private int numProbesets=0;
    private int vertPosExcld=35;
    private int vertPosIncld=60;
    boolean displayIntrons=false;
    ArrayList<TranscriptElement> elements=null;
    
    public FullTranscriptView(){
        this.setBackground(Color.WHITE);
    }
   
    
    public void setTranscript(Transcript t,boolean displayIntrons){
        this.trans=t;
        this.displayIntrons=displayIntrons;
        numProbesets=this.trans.getProbeSetCount(displayIntrons);
        
        //System.out.println("Selected Transcript:"+trans.getID()+":"+trans.getExonLength());
        this.setupGraphics();
    }

    public String getColorBy() {
        return colorBy;
    }

    public void setColorBy(String colorBy) {
        this.colorBy = colorBy;
    }
    
    private void setupGraphics(){
        /*if(!displayIntrons){//OLD METHOD TO DISPLAY ONLY EXONS  LEAVE FOR NOW SO NEW METHOD CAN BE DEVELOPED while old method still works
            if(trans!=null){
                boolean[] topFull=new boolean[trans.getProbeSetCount(false)+1],bottomFull=new boolean[trans.getProbeSetCount(false)+1];
                ArrayList<ArrayList> excluded=new ArrayList<ArrayList>();
                for(int i=0;i<trans.getProbeSetCount(false)+1;i++){
                    topFull[i]=false;
                    bottomFull[i]=false;
                    excluded.add(new ArrayList<RoundRectangle2D.Float>());
                }
                elementRects = new RoundRectangle2D.Float[trans.getExonLength()];
                elementNums=new String[trans.getExonLength()];
                if(trans.getExonLength()==0){

                }else{
                    elementLines = new Line2D[trans.getExonLength()-1];
                }
                int offset = (int) (currentDim * 0.2);
                if (offset < 1) {
                    offset = 1;
                }
                int mappedInd=0;
                int tmpExcldInd=0;
                int excludeCount=0;
                int curStartPosX=0;
                int excludeExonMult=1;
                elements = trans.getIncludedTranscriptElements(false);
                for (int i = 0; i < elementRects.length; i++) {
                    int curheight=20;
                    int vertPos = this.vertPosIncld;
                    int horPos=curStartPosX + offset;
                    //System.out.println("Values for Width"+currentDim+":"+ex.get(i).getIncludedProbeCount()+":"+offset*2);
                    int width=(currentDim *elements.get(i).getIncludedProbeSetCount(false))- (offset * 2);
                    //System.out.println("Width:"+width);
                    RoundRectangle2D.Float tmpRect=null;
                    //System.out.println("Full:Exon:"+ex.get(i).getID()+":"+ex.get(i).isExclude());
                    if (elements.get(i).isExclude()) {
                        curheight=10;
                        if(excludeCount==0){
                            excludeCount=trans.getConsecutiveExcluded(i,false);
                        }
                        vertPos = this.vertPosExcld+(excludeExonMult*7);
                        width=(int)((currentDim*2-offset*excludeCount)/(double)excludeCount);
                        if(excludeCount==1){
                            width=width/2;
                        }else if(excludeCount==2){
                            width=width/2;
                        }
                        horPos=(int)((horPos-currentDim-offset)+(tmpExcldInd*width+tmpExcldInd*offset))+offset/2;
                        if(excludeCount==1){
                            horPos=horPos+width/2;
                        }
                        tmpExcldInd++;
                        if(excludeExonMult==1){
                            bottomFull[mappedInd]=true;
                        }else{
                            topFull[mappedInd]=true;
                        }
                        tmpRect= new RoundRectangle2D.Float(horPos, vertPos,width, curheight, 4, 4);
                        excluded.get(mappedInd).add(tmpRect);
                        //System.out.println(i+":excluded:"+horPos+":"+vertPos+":"+width+":"+curheight);
                    }else{
                        mappedInd=mappedInd+elements.get(i).getIncludedProbeSetCount(false);
                        if(excludeCount>0){
                            if(excludeExonMult==1){
                                excludeExonMult=-1;
                            }else{
                                excludeExonMult=1;
                            }
                        }
                        excludeCount=0;
                        tmpExcldInd=0;
                        curStartPosX=curStartPosX+(currentDim *elements.get(i).getIncludedProbeSetCount(false));
                        tmpRect= new RoundRectangle2D.Float(horPos, vertPos,width, curheight, 4, 4);
                        //System.out.println(i+":Not excluded:"+horPos+":"+vertPos+":"+width+":"+curheight);
                    }      
                    elementRects[i] = tmpRect;
                    elementNums[i]= ((Exon)elements.get(i)).getExonNumber();
                }

                //Adjust excluded to use free space to allow larger exons to be drawn
                for(int i=0;i<excluded.size();i++){
                    //need to generally expand if more than 6 exons are excluded
                    if(excluded.get(i).size()>6||(i==0&&excluded.get(i).size()>2)){
                        ArrayList<Integer> openSpaceInd=new ArrayList<Integer>();
                        if(topFull[i]){
                            openSpaceInd.add(i-1);
                            openSpaceInd.add(i);
                            findRoomToExpand(topFull,openSpaceInd,excluded.get(i).size());
                        }else{
                            openSpaceInd.add(i-1);
                            openSpaceInd.add(i);
                            findRoomToExpand(bottomFull,openSpaceInd,excluded.get(i).size());
                        }
                        expand(openSpaceInd,excluded.get(i));
                    }
                }



                for(int i=0;i<elementRects.length-1;i++){
                    int y1=(int)elementRects[i].getCenterY();
                    int x1=(int)(elementRects[i].getCenterX()+(elementRects[i].getWidth()/2.0));
                    int y2=(int)elementRects[i+1].getCenterY();
                    int x2=(int)(elementRects[i+1].getCenterX()-(elementRects[i+1].getWidth()/2.0));
                    elementLines[i]=new Line2D.Float(x1,y1,x2,y2);
                }
            }
        }else{*/
            if(trans!=null){
                boolean[] topFull=new boolean[trans.getProbeSetCount(displayIntrons)+1],bottomFull=new boolean[trans.getProbeSetCount(displayIntrons)+1];
                ArrayList<ArrayList> excluded=new ArrayList<ArrayList>();
                for(int i=0;i<trans.getProbeSetCount(displayIntrons)+1;i++){
                    topFull[i]=false;
                    bottomFull[i]=false;
                    excluded.add(new ArrayList<RoundRectangle2D.Float>());
                }
                elementRects = new RoundRectangle2D.Float[trans.getIncludedElemLength(displayIntrons)];
                elementNums=new String[trans.getIncludedElemLength(displayIntrons)];
                if(trans.getExonLength()==0){

                }else{
                    elementLines = new Line2D[trans.getIncludedElemLength(displayIntrons)-1];
                }
                int offset = (int) (currentDim * 0.2);
                if (offset < 1) {
                    offset = 1;
                }
                int mappedInd=0;//# probesets displayed so far
                int tmpExcldInd=0;
                int excludeCount=0;
                int curStartPosX=0;
                int excludeExonMult=1;
                elements=trans.getIncludedTranscriptElements(displayIntrons);
                for (int i=0; i<elements.size();i++){
                    TranscriptElement curElem=elements.get(i);
                    int curheight=20;
                    int vertPos = this.vertPosIncld;
                    if(curElem.getType().equals("intron")){
                        vertPos=this.vertPosIncld+curheight/2;
                        curheight=2;
                    }
                    
                    int horPos=curStartPosX + offset;
                    int width=(currentDim *curElem.getIncludedProbeSetCount(displayIntrons))- (offset * 2);
                    RoundRectangle2D.Float tmpRect=null;
                    if(curElem.isExclude()){
                        curheight=10;
                        if(excludeCount==0){
                            excludeCount=this.trans.getConsecutiveExcluded(i,displayIntrons);
                        }
                        vertPos = this.vertPosExcld+(excludeExonMult*7);
                        width=(int)((currentDim*2-offset*excludeCount)/(double)excludeCount);
                        if(excludeCount==1){
                            width=width/2;
                        }else if(excludeCount==2){
                            width=width/2;
                        }
                        horPos=(int)((horPos-currentDim-offset)+(tmpExcldInd*width+tmpExcldInd*offset))+offset/2;
                        if(excludeCount==1){
                            horPos=horPos+width/2;
                        }
                        tmpExcldInd++;
                        if(excludeExonMult==1){
                            bottomFull[mappedInd]=true;
                        }else{
                            topFull[mappedInd]=true;
                        }
                        tmpRect= new RoundRectangle2D.Float(horPos, vertPos,width, curheight, 4, 4);
                        excluded.get(mappedInd).add(tmpRect);
                    }else{
                        mappedInd=mappedInd+curElem.getIncludedProbeSetCount(displayIntrons);
                        if(excludeCount>0){
                            if(excludeExonMult==1){
                                excludeExonMult=-1;
                            }else{
                                excludeExonMult=1;
                            }
                        }
                        excludeCount=0;
                        tmpExcldInd=0;
                        curStartPosX=curStartPosX+(currentDim *curElem.getIncludedProbeSetCount(displayIntrons));
                        tmpRect= new RoundRectangle2D.Float(horPos, vertPos,width, curheight, 4, 4);
                        //System.out.println(i+":Not excluded:"+horPos+":"+vertPos+":"+width+":"+curheight);
                    }
                    elementRects[i] = tmpRect;
                    if(curElem.getType().equals("exon")){
                        Exon curEx=(Exon) curElem;
                        elementNums[i]= curEx.getExonNumber();
                    }else{
                        elementNums[i]="";
                    }
                }

                //Adjust excluded to use free space to allow larger exons to be drawn
                for(int i=0;i<excluded.size();i++){
                    //need to generally expand if more than 6 exons are excluded
                    if(excluded.get(i).size()>6||(i==0&&excluded.get(i).size()>2)){
                        ArrayList<Integer> openSpaceInd=new ArrayList<Integer>();
                        if(topFull[i]){
                            openSpaceInd.add(i-1);
                            openSpaceInd.add(i);
                            findRoomToExpand(topFull,openSpaceInd,excluded.get(i).size());
                        }else{
                            openSpaceInd.add(i-1);
                            openSpaceInd.add(i);
                            findRoomToExpand(bottomFull,openSpaceInd,excluded.get(i).size());
                        }
                        expand(openSpaceInd,excluded.get(i));
                    }
                }



                for(int i=0;i<elementRects.length-1;i++){
                    int y1=(int)elementRects[i].getCenterY();
                    int x1=(int)(elementRects[i].getCenterX()+(elementRects[i].getWidth()/2.0));
                    int y2=(int)elementRects[i+1].getCenterY();
                    int x2=(int)(elementRects[i+1].getCenterX()-(elementRects[i+1].getWidth()/2.0));
                    elementLines[i]=new Line2D.Float(x1,y1,x2,y2);
                }
            }
        //}
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font(g2.getFont().getName(), g2.getFont().getStyle(), 16));
        int height = (int) this.getSize().getHeight();
        int width = (int) this.getSize().getWidth();
        g2.setColor(Color.BLUE);
        g2.drawString("5'", 2, 15);
        g2.drawString("3'", width - 20, 15);
        if (elements != null) {
            Rectangle2D namerect = g.getFont().getStringBounds("Transcript:" + trans.getID(), g.getFontMetrics().getFontRenderContext());
            double length = namerect.getMaxX() - namerect.getMinX();
            int half = (int) (length / 2);
            g2.drawString("Transcript:" + trans.getID(), width / 2 - (half), 15);
            Font curFont = g2.getFont();
            if (currentDim > 40) {
                curFont = new Font(curFont.getName(), curFont.getStyle(), 14);
            } else if (currentDim > 20) {
                curFont = new Font(curFont.getName(), curFont.getStyle(), 12);
            }
            for (int i = 0; i < elementRects.length; i++) {
                if (this.colorBy.equals("filter")) {
                    if (elements != null && elements.get(i).isExclude()) {
                        //System.out.println(elements.get(i).getID() +":"+elements.get(i).getExclusionReason());
                        if (elements.get(i).getProbeSet().size() == 0) {
                            g2.setColor(new Color(255, 0, 0));
                        } else if (elements.get(i).getExclusionReasons().equals("Heritability")) {
                            g2.setColor(new Color(0, 51, 255));
                        } else if (elements.get(i).getExclusionReasons().equals("DABG")) {
                            g2.setColor(new Color(0, 153, 51));
                        } else if (elements.get(i).getExclusionReasons().equals("Annotation")) {
                            g2.setColor(new Color(255, 153, 51));
                        } else if (elements.get(i).getExclusionReasons().equals("Masked")) {
                            g2.setColor(new Color(96, 192, 223));
                        } else if (elements.get(i).getExclusionReasons().contains(",")) {
                            g2.setColor(new Color(255, 255, 153));
                        } else {
                            g2.setColor(new Color(255, 255, 153));
                        }
                    } else {
                        g2.setColor(Color.LIGHT_GRAY);
                    }
                }
                g2.fill(elementRects[i]);
                if (elements.get(i).getType().equals("exon")) {
                    Exon ex = (Exon) elements.get(i);
                    if (!ex.isFullCoding()) {
                        int tmpwidth = 10;
                        if (ex.getPercentNonCoding() > 0.1) {
                            tmpwidth = (int) (ex.getPercentNonCoding() * elementRects[i].getWidth());
                        } else {
                            tmpwidth = (int) (0.1 * elementRects[i].getWidth());
                        }
                        RoundRectangle2D.Float temp = new RoundRectangle2D.Float((int) elementRects[i].getX(), (int) elementRects[i].getY(), tmpwidth, 20, 4, 4);
                        g2.setColor(Color.WHITE);
                        g2.fill(temp);
                    }
                }
                //g2.setColor(Color.WHITE);

                g2.setColor(Color.DARK_GRAY);
                g2.draw(elementRects[i]);
                if (currentDim > 20 && elements.get(i).getType().equals("exon")) {
                    g2.setFont(curFont);
                    int x = (int) ((elementRects[i].getWidth() / 2) + elementRects[i].getX());
                    int y = (int) (elementRects[i].getHeight() + elementRects[i].getY());
                    namerect = g.getFont().getStringBounds(elementNums[i], g.getFontMetrics().getFontRenderContext());
                    double numlen = namerect.getMaxX() - namerect.getMinX();
                    double numheight = namerect.getMaxY() - namerect.getMinY();
                    int halfW = (int) (numlen / 2);
                    int subHeight = (int) (elementRects[i].getHeight() * 0.2);
                    if (elements.get(i).isExclude()) {
                        g2.setFont(new Font(g2.getFont().getName(), g2.getFont().getStyle(), g2.getFont().getSize() - 4));
                        subHeight = (int) (elementRects[i].getHeight() * 0.1);
                    }
                    g2.drawString(elementNums[i], x - halfW, y - subHeight);
                }
            }
            g2.setColor(Color.BLACK);
            for (int i = 0; i < elementLines.length; i++) {
                g2.draw(elementLines[i]);
            }
        }
        
    }

    public void setCurDim(int currentDim) {
        this.currentDim=currentDim;
        int sizeMax=currentDim*(numProbesets+2);
        this.setPreferredSize(new Dimension(sizeMax,84));
        this.setupGraphics();
        this.revalidate();
    }

    /*public void setNumberOfProbeSets(int probesetCount) {
        this.numProbesets=probesetCount;
        setCurDim(currentDim);
    }*/
    
    public TranscriptElement getExonMouseIsOver(Point p){
        TranscriptElement ret=null;
        for(int i=0;i<elementRects.length&&ret==null;i++){
            if(elementRects[i].contains(p)){
                ret=elements.get(i);
            }
        }
        return ret;
    }

    private void findRoomToExpand(boolean[] fullList, ArrayList<Integer> openSpaceInd, int size) {
        double tmpsize=size;
        double ratio=tmpsize/openSpaceInd.size();
        boolean noSpace=false;
        //System.out.println("Open:"+openSpaceInd.toString());
        int rightIndex=openSpaceInd.get(0).intValue()-1;
        int leftIndex=openSpaceInd.get(1).intValue()+1;
        
        int rightopen=this.findRightOpenSpace(rightIndex, fullList);
        int leftopen=this.findLeftOpenSpace(leftIndex, fullList);
        //System.out.println("Find Room to expand:"+ratio);
        while(!noSpace&&ratio>3){
            if(rightopen==0 && leftopen==0){
                noSpace=true;
                //System.out.print("NO SPACE");
            }else if(rightopen>leftopen){
                //System.out.print("EXPAND RIGHT"+rightIndex);
                openSpaceInd.add(rightIndex);
                rightopen--;
                rightIndex--;
            }else{
                //System.out.print("EXPAND LEFT"+leftIndex);
                openSpaceInd.add(leftIndex);
                leftopen--;
                leftIndex++;
            }
            ratio=tmpsize/openSpaceInd.size();
            //System.out.println("  Ratio:"+ratio);
        }
    }
    private int findLeftOpenSpace(int leftIndex, boolean[] fullList){
        int open=0;
        boolean stop=false;
        for(int i=leftIndex;i<fullList.length&&!stop;i++){
            if(!fullList[i]){
                open++;
            }else{
                stop=true;
            }
        }
        return open;
    }
    private int findRightOpenSpace(int rightIndex, boolean[] fullList){
        int open=0;
        boolean stop=false;
        for(int i=rightIndex;i>=0&&!stop;i--){
            if(!fullList[i]){
                open++;
            }else{
                stop=true;
            }
        }
        return open;
    }

    private void expand(ArrayList<Integer> openSpaceInd, ArrayList<RoundRectangle2D.Float> rect) {
        //System.out.println("Expand:"+rect.size());
        int offset = (int) (currentDim * 0.05);
        if (offset < 1) {
            offset = 1;
        }
        float curheight = 10;
        float vertPos = (int)rect.get(0).getY();
        float tmpDim=(float)currentDim;
        float width = (float) (tmpDim * openSpaceInd.size()-((offset*2.0)*rect.size()))/rect.size();
        //System.out.println("Width:"+width);
        Collections.sort(openSpaceInd);
        int minInd=openSpaceInd.get(0);
        if(minInd<0){
            minInd=0;
        }
        //System.out.println("MinInd:"+minInd);
        float curStartPosX=currentDim*minInd;
        float horPos=0;
        for(int i=0;i<rect.size();i++){
            horPos = curStartPosX + offset;
            //System.out.println(i+"rect:("+horPos+","+vertPos+","+width+")");
            RoundRectangle2D.Float tmp=rect.get(i);
            tmp.setRoundRect(horPos, vertPos, width, curheight,4,4);
            curStartPosX=curStartPosX+width+offset*2;
        }
    }
}
