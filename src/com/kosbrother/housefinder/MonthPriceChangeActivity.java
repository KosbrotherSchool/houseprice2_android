package com.kosbrother.housefinder;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MonthPriceChangeActivity extends Activity implements
		OnNavigationListener
{

	private LinearLayout monthItemLayout;
	private LayoutInflater mInflater;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_change);
		
		monthItemLayout = (LinearLayout) findViewById(R.id.month_item_layout);
		mInflater = getLayoutInflater();
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayList<String> itemList = new ArrayList<String>();
		itemList.add("單價");
		itemList.add("總價");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, itemList)
		{

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);

				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				/* YOUR CHOICE OF COLOR */
				textView.setTextColor(Color.WHITE);

				return view;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);

				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				/* YOUR CHOICE OF COLOR */
				textView.setTextColor(Color.WHITE);

				return view;
			}
		};

		actionBar.setListNavigationCallbacks(adapter, this);

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId)
	{
		switch (itemPosition)
		{
		case 0:
			Toast.makeText(MonthPriceChangeActivity.this, "0",
					Toast.LENGTH_SHORT).show();
			setSquarePriceView();
			break;
		case 1:
			Toast.makeText(MonthPriceChangeActivity.this, "1",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return false;
	}

	private void setSquarePriceView()
	{
		monthItemLayout.removeAllViews();
		for (int i = 0; i < Datas.mArrayKey.size(); i++)
		{
			View view = mInflater.inflate(R.layout.item_month_square_price, null);
			TextView monthYearTextView = (TextView) view.findViewById(R.id.price_month_year_text);
			TextView priceHighTextView = (TextView) view.findViewById(R.id.price_month_high_text);
			TextView priceAvgTextView = (TextView) view.findViewById(R.id.price_month_avg_text);
			TextView priceLowTextView = (TextView) view.findViewById(R.id.price_month_low_text);
			TextView priceChangeTextView = (TextView) view.findViewById(R.id.price_month_change_text);
			TextView priceQuantityTextView = (TextView) view.findViewById(R.id.price_month_quantity_text);
			monthYearTextView.setText("111");
			priceHighTextView.setText("222");
			priceAvgTextView.setText("222");
			priceLowTextView.setText("222");
			priceChangeTextView.setText("222");
			priceQuantityTextView.setText("222");
			monthItemLayout.addView(view);
		}
		
	}

}
