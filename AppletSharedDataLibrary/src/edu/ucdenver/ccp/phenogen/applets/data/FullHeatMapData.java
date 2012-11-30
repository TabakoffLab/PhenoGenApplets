/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.data;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author smahaffey
 */
public class FullHeatMapData {
    ArrayList<HeatMapRow> hmr=new ArrayList<HeatMapRow>();
    HashMap probeTable=new HashMap();
    
    
    public FullHeatMapData(){
        
    }
    
    public void readFromFile(String surl){
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
                    first=false;
                }else{
                    //System.out.println(probeTableInd);
                    HeatMapRow tmp=new HeatMapRow(line.replaceAll("\"", ""));
                    String probe=tmp.getProbeID();
                    probeTable.put(probe, probeTableInd);
                    probeTableInd++;
                    hmr.add(tmp);
                }
                line=br.readLine();
            }
            /*in = new DataInputStream(url.openStream());
            byte[] bytes=new byte[2048];
            int read=in.read(bytes);
            boolean first=true;
            int count=1;
            while(read==2048){
                sb.append(new String(bytes,0,read));
                if(count%5==0){
                    String tmpleft=readFromString(sb,first,false);
                    if(first){
                        first=false;
                    }
                    sb=new StringBuilder();
                    sb.append(tmpleft);
                }
                read=in.read(bytes);
            }
            if(read>-1){
                sb.append(new String(bytes,0,read));
            }
            if(sb.length()>0){
                readFromString(sb,first,true);
            }*/
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
        //this.readFromString(sb.toString());
    }
    
    /*public String readFromString(StringBuilder sb,boolean skipFirst, boolean last){
        String[] lines=sb.toString().split("\n");
        String left="";
        if(!last&&lines.length==1){
            left=sb.toString();
        }else{
            int start=0;
            if(skipFirst){
                start=1;
            }
            if(!last){
                for(int i=start;i<lines.length-1;i++){
                    HeatMapRow tmp=new HeatMapRow(lines[i].replaceAll("\"", ""));
                    String probe=tmp.getProbeID();
                    probeTable.put(probe, probeTableInd);
                    probeTableInd++;
                    hmr.add(tmp);
                }
                left=lines[lines.length-1];
            }else{
                for(int i=start;i<lines.length;i++){
                    HeatMapRow tmp=new HeatMapRow(lines[i].replaceAll("\"", ""));
                    String probe=tmp.getProbeID();
                    probeTable.put(probe, probeTableInd);
                    probeTableInd++;
                    hmr.add(tmp);
                }
            }
        }
        System.out.println("Full Map Size:"+probeTable.size());
        return left;
    }*/
    
    public HeatMapData generateMap(ArrayList<ProbeSet> fullProbes){
        //System.out.println("generateMap");
        //System.out.println("full probe list size:"+fullProbes.size());
        ArrayList<Integer> probeInd=new ArrayList<Integer>();
        ArrayList<ProbeSet> probeList=new ArrayList<ProbeSet>();
        ArrayList<ProbeSet> missingList=new ArrayList<ProbeSet>();
        for(int i=0;i<fullProbes.size();i++){
            //System.out.println("Probe ID:"+fullProbes.get(i).getProbeSetID());
            if(probeTable.containsKey(fullProbes.get(i).getProbeSetID())){
                int index=Integer.parseInt(probeTable.get(fullProbes.get(i).getProbeSetID()).toString());
                //System.out.println("Included:"+index);
                probeInd.add(index);
                probeList.add(fullProbes.get(i));
            }else{
                System.out.println("ERROR SHOULD NO LONGER GET Excluded PROBES HERE");
                missingList.add(fullProbes.get(i));
            }
        }
        double[][] tmpValues=new double[probeInd.size()][probeInd.size()];
        for(int i=0;i<probeInd.size();i++){
            tmpValues[i]=hmr.get(probeInd.get(i).intValue()).getValuesForProbes(probeInd);
        }
        HeatMapData ret=new HeatMapData(HeatMapData.TWO_COLOR);
        ret.setMinMax(-1,1,0);
        ret.setData(tmpValues, probeList,true);
        return ret;
    }
    
    public boolean isProbeMasked(String ProbeID){
        return probeTable.containsKey(hmr);
    }
    
}

