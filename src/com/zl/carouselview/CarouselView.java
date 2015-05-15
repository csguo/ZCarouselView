package com.zl.carouselview;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class CarouselView extends RelativeLayout {

	int width;// ��Ļ�Ŀ�ȣ����أ�
	int height;// ��Ļ�߶ȣ����أ� ����
	
	// �����ֲ�ͼƬ
	private ImageView iv_cv_left;
	private ImageView iv_cv_middle;
	private ImageView iv_cv_right;

	private Drawable image_left;
	private Drawable image_middle;
	private Drawable image_right;

	private Context context;

	int flag = 1;// �����ֲ�˳�����

	private ImageOnClickListener imageOnClickListener; // �ֲ�ͼ�������

	int rightX = 0; // 3���ֲ�ͼ����ֵ
	int rightXs = 0;// 3���ֲ�ͼ��һ����ֵ
	int middleX = 0;// 2���ֲ�ͼ����ֵ
	int middleXs = 0;// 2���ֲ�ͼ��һ����ֵ

	private Status status;

	public enum Status {// ö�ٳ��ֲ�ͼright״̬�������ж��м��ֲ�ͼ�Ĳ���
		Open, Close;
	}

	public boolean isOpen() {// �ֲ�ͼright�Ƿ��
		return status == Status.Open;
	}

	private Handler mHandler = new Handler() {
		@SuppressLint({ "ResourceAsColor", "NewApi" })
		@Override
		public void handleMessage(android.os.Message msg) {
			if (flag % 4 == 1) {//�ֲ�ͼright�����ƶ����رգ�
				ObjectAnimator
						.ofFloat(iv_cv_right, "translationX", 0, width / 10 * 7)
						.setDuration(1000).start();
				status = Status.Close;

			}
			if (flag % 4 == 2) {//�ֲ�ͼmiddle�����ƶ����رգ�
				ObjectAnimator
						.ofFloat(iv_cv_middle, "translationX", 0,
								width / 10 * 7).setDuration(1000).start();

				status = Status.Close;
			}
			if (flag % 4 == 3) {//�ֲ�ͼmiddle�����ƶ�����ԭ��
				ObjectAnimator
						.ofFloat(iv_cv_middle, "translationX", width / 10 * 7,
								0).setDuration(1000).start();
				status = Status.Close;
			}
			if (flag % 4 == 0) {//�ֲ�ͼright�����ƶ�����ԭ��
				ObjectAnimator
						.ofFloat(iv_cv_right, "translationX", width / 10 * 7, 0)
						.setDuration(1000).start();
				status = Status.Open;

			}
			flag++;
			mHandler.sendEmptyMessageDelayed(1, 4000);

		};
	};

	public CarouselView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.CarouselView);
		// ���Զ���ؼ�������Ժ�attrs�ļ��е����Թ��������������Ϳ�����xml�ļ���ֱ�����øÿؼ���������

		this.image_left = ta.getDrawable(R.styleable.CarouselView_image_left);
		this.image_middle = ta
				.getDrawable(R.styleable.CarouselView_image_middle);
		this.image_right = ta.getDrawable(R.styleable.CarouselView_image_right);
		ta.recycle();
		initView(context);

	}

	/**
	 * ��ʼ������
	 */
	private void initView(Context context) {
		this.context = context;

		iv_cv_left = new ImageView(context);
		iv_cv_middle = new ImageView(context);
		iv_cv_right = new ImageView(context);

		iv_cv_left.setScaleType(ScaleType.CENTER_CROP);
		iv_cv_middle.setScaleType(ScaleType.CENTER_CROP);
		iv_cv_right.setScaleType(ScaleType.CENTER_CROP);

		addView(iv_cv_left);
		addView(iv_cv_middle);
		addView(iv_cv_right);
		iv_cv_left.setImageDrawable(image_left);
		iv_cv_middle.setImageDrawable(image_middle);
		iv_cv_right.setImageDrawable(image_right);
		status = Status.Open;//��ʼ��״̬
		mHandler.sendEmptyMessageDelayed(1, 1000);//��ʼ�Զ��ֲ�

		// ����¼�����
		iv_cv_left.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_middle.getX() < width / 10 * 3) {
					ObjectAnimator
							.ofFloat(iv_cv_middle, "translationX", 0,
									width / 10 * 7).setDuration(1000).start();
					if (iv_cv_right.getX() < width / 10 * 3) {
						ObjectAnimator
								.ofFloat(iv_cv_right, "translationX", 0,
										width / 10 * 7).setDuration(1000)
								.start();
						status = Status.Close;
					}
				} else {
					imageOnClickListener.image_left_Click();

				}
				flag = 3;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});

		iv_cv_middle.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_right.getX() > width / 10 * 3
						&& iv_cv_middle.getX() < width / 10 * 3) {
					imageOnClickListener.image_middle_Click();

				} else {
					if (iv_cv_middle.getX() < width / 10 * 3) {

						ObjectAnimator
								.ofFloat(iv_cv_right, "translationX", 0,
										width / 10 * 7).setDuration(1000)
								.start();
						status = Status.Close;
					} else {
						ObjectAnimator
								.ofFloat(iv_cv_middle, "translationX",
										width / 10 * 7, 0).setDuration(1000)
								.start();
					}
				}
				flag = 2;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});

		iv_cv_right.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_right.getX() < width / 10 * 3) {
					imageOnClickListener.image_right_Click();

				} else {

					ObjectAnimator
							.ofFloat(iv_cv_right, "translationX",
									width / 10 * 7, 0).setDuration(1000)
							.start();
					status = Status.Open;
					if (iv_cv_middle.getX() > width / 10 * 3) {

						ObjectAnimator
								.ofFloat(iv_cv_middle, "translationX",
										width / 10 * 7, 0).setDuration(1000)
								.start();
					}

				}
				flag = 1;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});
		iv_cv_right.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (!isTestCompete) {
					getEventType(ev);
					return false;
				}

				if (isleftrightEvent) {

					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:

						break;
					case MotionEvent.ACTION_MOVE:

						mHandler.removeMessages(1);
						
						rightXs = rightX;
						rightX += (int) ev.getX() - point.x;

						if (rightX >= 0 && rightX <= width / 10 * 7) {

							ViewHelper.setTranslationX(iv_cv_right, rightX);

						} else {
							if (rightX > width / 10 * 7) {
								rightX = width / 10 * 7;
							} else {
								rightX = 0;
							}

							ViewHelper.setTranslationX(iv_cv_right, rightX);

						}

						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						int j = rightX - rightXs;
						if (j > 0) {
							if (rightX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, width / 10 * 7)
										.setDuration(400).start();
								status = Status.Close;
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, 0).setDuration(400)
										.start();
								status = Status.Open;
								flag = 1;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						} else if (j < 0) {
							if (rightX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, width / 10 * 7)
										.setDuration(400).start();
								status = Status.Close;
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, 0).setDuration(400)
										.start();
								status = Status.Open;
								flag = 1;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						}

						isleftrightEvent = false;
						isTestCompete = false;
						break;
					}
				} else {
					switch (ev.getActionMasked()) {
					case MotionEvent.ACTION_UP:
						isleftrightEvent = false;
						isTestCompete = false;
						break;

					default:
						break;
					}
				}
				return false;
			}
		});
		iv_cv_middle.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (isOpen()) {
					return false;
				}
				if (!isTestCompete) {
					getEventType(ev);
					return false;
				}

				if (isleftrightEvent) {

					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:

						break;
					case MotionEvent.ACTION_MOVE:

						mHandler.removeMessages(1);
						
						middleXs = middleX;
						middleX += (int) ev.getX() - point.x;

						if (middleX >= 0 && middleX <= width / 10 * 7) {

							ViewHelper.setTranslationX(iv_cv_middle, middleX);

						} else {
							if (middleX > width / 10 * 7) {
								middleX = width / 10 * 7;
							} else {
								middleX = 0;
							}

							ViewHelper.setTranslationX(iv_cv_middle, middleX);

						}

						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						int z = middleX - middleXs;
						if (z > 0) {
							if (middleX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, width / 10 * 7)
										.setDuration(400).start();
								flag = 3;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, 0).setDuration(400)
										.start();
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						} else if (z < 0) {
							if (middleX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, width / 10 * 7)
										.setDuration(400).start();
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, 0).setDuration(400)
										.start();
								flag = 3;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						}

						isleftrightEvent = false;
						isTestCompete = false;
						break;
					}
				} else {
					switch (ev.getActionMasked()) {
					case MotionEvent.ACTION_UP:
						isleftrightEvent = false;
						isTestCompete = false;
						break;

					default:
						break;
					}
				}
				return false;
			}
		});
	}

	/**
	 * ������view
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
				(int) (width * 0.8f), MeasureSpec.EXACTLY);
		iv_cv_middle.measure(tempWidthMeasure, heightMeasureSpec);
		iv_cv_left.measure(tempWidthMeasure, heightMeasureSpec);
		iv_cv_right.measure(tempWidthMeasure, heightMeasureSpec);
	}

	/**
	 * ����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// Log.e("11111", iv_cv_middle.getMeasuredWidth()+"");
		iv_cv_middle.layout(l + iv_cv_middle.getMeasuredWidth() / 8, t, r
				- iv_cv_middle.getMeasuredWidth() / 8, b);
		iv_cv_left.layout(l, t, r - iv_cv_middle.getMeasuredWidth() / 4, b);
		iv_cv_right.layout(l + iv_cv_middle.getMeasuredWidth() / 4, t, r, b);
	}

	private boolean isTestCompete;
	private boolean isleftrightEvent;

	private Point point = new Point();
	private static final int TEST_DIS = 20;// �ƶ�����

	@SuppressLint("NewApi")
	private void getEventType(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			point.x = (int) ev.getX();
			point.y = (int) ev.getY();
			// super.dispatchTouchEvent(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			int dX = Math.abs((int) ev.getX() - point.x);
			int dY = Math.abs((int) ev.getY() - point.y);
			if (dX >= TEST_DIS && dX > dY) { // ���һ���
				isleftrightEvent = true;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			} else if (dY >= TEST_DIS && dY > dX) { // ���»���
				isleftrightEvent = false;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// super.dispatchTouchEvent(ev);
			isleftrightEvent = false;
			isTestCompete = false;
			break;
		}
	}
	/**
	 * һЩ��¶�������ɸ��������������
	 */
	public void setImageDrawable_left(int i) {

		// ����ͼƬ
		Picasso.with(context).load(i).into(iv_cv_left);

	}

	public void setImageDrawable_middle(int i) {

		Picasso.with(context).load(i).into(iv_cv_middle);

	}

	public void setImageDrawable_right(int i) {

		Picasso.with(context).load(i).into(iv_cv_right);

	}

	public void setImageURL_left(String url, int holder, int error) {

		// ����ͼƬ
		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_left);

	}

	public void setImageURL_middle(String url, int holder, int error) {

		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_middle);

	}

	public void setImageURL_right(String url, int holder, int error) {

		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_right);

	}

	/**
	 * ���õ���¼�����
	 * 
	 */
	public void setOnImageClickListener(
			ImageOnClickListener imageOnClickListener) {
		this.imageOnClickListener = imageOnClickListener;
	}

	public interface ImageOnClickListener {
		public void image_left_Click();

		public void image_middle_Click();

		public void image_right_Click();
	}
}
