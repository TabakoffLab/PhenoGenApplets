/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.util.ArrayList;

/**
 *
 * @author smahaffey
 */
public class ProbeSet implements Comparable {
    long start=-1,stop=-1,len=-1;
    String probeSetID="";
    String sequence="",strand="";
    boolean excluded=false;
    boolean filteredFromHeatMap=false;
    boolean filteredByAnnotation=false;
    boolean filteredByStrand=false;
    boolean filteredByHerit=false;
    boolean filteredByDabg=false;
    boolean oppositeStrand=false;
    String annotation="";
    ArrayList<Probe> probes=new ArrayList<Probe>();
    double herit=0,dabg=0;
    boolean updateLoc=false;
  
    
    public ProbeSet(long start, long stop, String probeID,String seq, String strand,String type,double dabg,double herit,String updatedLoc){
        this.start=start;
        this.stop=stop;
        this.probeSetID=probeID;
        this.sequence=seq;
        if(strand.equals("1")||strand.equals("+")||strand.equals("+1")){
            this.strand="+";
        }else if(strand.equals("-1")||strand.equals("-")){
            this.strand="-";
        }else{
            System.err.println("Unknown Strand Type:"+strand);
        }
        this.annotation=type;
        len=Math.abs(start-stop);
        this.herit=herit;
        this.dabg=dabg;
        if(updatedLoc.equals("N")){
            this.updateLoc=false;
        }else if(updatedLoc.equals("Y")){
            this.updateLoc=true;
        }
    }
    public void clearFilters(){
        this.filteredByAnnotation=false;
        this.filteredByDabg=false;
        this.filteredByHerit=false;
        this.filteredByStrand=false;
    }
    
    public boolean isLocationUpdated(){
        return this.updateLoc;
    }
    
    public boolean isMasked(){
        return !this.updateLoc;
    }
    
    public double getDabg() {
        return dabg;
    }

    public double getHerit() {
        return herit;
    }

    public long getLen() {
        return len;
    }

    public String getProbeSetID() {
        return probeSetID;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public String getSequence() {
        return sequence;
    }

    public String getStrand() {
        return strand;
    }

    public boolean isExcluded() {
        return excluded;
    }

    public void setExcluded(boolean excluded) {
        if(!this.filteredFromHeatMap){
            this.excluded = excluded;
        }
    }

    public String getAnnotation() {
        return annotation;
    }
    public void setProbes(ArrayList<Probe> probes){
        this.probes=probes;
    }
    
    public ArrayList<Probe> getProbes(){
        return this.probes;
    }

    @Override
    public int compareTo(Object t) {
        ProbeSet p2=(ProbeSet)t;
        int ret=0;
        if(this.probeSetID.equals(p2.probeSetID)&&this.start==p2.start&&this.stop==p2.stop){
            ret=0;
        }else if(this.stop>this.start){//+ strand
            if(this.start>p2.start){
               ret=1; 
            }else if(this.start<p2.start){
               ret=-1;
            }else if(this.start==p2.start){
                if(this.stop>p2.stop){
                    ret=1;
                }else{
                    ret=-1;
                }
            }
        }else if(this.stop<this.start){//- strand
            if(this.start<p2.start){
               ret=1; 
            }else if(this.start>p2.start){
               ret=-1;
            }else if(this.start==p2.start){
                if(this.stop<p2.stop){
                    ret=1;
                }else{
                    ret=-1;
                }
            }
        }
        return ret;
    }
    public String toString(){
        String ret=this.probeSetID+" "+this.annotation+" "+this.strand;
        if(this.dabg>-1){
            ret=ret+" Samples DABG:"+this.dabg;
        }
        if(this.herit>-1){
            ret=ret+"% Heritability:"+herit;
        }
        if(this.excluded){
            ret=ret+" (Not Displayed)";
        }
        return ret;
    }

    public boolean isFilteredByAnnotation() {
        return filteredByAnnotation;
    }

    public void setFilteredByAnnotation(boolean filteredByAnnotation) {
        this.filteredByAnnotation = filteredByAnnotation;
    }

    public boolean isFilteredByStrand() {
        return filteredByStrand;
    }

    public void setFilteredByStrand(boolean filteredByStrand) {
        this.filteredByStrand = filteredByStrand;
    }

    public boolean isFilteredFromHeatMap() {
        return filteredFromHeatMap;
    }

    public void setFilteredFromHeatMap(boolean filteredFromHeatMap) {
        if(filteredFromHeatMap){
            this.excluded=true;
            this.filteredFromHeatMap=true;
        }
    }

    public void setFilteredByHerit(boolean b) {
        this.filteredByHerit=b;
    }

    public void setFilteredByDabg(boolean b) {
       this.filteredByDabg=b;
    }
    
    public boolean isFilteredByDabg(){
        return this.filteredByDabg;
    }
    
    public boolean isFilteredByHerit(){
        return this.filteredByHerit;
    }
    public String getExclusionReason(){
        String reason="";
        if(this.filteredByStrand){
            reason="Wrong Strand";
        }else if(this.filteredFromHeatMap){
            reason="Masked";
        }else if(this.filteredByAnnotation){
            reason="Annotation";
        }else if(this.filteredByDabg){
            reason="DABG";
        }else if(this.filteredByHerit){
            reason="Heritability";
        }
        return reason;
    }

    void setOppositeStrand(boolean b) {
        this.oppositeStrand=b;
    }
    public boolean getOppositeStrand(){
        return this.oppositeStrand;
    }
}
