/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Screens.Dialogs;

import Actions.UserDBActions;
import Assets.Colors;
import Assets.Messages;
import Components.Atoms.CloseBtn;
import Enums.InputStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static Components.Atoms.CustomBtn.createFlatButton;
import Components.Atoms.CustomTxtBx;
import Screens.ProjectScreen;
import static Components.Atoms.ThemedText.RegularText;
import static Util.Validations.Validations.emptyCheck;

public class AddTicket extends javax.swing.JFrame {
    private Color selectedColor = Colors.secondaryBlue; // Default color

    public AddTicket(int projectID) {
        this.setUndecorated(true);
        this.getContentPane().setBackground(Colors.primaryBlack);
        initComponents();

        this.setSize(277, 327);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));

        JPanel dialogContent = new JPanel();
        dialogContent.setBackground(Colors.primaryBlack);
        dialogContent.setLayout(new BorderLayout(0, 0));
        dialogContent.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        dialogContent.setPreferredSize(new Dimension(277, 327));

        JPanel topBar = new JPanel();
        topBar.setBackground(Colors.primaryBlack);
        topBar.setLayout(new BorderLayout(0, 0));
        topBar.setPreferredSize(new Dimension(277, 20));
        JLabel Title = RegularText("Add Task Ticket", 18);
        topBar.add(Title, BorderLayout.WEST);
        CloseBtn closeBtn = new CloseBtn(false);
        topBar.add(closeBtn, BorderLayout.EAST);
        closeBtn.addActionListener(e -> {
            new ProjectScreen(projectID).setVisible(true);
            dispose();
        });

        JPanel dialogBody = new JPanel();
        dialogBody.setBackground(Colors.transparent);
        dialogBody.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 16));
        dialogBody.setPreferredSize(new Dimension(245, 270));

        JPanel ticketNameInputContainer = new JPanel();
        ticketNameInputContainer.setBackground(Colors.transparent);
        ticketNameInputContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        ticketNameInputContainer.setPreferredSize(new Dimension(245, 48));
        JLabel ticketNameLabel = RegularText("Task", 13);
        CustomTxtBx ticketNameInputObj = new CustomTxtBx("\uf772", 245, 28, InputStatus.REGULAR, false, 128, false);
        JPanel ticketNameInput = ticketNameInputObj.getElement();
        ticketNameInputContainer.add(ticketNameLabel);
        ticketNameInputContainer.add(ticketNameInput);

        JPanel ticketDescInputContainer = new JPanel();
        ticketDescInputContainer.setBackground(Colors.transparent);
        ticketDescInputContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        ticketDescInputContainer.setPreferredSize(new Dimension(245, 48));
        JLabel ticketDescLabel = RegularText("Description (optional)", 13);
        CustomTxtBx ticketDescInputObj = new CustomTxtBx("\uf303", 245, 28, InputStatus.REGULAR, false, 128, false);
        JPanel ticketDescInput = ticketDescInputObj.getElement();
        ticketDescInputContainer.add(ticketDescLabel);
        ticketDescInputContainer.add(ticketDescInput);

        JPanel ticketDeadlineInputContainer = new JPanel();
        ticketDeadlineInputContainer.setBackground(Colors.transparent);
        ticketDeadlineInputContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        ticketDeadlineInputContainer.setPreferredSize(new Dimension(245, 48));
        JLabel ticketDeadlineLabel = RegularText("Duration (days)    ", 13);
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        JSpinner ticketDeadlineInputObj = new JSpinner(model);
        ticketDeadlineInputObj.setEditor(new JSpinner.NumberEditor(ticketDeadlineInputObj, "#"));
        ticketDeadlineInputContainer.add(ticketDeadlineLabel);
        ticketDeadlineInputContainer.add(ticketDeadlineInputObj);



        // Add color chooser
        JPanel colorChooserContainer = new JPanel();
        colorChooserContainer.setBackground(Colors.transparent);
        colorChooserContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        colorChooserContainer.setPreferredSize(new Dimension(245, 48));
        JLabel colorChooserLabel = RegularText("Choose Color", 13);

        JButton chooseColorButton = new JButton("Choose Color");
        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                JDialog colorDialog = JColorChooser.createDialog(null, "Pick a Color", true, colorChooser, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedColor = colorChooser.getColor();
                    }
                }, null);
                colorDialog.setVisible(true);
            }
        });
        JPanel addBtn = createFlatButton("Add Ticket", Colors.primaryBlue, Colors.darkBlack, 14);
        addBtn.setPreferredSize(new Dimension(245, 28));
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (emptyCheck(ticketNameInputObj.getText())) {
                    Messages.fullyCustomError("Task is required.");
                } else {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate addedDate = currentDate.plusDays(((Number) ticketDeadlineInputObj.getValue()).longValue());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String DeadlineDate = addedDate.format(formatter);
                    UserDBActions userDBActions = new UserDBActions();
                    int result = userDBActions.addTicket(ticketNameInputObj.getText(), ticketDescInputObj.getText(), DeadlineDate);
                    if (result > 0) {
                        int linkingResult = userDBActions.linkTicketToProject(projectID, result);
                        if (linkingResult > 0) {
                            Messages.customSuccessMessage("ticket added");
                            new ProjectScreen(projectID).setVisible(true);
                            dispose();
                        } else {
                            Messages.customFailedMessage("add ticket");
                        }
                    } else {
                        Messages.customFailedMessage("add ticket");
                    }
                }
            }
        });

        colorChooserContainer.add(colorChooserLabel);
        colorChooserContainer.add(chooseColorButton);


        // 将 dialogBody 的布局设置为 BoxLayout，使组件垂直排列
        dialogBody.setLayout(new BoxLayout(dialogBody, BoxLayout.Y_AXIS));

// 设置每个输入框和按钮的 preferredSize
        ticketNameInputContainer.setPreferredSize(new Dimension(245, 48));
        ticketDescInputContainer.setPreferredSize(new Dimension(245, 48));
        ticketDeadlineInputContainer.setPreferredSize(new Dimension(245, 48));
        colorChooserContainer.setPreferredSize(new Dimension(245, 48));
        addBtn.setPreferredSize(new Dimension(245, 28));

        dialogBody.add(ticketNameInputContainer);
        dialogBody.add(ticketDescInputContainer);
        dialogBody.add(ticketDeadlineInputContainer);
        dialogBody.add(colorChooserContainer);
        dialogBody.add(addBtn);

        dialogContent.add(topBar, BorderLayout.NORTH);
        dialogContent.add(dialogBody, BorderLayout.SOUTH);
        this.add(dialogContent, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(AddTicket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddTicket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddTicket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddTicket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddTicket(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
