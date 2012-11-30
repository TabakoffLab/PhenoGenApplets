/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 *
 * @author smahaffey
 */
public class PanelProbesetData {
    HashMap data=new HashMap();
    HashMap tissues=new HashMap();
    HashMap tissueStrianHeaders=new HashMap();
    ArrayList<String> fullProbeList=new ArrayList<String>();
    ArrayList<String> heritDabgProbeList=new ArrayList<String>();
    
    public void readHerit(InputStream in){
        BufferedReader br=null;
        try {
            br=new BufferedReader(new InputStreamReader(in));
            String line="";
            line=br.readLine();
            boolean first=true;
            while(line!=null){
                if(first){
                    int tissueInd=0;
                    int strainInd=0;
                    String[] split=line.split(",");
                    for(int i=1;i<split.length;i++){
                        String curTissue=split[i].substring(0,split[i].indexOf("."));
                        if(!tissues.containsValue(curTissue)){
                            tissues.put(tissueInd, curTissue);
                            tissueInd++;
                        }
                    }
                    first=false;
                }else{
                    String[] split=line.split(",");
                    String probeID=split[0];
                    fullProbeList.add(probeID);
                    HashMap tmpData=new HashMap();
                    ArrayList<Double> herit=new ArrayList<Double>();
                    ArrayList<Double> dabg=new ArrayList<Double>();
                    for(int i=1;i<split.length;i+=2){
                        herit.add(Double.parseDouble(split[i]));
                        dabg.add(Double.parseDouble(split[i+1]));
                    }
                    tmpData.put("heritRow", herit);
                    tmpData.put("dabgRow",dabg);
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
    
    public void readExpr(InputStream in){
        BufferedReader br=null;
        try {
            br=new BufferedReader(new InputStreamReader(in));
            String line="";
            line=br.readLine();
            boolean first=true;
            HashMap oneColumn=new HashMap();
            HashMap strainOrder=new HashMap();
            while(line!=null){
                String[] split=line.split("\t");
                if(first||split[0].equals("Probeset")){
                    strainOrder=new HashMap();
                    oneColumn=new HashMap();
                    int count=0;
                    for(int i=2;i<split.length;i+=2){
                       String s1=split[i].substring(0,split[i].indexOf(".Mean"));
                       strainOrder.put(count,s1.trim());
                       count++;
                       if(split[i+1].indexOf(".StdErr")>-1){
                            //String s2=split[i+1].substring(0,split[i+1].indexOf(".StdErr"));
                       }else {
                           oneColumn.put(i,1);
                           i--;
                       }
                    }
                    first=false;
                }else{
                    
                    HashMap probe=null;
                    HashMap tissueExpr=null;
                    String probeID=split[0];
                    String tissue=split[1];
                    if(!tissueStrianHeaders.containsKey(tissue)){
                        tissueStrianHeaders.put(tissue, strainOrder);
                    }
                    if(data.containsKey(probeID)){
                        probe=(HashMap) data.get(probeID);
                        if(probe.containsKey("Expr")){
                            tissueExpr=(HashMap) probe.get("Expr");
                        }else{
                            tissueExpr=new HashMap();
                            probe.put("Expr", tissueExpr);
                        }
                    }else{
                        probe=new HashMap();
                        data.put(probeID,probe);
                        fullProbeList.add(probeID);
                        tissueExpr=new HashMap();
                        probe.put("Expr", tissueExpr);
                        
                    }
                    
                    HashMap tmpData=new HashMap();
                    ArrayList<Double> mean=new ArrayList<Double>();
                    ArrayList<Double> stderr=new ArrayList<Double>();
                    for(int i=2;i<split.length;i+=2){
                        mean.add(Double.parseDouble(split[i]));
                        if(!oneColumn.containsKey(i)){
                            stderr.add(Double.parseDouble(split[i+1]));
                        }else{
                            stderr.add(0.0);
                            i--;
                        }
                    }
                    tmpData.put("meanRow", mean);
                    tmpData.put("stderrRow",stderr);
                    tissueExpr.put(tissue, tmpData);
                    
                    //data.put(probeID, tmpData);
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

    public ArrayList<DefaultKeyedValues2DDataset> getChartData(ArrayList<String> filteredHeritProbeList) {
        ArrayList<DefaultKeyedValues2DDataset> ret=new ArrayList();
        for(int i=0;i<filteredHeritProbeList.size();i++){
            //System.out.print("looking for probeset"+filteredHeritProbeList.get(i));
            if(data.containsKey(filteredHeritProbeList.get(i))){
                //System.out.println("found");
                HashMap hm=(HashMap) data.get(filteredHeritProbeList.get(i));
                ArrayList<Double> heritRow=(ArrayList<Double>) hm.get("heritRow");
                if(heritRow!=null){
                    DefaultKeyedValues2DDataset dkvd=new DefaultKeyedValues2DDataset();
                    for(int j=0;j<tissues.size();j++){
                        dkvd.addValue(heritRow.get(j).doubleValue(), tissues.get(j).toString(), "Tissue");
                    }
                    ret.add(dkvd);
                }
            }else{
                //System.out.println("not found");
                filteredHeritProbeList.remove(i);
                i--;
            }
        }
        return ret;
        
    }
    
    public ArrayList<DefaultKeyedValues2DDataset> getChartDataSingle(ArrayList<String> filteredHeritProbeList) {
        ArrayList<DefaultKeyedValues2DDataset> ret=new ArrayList();
        DefaultKeyedValues2DDataset dkvd=new DefaultKeyedValues2DDataset();
        for(int i=0;i<filteredHeritProbeList.size();i++){
            //System.out.print("looking for probeset"+filteredHeritProbeList.get(i));
            if(data.containsKey(filteredHeritProbeList.get(i))){
                HashMap hm=(HashMap) data.get(filteredHeritProbeList.get(i));
                ArrayList<Double> heritRow=(ArrayList<Double>) hm.get("heritRow");
                if(heritRow!=null){
                    for(int j=0;j<tissues.size();j++){
                        dkvd.addValue(heritRow.get(j).doubleValue(), tissues.get(j).toString(), filteredHeritProbeList.get(i));
                    }
                }
            }else{
                //System.out.println("not found");
                filteredHeritProbeList.remove(i);
                i--;
            }
        }
        ret.add(dkvd);
        return ret;
        
    }
    
    public ArrayList<DefaultKeyedValues2DDataset> getChartDataTissue(ArrayList<String> filteredHeritProbeList) {
        ArrayList<DefaultKeyedValues2DDataset> ret=new ArrayList();
        for(int j=0;j<tissues.size();j++){
            DefaultKeyedValues2DDataset dkvd=new DefaultKeyedValues2DDataset();
            for(int i=0;i<filteredHeritProbeList.size();i++){
                //System.out.print("looking for probeset"+filteredHeritProbeList.get(i));
                if(data.containsKey(filteredHeritProbeList.get(i))){
                    HashMap hm=(HashMap) data.get(filteredHeritProbeList.get(i));
                    ArrayList<Double> heritRow=(ArrayList<Double>) hm.get("heritRow");
                    if(heritRow!=null){
                        dkvd.addValue(heritRow.get(j).doubleValue(), tissues.get(j).toString(),filteredHeritProbeList.get(i) );
                    }/*else{//added remove if causes problems
                        filteredHeritProbeList.remove(i);
                        i--;
                    }*/
                }else{
                    //System.out.println("not found");
                    filteredHeritProbeList.remove(i);
                    i--;
                }
            }
            ret.add(dkvd);
        }
        
        return ret;
        
    }
    public ArrayList<DefaultKeyedValues2DDataset> getChartDataExpTissue(ArrayList<String> filteredExpProbeList,String series) {
        ArrayList<DefaultKeyedValues2DDataset> ret=new ArrayList();
        for(int j=0;j<tissues.size();j++){
            DefaultKeyedValues2DDataset dkvd=new DefaultKeyedValues2DDataset();
            for(int i=0;i<filteredExpProbeList.size();i++){
                //System.out.print("looking for probeset"+filteredHeritProbeList.get(i));
                if(data.containsKey(filteredExpProbeList.get(i))){
                    HashMap hm=(HashMap) data.get(filteredExpProbeList.get(i));
                    HashMap expr=(HashMap) hm.get("Expr");
                    HashMap tissueData=(HashMap) expr.get(tissues.get(j));
                    if(tissueData!=null){
                        ArrayList<Double> expRow=(ArrayList<Double>) tissueData.get("meanRow");
                        HashMap strains=(HashMap) this.tissueStrianHeaders.get(tissues.get(j));
                        for(int k=0;k<strains.size();k++){
                            //System.out.println("get expRow"+expRow.get(k).doubleValue());
                            //System.out.println("get strains"+(String)strains.get(k));

                            String s1=filteredExpProbeList.get(i);
                            String s2=(String)strains.get(k);
                            if(series.equals("strain")){
                                s1=(String)strains.get(k);
                                s2=filteredExpProbeList.get(i);
                            }

                            dkvd.addValue(expRow.get(k).doubleValue(),s1 ,s2);
                        }
                    }
                }else{
                    //System.out.println("not found");
                    filteredExpProbeList.remove(i);
                    i--;
                }
            }
            ret.add(dkvd);
        }
        
        return ret;
        
    }
    
    public ArrayList<DefaultKeyedValues2DDataset> getChartDataExpTissue(ArrayList<String> filteredExpProbeList,String series,String tissue) {
        ArrayList<DefaultKeyedValues2DDataset> ret=new ArrayList();    
        for (int i = 0; i < filteredExpProbeList.size(); i++) {
            DefaultKeyedValues2DDataset dkvd = new DefaultKeyedValues2DDataset();
            if (data.containsKey(filteredExpProbeList.get(i))) {
                HashMap hm = (HashMap) data.get(filteredExpProbeList.get(i));
                HashMap expr = (HashMap) hm.get("Expr");
                HashMap tissueData = (HashMap) expr.get(tissue);
                ArrayList<Double> expRow = (ArrayList<Double>) tissueData.get("meanRow");
                HashMap strains = (HashMap) this.tissueStrianHeaders.get(tissue);
                for (int k = 0; k < strains.size(); k++) {
                    //System.out.println("get expRow" + expRow.get(k).doubleValue());
                    //System.out.println("get strains" + (String) strains.get(k));
                    //String s1 = filteredExpProbeList.get(i);
                    //String s2 = (String) strains.get(k);
                    //if (series.equals("strain")) {
                        String s1 = (String) strains.get(k);
                        String s2 = "1";
                    //}

                    dkvd.addValue(expRow.get(k).doubleValue(), s2,s1);
                }
                ret.add(dkvd);
            } else {
                //System.out.println("not found");
                filteredExpProbeList.remove(i);
                i--;
            }
        }
        return ret;
        
    }
    public String[] getTissueList(){
        String[] ret=new String[tissues.size()];
        for(int j=0;j<tissues.size();j++){
            ret[j]=tissues.get(j).toString();
        }
        return ret;
    }

    public ArrayList<String> getFullProbeList() {
        return fullProbeList;
    }

    public ArrayList<Double> getHerit(String probeID) {
        ArrayList<Double> ret=null;
        HashMap tmp=(HashMap) data.get(probeID);
        if(tmp!=null){
            ret=(ArrayList<Double>)tmp.get("heritRow");
        }
        return ret;
    }
    
    public ArrayList<Double> getDABG(String probeID) {
        ArrayList<Double> ret=null;
        HashMap tmp=(HashMap) data.get(probeID);
        if(tmp!=null){
            ret=(ArrayList<Double>)tmp.get("dabgRow");
        }
        return ret;
    }
    
    public String[] getStrainHeader(String tissue){
        String[] ret=null;
        HashMap header=(HashMap) this.tissueStrianHeaders.get(tissue);
        if(header!=null){
            ret=new String[header.size()];
            for(int i=0;i<header.size();i++){
                ret[i]=(String) header.get(i);
            }
        }
        return ret;
    }

    public ArrayList<DefaultStatisticalCategoryDataset> getChartDataExpTissueStats(ArrayList<String> filteredExpProbeList, String seriesType, String tissue) {
        ArrayList<DefaultStatisticalCategoryDataset> ret=new ArrayList();    
        for (int i = 0; i < filteredExpProbeList.size(); i++) {
            DefaultStatisticalCategoryDataset dkvd = new DefaultStatisticalCategoryDataset();
            if (data.containsKey(filteredExpProbeList.get(i))) {
                HashMap hm = (HashMap) data.get(filteredExpProbeList.get(i));
                HashMap expr = (HashMap) hm.get("Expr");
                HashMap tissueData = (HashMap) expr.get(tissue);
                ArrayList<Double> expRow = (ArrayList<Double>) tissueData.get("meanRow");
                ArrayList<Double> errRow = (ArrayList<Double>) tissueData.get("stderrRow");
                HashMap strains = (HashMap) this.tissueStrianHeaders.get(tissue);
                for (int k = 0; k < strains.size(); k++) {
                    //System.out.println("get expRow" + expRow.get(k).doubleValue());
                    //System.out.println("get strains" + (String) strains.get(k));
                    //String s1 = filteredExpProbeList.get(i);
                    //String s2 = (String) strains.get(k);
                    //if (series.equals("strain")) {
                        String s1 = (String) strains.get(k);
                        String s2 = "1";
                    //}
                    if(errRow!=null){
                        dkvd.add(expRow.get(k).doubleValue(),errRow.get(k).doubleValue(), s2,s1);
                    }else{
                        dkvd.add(expRow.get(k).doubleValue(),0, s2,s1);
                    }
                        
                }
                ret.add(dkvd);
            } else {
                //System.out.println("not found");
                filteredExpProbeList.remove(i);
                i--;
            }
        }
        return ret;
    }

   
}
