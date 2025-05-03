package com.yhsh.flowstudy.kit;

import com.yhsh.flowstudy.App;

public class SizeUtils {
    public static int dip2px(float dpValue) {
        float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
