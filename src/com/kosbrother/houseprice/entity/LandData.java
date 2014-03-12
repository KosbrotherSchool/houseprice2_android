package com.kosbrother.houseprice.entity;

public class LandData
{
	public int estate_id;
	public String land_position;
	public String land_area;
	public String land_usage;

	public LandData(int estate_id, String land_position, String land_area, String land_usage)
	{
		this.estate_id = estate_id;
		this.land_position = land_position;
		this.land_area = land_area;
		this.land_usage = land_usage;
	}
}
