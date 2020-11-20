package com.teasoft.passman;

import com.teasoft.passman.data.RecordList;
import com.teasoft.passman.document.Document;
import com.teasoft.passman.ui.swing.MainWindow;

public class App {
    public static void main(String[] args) {
        Document document = new Document(new RecordList());
        MainWindow.start(document);
    }
}
