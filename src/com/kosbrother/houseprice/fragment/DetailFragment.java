package com.kosbrother.houseprice.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.houseprice.Datas;
import com.kosbrother.houseprice.DetailActivity;
import com.kosbrother.houseprice.R;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.BuildingData;
import com.kosbrother.houseprice.entity.LandData;
import com.kosbrother.houseprice.entity.ParkingData;
import com.kosbrother.houseprice.entity.RealEstate;
import com.kosbrother.imageloader.ImageLoader;

public class DetailFragment extends Fragment
{
	int mNum;
	String mMonthKey;
	private ImageLoader imageLoader;
	private static DetailActivity mActivity;
	private RealEstate theEstate;
	private boolean isGotData = true;
	private int text_size = 13;

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
	public static DetailFragment newInstance(int num, String month_key,
			DetailActivity theDetailActivity)
	{
		DetailFragment f = new DetailFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("month_key", month_key);
		f.setArguments(args);
		mActivity = theDetailActivity;

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		mMonthKey = getArguments() != null ? getArguments().getString(
				"month_key") : Datas.mArrayKey.get(0);
		imageLoader = new ImageLoader(getActivity(), 100);
		if (mMonthKey.equals(""))
		{
			theEstate = Datas.mEstates.get(mNum);
		} else
		{
			theEstate = Datas.mEstatesMap.get(mMonthKey).get(mNum);
		}

		new GetEstatesTask().execute();
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */

	private ImageView image;

	// trade detail
	private TextView text_address;
	private TextView text_date;
	private TextView text_estate_type;
	private TextView text_content_buy;
	private TextView text_ground_exchange_area;
	private TextView text_building_rooms;
	private TextView text_buy_per_square_feet;
	private TextView text_buy_total_price;
	private TextView text_buiding_type;
	private LinearLayout buildingRoomsLayout;
	private LinearLayout detailEstateContentLayout;

	// ground detail
	private LinearLayout groundDetailLayout;

	// building detail
	private LinearLayout buildingDetailLayout;

	// parking detail
	private LinearLayout parkingDetailLayout;

	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_detail_pager, container,
				false);

		image = (ImageView) v.findViewById(R.id.imageview_detail);

		// trade detail
		text_address = (TextView) v.findViewById(R.id.text_detail_address);
		text_date = (TextView) v.findViewById(R.id.text_detail_date);
		text_estate_type = (TextView) v
				.findViewById(R.id.text_detail_estate_type);
		text_content_buy = (TextView) v
				.findViewById(R.id.text_detail_content_buy);
		text_ground_exchange_area = (TextView) v
				.findViewById(R.id.text_detail_ground_exchange_area);
		text_building_rooms = (TextView) v
				.findViewById(R.id.text_detail_building_rooms);
		text_buy_per_square_feet = (TextView) v
				.findViewById(R.id.text_detail_buy_per_square_feet);
		text_buy_total_price = (TextView) v
				.findViewById(R.id.text_detail_buy_total_price);
		text_buiding_type = (TextView) v
				.findViewById(R.id.text_detail_building_type);
		buildingRoomsLayout = (LinearLayout) v
				.findViewById(R.id.rooms_linear_layout);
		detailEstateContentLayout = (LinearLayout) v
				.findViewById(R.id.detail_estate_content_layout);

		groundDetailLayout = (LinearLayout) v.findViewById(R.id.layout_ground);
		buildingDetailLayout = (LinearLayout) v
				.findViewById(R.id.layout_building);
		parkingDetailLayout = (LinearLayout) v
				.findViewById(R.id.layout_parking);

		// set image
		String x_long = Double.toString(theEstate.x_long);
		String y_lat = Double.toString(theEstate.y_lat);
		String url = "http://maps.google.com/maps/api/staticmap?center="
				+ y_lat + "," + x_long
				+ "&zoom=17&markers=color:red%7Clabel:%7C" + y_lat + ","
				+ x_long + "&size=400x150&language=zh-TW&sensor=false";
		imageLoader.DisplayImage(url, image);

