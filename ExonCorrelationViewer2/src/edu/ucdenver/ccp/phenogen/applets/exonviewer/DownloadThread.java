/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucdenver.ccp.phenogen.applets.exonviewer;

import edu.ucdenver.ccp.phenogen.applets.data.ExCorFullHeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.Gene;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smahaffey
 */
public class DownloadThread extends Thread{
    String heatfile="",genefile="";
    ExonCorrelationView ecv=null;
    
    ExCorFullHeatMapData fhmd=null;
    Gene[] genes=null;
    DownloadThread(ExonCorrelationView exoncv){
        ecv=exoncv;
    }

    public String getGeneFile() {
        return genefile;
    }

    public void setGeneFile(String genefile) {
        this.genefile = genefile;
    }

    public String getHeatFile() {
        return heatfile;
    }

    public void setHeatFile(String heatfile) {
        this.heatfile = heatfile;
    }
    
    public ExCorFullHeatMapData getFHMD(){
        return fhmd;
    }
    
    public Gene[] getGenes(){
        return genes;
    }

    @Override
    public void run() {
        boolean outOfMem=false;
        try{
            System.out.println("Starting DOWNLOAD");
            fhmd=ReadDataFiles.readHeatMaps(heatfile);
            genes=ReadDataFiles.readGenes(genefile);
            System.out.println("END DOWNLOAD");
        }catch(OutOfMemoryError e){
            outOfMem=true;
        }
        /*try {
            this.wait(15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        ecv.setup(fhmd, genes,outOfMem);
        System.out.println("END DOWNLOAD AND GUI UPDATE");
    }
    
    
    
}
