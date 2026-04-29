package com.apiintegration.hngstage1profileaggregator.data.model;

import lombok.Data;

@Data
public class Query {
    private String gender;
    private String ageGroup;
    private String countryId;
    private AgeQualifier ageModifier;
    private AgeQualifier young;

    public Query (){
        ageModifier = new AgeQualifier();
        young = new AgeQualifier();
    }
}
