package com.kosbrother.houseprice.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.kosbrother.houseprice.Datas;
import com.kosbrother.houseprice.entity.BuildingData;
import com.kosbrother.houseprice.entity.LandData;
import com.kosbrother.houseprice.entity.ParkingData;
import com.kosbrother.houseprice.entity.RealEstate;

public class HouseApi
{
	final static String HOST = "http://1.34.193.26";
	public static final String TAG = "HOUSE_API";
	public static final boolean DEBUG = true;

	public static ArrayList<RealEstate> getAroundAllByAreas(
			double km_dis, double center_x,
			double center_y, int start_date, int end_date, String hp_min,
			String hp_max, String area_min, String area_max,
			String groundTypeString, String buildingTypeString)
	{

		String query_link = "/api/v2/estate/get_estate_by_distance?"
				+ "km_dis=" + km_dis + "&center_x=" + center_x + "&center_y="
				+ center_y + "&start_date=" + start_date + "&end_date="
				+ end_date;

		if (hp_min != null)
		{
			query_link = query_link + "&hp_min=" + hp_min;
		}

		if (hp_max != null)
		{
			query_link = query_link + "&hp_max=" + hp_max;
		}

		if (area_min != null)
		{
			query_link = query_link + "&a_min=" + area_min;
		}

		if (area_max != null)
		{
			query_link = query_link + "&a_max=" + area_max;
		}

		if (groundTypeString != null)
		{
			query_link = query_link + "&ground_type=" + groundTypeString;
		}

		if (buildingTypeString != null)
		{
			query_link = query_link + "&building_type=" + buildingTypeString;
		}

		String message = getMessageFromServer("GET", query_link, null, null);

		ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
		if (message == null)
		{
			return null;
		} else
		{
			return parseEstateMessage(message, realEstates);
		}
	}

	public static int getCurrentCrawlDate()
	{
		String message = getMessageFromServer("GET",
				"/api/v1/estate/get_current_crawl_data", null, null);
		if (message == null)
		{
			return 0;
		} else
		{
			return parseDateMessage(message);
		}
	}

	public static boolean getEstateDetails(int estate_id)
	{
		String message = getMessageFromServer("GET",
				"/api/v2/estate/get_estate_details?estate_id=" + estate_id,
				null, null);
		if (message == null)
		{
			return false;
		} else
		{
			return parseEstateDetailMessage(message);
		}
	}

	private static boolean parseEstateDetailMessage(String message)
	{
		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());

			// for estate data
			int estate_id = jArray.getJSONObject(0).getInt("id");

