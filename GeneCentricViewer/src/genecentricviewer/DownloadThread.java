/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genecentricviewer;

import edu.ucdenver.ccp.phenogen.applets.data.FullHeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.Gene;
import edu.ucdenver.ccp.phenogen.applets.data.PanelProbesetData;
import edu.ucdenver.ccp.phenogen.applets.data.ProbesetData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smahaffey
 */
public class DownloadThread extends Thread{
    ArrayList<String[]> downloads=new ArrayList<String[]>();
    GeneCentricViewer gcv=null;
    
    /*String geneFile="";
    String deMeanFile="";
    String deFoldDiffFile="";
    String panelHeritFile="";
    
    boolean geneFileFinished=false;
    boolean deMeanFileFinished=false;
    boolean deFoldDiffFileFinished=false;
    boolean panelHeritFileFinished=false;*/
    
    //edu.ucdenver.ccp.phenogen.applets.exonviewer.data.FullHeatMapData fhmd=null;
    ProbesetData psd=null;
    ArrayList<Gene> genes=null;
    ArrayList<Boolean> loaded=null;
    PanelProbesetData ppd=null;
    DownloadThread(GeneCentricViewer genecv){
        gcv=genecv;
    }
    
    public void setFiles(ArrayList<String[]> toDownload){
        downloads=toDownload;
        loaded=new ArrayList<Boolean>();
        for(int i=0;i<downloads.size();i++){
            String[] tmp=downloads.get(i);
            System.out.println("To Download:"+tmp[0]+":"+tmp[1]);
            loaded.add(new Boolean(false));
        }
    }
    

    @Override
    public void run() {
        boolean outOfMem=false;
        try{
            System.out.println("Starting DOWNLOAD");
            String deFoldDiffFile="";
            while(!downloads.isEmpty()){
                for(int i=0;i<downloads.size();i++){
                    String[] tmp=downloads.get(i);
                    System.out.println("Download Thread:process "+i+":"+tmp[0]+"::"+tmp[1]);
                    if(tmp[0].equals("geneXML")){
                        System.out.println("Download Gene.xml");
                        String geneFile=tmp[1];
                        genes=ReadDataFiles.readGenes(geneFile);
                        gcv.setupGene(genes);
                        loaded.set(i, true);
                    }else if(tmp[0].equals("deMean")){
                        System.out.println("Download Mean.csv");
                        String deMeanFile=tmp[1];
                        for(int j=i+1;j<downloads.size()&&deFoldDiffFile.equals("");j++){
                            String[] tmpj=downloads.get(j);
                            if(tmpj[0].equals("deFoldDiff")){
                                deFoldDiffFile=tmpj[1];
                                System.out.println("removing..."+j+":j:"+tmpj[0]);
                                downloads.remove(j);
                                loaded.remove(j);
                            }
                        }
                        if(!deFoldDiffFile.equals("")){
                            try {
                                psd=ReadDataFiles.readProbesetData(deMeanFile,deFoldDiffFile);
                                gcv.setupDE(psd);
                                loaded.set(i, true);
                            } catch (IOException ex) {
                               System.out.println("File Not Ready Yet:"+deMeanFile+" or "+deFoldDiffFile);
                            }
                            
                        }
                    }else if(tmp[0].equals("panelHerit")){
                        System.out.println("Download Panel Herit");
                        String panelHeritFile=tmp[1];
                        try{
                            ppd=ReadDataFiles.readPanelProbesetDataHerit(panelHeritFile);
                            gcv.setupHerit(ppd);
                            loaded.set(i,true);
                        }catch(IOException ex){
                            System.out.println("File Not Ready Yet:"+panelHeritFile);
                        }
                    }else if(tmp[0].equals("panelExp")){
                        System.out.println("Download Panel Expr");
                        String panelExprFile=tmp[1];
                        try{
                            ReadDataFiles.readPanelProbesetDataExpr(panelExprFile,ppd);
                            gcv.setupExpr(ppd);
                            loaded.set(i,true);
                        }catch(IOException ex){
                            System.out.println("File Not Ready Yet:"+panelExprFile);
                        }
                    }else if(tmp[0].equals("eqtlTranscript")){
                        //System.out.println("Download Exon Cor Heat Map");
                        String transcriptFile=tmp[1];
                        try{
                            ReadDataFiles.isTrancriptClusterIDListReady(transcriptFile);
                            gcv.setQTLEnabled(true);
                            loaded.set(i, true);
                        }catch(IOException ex){
                            System.out.println("File Not Ready Yet:"+transcriptFile);
                        }
                    }
                    if(loaded.get(i).booleanValue()){
                        System.out.println("removing..."+tmp[0]);
                        downloads.remove(i);
                        loaded.remove(i);
                        i--;
                    }
                }
                try {
                    this.sleep(15000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("END DOWNLOAD");
        }catch(OutOfMemoryError e){
            outOfMem=true;
            gcv.setOutOfMem(outOfMem);
        }
        /*try {
            this.wait(15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //gcv.setup(fhmd, genes,outOfMem);
        System.out.println("END DOWNLOAD AND GUI UPDATE");
    }
    
    
    
}
