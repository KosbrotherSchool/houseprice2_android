package com.kosbrother.housefinder.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.DetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.housefinder.api.InfoParserApi;
import com.kosbrother.housefinder.entity.RealEstate;
import com.kosbrother.imageloader.ImageLoader;

public class DetailListAdapter extends BaseAdapter
{

	private final Activity activity;
	private final ArrayList<RealEstate> theEstates;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private int mPosition;

	public DetailListAdapter(Activity a, ArrayList<RealEstate> d,
			int monthPosition)
	{
		activity = a;
		theEstates = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext(), 70);
		mPosition = monthPosition;
	}

	public int getCount()
	{
		return theEstates.size();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item_detail, null);

		TextView detail_time = (TextView) vi
				.findViewById(R.id.detail_time_textview);
		TextView detail_exchange = (TextView) vi
				.findViewById(R.id.detail_exchange_textview);
		TextView detail_building_type = (TextView) vi
				.findViewById(R.id.detail_building_type_textview);
		TextView detail_total_price = (TextView) vi
				.findViewById(R.id.detail_total_price_textview);
		TextView detail_square_price = (TextView) vi
				.findViewById(R.id.detail_square_price_textview);
		TextView detail_total_area = (TextView) vi
				.findViewById(R.id.detail_total_are_textview);

		String year = Integer
				.toString(theEstates.get(position).exchange_date / 100);
		String month = Integer
				.toString(theEstates.get(position).exchange_date % 100);
		detail_time.setText(year + "/" + month);

		int groundTypeId = theEstates.get(position).ground_type_id;
		String groundType = InfoParserApi.parseGroundType(groundTypeId);
		detail_exchange.setText(groundType);

		int buildingTypeId = theEstates.get(position).building_type_id;
		String buildType = InfoParserApi.parseBuildingType(buildingTypeId);
		detail_building_type.setText(buildType);

		detail_total_price
				.setText(Integer.toString(theEstates.get(position).total_price));
		detail_square_price
				.setText(Double.toString(theEstates.get(position).square_price));
		detail_total_area
				.setText(Double.toString(theEstates.get(position).total_area));

		if (position % 2 == 1)
		{
			vi.setBackgroundResource(R.drawable.table_row_odd_selector);

		} else
		{
			vi.setBackgroundResource(R.drawable.table_row_even_selector);

		}

		// imageLoader.DisplayImage(data.get(position).getThumbnail(), image);

		// vi.setClickable(true);
		// vi.setFocusable(true);
//		vi.setBackgroundResource(android.R.drawable.menuitem_background);
		vi.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent intent = new Intent();
				intent.putExtra("MonthKey", Datas.getKeyByPosition(mPosition));
				intent.putExtra("RowNumber", Integer.valueOf(position));
				intent.setClass(activity, DetailActivity.class);
				activity.startActivity(intent);

			}
		});

		return vi;
	}
}
