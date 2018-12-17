package com.id.yourway.providers.helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonLoaderHelper {

    public static int VVV_ITEM_SIZE = 26;

    public static JSONObject loadJsonFile(Context context, String jsonFile){
        InputStream is;
        JSONObject jsonObject = null;
        try {
            is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            if(json.startsWith("[")){
                jsonObject = new JSONObject();
                jsonObject.put("response", new JSONArray(json));
            }
            else{
                 jsonObject = new JSONObject(json);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
