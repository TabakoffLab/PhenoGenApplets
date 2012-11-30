/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genecentricviewer;


import edu.ucdenver.ccp.phenogen.applets.data.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author smahaffey
 */
public class ReadDataFiles {
    
    public static ArrayList<Gene> readGenes(String url) {
        ArrayList<Gene> genelist=new ArrayList<Gene>();
        try {
            
            DocumentBuilder build=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document transcriptDoc=build.parse(url);
            NodeList genes=transcriptDoc.getElementsByTagName("Gene");
            //System.out.println("# Genes"+genelist.length);
            for(int i=0;i<genes.getLength();i++){
                NamedNodeMap attrib=genes.item(i).getAttributes();
                if(attrib.getLength()>0){
                    String geneID=attrib.getNamedItem("ID").getNodeValue();
                    //System.out.println("reading gene ID:"+geneID);
                    String geneSymbol=attrib.getNamedItem("geneSymbol").getNodeValue();
                    String biotype=attrib.getNamedItem("biotype").getNodeValue();
                    long start=Long.parseLong(attrib.getNamedItem("start").getNodeValue());
                    long stop=Long.parseLong(attrib.getNamedItem("stop").getNodeValue());
                    String strand=attrib.getNamedItem("strand").getNodeValue();
                    String chr=attrib.getNamedItem("chromosome").getNodeValue();
                    Gene tmpG=new Gene(geneID,start,stop,chr,strand,biotype,geneSymbol);
                    NodeList transcripts=genes.item(i).getChildNodes();
                    ArrayList<Transcript> tmp=readTranscripts(transcripts.item(1).getChildNodes());
                    tmpG.setTranscripts(tmp);
                    genelist.add(tmpG);
                }
            }
        } catch (SAXException ex) {
            ex.printStackTrace(System.err);
            
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.err);
            
        }
        return genelist;
        
    }
    private static ArrayList<Transcript> readTranscripts(NodeList nodes) {
        ArrayList<Transcript> transcripts=new ArrayList<Transcript>();
        for(int i=0;i<nodes.getLength();i++){
            if(nodes.item(i).getNodeName().equals("Transcript")){
                ArrayList<Exon> exons=null;
                ArrayList<Intron> introns=null;
                NodeList children=nodes.item(i).getChildNodes();
                for(int j=0;j<children.getLength();j++){
                    //System.out.println(j+":"+children.item(j).getNodeName());
                    if(children.item(j).getNodeName().equals("exonList")){
                        exons=readExons(children.item(j).getChildNodes());
                    }
                    if(children.item(j).getNodeName().equals("intronList")){
                        introns=readIntrons(children.item(j).getChildNodes());
                    }
                }
                NamedNodeMap nnm=nodes.item(i).getAttributes();
                long start=Long.parseLong(nnm.getNamedItem("start").getNodeValue());
                long end=Long.parseLong(nnm.getNamedItem("stop").getNodeValue());
                Transcript tmptrans=new Transcript(nnm.getNamedItem("ID").getNodeValue(),nnm.getNamedItem("strand").getNodeValue(),start,end);
                tmptrans.setExon(exons);
                tmptrans.setIntron(introns);
                tmptrans.fillFullTranscript();
                transcripts.add(tmptrans); 
            }
        }
        //System.out.println("Transcript Array List Size at read:"+transcripts.size());
        return transcripts;
        
    }
    private static ArrayList<Exon> readExons(NodeList exonNodes) {
        ArrayList<Exon> ret=new ArrayList<Exon>();
        for(int z=0;z<exonNodes.getLength();z++){
            //System.out.println("exonNodes"+z+":"+exonNodes.item(z).getNodeName());
            if (exonNodes.item(z).getNodeName().equals("exon")) {
                NamedNodeMap attrib=exonNodes.item(z).getAttributes();
                String ExonID=attrib.getNamedItem("ID").getNodeValue();
                //System.out.println("ExonID:"+ExonID);
                long exonStart=-1,exonStop=-1,CodeStart=-1,CodeStop=-1;
                exonStart = Long.parseLong(attrib.getNamedItem("start").getNodeValue());
                exonStop = Long.parseLong(attrib.getNamedItem("stop").getNodeValue());
                CodeStart = Long.parseLong(attrib.getNamedItem("coding_start").getNodeValue());
                CodeStop = Long.parseLong(attrib.getNamedItem("coding_stop").getNodeValue());
                String exonNum=attrib.getNamedItem("alternateID2").getNodeValue();
                
                ArrayList<ProbeSet> probesets=new ArrayList<ProbeSet>();
                NodeList children=exonNodes.item(z).getChildNodes();
                for (int x = 0; x < children.getLength(); x++) {
                    if(children.item(x).getNodeName().equals("ProbesetList")){
                         NodeList probeNodes=children.item(x).getChildNodes();
                         probesets=readProbeSet(probeNodes);
                     }
                }
                Exon tmp=new Exon(exonStart,exonStop,ExonID,exonNum);
                tmp.setProteinCoding(CodeStart,CodeStop);
                tmp.setProbeSets(probesets);
                ret.add(tmp);
            }
        }
        //System.out.println("Exon Array List Size at read:"+ret.size());
        return ret;
    }
    private static ArrayList<Intron> readIntrons(NodeList intronNodes) {
        ArrayList<Intron> ret=new ArrayList<Intron>();
        for(int z=0;z<intronNodes.getLength();z++){
            //System.out.println("exonNodes"+z+":"+exonNodes.item(z).getNodeName());
            if (intronNodes.item(z).getNodeName().equals("intron")) {
                NamedNodeMap attrib=intronNodes.item(z).getAttributes();
                String intronID=attrib.getNamedItem("ID").getNodeValue();
                //System.out.println("ExonID:"+ExonID);
                long intronStart=-1,intronStop=-1;
                intronStart = Long.parseLong(attrib.getNamedItem("start").getNodeValue());
                intronStop = Long.parseLong(attrib.getNamedItem("stop").getNodeValue());
                
                ArrayList<ProbeSet> probesets=new ArrayList<ProbeSet>();
                NodeList children=intronNodes.item(z).getChildNodes();
                for (int x = 0; x < children.getLength(); x++) {
                    if(children.item(x).getNodeName().equals("ProbesetList")){
                         NodeList probeNodes=children.item(x).getChildNodes();
                         probesets=readProbeSet(probeNodes);
                     }
                }
                Intron tmp=new Intron(intronStart,intronStop,intronID);
                tmp.setProbeSets(probesets);
                ret.add(tmp);
            }
        }
        //System.out.println("Exon Array List Size at read:"+ret.size());
        return ret;
    }
    
    private static ArrayList<ProbeSet> readProbeSet(NodeList probesetNodes){
        ArrayList<ProbeSet> ret=new ArrayList<ProbeSet>();
        //System.out.println("Probeset Node size:"+probesetNodes.getLength());
        for(int z=0;z<probesetNodes.getLength();z++){
            if (probesetNodes.item(z).getNodeName().equals("Probeset")) {
                NamedNodeMap attrib=probesetNodes.item(z).getAttributes();
                String probeID= attrib.getNamedItem("ID").getNodeValue();
                long probeStart=-1,probeStop=-1;
                double dabg=-1,herit=-1;
                String seq="",strand="",type="",locUpdate="";
                probeStart=Integer.parseInt(attrib.getNamedItem("start").getNodeValue());
                probeStop=Integer.parseInt(attrib.getNamedItem("stop").getNodeValue());
                seq=attrib.getNamedItem("sequence").getNodeValue();
                strand=attrib.getNamedItem("strand").getNodeValue();
                locUpdate=attrib.getNamedItem("updatedlocation").getNodeValue();
                type=attrib.getNamedItem("type").getNodeValue();
                if(attrib.getNamedItem("heritability")!=null){
                    if(!attrib.getNamedItem("heritability").getNodeValue().equals("")){
                        herit=Double.parseDouble(attrib.getNamedItem("heritability").getNodeValue());
                    }
                    if(!attrib.getNamedItem("DABG").getNodeValue().equals("")){
                        dabg=Double.parseDouble(attrib.getNamedItem("DABG").getNodeValue());
                    }
                }
                ArrayList<Probe> probes=new ArrayList<Probe>();
                NodeList children=probesetNodes.item(z).getChildNodes();
                for (int x = 0; x < children.getLength(); x++) {
                    if(children.item(x).getNodeName().equals("ProbeList")){
                         NodeList probeNodes=children.item(x).getChildNodes();
                         probes=readProbe(probeNodes);
                     }
                }
                ProbeSet tmp=new ProbeSet(probeStart,probeStop,probeID,seq,strand,type,dabg,herit,locUpdate);
                tmp.setProbes(probes);
                ret.add(tmp);
            }
        }
        //System.out.println("Probeset Array List Size at read:"+ret.size());
        return ret;
    }
    
    private static ArrayList<Probe> readProbe(NodeList probeNodes){
        ArrayList<Probe> ret=new ArrayList<Probe>();
        //System.out.println("Probe Node size:"+probeNodes.getLength());
        for(int z=0;z<probeNodes.getLength();z++){
            if (probeNodes.item(z).getNodeName().equals("Probe")) {
                NamedNodeMap attrib=probeNodes.item(z).getAttributes();
                String probeID= attrib.getNamedItem("ID").getNodeValue();
                long probeStart=-1,probeStop=-1;
                String seq="",strand="",type="";
                probeStart=Integer.parseInt(attrib.getNamedItem("start").getNodeValue());
                probeStop=Integer.parseInt(attrib.getNamedItem("stop").getNodeValue());
                seq=attrib.getNamedItem("sequence").getNodeValue();
                strand=attrib.getNamedItem("strand").getNodeValue();
                Probe tmp=new Probe(probeStart,probeStop,probeID,seq,strand);
                ret.add(tmp);
            }
        }
        //System.out.println("Probe Array List Size at read:"+ret.size());
        return ret;
    }
    
    
    public static FullHeatMapData readHeatMaps(String url) {
        FullHeatMapData fhmd=new FullHeatMapData();
        fhmd.readFromFile(url);
        return fhmd;
    }
    
    public static ProbesetData readProbesetData(String meanURL,String foldDiffURL) throws IOException{
        ProbesetData pd=new ProbesetData();
        try{
            URL urlMean=new URL(meanURL);
            URL urlFoldDiff=new URL(foldDiffURL);
            try{
                URLConnection urlConnM=urlMean.openConnection();
                pd.readMeanHeatMap(urlConnM.getInputStream());
                URLConnection urlConnF=urlFoldDiff.openConnection();
                pd.readFoldDiffHeatMap(foldDiffURL);
            }catch(IOException e){
                throw e;
            }
        
        }catch(MalformedURLException m){
            
        }
       
        return pd;
    }
    
    public static PanelProbesetData readPanelProbesetDataHerit(String heritURL) throws IOException{
        PanelProbesetData pd=new PanelProbesetData();
        try{
            URL url=new URL(heritURL);
            try{
                URLConnection urlConn=url.openConnection();
                pd.readHerit(urlConn.getInputStream());
            }catch(IOException e){
                throw e;
            }
        }catch(MalformedURLException m){
            
        }
        return pd;
    }
    
   public static void readPanelProbesetDataExpr(String exprURL, PanelProbesetData pd) throws IOException{
        if(pd==null){
            pd=new PanelProbesetData();
        }
        try{
            URL url=new URL(exprURL);
            try{
                URLConnection urlConn=url.openConnection();
                pd.readExpr(urlConn.getInputStream());
            }catch(IOException e){
                throw e;
            }
        }catch(MalformedURLException m){
            
        }
    }
   
    public static boolean isTrancriptClusterIDListReady(String trxURL) throws IOException{
        InputStream in;
        try{
            URL url=new URL(trxURL);
            try{
                URLConnection urlConn=url.openConnection();
                in=urlConn.getInputStream();
                in.close();
            }catch(IOException e){
                throw e;
            }
        }catch(MalformedURLException m){
            
        }
        return true;
    }
    
    public static boolean isExCorFileReady(String trxURL) {
        boolean ret=false;
        URLConnection urlConn;
        InputStream in;
        try{
            URL url=new URL(trxURL);
            try{
                urlConn=url.openConnection();
                in=urlConn.getInputStream();
                System.err.println("before ret=true");
                ret=true;
                System.err.println("after ret=true");
                in.close();
            }catch(IOException e){
                ret=false;
                System.err.println("from isExCorFileReady");
                e.printStackTrace(System.err);
            }
        }catch(MalformedURLException m){
            
        }
        return ret;
    }
}
