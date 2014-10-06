package com.delectable.mobile.api.models;

public class Identifier {

    public enum Type {
        FACEBOOK("facebook"),
        EMAIL("email"),
        PHONE("phone"),
        TWITTER("twitter");

        private String mLabel;

        private Type(String label) {
            mLabel = label;
        }

        @Override
        public String toString() {
            return mLabel;
        }
    }


    private String id;

    private boolean primary;

    private String type;

    private boolean verified;

    private String string;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
