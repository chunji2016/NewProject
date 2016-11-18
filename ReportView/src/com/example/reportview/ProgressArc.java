package com.example.reportview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;


public class ProgressArc extends View {
	
	private Paint paint_black, paint_white;
	private RectF rectF;
	private float tb;
	private int black_color = 0x70000000; // 底黑色
	private int white_color = 0xddffffff; // 白色
	private int score;
	private float arc_y = 0f;
	private int score_text;
	
	public ProgressArc(Context context, int score) {
		this(context, null);
		initView(score);
	}
	
	public ProgressArc(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressArc(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private void initView(int score) {
		this.score = score;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb); //尺寸
		paint_black = new Paint();		//底部圆圈
		paint_black.setAntiAlias(true); //设置防锯齿
		paint_black.setColor(black_color); //设置颜色
		paint_black.setStrokeWidth(tb * 0.5f); //设置线条的宽度
		paint_black.setStyle(Style.STROKE);	//设置笔刷类型为“线条”
		
		paint_white = new Paint();		//前端白色圆圈笔刷
		paint_white.setAntiAlias(true);  //设置防锯齿
		paint_white.setColor(white_color); //设置颜色
		paint_white.setTextSize(tb*6.0f); //文字大小
		paint_white.setStrokeWidth(tb * 0.3f); //设置线条的宽度
		paint_white.setTextAlign(Align.CENTER); //文字显示位置
		paint_white.setStyle(Style.STROKE);	//设置笔刷类型为“线条”
		
		rectF = new RectF();
		rectF.set(tb * 0.5f, tb * 0.5f, tb * 18.5f, tb * 18.5f);
		//设置本控件布局的宽、高
		setLayoutParams(new LayoutParams((int) (tb * 19.5f), (int) (tb * 19.5f)));
		
		//View变化监听器
		this.getViewTreeObserver().addOnPreDrawListener(
				// 用于在屏幕上画 View 之前，要做什么额外的工作 
				new OnPreDrawListener() {
					// view绘制前，做什么事
					public boolean onPreDraw() {
						new DrawThread(); //创建新的绘画线程，并移除本监听对象
						getViewTreeObserver().removeOnPreDrawListener(this);
						return false;
					}
				});
	}

	protected void onDraw(Canvas c) {
		super.onDraw(c);
		c.drawArc(rectF, -90, 360, false, paint_black); //画圆弧， 扫过角度为360度，就是一圈
		c.drawArc(rectF, -90, arc_y, false, paint_white); // 画圆弧， 扫过角度arc_y逐步递增
		c.drawText("" + score_text, tb * 9.7f, tb * 11.0f, paint_white);
	}
	
	class DrawThread implements Runnable {
		private Thread thread;
		private int statek;
		int count;

		public DrawThread() {
			thread = new Thread(this);
			thread.start();
		}		
		
		
		@Override
		public void run() {
			while (true) {
				switch (statek) {
				case 0:
					try {
						Thread.sleep(200); //等待200毫秒开始绘制
						statek = 1;
					} catch (InterruptedException e) {
					}
					break;
				case 1:
					try {
						Thread.sleep(15);
						arc_y += 3.6f; //逐步递增3.6度
						score_text++;  //分值递增
						count++;
						postInvalidate(); //调用onDraw 重绘
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}
				if (count >= score)
					break;
			}
		
		}
		
	}
}
