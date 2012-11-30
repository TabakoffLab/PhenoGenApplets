/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExonCorrelationView.java
 *
 * Created on Dec 6, 2011, 2:26:19 PM
 */
package edu.ucdenver.ccp.phenogen.applets.exonviewer;

import edu.ucdenver.ccp.phenogen.applets.data.TranscriptElement;
import edu.ucdenver.ccp.phenogen.applets.data.ExCorHeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.Transcript;
import edu.ucdenver.ccp.phenogen.applets.data.ExCorFullHeatMapData;
import edu.ucdenver.ccp.phenogen.applets.data.Gene;
import edu.ucdenver.ccp.phenogen.applets.data.ProbeSet;
import edu.ucdenver.ccp.phenogen.applets.graphics.ExCorHeatMapLegend;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author smahaffey
 */
public class ExonCorrelationView extends javax.swing.JApplet {
    String imgURLbase="";
    boolean initComplete=false;

    private String getAnnotationString() {
        String ret="";
        if(annotationcb.getSelectedIndex()==0){
            ret="core";
        }else if(annotationcb.getSelectedIndex()==1){
            ret="extended";
        }else if(annotationcb.getSelectedIndex()==2){
            ret="full";
        }
        return ret;
    }

    

    /** Initializes the applet ExonCorrelationView */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(System.err);
            java.util.logging.Logger.getLogger(ExonCorrelationView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            ex.printStackTrace(System.err);
            java.util.logging.Logger.getLogger(ExonCorrelationView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace(System.err);
            java.util.logging.Logger.getLogger(ExonCorrelationView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
            java.util.logging.Logger.getLogger(ExonCorrelationView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        
        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    //System.out.println("INIT START");
                    initComponents();
                    
                }
                
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        /*final Thread t = new Thread( new Runnable() {
            public void run() {
                System.out.println("Starting DOWNLOAD");
                FullHeatMapData fhmd=ReadDataFiles.readHeatMaps(heatfile);
                Gene[] genes=ReadDataFiles.readGenes(genefile);
                System.out.println("END DOWNLOAD");
                try {
                    this.wait(15000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                setup(fhmd, genes);
                System.out.println("END DOWNLOAD AND GUI UPDATE");
            }
        } );
        
        t.start();*/
        /*while(!initComplete){
            System.out.println("WAITING...");
            try {
                dt.wait(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExonCorrelationView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(dt.getFHMD()!=null&&dt.getGenes()!=null){
            this.setup(dt.getFHMD(),dt.getGenes());
        }*/

        
       //heatMapGraphicsPanel1.setCurrentSize(jScrollPane1.getViewport().getHeight(), jScrollPane1.getViewport().getWidth());
        
    }

    @Override
    public void start() {
        //super.start();
        genefile = getParameter("gene_data");
        heatfile = getParameter("heatmap_data");
        DownloadThread dt=new DownloadThread(this);
        dt.setGeneFile(genefile);
        dt.setHeatFile(heatfile);
        dt.start();
        linkSBcb.setVisible(false);
        jPanel7.revalidate();
        //jSplitPane1.setDividerLocation(0);
        initComplete=true;
        /*System.out.println("INIT STOP");
        while(dt.getFHMD()==null||dt.getGenes()==null){
            System.out.println("waiting");
        }
        this.setup(dt.getFHMD(), dt.getGenes());*/
   }
    
    
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        geneListcb = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        leftTranscriptcb = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        rightTranscriptcb = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        annotationcb = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        herittxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        dabgtxt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        dabgNAcb = new javax.swing.JCheckBox();
        introncb = new javax.swing.JCheckBox();
        opStrandcb = new javax.swing.JCheckBox();
        linkSBcb = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        heritNAcb = new javax.swing.JCheckBox();
        mecp = new edu.ucdenver.ccp.phenogen.applets.exonviewer.MainExonCorPanel();

        setMinimumSize(new java.awt.Dimension(960, 800));
        setPreferredSize(new java.awt.Dimension(960, 1200));
        setSize(new java.awt.Dimension(960, 1200));

        jPanel3.setPreferredSize(new java.awt.Dimension(893, 133));
        jPanel3.setLayout(new java.awt.GridLayout(1, 3));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gene/Transcript"));
        jPanel1.setLayout(new java.awt.GridLayout(3, 1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Gene:");
        jLabel4.setToolTipText("Select the gene to display");

        geneListcb.setToolTipText("Select the gene to display");
        geneListcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geneListcbActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(geneListcb, 0, 427, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(geneListcb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(260, 43));

        leftTranscriptcb.setToolTipText("Select the transcript displayed.  Selects left transcript if two transcripts are displayed.");
        leftTranscriptcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftTranscriptcbActionPerformed(evt);
            }
        });

        jLabel1.setText("Transcript(Left)");
        jLabel1.setToolTipText("Select the transcript displayed.  Selects left transcript if two transcripts are displayed.");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jLabel1)
                .add(18, 18, 18)
                .add(leftTranscriptcb, 0, 354, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(leftTranscriptcb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9))
        );

