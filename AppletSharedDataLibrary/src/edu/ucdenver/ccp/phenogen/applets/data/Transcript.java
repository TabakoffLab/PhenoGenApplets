/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author smahaffey
 */
public class Transcript {
    ArrayList<Exon> exons=new ArrayList<Exon>();
    ArrayList<Intron> introns=new ArrayList<Intron>();
    ArrayList<TranscriptElement> fullTranscript=new ArrayList<TranscriptElement>();
    String ID="";
    String strand="";
    long start=0,stop=0,len=0;
    
    public Transcript(String ID){
        this(ID,"",0,0);
    }
    public Transcript(String ID,String strand){
        this(ID,strand,0,0);
    }
    public Transcript(String ID,long start,long stop){
        this(ID,"",start,stop);
    }
    public Transcript(String ID,String strand,long start,long stop){
        this.ID=ID;
        if(strand.equals("1")||strand.equals("+")||strand.equals("+1")){
            this.strand="+";
        }else if(strand.equals("-1")||strand.equals("-")){
            this.strand="-";
        }else{
            this.strand=".";
            System.err.println("Unknown Strand Type:"+strand);
        }
        this.start=start;
        this.stop=stop;
        this.len=Math.abs(stop-start)+1;
    }
    
    public void setExon(ArrayList<Exon> exons){
        this.exons=exons;
        Collections.sort(this.exons);
    }
    
    public void setIntron(ArrayList<Intron> introns){
        this.introns=introns;
        if(introns!=null&&introns.size()>0){
            Collections.sort(this.introns);
        }
    }
    
    public void fillFullTranscript(){
        this.fullTranscript=new ArrayList<TranscriptElement>();
        if(exons!=null){
            this.fullTranscript.addAll(exons);
            if(introns!=null){
                this.fullTranscript.addAll(introns);
            }
            Collections.sort(this.fullTranscript);
        }
    }
    
    public ArrayList<TranscriptElement> getFullTranscript(){
        return this.fullTranscript;
    }

    public String getID() {
        return ID;
    }

    public ArrayList<Exon> getExons() {
        return exons;
    }
    
    public ArrayList<Intron> getIntrons() {
        return introns;
    }
    
