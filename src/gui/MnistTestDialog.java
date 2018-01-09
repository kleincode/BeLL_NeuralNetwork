package gui;

import de.fk.neuralnetwork.NeuralNetwork;
import de.fk.neuralnetwork.data.ImageContainer;
import de.fk.neuralnetwork.data.Tester;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Felix
 */
public class MnistTestDialog extends javax.swing.JDialog {

    private int numImg = -1, numLbl = -1;
    private String imageFile = "", labelFile = "";
    private NeuralNetwork nn;
    
    /**
     * Creates new form MnistImportDialog
     * @param parent
     * @param modal
     * @param nn
     */
    public MnistTestDialog(java.awt.Frame parent, boolean modal, NeuralNetwork nn) {
        super(parent, modal);
        this.nn = nn;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        btnImageSet = new javax.swing.JButton();
        btnLabelSet = new javax.swing.JButton();
        btnTest = new javax.swing.JButton();
        lblImageSet = new javax.swing.JLabel();
        lblLabelSet = new javax.swing.JLabel();

        fileChooser.setCurrentDirectory(new java.io.File("D:\\Dokumente\\NetBeansProjects\\BeLL_NeuralNetwork"));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aus MNIST importieren");

        btnImageSet.setText("Image Set auswählen");
        btnImageSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageSetActionPerformed(evt);
            }
        });

        btnLabelSet.setText("Label Set auswählen");
        btnLabelSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLabelSetActionPerformed(evt);
            }
        });

        btnTest.setText("Test beginnen");
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestActionPerformed(evt);
            }
        });

        lblImageSet.setText("Kein Image Set ausgewählt");

        lblLabelSet.setText("Kein Label Set ausgewählt");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTest))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnImageSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLabelSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImageSet)
                            .addComponent(lblLabelSet))
                        .addGap(0, 69, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImageSet)
                    .addComponent(lblImageSet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLabelSet)
                    .addComponent(lblLabelSet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(btnTest)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImageSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageSetActionPerformed
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file == null) return;
            imageFile = file.getAbsolutePath();
            try {
                numImg = ImageContainer.validateMnistImageFile(imageFile);
                if(numImg <= 0) {
                    lblImageSet.setText("Keine gültige MNIST Image-Datei.");
                    btnTest.setEnabled(false);
                } else {
                    lblImageSet.setText(numImg + " Datensätze");
                    btnTest.setEnabled(numImg == numLbl);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Es ist ein Fehler beim Öffnen der Datei aufgetreten.");
                Logger.getLogger(MnistTestDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnImageSetActionPerformed

    private void btnLabelSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLabelSetActionPerformed
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file == null) return;
            labelFile = file.getAbsolutePath();
            try {
                numLbl = ImageContainer.validateMnistLabelFile(labelFile);
                if(numLbl <= 0) {
                    lblLabelSet.setText("Keine gültige MNIST Label-Datei.");
                    btnTest.setEnabled(false);
                } else {
                    lblLabelSet.setText(numLbl + " Datensätze");
                    btnTest.setEnabled(numImg == numLbl);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Es ist ein Fehler beim Öffnen der Datei aufgetreten.");
                Logger.getLogger(MnistTestDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnLabelSetActionPerformed

    private void btnTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestActionPerformed
        try {
            Tester.TestResult tr = Tester.testFromMnist(imageFile, labelFile, nn, 100000);
            JOptionPane.showMessageDialog(this, "Fehlerrate: " + tr.getError() + "\nAccuracy: " + tr.getAccuracy(), "Test abgeschlossen", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Es ist ein Fehler beim Einlesen der Dateien aufgetreten.");
            Logger.getLogger(MnistTestDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnTestActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImageSet;
    private javax.swing.JButton btnLabelSet;
    private javax.swing.JButton btnTest;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel lblImageSet;
    private javax.swing.JLabel lblLabelSet;
    // End of variables declaration//GEN-END:variables
}
