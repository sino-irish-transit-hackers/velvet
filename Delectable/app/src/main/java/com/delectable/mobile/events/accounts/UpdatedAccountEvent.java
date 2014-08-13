package com.delectable.mobile.events.accounts;

public class UpdatedAccountEvent {

    private String mAccountId;

    public UpdatedAccountEvent(String id) {
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
