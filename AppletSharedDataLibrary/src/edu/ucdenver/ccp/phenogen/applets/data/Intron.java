/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;


/**
 *
 * @author smahaffey
 */
public class Intron extends TranscriptElement {

    
    public Intron(long start,long stop,String id){
        this.start=start;
        this.stop=stop;
        len=Math.abs(stop-start);
        this.ID=id;
        this.type="intron";
    }

    
    
    public String getToolTipText(){
            String tooltiptext="<html>Intron:"+this.getID()+"<BR>Length:"+this.getLen()+"bp ("+this.getStart()+","+this.getStop()+")<BR>";
            tooltiptext=tooltiptext+"# Probes:"+this.probeset.size()+" ("+this.getIncludedProbeSetCount(true)+" Displayed)<BR>";
            tooltiptext=tooltiptext+"</HTML>";
            return tooltiptext;
    }

    
    
    
    
}
