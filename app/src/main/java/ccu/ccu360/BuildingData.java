package ccu.ccu360;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuildingData {
    public class Point{
        String name;
        double Ax;
        double Ay;
        double Bx;
        double By;
        double Cx;
        double Cy;
        double Dx;
        double Dy;
    }
    public List<Map<String,Point>> getdata()throws IOException {
        List<Map<String,Point>> listmpas = new ArrayList<>();
        InputStream is = BuildingData.this.getClass().getClassLoader().getResourceAsStream(("assets/" + "Building.json"));
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject obj = new JSONObject((stringBuilder.toString()));
            JSONArray infArray = obj.getJSONArray("inf");

            for(int i=0;i<infArray.length();i++){
                JSONObject inf_Array = infArray.getJSONObject(i);
                Map<String,Point> hashmap = new HashMap<>();
                Point point = new Point();
                point.name = inf_Array.getString("name");
                point.Ax = inf_Array.getDouble("pointax");
                point.Ay = inf_Array.getDouble("pointay");
                point.Bx = inf_Array.getDouble("pointbx");
                point.By = inf_Array.getDouble("pointby");
                point.Cx = inf_Array.getDouble("pointcx");
                point.Cy = inf_Array.getDouble("pointcy");
                point.Dx = inf_Array.getDouble("pointdx");
                point.Dy = inf_Array.getDouble("pointdy");
                hashmap.put("point",point);

                listmpas.add(hashmap);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return listmpas;
    }

}
