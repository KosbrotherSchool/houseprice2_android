package com.kosbrother.houseprice.entity;

public class BuildingData
{
	public int estate_id;
	public int age;
	public String building_area;
	public String building_purpose;
	public String building_material;
	public String built_date;
	public String building_total_layer;
	public String building_layer;

	public BuildingData(int estate_id, int age, String building_area, String building_purpose,
			String building_material, String built_date, String building_total_layer,
			String building_layer)
	{
		this.estate_id = estate_id;
		this.age = age;
		this.building_area = building_area;
		this.building_purpose = building_purpose;
		this.building_material = building_material;
		this.built_date = built_date;
		this.building_total_layer = building_total_layer;
		this.building_layer = building_layer;
	}
}
