/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jFrame;

import configuration.DriverConfiguration;
import configuration.UserConfiguration;
import core.Core;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
    private final JFileChooser filechooser;
    private final FileFilter filter = new FileNameExtensionFilter("EXE File", "exe");
    private final UserConfiguration userConfiguration = new UserConfiguration();
    private final DriverConfiguration driverConfiguration = new DriverConfiguration();
    private String[][] languages;
    private boolean initializedLang = false;

    public LoginFrame() {
        filechooser = new JFileChooser();
        initComponents();
        setLocationRelativeTo(null);
        this.setTitle("Gladiatus Tool");
        loadImageIcon();
        init();
        initServerList();
        initLanguageList();
        centerLoginPanel();
    }

    private void centerLoginPanel() {
        int widthP = loginPanel.getWidth();
        int heightP = loginPanel.getHeight();
        loginPanel.setLocation((this.getWidth() - widthP) / 2, (this.getHeight() - heightP) / 2);

    }

    private void loadImageIcon() {
        ImageIcon img = new ImageIcon("src/img/icon.jpg");
        this.setIconImage(img.getImage());
    }

    private void init() {
        userName.setText(userConfiguration.getUser());
        password.setText(userConfiguration.getPassword());
        expeditions.setSelected(userConfiguration.isExpeditions());
        dungeons.setSelected(userConfiguration.isDungeons());
        browserPath.setText(driverConfiguration.getWebDriver());
        arena.setSelected(userConfiguration.isArena());
        circuTurma.setSelected(userConfiguration.isTurma());
        criticalHealthLevel.setValue(userConfiguration.getCriticalHealthLevel());
        lagger.setValue(userConfiguration.getLag());
        setDungeonMode(userConfiguration.getDungeonMode());
        setExpeditionFocus(userConfiguration.getExpeditionFocus());
    }

    private void initServerList() {
        serverList.removeAllItems();
        List<String> servers = driverConfiguration.loadListOfServers("login_server");
        for (String server : servers) {
            serverList.addItem(server);
        }

        String server = userConfiguration.getServer();
        if (!server.isEmpty() || server != null) {
            serverList.setSelectedItem(server);
        }
    }

    private void initLanguageList() {
        language.removeAllItems();
        languages = driverConfiguration.loadListOfLanguages("mmoList1");
        for (int i = 0; i < languages.length; i++) {
            language.addItem(languages[i][1]);
        }

        String shortText = driverConfiguration.getLANG();
        if (!shortText.isEmpty() || shortText != null) {
            language.setSelectedItem(getLangFromShort(shortText));
        }
        initializedLang = true;
    }

    private String getLangFromShort(String shortText) {
        for (int i = 0; i < languages.length; i++) {
            if (languages[i][0].equals(shortText)) {
                return languages[i][1];
            }
        }
        throw new NullPointerException();
    }

    private String getShortFromLang(String lang) {
        for (int i = 0; i < languages.length; i++) {
            if (languages[i][1].equals(lang)) {
                return languages[i][0];
            }
        }
        throw new NullPointerException();
    }

    public int getServerIndex() {
        return serverList.getSelectedIndex();
    }

    private void saveConfig() throws IOException {
        userConfiguration.setUserConfig(userName.getText(), password.getText(),
                serverList.getSelectedItem().toString(), expeditions.isSelected(),
                dungeons.isSelected(), arena.isSelected(), circuTurma.isSelected(),
                getCriticalHealthLevel(), getLag(), getDungeonMode(), getExpeditionFocus(), getServerIndex());
        if (checkBrowserPath()) {
            driverConfiguration.setDriverConfig(browserPath.getText(), getShortFromLang(language.getSelectedItem().toString()));
        }
    }

