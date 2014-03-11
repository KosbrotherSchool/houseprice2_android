package com.kosbrother.housefinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class YungChingFragment extends Fragment 
{	
	String uRLString = "http://m.yungching.com.tw/";
	
	public static FiveNineOneRealtyFragment newInstance()
	{
		FiveNineOneRealtyFragment f = new FiveNineOneRealtyFragment();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		WebView mWebView = new WebView(getActivity());
		mWebView.loadUrl(uRLString);
		return mWebView;
	}
}
