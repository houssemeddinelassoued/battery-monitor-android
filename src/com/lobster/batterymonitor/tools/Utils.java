package com.lobster.batterymonitor.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.View;

public class Utils {
	public static String getPreferenceStr(Context context, String name) {
		return getPreferenceStr(context, name, "");
	}

	public static String getPreferenceStr(Context context, String name,
			String defValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"preferences", 0);
		return preferences.getString(name, defValue);
	}

	public static void setPreferenceStr(Context context, String name,
			String value) {
		SharedPreferences preferences = context.getSharedPreferences(
				"preferences", 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, value);
		editor.commit();
	}
	public static void setPreferenceStr(Context context, String name, String value, String preName) {
		SharedPreferences preferences = context.getSharedPreferences(
				preName, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, value);
		editor.commit();
	}
	public static String getPreferenceStr(Context context, String name, String preName, String defValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				preName, 0);
		return preferences.getString(name, defValue);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	private static Bitmap takeScreenShot(Activity activity, int... params) {
		// View鏄綘闇�鎴浘鐨刅iew
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 鑾峰彇鐘舵�鏍忛珮搴�
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		Log.i("TAG", "" + statusBarHeight);

		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		height = Math.min(height, b1.getHeight());
		width = Math.min(width, b1.getWidth());
		statusBarHeight = Math.max(0, statusBarHeight);
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b;
		if(params.length == 4) {
			Log.i("test",b1.getWidth() + " " + b1.getHeight());
			Log.i("test",params[0] + " " + params[1] + " " + params[2] + " " + params[3]);
			b = Bitmap.createBitmap(b1, params[0], params[1], params[2], params[3]);
		}else{
			b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		}
		view.destroyDrawingCache();
		return b;
	}

	public static void savePic(Activity activity, String strFileName, int... params) {
		Bitmap b = takeScreenShot(activity, params);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    //
	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); //
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png;text/plain");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	} 
	/* 
     * 获取控件宽 
     */  
    public static int getWidth(View view)  
    {  
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        view.measure(w, h);  
        return (view.getMeasuredWidth());         
    }  
    /* 
     * 获取控件高 
     */  
    public static int getHeight(View view)  
    {  
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        view.measure(w, h);  
        return (view.getMeasuredHeight());         
    }  
}