        jPanel1.add(jPanel6);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(260, 43));

        jLabel2.setText("Transcript(Right)");
        jLabel2.setToolTipText("Selects the trascript to display on the right.  If none is selected only the selected left transcript is displayed.");

        rightTranscriptcb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        rightTranscriptcb.setToolTipText("Selects the trascript to display on the right.  If none is selected only the selected left transcript is displayed.");
        rightTranscriptcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightTranscriptcbActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rightTranscriptcb, 0, 357, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(rightTranscriptcb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel2))
        );

        jPanel1.add(jPanel5);

        jPanel3.add(jPanel1);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(2090, 30));

        jButton3.setText("Filter");
        jButton3.setToolTipText("Filter probesets displayed based on the Annontation, Heritability, and Detection Above Background selected above.");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Annotation Confidence:");

        annotationcb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Core", "Extended", "Full" }));
        annotationcb.setToolTipText("Affymetrix Annotation for probeset.  Core has highest confidence to full with the lowest confidence.");

        jLabel5.setText("Probesets in % Samples DABG: >");

        jLabel6.setText("Heritability: >");

        herittxt.setText("0.33");

        jLabel7.setText("%");

        dabgtxt.setColumns(3);
        dabgtxt.setText("1");
        dabgtxt.setToolTipText("% of sample where probeset was detected above background.");
        dabgtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dabgtxtActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(annotationcb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(herittxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dabgtxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel7)))
                .addContainerGap(30, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(jButton3))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(annotationcb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6)
                    .add(herittxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(dabgtxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel7))
                        .add(29, 29, 29))
                    .add(jButton3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Filters", jPanel7);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        dabgNAcb.setText("Ignore DABG Filter");
        dabgNAcb.setToolTipText("<html>Display probesets regardless of DABG value.<BR>\nTry this for a probeset of interest, that is not being displayed due<BR>\nto DABG values.</html>");
        dabgNAcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dabgNAcbActionPerformed(evt);
            }
        });

        introncb.setText("Display Intron Probesets");
        introncb.setToolTipText("<html>Display additional probesets that do not fall within an annotated exon. <BR>\n These are still filtered based on the selected filtering criteria.</html>");

        opStrandcb.setText("Display Rev. Strand Probesets");
        opStrandcb.setToolTipText("<html>Display probesets on the strand opposite the gene.<BR>\nBe sure to note the arrows above the heatmap columns denoting <BR>\na reverse strand probeset.</html>");
        opStrandcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opStrandcbActionPerformed(evt);
            }
        });

        linkSBcb.setSelected(true);
        linkSBcb.setText("Link Scrollbars");
        linkSBcb.setToolTipText("<html>If two transcripts are displayed it links the scroll bars so similar <BR>\nsections should be visible.  If transcripts vary greatly in length you may <BR>\nwant/need to disable this.</html>");
        linkSBcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkSBcbActionPerformed(evt);
            }
        });

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        heritNAcb.setText("Ignore Herit Filter");
        heritNAcb.setToolTipText("<html>Display probesets regardless of heritability value.<BR>\nTry this for a probeset of interest, that is not being displayed due<BR>\nto heritability values.</html>");
        heritNAcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heritNAcbActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(dabgNAcb)
                                .add(18, 18, 18)
                                .add(heritNAcb))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(introncb)
                                .add(18, 18, 18)
                                .add(opStrandcb)))
                        .addContainerGap(31, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(linkSBcb)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton1))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(opStrandcb)
                    .add(introncb))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dabgNAcb)
                    .add(heritNAcb))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(linkSBcb)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(jButton1))
        );

        jTabbedPane1.addTab("Adv. Display Options", jPanel2);

        jPanel3.add(jTabbedPane1);

        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);
        getContentPane().add(mecp, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    
    
    
    private void rightTranscriptcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightTranscriptcbActionPerformed
        if (rightTranscriptcb.getSelectedIndex() > 0) {
            this.linkSBcb.setVisible(true);
        }else{
            this.linkSBcb.setVisible(false);
        }
        mecp.updateRightMap(rightTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
    }//GEN-LAST:event_rightTranscriptcbActionPerformed
    
    
    
    
    private void linkSBcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkSBcbActionPerformed
        linkSB=linkSBcb.isSelected();
        mecp.setLinkSB(linkSB);
    }//GEN-LAST:event_linkSBcbActionPerformed

    private void leftTranscriptcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftTranscriptcbActionPerformed
        //update Transcript
         mecp.updateLeftMap(leftTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
    }//GEN-LAST:event_leftTranscriptcbActionPerformed

    private void geneListcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_geneListcbActionPerformed
        Gene tmp=(Gene)geneListcb.getSelectedItem();
        selGene=tmp;
        //mecp.setSelectedGene(selGene);
        transcripts=tmp.getTranscripts();
        //mecp.setTranscripts(transcripts);
        leftTranscriptcb.removeAllItems();
        rightTranscriptcb.removeAllItems();
        rightTranscriptcb.addItem("None");
        for (int i = 0; i < transcripts.size(); i++) {
            leftTranscriptcb.addItem(transcripts.get(i).getID());
            rightTranscriptcb.addItem(transcripts.get(i).getID());
        }
        mecp.updateLeftMap(leftTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
        mecp.updateRightMap(rightTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
        String curImgURL=imgURLbase+"/"+tmp.getGeneID()+".png";
        String genelbltxt=tmp.getGeneID()+"("+tmp.getGeneSymbol()+")";
        if(!curImgURL.equals("")){
            //jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(genelbltxt));
            //genelbl.setText(genelbltxt);
            mecp.setURL(curImgURL,genelbltxt);
        }
        
        /*ArrayList<ProbeSet> tmpProbeList=transcripts.get(0).getProbeSetList(getAnnotationString(),dabgtxt.getText(),herittxt.getText());
        this.leftHeatMapPanel.setHeatMapData(fhmd.generateMap(tmpProbeList));
        transcripts.get(0).setExcluded(leftHeatMapPanel.getHmd().getExcludedProbes());
        leftColumnTrans.setTranscript(transcripts.get(0));
        leftRowTrans.setTranscript(transcripts.get(0));
        leftColumnTrans.repaint();
        leftRowTrans.repaint();
        this.leftColumnTrans.revalidate();
        this.leftRowTrans.revalidate();*/
        
    }//GEN-LAST:event_geneListcbActionPerformed

    private void dabgtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dabgtxtActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_dabgtxtActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        mecp.filter(leftTranscriptcb.getSelectedIndex(),rightTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void opStrandcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opStrandcbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_opStrandcbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mecp.filter(leftTranscriptcb.getSelectedIndex(),rightTranscriptcb.getSelectedIndex(),getAnnotationString(),dabgtxt.getText(),herittxt.getText(),opStrandcb.isSelected(),dabgNAcb.isSelected(),heritNAcb.isSelected(),introncb.isSelected());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dabgNAcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dabgNAcbActionPerformed
        dabgtxt.setEnabled(!dabgNAcb.isSelected());
        if(dabgNAcb.isSelected()){
            dabgtxt.setToolTipText("Disabled. De-select Adv. Display Options -> Ignore DABG Filter Checkbox to enable.");
        }else{
            dabgtxt.setToolTipText("% of sample where probeset was detected above background.");
        }
    }//GEN-LAST:event_dabgNAcbActionPerformed

    private void heritNAcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heritNAcbActionPerformed
        herittxt.setEnabled(!heritNAcb.isSelected());
        if(heritNAcb.isSelected()){
            dabgtxt.setToolTipText("Disabled. De-select Adv. Display Options -> Ignore Heritability Filter Checkbox to enable.");
        }else{
            dabgtxt.setToolTipText("");
        }
    }//GEN-LAST:event_heritNAcbActionPerformed

    

    void setup(ExCorFullHeatMapData fhmdIn, Gene[] genesIn, boolean outOfMem){
        String main_ensembl_id = getParameter("main_ensembl_id");
        String imageURLBase=genefile.substring(0, genefile.lastIndexOf("/"));
        int mainGeneIndex = 0;
        fhmd = fhmdIn;//ReadDataFiles.readHeatMaps(heatfile);
        genes = genesIn;
        if(genes!=null&& genes.length > 0){
            for (int i = 0; i < genes.length; i++) {
                genes[i].numberExons(fhmd);
                geneListcb.addItem(genes[i]);
                if (genes[i].getGeneID().equals(main_ensembl_id)) {
                    mainGeneIndex = i;
                }
            }
            geneListcb.setSelectedIndex(mainGeneIndex);
            transcripts = genes[mainGeneIndex].getTranscripts();
            selGene = genes[mainGeneIndex];
            mecp.setSelectedGene(selGene);
            mecp.setTranscripts(transcripts);
            mecp.setup(fhmd,genes,outOfMem,main_ensembl_id,imageURLBase,mainGeneIndex);
        }
        mecp.hideError();
    }

    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox annotationcb;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox dabgNAcb;
    private javax.swing.JTextField dabgtxt;
    private javax.swing.JComboBox geneListcb;
    private javax.swing.JCheckBox heritNAcb;
    private javax.swing.JTextField herittxt;
    private javax.swing.JCheckBox introncb;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox leftTranscriptcb;
    private javax.swing.JCheckBox linkSBcb;
    private edu.ucdenver.ccp.phenogen.applets.exonviewer.MainExonCorPanel mecp;
    private javax.swing.JCheckBox opStrandcb;
    private javax.swing.JComboBox rightTranscriptcb;
    // End of variables declaration//GEN-END:variables
    
    boolean linkSB=true;
    private ArrayList<Transcript> transcripts=new ArrayList<Transcript>();
    private Gene[] genes=new Gene[0];
    private Gene selGene=null;
    private ExCorFullHeatMapData fhmd=new ExCorFullHeatMapData();
    String genefile="",heatfile="";
    
    //private ExonInfoDialog eid=new ExonInfoDialog(false);
    

}
