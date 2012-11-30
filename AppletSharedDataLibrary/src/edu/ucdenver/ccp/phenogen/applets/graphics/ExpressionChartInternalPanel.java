/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.graphics;

import edu.ucdenver.ccp.phenogen.applets.data.ProbeSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.ui.Layer;

/**
 *
 * @author smahaffey
 */
public class ExpressionChartInternalPanel extends JPanel{
    //JFreeChart[] charts=null;
    BufferedImage[] imgCharts=null;
    double[] mins=null;
    double[] maxs=null;
    int minDiff=4;//represents the min height for a graph with a difference of 0.4 between Min and Max
    Color lineColor=Color.RED;
    ArrayList<String> probeIDList=null;
    String[] rProbe=null;
    ArrayList<Font> fontList=new ArrayList<Font>();
    HashMap completeProbeset=null;
    
    public ExpressionChartInternalPanel(){
        this.setBackground(Color.WHITE);
    }
    public void setProbeset(HashMap m){
        this.completeProbeset=m;
    }
    public void setData(ArrayList<DefaultKeyedValues2DDataset> dkvd,ArrayList<String> probeIDList,boolean showShapes, boolean wide){
        int totalHeight=0;
        //charts=new JFreeChart[dkvd.size()];
        imgCharts=new BufferedImage[dkvd.size()];
        mins=new double[dkvd.size()];
        maxs=new double[dkvd.size()];
        this.probeIDList=probeIDList;
        String os=System.getProperty("os.name");
        rProbe=new String[this.probeIDList.size()];
        for(int i=0;i<rProbe.length;i++){
            if(os.contains("Mac")){
                rProbe[i]=reverse(this.probeIDList.get(i));
            }else{
                rProbe[i]=this.probeIDList.get(i);
            }
        }
        for(int i=0;i<dkvd.size();i++){
            String title="";
            String xaxisLbl="";
            JFreeChart chart=ChartFactory.createLineChart(title,"","",dkvd.get(i),PlotOrientation.VERTICAL,false,false,false);
            chart.getCategoryPlot().getDomainAxis().setUpperMargin(0.01);
            chart.getCategoryPlot().getDomainAxis().setLowerMargin(0.01);
            chart.getCategoryPlot().setDomainCrosshairVisible(false);
            chart.getCategoryPlot().getDomainAxis().setAxisLineVisible(false);
            chart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
            chart.getCategoryPlot().getDomainAxis().setTickMarksVisible(false);
            
            NumberAxis tmpY= (NumberAxis) ((CategoryPlot) chart.getPlot()).getRangeAxis();
            tmpY.setTickUnit(new NumberTickUnit(0.2));
            tmpY.setNumberFormatOverride(new DecimalFormat("###.#"));
            LineAndShapeRenderer itemRenderer= (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
            itemRenderer.setSeriesPaint(0, lineColor);
            if(showShapes){
                itemRenderer.setSeriesShape(0, new Rectangle(-2,-2,4,4));
                itemRenderer.setSeriesShapesVisible(0, true);
                itemRenderer.setSeriesShapesFilled(0, true);
            }
            
            
            
            //chart.getCategoryPlot().getRenderer().setBasePaint( lineColor);
            DefaultKeyedValues2DDataset cur=dkvd.get(i);
            double sum=0;
            for(int j=0;j<dkvd.get(i).getColumnCount();j++){
                Number value=cur.getValue(0, j);
                double val=value.doubleValue();
                sum=val+sum;
                if(j==0){
                    mins[i]=val;
                }else if(val<mins[i]){
                    mins[i]=val;
                }
                if(j==0){
                    maxs[i]=val;
                }else if(val>maxs[i]){
                    maxs[i]=val;
                }
            }
            double mean=sum/dkvd.get(i).getColumnCount();
            ValueMarker meanValMarker=new ValueMarker(mean);
            meanValMarker.setPaint(Color.BLACK);
            chart.getCategoryPlot().addRangeMarker(meanValMarker, Layer.BACKGROUND);
            tmpY.setRange(mins[i], maxs[i]);
            int diff=(int)((maxs[i]-mins[i])*10);
            int height=40;
            
            if(diff<minDiff){
                height=40;
            }else{
                height=height+((diff-minDiff)*10);
            }
            //charts[i]=chart;
            int imgWidth=450;
            if(wide){
                imgWidth=980;
            }
            imgCharts[i]=chart.createBufferedImage(imgWidth, height);
            
            totalHeight+=height;
        }
        this.setSize(100, totalHeight);
        this.setPreferredSize(new Dimension(100,totalHeight));
    }
    
    public void setData(ArrayList<DefaultStatisticalCategoryDataset> dkvd,ArrayList<String> probeIDList,boolean showShapes,boolean showError,boolean wide){
        int totalHeight=0;
        //charts=new JFreeChart[dkvd.size()];
        imgCharts=new BufferedImage[dkvd.size()];
        mins=new double[dkvd.size()];
        maxs=new double[dkvd.size()];
        this.probeIDList=probeIDList;
        rProbe=new String[this.probeIDList.size()];
        String os=System.getProperty("os.name");
        for(int i=0;i<rProbe.length;i++){
            if(os.contains("Mac")){
                rProbe[i]=reverse(this.probeIDList.get(i));
            }else{
                rProbe[i]=this.probeIDList.get(i);
            }
        }
        for(int i=0;i<dkvd.size();i++){
            String title="";
            String xaxisLbl="";
            JFreeChart chart=ChartFactory.createLineChart(title,"","",dkvd.get(i),PlotOrientation.VERTICAL,false,false,false);
            chart.getCategoryPlot().getDomainAxis().setUpperMargin(0.01);
            chart.getCategoryPlot().getDomainAxis().setLowerMargin(0.01);
            chart.getCategoryPlot().setDomainCrosshairVisible(false);
            chart.getCategoryPlot().getDomainAxis().setAxisLineVisible(false);
            chart.getCategoryPlot().getDomainAxis().setTickLabelsVisible(false);
            chart.getCategoryPlot().getDomainAxis().setTickMarksVisible(false);
            
            NumberAxis tmpY= (NumberAxis) ((CategoryPlot) chart.getPlot()).getRangeAxis();
            tmpY.setTickUnit(new NumberTickUnit(0.2));
            tmpY.setNumberFormatOverride(new DecimalFormat("###.#"));
            if(showError){
                StatisticalLineAndShapeRenderer itemRenderer= new StatisticalLineAndShapeRenderer(true,showShapes);
                itemRenderer.setSeriesPaint(0, lineColor);
                if(showShapes){
                    itemRenderer.setSeriesShape(0, new Rectangle(-2,-2,4,4));
                    itemRenderer.setSeriesShapesVisible(0, true);
                    itemRenderer.setSeriesShapesFilled(0, true);
                }
                chart.getCategoryPlot().setRenderer(itemRenderer);
            }else{
                LineAndShapeRenderer itemRenderer= (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
                itemRenderer.setSeriesPaint(0, lineColor);
                if(showShapes){
                    itemRenderer.setSeriesShape(0, new Rectangle(-2,-2,4,4));
                    itemRenderer.setSeriesShapesVisible(0, true);
                    itemRenderer.setSeriesShapesFilled(0, true);
                }
            }
            
            
            //chart.getCategoryPlot().getRenderer().setBasePaint( lineColor);
            DefaultStatisticalCategoryDataset cur=dkvd.get(i);
            double sum=0;
            for(int j=0;j<dkvd.get(i).getColumnCount();j++){
                Number value=cur.getValue(0, j);
                double val=value.doubleValue();
                sum=val+sum;
                if(j==0){
                    mins[i]=val;
                }else if(val<mins[i]){
                    mins[i]=val;
                }
                if(j==0){
                    maxs[i]=val;
                }else if(val>maxs[i]){
                    maxs[i]=val;
                }
            }
            double mean=sum/dkvd.get(i).getColumnCount();
            ValueMarker meanValMarker=new ValueMarker(mean);
            meanValMarker.setPaint(Color.BLACK);
            chart.getCategoryPlot().addRangeMarker(meanValMarker, Layer.BACKGROUND);
            tmpY.setRange(mins[i], maxs[i]);
            int diff=(int)((maxs[i]-mins[i])*10);
            int height=40;
            
            if(diff<minDiff){
                height=40;
            }else{
                height=height+((diff-minDiff)*10);
            }
            //charts[i]=chart;
            int imgWidth=450;
            if(wide){
                imgWidth=980;
            }
            imgCharts[i]=chart.createBufferedImage(imgWidth, height);
            totalHeight+=height;
        }
        this.setSize(100, totalHeight);
        this.setPreferredSize(new Dimension(100,totalHeight));
    }
    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        String os=System.getProperty("os.name");
        if(fontList.size()==0){
            String fName=g2.getFont().getFontName();
            AffineTransform fontAT = new AffineTransform();
            fontAT.rotate(270 * java.lang.Math.PI/180 );
            for(int i=14;i>7;i--){
                Font theDerivedFont = new Font(fName, Font.PLAIN, i).deriveFont(fontAT);
                fontList.add(theDerivedFont);
            }
        }
        if(imgCharts!=null){
            int start=0;
            Font prevFont=g2.getFont();
            for(int i=0;i<imgCharts.length;i++){
                int h=imgCharts[i].getHeight();
                //Rectangle2D tmp=new Rectangle(2,start,width-2,start+h);
                g2.drawImage(imgCharts[i], null, 15, start);
                
                //charts[i].draw(g2,tmp);
                
                int x=15,y=-1;
                for(int j=0;j<fontList.size()&&y==-1;j++){
                    g2.setFont(fontList.get(j));
                    if(os.contains("Mac")){
                        Rectangle2D namerect=g2.getFont().getStringBounds(rProbe[i], g2.getFontMetrics().getFontRenderContext());
                        double numlen=namerect.getMaxX()-namerect.getMinX();
                        double numHeight=namerect.getMaxY()-namerect.getMinY();
                        //System.out.println("Font:"+j+" len:"+numlen+" h:"+numHeight+" size:"+fontList.get(j).getSize());
                        if(numlen<(h-10)){
                            y =start+(int)(h/2)-(int)(numlen/2);
                        }
                    }else{
                        Rectangle2D namerect=g2.getFont().getStringBounds(rProbe[i], g2.getFontMetrics().getFontRenderContext());
                        double numlen=namerect.getMaxX()-namerect.getMinX();
                        double numHeight=namerect.getMaxY()-namerect.getMinY();
                        //System.out.println("Font:"+j+" len:"+numlen+" h:"+numHeight+" size:"+fontList.get(j).getSize());
                        if(numlen<(h-10)){
                            y =start+(int)(h/2)+(int)(numlen/2);
                        }
                    }
                }
                g2.setColor(findColor(probeIDList.get(i)));

                if (y > -1) {
                    g2.drawString(rProbe[i], x, y);
                } else {
                    g2.drawString(rProbe[i], x, start);
                }

                g2.setColor(Color.black);
                start=start+h;
            }
            g2.setFont(prevFont);
        }
    }

    void setLineColor(Color lineColor) {
        this.lineColor=lineColor;
    }
    
    private String reverse(String s){
        String ret="";
        for(int i=s.length()-1;i>=0;i--){
            ret=ret+s.charAt(i);
        }
        return ret;
    }
    
    private Color findColor(String id){
        Color ret=Color.BLACK;
        if(this.completeProbeset.containsKey(id)){
            ProbeSet ps=(ProbeSet) completeProbeset.get(id);
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
