package com.kosbrother.houseprice;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting
{
	public final static String keyPref = "Preference";

	public final static String keyPurpose = "Purpose";
	public final static String keyHousePriceMin = "HP_Min";
	public final static String keyHousePriceMax = "HP_Max";
	public final static String keyGroundType = "GroundType";
	public final static String keyBuildingType = "BuildingType";
	public final static String keyAreaMin = "Area_Min";
	public final static String keyAreaMax = "Area_Max";
	public final static String keyKmDistance = "Km_Distance";

//	public final static String keyFirstOpen = "First_Open";
	public final static String KeyCurrentDateNum = "Current_Data_Date";
	public final static String KeyGiveStar = "Give_Star";
	public final static String KeyPushStarDialog = "Push_Star_Dialog";

	public final static String initialPurpose = "0"; // 0 for buy, 1 for sell
	public final static String initialHousePriceMin = "0";
	public final static String initialHousePriceMax = "0"; // 0 for max
	public final static String initialGroundType = "0";
	public final static String initialBuildingType = "0";
	public final static String initialAreaMin = "0";
	public final static String initialAreaMax = "0"; // 0 for max
	public final static String initialKmDistance = "0.5";
//	public final static boolean initialFirstOpen = true;
	public final static int initialCurrentDate = 10212;
	public final static boolean initialGiveStar = false;
	public final static boolean initialPushStarDialog = true;

	private static final HashMap<String, String> initMap = new HashMap<String, String>()
	{
		{
			put(keyPurpose, initialPurpose);
			put(keyHousePriceMin, initialHousePriceMin);
			put(keyHousePriceMax, initialHousePriceMax);
			put(keyGroundType, initialGroundType);
			put(keyBuildingType, initialBuildingType);
			put(keyAreaMin, initialAreaMin);
			put(keyAreaMax, initialAreaMax);
			put(keyKmDistance, initialKmDistance);
		}
	};

	private static final HashMap<String, Boolean> booleanMap = new HashMap<String, Boolean>()
	{
		{
			put(KeyGiveStar, initialGiveStar);
			put(KeyPushStarDialog, initialPushStarDialog);
		}
	};

	public static String getSetting(String settingKey, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		String settingValue = sharePreference.getString(settingKey,
				initMap.get(settingKey));
		return settingValue;
	}

	public static void saveSetting(String settingKey, String settingValue,
			Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putString(settingKey, settingValue).commit();
	}

	public static boolean getBooleanSetting(String settingkey, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		boolean booleanValue = sharePreference.getBoolean(settingkey,
				booleanMap.get(settingkey));
		return booleanValue;
	}

	public static void saveBooleanSetting(String settingKey,
			boolean settingBooleanValue, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putBoolean(settingKey, settingBooleanValue)
				.commit();
	}

//	public static boolean getFirstBoolean(Context context)
//	{
//		SharedPreferences sharePreference = context.getSharedPreferences(
//				keyPref, 0);
//		boolean firstBoolean = sharePreference.getBoolean(keyFirstOpen,
//				initialFirstOpen);
//		return firstBoolean;
//	}
//
//	public static void setFirstBoolean(Context context)
//	{
//		SharedPreferences sharePreference = context.getSharedPreferences(
//				keyPref, 0);
//		sharePreference.edit().putBoolean(keyFirstOpen, false).commit();
//	}

	public static int getCurrentDateNum(Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		int dateNum = sharePreference.getInt(KeyCurrentDateNum,
				initialCurrentDate);
		return dateNum;
	}

	public static void setCurrentDateNum(Context context, int currentDate)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putInt(KeyCurrentDateNum, currentDate).commit();
	}

//	public static boolean getGiveStarBoolean(Context context)
//	{
//		SharedPreferences sharePreference = context.getSharedPreferences(
//				keyPref, 0);
//		boolean firstBoolean = sharePreference.getBoolean(KeyGiveStar,
//				initialGiveStar);
//		return firstBoolean;
//	}
//
//	public static void setGivenStarBoolean(Context context)
//	{
//		SharedPreferences sharePreference = context.getSharedPreferences(
//				keyPref, 0);
//		sharePreference.edit().putBoolean(KeyGiveStar, true).commit();
//	}

}
