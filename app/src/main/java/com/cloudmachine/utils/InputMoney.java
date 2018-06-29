package com.cloudmachine.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class InputMoney implements InputFilter {


    private EditText mEditText;

    public InputMoney(EditText editText) {
        mEditText = editText;
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        if (source.toString().equals(".") && dstart == 0 && dend == 0) {//判断小数点是否在第一位
            mEditText.setText(0 + "" + source + dest);//给小数点前面加0
            mEditText.setSelection(2);//设置光标
        }

        int len = dest.length();
        String destStr = dest.toString();
        if (destStr.contains(".") && (len - destStr.indexOf(".")) > 2) {//判断小数点是否存在并且小数点后面是否已有两个字符
            if ((len - dstart) < 3) {//判断现在输入的字符是不是在小数点后面
                return "";//过滤当前输入的字符
            }
        }
        return null;
    }

}  