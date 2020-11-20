package com.teasoft.passman.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordList implements Comparable<RecordList> {

    private final ArrayList<RecordListEventListenser> eventListeners = new ArrayList<>();
    private final ArrayList<Record> records = new ArrayList<>();

    public void addEventListener(RecordListEventListenser eventListener) {
        this.eventListeners.add(eventListener);
    }

    public void removeEventListener(RecordListEventListenser eventListener) {
        this.eventListeners.remove(eventListener);
    }

    public void addRecord(Record record) {
        this.records.add(record);
        Collections.sort(this.records);
        int index = this.records.indexOf(record);
        for (RecordListEventListenser eventListener : this.eventListeners) {
            eventListener.recordAdded(index);
        }
    }

    public void removeRecord(Record record) {
        int index = this.records.indexOf(record);
        this.records.remove(record);
        Collections.sort(this.records);
        for (RecordListEventListenser eventListener : this.eventListeners) {
            eventListener.recordRemoved(index);
        }
    }

    public void removeRecord(int index) {
        this.records.remove(index);
        Collections.sort(this.records);
        for (RecordListEventListenser eventListener : this.eventListeners) {
            eventListener.recordRemoved(index);
        }
    }

    public void clear() {
        this.records.clear();
        // Don't clear event listeners
        for (RecordListEventListenser eventListener : this.eventListeners) {
            eventListener.cleared();
        }
    }

    public int getRecordCount() {
        return this.records.size();
    }

    public Record getRecord(int index) {
        return this.records.get(index);
    }

    public List<Record> getRecords() {
        return new ArrayList<>(this.records);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.records.size(); i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(System.lineSeparator()).append("Record ").append(i);
            sb.append(this.records.get(i).toString());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(RecordList recordList) {
        int compare = 0;

        if (this.getRecordCount() != recordList.getRecordCount()) {
            return -1;
        }

        for (int i = 0; i < this.getRecordCount(); i++) {
            compare = this.getRecord(i).compareTo(recordList.getRecord(i));
            if (compare != 0) {
                return compare;
            }
        }

        return compare;
    }
}
