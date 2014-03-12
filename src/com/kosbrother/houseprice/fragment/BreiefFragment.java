package com.kosbrother.houseprice.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.houseprice.AppConstants;
import com.kosbrother.houseprice.Datas;
import com.kosbrother.houseprice.R;
import com.kosbrother.houseprice.Setting;
import com.kosbrother.houseprice.adapter.DetailListAdapter;
import com.kosbrother.houseprice.entity.RealEstate;

public class BreiefFragment extends Fragment
{

//	private TableLayout detailTableLayout;
	private ListView detailListView;
	private int mPosition;

	private TextView textEstateItemNums;
	private TextView textEstateSquarePrice;
	private TextView textSquarePriceChange;
	
	
	// private static BreiefFragment mBreiefFragment;
	
	
	public static BreiefFragment newInstance(int position)
	{
		BreiefFragment f = new BreiefFragment();
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// mBreiefFragment = this;
		View v = inflater.inflate(R.layout.fragment_breief, null);

		textEstateItemNums = (TextView) v.findViewById(R.id.text_estate_item_num);
		textEstateSquarePrice = (TextView) v.findViewById(R.id.text_estate_square_price);
		textSquarePriceChange = (TextView) v.findViewById(R.id.text_square_price_change);

		detailListView = (ListView) v.findViewById(R.id.listview_detail);
		Bundle bundle = getArguments();
		mPosition = bundle.getInt("num");
		
		setBriefViews();
		addDetailViews();
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}
	
	
	public void addDetailViews()
	{	
		ArrayList<RealEstate> theEstates = new ArrayList<RealEstate>();
		theEstates = Datas.mEstatesMap.get(Datas.getKeyByPosition(mPosition));
		DetailListAdapter newAdapter = new DetailListAdapter(getActivity(), theEstates, mPosition);
		detailListView.setAdapter(newAdapter);
	}

	public void setBriefViews()
	{
		
		String monthKey = Datas.getKeyByPosition(mPosition);

		int itemNums = Datas.getMonthEstatesNum(monthKey);
		textEstateItemNums.setText(Integer.toString(itemNums) + "筆");

		if (itemNums == 0)
		{
			textEstateSquarePrice.setText("~" + "萬");
		} else
		{
			double avgSquarePrice = Datas.getMonthAvgSquarePrice(monthKey);
			String avgSquarePriceString = Double.toString(avgSquarePrice);
			if (avgSquarePriceString.indexOf(".") != -1)
			{
				textEstateSquarePrice.setText(avgSquarePriceString.substring(0,
						avgSquarePriceString.indexOf(".") + 2) + "萬");
			} else
			{
				textEstateSquarePrice.setText(avgSquarePriceString + "萬");
			}
		}

		try
		{
			
			String lastMonthKey = Datas.getKeyByPosition(mPosition-1);
			double percentChange = Datas.getSquarePriceChange(monthKey, lastMonthKey);
			if (percentChange > 1)
			{
				percentChange = (percentChange - 1) * 100;
				String percentString = Double.toString(percentChange);
				if (percentString.indexOf(".") != -1)
				{
					textSquarePriceChange.setText("漲"
							+ percentString.substring(0, percentString.indexOf(".") + 2) + "%");
				} else
				{
					textSquarePriceChange.setText("漲" + percentString + "%");
				}
			} else if (percentChange == 0) {
				textSquarePriceChange.setText("漲0%");
			}else
			{
				percentChange = (1 - percentChange) * 100;
				String percentString = Double.toString(percentChange);
				if (percentString.indexOf(".") != -1)
				{
					textSquarePriceChange.setText("跌"
							+ percentString.substring(0, percentString.indexOf(".") + 2) + "%");
				} else
				{
					textSquarePriceChange.setText("跌" + percentString + "%");
				}
			}

		} catch (Exception e)
		{
			textSquarePriceChange.setText(" ~ " + "%");
		}

	}


	// public static BreiefFragment getBreiefFragment(){
	// return mBreiefFragment;
	// }

}