//    private String getLanguageShort() {
//        language.getSelectedItem().toString();
//    }
    private int getExpeditionFocus() {
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

    private void setExpeditionFocus(int focus) {
        switch (focus) {
            case 0:
                jRadioButton2.setSelected(true);
                break;
            case 1:
                jRadioButton3.setSelected(true);
                break;
            case 2:
                jRadioButton4.setSelected(true);
                break;
            case 3:
                jRadioButton5.setSelected(true);
                break;
            default:
                jRadioButton2.setSelected(true);
                break;
        }
    }

    private int getDungeonMode() {
        return jRadioButton6.isSelected() ? 1 : 2;
    }

    private void setDungeonMode(int mode) {
        if (mode == 1) {
            jRadioButton6.setSelected(true);
        } else {
            jRadioButton7.setSelected(true);
        }
    }

    private int getLag() {
        return Integer.parseInt(lagger.getValue().toString());
    }

    private int getCriticalHealthLevel() {
        return Integer.parseInt(criticalHealthLevel.getValue().toString());
    }

    private void start() throws IOException {
        if (checkBrowserPath() && checkLogin()) {
            saveConfig();
            Thread thread = new Thread(new Core(userConfiguration,
                    driverConfiguration));
            thread.start();
        }
    }

    private boolean checkLogin() {
        if(userName.getText().isEmpty() || password.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "You have to fill name and password", "Alert", JOptionPane.ERROR_MESSAGE);
             return false;
        }
        return true;
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
            browserPath.setText(getPath());
            return false;
        }
        return true;
    }

    private void visibleExpeditionOptions() {
        boolean enabled = expeditions.isSelected();
        jRadioButton2.setEnabled(enabled);
        jRadioButton3.setEnabled(enabled);
        jRadioButton4.setEnabled(enabled);
        jRadioButton5.setEnabled(enabled);
    }

    private void visibleDungeonOptions() {
        boolean enabled = dungeons.isSelected();
        jRadioButton6.setEnabled(enabled);
        jRadioButton7.setEnabled(enabled);
    }

    private void languageStateChange() {
        if (driverConfiguration != null && language != null && initializedLang) {
            String shortLang = getShortFromLang(language.getSelectedItem().toString());
            if (!driverConfiguration.getLANG().equals(shortLang)) {
                driverConfiguration.setLANG(shortLang);
                initServerList();
            }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        language = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        serverList = new javax.swing.JComboBox<>();
        password = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        expeditionOption = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        expeditions = new javax.swing.JCheckBox();
        dungeonOption = new javax.swing.JPanel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        dungeons = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        circuTurma = new javax.swing.JCheckBox();
        arena = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lagger = new javax.swing.JSpinner();
        criticalHealthLevel = new javax.swing.JSpinner();
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

        jTabbedPane1.setOpaque(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12), javax.swing.UIManager.getDefaults().getColor("InternalFrameTitlePane.darcula.selected.borderColorTop"))); // NOI18N

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Language:");

        language.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        language.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                languageItemStateChanged(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("User:");

        userName.setText("jTextField1");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Password:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Server:");

        serverList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        password.setText("jPasswordField1");

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(language, 0, 143, Short.MAX_VALUE)
                    .addComponent(serverList, 0, 143, Short.MAX_VALUE)
                    .addComponent(password))
                .addContainerGap())
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(language, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(4, 4, 4)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(7, 7, 7)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
        );

        jPanel1.add(loginPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 240, 150));

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 94, -1));

        jTabbedPane1.addTab("Login", jPanel1);

        jPanel3.setLayout(new java.awt.GridLayout(2, 0));

        expeditionOption.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Expeditions", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jRadioButton4.setText("3");

        jRadioButton3.setText("2");

        jRadioButton2.setSelected(true);
        jRadioButton2.setText("1");

        jRadioButton5.setText("4");

        jLabel5.setText("Which one of the four enemies");

        expeditions.setText("Expeditions");
        expeditions.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                expeditionsStateChanged(evt);
            }
        });
        expeditions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expeditionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout expeditionOptionLayout = new javax.swing.GroupLayout(expeditionOption);
        expeditionOption.setLayout(expeditionOptionLayout);
        expeditionOptionLayout.setHorizontalGroup(
            expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expeditionOptionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(expeditionOptionLayout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addGap(0, 0, 0)
                        .addComponent(jRadioButton3)
                        .addGap(0, 0, 0)
                        .addComponent(jRadioButton4)
                        .addGap(0, 0, 0)
                        .addComponent(jRadioButton5))
                    .addComponent(expeditions))
                .addContainerGap())
        );
        expeditionOptionLayout.setVerticalGroup(
            expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expeditionOptionLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(expeditions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(expeditionOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(expeditionOption);

        dungeonOption.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dungeons", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Normal mode");

        jRadioButton7.setText("Hard mode");

        jLabel6.setText("Which mode of dungeon");

        dungeons.setText("Dungeons");
        dungeons.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dungeonsStateChanged(evt);
            }
        });

        javax.swing.GroupLayout dungeonOptionLayout = new javax.swing.GroupLayout(dungeonOption);
        dungeonOption.setLayout(dungeonOptionLayout);
        dungeonOptionLayout.setHorizontalGroup(
            dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dungeonOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7)
                    .addComponent(jLabel6)
                    .addComponent(dungeons))
                .addContainerGap())
        );
        dungeonOptionLayout.setVerticalGroup(
            dungeonOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dungeonOptionLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(dungeons)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton6)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton7)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.add(dungeonOption);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Arenas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        jPanel5.setPreferredSize(new java.awt.Dimension(183, 121));

        circuTurma.setText("Circu Turma Provinciarum");

        arena.setText("Arena Provinciarum");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(circuTurma)
                    .addComponent(arena))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(arena)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(circuTurma)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Other", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jLabel8.setText("Heal at (%):");

        jLabel7.setText("Lag+-(Seconds):");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lagger)
                    .addComponent(criticalHealthLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(criticalHealthLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lagger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6);

        jTabbedPane1.addTab("Game settings", jPanel3);

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
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browserPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(getPath))
                .addContainerGap(264, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Browser settings", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            start();
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void expeditionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expeditionsActionPerformed

    }//GEN-LAST:event_expeditionsActionPerformed

    private void languageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_languageItemStateChanged
        languageStateChange();
    }//GEN-LAST:event_languageItemStateChanged

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
    private javax.swing.JCheckBox arena;
    private javax.swing.JTextField browserPath;
    private javax.swing.JCheckBox circuTurma;
    private javax.swing.JSpinner criticalHealthLevel;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner lagger;
    private javax.swing.JComboBox<String> language;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPasswordField password;
    private javax.swing.JComboBox<String> serverList;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}
