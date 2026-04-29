package com.apiintegration.hngstage1profileaggregator.utils;

import com.apiintegration.hngstage1profileaggregator.data.model.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class QueryParser {

    private static final Map<String, String[]> KEYWORD_MAP = new HashMap<>();

    static {
        KEYWORD_MAP.put("male",         new String[]{"gender",      "male"});
        KEYWORD_MAP.put("males",        new String[]{"gender",      "male"});
        KEYWORD_MAP.put("man",          new String[]{"gender",      "male"});
        KEYWORD_MAP.put("men",          new String[]{"gender",      "male"});
        KEYWORD_MAP.put("boy",          new String[]{"gender",      "male"});
        KEYWORD_MAP.put("female",       new String[]{"gender",      "female"});
        KEYWORD_MAP.put("females",      new String[]{"gender",      "female"});
        KEYWORD_MAP.put("woman",        new String[]{"gender",      "female"});
        KEYWORD_MAP.put("women",        new String[]{"gender",      "female"});
        KEYWORD_MAP.put("girl",         new String[]{"gender",      "female"});
        KEYWORD_MAP.put("child",        new String[]{"ageGroup",    "child"});
        KEYWORD_MAP.put("children",     new String[]{"ageGroup",    "child"});
        KEYWORD_MAP.put("kid",          new String[]{"ageGroup",    "child"});
        KEYWORD_MAP.put("kids",         new String[]{"ageGroup",    "child"});
        KEYWORD_MAP.put("teenager",     new String[]{"ageGroup",    "teenager"});
        KEYWORD_MAP.put("teenagers",    new String[]{"ageGroup",    "teenager"});
        KEYWORD_MAP.put("teen",         new String[]{"ageGroup",    "teenager"});
        KEYWORD_MAP.put("teens",        new String[]{"ageGroup",    "teenager"});
        KEYWORD_MAP.put("adult",        new String[]{"ageGroup",    "adult"});
        KEYWORD_MAP.put("adults",       new String[]{"ageGroup",    "adult"});
        KEYWORD_MAP.put("senior",       new String[]{"ageGroup",    "senior"});
        KEYWORD_MAP.put("seniors",      new String[]{"ageGroup",    "senior"});
        KEYWORD_MAP.put("elderly",      new String[]{"ageGroup",    "senior"});
        KEYWORD_MAP.put("young",        new String[]{"young",       "16-24"});
        KEYWORD_MAP.put("youth",        new String[]{"young",       "16-24"});
        KEYWORD_MAP.put("above",        new String[]{"ageModifier", "min"});
        KEYWORD_MAP.put("over",         new String[]{"ageModifier", "min"});
        KEYWORD_MAP.put("older",        new String[]{"ageModifier", "min"});
        KEYWORD_MAP.put("below",        new String[]{"ageModifier", "max"});
        KEYWORD_MAP.put("under",        new String[]{"ageModifier", "max"});
        KEYWORD_MAP.put("younger",      new String[]{"ageModifier", "max"});
        KEYWORD_MAP.put("nigeria",      new String[]{"country",     "NG"});
        KEYWORD_MAP.put("nigerian",     new String[]{"country",     "NG"});
        KEYWORD_MAP.put("kenya",        new String[]{"country",     "KE"});
        KEYWORD_MAP.put("kenyan",       new String[]{"country",     "KE"});
        KEYWORD_MAP.put("ghana",        new String[]{"country",     "GH"});
        KEYWORD_MAP.put("ghanaian",     new String[]{"country",     "GH"});
        KEYWORD_MAP.put("angola",       new String[]{"country",     "AO"});
        KEYWORD_MAP.put("angolan",      new String[]{"country",     "AO"});
        KEYWORD_MAP.put("ethiopia",     new String[]{"country",     "ET"});
        KEYWORD_MAP.put("ethiopian",    new String[]{"country",     "ET"});
        KEYWORD_MAP.put("tanzania",     new String[]{"country",     "TZ"});
        KEYWORD_MAP.put("tanzanian",    new String[]{"country",     "TZ"});
        KEYWORD_MAP.put("uganda",       new String[]{"country",     "UG"});
        KEYWORD_MAP.put("ugandan",      new String[]{"country",     "UG"});
        KEYWORD_MAP.put("cameroon",     new String[]{"country",     "CM"});
        KEYWORD_MAP.put("cameroonian",  new String[]{"country",     "CM"});
        KEYWORD_MAP.put("senegal",      new String[]{"country",     "SN"});
        KEYWORD_MAP.put("senegalese",   new String[]{"country",     "SN"});
        KEYWORD_MAP.put("benin",        new String[]{"country",     "BJ"});
        KEYWORD_MAP.put("beninese",     new String[]{"country",     "BJ"});
        KEYWORD_MAP.put("egypt",        new String[]{"country",     "EG"});
        KEYWORD_MAP.put("egyptian",     new String[]{"country",     "EG"});
        KEYWORD_MAP.put("rwanda",       new String[]{"country",     "RW"});
        KEYWORD_MAP.put("rwandan",      new String[]{"country",     "RW"});
        KEYWORD_MAP.put("zambia",       new String[]{"country",     "ZM"});
        KEYWORD_MAP.put("zambian",      new String[]{"country",     "ZM"});
        KEYWORD_MAP.put("zimbabwe",     new String[]{"country",     "ZW"});
        KEYWORD_MAP.put("zimbabwean",   new String[]{"country",     "ZW"});
        KEYWORD_MAP.put("mali",         new String[]{"country",     "ML"});
        KEYWORD_MAP.put("malian",       new String[]{"country",     "ML"});
        KEYWORD_MAP.put("niger",        new String[]{"country",     "NE"});
        KEYWORD_MAP.put("nigerien",     new String[]{"country",     "NE"});
        KEYWORD_MAP.put("chad",         new String[]{"country",     "TD"});
        KEYWORD_MAP.put("chadian",      new String[]{"country",     "TD"});
        KEYWORD_MAP.put("mozambique",   new String[]{"country",     "MZ"});
        KEYWORD_MAP.put("mozambican",   new String[]{"country",     "MZ"});
        KEYWORD_MAP.put("togo",         new String[]{"country",     "TG"});
        KEYWORD_MAP.put("togolese",     new String[]{"country",     "TG"});
        KEYWORD_MAP.put("liberia",      new String[]{"country",     "LR"});
        KEYWORD_MAP.put("liberian",     new String[]{"country",     "LR"});
        KEYWORD_MAP.put("guinea",       new String[]{"country",     "GN"});
        KEYWORD_MAP.put("guinean",      new String[]{"country",     "GN"});
        KEYWORD_MAP.put("sudan",        new String[]{"country",     "SD"});
        KEYWORD_MAP.put("sudanese",     new String[]{"country",     "SD"});
        KEYWORD_MAP.put("somalia",      new String[]{"country",     "SO"});
        KEYWORD_MAP.put("somali",       new String[]{"country",     "SO"});
        KEYWORD_MAP.put("morocco",      new String[]{"country",     "MA"});
        KEYWORD_MAP.put("moroccan",     new String[]{"country",     "MA"});
        KEYWORD_MAP.put("algeria",      new String[]{"country",     "DZ"});
        KEYWORD_MAP.put("algerian",     new String[]{"country",     "DZ"});
        KEYWORD_MAP.put("ivorian",      new String[]{"country",     "CI"});
        KEYWORD_MAP.put("south african",new String[]{"country",     "ZA"});
        KEYWORD_MAP.put("sierra leone", new String[]{"country",     "SL"});
    }

    public static String[] sortNote(String[] note,int i){
        return KEYWORD_MAP.get(note[i].toLowerCase());
    }

    public static void getFieldName(String[] fieldName, Query query,String[] arrayOfNote,int i){
        switch (fieldName[0]){
            case "gender"->query.setGender(fieldName[1]);

            case "ageGroup"->
                query.setAgeGroup(fieldName[1]);
            case "country"->
                query.setCountryId(fieldName[1]);

            case "ageModifier"->{
                try {
                    if (fieldName[1].equals("min")) {
                        query.getAgeModifier().setMin(Integer.parseInt(arrayOfNote[i + 1]));
                    } else if (fieldName[1].equals("max")) {
                        query.getAgeModifier().setMax(Integer.valueOf(arrayOfNote[i + 1]));
                    }
                } catch (NumberFormatException e) {
                    log.error("Invalid number format for age modifier: " + arrayOfNote[i + 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    log.error("No age value provided after age modifier keyword: " + fieldName[1]);
                }
            }
            case "young"->{
                query.getYoung().setMin(Integer.valueOf("16"));
                query.getYoung().setMax(Integer.valueOf("24"));

            }
            default -> log.info("word doesnt get in like others");

        }
    }

    public static String[] arrayOfNOtes( String notes){
        return notes.trim().split("\\s+");
    }
}
