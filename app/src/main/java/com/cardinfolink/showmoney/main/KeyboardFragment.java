package com.cardinfolink.showmoney.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardinfolink.showmoney.R;
import com.cardinfolink.showmoney.base.BaseFragment;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jie on 2016/12/23.
 */

public class KeyboardFragment extends BaseFragment {

    @Bind(R.id.tv_amount)
    TextView tvAmount;

    private final String DEFAULT_AMOUNT = "0.00";

    private final String AMOUNT_SIGN = "€";

    private Stack<String> amountStack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amountStack = new Stack<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard_layout, container, false);
        ButterKnife.bind(this, view);
        formatAmount();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_qr_pay, R.id.btn_clear, R.id.btn_seven, R.id.btn_eight, R.id.btn_nine, R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_zero, R.id.btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qr_pay:
                gotoQRPay();
                break;
            case R.id.btn_clear:
                popAll();
                break;
            case R.id.btn_seven:
                push(getString(R.string.str_key_seven));
                break;
            case R.id.btn_eight:
                push(getString(R.string.str_key_eight));
                break;
            case R.id.btn_nine:
                push(getString(R.string.str_key_nine));
                break;
            case R.id.btn_four:
                push(getString(R.string.str_key_four));
                break;
            case R.id.btn_five:
                push(getString(R.string.str_key_five));
                break;
            case R.id.btn_six:
                push(getString(R.string.str_key_six));
                break;
            case R.id.btn_one:
                push(getString(R.string.str_key_one));
                break;
            case R.id.btn_two:
                push(getString(R.string.str_key_two));
                break;
            case R.id.btn_three:
                push(getString(R.string.str_key_three));
                break;
            case R.id.btn_zero:
                push(getString(R.string.str_key_zero));
                break;
            case R.id.btn_del:
                pop();
                break;
            default:
                break;
        }
    }

    private void gotoQRPay() {

    }

    private String getAmount() {
        String amount = "";
        if (!amountStack.isEmpty()) {
            for (String num : amountStack) {
                amount = amount + num;
            }
        } else {
            amount = "0";
        }
        return amount;
    }

    private void pop() {
        if (!amountStack.isEmpty()) {
            amountStack.pop();
            formatAmount();
        }
    }

    private void popAll() {
        while (!amountStack.isEmpty()) {
            amountStack.pop();
        }
        formatAmount();
    }

    private void push(String num) {
        if (amountStack.size() >= 12 || ("0".equals(num) && amountStack.isEmpty())) {
            return;
        }
        amountStack.push(num);
        formatAmount();
    }

    private void formatAmount() {
        String amount = getAmount();
        BigDecimal bigDecimal = new BigDecimal(amount);
        bigDecimal = bigDecimal.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String format = String.format(Locale.getDefault(), "%s %s", AMOUNT_SIGN, bigDecimal.toString());
        SpannableString ss = new SpannableString(format);

        int textSize, signSize;//dp
        int textColor;
        if (amountStack.size() <= 8) {
            textSize = 36;
            signSize = 20;
        } else if (amountStack.size() <= 9) {
            textSize = 34;
            signSize = 19;
        } else if (amountStack.size() <= 10) {
            textSize = 32;
            signSize = 18;
        } else if (amountStack.size() <= 11) {
            textSize = 30;
            signSize = 17;
        } else {
            textSize = 28;
            signSize = 16;
        }
        if (amountStack.isEmpty()) {
            textColor = Color.parseColor("#999999");
        } else {
            textColor = Color.BLACK;
        }
        ss.setSpan(new AbsoluteSizeSpan(signSize, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new SuperscriptSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//上标
        ss.setSpan(new AbsoluteSizeSpan(textSize, true), 1, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAmount.setText(ss);
        tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tvAmount.setTextColor(textColor);
    }
}