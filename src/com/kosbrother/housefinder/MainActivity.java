package com.kosbrother.housefinder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.housefinder.api.HouseApi;
import com.kosbrother.housefinder.fragment.TransparentSupportMapFragment;

public class MainActivity extends SherlockFragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, OnMapClickListener
{

	private LocationClient mLocationClient;
	private GoogleMap mGoogleMap;

	private double km_dis;
	private double center_x;
	private double center_y;
	private float mapSize;
	private int startDate;
	private int endDate;

	private MenuItem itemSearch;
	private static final int ID_SEARCH = 5;

	private LayoutInflater inflater;
	private ImageButton btnFocusButton;
	private ImageButton btnLayerButton;
	private ImageButton btnFilterButton;
	private int currentMapTypePosition = 0;
	private LinearLayout leftDrawer;
	private ImageButton previousImageButton;
	private ImageButton nextImageButton;
	private TextView titleTextView;
	private ProgressBar titleProgressBar;
//	private Button btnDistance;
	
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	private Button dateButton;
	private Button distanceButton;
	private TextView housePriceChangeingTextView;
	private TextView housePriceLisTextView;

	private MarkerOptions loacationMarker;

	private int crawlDateNum;
	private boolean isEstatesTaskRunning = false;

	private ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();
	private int memorySize = 256;
	private int mPage = 0;
	
	public static boolean isReSearch = true;
	// private DatabaseHelper databaseHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		km_dis = Double
				.valueOf(Setting.getSetting(Setting.keyKmDistance, this));
		crawlDateNum = Setting.getCurrentDateNum(this);
		housePriceChangeingTextView = (TextView) findViewById(R.id.house_price_changing_text);
		housePriceLisTextView = (TextView) findViewById(R.id.house_price_data_list_text);
		dateButton = (Button) findViewById(R.id.button_date);
		previousImageButton = (ImageButton) findViewById(R.id.previous_img_button);
		nextImageButton = (ImageButton) findViewById(R.id.next_img_button);
		titleTextView = (TextView) findViewById(R.id.title_text);
		titleProgressBar = (ProgressBar) findViewById(R.id.title_progress);
		btnLayerButton = (ImageButton) findViewById(R.id.image_btn_layers);
		
		setStartAndEndDate(crawlDateNum);
		
