package com.delectable.mobile.util;

public enum USStates {
    AL("AL", "Alabama"),
    AK("AK", "Alaska"),
    AZ("AZ", "Arizona"),
    AR("AR", "Arkansas"),
    CA("CA", "California"),
    CO("CO", "Colorado"),
    CT("CT", "Connecticut"),
    DE("DE", "Delaware"),
    DC("DC", "District of Columbia"),
    FL("FL", "Florida"),
    GA("GA", "Georgia"),
    HI("HI", "Hawaii"),
    ID("ID", "Idaho"),
    IL("IL", "Illinois"),
    IN("IN", "Indiana"),
    IA("IA", "Iowa"),
    KS("KS", "Kansas"),
    KY("KY", "Kentucky"),
    LA("LA", "Louisiana"),
    ME("ME", "Maine"),
    MD("MD", "Maryland"),
    MA("MA", "Massachusetts"),
    MI("MI", "Michigan"),
    MN("MN", "Minnesota"),
    MS("MS", "Mississippi"),
    MO("MO", "Missouri"),
    MT("MT", "Montana"),
    NE("NE", "Nebraska"),
    NV("NV", "Nevada"),
    NH("NH", "New Hampshire"),
    NJ("NJ", "New Jersey"),
    NM("NM", "New Mexico"),
    NY("NY", "New York"),
    NC("NC", "North Carolina"),
    ND("ND", "North Dakota"),
    OH("OH", "Ohio"),
    OK("OK", "Oklahoma"),
    OR("OR", "Oregon"),
    PA("PA", "Pennsylvania"),
    // Not shipping to Puerto Rico
//    PR("PR", "Puerto Rico"),
    RI("RI", "Rhode Island"),
    SC("SC", "South Carolina"),
    SD("SD", "South Dakota"),
    TN("TN", "Tennessee"),
    TX("TX", "Texas"),
    UT("UT", "Utah"),
    VT("VT", "Vermont"),
    VA("VA", "Virginia"),
    WA("WA", "Washington"),
    WV("WV", "West Virginia"),
    WI("WI", "Wisconsin"),
    WY("WY", "Wyoming"),;

    private String mStateAbbreviation;

    private String mStateName;

    USStates(String stateAbbreviation, String stateName) {
        mStateName = stateName;
        mStateAbbreviation = stateAbbreviation;
    }

    public static USStates stateByNameOrAbbreviation(String state) {
        if (state == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            if (state.equalsIgnoreCase(values()[i].getStateName()) ||
                    state.equalsIgnoreCase(values()[i].getStateAbbreviation())) {
                return values()[i];
            }
        }
        return null;
    }

    public String getStateAbbreviation() {
        return mStateAbbreviation;
    }

    public String getStateName() {
        return mStateName;
    }
}
