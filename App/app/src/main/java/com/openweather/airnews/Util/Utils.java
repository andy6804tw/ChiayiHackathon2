/*
 * Copyright 2017 GuDong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.openweather.airnews.Util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.openweather.airnews.R;

public class Utils {
    private static final int LEVEL_AQI_1 = 1;
    private static final int LEVEL_AQI_2 = 2;
    private static final int LEVEL_AQI_3 = 3;
    private static final int LEVEL_AQI_4 = 4;
    private static final int LEVEL_AQI_5 = 5;
    private static final int LEVEL_AQI_6 = 6;
    private static final int LEVEL_AQI_7 = 7;

    /**
     * 根据资源 ID 获取颜色
     * @param resId
     * @return
     */
    public static final int getColor(Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context,resId);
    }

    public static int getAqiIndex(int aqiValue){
        int aqiIndex = 1;
        if(aqiValue>0 && aqiValue<=50){
            aqiIndex = 1;
        }else if(aqiValue<=100){
            aqiIndex = 2;
        }else if(aqiValue<=150){
            aqiIndex = 3;
        }else if(aqiValue<=200){
            aqiIndex = 4;
        }else if(aqiValue<=300){
            aqiIndex = 5;
        }else{
            aqiIndex = 6;
        }
        return aqiIndex;
    }



    /**
     * AQI 顏色等級
     * @param aqiIndex
     * @return
     */
    public static @ColorRes
    int getIndexColor(int aqiIndex) {
        switch (aqiIndex) {
            case LEVEL_AQI_1:
                return R.color.alermAir1_1;
            case LEVEL_AQI_2:
                return R.color.alermAir2_2;
            case LEVEL_AQI_3:
                return R.color.alermAir3;
            case LEVEL_AQI_4:
                return R.color.alermAir4;
            case LEVEL_AQI_5:
                return R.color.alermAir5;
            case LEVEL_AQI_6:
                return R.color.alermAir6;
            default:
                return R.color.alermAir6;
        }
    }

    public static @StringRes
    int getIndexDescription(int aqiIndex) {
        switch (aqiIndex) {
            case LEVEL_AQI_1:
                return R.string.pm_describes_1;
            case LEVEL_AQI_2:
                return R.string.pm_describes_2;
            case LEVEL_AQI_3:
                return R.string.pm_describes_3;
            case LEVEL_AQI_4:
                return R.string.pm_describes_4;
            case LEVEL_AQI_5:
                return R.string.pm_describes_5;
            case LEVEL_AQI_6:
                return R.string.pm_describes_6;
            default:
                return R.string.pm_describes_1;
        }
    }
}
