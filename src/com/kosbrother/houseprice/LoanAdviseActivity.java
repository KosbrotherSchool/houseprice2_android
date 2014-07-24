package com.kosbrother.houseprice;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.entity.County;

public class LoanAdviseActivity extends FragmentActivity
{
	private Spinner phoneSpinner;
	private Spinner locationSpinner;
	private TextView sentLenderTextView;
	private EditText lenderNameEditText;
	private RadioButton radioBoyButton;
	private RadioButton radioGirButton;
	private EditText lenderPhoneEditText;
	
	private boolean returnBoolean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_advise);

		if (Build.VERSION.SDK_INT >= 14)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}
		getActionBar().setTitle("房貸咨詢");
		
		lenderNameEditText = (EditText) findViewById(R.id.edit_lender_name);
		lenderPhoneEditText = (EditText) findViewById(R.id.edit_lender_phone);
		radioBoyButton = (RadioButton) findViewById(R.id.radio_boy);
		radioGirButton = (RadioButton) findViewById(R.id.radio_girl);
		
		phoneSpinner = (Spinner) findViewById(R.id.spinner_phone);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.phone_time));
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		phoneSpinner.setAdapter(dataAdapter);
		
		locationSpinner = (Spinner) findViewById(R.id.spinner_location);
		ArrayList<County> mCounties = HouseApi.getCounties();
		List<String> mList = getListFromCounties(mCounties);
		ArrayAdapter<String> dataAdapterLocation = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mList);
		dataAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(dataAdapterLocation);
		
		sentLenderTextView = (TextView) findViewById(R.id.text_lender_send);
		sentLenderTextView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{	
				if (lenderNameEditText.getText().toString().equals(""))
				{
					Toast.makeText(LoanAdviseActivity.this, "請輸入姓名", Toast.LENGTH_SHORT).show();
				}else if (lenderPhoneEditText.getText().toString().equals("")) {
					Toast.makeText(LoanAdviseActivity.this, "請輸入電話", Toast.LENGTH_SHORT).show();
				}else {
					new addLenderTask().execute();
				}
				
			}
		});
		
	}
	
	
	private class addLenderTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(LoanAdviseActivity.this, "傳送資料中...", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			// TODO Auto-generated method stub
			String name = lenderNameEditText.getText().toString();
			String sexual = "";
			if (radioBoyButton.isChecked())
			{
				sexual = "男生";
			}else {
				sexual = "女生";
			}
			String location = locationSpinner.getSelectedItem().toString();
			String phone = lenderPhoneEditText.getText().toString();
			String phone_time = phoneSpinner.getSelectedItem().toString();
			returnBoolean = HouseApi.postLender(name, sexual, location, phone, phone_time);
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if (returnBoolean)
			{
				Toast.makeText(LoanAdviseActivity.this, "傳送成功", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(LoanAdviseActivity.this, "傳送失敗", Toast.LENGTH_SHORT).show();
			}


		}

	}
	
	private List<String> getListFromCounties(ArrayList<County> mCounties)
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < mCounties.size(); i++)
		{
			list.add(mCounties.get(i).name);
		}
		return list;
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
