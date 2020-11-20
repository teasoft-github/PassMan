package com.teasoft.passman.document;

import com.teasoft.passman.crypto.Crypto;
import com.teasoft.passman.data.RecordList;
import com.teasoft.passman.data.RecordListEventListenser;
import com.teasoft.passman.persistence.Persistence;
import com.teasoft.passman.persistence.xml.Xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Document implements RecordListEventListenser {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final Persistence<RecordList> persistence = new Xml();
    public static String fileExtension = ".pmd";
    public static String fileExtensionDescription = "NadraSoft Password Manager Files";
    private final RecordList recordList;
    private String filePath;
    private boolean isModified;

    public Document(RecordList recordList) {
        this.recordList = recordList;
        this.recordList.addEventListener(this);
    }

    public void clear() {
        this.recordList.clear();
        this.filePath = null;
        this.isModified = false;
    }

    public RecordList getModel() {
        return this.recordList;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        if (!filePath.endsWith(Document.fileExtension)) {
            filePath = filePath + Document.fileExtension;
        }
        this.filePath = filePath;
    }

    public boolean isModified() {
        return this.isModified;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public void save(Crypto crypto) {
        Document.persistence.save(this.recordList, this.filePath, true, crypto);
        this.isModified = false;
        try {
            String datedFilePath = String.format("%s.%s", this.filePath, Document.dateFormat.format(new Date()));
            Files.copy(Paths.get(this.filePath), Paths.get(datedFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(String filePath, Crypto crypto) {
        this.recordList.clear();
        Document.persistence.load(this.recordList, filePath, true, crypto);
        this.filePath = filePath;
        this.isModified = false;
    }

    public void export(Xml xml, String filePath) {
        xml.save(this.recordList, filePath, false, null);
    }

    @Override
    public void recordAdded(int index) {
        this.isModified = true;
    }

    @Override
    public void recordRemoved(int index) {
        this.isModified = true;
    }

    @Override
    public void cleared() {
        this.isModified = true;
    }
}
