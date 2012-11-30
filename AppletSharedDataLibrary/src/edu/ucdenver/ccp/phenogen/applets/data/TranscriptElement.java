/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author smahaffey
 */
abstract public class TranscriptElement implements Comparable {
    String ID="",exclusionReason="";
    boolean exclude=false;
    ArrayList<ProbeSet> probeset=new ArrayList<ProbeSet>();
    long start=-1,stop=-1,len=-1;
    int number=-1;
    String type="";
    
    
    static int WRONG_STRAND=1;
    static int MISSING_HEATMAP=5;
    static int HERIT_CUTOFF=2;
    static int DABG_CUTOFF=3;
    static int ANNOTATION_CUTOFF=4;
    
    int exclusionCode=-1;
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public String getExclusionReason() {
        this.findExclusionReason();
        assignExcludeReasonFromCode();
        return exclusionReason;
    }
    
    public String getExclusionReasons() {
        return this.findExclusionReasons();
    }
    
    public String getType(){
        return type;
    }
    public long getLen() {
        return len;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<ProbeSet> getProbeSet() {
        return probeset;
    }
    
    public void setProbeSets(ArrayList<ProbeSet> probeset) {
        this.probeset=probeset;
        Collections.sort(probeset);
        if(probeset.size()==0){
            exclude=true;
        }
        
    }
    
    public int getIncludedProbeSetCount(boolean includeIntrons){
        int countIncluded=0;
        if(this.type.equals("exon")||(this.type.equals("intron")&&includeIntrons)){
            for(int i=0;i<this.probeset.size();i++){
                if(!this.probeset.get(i).isExcluded()){
                    countIncluded++;
                }
            }
        }
        return countIncluded;
    }
    
    
    
    public String findExclusionReasons(){
        String reasons="";
        if(this.exclude){
            if(probeset.size()==0){
                reasons="No Probesets";
            }
            for(int i=0;i<this.probeset.size();i++){
                    ProbeSet tmp=this.probeset.get(i);
                    if(tmp.isFilteredByHerit()){
                        reasons=this.addToString("Heritability", reasons);
                    }
                    if(tmp.isFilteredByDabg()){
                        reasons=this.addToString("DABG", reasons);
                    }
                    if(tmp.isFilteredByAnnotation()){
                        reasons=this.addToString("Annotation", reasons);
                    }
                    if(tmp.isFilteredFromHeatMap()){
                        reasons=this.addToString("Masked", reasons);
                    }
            }
        }
        return reasons;
    }
    
    private String addToString(String toAdd,String addTo){
        String ret="";
        if(addTo.contains(toAdd)){
            ret=addTo;
        }else{
            if(addTo.equals("")){
                ret=toAdd;
            }else{
                ret=addTo+", "+toAdd;
            }
        }
        return ret;
    }
    
    public void clearExclusionReason(){
        this.exclusionCode=-1;
    }
    
    public boolean isAllProbesExcluded(){
        boolean allExcluded=true;
        for(int i=0;i<this.probeset.size()&&allExcluded;i++){
            if(!this.probeset.get(i).isExcluded()){
                allExcluded=false;
            }
        }
        return allExcluded;
    }
    
    private void assignExcludeReasonFromCode() {
        if(this.exclusionCode==Exon.MISSING_HEATMAP){
            this.exclusionReason="Masked";
        }else if(this.exclusionCode==Exon.WRONG_STRAND){
            this.exclusionReason="Strand";
        }else if(this.exclusionCode==Exon.HERIT_CUTOFF){
            this.exclusionReason="Herit";
        }else if(this.exclusionCode==Exon.ANNOTATION_CUTOFF){
            this.exclusionReason="Annotation";
        }else if(this.exclusionCode==Exon.DABG_CUTOFF){
            this.exclusionReason="DABG";
        }else{
            this.exclusionReason="";
        }
    }

    void setMaskedProbesets(FullHeatMapData fhmd) {
        boolean allMasked=true;
        for(int i=0;i<probeset.size();i++){
            ProbeSet ps=probeset.get(i);
            if(fhmd.isProbeMasked(ps.getProbeSetID())){
                //ps.setExcluded(true);
                //System.out.println("MASKED:"+ps.getProbeSetID());
                ps.setFilteredFromHeatMap(true);
            }else{
                allMasked=false;
            }
        }
        if(allMasked){
            this.setExclude(true);
            this.exclusionCode=Exon.MISSING_HEATMAP;
        }
    }
    
    void setMaskedProbesets(ExCorFullHeatMapData fhmd) {
        boolean allMasked=true;
        for(int i=0;i<probeset.size();i++){
            ProbeSet ps=probeset.get(i);
            if(fhmd.isProbeMasked(ps.getProbeSetID())){
                //ps.setExcluded(true);
                //System.out.println("MASKED:"+ps.getProbeSetID());
                ps.setFilteredFromHeatMap(true);
            }else{
                allMasked=false;
            }
        }
        if(allMasked){
            this.setExclude(true);
            this.exclusionCode=Exon.MISSING_HEATMAP;
        }
    }
    
    public void findExclusionReason(){
        if(this.exclude){
            for(int i=0;i<this.probeset.size();i++){
                    ProbeSet tmp=this.probeset.get(i);
                    if(tmp.isFilteredByHerit()&&this.exclusionCode<Exon.HERIT_CUTOFF){
                        this.exclusionCode=Exon.HERIT_CUTOFF;
                    }else if(tmp.isFilteredByDabg()&&this.exclusionCode<Exon.DABG_CUTOFF){
                        this.exclusionCode=Exon.DABG_CUTOFF;
                    }else if(tmp.isFilteredByAnnotation()&&this.exclusionCode<Exon.ANNOTATION_CUTOFF){
                        this.exclusionCode=Exon.ANNOTATION_CUTOFF;
                    }
            }
        }else{
            this.exclusionCode=-1;
        }
    }
    
    abstract public String getToolTipText();
    
    @Override
    public int compareTo(Object t) {
        TranscriptElement ex2=(TranscriptElement)t;
        int ret=0;
        if(this.ID.equals(ex2.ID)&&this.start==ex2.start&&this.stop==ex2.stop){
            ret=0;
        }else if(this.stop>this.start){//+ strand
            if(this.start>ex2.start){
               ret=1; 
            }else if(this.start<ex2.start){
               ret=-1;
            }else if(this.start==ex2.start){
                if(this.stop>ex2.stop){
                    ret=1;
                }else{
                    ret=-1;
                }
            }
        }else if(this.stop<this.start){//- strand
            if(this.start<ex2.start){
               ret=1; 
            }else if(this.start>ex2.start){
               ret=-1;
            }else if(this.start==ex2.start){
                if(this.stop<ex2.stop){
                    ret=1;
                }else{
                    ret=-1;
                }
            }
        }
        return ret;
    }
}