		return v;
	}

	private class GetEstatesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// show progress dialog
			// mProgressDialog = ProgressDialog.show(mActivity, null,
			// "Ë≥áÊñôÂÇ≥ÈÅû‰∏≠");
			// mProgressDialog.setCancelable(true);
		}

		@Override
		protected Void doInBackground(Void... arg)
		{
			// check if already got data
			boolean isNeedRun = true;
			for (int i = 0; i < Datas.mDetailEstates.size(); i++)
			{
				RealEstate tEstate = Datas.mDetailEstates.get(i);
				if (theEstate.estate_id == tEstate.estate_id)
				{
					isNeedRun = false;
				}
			}
			if (isNeedRun)
			{
				isGotData = HouseApi.getEstateDetails(theEstate.estate_id);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void arg)
		{
			super.onPostExecute(null);

			if (isGotData)
			{
				if (isAdded())
				{

					detailEstateContentLayout.setVisibility(View.VISIBLE);
					for (int i = 0; i < Datas.mDetailEstates.size(); i++)
					{
						RealEstate tEstate = Datas.mDetailEstates.get(i);
						if (theEstate.estate_id == tEstate.estate_id)
						{
							theEstate = tEstate;
						}
					}

					text_address.setText(theEstate.estate_address);
					int year = theEstate.exchange_date / 100;
					int month = theEstate.exchange_date % 100;
					text_date.setText(Integer.toString(year) + "/"
							+ Integer.toString(month));
					text_estate_type.setText(InfoParserApi
							.parseGroundType(theEstate.ground_type_id));
					text_buiding_type.setText(InfoParserApi
							.parseBuildingType(theEstate.building_type_id));

					text_content_buy.setText(theEstate.exchange_content);

					text_ground_exchange_area.setText(Double
							.toString(theEstate.total_area) + "坪");

					if (theEstate.building_rooms.equals(""))
					{
						buildingRoomsLayout.setVisibility(View.GONE);
					} else
					{
						text_building_rooms.setText(theEstate.building_rooms);
					}

					text_buy_per_square_feet.setText(Double
							.toString(theEstate.square_price) + "萬");
					text_buy_total_price.setText(Integer
							.toString(theEstate.total_price) + "萬");

					if (Datas.mLandDatas.size() == 0)
					{
						TextView noDataTextView = new TextView(getActivity());
						noDataTextView.setText("無資料");
						noDataTextView.setTextSize(text_size);
						noDataTextView.setTextColor(getResources().getColor(
								R.color.white));
						groundDetailLayout.addView(noDataTextView);
					} else
					{
						Boolean isHasData = false;
						int num = 0;
						for (int i = 0; i < Datas.mLandDatas.size(); i++)
						{
							LandData theLandData = Datas.mLandDatas.get(i);
							if (theLandData.estate_id == theEstate.estate_id)
							{
								num = num + 1;
								isHasData = true;
								TextView noDataTextView = new TextView(
										getActivity());
								noDataTextView.setText("第"
										+ Integer.valueOf(num) + "筆資料");
								noDataTextView.setTextSize(text_size);
								noDataTextView.setTextColor(getResources()
										.getColor(R.color.light_blue));
								groundDetailLayout.addView(noDataTextView);
								addLandData(theLandData);

							}
						}
						if (!isHasData)
						{
							TextView noDataTextView = new TextView(
									getActivity());
							noDataTextView.setText("無資料");
							noDataTextView.setTextSize(text_size);
							noDataTextView.setTextColor(getResources()
									.getColor(R.color.white));
							groundDetailLayout.addView(noDataTextView);
						}
					}

					if (Datas.mBuildingDatas.size() == 0)
					{
						TextView noDataTextView = new TextView(getActivity());
						noDataTextView.setText("無資料");
						noDataTextView.setTextSize(text_size);
						noDataTextView.setTextColor(getResources().getColor(
								R.color.white));
						buildingDetailLayout.addView(noDataTextView);

					} else
					{
						Boolean isHasData = false;
						int num = 0;
						for (int i = 0; i < Datas.mBuildingDatas.size(); i++)
						{
							BuildingData theBuildingData = Datas.mBuildingDatas
									.get(i);
							if (theBuildingData.estate_id == theEstate.estate_id)
							{
								num = num + 1;
								isHasData = true;
								TextView noDataTextView = new TextView(
										getActivity());
								noDataTextView.setText("第"
										+ Integer.valueOf(num) + "筆資料");
								noDataTextView.setTextSize(text_size);
								noDataTextView.setTextColor(getResources()
										.getColor(R.color.light_blue));
								buildingDetailLayout.addView(noDataTextView);
								addBuildingData(theBuildingData);
							}
						}
						if (!isHasData)
						{
							TextView noDataTextView = new TextView(
									getActivity());
							noDataTextView.setText("無資料");
							noDataTextView.setTextSize(text_size);
							noDataTextView.setTextColor(getResources()
									.getColor(R.color.white));
							buildingDetailLayout.addView(noDataTextView);
						}
					}

					if (Datas.mParkingDatas.size() == 0)
					{
						TextView noDataTextView = new TextView(getActivity());
						noDataTextView.setText("無資料");
						noDataTextView.setTextSize(text_size);
						noDataTextView.setTextColor(getResources().getColor(
								R.color.white));
						parkingDetailLayout.addView(noDataTextView);
					} else
					{
						Boolean isHasData = false;
						int num = 0;
						for (int j = 0; j < Datas.mParkingDatas.size(); j++)
						{
							ParkingData theParkingData = Datas.mParkingDatas
									.get(j);
							if (theParkingData.estate_id == theEstate.estate_id)
							{
								num = num + 1;
								isHasData = true;
								TextView noDataTextView = new TextView(
										getActivity());
								noDataTextView.setText("第"
										+ Integer.valueOf(num) + "筆資料");
								noDataTextView.setTextSize(text_size);
								noDataTextView.setTextColor(getResources()
										.getColor(R.color.light_blue));
								parkingDetailLayout.addView(noDataTextView);
								addParkingData(theParkingData);
							}
						}
						if (!isHasData)
						{
							TextView noDataTextView = new TextView(
									getActivity());
							noDataTextView.setText("無資料");
							noDataTextView.setTextSize(text_size);
							noDataTextView.setTextColor(getResources()
									.getColor(R.color.white));
							parkingDetailLayout.addView(noDataTextView);
						}
					}
				}

			} else
			{
				Toast.makeText(getActivity(), "Detail Data Wrong",
						Toast.LENGTH_SHORT);
			}

		}
	}

	private void addParkingData(ParkingData theParkingData)
	{
		TextView t_type = new TextView(getActivity());
		t_type.setText("車位類別:" + theParkingData.parking_type);
		t_type.setTextSize(text_size);
		t_type.setTextColor(getResources().getColor(R.color.white));
		parkingDetailLayout.addView(t_type);

		TextView t_price = new TextView(getActivity());
		t_price.setText("車位價格:" + theParkingData.parking_price);
		t_price.setTextSize(text_size);
		t_price.setTextColor(getResources().getColor(R.color.white));
		parkingDetailLayout.addView(t_price);

		TextView t_area = new TextView(getActivity());
		t_area.setText("車位面積:" + theParkingData.parking_area);
		t_area.setTextSize(text_size);
		t_area.setTextColor(getResources().getColor(R.color.white));
		parkingDetailLayout.addView(t_area);
	}

	private void addLandData(LandData theLandData)
	{
		TextView t_position = new TextView(getActivity());
		t_position.setText("區域路段:" + theLandData.land_position);
		t_position.setTextSize(text_size);
		t_position.setTextColor(getResources().getColor(R.color.white));
		groundDetailLayout.addView(t_position);

		TextView t_area = new TextView(getActivity());
		t_area.setText("土地坪數:" + theLandData.land_area);
		t_area.setTextSize(text_size);
		t_area.setTextColor(getResources().getColor(R.color.white));
		groundDetailLayout.addView(t_area);

		TextView t_usage = new TextView(getActivity());
		t_usage.setText("使用分區:" + theLandData.land_usage);
		t_usage.setTextSize(text_size);
		t_usage.setTextColor(getResources().getColor(R.color.white));
		groundDetailLayout.addView(t_usage);
	}

	private void addBuildingData(BuildingData theBuildingData)
	{
		TextView t_age = new TextView(getActivity());
		t_age.setText("建物年齡:" + Integer.toString(theBuildingData.age));
		t_age.setTextSize(text_size);
		t_age.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_age);

		TextView t_area = new TextView(getActivity());
		t_area.setText("建物坪數:" + theBuildingData.building_area);
		t_area.setTextSize(text_size);
		t_area.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_area);

		TextView t_purpose = new TextView(getActivity());
		t_purpose.setText("建物目的:" + theBuildingData.building_purpose);
		t_purpose.setTextSize(text_size);
		t_purpose.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_purpose);

		TextView t_material = new TextView(getActivity());
		t_material.setText("主要建材:" + theBuildingData.building_material);
		t_material.setTextSize(text_size);
		t_material.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_material);

		TextView t_date = new TextView(getActivity());
		t_date.setText("完成年月日:" + theBuildingData.built_date);
		t_date.setTextSize(text_size);
		t_date.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_date);

		TextView t_layer = new TextView(getActivity());
		t_layer.setText("樓層:" + theBuildingData.building_layer + "/"
				+ theBuildingData.building_total_layer);
		t_layer.setTextSize(text_size);
		t_layer.setTextColor(getResources().getColor(R.color.white));
		buildingDetailLayout.addView(t_layer);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

}
