/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

/**
 *
 * @author smahaffey
 */
public class Probe implements Comparable {
    long start=-1,stop=-1,len=-1;
    String probeID="";
    String sequence="",strand="";
    
    public Probe(long start, long stop, String probeID,String seq, String strand){
        this.start=start;
        this.stop=stop;
        this.probeID=probeID;
        this.sequence=seq;
        this.strand=strand;
        len=Math.abs(start-stop);
    }

    public long getLen() {
        return len;
    }

    public String getProbeID() {
        return probeID;
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

    
    

    @Override
    public int compareTo(Object t) {
        ProbeSet p2=(ProbeSet)t;
        int ret=0;
        if(this.probeID.equals(p2.probeSetID)&&this.start==p2.start&&this.stop==p2.stop){
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
    
    public boolean equals(Object t){
        ProbeSet p2=(ProbeSet)t;
        boolean ret=true;
        if(this.probeID.equals(p2.probeSetID)&&this.start==p2.start&&this.stop==p2.stop){
            ret=true;
        }else{
            ret=false;
        }
        return ret;
    }
    
    
    @Override
    public String toString(){
        String ret=this.probeID;
        return ret;
    }
    
}