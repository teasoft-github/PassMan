package com.teasoft.passman.ui.swing;

import com.teasoft.passman.data.RecordList;
import com.teasoft.passman.data.RecordListEventListenser;

import javax.swing.*;

public class RecordListTableModel extends AbstractListModel<String> implements RecordListEventListenser {

    private RecordList recordList;

    public RecordListTableModel(RecordList recordList) {
        this.setModel(recordList);
    }

    public void setModel(RecordList recordList) {
        if (this.recordList != null) {
            this.recordList.removeEventListener(this);
        }
        this.recordList = recordList;
        this.recordList.addEventListener(this);
        this.fireContentsChanged(this, 0, 0);
    }

    @Override
    public int getSize() {
        return this.recordList.getRecordCount();
    }

    @Override
    public String getElementAt(int index) {
        return this.recordList.getRecord(index).getName();
    }

    private void executeOnSwingThread(Runnable runnable) {
        try {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeAndWait(runnable);
            } else {
                runnable.run();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load main window", e);
        }
    }

    @Override
    public void recordAdded(int index) {
        Runnable runnable = () -> this.fireIntervalAdded(this, index, index);
        this.executeOnSwingThread(runnable);
    }

    @Override
    public void recordRemoved(int index) {
        Runnable runnable = () -> this.fireIntervalRemoved(this, index, index);
        this.executeOnSwingThread(runnable);
    }

    @Override
    public void cleared() {
        Runnable runnable = () -> this.fireContentsChanged(this, 0, 0);
        this.executeOnSwingThread(runnable);
    }
}
