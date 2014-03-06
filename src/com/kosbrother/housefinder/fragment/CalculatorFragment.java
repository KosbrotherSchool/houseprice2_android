package com.kosbrother.housefinder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.housefinder.AppConstants;
import com.kosbrother.housefinder.R;


public class CalculatorFragment extends Fragment implements OnClickListener
{

	private Button btn_calclate;
	private EditText editLoanMoney;
	private EditText editLoanRate;
	private EditText editLoanPeriod;
	private TextView textReturnMonthMoney;
	private TextView textReturnTotalRateMoney;
	private TextView textReturnTotalMoney;
	private InputMethodManager imm;
	
	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
	
	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	public CalculatorFragment()
	{

	}

	public static CalculatorFragment newInstance()
	{
		CalculatorFragment f = new CalculatorFragment();
		
		
		return f;
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.calculator, null);
		
		adBannerLayout = (RelativeLayout) v.findViewById(R.id.adLayout);
		editLoanMoney = (EditText) v.findViewById(R.id.edit_loan_money);
		editLoanRate = (EditText) v.findViewById(R.id.edit_loan_rate);
		editLoanPeriod = (EditText) v.findViewById(R.id.edit_loan_period);

		btn_calclate = (Button) v.findViewById(R.id.btn_calculate);
		btn_calclate.setOnClickListener(this);

		textReturnMonthMoney = (TextView) v.findViewById(R.id.text_return_month_money);
		textReturnTotalRateMoney = (TextView) v.findViewById(R.id.text_return_total_rate_money);
		textReturnTotalMoney = (TextView) v.findViewById(R.id.text_return_total_money);
		
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    
//		CallAds();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v)
	{
		if (v == btn_calclate)
		{
			int loanMoney;
			double loanRate;
			double loanPeriod;
			int moneyPerMonth;
			int moneyTotalRate;
			int moneyTotal;
			
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			
			if (editLoanMoney.getText().toString().equals(""))
			{
				Toast.makeText(getActivity(), "請輸入貸款金額", Toast.LENGTH_SHORT).show();
			} else
			{
				loanMoney = Integer.parseInt(editLoanMoney.getText().toString()) * 10000;
				loanRate = Double.parseDouble(editLoanRate.getText().toString()) * 0.01;
				loanPeriod = Double.parseDouble(editLoanPeriod.getText().toString());

				moneyPerMonth = (int) (calculate(loanMoney, loanRate, loanPeriod));
				moneyTotal = (int) (moneyPerMonth * 12 * loanPeriod);
				moneyTotalRate = moneyTotal - loanMoney;

				textReturnMonthMoney.setText(Integer.toString(moneyPerMonth));
				textReturnTotalRateMoney.setText(Integer.toString(moneyTotalRate));
				textReturnTotalMoney.setText(Integer.toString(moneyTotal));

			}

		}
	}

	private double calculate(double loan_money, double loan_rate, double loan_period)
	{
		double rate = Math.pow((1 + loan_rate / 12), loan_period * 12) * (loan_rate / 12)
				/ (Math.pow((1 + loan_rate / 12), loan_period * 12) - 1);
		double money = loan_money * rate;

		Log.i("MainActivity", "rate =" + rate + " money =" + money);
		return money;
	}
	
	private void CallAds()
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
