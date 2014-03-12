package com.kosbrother.houseprice.entity;

public class ParkingData
{
	public int estate_id;
	public String parking_type;
	public String parking_price;
	public String parking_area;

	public ParkingData(int estate_id, String parking_type,
			String parking_price, String parking_area)
	{
		this.estate_id = estate_id;
		this.parking_type = parking_type;
		this.parking_price = parking_price;
		this.parking_area = parking_area;
	}

}
