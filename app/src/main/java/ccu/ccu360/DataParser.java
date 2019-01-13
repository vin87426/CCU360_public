package ccu.ccu360;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap<String, String> getSingleMarkers(JSONObject placeJSON)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String latitude = "";
        String longitude = "";

        try
        {
            if(!placeJSON.isNull("name"))
            {
                NameOfPlace = placeJSON.getString("name");
            }
            latitude = placeJSON.getString("lat");
            longitude = placeJSON.getString("lng");

            googlePlaceMap.put("name", NameOfPlace);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }


    private List<HashMap<String, String>> getAllMarkers (JSONArray jsonArray)
    {
        int counter = jsonArray.length();

        List<HashMap<String, String>> MarkerList = new ArrayList<>();

        HashMap<String, String> MarkerMap = null;

        for(int i=0; i<counter; i++)
        {
            try
            {
                MarkerMap = getSingleMarkers((JSONObject) jsonArray.get(i));
                MarkerList.add(MarkerMap);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        return MarkerList;
    }

    public List<HashMap<String, String>> parse(String jSONdata)
    {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jSONdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getAllMarkers(jsonArray);
    }
}
