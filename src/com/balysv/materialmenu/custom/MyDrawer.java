package com.balysv.materialmenu.custom;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyDrawer extends DrawerLayout {

	public MyDrawer(Context context) {
		super(context);
		
	}

	public MyDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public MyDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			Field declaredField = DrawerLayout.class.getDeclaredField("mRightDragger");
			declaredField.setAccessible(true);
			Object object = declaredField.get(this);
			if(object instanceof ViewDragHelper) {
				Log.i("hha", "haode");
				return false;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return super.onInterceptTouchEvent(ev);
	}

	
}
