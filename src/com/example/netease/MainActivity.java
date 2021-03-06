package com.example.netease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import com.example.addresslisttest.CharacterParser;
import com.example.addresslisttest.ClearEditText;
import com.example.addresslisttest.PinyinComparator;
import com.example.addresslisttest.SideBar;
import com.example.addresslisttest.SortAdapter;
import com.example.addresslisttest.SortModel;
import com.example.addresslisttest.SideBar.OnTouchingLetterChangedListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	/** 左边栏菜单 */
//	private ListView mMenuListView;
	/** 右边栏 */
	private RelativeLayout right_drawer;
	/** 菜单列表 */
	private String[] mMenuTitles;
	/** Material Design风格 */
	private MaterialMenuIcon mMaterialMenuIcon;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	/** 右边栏打开/关闭状态 */
	private boolean isDirection_right = false;
	private View showView;
	
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	
	

	/**
	 * 姹夊瓧杞崲鎴愭嫾闊崇殑绫�
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 鏍规嵁鎷奸煶鏉ユ帓鍒桳istView閲岄潰鐨勬暟鎹被
	 */
	private PinyinComparator pinyinComparator;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		final TextView textview = (TextView) findViewById(R.id.youbian);
		textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "吐", Toast.LENGTH_LONG).show();
				
			}
		});
		
		mDrawerLayout.setScrimColor(0x00000000);
//		mMenuListView = (ListView) findViewById(R.id.left_drawer);
		right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
//		this.showView = mMenuListView;

		// 初始化菜单列表
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
//		mMenuListView.setAdapter(new ArrayAdapter<String>(this,
//				R.layout.drawer_list_item, mMenuTitles));
//		mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(android.R.color.transparent, //R.drawable.drawer_shadow,
				GravityCompat.START);// LOCK_MODE_LOCKED_OPEN
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		
		// 设置ActionBar可见，并且切换菜单和内容视图
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mMaterialMenuIcon = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());

//		if (savedInstanceState == null) {
//			selectItem(0);
//		}
		initView();

	}
	
	public void initView() {
		//瀹炰緥鍖栨眽瀛楄浆鎷奸煶绫�
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//璁剧疆鍙充晶瑙︽懜鐩戝惉
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//璇ュ瓧姣嶉娆″嚭鐜扮殑浣嶇疆
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//杩欓噷瑕佸埄鐢╝dapter.getItem(position)鏉ヨ幏鍙栧綋鍓峱osition鎵�搴旂殑瀵硅薄
				Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				mDrawerLayout.openDrawer(right_drawer);
			}
		});
		
		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		
		// 鏍规嵁a-z杩涜鎺掑簭婧愭暟鎹�
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		sortListView.requestDisallowInterceptTouchEvent(true);
		
//	mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
	//鏍规嵁杈撳叆妗嗚緭鍏ュ�鐨勬敼鍙樻潵杩囨护鎼滅储
//		mClearEditText.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				//褰撹緭鍏ユ閲岄潰鐨勫�涓虹┖锛屾洿鏂颁负鍘熸潵鐨勫垪琛紝鍚﹀垯涓鸿繃婊ゆ暟鎹垪琛�
//				filterData(s.toString());
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
		
	}

	

	/**
	 * 涓篖istView濉厖鏁版嵁
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//姹夊瓧杞崲鎴愭嫾闊�
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 姝ｅ垯琛ㄨ揪寮忥紝鍒ゆ柇棣栧瓧姣嶆槸鍚︽槸鑻辨枃瀛楁瘝
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 鏍规嵁杈撳叆妗嗕腑鐨勫�鏉ヨ繃婊ゆ暟鎹苟鏇存柊ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 鏍规嵁a-z杩涜鎺掑簭
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}


	/**
	 * ListView上的Item点击事件
	 * 
	 */
//	private class DrawerItemClickListener implements
//			ListView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			selectItem(position);
//		}
//	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends
			DrawerLayout.SimpleDrawerListener {
		/**
		 * 当导航菜单滑动的时候被执行
		 */
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			showView = drawerView;
			/*if (drawerView == mMenuListView) {// 根据isDirection_left决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_left ? 2 - slideOffset : slideOffset);
			} else */if (drawerView == right_drawer) {// 根据isDirection_right决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_right ? 2 - slideOffset : slideOffset);
			}
		}

		/**
		 * 当导航菜单打开时执行
		 */
		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			/*if (drawerView == mMenuListView) {
				isDirection_left = true;
			} else*/ if (drawerView == right_drawer) {
				isDirection_right = true;
			}
		}

		/**
		 * 当导航菜单关闭时执行
		 */
		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			/*if (drawerView == mMenuListView) {
				isDirection_left = false;
			} else */if (drawerView == right_drawer) {
				isDirection_right = false;
//				showView = mMenuListView;
			}
		}
	}

	/**
	 * 切换主视图区域的Fragment
	 * 
	 * @param position
	 */
//	private void selectItem(int position) {
//		Fragment fragment = new ContentFragment();
//		Bundle args = new Bundle();
//		switch (position) {
//		case 0:
//			args.putString("key", mMenuTitles[position]);
//			break;
//		case 1:
//			args.putString("key", mMenuTitles[position]);
//			break;
//		case 2:
//			args.putString("key", mMenuTitles[position]);
//			break;
//		case 3:
//			args.putString("key", mMenuTitles[position]);
//			break;
//		default:
//			break;
//		}
//		fragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		fragmentManager.beginTransaction()
//				.replace(R.id.content_frame, fragment).commit();
//
//		// 更新选择后的item和title，然后关闭菜单
//		mMenuListView.setItemChecked(position, true);
//		setTitle(mMenuTitles[position]);
//		mDrawerLayout.closeDrawer(mMenuListView);
//	}

	/**
	 * 点击ActionBar上菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			/*if (showView == mMenuListView) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(mMenuListView);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(mMenuListView);
				}
			} else*/ if (showView == right_drawer) {
				if (!isDirection_right) {// 右边栏关闭时，打开
					mDrawerLayout.openDrawer(right_drawer);
				} else {// 右边栏打开时，关闭
					mDrawerLayout.closeDrawer(right_drawer);
				}
			}
			break;
		case R.id.action_personal:
			if (!isDirection_right) {// 右边栏关闭时，打开
				/*if (showView == mMenuListView) {
					mDrawerLayout.closeDrawer(mMenuListView);
				}*/
				mDrawerLayout.openDrawer(right_drawer);
			} else {// 右边栏打开时，关闭
				mDrawerLayout.closeDrawer(right_drawer);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 根据onPostCreate回调的状态，还原对应的icon state
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mMaterialMenuIcon.syncState(savedInstanceState);
	}

	/**
	 * 根据onSaveInstanceState回调的状态，保存当前icon state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mMaterialMenuIcon.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 加载菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
