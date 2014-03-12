package com.kosbrother.houseprice.api;

public class InfoParserApi
{
	public static String parseBuildingType(int buildingTypeId)
	{
		String buildType = "";
		try
		{
			switch (buildingTypeId)
			{
			case 1:
				buildType = "公寓";
				break;
			case 2:
				buildType = "透天厝";
				break;
			case 3:
				buildType = "店面";
				break;
			case 4:
				buildType = "商辦";
				break;
			case 5:
				buildType = "住宅";
				break;
			case 6:
				buildType = "華廈";
				break;
			case 7:
				buildType = "套房";
				break;
			case 8:
				buildType = "工廠";
				break;
			case 9:
				buildType = "廠辦";
				break;
			case 10:
				buildType = "農舍";
				break;
			case 11:
				buildType = "倉庫";
				break;
			case 12:
				buildType = "其他";
				break;
			default:
				break;
			}
		} catch (Exception e)
		{

		}
		return buildType;
	}

	public static String parseGroundType(int groundTypeId)
	{
		String buildType = "";
		try
		{
			switch (groundTypeId)
			{
			case 1:
				buildType = "房地";
				break;
			case 2:
				buildType = "房地+車";
				break;
			case 3:
				buildType = "土地";
				break;
			case 4:
				buildType = "建物";
				break;
			case 5:
				buildType = "車位";
				break;
			default:
				break;
			}
		} catch (Exception e)
		{

		}
		return buildType;
	}

	public static String parseMonthKey(String monthKey)
	{
		return monthKey.substring(0, 3) + "/" + monthKey.substring(3);
	}

	public static String parseSquarePrice(double sq_price)
	{
		String sqPriceString = Double.toString(sq_price);
		if (sqPriceString.indexOf(".")!=-1)
		{	
			return sqPriceString.substring(0, sqPriceString.indexOf(".")+2) + "萬";
		}else {
			return sqPriceString + "萬";
		}
	}
	
	public static String parsePriceChangePercent(double percentChange){
		if (percentChange > 1){
			percentChange = (percentChange -1)*100;
			String percentString = Double.toString(percentChange);
			if (percentString.indexOf(".")!=-1){
				return "漲"+percentString.substring(0, percentString.indexOf(".")+2)+"%";
			}else {
				return "漲"+percentString+"%";
			}
		}else if (0 < percentChange && percentChange< 1){
			percentChange = (1 - percentChange)*100;
			String percentString = Double.toString(percentChange);
			if (percentString.indexOf(".")!=-1){
				return "跌"+percentString.substring(0, percentString.indexOf(".")+2)+"%";
			}else {
				return "跌"+percentString+"%";
			}
		}else {
			return " ~ " + "%";
		}
	}

}
