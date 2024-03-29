/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProbePanel.java
 *
 * Created on Jan 11, 2012, 9:26:04 AM
 */
package edu.ucdenver.ccp.phenogen.applets.exonviewer;

import edu.ucdenver.ccp.phenogen.applets.data.Probe;

/**
 *
 * @author smahaffey
 */
public class ProbePanel extends javax.swing.JPanel {
    /** Creates new form ProbePanel */
    public ProbePanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IDlbl = new javax.swing.JLabel();
        Startlbl = new javax.swing.JLabel();
        Stoplbl = new javax.swing.JLabel();
        Strandlbl = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Sequencetxt = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 255, 255));

        IDlbl.setText("Probe ID:");

        Startlbl.setText("Start:");

        Stoplbl.setText("Stop:");

        Strandlbl.setText("Strand:");

        jLabel6.setText("Sequence:");

        Sequencetxt.setColumns(20);
        Sequencetxt.setRows(5);
        jScrollPane1.setViewportView(Sequencetxt);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(Startlbl)
                            .add(IDlbl))
                        .add(96, 96, 96)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(Strandlbl)
                            .add(Stoplbl)))
                    .add(jLabel6))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(IDlbl)
                    .add(Strandlbl))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(Startlbl)
                    .add(Stoplbl))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IDlbl;
    private javax.swing.JTextArea Sequencetxt;
    private javax.swing.JLabel Startlbl;
    private javax.swing.JLabel Stoplbl;
    private javax.swing.JLabel Strandlbl;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    void setProbe(Probe p) {
        if(p!=null){
            IDlbl.setText("Probe ID:  "+p.getProbeID());
            Sequencetxt.setText(p.getSequence());
            Startlbl.setText("Starting bp:  "+p.getStart());
            Stoplbl.setText("Ending bp:  "+p.getStop());
            Strandlbl.setText("Strand:  "+p.getStrand());
        }else{
            IDlbl.setText("Probe ID:");
            Sequencetxt.setText("");
            Startlbl.setText("Start:");
            Stoplbl.setText("Stop:");
            Strandlbl.setText("Strand:");
        }
    }
}
