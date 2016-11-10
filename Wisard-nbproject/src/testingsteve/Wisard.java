package testingsteve;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Steve
 */
public class Wisard extends javax.swing.JFrame implements UserInterface {
//Web Internal Structure Action RecorDer

    private ImageIcon icon;
    private final javax.swing.table.DefaultTableModel jTable1Model;
    private final JPopupMenu popup;
    private final BrowserDriver bd = new BrowserDriver(this);
    private final Preferences prefs;

    /**
     * Creates new form Wisard
     */
    public Wisard() {
        URL iconURL = getClass().getResource("wisard.png");
        icon = new ImageIcon(iconURL);

        //create the tablemodel for the element table
        jTable1Model = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Element", "Location", "Name", "id", "Value", "webElement"
                }) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        initComponents();

        //create the popum menu for the element table
        popup = new JPopupMenu();
        MouseListener popupListener = new EventListener(popup, tableElements, bd);

        JMenuItem menuItem = new JMenuItem(Const.CLICK);
        menuItem.addActionListener((ActionListener) popupListener);
        popup.add(menuItem);
        menuItem = new JMenuItem(Const.FIND);
        menuItem.addActionListener((ActionListener) popupListener);
        popup.add(menuItem);
        menuItem = new JMenuItem(Const.ASSERT);
        menuItem.addActionListener((ActionListener) popupListener);
        popup.add(menuItem);
        menuItem = new JMenuItem(Const.IDENTIFY);
        menuItem.addActionListener((ActionListener) popupListener);
        popup.add(menuItem);
        tableElements.addMouseListener(popupListener);
        //hide the webElements
        tableElements.removeColumn(tableElements.getColumn("webElement"));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });

        //get prefs
        prefs = Preferences.userRoot().node("wisard");
        if (prefs.get("browser", "").contentEquals("")) {
            //System.out.println("no prefs, writing defaults");
            prefs.put("browser", "FF");
            prefs.put("output", "junit");
            prefs.put("driverFF", "./geckodriver.exe");
            prefs.put("driverCR", "./chromedriver.exe");
            prefs.put("driverIE", "./IEDriverServer.exe");
            prefs.put("driverED", "./MicrosoftWebDriver.exe");
            prefs.putBoolean("showId", false);
            prefs.putBoolean("showInvis", false);
            prefs.put("defaultURL", "http://www.google.com");
        }
        pathFF.setText(prefs.get("driverFF", ""));
        pathCR.setText(prefs.get("driverCR", ""));
        pathIE.setText(prefs.get("driverIE", ""));
        pathED.setText(prefs.get("driverED", ""));
        defaultURL.setText(prefs.get("defaultURL", ""));
        currentUrl.setText(prefs.get("defaultURL", ""));
        if (prefs.get("browser", "").contentEquals("FF")) {
            buttonFF.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("CR")) {
            buttonCR.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("IE")) {
            buttonIE.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("ED")) {
            buttonED.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("SA")) {
            buttonSA.setSelected(true);
        }
        if (prefs.getBoolean("showId", false)) {
            checkBoxId.setSelected(true);
        } else {
            //hide id column
            tableElements.removeColumn(tableElements.getColumn("id"));
        }
        if (prefs.get("output", "").contentEquals("java")) {
            buttonJava.setSelected(true);
        }
        if (prefs.get("output", "").contentEquals("junit")) {
            buttonJunit.setSelected(true);
        }

    }

    /**
     * This method is called from within the constructor to initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonsBrowser = new javax.swing.ButtonGroup();
        buttonsOutput = new javax.swing.ButtonGroup();
        dialogAbout = new javax.swing.JDialog();
        panelAbout = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        labelCopyright = new javax.swing.JLabel();
        labelLink = new javax.swing.JLabel();
        buttonCool = new javax.swing.JButton();
        dialogSettings = new javax.swing.JDialog();
        panelSettings = new javax.swing.JPanel();
        labelDefurl = new javax.swing.JLabel();
        defaultURL = new javax.swing.JTextField();
        labelDispopts = new javax.swing.JLabel();
        checkBoxId = new javax.swing.JCheckBox();
        checkBoxInvis = new javax.swing.JCheckBox();
        labelOutput = new javax.swing.JLabel();
        buttonJunit = new javax.swing.JRadioButton();
        buttonJava = new javax.swing.JRadioButton();
        labelDriver = new javax.swing.JLabel();
        buttonFF = new javax.swing.JRadioButton();
        buttonCR = new javax.swing.JRadioButton();
        buttonIE = new javax.swing.JRadioButton();
        buttonED = new javax.swing.JRadioButton();
        buttonSA = new javax.swing.JRadioButton();
        pathFF = new javax.swing.JTextField();
        pathCR = new javax.swing.JTextField();
        pathIE = new javax.swing.JTextField();
        labelPlugin = new javax.swing.JLabel();
        buttonSave = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        pathED = new javax.swing.JTextField();
        tabPane = new javax.swing.JTabbedPane();
        panelElements = new javax.swing.JPanel();
        paneElements = new javax.swing.JScrollPane();
        tableElements = new javax.swing.JTable();
        tableElements.getTableHeader().setReorderingAllowed(false);
        jToolBar1 = new javax.swing.JToolBar();
        labelUrl = new javax.swing.JLabel();
        currentUrl = new javax.swing.JTextField();
        buttonInspect = new javax.swing.JToggleButton();
        buttonRefresh = new javax.swing.JButton();
        panelCode = new javax.swing.JScrollPane();
        code = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuSettings = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuAbout = new javax.swing.JMenuItem();

        dialogAbout.setTitle("About");
        dialogAbout.setIconImage(icon.getImage());
        dialogAbout.setMinimumSize(new java.awt.Dimension(400, 400));
        dialogAbout.setModal(true);

        panelAbout.setLayout(new java.awt.GridBagLayout());

        labelName.setFont(new java.awt.Font("Tahoma", 0, 72)); // NOI18N
        labelName.setText("Wisard");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelAbout.add(labelName, gridBagConstraints);

        labelDesc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDesc.setText("Web Internal Structure Action RecorDer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelAbout.add(labelDesc, gridBagConstraints);

        labelCopyright.setText("© Steve Mellor 2014-2016");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panelAbout.add(labelCopyright, gridBagConstraints);

        labelLink.setText("<html> <a href=\"https://github.com/testingsteve/wisard\">Wisard on github</a></html>");
        labelLink.setToolTipText("");
        labelLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelLinkMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelLinkMouseEntered(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panelAbout.add(labelLink, gridBagConstraints);

        buttonCool.setText("Cool");
        buttonCool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCoolActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        panelAbout.add(buttonCool, gridBagConstraints);

        javax.swing.GroupLayout dialogAboutLayout = new javax.swing.GroupLayout(dialogAbout.getContentPane());
        dialogAbout.getContentPane().setLayout(dialogAboutLayout);
        dialogAboutLayout.setHorizontalGroup(
            dialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAboutLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dialogAboutLayout.setVerticalGroup(
            dialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAboutLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        dialogSettings.setTitle("Settings");
        dialogSettings.setIconImage(icon.getImage());
        dialogSettings.setMinimumSize(new java.awt.Dimension(500, 500));
        dialogSettings.setModal(true);

        panelSettings.setLayout(new java.awt.GridBagLayout());

        labelDefurl.setText("Default URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panelSettings.add(labelDefurl, gridBagConstraints);

        defaultURL.setColumns(20);
        defaultURL.setText("defaultURL");
        defaultURL.setToolTipText("Default URL");
        defaultURL.setMinimumSize(new java.awt.Dimension(166, 20));
        defaultURL.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelSettings.add(defaultURL, gridBagConstraints);
        defaultURL.getAccessibleContext().setAccessibleName("Default URL");

        labelDispopts.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDispopts.setText("Display Options");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelSettings.add(labelDispopts, gridBagConstraints);

        checkBoxId.setText("Show Element 'id'");
        checkBoxId.setToolTipText("Show Element 'id'");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panelSettings.add(checkBoxId, gridBagConstraints);
        checkBoxId.getAccessibleContext().setAccessibleDescription("Show id");

        checkBoxInvis.setText("List invisible Elements");
        checkBoxInvis.setToolTipText("List invisible Elements");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panelSettings.add(checkBoxInvis, gridBagConstraints);
        checkBoxInvis.getAccessibleContext().setAccessibleDescription("List Invisible");

        labelOutput.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelOutput.setText("Generated Output");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelSettings.add(labelOutput, gridBagConstraints);

        buttonsOutput.add(buttonJunit);
        buttonJunit.setText("jUnit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelSettings.add(buttonJunit, gridBagConstraints);

        buttonsOutput.add(buttonJava);
        buttonJava.setText("Java");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelSettings.add(buttonJava, gridBagConstraints);

        labelDriver.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDriver.setText("Target Browser and driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 20);
        panelSettings.add(labelDriver, gridBagConstraints);

        buttonsBrowser.add(buttonFF);
        buttonFF.setText("Firefox");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panelSettings.add(buttonFF, gridBagConstraints);

        buttonsBrowser.add(buttonCR);
        buttonCR.setText("Chrome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelSettings.add(buttonCR, gridBagConstraints);

        buttonsBrowser.add(buttonIE);
        buttonIE.setText("Internet Explorer");
        buttonIE.setToolTipText("Internet Explorer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelSettings.add(buttonIE, gridBagConstraints);
        buttonIE.getAccessibleContext().setAccessibleDescription("IE");

        buttonsBrowser.add(buttonED);
        buttonED.setText("Edge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelSettings.add(buttonED, gridBagConstraints);

        buttonsBrowser.add(buttonSA);
        buttonSA.setText("Safari");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelSettings.add(buttonSA, gridBagConstraints);

        pathFF.setColumns(20);
        pathFF.setText("pathFF");
        pathFF.setToolTipText("Path to Gecko driver");
        pathFF.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelSettings.add(pathFF, gridBagConstraints);

        pathCR.setColumns(20);
        pathCR.setText("pathCR");
        pathCR.setToolTipText("Path to Chrome driver");
        pathCR.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelSettings.add(pathCR, gridBagConstraints);
        pathCR.getAccessibleContext().setAccessibleName("Crome driver");

        pathIE.setColumns(20);
        pathIE.setText("pathIE");
        pathIE.setToolTipText("Path to IE driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelSettings.add(pathIE, gridBagConstraints);
        pathIE.getAccessibleContext().setAccessibleName("IE driver");

        labelPlugin.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        labelPlugin.setText("built in");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        panelSettings.add(labelPlugin, gridBagConstraints);

        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelSettings.add(buttonSave, gridBagConstraints);

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panelSettings.add(buttonCancel, gridBagConstraints);

        pathED.setColumns(20);
        pathED.setText("pathED");
        pathED.setToolTipText("Path to Edge driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelSettings.add(pathED, gridBagConstraints);

        javax.swing.GroupLayout dialogSettingsLayout = new javax.swing.GroupLayout(dialogSettings.getContentPane());
        dialogSettings.getContentPane().setLayout(dialogSettingsLayout);
        dialogSettingsLayout.setHorizontalGroup(
            dialogSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(dialogSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE))
        );
        dialogSettingsLayout.setVerticalGroup(
            dialogSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(dialogSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelSettings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Wisard");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(icon.getImage());
        setPreferredSize(new java.awt.Dimension(500, 500));

        tabPane.setMinimumSize(new java.awt.Dimension(300, 300));
        tabPane.setPreferredSize(new java.awt.Dimension(300, 300));

        panelElements.setPreferredSize(new java.awt.Dimension(300, 300));
        panelElements.setLayout(new java.awt.BorderLayout());

        paneElements.setPreferredSize(new java.awt.Dimension(300, 300));

        tableElements.setModel(jTable1Model);
        tableElements.setColumnSelectionAllowed(true);
        tableElements.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        paneElements.setViewportView(tableElements);
        tableElements.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        panelElements.add(paneElements, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        labelUrl.setText("URL");
        jToolBar1.add(labelUrl);

        currentUrl.setColumns(15);
        currentUrl.setText("URL");
        currentUrl.setToolTipText("Website URL");
        currentUrl.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        currentUrl.setMinimumSize(new java.awt.Dimension(6, 15));
        currentUrl.setPreferredSize(new java.awt.Dimension(12, 20));
        currentUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentUrlActionPerformed(evt);
            }
        });
        jToolBar1.add(currentUrl);
        currentUrl.getAccessibleContext().setAccessibleName("URL");

        buttonInspect.setText("Inspect");
        buttonInspect.setToolTipText("Open the URL");
        buttonInspect.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonInspect.setFocusable(false);
        buttonInspect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonInspect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonInspect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInspectActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonInspect);

        buttonRefresh.setText("Refresh");
        buttonRefresh.setToolTipText("Refresh the page elements list");
        buttonRefresh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonRefresh.setFocusable(false);
        buttonRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonRefresh);

        panelElements.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        tabPane.addTab("Elements", panelElements);

        code.setEditable(false);
        code.setColumns(20);
        code.setRows(5);
        code.setTabSize(4);
        panelCode.setViewportView(code);

        tabPane.addTab("Generated Code", panelCode);

        menuFile.setText("File");

        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        menuFile.add(menuExit);

        menuBar.add(menuFile);

        menuEdit.setText("Edit");

        menuSettings.setText("Settings");
        menuSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSettingsActionPerformed(evt);
            }
        });
        menuEdit.add(menuSettings);

        menuBar.add(menuEdit);

        menuHelp.setText("Help");

        menuAbout.setText("About");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuAbout);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void currentUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentUrlActionPerformed
        buttonInspect.doClick();
    }//GEN-LAST:event_currentUrlActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed

        //update UI
        currentUrl.setText(defaultURL.getText());

        if (!prefs.getBoolean("showId", false) && checkBoxId.isSelected()) {
            //reset table to show id
            jTable1Model.fireTableStructureChanged();
            //re-hide webElements
            tableElements.removeColumn(tableElements.getColumn("webElement"));
        }
        if (prefs.getBoolean("showId", false) && !checkBoxId.isSelected()) {
            //hide id column
            tableElements.removeColumn(tableElements.getColumn("id"));
        }

        //save new settings
        prefs.put("defaultURL", defaultURL.getText());
        prefs.putBoolean("showId", checkBoxId.isSelected());
        prefs.putBoolean("showInvis", checkBoxInvis.isSelected());

        if (buttonFF.isSelected()) {
            prefs.put("browser", "FF");
        }
        if (buttonIE.isSelected()) {
            prefs.put("browser", "IE");
        }
        if (buttonED.isSelected()) {
            prefs.put("browser", "ED");
        }
        if (buttonCR.isSelected()) {
            prefs.put("browser", "CR");
        }
        if (buttonSA.isSelected()) {
            prefs.put("browser", "SA");
        }
        if (buttonJunit.isSelected()) {
            prefs.put("output", "junit");
        }
        if (buttonJava.isSelected()) {
            prefs.put("output", "java");
        }

        prefs.put("driverFF", pathFF.getText());
        prefs.put("driverCR", pathCR.getText());
        prefs.put("driverIE", pathIE.getText());
        prefs.put("driverED", pathED.getText());

        dialogSettings.setVisible(false);
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonInspectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInspectActionPerformed
        // TODO add your handling code here:
        //System.out.println(evt.getActionCommand());
        if (buttonInspect.isSelected()) {
            currentUrl.setEnabled(false);
            Boolean success = false;
            bd.initWriter(prefs.get("output", "junit"));
            if (prefs.get("browser", "").contentEquals("FF")) {
                success = bd.initFF(currentUrl.getText(), prefs.get("driverFF", ""));
            }
            if (prefs.get("browser", "").contentEquals("CR")) {
                success = bd.initCR(currentUrl.getText(), prefs.get("driverCR", ""));
            }
            if (prefs.get("browser", "").contentEquals("IE")) {
                success = bd.initIE(currentUrl.getText(), prefs.get("driverIE", ""));
            }
            if (prefs.get("browser", "").contentEquals("ED")) {
                success = bd.initED(currentUrl.getText(), prefs.get("driverED", ""));
            }
            if (prefs.get("browser", "").contentEquals("SA")) {
                success = bd.initSA(currentUrl.getText());
            }

            if (success) {
                Thread t = new Thread(bd, "Page Examiner");
                t.start();
            } else {
                currentUrl.setEnabled(true);
                buttonInspect.doClick();
                bd.close();
            }

        } else {
            currentUrl.setEnabled(true);
            //tidy up
            popup.setVisible(false);
            for (int i = jTable1Model.getRowCount(); i > 0; i--) {
                jTable1Model.removeRow(i - 1);
            }
            bd.close();
        }
    }//GEN-LAST:event_buttonInspectActionPerformed

    private void buttonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshActionPerformed
        rescan();
    }//GEN-LAST:event_buttonRefreshActionPerformed

    private void labelLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLinkMouseClicked
        // TODO add your handling code here:
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/testingsteve/wisard"));
            } catch (URISyntaxException | IOException | UnsupportedOperationException ex) {
                this.errorMessage("Unable to open browser, please visit:\n https://github.com/testingsteve/wisard");
            }
        } else {
            this.errorMessage("Unable to open browser, please visit:\n https://github.com/testingsteve/wisard");
        }
    }//GEN-LAST:event_labelLinkMouseClicked

    private void labelLinkMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLinkMouseEntered
        // TODO add your handling code here:
        labelLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_labelLinkMouseEntered

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_menuExitActionPerformed

    private void menuSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSettingsActionPerformed
        // TODO add your handling code here:
        dialogSettings.setVisible(true);
    }//GEN-LAST:event_menuSettingsActionPerformed

    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAboutActionPerformed
        // TODO add your handling code here:
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_menuAboutActionPerformed

    private void buttonCoolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCoolActionPerformed
        // TODO add your handling code here:
        dialogAbout.setVisible(false);
    }//GEN-LAST:event_buttonCoolActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        // TODO add your handling code here:
        //restore original settings
        defaultURL.setText((prefs.get("defaultURL", "")));
        checkBoxId.setSelected(prefs.getBoolean("showId", true));
        checkBoxInvis.setSelected(prefs.getBoolean("showInvis", true));

        if (prefs.get("browser", "").contentEquals("FF")) {
            buttonFF.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("CR")) {
            buttonCR.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("IE")) {
            buttonIE.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("ED")) {
            buttonED.setSelected(true);
        }
        if (prefs.get("browser", "").contentEquals("SA")) {
            buttonSA.setSelected(true);
        }

        pathFF.setText(prefs.get("driverFF", ""));
        pathCR.setText(prefs.get("driverCR", ""));
        pathIE.setText(prefs.get("driverIE", ""));
        pathED.setText(prefs.get("driverED", ""));

        dialogSettings.setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">

        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Wisard().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton buttonCR;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonCool;
    private javax.swing.JRadioButton buttonED;
    private javax.swing.JRadioButton buttonFF;
    private javax.swing.JRadioButton buttonIE;
    private javax.swing.JToggleButton buttonInspect;
    private javax.swing.JRadioButton buttonJava;
    private javax.swing.JRadioButton buttonJunit;
    private javax.swing.JButton buttonRefresh;
    private javax.swing.JRadioButton buttonSA;
    private javax.swing.JButton buttonSave;
    private javax.swing.ButtonGroup buttonsBrowser;
    private javax.swing.ButtonGroup buttonsOutput;
    private javax.swing.JCheckBox checkBoxId;
    private javax.swing.JCheckBox checkBoxInvis;
    private javax.swing.JTextArea code;
    private javax.swing.JTextField currentUrl;
    private javax.swing.JTextField defaultURL;
    private javax.swing.JDialog dialogAbout;
    private javax.swing.JDialog dialogSettings;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelCopyright;
    private javax.swing.JLabel labelDefurl;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelDispopts;
    private javax.swing.JLabel labelDriver;
    private javax.swing.JLabel labelLink;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelPlugin;
    private javax.swing.JLabel labelUrl;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuSettings;
    private javax.swing.JScrollPane paneElements;
    private javax.swing.JPanel panelAbout;
    private javax.swing.JScrollPane panelCode;
    private javax.swing.JPanel panelElements;
    private javax.swing.JPanel panelSettings;
    private javax.swing.JTextField pathCR;
    private javax.swing.JTextField pathED;
    private javax.swing.JTextField pathFF;
    private javax.swing.JTextField pathIE;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tableElements;
    // End of variables declaration//GEN-END:variables

    private void exitProcedure() {
        bd.close();
        this.dispose();
        System.exit(0);
    }

    @Override
    public void addItem(String element, String location, String name, String id, String value, Object webElement, Boolean displayed) {
        //adds content to the elements table
        if ((!displayed) && (prefs.getBoolean("showInvis", false))) {
            location = location + Const.INVISIBLE;
            Object item = new Object[]{element, location, name, id, value, webElement};
            jTable1Model.addRow((Object[]) item);
        } else if (displayed) {
            Object item = new Object[]{element, location, name, id, value, webElement};
            jTable1Model.addRow((Object[]) item);
        }
    }

    @Override
    public void rescan() {
        //triggers rescan of the page

        //tidy up first
        popup.setVisible(false);
        for (int i = jTable1Model.getRowCount(); i > 0; i--) {
            jTable1Model.removeRow(i - 1);
        }

        //off we go
        Thread t = new Thread(bd, "Page Examiner");
        t.start();
    }

    @Override
    public void abort() {
        //triggers a UI cleanup
        this.errorMessage("Unexpected error!");
        buttonInspect.doClick();
    }

    @Override
    public void addCode(String fragment) {
        //adds content to the generated code
        code.setText(fragment);
    }

    @Override
    public void insertCode(String fragment, int above) {
        //inserts content to the generated code
        int point;
        try {
            point = code.getLineEndOffset(code.getLineCount() - above);
        } catch (BadLocationException e) {
            point = 1;
            //it'll be messy
        }
        code.insert(fragment, point - 1);

    }

    @Override
    public boolean alertResponse(String title) {
        int ok = JOptionPane.showConfirmDialog(new JFrame(), title, "Alert box opened by browser", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String enterValue(String title) {
        return (String) JOptionPane.showInputDialog(new JFrame(), title, "Enter Value", JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public String enterSelection(String title, String choices[]) {
        return (String) JOptionPane.showInputDialog(new JFrame(), title, "Make Selection", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
    }

    @Override
    public void elementIdent(String text) {
        if (text.length() > Const.MAX_SIZ) {
            //crop the text
            text = text.substring(0, Const.MAX_SIZ);
        }
        JOptionPane.showMessageDialog(new JFrame(), text, "Element Highlighted in Browser", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void errorMessage(String message) {
        if (message.length() > Const.MAX_SIZ) {
            //crop the text
            message = message.substring(0, Const.MAX_SIZ);
        }
        JOptionPane.showMessageDialog(new JFrame(), message, "Error", JOptionPane.INFORMATION_MESSAGE);
    }
}