			String estate_address = "";
			try
			{
				estate_address = jArray.getJSONObject(0).getString("address");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int exchange_date = 0;
			try
			{
				exchange_date = jArray.getJSONObject(0).getInt("exchange_date");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int total_price = 0;
			try
			{
				total_price = jArray.getJSONObject(0).getInt("total_price");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double square_price = 0;
			try
			{
				square_price = jArray.getJSONObject(0)
						.getDouble("square_price");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double total_area = 0;
			try
			{
				total_area = jArray.getJSONObject(0).getDouble("total_area");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String exchange_content = "";
			try
			{
				exchange_content = jArray.getJSONObject(0).getString(
						"exchange_content");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String building_type = "";
			try
			{
				building_type = jArray.getJSONObject(0).getString(
						"building_type");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String building_rooms = "";
			try
			{
				building_rooms = jArray.getJSONObject(0).getString(
						"building_rooms");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			Double x_long = jArray.getJSONObject(0).getDouble("x_long");
			Double y_lat = jArray.getJSONObject(0).getDouble("y_lat");

			int county_id = jArray.getJSONObject(0).getInt("county_id");
			int town_id = jArray.getJSONObject(0).getInt("town_id");

			int ground_type_id = 0;
			try
			{
				ground_type_id = jArray.getJSONObject(0).getInt(
						"ground_type_id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_type_id = 0;
			try
			{
				building_type_id = jArray.getJSONObject(0).getInt(
						"building_type_id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String notes = jArray.getJSONObject(0).getString("notes");

			RealEstate newEstate = new RealEstate(estate_id, 1, estate_address,
					exchange_date, total_price, square_price, total_area,
					exchange_content, building_type, building_rooms, x_long,
					y_lat, county_id, town_id, ground_type_id,
					building_type_id, notes);
			Datas.mDetailEstates.add(newEstate);

			// for land data
			JSONArray jLandArray = jArray.getJSONArray(1);
			for (int i = 0; i < jLandArray.length(); i++)
			{
				int l_estate_id = jLandArray.getJSONObject(i).getInt(
						"realestate_id");
				String l_position = jLandArray.getJSONObject(i).getString(
						"land_position");
				String l_area = jLandArray.getJSONObject(i).getString(
						"land_area");
				String l_usage = jLandArray.getJSONObject(i).getString(
						"land_usage");
				LandData newLandData = new LandData(l_estate_id, l_position,
						l_area, l_usage);
				Datas.mLandDatas.add(newLandData);
			}

			// for building data
			JSONArray jBuildingArray = jArray.getJSONArray(2);
			for (int j = 0; j < jBuildingArray.length(); j++)
			{
				int b_estate_id = jBuildingArray.getJSONObject(j).getInt(
						"realestate_id");
				int b_age = jBuildingArray.getJSONObject(j).getInt(
						"building_age");
				String b_area = jBuildingArray.getJSONObject(j).getString(
						"building_area");
				String b_purpose = jBuildingArray.getJSONObject(j).getString(
						"building_purpose");
				String b_material = jBuildingArray.getJSONObject(j).getString(
						"building_material");
				String b_built_date = jBuildingArray.getJSONObject(j)
						.getString("building_built_date");
				String b_total_layer = jBuildingArray.getJSONObject(j)
						.getString("building_total_layer");
				String b_building_layer = jBuildingArray.getJSONObject(j)
						.getString("building_layer");
				BuildingData newBuildingData = new BuildingData(b_estate_id,
						b_age, b_area, b_purpose, b_material, b_built_date,
						b_total_layer, b_building_layer);
				Datas.mBuildingDatas.add(newBuildingData);
			}

			// for parking data
			JSONArray jParkingArray = jArray.getJSONArray(3);
			for (int k = 0; k < jParkingArray.length(); k++)
			{
				int p_estate_id = jParkingArray.getJSONObject(k).getInt(
						"realestate_id");
				String p_type = jParkingArray.getJSONObject(k).getString(
						"parking_type");
				String p_price = jParkingArray.getJSONObject(k).getString(
						"parking_price");
				String p_area = jParkingArray.getJSONObject(k).getString(
						"parking_area");
				ParkingData newParkingData = new ParkingData(p_estate_id,
						p_type, p_price, p_area);
				Datas.mParkingDatas.add(newParkingData);
			}

			return true;
		} catch (Exception e)
		{
			return false;
		}
	}

	private static int parseDateMessage(String message)
	{
		try
		{
			// JSONArray jArray;
			// jArray = new JSONArray(message.toString());
			JSONObject jsonObject = new JSONObject(message.toString());
			int year = jsonObject.getInt("crawl_year");
			int month = jsonObject.getInt("crawl_month");
			return year * 100 + month;
		} catch (Exception e)
		{
			return 0;
		}

	}

	private static ArrayList<RealEstate> parseEstateMessage(String message,
			ArrayList<RealEstate> realEstates)
	{

		// AndroidDatabaseConnection connection = null;
		// Savepoint savepoint = null;
		// String pointName = "pointName";

		Log.i(TAG, "Start Parse Json:" + System.currentTimeMillis());

		// try
		// {
		// connection = new AndroidDatabaseConnection(mActivity.getHelper()
		// .getWritableDatabase(), true);
		// Dao<OrmRealEstate, Integer> estateDao = mActivity.getHelper()
		// .getOrmEsateDao();
		// estateDao.setAutoCommit(connection, false);
		// savepoint = connection.setSavePoint(pointName);

		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());
			for (int i = 0; i < jArray.length(); i++)
			{

				int id = 0;
				try
				{
					id = jArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int exchange_date = 0;
				try
				{
					exchange_date = jArray.getJSONObject(i).getInt(
							"exchange_date");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int total_price = 0;
				try
				{
					total_price = jArray.getJSONObject(i).getInt("total_price");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double square_price = 0;
				try
				{
					square_price = jArray.getJSONObject(i).getDouble(
							"square_price");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int building_type_id = 0;
				try
				{
					building_type_id = jArray.getJSONObject(i).getInt(
							"building_type_id");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int ground_type_id = 0;
				try
				{
					ground_type_id = jArray.getJSONObject(i).getInt(
							"ground_type_id");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double total_area = 0;
				try
				{
					total_area = jArray.getJSONObject(i)
							.getDouble("total_area");
				} catch (Exception e)
				{

				}

				double x_long = jArray.getJSONObject(i).getDouble("x_long");
				double y_lat = jArray.getJSONObject(i).getDouble("y_lat");

				RealEstate newEstate = new RealEstate(id, exchange_date,
						total_price, square_price, x_long, y_lat,
						building_type_id, ground_type_id, total_area);
				realEstates.add(newEstate);

				// save to database
				// OrmRealEstate newEstate = new OrmRealEstate();
				// newEstate.estate_id = id;
				// newEstate.exchange_date = exchange_date;
				// newEstate.total_price = total_price;
				// newEstate.square_price = square_price;
				// newEstate.building_type_id = building_type_id;
				// newEstate.ground_type_id = ground_type_id;
				// newEstate.total_area = total_area;
				// newEstate.x_long = x_long;
				// newEstate.y_lat = y_lat;
				// estateDao.create(newEstate);

			}

			// connection.commit(savepoint);
			// save jArray.length()

			// } catch (JSONException e)
			// {
			// e.printStackTrace();
			// return false;
			// }

		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Log.i(TAG, "End   Parse Json:" + System.currentTimeMillis() + " array ");
		return realEstates;

	}

	public static String getMessageFromServer(String requestMethod,
			String apiPath, JSONObject json, String apiUrl)
	{
		Log.i(TAG, "Start Load from server:" + System.currentTimeMillis());
		URL url;
		try
		{
			if (apiUrl != null)
				url = new URL(apiUrl);
			else
				url = new URL(HOST + apiPath);

			if (DEBUG)
				Log.d(TAG, "URL: " + url);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(requestMethod);

			connection.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");
			if (requestMethod.equalsIgnoreCase("POST"))
				connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();

			if (requestMethod.equalsIgnoreCase("POST"))
			{
				OutputStream outputStream;

				outputStream = connection.getOutputStream();
				if (DEBUG)
					Log.d("post message", json.toString());

				outputStream.write(json.toString().getBytes());
				outputStream.flush();
				outputStream.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder lines = new StringBuilder();
			;
			String tempStr;

			while ((tempStr = reader.readLine()) != null)
			{
				lines = lines.append(tempStr);
			}
			if (DEBUG)
				Log.d("MOVIE_API", lines.toString());

			reader.close();
			connection.disconnect();

			Log.i(TAG, "End Load from server:" + System.currentTimeMillis());

			return lines.toString();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
