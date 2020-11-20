package com.teasoft.passman.ui.swing;

import com.teasoft.passman.data.Record;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RecordEditorWindow extends JDialog {

    private final JPanel contentPane;
    private final JPanel panelRecord;
    private final JPanel panelButtons;
    private final JButton btnOk;
    private final JButton btnCancel;
    private final JLabel lblName;
    private final JLabel lblUrl;
    private final JLabel lblUserName;
    private final JLabel lblPassword;
    private final JLabel lblNotes;
    private final JTextField txtName;
    private final JTextField txtUrl;
    private final JTextField txtUserName;
    private final JTextField txtPassword;
    private final JScrollPane panelNotes;
    private final JTextArea txtNotes;
    private final Record record;
    private boolean result;

    /**
     * Create the frame.
     */
    public RecordEditorWindow(JFrame parent, Record record) {
        super(parent, true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                RecordEditorWindow.this.handle_this_windowClosing(e);
            }
        });
        this.setTitle("Edit Record");
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(RecordEditorWindow.class.getResource("/resources/images/entry_edit.png")));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 450, 362);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setLayout(new BorderLayout(0, 0));
        this.setContentPane(this.contentPane);

        this.panelRecord = new JPanel();
        this.contentPane.add(this.panelRecord, BorderLayout.CENTER);
        GridBagLayout gbl_panelRecord = new GridBagLayout();
        gbl_panelRecord.columnWidths = new int[]{0, 0, 0};
        gbl_panelRecord.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_panelRecord.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelRecord.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        this.panelRecord.setLayout(gbl_panelRecord);

        this.lblName = new JLabel("Name");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.anchor = GridBagConstraints.WEST;
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 0;
        this.panelRecord.add(this.lblName, gbc_lblName);

        this.txtName = new JTextField();
        this.lblName.setLabelFor(this.txtName);
        GridBagConstraints gbc_txtName = new GridBagConstraints();
        gbc_txtName.insets = new Insets(0, 0, 5, 0);
        gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtName.gridx = 1;
        gbc_txtName.gridy = 0;
        this.panelRecord.add(this.txtName, gbc_txtName);
        this.txtName.setColumns(40);

        this.lblUrl = new JLabel("Url");
        GridBagConstraints gbc_lblUrl = new GridBagConstraints();
        gbc_lblUrl.anchor = GridBagConstraints.WEST;
        gbc_lblUrl.insets = new Insets(0, 0, 5, 5);
        gbc_lblUrl.gridx = 0;
        gbc_lblUrl.gridy = 1;
        this.panelRecord.add(this.lblUrl, gbc_lblUrl);

        this.txtUrl = new JTextField();
        this.lblUrl.setLabelFor(this.txtUrl);
        GridBagConstraints gbc_txtUrl = new GridBagConstraints();
        gbc_txtUrl.insets = new Insets(0, 0, 5, 0);
        gbc_txtUrl.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtUrl.gridx = 1;
        gbc_txtUrl.gridy = 1;
        this.panelRecord.add(this.txtUrl, gbc_txtUrl);
        this.txtUrl.setColumns(40);

        this.lblUserName = new JLabel("User Name");
        GridBagConstraints gbc_lblUserName = new GridBagConstraints();
        gbc_lblUserName.anchor = GridBagConstraints.WEST;
        gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
        gbc_lblUserName.gridx = 0;
        gbc_lblUserName.gridy = 2;
        this.panelRecord.add(this.lblUserName, gbc_lblUserName);

        this.txtUserName = new JTextField();
        this.lblUserName.setLabelFor(this.txtUserName);
        GridBagConstraints gbc_txtUserName = new GridBagConstraints();
        gbc_txtUserName.insets = new Insets(0, 0, 5, 0);
        gbc_txtUserName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtUserName.gridx = 1;
        gbc_txtUserName.gridy = 2;
        this.panelRecord.add(this.txtUserName, gbc_txtUserName);
        this.txtUserName.setColumns(40);

        this.lblPassword = new JLabel("Password");
        GridBagConstraints gbc_lblPassword = new GridBagConstraints();
        gbc_lblPassword.anchor = GridBagConstraints.WEST;
        gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
        gbc_lblPassword.gridx = 0;
        gbc_lblPassword.gridy = 3;
        this.panelRecord.add(this.lblPassword, gbc_lblPassword);

        this.txtPassword = new JTextField();
        this.lblPassword.setLabelFor(this.txtPassword);
        GridBagConstraints gbc_txtPassword = new GridBagConstraints();
        gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
        gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtPassword.gridx = 1;
        gbc_txtPassword.gridy = 3;
        this.panelRecord.add(this.txtPassword, gbc_txtPassword);
        this.txtPassword.setColumns(40);

        this.lblNotes = new JLabel("Notes");
        GridBagConstraints gbc_lblNotes = new GridBagConstraints();
        gbc_lblNotes.anchor = GridBagConstraints.WEST;
        gbc_lblNotes.insets = new Insets(0, 0, 5, 5);
        gbc_lblNotes.gridx = 0;
        gbc_lblNotes.gridy = 4;
        this.panelRecord.add(this.lblNotes, gbc_lblNotes);

        this.panelNotes = new JScrollPane();
        GridBagConstraints gbc_panelNotes = new GridBagConstraints();
        gbc_panelNotes.gridwidth = 2;
        gbc_panelNotes.insets = new Insets(0, 0, 0, 5);
        gbc_panelNotes.fill = GridBagConstraints.BOTH;
        gbc_panelNotes.gridx = 0;
        gbc_panelNotes.gridy = 5;
        this.panelRecord.add(this.panelNotes, gbc_panelNotes);

        this.txtNotes = new JTextArea();
        this.txtNotes.setFont(UIManager.getFont("TextField.font"));
        this.txtNotes.setColumns(40);
        this.txtNotes.setRows(8);
        this.panelNotes.setViewportView(this.txtNotes);

        this.panelButtons = new JPanel();
        FlowLayout fl_panelButtons = (FlowLayout) this.panelButtons.getLayout();
        fl_panelButtons.setAlignment(FlowLayout.RIGHT);
        this.contentPane.add(this.panelButtons, BorderLayout.SOUTH);

        this.btnOk = new JButton("OK");
        this.btnOk.addActionListener(RecordEditorWindow.this::handle_btnOk_actionPerformed);
        this.btnOk.setIcon(new ImageIcon(RecordEditorWindow.class.getResource("/resources/images/accept.png")));
        this.panelButtons.add(this.btnOk);

        this.btnCancel = new JButton("Cancel");
        this.btnCancel.addActionListener(RecordEditorWindow.this::handle_btnCancel_actionPerformed);
        this.btnCancel.setIcon(new ImageIcon(RecordEditorWindow.class.getResource("/resources/images/cancel.png")));
        this.panelButtons.add(this.btnCancel);

        this.setLocationRelativeTo(this.getParent());
        this.pack();

        this.contentPane.registerKeyboardAction(this::handle_btnCancel_actionPerformed,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.contentPane.registerKeyboardAction(this::handle_btnOk_actionPerformed,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.record = record;
        this.txtName.setText(this.record.getName());
        this.txtUrl.setText(this.record.getUrl());
        this.txtUserName.setText(this.record.getUserName());
        this.txtPassword.setText(this.record.getPassword());
        this.txtNotes.setText(this.record.getNotes());

        this.setVisible(true);
    }

    private void handle_this_windowClosing(WindowEvent e) {
        this.result = false;
    }

    private void handle_btnOk_actionPerformed(ActionEvent e) {
        String name = this.txtName.getText().trim();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = this.txtUrl.getText().trim();
        if (url.length() == 0) {
            JOptionPane.showMessageDialog(this, "Url cannot be empty", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userName = this.txtUserName.getText().trim();
        if (userName.length() == 0) {
            JOptionPane.showMessageDialog(this, "User name cannot be empty", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = this.txtPassword.getText().trim();
        if (password.length() == 0) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String notes = this.txtNotes.getText().trim();

        this.record.setName(name);
        this.record.setUrl(url);
        this.record.setUserName(userName);
        this.record.setPassword(password);
        this.record.setNotes(notes);
        this.result = true;

        this.dispose();
    }

    private void handle_btnCancel_actionPerformed(ActionEvent e) {
        this.result = false;
        this.dispose();
    }

    public boolean getResult() {
        return this.result;
    }
}