    public ArrayList<ProbeSet> getProbeSetList(String annotationFilter,String dabgCutoffs,String heritCutoffs,boolean oppositeStrand,boolean dabgNA,boolean heritNA, boolean dispintrons){
        double dabgCutoff=Double.parseDouble(dabgCutoffs);
        double heritCutoff=Double.parseDouble(heritCutoffs);
        //System.out.println("getPROBESETLIST()\ntranscript:"+this.ID);
        ArrayList<ProbeSet> probeList=new ArrayList<ProbeSet>();
        for(int i=0;i<exons.size();i++){
            ArrayList<ProbeSet> tmp=exons.get(i).getProbeSet();
            //System.out.println("getProgeSetList"+i+":"+tmp.size());
            for(int j=0;j<tmp.size();j++){
                ProbeSet ps=tmp.get(j);
                ps.clearFilters();
                boolean exclude=false;
                if(!oppositeStrand&&!ps.getStrand().equals(this.getStrand())){
                    //System.out.println(j+":ex strand:"+ps.getProbeSetID());
                    ps.setFilteredByStrand(true);
                    ps.setOppositeStrand(true);
                    exclude=true;
                }else if(!ps.getStrand().equals(this.getStrand())){
                    ps.setOppositeStrand(true);
                }
                if(ps.getAnnotation().equals(annotationFilter)||(annotationFilter.equals("extended")&&ps.getAnnotation().equals("core")) ||annotationFilter.equals("full")){
                        
                }else{
                    //System.out.println(j+":ex annotation"+ps.getProbeSetID());
                    ps.setFilteredByAnnotation(true);
                    exclude=true;
                }
                if(!dabgNA){
                    if(ps.getDabg()<dabgCutoff){
                        //System.out.println(j+":ex dabg"+ps.getProbeSetID());
                        ps.setFilteredByDabg(true);
                        exclude=true;
                    }
                }
                if(!heritNA){
                    if(ps.getHerit()<heritCutoff){
                        //System.out.println(j+":ex herit"+ps.getProbeSetID());
                        ps.setFilteredByHerit(true);
                        exclude=true;
                    }
                }
                if(ps.isFilteredFromHeatMap()){
                    //System.out.println(j+":Masked"+ps.getProbeSetID());
                    exclude=true;
                }
                if(!exclude){
                    //System.out.println(j+":INCLUDED:"+ps.getProbeSetID());
                    probeList.add(ps);
                    ps.setExcluded(false);
                }else{
                    ps.setExcluded(true);
                }
            }
            //NEW Delete if it causes problems
            if(exons.get(i).isAllProbesExcluded()){
                exons.get(i).setExclude(true);
                exons.get(i).findExclusionReason();
            }else{
                exons.get(i).setExclude(false);
                exons.get(i).clearExclusionReason();
            }
        }
        //System.out.println("probelist:"+probeList.size());
        if(dispintrons){
            for(int i=0;i<introns.size();i++){
                ArrayList<ProbeSet> tmp=introns.get(i).getProbeSet();
                //System.out.println("getProgeSetList"+i+":"+tmp.size());
                for(int j=0;j<tmp.size();j++){
                    ProbeSet ps=tmp.get(j);
                    ps.clearFilters();
                    boolean exclude=false;
                    if(!oppositeStrand&&!ps.getStrand().equals(this.getStrand())){
                        //System.out.println(j+":ex strand:"+ps.getProbeSetID());
                        ps.setFilteredByStrand(true);
                        exclude=true;
                    }else if(!ps.getStrand().equals(this.getStrand())){
                        ps.setOppositeStrand(true);
                    }
                    if(ps.getAnnotation().equals(annotationFilter)||(annotationFilter.equals("extended")&&ps.getAnnotation().equals("core")) ||annotationFilter.equals("full")){

                    }else{
                        //System.out.println(j+":ex annotation"+ps.getProbeSetID());
                        ps.setFilteredByAnnotation(true);
                        exclude=true;
                    }
                    if(!dabgNA){
                        if(ps.getDabg()<dabgCutoff){
                            //System.out.println(j+":ex dabg"+ps.getProbeSetID());
                            ps.setFilteredByDabg(true);
                            exclude=true;
                        }
                    }
                    if(!heritNA){
                        if(ps.getHerit()<heritCutoff){
                            //System.out.println(j+":ex herit"+ps.getProbeSetID());
                            ps.setFilteredByHerit(true);
                            exclude=true;
                        }
                    }
                    if(ps.isFilteredFromHeatMap()){
                        //System.out.println(j+":Masked"+ps.getProbeSetID());
                        exclude=true;
                    }
                    if(!exclude){
                        //System.out.println(j+":INCLUDED:"+ps.getProbeSetID());
                        probeList.add(ps);
                        ps.setExcluded(false);
                    }else{
                        ps.setExcluded(true);
                    }
                    
                }
                if(introns.get(i).isAllProbesExcluded()){
                    introns.get(i).setExclude(true);
                    introns.get(i).findExclusionReason();
                }else{
                    introns.get(i).setExclude(false);
                    introns.get(i).clearExclusionReason();
                }
            }
        }
        //System.out.println("probelist w/ introns:"+probeList.size());
        return probeList;
    }
    public ArrayList<String> getProbeSetList(boolean dispintrons,boolean oppositeStrand,boolean filterAnnot, String annotationFilter){
        ArrayList<String> probeList=new ArrayList<String>();
        for(int i=0;i<exons.size();i++){
            ArrayList<ProbeSet> tmp=exons.get(i).getProbeSet();
            //System.out.println("getProgeSetList"+i+":"+tmp.size());
            for(int j=0;j<tmp.size();j++){
                ProbeSet ps=tmp.get(j);
                ps.clearFilters();
                boolean exclude=false;
                if(!oppositeStrand&&!ps.getStrand().equals(this.getStrand())){
                    //System.out.println(j+":ex strand:"+ps.getProbeSetID());
                    ps.setFilteredByStrand(true);
                    ps.setOppositeStrand(true);
                    exclude=true;
                }else if(!ps.getStrand().equals(this.getStrand())){
                    ps.setOppositeStrand(true);
                }
                if(filterAnnot){
                    if(ps.getAnnotation().equals(annotationFilter)||(annotationFilter.equals("extended")&&ps.getAnnotation().equals("core")) ||annotationFilter.equals("full")){

                    }else{
                        //System.out.println(j+":ex annotation"+ps.getProbeSetID());
                        ps.setFilteredByAnnotation(true);
                        exclude=true;
                    }
                }
                if(!exclude){
                    //System.out.println(j+":INCLUDED:"+ps.getProbeSetID());
                    //if(!existIn(ps.getProbeSetID(),probeList)){
                        probeList.add(ps.getProbeSetID());
                    //}
                    ps.setExcluded(false);
                }else{
                    ps.setExcluded(true);
                }
            }
            //NEW Delete if it causes problems
            if(exons.get(i).isAllProbesExcluded()){
                exons.get(i).setExclude(true);
                exons.get(i).findExclusionReason();
            }else{
                exons.get(i).setExclude(false);
                exons.get(i).clearExclusionReason();
            }
        }
        //System.out.println("probelist:"+probeList.size());
        if(dispintrons){
            for(int i=0;i<introns.size();i++){
                ArrayList<ProbeSet> tmp=introns.get(i).getProbeSet();
                //System.out.println("getProgeSetList"+i+":"+tmp.size());
                for(int j=0;j<tmp.size();j++){
                    ProbeSet ps=tmp.get(j);
                    ps.clearFilters();
                    boolean exclude=false;
                    if(!oppositeStrand&&!ps.getStrand().equals(this.getStrand())){
                        //System.out.println(j+":ex strand:"+ps.getProbeSetID());
                        ps.setFilteredByStrand(true);
                        exclude=true;
                    }else if(!ps.getStrand().equals(this.getStrand())){
                        ps.setOppositeStrand(true);
                    }
                    if(filterAnnot){
                        if(ps.getAnnotation().equals(annotationFilter)||(annotationFilter.equals("extended")&&ps.getAnnotation().equals("core")) ||annotationFilter.equals("full")){

                        }else{
                            //System.out.println(j+":ex annotation"+ps.getProbeSetID());
                            ps.setFilteredByAnnotation(true);
                            exclude=true;
                        }
                    }
                    if(!exclude){
                        //System.out.println(j+":INCLUDED:"+ps.getProbeSetID());
                        //if(!existIn(ps.getProbeSetID(),probeList)){
                            probeList.add(ps.getProbeSetID());
                        //}
                        ps.setExcluded(false);
                    }else{
                        ps.setExcluded(true);
                    }
                    
                }
                if(introns.get(i).isAllProbesExcluded()){
                    introns.get(i).setExclude(true);
                    introns.get(i).findExclusionReason();
                }else{
                    introns.get(i).setExclude(false);
                    introns.get(i).clearExclusionReason();
                }
            }
        }
        return probeList;
    }
    /*public ArrayList<Intron> getIntrons() {
        return introns;
    }*/
    
