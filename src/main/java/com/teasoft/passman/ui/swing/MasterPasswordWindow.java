package com.teasoft.passman.ui.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MasterPasswordWindow extends JDialog {

    private final JPanel contentPane;
    private final JPanel panelRecord;
    private final JPanel panelButtons;
    private final JButton btnOk;
    private final JButton btnCancel;
    private final JLabel lblMasterPassword;
    private final JPasswordField txtMasterPassword;
    private boolean result;

    public MasterPasswordWindow(JFrame parent) {
        super(parent, true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MasterPasswordWindow.this.handle_this_windowClosing(e);
            }
        });
        this.setTitle("Enter Master Password");
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(MasterPasswordWindow.class.getResource("/resources/images/keyring.png")));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 384, 143);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setLayout(new BorderLayout(0, 0));
        this.setContentPane(this.contentPane);

        this.panelRecord = new JPanel();
        this.contentPane.add(this.panelRecord, BorderLayout.CENTER);
        GridBagLayout gbl_panelRecord = new GridBagLayout();
        gbl_panelRecord.columnWeights = new double[]{0.0, 1.0};
        gbl_panelRecord.rowWeights = new double[]{0.0};
        this.panelRecord.setLayout(gbl_panelRecord);

        this.lblMasterPassword = new JLabel("Enter Master Password");
        GridBagConstraints gbc_lblMasterPassword = new GridBagConstraints();
        gbc_lblMasterPassword.insets = new Insets(0, 0, 5, 5);
        gbc_lblMasterPassword.anchor = GridBagConstraints.WEST;
        gbc_lblMasterPassword.gridx = 0;
        gbc_lblMasterPassword.gridy = 0;
        this.panelRecord.add(this.lblMasterPassword, gbc_lblMasterPassword);

        this.txtMasterPassword = new JPasswordField();
        this.lblMasterPassword.setLabelFor(this.txtMasterPassword);
        GridBagConstraints gbc_txtMasterPassword = new GridBagConstraints();
        gbc_txtMasterPassword.insets = new Insets(0, 0, 5, 0);
        gbc_txtMasterPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMasterPassword.gridx = 1;
        gbc_txtMasterPassword.gridy = 0;
        this.panelRecord.add(this.txtMasterPassword, gbc_txtMasterPassword);
        this.txtMasterPassword.setColumns(40);

        this.panelButtons = new JPanel();
        FlowLayout fl_panelButtons = (FlowLayout) this.panelButtons.getLayout();
        fl_panelButtons.setAlignment(FlowLayout.RIGHT);
        this.contentPane.add(this.panelButtons, BorderLayout.SOUTH);

        this.btnOk = new JButton("OK");
        this.btnOk.addActionListener(MasterPasswordWindow.this::handle_btnOk_actionPerformed);
        this.btnOk.setIcon(new ImageIcon(MasterPasswordWindow.class.getResource("/resources/images/accept.png")));
        this.panelButtons.add(this.btnOk);

        this.btnCancel = new JButton("Cancel");
        this.btnCancel.addActionListener(MasterPasswordWindow.this::handle_btnCancel_actionPerformed);
        this.btnCancel.setIcon(new ImageIcon(MasterPasswordWindow.class.getResource("/resources/images/cancel.png")));
        this.panelButtons.add(this.btnCancel);

        this.setLocationRelativeTo(this.getParent());
        this.pack();

        this.contentPane.registerKeyboardAction(this::handle_btnCancel_actionPerformed,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.contentPane.registerKeyboardAction(this::handle_btnOk_actionPerformed,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.setVisible(true);
    }

    private void handle_this_windowClosing(WindowEvent e) {
        this.result = false;
    }

    private void handle_btnOk_actionPerformed(ActionEvent e) {
        this.result = true;
        this.dispose();
    }

    private void handle_btnCancel_actionPerformed(ActionEvent e) {
        this.result = false;
        this.dispose();
    }

    public char[] getPassword() {
        return this.txtMasterPassword.getPassword();
    }

    public boolean getResult() {
        return this.result;
    }
}
