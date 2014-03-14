package com.kosbrother.houseprice;

import java.util.ArrayList;
import java.util.TreeMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.entity.RealEstate;
import com.kosbrother.houseprice.fragment.BreiefFragment;

public class ListActivity extends FragmentActivity
{
	int NUM_ITEMS;
	MyAdapter mAdapter;
	ViewPager mPager;
	private ActionBar mActionBar;
	private TextView yearMonthTextView;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	private Button dateButton;
	private Button distanceButton;
	private ImageButton previousImageButton;
	private ImageButton nextImageButton;
	
	private LinearLayout yearMonthLinearLayout;
	private LinearLayout titleLinearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actiity_list);
		
		if (Datas.mEstatesMap == null)
		{
			Toast.makeText(ListActivity.this, "無資料!", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		yearMonthLinearLayout = (LinearLayout) findViewById(R.id.year_month_linear_layout);
		titleLinearLayout = (LinearLayout) findViewById(R.id.title_linear_layout);
		yearMonthLinearLayout.setVisibility(View.VISIBLE);
		titleLinearLayout.setVisibility(View.INVISIBLE);
		previousImageButton = (ImageButton) findViewById(R.id.button_previous);
		nextImageButton = (ImageButton) findViewById(R.id.button_next);
		
		mPager = (ViewPager) findViewById(R.id.pager);
		yearMonthTextView = (TextView) findViewById(R.id.text_year_month);
		NUM_ITEMS = Datas.mArrayKey.size();
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(NUM_ITEMS - 1);
		yearMonthTextView.setText(makeYearMonthString(Datas
				.getKeyByPosition(NUM_ITEMS - 1)));
		
		
		previousImageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mPager.getCurrentItem() == 0)
				{
					Toast.makeText(ListActivity.this, "無上月資料,請設定搜索時間", Toast.LENGTH_SHORT).show();
				}else {
					mPager.setCurrentItem(mPager.getCurrentItem()-1);
				}

			}
		});

		nextImageButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mPager.getCurrentItem() == mAdapter.getCount() -1)
				{
					Toast.makeText(ListActivity.this, "無下月資料", Toast.LENGTH_SHORT).show();
				}else {
					mPager.setCurrentItem(mPager.getCurrentItem()+1);
				}

			}
		});
		
		
		distanceButton = (Button) findViewById(R.id.distance_button);
		distanceButton.setText(Double.toString(AppConstants.km_dis) + "km");
		distanceButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showSelectDistanceDialog();
			}
		});

		dateButton = (Button) findViewById(R.id.button_date);
		setDateButtonText();
		dateButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				showDateDialog();
			}

			private void showDateDialog()
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						ListActivity.this);

				LayoutInflater inflater = ListActivity.this.getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_set_date, null);
				final EditText startYearEditText = (EditText) layout
						.findViewById(R.id.start_year_edittext);
				final EditText startMonthEditText = (EditText) layout
						.findViewById(R.id.start_month_edittext);
				final EditText endYearEditText = (EditText) layout
						.findViewById(R.id.end_year_edittext);
				final EditText endMonthEditText = (EditText) layout
						.findViewById(R.id.end_month_edittext);

				int startYear = AppConstants.startDate / 100;
				int startMonth = AppConstants.startDate % 100;

				startYearEditText.setHint(Integer.toString(startYear));
				startMonthEditText.setHint(Integer.toString(startMonth));

				int endYear = AppConstants.endDate / 100;
				int endMonth = AppConstants.endDate % 100;

				endYearEditText.setHint(Integer.toString(endYear));
				endMonthEditText.setHint(Integer.toString(endMonth));

				dialog.setTitle("選擇搜索日期");
				dialog.setView(layout);
				dialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{

							}
						});
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{

								int startYear = AppConstants.startDate / 100;
								try
								{
									startYear = Integer
											.valueOf(startYearEditText
													.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int startMonth = AppConstants.startDate % 100;
								try
								{
									startMonth = Integer
											.valueOf(startMonthEditText
													.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int endYear = AppConstants.endDate / 100;
								try
								{
									endYear = Integer.valueOf(endYearEditText
											.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int endMonth = AppConstants.endDate % 100;
								try
								{
									endMonth = Integer.valueOf(endMonthEditText
											.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								if (startYear < 96)
								{
									Toast.makeText(ListActivity.this,
											"起始年不可低於96年", Toast.LENGTH_SHORT)
											.show();
								} else if ((startYear * 100 + startMonth) > (endYear * 100 + endMonth))
								{
									Toast.makeText(ListActivity.this,
											"起始期間不可高於結束期間", Toast.LENGTH_SHORT)
											.show();
								} else
								{
									AppConstants.startDate = startYear * 100
											+ startMonth;
									AppConstants.endDate = endYear * 100
											+ endMonth;
									// Toast.makeText(MainActivity.this,
									// Integer.toString(startDate),
									// Toast.LENGTH_SHORT).show();
									dateButton.setText(Integer
											.toString(startYear)
											+ "/"
											+ Integer.toString(startMonth)
											+ "~"
											+ Integer.toString(endYear)
											+ "/" + Integer.toString(endMonth));
									maekKeyArray();
									getLocation(false, 1);
								}

							}
						});
				dialog.show();
			}
		});

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);

		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int current_position)
			{
				yearMonthTextView.setText(makeYearMonthString(Datas
						.getKeyByPosition(current_position)));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});

		 CallAds();
	}

	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void setDateButtonText()
	{
		int startYear = AppConstants.startDate / 100;
		int startMonth = AppConstants.startDate % 100;

		int endYear = AppConstants.endDate / 100;
		int endMonth = AppConstants.endDate % 100;

		dateButton.setText(Integer.toString(startYear) + "/"
				+ Integer.toString(startMonth) + "~"
				+ Integer.toString(endYear) + "/" + Integer.toString(endMonth));

	}

	private void getLocation(Boolean isReGetLoc, int aniParam)
	{

		if (NetworkUtil.getConnectivityStatus(ListActivity.this) == 0)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ListActivity.this);
			dialog.setTitle("無網路");
			dialog.setMessage("偵測不到網路");
			dialog.setPositiveButton("確定",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialoginterface,
								int i)
						{
							getLocation(true, 0);
						}
					});
			dialog.show();
		} else
		{
			new GetEstatesTask().execute();

		}

	}

	protected class GetEstatesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yearMonthLinearLayout.setVisibility(View.INVISIBLE);
			titleLinearLayout.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected Void doInBackground(Void... Void)
		{
			try
			{
				Datas.mEstates.clear();
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String hpMinString = Setting.getSetting(Setting.keyHousePriceMin,
					ListActivity.this);
			if (hpMinString.equals("0"))
			{
				hpMinString = null;
			}
			String hpMaxString = Setting.getSetting(Setting.keyHousePriceMax,
					ListActivity.this);
			if (hpMaxString.equals("0"))
			{
				hpMaxString = null;
			}
			String areaMinString = Setting.getSetting(Setting.keyAreaMin,
					ListActivity.this);
			if (areaMinString.equals("0"))
			{
				areaMinString = null;
			}
			String areaMaxString = Setting.getSetting(Setting.keyAreaMax,
					ListActivity.this);
			if (areaMaxString.equals("0"))
			{
				areaMaxString = null;
			}
			String groundTypeString = Setting.getSetting(Setting.keyGroundType,
					ListActivity.this);
			if (groundTypeString.equals("0"))
			{
				groundTypeString = null;
			}
			String buildingTypeString = Setting.getSetting(
					Setting.keyBuildingType, ListActivity.this);
			if (buildingTypeString.equals("0"))
			{
				buildingTypeString = null;
			}

			Datas.mEstates = HouseApi.getAroundAllByAreas(AppConstants.km_dis,
					AppConstants.currentLatLng.longitude,
					AppConstants.currentLatLng.latitude,
					AppConstants.startDate, AppConstants.endDate, hpMinString,
					hpMaxString, areaMinString, areaMaxString,
					groundTypeString, buildingTypeString);

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			
			MainActivity.isResetView = true;
			
			if (Datas.mEstates != null && Datas.mEstates.size() != 0)
			{
				
				
				Datas.mEstatesMap = getRealEstatesMap(Datas.mEstates);

				// set new pager adapter
				NUM_ITEMS = Datas.mArrayKey.size();
				mAdapter = new MyAdapter(getSupportFragmentManager());
				mPager.setAdapter(mAdapter);
				mPager.setCurrentItem(NUM_ITEMS - 1);
				yearMonthTextView.setText(makeYearMonthString(Datas
						.getKeyByPosition(NUM_ITEMS - 1)));

			} else
			{
				Datas.mEstates = new ArrayList<RealEstate>();
				Datas.mEstatesMap = getRealEstatesMap(Datas.mEstates);
				NUM_ITEMS = Datas.mArrayKey.size();
				mAdapter = new MyAdapter(getSupportFragmentManager());
				mPager.setAdapter(mAdapter);
				mPager.setCurrentItem(NUM_ITEMS - 1);
				yearMonthTextView.setText(makeYearMonthString(Datas
						.getKeyByPosition(NUM_ITEMS - 1)));
				
				Toast.makeText(ListActivity.this, "無資料~", Toast.LENGTH_SHORT)
						.show();
				// titleTextView.setText("無資料~");
			}
			
			yearMonthLinearLayout.setVisibility(View.VISIBLE);
			titleLinearLayout.setVisibility(View.INVISIBLE);

		}

	}

	private TreeMap<String, ArrayList<RealEstate>> getRealEstatesMap(
			ArrayList<RealEstate> realEstates)
	{

		TreeMap<String, ArrayList<RealEstate>> estateMap = new TreeMap<String, ArrayList<RealEstate>>();
		for (int i = 0; i < realEstates.size(); i++)
		{
			RealEstate realEstate = realEstates.get(i);
			String realEstateKey = Integer.toString(realEstate.exchange_date);
			// 先確認key是否存在
			if (estateMap.containsKey(realEstateKey))
			{
				// 已經有的話就把movie加進去
				((ArrayList<RealEstate>) estateMap.get(realEstateKey))
						.add(realEstate);
			} else
			{
				// 沒有的話就建一個加進去
				ArrayList<RealEstate> newRealEstateList = new ArrayList<RealEstate>(
						10);
				newRealEstateList.add(realEstate);
				estateMap.put(realEstateKey, newRealEstateList);
			}
		}
		return estateMap;
	}

	private void maekKeyArray()
	{
		Datas.mArrayKey.clear();
		int startYear = AppConstants.startDate / 100;
		int startMonth = AppConstants.startDate % 100;
		int endYear = AppConstants.endDate / 100;
		int endMonth = AppConstants.endDate % 100;

		int num = (endYear - startYear) * 12 + (endMonth - startMonth) + 1;

		for (int i = 0; i < num; i++)
		{
			String key = Integer.toString(endYear * 100 + endMonth);
			Datas.mArrayKey.add(key);
			if ((endMonth - 1) != 0)
			{
				endMonth = endMonth - 1;
			} else
			{
				endYear = endYear - 1;
				endMonth = 12;
			}
		}

	}

	private String makeYearMonthString(String key)
	{
		int yearMonth = Integer.valueOf(key);
		int year = yearMonth / 100;
		int month = yearMonth % 100;
		return Integer.toString(year) + "/" + Integer.toString(month);

	}

	public class MyAdapter extends FragmentStatePagerAdapter
	{
		public MyAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position)
		{
			return BreiefFragment.newInstance(position);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		MainActivity.isReSearch = false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			MainActivity.isReSearch = false;
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		// getSupportMenuInflater().inflate(R.menu.detail, menu);
		return super.onCreateOptionsMenu(menu);
	}


	private void showSelectDistanceDialog()
	{

		AlertDialog.Builder editDialog = new AlertDialog.Builder(
				ListActivity.this);
		editDialog.setTitle("選取搜索範圍");

		// final EditText editText = new EditText(ArticleActivity.this);
		// editDialog.setView(editText);

		LayoutInflater inflater = LayoutInflater.from(ListActivity.this);
		View distance_view = inflater.inflate(R.layout.dialog_select_distance,
				null);
		final TextView textDistance = (TextView) distance_view
				.findViewById(R.id.text_distance);
		SeekBar seekBarDistance = (SeekBar) distance_view
				.findViewById(R.id.seekbar_distance);

		textDistance.setText(Double.toString(AppConstants.km_dis) + "km");
		int pp = (int) (AppConstants.km_dis / 0.03);
		seekBarDistance.setProgress(pp);

		seekBarDistance
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
				{

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser)
					{
						// TODO Auto-generated method stub
						double d = progress * 0.03;
						String d_String = Double.toString(d).substring(0, 3);

						textDistance.setText(d_String + "km");
						AppConstants.km_dis = Double.valueOf(d_String);
					}
				});

		editDialog.setView(distance_view);

		editDialog.setPositiveButton("確定",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						if (AppConstants.km_dis != 0)
						{
							distanceButton.setText(Double
									.toString(AppConstants.km_dis) + "km");
							Setting.saveSetting(Setting.keyKmDistance,
									Double.toString(AppConstants.km_dis),
									ListActivity.this);
							getLocation(false, 0);
						} else
						{
							Toast.makeText(ListActivity.this, "半徑不能為0",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		editDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
					}
				});
		editDialog.show();
	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar,
				ListActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			// 12-18 17:01:12.438: I/Ads(8252): Use
			// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
			// to get test ads on this device.

			adMobAdView = new AdView(ListActivity.this);
			adMobAdView.setAdSize(AdSize.SMART_BANNER);
			adMobAdView.setAdUnitId(AppConstants.MEDIATION_KEY);

			adMobAdView.loadAd(adReq);
			adMobAdView.setAdListener(new AdListener()
			{
				@Override
				public void onAdLoaded()
				{
					adBannerLayout.setVisibility(View.VISIBLE);
					if (adBannerLayout.getChildAt(0) != null)
					{
						adBannerLayout.removeViewAt(0);
					}
					adBannerLayout.addView(adMobAdView);
				}

				public void onAdFailedToLoad(int errorCode)
				{
					adBannerLayout.setVisibility(View.GONE);
				}

			});
		}
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

}