    public int getExonLength(){
        return exons.size();
    }
    public int getIntronLength(){
        return introns.size();
    }
    /*public int getIntronLength(){
        return introns.size();
    }*/

    public int getConsecutiveExcluded(int i,boolean displayIntrons) {
        boolean stop=false;
        int numExcludedAfterI=1;
        ArrayList<TranscriptElement> elem=this.getIncludedTranscriptElements(displayIntrons);
        for(int j=i+1;j<elem.size()&&!stop;j++){
            TranscriptElement e=elem.get(j);
            if(e.getType().equals("exon")){
                if(e.isExclude()){
                    numExcludedAfterI++;
                }else{
                        stop=true;
                }
            }else{
                if(displayIntrons&&!e.isExclude()){
                    stop=true;
                }
            }
        }
        return numExcludedAfterI;
    }

    public String getStrand() {
        return this.strand;
    }

    /*public void setExcluded(ArrayList<ProbeSet> excludedProbes) {
        for(int i=0;i<exons.size();i++){
            exons.get(i).setProbesExcluded(excludedProbes);
            System.out.println("exon:"+i+":"+exons.get(i).isExclude()+":"+exons.get(i).getExclusionReason());
        }
    }*/
    
    public int getProbeSetCount(boolean includeIntrons){
        int ret=0;
        for(int i=0;i<this.fullTranscript.size();i++){
            ret=ret+fullTranscript.get(i).getIncludedProbeSetCount(includeIntrons);
        }
        return ret;
    }
    public int getIncludedElemLength(boolean includeIntrons){
        int ret=0;
        for(int i=0;i<fullTranscript.size();i++){
            TranscriptElement tmp=fullTranscript.get(i);
            if(tmp.getType().equals("exon")){//add all exons excluded or not
                    ret++;
            }else if(includeIntrons){//add introns only when displayed
                if(!tmp.isExclude()){//add only introns not excluded
                    ret++;
                }
            }
        }
        return ret;
    }
    //note this includes all exons regardless of exclusion because these will be drawn even if excluded.  Introns are not included
    public ArrayList<TranscriptElement> getIncludedTranscriptElements(boolean includeIntrons){
        ArrayList<TranscriptElement> ret=new ArrayList<TranscriptElement>();
        for(int i=0;i<fullTranscript.size();i++){
            TranscriptElement tmp=fullTranscript.get(i);
            if(tmp.getType().equals("exon")){//add all exons excluded or not
                    ret.add(tmp);
            }else if(includeIntrons){//add introns only when displayed
                if(!tmp.isExclude()){//add only introns not excluded
                    ret.add(tmp);
                }
            }
        }
        return ret;
    }

    public void fillProbesetMap(HashMap m){
        for(int i=0;i<exons.size();i++){
            ArrayList<ProbeSet> tmp=exons.get(i).getProbeSet();
            //System.out.println("getProgeSetList"+i+":"+tmp.size());
            for(int j=0;j<tmp.size();j++){
                ProbeSet ps=tmp.get(j);
                if(!m.containsKey(ps.getProbeSetID())){
                    m.put(ps.getProbeSetID(), ps);
                }
            }
        }
        if(introns!=null){
            for(int i=0;i<introns.size();i++){
                ArrayList<ProbeSet> tmp=introns.get(i).getProbeSet();
                //System.out.println("getProgeSetList"+i+":"+tmp.size());
                for(int j=0;j<tmp.size();j++){
                    ProbeSet ps=tmp.get(j);
                    if(!m.containsKey(ps.getProbeSetID())){
                        m.put(ps.getProbeSetID(), ps);
                    }
                }
            }
        }
    }
    
}
