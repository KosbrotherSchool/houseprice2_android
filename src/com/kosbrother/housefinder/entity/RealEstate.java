package com.kosbrother.housefinder.entity;

public class RealEstate
{

	public int estate_id;
	public int estate_group;

	public String estate_address;

	public int exchange_year;
	public int exchange_month;
	public int total_price;
	public double square_price;
	public double total_area;

	public String exchange_content;
	public String building_type;
	public String building_rooms;

	public double x_long;
	public double y_lat;

	public int county_id;
	public int town_id;
	public int ground_type_id;
	public int building_type_id;
	public String notes;

	public RealEstate(int id, int exchange_year, int exhange_month,
			int total_price, double square_price, double x_long, double y_lat,
			int building_type_id, int ground_type_id, double total_area)
	{
		this.estate_id = id;
		this.estate_group = 1;

		this.estate_address = "";

		this.exchange_year = exchange_year;
		this.exchange_month = exhange_month;
		this.total_price = total_price;
		this.square_price = square_price;
		this.total_area = total_area;

		this.exchange_content = "";
		this.building_type = "";
		this.building_rooms = "";

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.county_id = 0;
		this.town_id = 0;
		this.ground_type_id = ground_type_id;
		this.building_type_id = building_type_id;
		this.notes = "";
	}

	public RealEstate(int id, int estate_group, String estate_address,
			int exchange_year, int exchange_month, int total_price,
			double square_price, double total_area, String exchange_content,
			String building_type, String building_rooms, double x_long,
			double y_lat, int county_id, int town_id, int ground_type_id,
			int building_type_id, String notes)
	{

		this.estate_id = id;
		this.estate_group = estate_group;

		this.estate_address = estate_address;

		this.exchange_year = exchange_year;
		this.exchange_month = exchange_month;
		this.total_price = total_price;
		this.square_price = square_price;
		this.total_area = total_area;

		this.exchange_content = exchange_content;
		this.building_type = building_type;
		this.building_rooms = building_rooms;

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.county_id = county_id;
		this.town_id = town_id;
		this.ground_type_id = ground_type_id;
		this.building_type_id = building_type_id;
		this.notes = notes;
	}

}
