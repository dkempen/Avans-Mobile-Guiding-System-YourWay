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

import java.util.ArrayList;
import java.util.List;

public class BlindWallsProvider implements SightProvider {

    private final RestProvider restProvider;

    public BlindWallsProvider(Context context) {
        restProvider = RestProvider.getInstance(context);
    }

    @Override
    public void getSights(SightProviderListener listener) {
        restProvider.getRequest("https://api.blindwalls.gallery/apiv2/murals",
                new RestProviderListener() {
                    @Override
                    public void onRequestObjectAvailible(JSONObject response) {
                        List<Sight> sights = new ArrayList<>();

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
                                String descriptionNL = wall.getJSONObject("description").getString("en");
                                String descriptionEN = wall.getJSONObject("description").getString("en");
                                String materialNL = wall.getJSONObject("material").getString("en");
                                String materialEN = wall.getJSONObject("material").getString("en");
                                String catogoryNL = wall.getJSONObject("category").getString("en");
                                String catogoryEN = wall.getJSONObject("category").getString("en");

                                JSONArray imagesArray = wall.getJSONArray("images");

                                List imageUrls = new ArrayList<String>();
                                for (int j = 0; j < imagesArray.length(); j++)
                                {
                                    imageUrls.add("https://api.blindwalls.gallery/" +
                                            imagesArray.getJSONObject(j).getString("url"));
                                }

                                sights.add(new Sight(id, date, latitude, longitude, address, videoUrl,
                                        photographer, author, titleNL, titleEN, descriptionNL, descriptionEN,
                                        materialNL, materialEN, catogoryNL, catogoryEN, imageUrls));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listener.onSightsAvailable(sights);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        // TODO: Error handling of BlindWallsAPI
                    }
                }, false);
    }
}
