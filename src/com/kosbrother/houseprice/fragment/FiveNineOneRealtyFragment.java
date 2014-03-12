package com.kosbrother.houseprice.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kosbrother.houseprice.R;

public class FiveNineOneRealtyFragment extends Fragment
{
	String uRLString = "http://m.591.com.tw/mobile-index.html?f=app";
	private ProgressBar progress;
	private WebView mWebView;
	private Bundle webViewBundle;

	public static FiveNineOneRealtyFragment newInstance()
	{
		FiveNineOneRealtyFragment f = new FiveNineOneRealtyFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_webview, null);
		progress = (ProgressBar) v.findViewById(R.id.progressBar);
		mWebView = (WebView) v.findViewById(R.id.fragment_webview);

		if (webViewBundle == null)
		{
			mWebView.loadUrl(uRLString);
		} else
		{
			mWebView.restoreState(webViewBundle);
		}

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new myWebViewClient());

		mWebView.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event)
			{
				// Toast.makeText(getActivity(), "back pressed",
				// Toast.LENGTH_SHORT).show();
				if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())
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

	@Override
	public void onPause()
	{
		super.onPause();

		webViewBundle = new Bundle();
		mWebView.saveState(webViewBundle);
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
