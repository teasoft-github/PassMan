package com.teasoft.passman.ui.swing;

import com.teasoft.passman.crypto.AesGcmCrypto;
import com.teasoft.passman.crypto.Crypto;
import com.teasoft.passman.data.Record;
import com.teasoft.passman.document.Document;
import com.teasoft.passman.persistence.xml.Xml;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MainWindow extends JFrame {
    private final Document document;
    private final JPanel contentPane;
    private final JMenuBar menuBar;
    private final JMenu menuItemFile;
    private final JMenuItem menuItemNew;
    private final JMenuItem menuItemOpen;
    private final JMenuItem menuItemSave;
    private final JMenuItem menuItemSaveAs;
    private final JMenuItem menuItemExportToXml;
    private final JMenuItem menuItemExit;
    private final JMenu menuItemEdit;
    private final JMenuItem menuItemAddRecord;
    private final JMenuItem menuItemEditRecord;
    private final JMenuItem menuItemDeleteRecord;
    private final JMenuItem menuItemFindRecord;
    private final JMenuItem menuItemCopyUrl;
    private final JMenuItem menuItemCopyUserName;
    private final JMenuItem menuItemCopyPassword;
    private final JMenuItem menuItemClearClipboard;
    private final JToolBar toolBar;
    private final JButton buttonNew;
    private final JButton buttonOpen;
    private final JButton buttonSave;
    private final JButton buttonAddRecord;
    private final JButton buttonEditRecord;
    private final JButton buttonRemoveRecord;
    private final JButton buttonFindRecord;
    private final JButton buttonCopyUrl;
    private final JButton buttonCopyUserName;
    private final JButton buttonCopyPassword;
    private final JButton buttonClearClipboard;
    private final JButton buttonExit;
    private final JScrollPane scrollPane;
    private final JList<String> entriesList;
    private final RecordListTableModel entriesListModel;
    private final Timer clearClipboardTimer;
    private final JFileChooser fileChooser = new JFileChooser();
    private Crypto crypto;

    public MainWindow(Document document) {
        this.document = document;

        this.setMinimumSize(new Dimension(600, 400));
        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/images/lock.png")));
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.this.exit();
            }
        });

        this.contentPane = new JPanel();
        this.contentPane.setBorder(null);
        this.contentPane.setLayout(new BorderLayout(0, 0));
        this.setContentPane(this.contentPane);

        this.menuBar = new JMenuBar();
        this.setJMenuBar(this.menuBar);

        this.menuItemFile = new JMenu("File");
        this.menuBar.add(this.menuItemFile);

        this.menuItemNew = new JMenuItem("New");
        this.menuItemNew.addActionListener(e -> this.newDocument());
        this.menuItemNew.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/new.png")));
        this.menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        this.menuItemFile.add(this.menuItemNew);

        this.menuItemOpen = new JMenuItem("Open");
        this.menuItemOpen.addActionListener(e -> this.openDocument());
        this.menuItemOpen.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/open.png")));
        this.menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        this.menuItemFile.add(this.menuItemOpen);

        this.menuItemSave = new JMenuItem("Save");
        this.menuItemSave.addActionListener(e -> this.saveDocument());
        this.menuItemSave.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/save.png")));
        this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        this.menuItemFile.add(this.menuItemSave);

        this.menuItemSaveAs = new JMenuItem("Save As");
        this.menuItemSaveAs.addActionListener(e -> this.saveAsDocument());
        this.menuItemSaveAs.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/save_as.png")));
        this.menuItemFile.add(this.menuItemSaveAs);

        this.menuItemFile.addSeparator();

        this.menuItemExportToXml = new JMenuItem("Export to XML");
        this.menuItemExportToXml.addActionListener(e -> this.exportToXml());
        this.menuItemFile.add(this.menuItemExportToXml);

        this.menuItemFile.addSeparator();

        this.menuItemExit = new JMenuItem("Exit");
        this.menuItemExit.addActionListener(e -> this.exit());
        this.menuItemExit.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/exit.png")));
        this.menuItemFile.add(this.menuItemExit);

        this.menuItemEdit = new JMenu("Edit");
        this.menuBar.add(this.menuItemEdit);

        this.menuItemAddRecord = new JMenuItem("Add Record");
        this.menuItemAddRecord.addActionListener(e -> this.addRecord());
        this.menuItemAddRecord.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_new.png")));
        this.menuItemEdit.add(this.menuItemAddRecord);

        this.menuItemEditRecord = new JMenuItem("Edit Record");
        this.menuItemEditRecord.addActionListener(e -> this.editRecord());
        this.menuItemEditRecord
                .setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_edit.png")));
        this.menuItemEdit.add(this.menuItemEditRecord);

        this.menuItemDeleteRecord = new JMenuItem("Delete Record");
        this.menuItemDeleteRecord.addActionListener(e -> this.removeRecord());
        this.menuItemDeleteRecord
                .setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_delete.png")));
        this.menuItemEdit.add(this.menuItemDeleteRecord);

        this.menuItemFindRecord = new JMenuItem("Find Record");
        this.menuItemFindRecord.addActionListener(e -> this.findRecord());
        this.menuItemFindRecord.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/find.png")));
        this.menuItemEdit.add(this.menuItemFindRecord);

        this.menuItemEdit.addSeparator();

        this.menuItemCopyUrl = new JMenuItem("Copy Url");
        this.menuItemCopyUrl.addActionListener(e -> this.copyUrl());
        this.menuItemCopyUrl.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/url.png")));
        this.menuItemEdit.add(this.menuItemCopyUrl);

        this.menuItemCopyUserName = new JMenuItem("Copy User Name");
        this.menuItemCopyUserName.addActionListener(e -> this.copyUserName());
        this.menuItemCopyUserName.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/user.png")));
        this.menuItemEdit.add(this.menuItemCopyUserName);

        this.menuItemCopyPassword = new JMenuItem("Copy Password");
        this.menuItemCopyPassword.addActionListener(e -> this.copyPassword());
        this.menuItemCopyPassword.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/keyring.png")));
        this.menuItemEdit.add(this.menuItemCopyPassword);

        this.menuItemClearClipboard = new JMenuItem("Clear Clipboard");
        this.menuItemClearClipboard.addActionListener(e -> this.clearClipboard());
        this.menuItemClearClipboard.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/clear.png")));
        this.menuItemEdit.add(this.menuItemClearClipboard);

        this.toolBar = new JToolBar();
        this.toolBar.setFloatable(false);
        this.contentPane.add(this.toolBar, BorderLayout.NORTH);

        this.buttonNew = new JButton("");
        this.buttonNew.addActionListener(e -> this.newDocument());
        this.buttonNew.setFocusable(false);
        this.buttonNew.setToolTipText("New");
        this.buttonNew.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/new.png")));
        this.toolBar.add(this.buttonNew);

        this.buttonOpen = new JButton("");
        this.buttonOpen.addActionListener(e -> this.openDocument());
        this.buttonOpen.setFocusable(false);
        this.buttonOpen.setToolTipText("Open");
        this.buttonOpen.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/open.png")));
        this.toolBar.add(this.buttonOpen);

        this.buttonSave = new JButton("");
        this.buttonSave.addActionListener(e -> this.saveDocument());
        this.buttonSave.setFocusable(false);
        this.buttonSave.setToolTipText("Save");
        this.buttonSave.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/save.png")));
        this.toolBar.add(this.buttonSave);

        this.toolBar.addSeparator();

        this.buttonAddRecord = new JButton("");
        this.buttonAddRecord.addActionListener(e -> this.addRecord());
        this.buttonAddRecord.setFocusable(false);
        this.buttonAddRecord.setToolTipText("Add Record");
        this.buttonAddRecord.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_new.png")));
        this.toolBar.add(this.buttonAddRecord);

        this.buttonEditRecord = new JButton("");
        this.buttonEditRecord.addActionListener(e -> this.editRecord());
        this.buttonEditRecord.setFocusable(false);
        this.buttonEditRecord.setToolTipText("Edit Record");
        this.buttonEditRecord.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_edit.png")));
        this.toolBar.add(this.buttonEditRecord);

        this.buttonRemoveRecord = new JButton("");
        this.buttonRemoveRecord.addActionListener(e -> this.removeRecord());
        this.buttonRemoveRecord.setFocusable(false);
        this.buttonRemoveRecord.setToolTipText("Remove Record");
        this.buttonRemoveRecord
                .setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/entry_delete.png")));
        this.toolBar.add(this.buttonRemoveRecord);

        this.buttonFindRecord = new JButton("");
        this.buttonFindRecord.addActionListener(e -> this.findRecord());
        this.buttonFindRecord.setFocusable(false);
        this.buttonFindRecord.setToolTipText("Find Record");
        this.buttonFindRecord.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/find.png")));
        this.toolBar.add(this.buttonFindRecord);

        this.toolBar.addSeparator();

        this.buttonCopyUrl = new JButton("");
        this.buttonCopyUrl.addActionListener(e -> this.copyUrl());
        this.buttonCopyUrl.setFocusable(false);
        this.buttonCopyUrl.setToolTipText("Copy Url");
        this.buttonCopyUrl.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/url.png")));
        this.toolBar.add(this.buttonCopyUrl);

        this.buttonCopyUserName = new JButton("");
        this.buttonCopyUserName.addActionListener(e -> this.copyUserName());
        this.buttonCopyUserName.setFocusable(false);
        this.buttonCopyUserName.setToolTipText("Copy User Name");
        this.buttonCopyUserName.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/user.png")));
        this.toolBar.add(this.buttonCopyUserName);

        this.buttonCopyPassword = new JButton("");
        this.buttonCopyPassword.addActionListener(e -> this.copyPassword());
        this.buttonCopyPassword.setFocusable(false);
        this.buttonCopyPassword.setToolTipText("Copy Password");
        this.buttonCopyPassword.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/keyring.png")));
        this.toolBar.add(this.buttonCopyPassword);

        this.buttonClearClipboard = new JButton("");
        this.buttonClearClipboard.addActionListener(e -> this.clearClipboard());
        this.buttonClearClipboard.setFocusable(false);
        this.buttonClearClipboard.setToolTipText("Clear Clipboard");
        this.buttonClearClipboard.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/clear.png")));
        this.toolBar.add(this.buttonClearClipboard);

        this.toolBar.addSeparator();

        this.buttonExit = new JButton("");
        this.buttonExit.addActionListener(e -> this.exit());
        this.buttonExit.setFocusable(false);
        this.buttonExit.setToolTipText("Exit");
        this.buttonExit.setIcon(new ImageIcon(MainWindow.class.getResource("/resources/images/exit.png")));
        this.toolBar.add(this.buttonExit);

        this.scrollPane = new JScrollPane();
        this.contentPane.add(this.scrollPane, BorderLayout.CENTER);

        this.entriesList = new JList<>();
        this.entriesList.addListSelectionListener(e -> this.updateSelection());
        this.scrollPane.setViewportView(this.entriesList);

        this.setLocationRelativeTo(null);

        this.clearClipboardTimer = new Timer(10 * 1000, e -> this.clearClipboard());
        this.clearClipboardTimer.stop();

        this.menuItemClearClipboard.setEnabled(false);
        this.buttonClearClipboard.setEnabled(false);

        this.entriesListModel = new RecordListTableModel(this.document.getModel());
        this.entriesList.setModel(this.entriesListModel);

        this.newDocument();
        this.updateSelection();
        this.setVisible(true);
    }

    public static void start(Document document) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainWindow(document);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load main window", e);
            }
        });
    }

    private void updateTitle() {
        String title = "PassMan";
        if (this.document.isModified()) {
            title += " *";
        }
        if (this.document.getFilePath() != null) {
            title += " - " + this.document.getFilePath();
        }
        this.setTitle(title);
    }

    private boolean showOpenFileDialog() {
        this.fileChooser.setDialogTitle("Open");
        this.fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return Document.fileExtensionDescription;
            }

            @Override
            public boolean accept(File f) {
                return f.exists() && f.getName().endsWith(Document.fileExtension);
            }
        });
        this.fileChooser.setCurrentDirectory(new File("."));
        int choice = this.fileChooser.showOpenDialog(this);
        return choice == JFileChooser.APPROVE_OPTION;
    }

    private boolean showSaveFileDialog() {
        this.fileChooser.setDialogTitle("Save As");
        this.fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return Document.fileExtensionDescription;
            }

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(Document.fileExtension);
            }
        });
        this.fileChooser.setCurrentDirectory(new File("."));
        int choice = this.fileChooser.showSaveDialog(this);
        return choice == JFileChooser.APPROVE_OPTION;
    }

    private boolean saveChanges() {
        if (!this.document.isModified()) {
            return true;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", this.getTitle(),
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION) {
            return false;
        }
        if (choice == JOptionPane.NO_OPTION) {
            return true;
        }
        this.saveDocument();
        return true;
    }

    private boolean readMasterPassword() {
        if (this.crypto != null) {
            return true;
        }
        MasterPasswordWindow window = new MasterPasswordWindow(this);
        if (window.getResult()) {
            this.crypto = new AesGcmCrypto(window.getPassword());
            return true;
        }
        return false;
    }

    private void clearMasterPassword() {
        this.crypto = null;
    }

    private void newDocument() {
        if (!this.saveChanges()) {
            return;
        }
        this.clearMasterPassword();
        this.document.clear();
        this.entriesListModel.setModel(this.document.getModel());
        this.updateTitle();
    }

    private void openDocument() {
        if (!this.saveChanges()) {
            return;
        }
        if (!this.showOpenFileDialog()) {
            return;
        }
        // Always ask for master password
        this.clearMasterPassword();
        if (!this.readMasterPassword()) {
            return;
        }
        this.document.load(this.fileChooser.getSelectedFile().getAbsolutePath(), this.crypto);
        this.entriesListModel.setModel(this.document.getModel());
        this.updateTitle();
    }

    private void saveDocument() {
        if (this.document.getFilePath() != null) {
            if (!this.readMasterPassword()) {
                return;
            }
            this.document.save(this.crypto);
            this.updateTitle();
            return;
        }
        this.saveAsDocument();
    }

    private void saveAsDocument() {
        if (!this.document.isModified()) {
            return;
        }
        if (!this.readMasterPassword()) {
            return;
        }
        if (!this.showSaveFileDialog()) {
            return;
        }
        this.document.setFilePath(this.fileChooser.getSelectedFile().getAbsolutePath());
        this.document.save(this.crypto);
        this.updateTitle();
    }

    private void exportToXml() {
        if (!this.showSaveFileDialog()) {
            return;
        }
        this.document.export(new Xml(), this.fileChooser.getSelectedFile().getAbsolutePath());
    }

    private void exit() {
        if (!this.saveChanges()) {
            return;
        }
        if (this.clearClipboardTimer.isRunning())
            this.clearClipboard();
        this.setVisible(false);
        this.dispose();
    }

    private void addRecord() {
        Record record = new Record();
        RecordEditorWindow dialog = new RecordEditorWindow(this, record);
        if (dialog.getResult()) {
            this.document.getModel().addRecord(record);
        }
        this.updateTitle();
    }

    private void editRecord() {
        int[] index = this.entriesList.getSelectedIndices();
        if (index.length != 1) {
            return;
        }
        Record record = this.document.getModel().getRecord(index[0]);
        Record edit = new Record(record);
        RecordEditorWindow dialog = new RecordEditorWindow(this, edit);
        if (dialog.getResult()) {
            this.document.getModel().removeRecord(record);
            this.document.getModel().addRecord(edit);
        }
        this.updateTitle();
    }

    private void removeRecord() {
        while (this.entriesList.getSelectedIndices().length > 0) {
            this.document.getModel().removeRecord(this.entriesList.getSelectedIndices()[0]);
        }
        this.updateTitle();
    }

    private void findRecord() {
    }

    private void copyUrl() {
        int[] index = this.entriesList.getSelectedIndices();
        if (index.length != 1) {
            return;
        }
        Record record = this.document.getModel().getRecord(index[0]);
        this.menuItemClearClipboard.setEnabled(true);
        this.buttonClearClipboard.setEnabled(true);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(record.getUrl()), null);
        this.clearClipboardTimer.start();
    }

    private void copyUserName() {
        int[] index = this.entriesList.getSelectedIndices();
        if (index.length != 1) {
            return;
        }
        Record record = this.document.getModel().getRecord(index[0]);
        this.menuItemClearClipboard.setEnabled(true);
        this.buttonClearClipboard.setEnabled(true);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(record.getUserName()), null);
        this.clearClipboardTimer.start();
    }

    private void copyPassword() {
        int[] index = this.entriesList.getSelectedIndices();
        if (index.length != 1) {
            return;
        }
        Record record = this.document.getModel().getRecord(index[0]);
        this.menuItemClearClipboard.setEnabled(true);
        this.buttonClearClipboard.setEnabled(true);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(record.getPassword()), null);
        this.clearClipboardTimer.start();
    }

    private void clearClipboard() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
        this.clearClipboardTimer.stop();
        this.menuItemClearClipboard.setEnabled(false);
        this.buttonClearClipboard.setEnabled(false);
    }

    private void updateSelection() {
        switch (this.entriesList.getSelectedIndices().length) {
            case 0:
                this.menuItemEditRecord.setEnabled(false);
                this.buttonEditRecord.setEnabled(false);

                this.menuItemDeleteRecord.setEnabled(false);
                this.buttonRemoveRecord.setEnabled(false);

                this.menuItemCopyUrl.setEnabled(false);
                this.buttonCopyUrl.setEnabled(false);

                this.menuItemCopyUserName.setEnabled(false);
                this.buttonCopyUserName.setEnabled(false);

                this.menuItemCopyPassword.setEnabled(false);
                this.buttonCopyPassword.setEnabled(false);
                break;

            case 1:
                this.menuItemEditRecord.setEnabled(true);
                this.buttonEditRecord.setEnabled(true);

                this.menuItemDeleteRecord.setEnabled(true);
                this.buttonRemoveRecord.setEnabled(true);

                this.menuItemCopyUrl.setEnabled(true);
                this.buttonCopyUrl.setEnabled(true);

                this.menuItemCopyUserName.setEnabled(true);
                this.buttonCopyUserName.setEnabled(true);

                this.menuItemCopyPassword.setEnabled(true);
                this.buttonCopyPassword.setEnabled(true);
                break;

            default:
                this.menuItemEditRecord.setEnabled(false);
                this.buttonEditRecord.setEnabled(false);

                this.menuItemDeleteRecord.setEnabled(true);
                this.buttonRemoveRecord.setEnabled(true);

                this.menuItemCopyUrl.setEnabled(false);
                this.buttonCopyUrl.setEnabled(false);

                this.menuItemCopyUserName.setEnabled(false);
                this.buttonCopyUserName.setEnabled(false);

                this.menuItemCopyPassword.setEnabled(false);
                this.buttonCopyPassword.setEnabled(false);
                break;
        }
    }
}
