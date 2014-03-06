package com.kosbrother.housefinder.data;

import com.j256.ormlite.field.DatabaseField;

public class OrmRealEstate
{
	
	public final static String Column_Estate_ID_NAME = "estate_id";
	
	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(columnName = Column_Estate_ID_NAME)
	public int estate_id;
	
	@DatabaseField
	public int estate_group;
	
	@DatabaseField
	public String estate_address;
	
	@DatabaseField
	public int exchange_date;
	@DatabaseField
	public int total_price;
	@DatabaseField
	public double square_price;
	@DatabaseField
	public double total_area;
	
	
	@DatabaseField
	public double x_long;
	@DatabaseField
	public double y_lat;

	@DatabaseField
	public int ground_type_id;
	@DatabaseField
	public int building_type_id;

	
//	@DatabaseField
//	public int estate_group;
//
//	@DatabaseField
//	public String estate_town;
//	@DatabaseField
//	public String estate_type;
//	@DatabaseField
//	public String estate_address;
//
//	@DatabaseField
//	public double ground_exchange_area;
//	@DatabaseField
//	public double ground_rent_area;
//
//	@DatabaseField
//	public String ground_usage;
//
//	@DatabaseField
//	public String date_buy;
//	@DatabaseField
//	public String content_buy;
//	@DatabaseField
//	public String date_rent;
//	@DatabaseField
//	public String content_rent;
//
//	@DatabaseField
//	public String rent_layer;
//	@DatabaseField
//	public String buy_layer;
//	@DatabaseField
//	public int building_total_layer;
//	@DatabaseField
//	public String building_type;
//	@DatabaseField
//	public String main_purpose;
//	@DatabaseField
//	public String main_material;
//	@DatabaseField
//	public String date_built;
//
//	@DatabaseField
//	public double building_exchange_area;
//	@DatabaseField
//	public int building_room; // 房
//	@DatabaseField
//	public int building_sitting_room; // 廳
//	@DatabaseField
//	public int building_rest_room; // 浴室
//
//	@DatabaseField
//	public String is_guarding;
//	@DatabaseField
//	public String is_having_furniture;
//
//	@DatabaseField
//	public int buy_total_price;
//	@DatabaseField
//	public double buy_per_square_feet;
//
//	@DatabaseField
//	public int rent_total_price;
//	@DatabaseField
//	public double rent_per_square_feet;
//
//	@DatabaseField
//	public String parking_type;
//	@DatabaseField
//	public double parking_exchange_area;
//	@DatabaseField
//	public int parking_total_price;
//
//	@DatabaseField
//	public double x_lat;
//	@DatabaseField
//	public double y_long;

	public OrmRealEstate()
	{
		// needed by ormlite
	}

}
