package com.kosbrother.housefinder.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.housefinder.AppConstants;
import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.DetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.housefinder.Setting;
import com.kosbrother.housefinder.api.InfoParserApi;
import com.kosbrother.housefinder.entity.RealEstate;

public class BreiefFragment extends Fragment
{

	private TableLayout detailTableLayout;
	private LinearLayout layoutBrief;
	private View layoutDetailView;
	private int mPosition;

	private TextView textEstateItemNums;
	private TextView textEstateSquarePrice;
	private TextView textSquarePriceChange;
	
	private int rowHight = 0; 
	
	// private static BreiefFragment mBreiefFragment;
	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;
	
	
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
		layoutBrief = (LinearLayout) v.findViewById(R.id.layout_breif);
		layoutDetailView = v.findViewById(R.id.layout_detail);

		textEstateItemNums = (TextView) v.findViewById(R.id.text_estate_item_num);
		textEstateSquarePrice = (TextView) v.findViewById(R.id.text_estate_square_price);
		textSquarePriceChange = (TextView) v.findViewById(R.id.text_square_price_change);

		detailTableLayout = (TableLayout) v.findViewById(R.id.detail_talble);
		Bundle bundle = getArguments();
		mPosition = bundle.getInt("num");
		
		adBannerLayout = (RelativeLayout) v.findViewById(R.id.adLayout);
		
//		CallAds();
		
		setBriefViews();
		addDetailViews();
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}
	
	private void CallAds()
	{

		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar, getActivity());
		
		if (!isGivenStar)
		{
			final AdRequest adReq = new AdRequest.Builder().build();

			// 12-18 17:01:12.438: I/Ads(8252): Use
			// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
			// to get test ads on this device.

			adMobAdView = new AdView(getActivity());
			adMobAdView.setAdSize(AdSize.SMART_BANNER);
			adMobAdView.setAdUnitId(AppConstants.MEDIATION_KEY);

			adMobAdView.loadAd(adReq);
			adMobAdView.setAdListener(new AdListener()
			{
				@Override
				public void onAdLoaded() {
					adBannerLayout.setVisibility(View.VISIBLE);
					if (adBannerLayout.getChildAt(0)!=null)
					{
						adBannerLayout.removeViewAt(0);
					}
					adBannerLayout.addView(adMobAdView);
				}
				
				public void onAdFailedToLoad(int errorCode) {
					adBannerLayout.setVisibility(View.GONE);
				}
				
			});	
		}
		
	}
	
	
//	public boolean changeToDetailView()
//	{
//		if (layoutBrief.getVisibility() == View.VISIBLE)
//		{
//			layoutBrief.setVisibility(View.GONE);
//			layoutDetailView.setVisibility(View.VISIBLE);
//			return true;
//		}else {
//			layoutBrief.setVisibility(View.VISIBLE);
//			layoutDetailView.setVisibility(View.GONE);
//			return false;
//		}		
//	}
	
//	public void showBrief(){
//		layoutBrief.setVisibility(View.VISIBLE);
//		layoutDetailView.setVisibility(View.GONE);
//	}
//	
//	public void showDetail(){
//		layoutBrief.setVisibility(View.VISIBLE);
//		layoutDetailView.setVisibility(View.GONE);
//	}
	
	public void addDetailViews()
	{	
	
		detailTableLayout.removeAllViews();
		ArrayList<RealEstate> theEstates = new ArrayList<RealEstate>();
		theEstates = Datas.mEstatesMap.get(Datas.getKeyByPosition(mPosition));

		for (int i = 0; i < theEstates.size(); i++)
		{

			final TableRow newTableRow = new TableRow(getActivity());
			newTableRow.setGravity(Gravity.CENTER_HORIZONTAL);
			// newTableRow.setWeightSum(7);
			if (rowHight == 0)
			{
				rowHight =(int) AppConstants.convertDpToPixel(36, getActivity());
			}
//			
//			TableRow.LayoutParams tlparams = new TableRow.LayoutParams(
//					TableRow.LayoutParams.WRAP_CONTENT, rowHight);
//			newTableRow.setLayoutParams(tlparams);
			
			String year = Integer.toString(theEstates.get(i).exchange_date/100);
			String month = Integer.toString(theEstates.get(i).exchange_date % 100);

			TextView tDateView = new TextView(getActivity());
			tDateView.setText(year + "/" + month);
			tDateView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tDateView.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.addView(tDateView);

			int groundTypeId = theEstates.get(i).ground_type_id;
			String groundType = InfoParserApi.parseGroundType(groundTypeId);
			TextView tBuyTypeView = new TextView(getActivity());
			tBuyTypeView.setText(groundType);
			tBuyTypeView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tBuyTypeView.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.addView(tBuyTypeView);

			int buildingTypeId = theEstates.get(i).building_type_id;
			String buildType = InfoParserApi.parseBuildingType(buildingTypeId);
			TextView tBuildingView = new TextView(getActivity());
			tBuildingView.setText(buildType);
			tBuildingView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tBuildingView.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.addView(tBuildingView);

			TextView tTotalPriceView = new TextView(getActivity());
			tTotalPriceView.setText(Integer.toString(theEstates.get(i).total_price));
			tTotalPriceView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tTotalPriceView.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.addView(tTotalPriceView);

			TextView tSquarePriceView = new TextView(getActivity());
			tSquarePriceView.setText(Double.toString(theEstates.get(i).square_price));
			tSquarePriceView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tSquarePriceView.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.addView(tSquarePriceView);

			TextView tArea = new TextView(getActivity());
			tArea.setText(Double.toString(theEstates.get(i).total_area));
			tArea.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tArea.setLayoutParams(new TableRow.LayoutParams(0, rowHight, 1f));
			newTableRow.setTag(i);
			newTableRow.addView(tArea);
			
			if (i%2 == 1)
			{	
				newTableRow.setBackgroundResource(R.drawable.table_row_odd_selector);
//				newTableRow.setBackground(getResources().getDrawable(R.drawable.table_row_odd_selector));
			}else {
				newTableRow.setBackgroundResource(R.drawable.table_row_even_selector);
//				newTableRow.setBackground(getResources().getDrawable(R.drawable.table_row_even_selector));
			}
			

			newTableRow.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// Toast.makeText(getActivity(), "row click",
					// Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra("MonthKey", Datas.getKeyByPosition(mPosition));
					intent.putExtra("RowNumber", Integer.valueOf(newTableRow.getTag().toString()));
					intent.setClass(getActivity(), DetailActivity.class);
					startActivity(intent);
				}
			});

			// TextView tRoomsView = new TextView(getActivity());
			// tRoomsView.setText("3/2/1");
			// tRoomsView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
			// tRoomsView.setLayoutParams(new TableRow.LayoutParams(0, 40, 1f));
			// newTableRow.addView(tRoomsView);

			detailTableLayout.addView(newTableRow);

		}
	}

	public void setBriefViews()
	{
		// get data, and last month data
		// ArrayList<RealEstate> theEstates = new ArrayList<RealEstate>();
		// theEstates = Datas.mEstatesMap.get(Datas.mArrayKey.get(mPosition));
		
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
