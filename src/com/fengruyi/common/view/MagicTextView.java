package com.fengruyi.common.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 以一定速度增长到指定值显示数字
 * @author fengruyi
 *
 */
public class MagicTextView extends TextView{
	// 递减/递增 的变量值
		private double mRate;
		// view 设置的值
		private double mValue;
		// 当前显示的值
		private double mCurValue;
		// 当前变化后最终状态的目标值
		private double mGalValue;
		// 控制加减法
		private int rate = 1;
		private boolean refreshing;
		private static final int REFRESH = 1;
		private static final int SCROLL = 2;
		DecimalFormat fnum = new DecimalFormat("0.00");

		private Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {

				case REFRESH:
					if (rate * mCurValue < mGalValue) {
						refreshing = true;
						setText(fnum.format(mCurValue));
						mCurValue += mRate * rate;
						mHandler.sendEmptyMessageDelayed(REFRESH, 50);
					} else {
						refreshing = false;
						setText(fnum.format(mGalValue));
					}
					break;
				case SCROLL:
					doScroll(msg.arg1, msg.arg2);
					break;

				default:
					break;
				}
			};
		};

		public MagicTextView(Context context) {
			super(context);
		}

		public MagicTextView(Context context, AttributeSet set) {
			super(context, set);
		}

		public MagicTextView(Context context, AttributeSet set, int defStyle) {
			super(context, set, defStyle);
		}

		public void setValue(double value) {
			mCurValue = 0.00;
			mGalValue = isShown() ? value : 0;
			mValue = value;
			mRate = (double) (mValue / 15.00);
			BigDecimal b = new BigDecimal(mRate);
			mRate = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Message msg = mHandler.obtainMessage();
			msg.what = SCROLL;
			mHandler.sendMessage(msg);
		}

		private void doScroll(int state, int scroll) {
			if (refreshing) {
				return;
			}
			rate = 1;
			mGalValue = mValue;
			mHandler.sendEmptyMessage(REFRESH);
		}

}
