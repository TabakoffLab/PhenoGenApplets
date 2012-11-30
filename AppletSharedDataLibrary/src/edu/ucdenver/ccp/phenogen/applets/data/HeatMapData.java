/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author smahaffey
 */
public class HeatMapData {
    
    double data[][]=null;
    Color colors[][]=new Color[0][0];
    double min=0,max=0;
    boolean skipFind=false;
    double colorSlope=1/255;
    double colorSlope2=1/255;
    double offset=0;
    int type=1;
    double midPoint=0;
    public final static int ONE_COLOR=1;
    public final static int TWO_COLOR=2;
    ArrayList<ProbeSet> probes=null;
    ArrayList<String> probeID=null;
    Color colorMax=Color.RED;
    Color colorMid=Color.BLACK;
    Color colorMin=Color.GREEN;
    
    public HeatMapData(int Type){
        this.type=Type;
        if(type==HeatMapData.ONE_COLOR){
            colorMin=colorMid;
        }
        if(type==HeatMapData.TWO_COLOR){
            colorMin=Color.GREEN;
        }
    }
    
    public void setData(double[][] data,ArrayList<ProbeSet> probes,boolean forceEqualAroundZero){
        //System.out.println("HeatMapData.setData():"+data.length);
        this.data=data;
        this.probes=probes;
        process(forceEqualAroundZero);
    }
    
    public void setDataString(double[][] data,ArrayList<String> probes,boolean forceEqualAroundZero){
        System.out.println("HeatMapData.setData():"+data.length+":"+probes.size());
        this.data=data;
        this.probeID=probes;
        process(forceEqualAroundZero);
    }
    
    public void process(boolean forceEqualAroundZero){
        if(!skipFind){
            findMinMax(forceEqualAroundZero);
        }
        if(this.type==1){
            colorSlope=(max-min)/255;
            offset=0-min;
            fillOneColor();
        }else{
            colorSlope=(max-midPoint)/255;
            colorSlope2=(min-midPoint)/255;
            fillTwoColor();
        }
    }
    
    public void setMinMax(double min,double max,double mid){
        skipFind=true;
        this.min=min;
        this.max=max;
        this.midPoint=mid;
    }
    
    public void findMinMax(boolean forceEqualAroundZero){
        if(!forceEqualAroundZero){
            if (data != null&&data.length>0) {
                min = data[0][0];
                max = data[0][0];
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++) {
                        if (data[i][j] < min) {
                            min = data[i][j];
                        } else if (data[i][j] > max) {
                            max = data[i][j];
                        }
                    }
                }
                midPoint=min+((max-min)/2);
            }
        }else{
            if (data != null&&data.length>0) {
                min = data[0][0];
                max = data[0][0];
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++) {
                        if (data[i][j] < min) {
                            min = data[i][j];
                        } else if (data[i][j] > max) {
                            max = data[i][j];
                        }
                    }
                }
                double val=Math.abs(max);
                if(Math.abs(min)>Math.abs(max)){
                    val=Math.abs(min);
                }
                min=-val;
                max=val;
                midPoint=0;
            }
        }
        
    }
    public Color getColor(int i,int j){
        if(i>=colors.length||j>=colors[i].length){
            return Color.BLACK;
        }
        return colors[i][j];
    }
    
    public void fillOneColor(){
        if(data.length>0&&data[0].length>0){
            colors=new Color[data.length][data[0].length];
            for(int i=0;i<data.length;i++){
                for(int j=0;j<data[i].length;j++){
                    int red=(int)Math.floor((data[i][j]+offset)/colorSlope);
                    if(red>255){
                        red=255;
                    }
                    if(red<0){
                        red=0;
                    }
                    //System.out.println("Color:"+data[i][j]+"::"+green);
                    colors[i][j]=new Color(red,0,0);
                }
            }
        }
        
    }
    public void fillTwoColor(){
        if(data.length>0&&data[0].length>0){
            colors=new Color[data.length][data[0].length];
            for(int i=0;i<data.length;i++){
                for(int j=0;j<data[i].length;j++){
                    int colorVal=0;
                    if(data[i][j]>=midPoint){
                        colorVal=(int)Math.floor(data[i][j]/colorSlope);
                    }else{
                        colorVal=(int)Math.floor(data[i][j]/colorSlope2);
                    }
                    if(colorVal>255){
                        colorVal=255;
                    }
                    if(colorVal<0){
                        colorVal=0;
                    }
                    if(data[i][j]>=midPoint){
                        //System.out.println("Color:"+data[i][j]+":green:"+colorVal);
                        colors[i][j]=new Color(colorVal,0,0);
                    }else{
                        //System.out.println("Color:"+data[i][j]+":red:"+colorVal);
                        colors[i][j]=new Color(0,colorVal,0);
                    }
                }
            }
        }
        
    }
    
    public int getSize(){
        return data.length;
    }
    
    public int getRows(){
        if(data==null||data.length==0){
            return 0;
        }
        return data.length;
    }
    public int getColumns(){
        if(data==null||data.length==0){
            return 0;
        }
        return data[0].length;
    }
    
    public ArrayList<ProbeSet> getIncludedProbes() {
        return probes;
    }
    
    public ArrayList<String> getIncludedProbeString() {
        return probeID;
    }

    public double getValueAt(int row, int col) {
        return data[row][col];
    }
    
    public boolean isTwoColor(){
        return type==HeatMapData.TWO_COLOR;
    }
    public boolean isOneColor(){
        return type==HeatMapData.ONE_COLOR;
    }
    public double getMin(){
        return min;
    }
    public double getMax(){
        return max;
    }
    public double getMid(){
        return midPoint;
    }
    public Color getMaxColor(){
        return colorMax;
    }
    public Color getMinColor(){
        return colorMin;
    }
    
    public Color getMidColor(){
        return colorMid;
    }
}
