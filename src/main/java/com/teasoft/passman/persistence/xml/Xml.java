package com.teasoft.passman.persistence.xml;

import com.teasoft.passman.crypto.Crypto;
import com.teasoft.passman.data.Record;
import com.teasoft.passman.data.RecordList;
import com.teasoft.passman.persistence.Persistence;
import com.teasoft.passman.persistence.xml.binding.ModelType;
import com.teasoft.passman.persistence.xml.binding.RecordType;
import com.teasoft.passman.persistence.xml.binding.RecordsType;
import com.teasoft.passman.persistence.xml.binding.Root;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

public class Xml implements Persistence<RecordList> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSZ");

    public void marshal(Root root, OutputStream outputStream) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Root.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.marshal(root, outputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public Root unmarshal(InputStream inputStream) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Root.class);
            Unmarshaller u = jc.createUnmarshaller();
            return (Root) u.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(RecordList recordList, OutputStream outputStream) {
        try {
            Root xmlRoot = new Root();
            ModelType xmlModel = new ModelType();
            xmlRoot.setModel(xmlModel);
            RecordsType xmlRecords = new RecordsType();
            xmlModel.setRecords(xmlRecords);
            for (Record record : recordList.getRecords()) {
                RecordType xmlRecord = new RecordType();
                xmlRecords.getRecord().add(xmlRecord);
                xmlRecord.setName(record.getName());
                xmlRecord.setUrl(record.getUrl());
                xmlRecord.setUserName(record.getUserName());
                xmlRecord.setPassword(record.getPassword());
                xmlRecord.setNotes(record.getNotes());
                xmlRecord.setCreatedDate(this.dateFormat.format(record.getCreatedDate()));
                xmlRecord.setModifiedDate(this.dateFormat.format(record.getModifiedDate()));
            }
            this.marshal(xmlRoot, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load(RecordList recordList, InputStream inputStream) {
        try {
            Root xmlRoot = this.unmarshal(inputStream);
            ModelType xmlModel = xmlRoot.getModel();
            for (RecordType xmlRecord : xmlModel.getRecords().getRecord()) {
                Record record = new Record();
                record.setName(xmlRecord.getName());
                record.setUrl(xmlRecord.getUrl());
                record.setUserName(xmlRecord.getUserName());
                record.setPassword(xmlRecord.getPassword());
                record.setNotes(xmlRecord.getNotes());
                record.setCreatedDate(this.dateFormat.parse(xmlRecord.getCreatedDate()));
                record.setModifiedDate(this.dateFormat.parse(xmlRecord.getModifiedDate()));
                recordList.addRecord(record);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(RecordList recordList, String filePath, Boolean applyCrypto, Crypto crypto) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            if (applyCrypto) {
                try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream,
                        crypto.getEncryptionCipher())) {
                    crypto.writeIvAndSalt(outputStream);
                    this.save(recordList, cipherOutputStream);
                }
            } else {
                this.save(recordList, outputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Verify all data was saved correctly
        RecordList loaded = new RecordList();
        this.load(loaded, filePath, applyCrypto, crypto);
        if (recordList.compareTo(loaded) != 0) {
            throw new RuntimeException("Loaded data doesn't match saved data");
        }
    }

    @Override
    public void load(RecordList recordList, String filePath, Boolean applyCrypto, Crypto crypto) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            if (applyCrypto) {
                crypto.readIvAndSalt(inputStream);
                try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream,
                        crypto.getDecryptionCipher())) {
                    this.load(recordList, cipherInputStream);
                }
            } else {
                this.load(recordList, inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
