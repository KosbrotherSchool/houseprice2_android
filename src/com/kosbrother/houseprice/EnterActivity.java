package com.kosbrother.houseprice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.analytics.tracking.android.EasyTracker;
import com.kosbrother.houseprice.api.HouseApi;

public class EnterActivity extends Activity
{	
	private int crawlDateNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);

		ImageView img = (ImageView) findViewById(R.id.load_image);
		img.setBackgroundResource(R.anim.loading);
		
		AnimationDrawable ad = (AnimationDrawable) img.getBackground();
		ad.start();
		
		new GetCurrentDateTask().execute();
		
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
				Setting.setCurrentDateNum(EnterActivity.this, crawlDateNum);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// end Activity and Start MainActivity
			EnterActivity.this.finish();
			Intent intent = new Intent();
			intent.setClass(EnterActivity.this, MainActivity.class);
			startActivity(intent);

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
