package com.kosbrother.houseprice.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;


public class County
{
	
	static String CountyMessage ="[{\"id\":1,\"name\":\"基隆市\"},{\"id\":2,\"name\":\"台北市\"},{\"id\":3,\"name\":\"新北市\"},{\"id\":4,\"name\":\"桃園縣\"},{\"id\":5,\"name\":\"新竹市\"},{\"id\":6,\"name\":\"新竹縣\"},{\"id\":7,\"name\":\"苗栗縣\"},{\"id\":8,\"name\":\"台中市\"},{\"id\":9,\"name\":\"南投縣\"},{\"id\":10,\"name\":\"彰化縣\"},{\"id\":11,\"name\":\"雲林縣\"},{\"id\":12,\"name\":\"嘉義市\"},{\"id\":13,\"name\":\"嘉義縣\"},{\"id\":14,\"name\":\"台南市\"},{\"id\":15,\"name\":\"高雄市\"},{\"id\":16,\"name\":\"屏東縣\"},{\"id\":17,\"name\":\"宜蘭縣\"},{\"id\":18,\"name\":\"花蓮縣\"},{\"id\":19,\"name\":\"臺東縣\"},{\"id\":20,\"name\":\"澎湖縣\"},{\"id\":21,\"name\":\"金門縣\"},{\"id\":22,\"name\":\"連江縣\"}]";
	
	public int id;
	public String name;
	
	public County(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public static ArrayList<County> getCounties() {
        ArrayList<County> counties = new ArrayList<County>();
        JSONArray contyArray;
        try {
        	contyArray = new JSONArray(CountyMessage);
            for (int i = 0; i < contyArray.length(); i++) {
                int county_id = contyArray.getJSONObject(i).getInt("id");
                String name = contyArray.getJSONObject(i).getString("name");
                County theCounty = new County(county_id, name);
                counties.add(theCounty);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return counties;
    }
}
