/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kosbrother.houseprice;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * This demonstrates the use of action bar tabs and how they interact with other
 * action bar features.
 */
@SuppressLint("ValidFragment")
public class ActionBarTabs extends FragmentActivity
{

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.action_bar_tabs);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionBar.setDisplayShowTitleEnabled(false);
		if (Build.VERSION.SDK_INT >= 14)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}
		actionBar.addTab(actionBar
				.newTab()
				.setText("永慶房屋")
				.setTabListener(
						new TabListener("http://m.yungching.com.tw/",
								"YungChing")));
		actionBar.addTab(actionBar
				.newTab()
				.setText("信義房屋")
				.setTabListener(
						new TabListener("http://m.sinyi.com.tw/", "SinYi")));
		actionBar.addTab(actionBar
				.newTab()
				.setText("591租屋")
				.setTabListener(
						new TabListener(
								"http://m.591.com.tw/mobile-index.html?f=app",
								"591")));
		CallAds();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A TabListener receives event callbacks from the action bar as tabs are
	 * deselected, selected, and reselected. A FragmentTransaction is provided
	 * to each of these callbacks; if any operations are added to it, it will be
	 * committed at the end of the full tab switch operation. This lets tab
	 * switches be atomic without the app needing to track the interactions
	 * between different tabs.
	 * 
	 * NOTE: This is a very simple implementation that does not retain fragment
	 * state of the non-visible tabs across activity instances. Look at the
	 * FragmentTabs example for how to do a more complete implementation.
	 */

	private class TabListener implements ActionBar.TabListener
	{
		private TabContentFragment mFragment;
		private String mURL;
		private String mTag;

		public TabListener(String URL, String tag)
		{
			mURL = URL;
			mTag = tag;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			if (mFragment == null)
			{
				mFragment = TabContentFragment.newInstance(mURL, mTag);
				ft.add(R.id.fragment_content, mFragment, mFragment.getText());
			} else
			{
				ft.show(mFragment);
			}

		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			if (mFragment != null)
			{
				// Detach the fragment, because another one is being attached
				// ft.detach(mFragment);
				ft.hide(mFragment);
			}
			// ft.remove(mFragment);
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
			Toast.makeText(ActionBarTabs.this, "Reselected!",
					Toast.LENGTH_SHORT).show();
		}

	}

	public static class TabContentFragment extends Fragment
	{

		private String mURL;
		private ProgressBar progress;
		private WebView mWebView;
		private String mTag;

		// public TabContentFragment(String URL, String tag)
		// {
		// mURL = URL;
		// mTag = tag;
		//
		// }

		public static TabContentFragment newInstance(String URL, String tag)
		{
			TabContentFragment f = new TabContentFragment();
			Bundle args = new Bundle();
			args.putString("URL", URL);
			args.putString("TAG", tag);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			mURL = getArguments() != null ? getArguments().getString("URL")
					: "http://m.591.com.tw/mobile-index.html?f=app";
			mTag = getArguments() != null ? getArguments().getString("TAG")
					: "591";
		}

		public String getText()
		{
			return mTag;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_webview, null);
			progress = (ProgressBar) v.findViewById(R.id.progressBar);
			mWebView = (WebView) v.findViewById(R.id.fragment_webview);
			mWebView.loadUrl(mURL);

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new myWebViewClient());

			mWebView.setOnKeyListener(new OnKeyListener()
			{

				@Override
				public boolean onKey(View arg0, int keyCode, KeyEvent event)
				{
					if ((keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
							&& mWebView.canGoBack())
					{
						mWebView.goBack();
						return true;
					}

					return false;
				}
			});

			mWebView.setWebChromeClient(new WebChromeClient()
			{

				public void onProgressChanged(WebView view, int progress_value)
				{
					progress.setProgress(progress_value);
				}

			});

			return v;
		}

		public class myWebViewClient extends WebViewClient
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				progress.setVisibility(View.GONE);
			}
		}

	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar,
				ActionBarTabs.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			// 12-18 17:01:12.438: I/Ads(8252): Use
			// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
			// to get test ads on this device.

			adMobAdView = new AdView(ActionBarTabs.this);
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
