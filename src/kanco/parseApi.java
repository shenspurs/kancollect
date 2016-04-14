package kanco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class parseApi {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("body.json")));
        String str = reader.readLine();
        System.out.println(str);
        JSONObject obj = (JSONObject) JSON.parse(str);
        Object ships = obj.get("api_mst_ship");
        if (ships instanceof JSONArray){
            JSONArray shipsArrys = (JSONArray)ships;
            for (Object objship :shipsArrys){
                JSONObject ship = (JSONObject) objship;
                String apiName = ship.getString("api_name");;
                Integer shipAppId = (Integer) ship.get("api_id");
                System.out.println("apiFilename = " + apiName + " app_id = " + shipAppId);
            }
//            System.out.println(str);
        }
        Object shipgraph = obj.get("api_mst_shipgraph");
        if (shipgraph instanceof JSONArray){
            JSONArray shipsArrys = (JSONArray)shipgraph;
            for (Object objship :shipsArrys){
                JSONObject ship = (JSONObject) objship;
                String apiFilename = ship.getString("api_filename");;
                Integer shipAppId = (Integer) ship.get("api_id");
//                System.out.println(ship);
                System.out.println("apiFilename = " + apiFilename + " app_id = " + ship.get("api_id"));
            }
//            System.out.println(str);
        }
        reader.close();
//        System.out.println(str);
    }
}
