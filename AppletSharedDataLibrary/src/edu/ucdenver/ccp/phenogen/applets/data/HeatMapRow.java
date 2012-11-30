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
public class HeatMapRow {

    String probeID="";
    ArrayList<Double> values=new ArrayList<Double>();
    
    public HeatMapRow(String row){
        //System.out.println(row);
        String[] split=row.split(",");
        probeID=split[0];
        //System.out.println("Probe:"+this.probeID+":"+split.length);
        for(int i=1;i<split.length;i++){
            values.add(Double.parseDouble(split[i]));
        }
        //System.out.println("HMR:Val len:"+values.size());
    }

    String getProbeID() {
        return probeID;
    }
    
    double[] getValuesForProbes(ArrayList<Integer> probeIndex){
        double[] ret=new double[probeIndex.size()];
        //System.out.println("getValues for row");
        for(int i=0;i<probeIndex.size();i++){
            //System.out.print(probeIndex.get(i).intValue()+" ");
            ret[i]=values.get(probeIndex.get(i).intValue());
        }
        //System.out.println();
        return ret;
    }

}
