package com.example.addresslisttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.netease.R;


public class SideBar extends View {
	// 瑙︽懜浜嬩欢
		private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
		// 26涓瓧姣�
		public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "#" };
		private int choose = -1;// 閫変腑
		private Paint paint = new Paint();

		private TextView mTextDialog;

		public void setTextView(TextView mTextDialog) {
			this.mTextDialog = mTextDialog;
		}


		public SideBar(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public SideBar(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public SideBar(Context context) {
			super(context);
		}

		/**
		 * 閲嶅啓杩欎釜鏂规硶
		 */
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 鑾峰彇鐒︾偣鏀瑰彉鑳屾櫙棰滆壊.
			int height = getHeight();// 鑾峰彇瀵瑰簲楂樺害
			int width = getWidth(); // 鑾峰彇瀵瑰簲瀹藉害
			int singleHeight = height / b.length;// 鑾峰彇姣忎竴涓瓧姣嶇殑楂樺害

			for (int i = 0; i < b.length; i++) {
				paint.setColor(Color.rgb(33, 65, 98));
				// paint.setColor(Color.WHITE);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				paint.setAntiAlias(true);
				paint.setTextSize(20);
				// 閫変腑鐨勭姸鎬�
				if (i == choose) {
					paint.setColor(Color.parseColor("#3399ff"));
					paint.setFakeBoldText(true);
				}
				// x鍧愭爣绛変簬涓棿-瀛楃涓插搴︾殑涓�崐.
				float xPos = width / 2 - paint.measureText(b[i]) / 2;
				float yPos = singleHeight * i + singleHeight;
				canvas.drawText(b[i], xPos, yPos, paint);
				paint.reset();// 閲嶇疆鐢荤瑪
			}

		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			final int action = event.getAction();
			final float y = event.getY();// 鐐瑰嚮y鍧愭爣
			final int oldChoose = choose;
			final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
			final int c = (int) (y / getHeight() * b.length);// 鐐瑰嚮y鍧愭爣鎵�崰鎬婚珮搴︾殑姣斾緥*b鏁扮粍鐨勯暱搴﹀氨绛変簬鐐瑰嚮b涓殑涓暟.

			switch (action) {
			case MotionEvent.ACTION_UP:
				setBackgroundDrawable(new ColorDrawable(0x00000000));
				choose = -1;//
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;

			default:
				//璁剧疆鍙充晶瀛楁瘝鍒楄〃[A,B,C,D,E....]鐨勮儗鏅鑹�
				setBackgroundResource(R.drawable.sortlistview_sidebar_background);
				if (oldChoose != c) {
					if (c >= 0 && c < b.length) {
						if (listener != null) {
							listener.onTouchingLetterChanged(b[c]);
						}
						if (mTextDialog != null) {
							mTextDialog.setText(b[c]);
							mTextDialog.setVisibility(View.VISIBLE);
						}
						
						choose = c;
						invalidate();
					}
				}

				break;
			}
			return true;
		}

		/**
		 * 鍚戝鍏紑鐨勬柟娉�
		 * 
		 * @param onTouchingLetterChangedListener
		 */
		public void setOnTouchingLetterChangedListener(
				OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
			this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
		}

		/**
		 * 鎺ュ彛
		 * 
		 * @author coder
		 * 
		 */
		public interface OnTouchingLetterChangedListener {
			public void onTouchingLetterChanged(String s);
		}

}