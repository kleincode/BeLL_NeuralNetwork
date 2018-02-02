package gui;

import de.fk.neuralnetwork.Main;
import de.fk.neuralnetwork.NeuralNetwork;
import de.fk.neuralnetwork.data.ImageContainer;
import de.fk.neuralnetwork.learning.Backpropagator;
import de.fk.neuralnetwork.training.LabeledImageTrainingSupplier;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Felix
 */
public class MainFrame extends javax.swing.JFrame {

    private TrainingExamplesFrame teframe = null;
    private DrawFrame dframe = null;
    private NeuralNetwork nn;
    private Backpropagator bp = null;
    private int threadCount = Runtime.getRuntime().availableProcessors(), batchSize = 3000;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        createNeuralNet();
        initComponents();
        bp = new Backpropagator(nn, 0.01, 0, 0);
        bp.setLearningRateUpdated(this::updateLearningRate);
        lblLearningRate.setText(bp.getLearningRate() + "");
        tfLearningRate.setText(bp.getLearningRate() + "");
        spExamplesPerThread.setValue(batchSize / threadCount);
        batchSize = ((int) spExamplesPerThread.getValue()) * threadCount;
        spBatchSize.setValue(batchSize);
    }
    
    private void createNeuralNet() {
        if(bp != null) bp.stopTraining();
        //nn = new NeuralNetwork(1, 300, 28*28, 10);
        nn = new NeuralNetwork(28 * 28, 10, new int[]{300, 100});
        if(bp != null) bp.setNet(nn);
    }
    
    public final void updateLearningRate() {
        lblLearningRate.setText(bp.getLearningRate() + "");
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
        panelTraining = new javax.swing.JPanel();
        slThreadCount = new javax.swing.JSlider();
        lblThreadCount = new javax.swing.JLabel();
        lblThreadCount1 = new javax.swing.JLabel();
        lblBatchSize = new javax.swing.JLabel();
        spBatchSize = new javax.swing.JSpinner();
        lblExamplesPerThread = new javax.swing.JLabel();
        spExamplesPerThread = new javax.swing.JSpinner();
        tfLearningRate = new javax.swing.JTextField();
        btnUpdateLearningRate = new javax.swing.JButton();
        lblLearningRate = new javax.swing.JLabel();
        panelActions = new javax.swing.JPanel();
        btnTrain = new javax.swing.JButton();
        btnStopTraining = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        mbNetwork = new javax.swing.JMenu();
        miOpen = new javax.swing.JMenuItem();
        miSave = new javax.swing.JMenuItem();
        miReset = new javax.swing.JMenuItem();
        mbTraining = new javax.swing.JMenu();
        miTrainingExamples = new javax.swing.JMenuItem();
        mbTest = new javax.swing.JMenu();
        mbTestMNIST = new javax.swing.JMenuItem();
        mbTestDraw = new javax.swing.JMenuItem();

        fileChooser.setCurrentDirectory(new java.io.File("D:\\Dokumente\\NetBeansProjects\\BeLL_NeuralNetwork"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Neuronales Netz zur Handschrifterkennung");

        panelTraining.setBorder(javax.swing.BorderFactory.createTitledBorder("Training"));

        slThreadCount.setMajorTickSpacing(1);
        slThreadCount.setMaximum(Runtime.getRuntime().availableProcessors() * 2);
        slThreadCount.setMinimum(1);
        slThreadCount.setMinorTickSpacing(1);
        slThreadCount.setPaintLabels(true);
        slThreadCount.setPaintTicks(true);
        slThreadCount.setSnapToTicks(true);
        slThreadCount.setValue(threadCount);
        slThreadCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slThreadCountStateChanged(evt);
            }
        });

        lblThreadCount.setText("Threads");

        lblThreadCount1.setText("Lernrate");

        lblBatchSize.setText("Batch Size");

        spBatchSize.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
        spBatchSize.setEnabled(false);

        lblExamplesPerThread.setText("Beispiele pro Thread");

        spExamplesPerThread.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spExamplesPerThread.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spExamplesPerThreadStateChanged(evt);
            }
        });

        tfLearningRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfLearningRate.setText("0.01");

        btnUpdateLearningRate.setText("Update");
        btnUpdateLearningRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateLearningRateActionPerformed(evt);
            }
        });

        lblLearningRate.setText("0.01");

        javax.swing.GroupLayout panelTrainingLayout = new javax.swing.GroupLayout(panelTraining);
        panelTraining.setLayout(panelTrainingLayout);
        panelTrainingLayout.setHorizontalGroup(
            panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(slThreadCount, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblThreadCount)
                        .addComponent(lblThreadCount1)
                        .addGroup(panelTrainingLayout.createSequentialGroup()
                            .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(tfLearningRate, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblBatchSize, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(spBatchSize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                            .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelTrainingLayout.createSequentialGroup()
                                    .addGap(46, 46, 46)
                                    .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spExamplesPerThread, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblExamplesPerThread))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(panelTrainingLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUpdateLearningRate)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblLearningRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        panelTrainingLayout.setVerticalGroup(
            panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrainingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThreadCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slThreadCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblThreadCount1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLearningRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateLearningRate)
                    .addComponent(lblLearningRate))
                .addGap(12, 12, 12)
                .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatchSize)
                    .addComponent(lblExamplesPerThread))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTrainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spBatchSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spExamplesPerThread, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        panelActions.setBorder(javax.swing.BorderFactory.createTitledBorder("Aktionen"));

        btnTrain.setText("Training beginnen");
        btnTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrainActionPerformed(evt);
            }
        });

        btnStopTraining.setText("Training stoppen");
        btnStopTraining.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopTrainingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelActionsLayout = new javax.swing.GroupLayout(panelActions);
        panelActions.setLayout(panelActionsLayout);
        panelActionsLayout.setHorizontalGroup(
            panelActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTrain)
                    .addComponent(btnStopTraining, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelActionsLayout.setVerticalGroup(
            panelActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTrain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStopTraining)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        mbNetwork.setText("Netz");

        miOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        miOpen.setText("Öffnen");
        miOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenActionPerformed(evt);
            }
        });
        mbNetwork.add(miOpen);

        miSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        miSave.setText("Speichern");
        miSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSaveActionPerformed(evt);
            }
        });
        mbNetwork.add(miSave);

        miReset.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        miReset.setText("Zurücksetzen");
        miReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miResetActionPerformed(evt);
            }
        });
        mbNetwork.add(miReset);

        menuBar.add(mbNetwork);

        mbTraining.setText("Trainieren");

        miTrainingExamples.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        miTrainingExamples.setText("Trainingsbeispiele");
        miTrainingExamples.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTrainingExamplesActionPerformed(evt);
            }
        });
        mbTraining.add(miTrainingExamples);

        menuBar.add(mbTraining);

        mbTest.setText("Testen");

        mbTestMNIST.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mbTestMNIST.setText("Mit MNIST testen");
        mbTestMNIST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbTestMNISTActionPerformed(evt);
            }
        });
        mbTest.add(mbTestMNIST);

        mbTestDraw.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        mbTestDraw.setText("Zeichenfeld");
        mbTestDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbTestDrawActionPerformed(evt);
            }
        });
        mbTest.add(mbTestDraw);

        menuBar.add(mbTest);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelTraining, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTraining, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miTrainingExamplesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTrainingExamplesActionPerformed
        if(teframe == null || !teframe.isDisplayable()) teframe = new TrainingExamplesFrame();
        teframe.setVisible(true);
    }//GEN-LAST:event_miTrainingExamplesActionPerformed

    private void btnTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrainActionPerformed
        LabeledImageTrainingSupplier trainingSupplier = new LabeledImageTrainingSupplier(ImageContainer.getImages(), 28, 28, 10);
        
        FileOutputStream fos = null;
        try {
            //Logging
            fos = new FileOutputStream("errorlog.txt", true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //bp.train(trainingSupplier, 500, fos, 1);
        bp.trainParallel(trainingSupplier, 5000, fos, threadCount, batchSize / threadCount, false);
    }//GEN-LAST:event_btnTrainActionPerformed

    private void btnStopTrainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopTrainingActionPerformed
        if(bp != null) bp.stopTraining();
    }//GEN-LAST:event_btnStopTrainingActionPerformed

    private void miSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSaveActionPerformed
        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                nn.sout();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()));
                oos.writeObject(nn);
                oos.close();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_miSaveActionPerformed

    private void miOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenActionPerformed
        if(bp != null) bp.stopTraining();
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()));
                nn = (NeuralNetwork) ois.readObject();
                ois.close();
                if(bp != null) bp.setNet(nn);
            } catch (IOException | NullPointerException | ClassNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_miOpenActionPerformed

    private void mbTestMNISTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbTestMNISTActionPerformed
        new MnistTestDialog(this, true, nn).setVisible(true);
    }//GEN-LAST:event_mbTestMNISTActionPerformed

    private void mbTestDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbTestDrawActionPerformed
        if(dframe == null || !dframe.isDisplayable()) dframe = new DrawFrame(nn);
        dframe.setVisible(true);
    }//GEN-LAST:event_mbTestDrawActionPerformed

    private void miResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miResetActionPerformed
        createNeuralNet();
    }//GEN-LAST:event_miResetActionPerformed

    private void slThreadCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slThreadCountStateChanged
        threadCount = slThreadCount.getValue();
        spExamplesPerThread.setValue(batchSize / threadCount);
        batchSize = (int) spExamplesPerThread.getValue() * threadCount;
        spBatchSize.setValue(batchSize);
    }//GEN-LAST:event_slThreadCountStateChanged

    private void spExamplesPerThreadStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spExamplesPerThreadStateChanged
        batchSize = ((int) spExamplesPerThread.getValue()) * threadCount;
        spBatchSize.setValue(batchSize);
    }//GEN-LAST:event_spExamplesPerThreadStateChanged

    private void btnUpdateLearningRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateLearningRateActionPerformed
        try {
            bp.setLearningRate(Double.parseDouble(tfLearningRate.getText().replace(",", ".")));
            updateLearningRate();
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Bitte gültige Gleitkommazahl eingeben!", "Ungültige Lernrate", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateLearningRateActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStopTraining;
    private javax.swing.JButton btnTrain;
    private javax.swing.JButton btnUpdateLearningRate;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel lblBatchSize;
    private javax.swing.JLabel lblExamplesPerThread;
    private javax.swing.JLabel lblLearningRate;
    private javax.swing.JLabel lblThreadCount;
    private javax.swing.JLabel lblThreadCount1;
    private javax.swing.JMenu mbNetwork;
    private javax.swing.JMenu mbTest;
    private javax.swing.JMenuItem mbTestDraw;
    private javax.swing.JMenuItem mbTestMNIST;
    private javax.swing.JMenu mbTraining;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem miOpen;
    private javax.swing.JMenuItem miReset;
    private javax.swing.JMenuItem miSave;
    private javax.swing.JMenuItem miTrainingExamples;
    private javax.swing.JPanel panelActions;
    private javax.swing.JPanel panelTraining;
    private javax.swing.JSlider slThreadCount;
    private javax.swing.JSpinner spBatchSize;
    private javax.swing.JSpinner spExamplesPerThread;
    private javax.swing.JTextField tfLearningRate;
    // End of variables declaration//GEN-END:variables
}