		btnFocusButton = (ImageButton) findViewById(R.id.image_btn_focus);
		btnFocusButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getLocation(true, 1);
			}
		});
		
		distanceButton = (Button) findViewById(R.id.distance_button);
		distanceButton.setText(Double.toString(km_dis) + "km");
		distanceButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showSelectDistanceDialog();
			}
		});
		
		btnLayerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				// Set the dialog title
				builder.setTitle("顯示地圖").setSingleChoiceItems(R.array.map_type,
						currentMapTypePosition,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int position)
							{
								setMapTypeByPosition(position);
								currentMapTypePosition = position;
								dialog.cancel();
							}

							private void setMapTypeByPosition(int position)
							{
								switch (position)
								{
								case 0:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_NORMAL);
									break;
								case 1:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
									break;
								case 2:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_HYBRID);
									break;
								default:
									break;
								}

							}
						});
				builder.show();

			}
		});
		
		previousImageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mPage == 0)
				{
					Toast.makeText(MainActivity.this, "上頁無資料",
							Toast.LENGTH_SHORT).show();
				} else
				{
					mPage = mPage - 1;
					if (getCurrentMemory() > memorySize)
					{
						new addMarkerTask().execute();
					} else
					{	
						setTitleText(mPage);
						addCurrentLocationMarker();
						addMarkerNoPrice(mPage);
					}
				}

			}
		});

		nextImageButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if ((mPage + 1) * 100 > Datas.mEstates.size())
				{
					Toast.makeText(MainActivity.this, "下頁無資料",
							Toast.LENGTH_SHORT).show();
				} else
				{
					mPage = mPage + 1;
					if (getCurrentMemory() > memorySize)
					{
						new addMarkerTask().execute();
					} else
					{
						setTitleText(mPage);
						addCurrentLocationMarker();
						addMarkerNoPrice(mPage);
					}
				}

			}
		});

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
						MainActivity.this);

				LayoutInflater inflater = MainActivity.this.getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_set_date, null);
				final EditText startYearEditText = (EditText) layout
						.findViewById(R.id.start_year_edittext);
				final EditText startMonthEditText = (EditText) layout
						.findViewById(R.id.start_month_edittext);
				final EditText endYearEditText = (EditText) layout
						.findViewById(R.id.end_year_edittext);
				final EditText endMonthEditText = (EditText) layout
						.findViewById(R.id.end_month_edittext);

				int startYear = startDate / 100;
				int startMonth = startDate % 100;

				startYearEditText.setHint(Integer.toString(startYear));
				startMonthEditText.setHint(Integer.toString(startMonth));

				int endYear = endDate / 100;
				int endMonth = endDate % 100;

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

								int startYear = startDate / 100;
								try
								{
									startYear = Integer
											.valueOf(startYearEditText
													.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int startMonth = startDate % 100;
								try
								{
									startMonth = Integer
											.valueOf(startMonthEditText
													.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int endYear = endDate / 100;
								try
								{
									endYear = Integer.valueOf(endYearEditText
											.getText().toString());
								} catch (Exception e)
								{
									// TODO: handle exception
								}

								int endMonth = endDate % 100;
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
									Toast.makeText(MainActivity.this,
											"起始年不可低於96年", Toast.LENGTH_SHORT)
											.show();
								} else if ((startYear * 100 + startMonth) > (endYear * 100 + endMonth))
								{
									Toast.makeText(MainActivity.this,
											"起始期間不可高於結束期間", Toast.LENGTH_SHORT)
											.show();
								} else
								{
									startDate = startYear * 100 + startMonth;
									endDate = endYear * 100 + endMonth;
//									Toast.makeText(MainActivity.this,
//											Integer.toString(startDate),
//											Toast.LENGTH_SHORT).show();
									dateButton.setText(Integer
											.toString(startYear)
											+ "/"
											+ Integer.toString(startMonth)
											+ "~"
											+ Integer.toString(endYear)
											+ "/" + Integer.toString(endMonth));
									getLocation(false, 1);
								}

							}
						});
				dialog.show();
			}
		});

		housePriceChangeingTextView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});

		housePriceLisTextView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});

		// test total memory
		// String totalString = getTotalRAM();
		// Long avalableLong = getCurrentMemory();
		// titleText.setText(Long.toString(avalableLong) + "/" + totalString);

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		btnFocusButton = (ImageButton) findViewById(R.id.image_btn_focus);
		btnLayerButton = (ImageButton) findViewById(R.id.image_btn_layers);
		btnFilterButton = (ImageButton) findViewById(R.id.image_btn_filter);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

		// mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close)
		{
			public void onDrawerClosed(View view)
			{
				// getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				// getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		setDrawerLayout();

		btnFilterButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FilterActivity.class);
				startActivity(intent);
			}
		});

		mLocationClient = new LocationClient(this, this, this);

		try
		{
			// Loading map
			initilizeMap();
			// mWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void setStartAndEndDate(int crawlDateNum)
	{

		int year = crawlDateNum / 100;
		int month = crawlDateNum % 100;

		int startMonth = 0;
		int startYear = 0;
		if (month > 4)
		{
			startMonth = month - 4;
			startYear = year;

		} else
		{
			for (int i = 0; i < 4; i++)
			{
				startMonth = (month + 12 - 4) % 12;
				startYear = year - 1;
			}
		}

		startDate = startYear * 100 + startMonth;
		endDate = crawlDateNum;

		String dateStartString = Integer.toString(startYear) + "/"
				+ Integer.toString(startMonth);

		String dateEndString = Integer.toString(year) + "/"
				+ Integer.toString(month);

		dateButton.setText(dateStartString + "~" + dateEndString);

	}

	private void initilizeMap()
	{
		if (mGoogleMap == null)
		{
			mGoogleMap = ((TransparentSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// mLocationManager = (LocationManager)
			// getSystemService(LOCATION_SERVICE);
			// mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
			mGoogleMap.getUiSettings().setCompassEnabled(false);

			mGoogleMap.setOnMapClickListener(this);

			if (mGoogleMap == null)
			{
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		itemSearch = menu
				.add(0, ID_SEARCH, 0, "搜索")
				.setIcon(R.drawable.icon_search_white)
				.setOnActionExpandListener(
						new MenuItem.OnActionExpandListener()
						{
							private EditText search;

							@Override
							public boolean onMenuItemActionExpand(MenuItem item)
							{
								search = (EditText) item.getActionView();
								search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
								search.setInputType(InputType.TYPE_CLASS_TEXT);
								search.requestFocus();
								search.setOnEditorActionListener(new TextView.OnEditorActionListener()
								{
									@Override
									public boolean onEditorAction(TextView v,
											int actionId, KeyEvent event)
									{
										if (actionId == EditorInfo.IME_ACTION_SEARCH
												|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
										{
											String inputString = v.getText()
													.toString();
											Geocoder geocoder = new Geocoder(
													MainActivity.this);
											List<Address> addresses = null;
											Address address = null;
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													v.getWindowToken(), 0);
											try
											{
												addresses = geocoder
														.getFromLocationName(
																inputString, 1);
											} catch (Exception e)
											{
												Log.e("MainActivity",
														e.toString());
											}
											if (addresses == null
													|| addresses.isEmpty())
											{
												Toast.makeText(
														MainActivity.this,
														"無此地點",
														Toast.LENGTH_SHORT)
														.show();
											} else
											{
												address = addresses.get(0);
												double geoLat = address
														.getLatitude();
												double geoLong = address
														.getLongitude();
												AppConstants.currentLatLng = new LatLng(
														geoLat, geoLong);
//												mGoogleMap
//														.animateCamera(CameraUpdateFactory
//																.newLatLngZoom(
//																		new LatLng(
//																				AppConstants.currentLatLng.latitude,
//																				AppConstants.currentLatLng.longitude),
//																		16.0f));
												getLocation(false, 1);
											}
											return true;
										}
										return false;
									}
								});
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(search,
										InputMethodManager.SHOW_IMPLICIT);
								return true;
							}

							@Override
							public boolean onMenuItemActionCollapse(
									MenuItem item)
							{

								search.setText("");
								return true;
							}
						}).setActionView(R.layout.collapsible_edittext);

		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0)
	{
		// TODO Auto-generated method stub
		if (isReSearch)
		{
			getLocation(true, 0);
			isReSearch = false;
		}
	}

	@Override
	public void onDisconnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if (!mLocationClient.isConnected())
		{
			mLocationClient.connect();
		}
	}

	@Override
	public void onStop()
	{

		// // If the client is connected
		if (mLocationClient.isConnected())
		{
			stopPeriodicUpdates();
		}
		//
		// // After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onStop();
	}

	private void stopPeriodicUpdates()
	{
		mLocationClient.removeLocationUpdates(this);
		// mConnectionState.setText(R.string.location_updates_stopped);
	}

	private void getLocation(Boolean isReGetLoc, int aniParam)
	{

		// If Google Play Services is available
		if (servicesConnected())
		{
			boolean isNeedChangeMap = false;

			if (isReGetLoc)
			{

				try
				{
					Location currentLocation = mLocationClient
							.getLastLocation();
					if (currentLocation != null)
					{
						AppConstants.currentLatLng = new LatLng(
								currentLocation.getLatitude(),
								currentLocation.getLongitude());
					} else
					{

						AppConstants.currentLatLng = new LatLng(25.0478,
								121.5172);

					}
					// add location marker
					addCurrentLocationMarker();
				} catch (Exception e)
				{
					isNeedChangeMap = true;
				}

			}

			if (!isNeedChangeMap)
			{
				center_x = AppConstants.currentLatLng.longitude;
				center_y = AppConstants.currentLatLng.latitude;

				mapSize = 15.0f;

				if (0 < km_dis && km_dis <= 0.3)
				{
					mapSize = 16.0f;
				} else if (0.3 < km_dis && km_dis <= 0.5)
				{
					mapSize = 15.0f;
				} else if (0.5 < km_dis && km_dis <= 1)
				{
					mapSize = 14.0f;
				} else if (1 < km_dis && km_dis <= 2)
				{
					mapSize = 13.0f;
				} else
				{
					mapSize = 12.0f;
				}

				if (aniParam == 0)
				{
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(AppConstants.currentLatLng.latitude,
									AppConstants.currentLatLng.longitude),
							mapSize));
				} else
				{
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(AppConstants.currentLatLng).zoom(mapSize)
							.build();
					mGoogleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}

			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener()
			{

				@Override
				public boolean onMarkerClick(Marker marker)
				{
					// if (marker == null || marker.getTitle() == null)
					// {
					// // Toast.makeText(MainActivity.this, "marker null",
					// Toast.LENGTH_SHORT).show();
					// return true;
					// }
					//
					// Intent intent = new Intent();
					// String monthKey = Datas.getKeyByPosition(mPager
					// .getCurrentItem());
					// intent.putExtra("MonthKey", monthKey);
					// try
					// {
					// Log.i("RowNumber", marker.getTitle().trim());
					// intent.putExtra("RowNumber",
					// Integer.valueOf(marker.getTitle().trim()));
					// } catch (Exception e)
					// {
					// // Toast.makeText(MainActivity.this, "marker error",
					// Toast.LENGTH_SHORT).show();
					// intent.putExtra("RowNumber", 1);
					// }
					//
					// intent.setClass(MainActivity.this, DetailActivity.class);
					// startActivity(intent);
					return true;
				}
			});

			if (NetworkUtil.getConnectivityStatus(MainActivity.this) == 0)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MainActivity.this);
				dialog.setTitle("無網路");
				dialog.setMessage("偵測不到網路");
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{
								getLocation(true, 0);
							}
						});
				dialog.show();
			} else
			{
				if (isNeedChangeMap)
				{
					// do nothing
				} else
				{

					new GetEstatesTask().execute();

					// if (Datas.mArrayKey.size() == 0)
					// {
					// new GetCurrentDateTask().execute();
					// } else
					// {
					//
					// }
				}

			}

		}
	}

	protected class GetEstatesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// linearTitleLayout.setVisibility(View.VISIBLE);
			isEstatesTaskRunning = true;
			showProgress();
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
					MainActivity.this);
			if (hpMinString.equals("0"))
			{
				hpMinString = null;
			}
			String hpMaxString = Setting.getSetting(Setting.keyHousePriceMax,
					MainActivity.this);
			if (hpMaxString.equals("0"))
			{
				hpMaxString = null;
			}
			String areaMinString = Setting.getSetting(Setting.keyAreaMin,
					MainActivity.this);
			if (areaMinString.equals("0"))
			{
				areaMinString = null;
			}
			String areaMaxString = Setting.getSetting(Setting.keyAreaMax,
					MainActivity.this);
			if (areaMaxString.equals("0"))
			{
				areaMaxString = null;
			}
			String groundTypeString = Setting.getSetting(Setting.keyGroundType,
					MainActivity.this);
			if (groundTypeString.equals("0"))
			{
				groundTypeString = null;
			}
			String buildingTypeString = Setting.getSetting(
					Setting.keyBuildingType, MainActivity.this);
			if (buildingTypeString.equals("0"))
			{
				buildingTypeString = null;
			}

			Datas.mEstates = HouseApi.getAroundAllByAreas(MainActivity.this,
					km_dis, center_x, center_y, startDate, endDate, hpMinString,
					hpMaxString, areaMinString, areaMaxString,
					groundTypeString, buildingTypeString);

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			endProgress();
			isEstatesTaskRunning = false;
			// linearTitleLayout.setVisibility(View.INVISIBLE);
			if (Datas.mEstates != null && Datas.mEstates.size() != 0)
			{
				mPage = 0;

				// Datas.mEstatesMap = getRealEstatesMap(Datas.mEstates);
				setTitleText(mPage);

				if (getCurrentMemory() > memorySize)
				{
					new addMarkerTask().execute();
				} else
				{
					addCurrentLocationMarker();
					addMarkerNoPrice(0);
				}

			} else
			{
				Toast.makeText(MainActivity.this, "無資料~", Toast.LENGTH_SHORT)
						.show();
				titleTextView.setText("無資料~");
			}

		}

	}

	private void showProgress()
	{
		previousImageButton.setVisibility(View.GONE);
		nextImageButton.setVisibility(View.GONE);
		titleTextView.setText("搜索中...");
		titleProgressBar.setVisibility(View.VISIBLE);
	}

	private void endProgress()
	{
		previousImageButton.setVisibility(View.VISIBLE);
		nextImageButton.setVisibility(View.VISIBLE);
		titleProgressBar.setVisibility(View.GONE);

	}

	private void setTitleText(int page)
	{
		int num = 0;
		int initialNum = 0;

		if (Datas.mEstates.size() >= (100 * (page + 1)))
		{
			num = 100 * (page + 1);
			initialNum = page * 100 + 1;
		} else
		{
			num = Datas.mEstates.size();
			initialNum = (num / 100) * 100 + 1;
		}

		titleTextView.setText("顯示" + Integer.toString(initialNum) + "~"
				+ Integer.toString(num) + "/"
				+ Integer.toString(Datas.mEstates.size()) + "筆");

	}

	// page start from 0
	private void addMarkerNoPrice(int page)
	{
		// Toast.makeText(MainActivity.this, "Start Add!!", Toast.LENGTH_SHORT)
		// .show();

		int num = 0;
		int initialNum = 0;

		if (Datas.mEstates.size() >= (100 * (page + 1)))
		{
			num = 100 * (page + 1);
			initialNum = page * 100 + 1;
		} else
		{
			num = Datas.mEstates.size();
			initialNum = (num / 100) * 100 + 1;
		}

		for (int i = initialNum - 1; i < num - 1; i++)
		{
			LatLng newLatLng = new LatLng(Datas.mEstates.get(i).y_lat,
					Datas.mEstates.get(i).x_long);
			MarkerOptions marker = new MarkerOptions().position(newLatLng)
					.title(Integer.toString(i));
			marker.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.marker_sale));
			mGoogleMap.addMarker(marker);
		}

		// try
		// {
		// Dao<OrmRealEstate, Integer> estateDao = MainActivity.this
		// .getHelper().getOrmEsateDao();
		// List<OrmRealEstate> estates = estateDao.queryBuilder().where()
		// .between("id", 0 + page * 100, 100 + page * 100).query();
		// for (int i = 0; i < estates.size(); i++)
		// {
		// LatLng newLatLng = new LatLng(estates.get(i).y_lat,
		// estates.get(i).x_long);
		// MarkerOptions marker = new MarkerOptions().position(newLatLng)
		// .title(Integer.toString(i));
		// marker.icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.marker_sale));
		// mGoogleMap.addMarker(marker);
		// }
		//
		// } catch (SQLException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	private class GetCurrentDateTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... arg0)
		{
			// TODO Auto-generated method stub
			int currentDate = HouseApi.getCurrentCrawlDate();
			if (!(currentDate == 0))
			{
				crawlDateNum = currentDate;
				Setting.setCurrentDateNum(MainActivity.this, crawlDateNum);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// Toast.makeText(MainActivity.this, Integer.toString(crawlDateNum),
			// Toast.LENGTH_SHORT).show();
			if (!isEstatesTaskRunning)
			{
				getLocation(true, 0);
			}

		}
	}

	private class addMarkerTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			mGoogleMap.clear();
			addCurrentLocationMarker();
			setTitleText(mPage);
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			// TODO Auto-generated method stub
			setMapMark(mPage);
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// Toast.makeText(MainActivity.this, Integer.toString(crawlDateNum),
			// Toast.LENGTH_SHORT).show();

			for (int i = 0; i < mMarkers.size(); i++)
			{
				mGoogleMap.addMarker(mMarkers.get(i));
			}

		}

	}

	private void setMapMark(int page)
	{

		mMarkers.clear();

		int num = 0;
		int initialNum = 0;

		if (Datas.mEstates.size() >= (100 * (page + 1)))
		{
			num = 100 * (page + 1);
			initialNum = page * 100 + 1;
		} else
		{
			num = Datas.mEstates.size();
			initialNum = (num / 100) * 100 + 1;
		}

		for (int i = initialNum - 1; i < num - 1; i++)
		{
			LatLng newLatLng = new LatLng(Datas.mEstates.get(i).y_lat,
					Datas.mEstates.get(i).x_long);

			View layout = inflater.inflate(R.layout.item_marker, null);
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			ImageView markerView = (ImageView) layout
					.findViewById(R.id.image_marker);
			TextView markerText = (TextView) layout
					.findViewById(R.id.text_marker_price);

			// for later marker info window use
			MarkerOptions marker = new MarkerOptions().position(newLatLng)
					.title(Integer.toString(i));
			markerText
					.setText(Double.toString(Datas.mEstates.get(i).square_price));

			markerView.setImageResource(R.drawable.marker_sale);

			Bitmap bm = loadBitmapFromView(layout);

			// Changing marker icon
			marker.icon(BitmapDescriptorFactory.fromBitmap(bm));

			mMarkers.add(marker);
		}

		// try
		// {
		// Dao<OrmRealEstate, Integer> estateDao = MainActivity.this
		// .getHelper().getOrmEsateDao();
		// List<OrmRealEstate> estates = estateDao.queryBuilder().where()
		// .between("id", 0 + page * 100, 100 + page * 100).query();
		// for (int i = 0; i < estates.size(); i++)
		// {
		// LatLng newLatLng = new LatLng(estates.get(i).y_lat,
		// estates.get(i).x_long);
		//
		// View layout = inflater.inflate(R.layout.item_marker, null);
		// layout.setLayoutParams(new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT));
		// ImageView markerView = (ImageView) layout
		// .findViewById(R.id.image_marker);
		// TextView markerText = (TextView) layout
		// .findViewById(R.id.text_marker_price);
		//
		// // for later marker info window use
		// MarkerOptions marker = new MarkerOptions().position(newLatLng)
		// .title(Integer.toString(i));
		// markerText
		// .setText(Double.toString(estates.get(i).square_price));
		//
		// markerView.setImageResource(R.drawable.marker_sale);
		//
		// Bitmap bm = loadBitmapFromView(layout);
		//
		// // Changing marker icon
		// marker.icon(BitmapDescriptorFactory.fromBitmap(bm));
		//
		// mMarkers.add(marker);
		// }
		//
		// } catch (SQLException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public static Bitmap loadBitmapFromView(View v)
	{
		if (v.getMeasuredHeight() <= 0)
		{
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
					v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);
			return b;
		}

		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
				v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	private boolean servicesConnected()
	{

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG, "Google Play Service Available");

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else
		{
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null)
			{
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						LocationUtils.APPTAG);
			}
			return false;
		}
	}

	private void addCurrentLocationMarker()
	{
		// TODO Auto-generated method stub
		mGoogleMap.clear();
		loacationMarker = new MarkerOptions().position(
				AppConstants.currentLatLng).draggable(true);
		loacationMarker.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.pin_red));
		mGoogleMap.addMarker(loacationMarker);
	}

	public static class ErrorDialogFragment extends DialogFragment
	{

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment()
		{
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 * 
		 * @param dialog
		 *            An error dialog
		 */
		public void setDialog(Dialog dialog)
		{
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			return mDialog;
		}
	}

	private void setDrawerLayout()
	{

		items.add(new SectionItem("實價登錄搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("條件篩選", R.drawable.icon_filter));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("設定", R.drawable.icon_setting));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(MainActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		mDrawerListView.setOnItemClickListener((new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				if (!items.get(position).isSection())
				{
					// EntryItem item = (EntryItem) items.get(position);

					switch (position)
					{
					case 1:
						// move to position
						// CameraPosition cameraPosition = new
						// CameraPosition.Builder()
						// .target(Constants.currentLatLng).zoom(14)
						// .build();
						// googleMap.animateCamera(CameraUpdateFactory
						// .newCameraPosition(cameraPosition));
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 2:
						// filter dialog
						// showFilterDialog();
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 4:
						// Intent intent = new Intent(MainActivity.this,
						// CalculatorActivity.class);
						// startActivity(intent);
						break;
					case 6:
						// setting activity
						// Intent intent1 = new Intent(MainActivity.this,
						// SettingActivity.class);
						// startActivity(intent1);
						break;
					case 7:
						// about us
						// Intent intent2 = new Intent(MainActivity.this,
						// AboutUsActivity.class);
						// startActivity(intent2);
						break;
					default:
						break;
					}
				}
			}
		}));

	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item)))
		{
			return true;
		} else
		{
			switch (item.getItemId())
			{
			case ID_SEARCH:
				// Toast.makeText(MainActivity.this, "search",
				// Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private android.view.MenuItem getMenuItem(final MenuItem item)
	{
		return new android.view.MenuItem()
		{
			@Override
			public int getItemId()
			{
				return item.getItemId();
			}

			public boolean isEnabled()
			{
				return true;
			}

			@Override
			public boolean collapseActionView()
			{
				return false;
			}

			@Override
			public boolean expandActionView()
			{
				return false;
			}

			@Override
			public ActionProvider getActionProvider()
			{
				return null;
			}

			@Override
			public View getActionView()
			{
				return null;
			}

			@Override
			public char getAlphabeticShortcut()
			{
				return 0;
			}

			@Override
			public int getGroupId()
			{
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				return null;
			}

			@Override
			public Intent getIntent()
			{
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo()
			{
				return null;
			}

			@Override
			public char getNumericShortcut()
			{
				return 0;
			}

			@Override
			public int getOrder()
			{
				return 0;
			}

			@Override
			public SubMenu getSubMenu()
			{
				return null;
			}

			@Override
			public CharSequence getTitle()
			{
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				return null;
			}

			@Override
			public boolean hasSubMenu()
			{
				return false;
			}

			@Override
			public boolean isActionViewExpanded()
			{
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				return false;
			}

			@Override
			public boolean isChecked()
			{

				return false;
			}

			@Override
			public boolean isVisible()
			{
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar)
			{
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum)
			{

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible)
			{
				return null;
			}
		};
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	private String getTotalRAM()
	{
		RandomAccessFile reader = null;
		String load = null;
		try
		{
			reader = new RandomAccessFile("/proc/meminfo", "r");
			load = reader.readLine();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		} finally
		{
			// Streams.close(reader);
		}
		// titleText.setText(load);
		Toast.makeText(MainActivity.this, load, Toast.LENGTH_SHORT).show();
		return load;
	}

	private Long getCurrentMemory()
	{
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		return availableMegs;
	}

	@Override
	public void onMapClick(LatLng arg0)
	{
		// TODO Auto-generated method stub
		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
		AppConstants.currentLatLng = arg0;	
		mGoogleMap.clear();
		addCurrentLocationMarker();
		getLocation(false, 1);
	}
	
	@Override
	public void onBackPressed()
	{
		boolean isShowGiveStarDialog = Setting.getBooleanSetting(
				Setting.KeyPushStarDialog, MainActivity.this);
		if (isShowGiveStarDialog)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);

			dialog.setTitle("給星星");
			dialog.setMessage("您的五星評價是加速我們改進產品的動力，針對早期用戶, 我們會拿去廣告回饋喔~");
			dialog.setPositiveButton("給星星",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialoginterface,
								int i)
						{
							Uri uri = Uri
									.parse("https://play.google.com/store/apps/details?id=com.kosbrother.houseprice");
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(it);
							Setting.saveBooleanSetting(Setting.KeyGiveStar,
									true, MainActivity.this);
							Setting.saveBooleanSetting(
									Setting.KeyPushStarDialog, false,
									MainActivity.this);
						}
					});
			dialog.setNeutralButton("稍後提醒",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialoginterface,
								int i)
						{
							// do nothing
							finish();
							isReSearch = true;
						}
					});

			dialog.setNegativeButton("不再提醒",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialoginterface,
								int i)
						{
							Setting.saveBooleanSetting(
									Setting.KeyPushStarDialog, false,
									MainActivity.this);
							finish();
							isReSearch = true;
						}
					});

			dialog.show();

		} else
		{
			super.onBackPressed();
			isReSearch = true;
		}

	}
	
	private void showSelectDistanceDialog()
	{

		AlertDialog.Builder editDialog = new AlertDialog.Builder(
				MainActivity.this);
		editDialog.setTitle("選取搜索範圍");

		// final EditText editText = new EditText(ArticleActivity.this);
		// editDialog.setView(editText);

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View distance_view = inflater.inflate(R.layout.dialog_select_distance,
				null);
		final TextView textDistance = (TextView) distance_view
				.findViewById(R.id.text_distance);
		SeekBar seekBarDistance = (SeekBar) distance_view
				.findViewById(R.id.seekbar_distance);

		textDistance.setText(Double.toString(km_dis) + "km");
		int pp = (int) (km_dis / 0.03);
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
						km_dis = Double.valueOf(d_String);
					}
				});

		editDialog.setView(distance_view);

		editDialog.setPositiveButton("確定",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						if (km_dis != 0)
						{
							distanceButton.setText(Double.toString(km_dis) + "km");
							Setting.saveSetting(Setting.keyKmDistance,
									Double.toString(km_dis), MainActivity.this);
							getLocation(false, 0);
						} else
						{
							Toast.makeText(MainActivity.this, "半徑不能為0",
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

	// public DatabaseHelper getHelper()
	// {
	// if (databaseHelper == null)
	// {
	// databaseHelper = OpenHelperManager.getHelper(this,
	// DatabaseHelper.class);
	// }
	// return databaseHelper;
	// }

}
