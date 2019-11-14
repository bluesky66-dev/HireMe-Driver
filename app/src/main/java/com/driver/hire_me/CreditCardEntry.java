package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.driver.hire_me.CreditCardUtil.CardType;
import com.driver.hire_me.CreditCardUtil.CreditCardFieldDelegate;




public class CreditCardEntry extends HorizontalScrollView implements
		OnTouchListener, OnGestureListener, CreditCardFieldDelegate {

	private Context context;

	private LinearLayout container;

	private ImageView cardImage;
	private ImageView backCardImage;
	private CreditCardText creditCardText;
	private ExpDateText expDateText;
	private SecurityCodeText securityCodeText;
	


	private TextView textFourDigits;

	private TextView textHelper;

	private boolean showingBack;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public CreditCardEntry(Context context) {
		super(context);

		this.context = context;

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		int width, height;

		if (currentapiVersion < 13) {
			width = display.getWidth(); // deprecated
			height = display.getHeight();
		} else {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);

		this.setHorizontalScrollBarEnabled(false);
		this.setOnTouchListener(this);
		this.setSmoothScrollingEnabled(true);

		container = new LinearLayout(context);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		container.setOrientation(LinearLayout.HORIZONTAL);

		creditCardText = new CreditCardText(context);
		creditCardText.setDelegate(this);
		creditCardText.setWidth((int) (width));
		container.addView(creditCardText);

		textFourDigits = new TextView(context);
		textFourDigits.setTextSize(20);
		container.addView(textFourDigits);

		expDateText = new ExpDateText(context);
		expDateText.setDelegate(this);
		container.addView(expDateText);

		securityCodeText = new SecurityCodeText(context);
		securityCodeText.setDelegate(this);
		container.addView(securityCodeText);

		
		this.addView(container);

		creditCardText.requestFocus();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		focusOnField(creditCardText);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onCardTypeChange(CardType type) {
		cardImage.setImageResource(CreditCardUtil.cardImageForCardType(type,
				false));
		backCardImage.setImageResource(CreditCardUtil.cardImageForCardType(
				type, true));
		updateCardImage(false);
	}

	@Override
	public void onCreditCardNumberValid() {
		focusOnField(expDateText);

		String number = creditCardText.getText().toString();
		int length = number.length();
		String digits = number.substring(length - 4);
		textFourDigits.setText(digits);
		Log.i("CreditCardNumber", number);
	}

	@Override
	public void onExpirationDateValid() {
		focusOnField(securityCodeText);
	}

	@Override
	public void onSecurityCodeValid() {
			
	}

	
	@Override
	public void onBadInput(final EditText field) {
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		field.startAnimation(shake);
		field.setTextColor(Color.RED);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				field.setTextColor(Color.BLACK);
			}
		}, 1000);
	}

	public void setCardImageView(ImageView image) {
		cardImage = image;
	}

	public void updateCardImage(boolean back) {
		if (showingBack != back) {
			flipCardImage();
		}

		showingBack = back;
	}

	public void flipCardImage() {
		FlipAnimator animator = new FlipAnimator(cardImage, backCardImage,
				backCardImage.getWidth() / 2, backCardImage.getHeight() / 2);
		if (cardImage.getVisibility() == View.GONE) {
			animator.reverse();
		}
		cardImage.startAnimation(animator);
	}

	@Override
	public void focusOnField(CreditEntryFieldBase field) {
		field.setFocusableInTouchMode(true);
		field.requestFocus();
		field.setFocusableInTouchMode(false);

		if (this.textHelper != null) {
			this.textHelper.setText(field.helperText());
		}

		if (field.getClass().equals(CreditCardText.class)) {
			new CountDownTimer(1000, 20) {

				public void onTick(long millisUntilFinished) {
					CreditCardEntry.this.scrollTo((int) (millisUntilFinished),
							0);
				}

				public void onFinish() {
					CreditCardEntry.this.scrollTo(0, 0);
				}
			}.start();
		} else {
			new CountDownTimer(1500, 20) {

				public void onTick(long millisUntilFinished) {
					CreditCardEntry.this.scrollTo(
							(int) (2000 - millisUntilFinished), 0);
				}

				public void onFinish() {
					

				}
			}.start();
		}

		if (field.getClass().equals(SecurityCodeText.class)) {
			((SecurityCodeText) field).setType(creditCardText.getType());
			updateCardImage(true);
			
		} else {
			updateCardImage(false);
		}
	}

	@Override
	public void focusOnPreviousField(CreditEntryFieldBase field) {
		if (field.getClass().equals(ExpDateText.class)) {
			focusOnField(creditCardText);
		} else if (field.getClass().equals(SecurityCodeText.class)) {
			focusOnField(expDateText);
		} 
	}



	public void setBackCardImage(ImageView backCardImage) {
		this.backCardImage = backCardImage;
	}



	public void setTextHelper(TextView textHelper) {
		this.textHelper = textHelper;
	}

	public boolean isCreditCardValid() {
		
		return creditCardText.isValid() && expDateText.isValid()
				&& securityCodeText.isValid();
	}

	public CreditCard getCreditCard() {
		if (isCreditCardValid()) {

			return new CreditCard(creditCardText.getText().toString(),
					expDateText.getText().toString(), securityCodeText
							.getText().toString());
			
		} else {
			return null;
		}
	}
}
