package com.driver.hire_me;



import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.driver.hire_me.CreditCardUtil.CardType;

//public class SecurityCodeText extends Activity, CreditEntryFieldBase 
public class SecurityCodeText extends CreditEntryFieldBase
{

	private CardType type;

	private int length;

	public SecurityCodeText(Context context) {
		super(context);
		init();
	}

	public SecurityCodeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SecurityCodeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
			
	}

	public void init() {
		super.init();
		setHint("CVV");
	}
	
	/* TextWatcher Implementation Methods */
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void afterTextChanged(Editable s) {

		if (type != null) {
			String number = s.toString();

			if (number.length() == length) {
				delegate.onSecurityCodeValid();
				setValid(true);
				
			}
			else
			{
				setValid(false);
			}
		} else {
			this.removeTextChangedListener(this);
			this.setText("");
			this.addTextChangedListener(this);
		}

	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
		this.length = CreditCardUtil.securityCodeValid(type);
		
		setFilters(new InputFilter[] { new InputFilter.LengthFilter(length) });
		
	}
	
	@Override
	public String helperText() {
		return context.getString(R.string.SecurityCodeHelp);
	}
}
