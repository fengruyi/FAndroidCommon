package com.fengruyi.common.view;



import com.fengruyi.common.app.BaseApplication;
import com.fengruyi.common.util.ViewUtil;
import com.fengruyi.fandroidcommon.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 应用 通用带有点击滑动切换AB标题的布局,sdk11以上能使用
 * @author fengruyi
 *
 */
public class TabSwitchPagerView extends RelativeLayout{
    /** 切换页面的水平指示条*/
    private View mTabDiliver;
    
    private ViewPager mViewPager;
    /**指示条的长度*/
    private int tabdiliverWidth;
    
    /**一个tab的长度*/
    private int tabWidth;
    /**指示条距离一个tab左边的距离*/
    private int marginLeft;
    private SwitchChangeListener listner;
    private TextView[] tabs;//tab的个数
    public TabSwitchPagerView(Context context) {
        super(context);
    }
    
    @SuppressLint("NewApi")
	public TabSwitchPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,  
                R.styleable.Tab_switch_style);  
     int tabLength = a.getInt(R.styleable.Tab_switch_style_tabLength, 3);
     a.recycle();
    tabs = new TextView[tabLength];
    RelativeLayout.LayoutParams rtlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout lt = new LinearLayout(context);
    lt.setId(1);
    LinearLayout.LayoutParams ltlp = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
    ltlp.weight=1;
    TextView tv;
    int padding = getResources().getDimensionPixelSize(R.dimen.tab_title_padding);
    for (int i = 0; i < tabLength; i++) {
         final int index = i;
         tv = new TextView(context);
         tv.setText("tab"+i);
         tv.setTextAppearance(context, R.style.tab_title_style);
         tv.setBackgroundResource(R.drawable.btn_tab);
         tv.setGravity(Gravity.CENTER);
         tv.setPadding(0, padding, 0, padding);
         lt.addView(tv,ltlp);
         tv.setId(i+2);
         tv.setOnClickListener(new OnClickListener() {
            
            public void onClick(View arg0) {
                mViewPager.setCurrentItem(index, true);
                
            }
        });
         tabs[i] = tv;
    }
    addView(lt, rtlp);
    
    RelativeLayout.LayoutParams rtlp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.diliver_line_height));
    View diliver = new View(context);
    diliver.setId(10);
    diliver.setBackgroundColor(getResources().getColor(R.color.tab_color));
    rtlp2.addRule(RelativeLayout.BELOW, lt.getId());
    addView(diliver,rtlp2);
    
    RelativeLayout.LayoutParams rtlp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.tab_diliver_height));
    mTabDiliver = new View(context);
    rtlp3.addRule(RelativeLayout.ALIGN_BOTTOM, lt.getId());
    mTabDiliver.setBackgroundColor(getResources().getColor(R.color.tab_color));
    addView(mTabDiliver,rtlp3);
    
    RelativeLayout.LayoutParams rtlp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
    rtlp4.addRule(RelativeLayout.BELOW, diliver.getId());
    mViewPager = new ViewPager(context);
    mViewPager.setId(100);
    addView(mViewPager, rtlp4);
    
    mViewPager.setOffscreenPageLimit(tabLength);
    tabWidth = (int)BaseApplication.sWidthPixels/tabLength;
    tabdiliverWidth = (int)BaseApplication.sWidthPixels/7;
    ViewUtil.setViewWidth(mTabDiliver,tabdiliverWidth);//设置水平指示条占宽的一半    
    mViewPager.setOnPageChangeListener(pageChangeListener);
    marginLeft = (tabWidth - tabdiliverWidth)/2;
    tabs[0].setSelected(true);//默认选第一个
    mTabDiliver.setTranslationX(marginLeft);
   
    }
    
    /**
     * 设置tab标题文字
     * @param titleId
     * 
     */
    public void setTabTitle(int... titleId){
        for(int i = 0,length = tabs.length;i<length;i++){
            tabs[i].setText(titleId[i]);
        }
       
    }
    
    /**
     * 设置tab标题文字
     * 
     */
    public void setTabTtile(String... title){
        for(int i = 0,length = tabs.length;i<length;i++){
            tabs[i].setText(title[i]);
        }
    }
    
    /**
     * 为viewpage适配apapter
     * @param adapter
     */
    public void setPagerAdapter(PagerAdapter adapter){
        mViewPager.setAdapter(adapter);
    }
    
    /**
     * 返回当前viewpager显示的索引
     * @return
     */
    public int getCurrenItem(){
        return mViewPager.getCurrentItem();
    }
    
    public void setCurrentItem(int index){
        mViewPager.setCurrentItem(index);
    }
  
    @SuppressLint("NewApi")
    private OnPageChangeListener pageChangeListener =  new OnPageChangeListener() {
        private boolean isAnim = false;
        private int pos = 0;        
        public void onPageSelected(int position) {
            mTabDiliver.setTranslationX(position*tabWidth + marginLeft);
            pos = position;
            switchTab(position);
            if(listner!=null){
                listner.onPageSelected(position);
            }
        }

        //arg1:当前页面偏移的百分比
        
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if(isAnim){
                mTabDiliver.setTranslationX((arg0*tabWidth + marginLeft)+tabWidth*arg1);
                
            }
        }
        
        public void onPageScrollStateChanged(int arg0) {
             if(arg0==1){ //开始状态，开始滑动的时候就会执行
                    isAnim  = true;
              }else if(arg0==2){ //分界状态,也就是松手pager切换页面过程，松手后会执行onPageScrolled方法的
                    isAnim = false;
                    mTabDiliver.setTranslationX(pos*tabWidth + marginLeft);
              }else if(arg0==0){ //滑动停止过程，结束状态
                    mTabDiliver.setTranslationX(pos*tabWidth + marginLeft);
              }
        }
    };
    
    /**
     * tab页切换
     * @param index
     */
    @SuppressLint("NewApi")
    private void switchTab(int index){
        for (int i = 0; i < tabs.length; i++) {
            if(index == i){
                tabs[i].setSelected(true);
            }else{
                tabs[i].setSelected(false);
            }   
        }
    }
    /**
     * viewpager当前位置
     * @return
     */
    public int getCurrentPosition(){
        return mViewPager.getCurrentItem();
    }
    
    public SwitchChangeListener getListner() {
        return listner;
    }

    public void setListner(SwitchChangeListener listner) {
        this.listner = listner;
    }

    public interface SwitchChangeListener{
        public void onPageSelected(int index);
    }
}
