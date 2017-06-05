package com.cloudmachine.helper;

import android.content.Context;

import com.cloudmachine.struc.Member;
import com.cloudmachine.utils.MemeberKeeper;

/**
 * Created by xiaojw on 2017/5/6.
 */

public class UserHelper {

    public static Long getWjdxID(Context context) {
        Member member = MemeberKeeper.getOauth(context);
        if (member != null) {
            Long id = member.getWjdsId();
            if (id != null) {
                return id;

            }
        }
        return null;
    }

    public static boolean isLogin(Context context) {
        return MemeberKeeper.getOauth(context) != null;
    }

}
