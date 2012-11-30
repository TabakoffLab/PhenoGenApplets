/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author smahaffey
 */
public class Gene {
    String geneID="",bioType="",chromosome="",strand="",geneSymbol="";
    long start=0,end=0,length=0;
    ArrayList<Transcript> transcripts=new ArrayList<Transcript>();
    
    public Gene(String geneID,long start,long end){
        this(geneID,start,end,"","","","");
    }
    public Gene(String geneID,long start, long end,String chromosome,String strand,String biotype,String symbol){
        this.geneID=geneID;
        this.start=start;
        this.end=end;
        this.geneSymbol=symbol;
        this.chromosome=chromosome;
        if(strand.equals("1")||strand.equals("+")||strand.equals("+1")){
            this.strand="+";
        }else if(strand.equals("-1")||strand.equals("-")){
            this.strand="-";
        }else{
            this.strand=".";
            System.err.println("Unknown Strand Type:"+strand);
        }
        this.bioType=biotype;
        if(start>end){
            this.length=start-end;
        }else{
            this.length=end-start;
        }
        
    }

    public String getBioType() {
        return bioType;
    }

    public void setBioType(String bioType) {
        this.bioType = bioType;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getGeneID() {
        return geneID;
    }

    public void setGeneID(String geneID) {
        this.geneID = geneID;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public ArrayList<Transcript> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(ArrayList<Transcript> transcripts) {
        this.transcripts = transcripts;
    }
    
    @Override
    public String toString(){
        return this.geneID+" "+this.geneSymbol+" "+this.bioType+" "+this.chromosome+" "+this.length+"bp";
    }
    
    public void numberExons(FullHeatMapData fhmd){
        //System.out.println(this.geneID+":"+transcripts.size());
        ArrayList<Exon> totalExons=new ArrayList<Exon>();
        if(transcripts!=null){
            for(int i=0;i<transcripts.size();i++){
                Transcript curTrans=transcripts.get(i);
                //System.out.println(curTrans.getID());
                ArrayList<Exon> curExons=curTrans.getExons();
                if(curExons!=null){
                    for(int j=0;j<curExons.size();j++){
                        Exon curExon=curExons.get(j);
                        curExon.setMaskedProbesets(fhmd);
                        //System.out.println(curExon.getID());
                        boolean found=false;
                        for(int k=0;k<totalExons.size()&&!found;k++){
                            Exon tExon=totalExons.get(k);
                            if(tExon.getID().equals(curExon.getID())){
                                //System.out.println("Found Existing");
                                found=true;
                            }else if(curExon.getStart()<tExon.getStart()){
                                //System.out.println("Inserted");
                                totalExons.add(k, curExon);
                                found=true;
                            }
                        }
                        if(!found){
                            //System.out.println("Added");
                            totalExons.add(curExon);
                        }
                    }
                    ArrayList<Intron> curIntrons=curTrans.getIntrons();
                    if(curIntrons!=null){
                        for(int j=0;j<curIntrons.size();j++){
                            Intron curIntron=curIntrons.get(j);
                            curIntron.setMaskedProbesets(fhmd);
                        }
                    }
                }
            }
        }
        HashMap hm=new HashMap();
        for(int i=0;i<totalExons.size();i++){
            Exon cur=totalExons.get(i);
            hm.put(cur.getID(), i);
            //System.out.println(cur.getID()+":"+i);
        }
        if(transcripts!=null){
            for(int i=0;i<transcripts.size();i++){
                Transcript curTrans=transcripts.get(i);
                ArrayList<Exon> curExons=curTrans.getExons();
                for(int j=0;j<curExons.size();j++){
                    Exon curExon=curExons.get(j);
                    Object index=hm.get(curExon.getID());
                    int ind=-1;
                    if(index!=null){
                        ind=Integer.parseInt(index.toString());
                    }
                    curExon.setNumber(ind+1);
                }
            }
        }
    }
    
    public void numberExons(ExCorFullHeatMapData fhmd){
        //System.out.println(this.geneID+":"+transcripts.size());
        ArrayList<Exon> totalExons=new ArrayList<Exon>();
        if(transcripts!=null){
            for(int i=0;i<transcripts.size();i++){
                Transcript curTrans=transcripts.get(i);
                //System.out.println(curTrans.getID());
                ArrayList<Exon> curExons=curTrans.getExons();
                if(curExons!=null){
                    for(int j=0;j<curExons.size();j++){
                        Exon curExon=curExons.get(j);
                        curExon.setMaskedProbesets(fhmd);
                        //System.out.println(curExon.getID());
                        boolean found=false;
                        for(int k=0;k<totalExons.size()&&!found;k++){
                            Exon tExon=totalExons.get(k);
                            if(tExon.getID().equals(curExon.getID())){
                                //System.out.println("Found Existing");
                                found=true;
                            }else if(curExon.getStart()<tExon.getStart()){
                                //System.out.println("Inserted");
                                totalExons.add(k, curExon);
                                found=true;
                            }
                        }
                        if(!found){
                            //System.out.println("Added");
                            totalExons.add(curExon);
                        }
                    }
                    ArrayList<Intron> curIntrons=curTrans.getIntrons();
                    if(curIntrons!=null){
                        for(int j=0;j<curIntrons.size();j++){
                            Intron curIntron=curIntrons.get(j);
                            curIntron.setMaskedProbesets(fhmd);
                        }
                    }
                }
            }
        }
        HashMap hm=new HashMap();
        for(int i=0;i<totalExons.size();i++){
            Exon cur=totalExons.get(i);
            hm.put(cur.getID(), i);
            //System.out.println(cur.getID()+":"+i);
        }
        if(transcripts!=null){
            for(int i=0;i<transcripts.size();i++){
                Transcript curTrans=transcripts.get(i);
                ArrayList<Exon> curExons=curTrans.getExons();
                for(int j=0;j<curExons.size();j++){
                    Exon curExon=curExons.get(j);
                    Object index=hm.get(curExon.getID());
                    int ind=-1;
                    if(index!=null){
                        ind=Integer.parseInt(index.toString());
                    }
                    curExon.setNumber(ind+1);
                }
            }
        }
    }
}
