package com.id.yourway.providers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.interfaces.SightProvider;
import com.id.yourway.providers.listeners.RestProviderListener;
import com.id.yourway.providers.listeners.SightProviderListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BlindWallsProvider implements SightProvider {

    private final RestProvider restProvider;
    private List<Sight> sights = new ArrayList<>();
    private Context context;

    public BlindWallsProvider(Context context) {
        restProvider = RestProvider.getInstance(context);
        this.context = context;
    }

    @Override
    public void getSights(SightProviderListener listener) {
        restProvider.getRequest("https://api.blindwalls.gallery/apiv2/murals",
                new RestProviderListener() {
                    @Override
                    public void onRequestObjectAvailible(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("response");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject wall = array.getJSONObject(i);

                                int id = wall.getInt("id");
                                long date = wall.getLong("date");
                                double latitude = wall.getDouble("latitude");
                                double longitude = wall.getDouble("longitude");
                                String address = wall.getString("address");
                                String videoUrl = wall.getString("videoUrl");
                                String photographer = wall.getString("photographer");
                                String author = wall.getString("author");
                                String titleNL = wall.getJSONObject("title").getString("nl");
                                String titleEN = wall.getJSONObject("title").getString("en");
                                String descriptionNL = wall.getJSONObject("description").getString("nl");
                                String descriptionEN = wall.getJSONObject("description").getString("en");
                                String materialNL = wall.getJSONObject("material").getString("nl");
                                String materialEN = wall.getJSONObject("material").getString("en");
                                String catogoryNL = wall.getJSONObject("category").getString("nl");
                                String catogoryEN = wall.getJSONObject("category").getString("en");

                                JSONArray imagesArray = wall.getJSONArray("images");

                                List imageUrls = new ArrayList<String>();
                                for (int j = 0; j < imagesArray.length(); j++) {
                                    imageUrls.add("https://api.blindwalls.gallery/" +
                                            imagesArray.getJSONObject(j).getString("url"));
                                }

                                sights.add(new Sight(id, date, latitude, longitude, address, videoUrl,
                                        photographer, author, titleNL, titleEN, descriptionNL, descriptionEN,
                                        materialNL, materialEN, catogoryNL, catogoryEN, imageUrls, "Blindwall"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String json = loadJSONFromAsset();
                        try {
                            JSONArray jsonObj = new JSONArray(json);
                            for (int i = 0; i < jsonObj.length(); i++) {
                                JSONObject vvv = jsonObj.getJSONObject(i);
                                int id = vvv.getInt("id");
                                double lat = vvv.getDouble("lat");
                                double lon = vvv.getDouble("long");
                                String name = vvv.getString("name");
                                String note = vvv.getString("note");
                                int photoid = vvv.getInt("photoid");
                                String descriptionNL = vvv.getString("description-nl");
                                String descriptionEN = vvv.getString("description-en");

                                List<String> imageUrls2 = new ArrayList<>();
                                imageUrls2.add(String.valueOf(photoid));

                                if (!name.equals("")) {
                                    sights.add(new Sight(id, lat, lon, descriptionNL,
                                            descriptionEN, imageUrls2, name, "VVV"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listener.onSightsAvailable(sights);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        listener.onError(error);
                    }
                }, false);
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = context.getAssets().open("json/vvv.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
