package com.kosbrother.houseprice.entity;

public class MonthAverage
{
	public String month_key;
	public String avg_square_price;
	public String percent_change;
	public double high_square_price;
	public double low_square_price;

	public MonthAverage(String month_key, String avg_square_price, String percent_change,
			double high_square_price, double low_square_price)
	{
		this.month_key = month_key;
		this.avg_square_price = avg_square_price;
		this.percent_change = percent_change;
		this.high_square_price = high_square_price;
		this.low_square_price = low_square_price;
	}
}
