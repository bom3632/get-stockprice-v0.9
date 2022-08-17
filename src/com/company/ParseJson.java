package com.company;

import org.json.JSONObject;


public class ParseJson {
    private JSONObject jObject ;

    public ParseJson(String rawString) {
        this.jObject = new JSONObject(rawString);
    }

}
