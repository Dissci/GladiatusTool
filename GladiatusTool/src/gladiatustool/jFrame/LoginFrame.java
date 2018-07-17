/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.jFrame;

import gladiatustool.configuration.DriverConfiguration;
import gladiatustool.configuration.UserConfiguration;
import gladiatustool.core.Core;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Tomáš
 */
public class LoginFrame extends javax.swing.JFrame {

    /**
     * Creates new form LoginFrame
     */
    private final JFileChooser filechooser = new JFileChooser();
    private final FileFilter filter = new FileNameExtensionFilter("EXE File", "exe");
    private final UserConfiguration userConfiguration = new UserConfiguration();
    private final DriverConfiguration driverConfiguration = new DriverConfiguration();
    private Core core;

    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        this.setTitle("Gladiatus Tool");
        //loadImageIcon();
        init();
        initServerList();
    }

    private void loadImageIcon() {
        ImageIcon img = new ImageIcon("img/icon.jpg");
        this.setIconImage(img.getImage());
    }
    
    private void init() {
        userName.setText(userConfiguration.getUser());
        password.setText(userConfiguration.getPassword());
        expeditions.setSelected(userConfiguration.isExpeditions());
        dungeons.setSelected(userConfiguration.isDungeons());
        browserPath.setText(driverConfiguration.getWebDriver());
    }

    private void initServerList() {
        serverList.removeAllItems();
        List<String> servers = driverConfiguration.loadServersFromURL();
        for (String server : servers) {
            serverList.addItem(server);
        }

        String server = userConfiguration.getServer();
        if (!server.isEmpty()) {
            serverList.setSelectedItem(server);
        }
    }

    public int getServerIndex() {
        return serverList.getSelectedIndex();
    }

    private void saveConfig() throws IOException {
        userConfiguration.setUserConfig(userName.getText(), password.getText(), serverList.getSelectedItem().toString(), expeditions.isSelected(), dungeons.isSelected());
        if (checkBrowserPath()) {
            driverConfiguration.setDriverConfig(browserPath.getText(), "sk");
        }
    }

    private int getExpeditionEnemy() {
        if (jRadioButton2.isSelected()) {
            return 0;
        } else if (jRadioButton3.isSelected()) {
            return 1;
        } else if (jRadioButton4.isSelected()) {
            return 2;
        } else {
            return 3;
        }
    }

    private int getDungeonMode() {
        if (jRadioButton6.isSelected()) {
            return 1;
        } else {
            return 2;
        }
    }

    private Long getLag() {
        return Long.getLong(lagger.getValue().toString(), 10) * 60000;
    }

    private void start() {
        if (checkBrowserPath()) {
            Thread thread = new Thread(new Core(driverConfiguration.getLANG() + driverConfiguration.getURL(),
                    driverConfiguration.isIsChrome(), userConfiguration, getServerIndex(),
                    getExpeditionEnemy(), getDungeonMode(), getLag()));
            thread.start();
        }
    }

    private String getPath() {
        filechooser.setFileFilter(filter);
        filechooser.showOpenDialog(this);
        driverConfiguration.setWebDriver(filechooser.getSelectedFile().toPath().toString());
        return driverConfiguration.getWebDriver();
    }

    private boolean checkBrowserPath() {
        if (driverConfiguration.getWebDriver() == null || browserPath.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have to set path to your browser.exe", "Alert", JOptionPane.ERROR_MESSAGE);
            this.getPath();
            return false;
        }
        return true;
    }

    private void visibleExpeditionOptions() {
        if (expeditions.isSelected()) {
            expeditionOption.setVisible(true);
        } else {
            expeditionOption.setVisible(false);
        }
    }

    private void visibleDungeonOptions() {
        if (dungeons.isSelected()) {
            dungeonOption.setVisible(true);
        } else {
            dungeonOption.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        expeditionGroup = new javax.swing.ButtonGroup();
        dungeonGroup = new javax.swing.ButtonGroup();
        saveButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        serverList = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        userName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dungeons = new javax.swing.JCheckBox();
        expeditionOption = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        expeditions = new javax.swing.JCheckBox();
        dungeonOption = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        lagger = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        browserPath = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        getPath = new javax.swing.JButton();

        expeditionGroup.add(jRadioButton2);
        expeditionGroup.add(jRadioButton3);
        expeditionGroup.add(jRadioButton4);
        expeditionGroup.add(jRadioButton5);

        dungeonGroup.add(jRadioButton6);
        dungeonGroup.add(jRadioButton7);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        serverList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("User:");

        password.setText("jTextField2");

        userName.setText("jTextField1");

        jLabel2.setText("Password:");

        jLabel3.setText("Server:");

        dungeons.setText("Dungeons");
        dungeons.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dungeonsStateChanged(evt);
            }
        });

        jRadioButton2.setSelected(true);
        jRadioButton2.setText("1");

        jRadioButton3.setText("2");

        jRadioButton4.setText("3");

        jRadioButton5.setText("4");

        jLabel5.setText("Which one of the four enemies");

        javax.swing.GroupLayout expeditionOptionLayout = new javax.swing.GroupLayout(expeditionOption);
        expeditionOption.setLayout(expeditionOptionLayout);
        expeditionOptionLayout.setHorizontalGroup(
            expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expeditionOptionLayout.createSequentialGroup()
                .addGroup(expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(expeditionOptionLayout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton5))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        expeditionOptionLayout.setVerticalGroup(
            expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expeditionOptionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton5))
                .addContainerGap())
        );

        expeditions.setText("Expeditions");
        expeditions.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                expeditionsStateChanged(evt);
            }
        });

        jLabel6.setText("Which mode of dungeon");

        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Normal mode");

        jRadioButton7.setText("Hard mode");

        javax.swing.GroupLayout dungeonOptionLayout = new javax.swing.GroupLayout(dungeonOption);
        dungeonOption.setLayout(dungeonOptionLayout);
        dungeonOptionLayout.setHorizontalGroup(
            dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dungeonOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dungeonOptionLayout.setVerticalGroup(
            dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dungeonOptionLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton7)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jLabel7.setText("Lag+-(Seconds):");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(serverList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(userName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(expeditionOption, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(expeditions, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dungeons, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lagger, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dungeonOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(expeditions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(expeditionOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dungeons)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dungeonOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22, 22, 22))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serverList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lagger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38))))
        );

        jTabbedPane1.addTab("Login", jPanel1);

        jLabel4.setText("Path to browser:");

        getPath.setText("Get");
        getPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getPathActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(browserPath, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(getPath)
                .addContainerGap(214, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browserPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(getPath))
                .addContainerGap(201, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Settings", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(jButton1))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            saveConfig();
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void getPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPathActionPerformed
        browserPath.setText(getPath());
    }//GEN-LAST:event_getPathActionPerformed

    private void expeditionsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_expeditionsStateChanged
        visibleExpeditionOptions();
    }//GEN-LAST:event_expeditionsStateChanged

    private void dungeonsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dungeonsStateChanged
        visibleDungeonOptions();
    }//GEN-LAST:event_dungeonsStateChanged

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField browserPath;
    private javax.swing.ButtonGroup dungeonGroup;
    private javax.swing.JPanel dungeonOption;
    private javax.swing.JCheckBox dungeons;
    private javax.swing.ButtonGroup expeditionGroup;
    private javax.swing.JPanel expeditionOption;
    private javax.swing.JCheckBox expeditions;
    private javax.swing.JButton getPath;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner lagger;
    private javax.swing.JTextField password;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> serverList;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}
