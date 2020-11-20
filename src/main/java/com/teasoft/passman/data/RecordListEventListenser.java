package com.teasoft.passman.data;

public interface RecordListEventListenser {

    void recordAdded(int index);

    void recordRemoved(int index);

    void cleared();

}
