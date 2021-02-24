/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author evilc
 */
public class GUI extends javax.swing.JFrame {
    Vector<Process> allProcesses = new Vector<Process>(5);
    PriorityQueue<Process> pqc_temp;
    int CPU;
    String execStatus;
    double timeRemaining;
    boolean alreadyStarted = false;
    boolean paused = false;
    int pollRateVal = 0;
    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        StatusPane1.setText("CPU: " + CPU + "\nexec: " + execStatus + "\ntime remaining: " + timeRemaining);
        StatusPane2.setText("System Log:\n\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StartButton = new javax.swing.JButton();
        PauseButton = new javax.swing.JButton();
        SystemStatus = new javax.swing.JLabel();
        pollRateInput = new javax.swing.JTextField("1000");
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        StatusPane1 = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        StatusPane2 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        StartButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        StartButton.setText("Start System");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        PauseButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        PauseButton.setText("Pause System");
        PauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PauseButtonActionPerformed(evt);
            }
        });

        SystemStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        SystemStatus.setText("System Status");

        pollRateInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pollRateInputActionPerformed(evt);
            }
        });
        jLabel3.setText("1 time unit =");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Waiting Process Queue");

        Table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Process Name", "Service Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Table1.setRowHeight(30);
        Table1.getTableHeader().setReorderingAllowed(false);
        Table1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                Table1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(Table1);

        StatusPane1.setEditable(false);
        jScrollPane2.setViewportView(StatusPane1);

        jLabel4.setText("ms");

        StatusPane2.setEditable(false);
        jScrollPane3.setViewportView(StatusPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(74, 74, 74)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pollRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(StartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SystemStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SystemStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(pollRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void set_pqc(PriorityQueue<Process> input)
    {
        pqc_temp = new PriorityQueue<>(input);
        //System.out.println("TEST");
    }
    
    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
        // TODO add your handling code here:
        //String temp = allProcesses.get(0).getID();
        StartButton.setEnabled(false);
        PauseButton.setEnabled(true);

        
        if (alreadyStarted == false)
        {
            pollRateVal = Integer.parseInt(pollRateInput.getText());
            //allProcesses.addElement(pqc_temp.poll());
            //allProcesses.addElement(pqc_temp.poll());
            //allProcesses.addElement(pqc_temp.poll());
            DefaultTableModel model = (DefaultTableModel) Table1.getModel();

            int pqc_temp_size = pqc_temp.size();

            for (int i = 0; i < pqc_temp_size; i++)
            {
                Process nextProcess = pqc_temp.poll();
                String processID = nextProcess.getID();
                String serviceTime = "" + nextProcess.getSerTime();

                if (i < 7) // Initially, the table has 7 rows
                {
                    model.setValueAt(processID, i, 0);
                    model.setValueAt(serviceTime, i, 1);
                }
                else
                {
                    Object[] rowInput = {processID, serviceTime};
                    model.addRow(rowInput);
                }

            }
            alreadyStarted = true;
        }
        else
        {
            paused = false;
        }
       
        
    }//GEN-LAST:event_StartButtonActionPerformed

    private void PauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PauseButtonActionPerformed
        // TODO add your handling code here:
        paused = true;
        PauseButton.setEnabled(false);
        StartButton.setEnabled(true);
    }//GEN-LAST:event_PauseButtonActionPerformed

    private void Table1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_Table1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1AncestorAdded

    private void pollRateInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pollRateInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pollRateInputActionPerformed

    public int getPollRateVal() {
        return pollRateVal;
    }

    public boolean getPauseState(){
        return paused;
    }

    public void removeProcessFromTable()
    {
        int numRows = Table1.getRowCount();

        for (int i = 1; i < numRows; i++)
        {
            if (Table1.getValueAt(0, 0).toString().equals(""))
            {
                break; // no more values in the table
            }
            else if (Table1.getValueAt(i, 0).toString().equals(""))
            {
                Table1.setValueAt("", i - 1, 0);
                Table1.setValueAt("", i - 1, 1);
                break; // This row has no values; nothing to shift up
            }
            else
            {
                // Shift the process's data up one row
                Table1.setValueAt(Table1.getValueAt(i, 0), i - 1, 0);
                Table1.setValueAt(Table1.getValueAt(i, 1), i - 1, 1);
                Table1.setValueAt("", i, 0);
                Table1.setValueAt("", i, 1);
            }

        }
    }

    public void updateSystemStats(String processID, int serviceTime, double throughput)
    {
        StyledDocument document = (StyledDocument) StatusPane2.getDocument();

        try {
            document.insertString(document.getLength(), processID + " is complete and took " + serviceTime + " seconds.\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        try {
            document.insertString(document.getLength(), "Current throughput is " + String.format("%.2f", throughput) + " processes completed per time unit.\n\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void updateCPUStats(String processID, int CPU, double timeRemaining)
    {
        StyledDocument document = (StyledDocument) StatusPane1.getDocument();
        // Cleaning Document
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        // Updating new information
        try {
            document.insertString(document.getLength(), "CPU: " + CPU + "\nexec: " + processID + "\ntime remaining: " + String.format("%4.0f", timeRemaining) + " ms", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PauseButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JTextPane StatusPane1;
    private javax.swing.JTextPane StatusPane2;
    private javax.swing.JLabel SystemStatus;
    private javax.swing.JTable Table1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField pollRateInput;
    // End of variables declaration//GEN-END:variables
}
