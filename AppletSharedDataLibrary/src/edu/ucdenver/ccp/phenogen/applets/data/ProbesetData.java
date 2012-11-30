/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smahaffey
 */
public class ProbesetData {
    //ArrayList<HeatMapRow> meanHMR=new ArrayList<HeatMapRow>();
    //HashMap meanProbes=new HashMap();
    HashMap data=new HashMap();
    HashMap tissues=new HashMap();
    HashMap meanStrains=new HashMap();
    HashMap fdStrains=new HashMap();
    ArrayList<String> fullProbeList=new ArrayList<String>();
    //HashMap fcProbes=new HashMap();
    
    public void readMeanHeatMap(InputStream in){
        BufferedReader br=null;
        try {
            br=new BufferedReader(new InputStreamReader(in));
            String line="";
            line=br.readLine();
            boolean first=true;
            int probeTableInd=0;
            while(line!=null){
                if(first){
                    int tissueInd=0;
                    int strainInd=0;
                    String[] split=line.split(",");
                    for(int i=1;i<split.length;i++){
                        String curTissue=split[i].substring(0,split[i].indexOf("."));
                        String curStrain=split[i].substring(split[i].indexOf(".")+1,split[i].lastIndexOf("."));
                        if(!tissues.containsValue(curTissue)){
                            tissues.put(tissueInd, curTissue);
                            tissueInd++;
                        }
                        if(!meanStrains.containsValue(curStrain)){
                            meanStrains.put(strainInd, curStrain);
                            strainInd++;
                        }
                    }
                    first=false;
                }else{
                    String[] split=line.split(",");
                    String probeID=split[0];
                    fullProbeList.add(probeID);
                    HashMap tmpData=new HashMap();
                    ArrayList<Double> means=new ArrayList<Double>();
                    for(int i=1;i<split.length;i++){
                        means.add(Double.parseDouble(split[i]));
                    }
                    tmpData.put("meanRow", means);
                    data.put(probeID, tmpData);
                }
                line=br.readLine();
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(FullHeatMapData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                Logger.getLogger(FullHeatMapData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
    public void readFoldDiffHeatMap(String surl){
        BufferedReader br=null;
        try {
            URL url=new URL(surl);
            br=new BufferedReader(new InputStreamReader(url.openStream()));
            String line="";
            line=br.readLine();
            boolean first=true;
            int probeTableInd=0;
            while(line!=null){
                if(first){
                    int tissueInd=0;
                    int strainInd=0;
                    String[] split=line.split(",");
                    for(int i=1;i<split.length;i+=3){
                        String curTissue=split[i].substring(0,split[i].indexOf("."));
                        String curStrain=split[i].substring(split[i].indexOf(".")+1,split[i].lastIndexOf("."));
                        if(!tissues.containsValue(curTissue)){
                            tissues.put(tissueInd, curTissue);
                            tissueInd++;
                        }
                        if(!fdStrains.containsValue(curStrain)){
                            fdStrains.put(strainInd, curStrain);
                            strainInd++;
                        }
                    }
                    first=false;
                }else{
                    String[] split=line.split(",");
                    String probeID=split[0];
                    fullProbeList.add(probeID);
                    HashMap tmpData=(HashMap)data.get(probeID);
                    ArrayList<Double> fcList=new ArrayList<Double>();
                    ArrayList<Double> fdrList=new ArrayList<Double>();
                    ArrayList<Double> pvalList=new ArrayList<Double>();
                    for(int i=1;i<split.length;i+=3){
                        double fc=Double.parseDouble(split[i]);
                        double pval=Double.parseDouble(split[i+1]);
                        double fdr=Double.parseDouble(split[i+2]);
                        fcList.add(fc);
                        fdrList.add(fdr);
                        pvalList.add(pval);
                    }
                    tmpData.put("fcRow", fcList);
                    tmpData.put("pvalRow",pvalList);
                    tmpData.put("fdrRow",fdrList);
                }
                line=br.readLine();
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            Logger.getLogger(FullHeatMapData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                Logger.getLogger(FullHeatMapData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public HeatMapData generateMeanHeatMapData(ArrayList<String> probeList){
        HeatMapData hmd=new HeatMapData(HeatMapData.ONE_COLOR);
        double[][] ddata=new double[probeList.size()][tissues.size()*meanStrains.size()];
        int actualSize=0;
        for(int i=0;i<probeList.size();i++){
            //System.out.println("get data:"+probeList.get(i));
            HashMap tmp=(HashMap)data.get(probeList.get(i));
            if(tmp!=null){
                ArrayList<Double> row=(ArrayList<Double>)tmp.get("meanRow");
                for(int j=0;j<row.size();j++){
                    ddata[i][j]=row.get(j).doubleValue();
                }
                actualSize++;
            }else{
                //System.out.println();
                probeList.remove(i);
                i--;
            }
        }
        double[][] tmpdata=new double[actualSize][tissues.size()*meanStrains.size()];
        for(int i=0;i<tmpdata.length;i++){
            tmpdata[i]=ddata[i];
        }
        hmd.setDataString(tmpdata, probeList,false);
        return hmd;
    }
    
    public HeatMapData generateFoldDiffHeatMapData(ArrayList<String> probeList){
        HeatMapData hmd=new HeatMapData(HeatMapData.TWO_COLOR);
        double[][] ddata=new double[probeList.size()][tissues.size()*fdStrains.size()];
        int actualSize=0;
        for(int i=0;i<probeList.size();i++){
            HashMap tmp=(HashMap)data.get(probeList.get(i));
            if(tmp!=null){
                ArrayList<Double> row=(ArrayList<Double>)tmp.get("fcRow");
                for(int j=0;j<row.size();j++){
                    ddata[i][j]=row.get(j).doubleValue();
                }
                actualSize++;
            }else{
                probeList.remove(i);
                i--;
            }
        }
        double[][] tmpdata=new double[actualSize][tissues.size()*meanStrains.size()];
        for(int i=0;i<tmpdata.length;i++){
            tmpdata[i]=ddata[i];
        }
        hmd.setDataString(tmpdata, probeList,true);
        return hmd;
    }
    
    public ArrayList<String> getProbeList(){
        return fullProbeList;
    }
    public HashMap getTissues(){
        return this.tissues;
    }
    public HashMap getMeanStrains()
    {
        return this.meanStrains;
    }

    public HashMap getFoldDiffStrains() {
        return this.fdStrains;
    }

    public ArrayList<Double> getFoldDiff(String probeID) {
        ArrayList<Double> ret=null;
        HashMap tmp=(HashMap) data.get(probeID);
        if(tmp!=null){
            ret=(ArrayList<Double>)tmp.get("fcRow");
        }
        return ret;
    }

    public ArrayList<Double> getPval(String probeID) {
        ArrayList<Double> ret=null;
        HashMap tmp=(HashMap) data.get(probeID);
        if(tmp!=null){
            ret=(ArrayList<Double>)tmp.get("pvalRow");
        }
        return ret;
    }

    public ArrayList<Double> getFDR(String probeID) {
        ArrayList<Double> ret=null;
        HashMap tmp=(HashMap) data.get(probeID);
        if(tmp!=null){
            ret=(ArrayList<Double>)tmp.get("fdrRow");
        }
        return ret;
    }
}

/*class DEHeatMapRow{
    String probeID="";
    ArrayList<Double> values=new ArrayList<Double>();
    
    public DEHeatMapRow(String row){
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
}*/