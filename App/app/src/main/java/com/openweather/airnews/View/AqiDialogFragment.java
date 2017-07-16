package com.openweather.airnews.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openweather.airnews.DataBase.DBAccess;
import com.openweather.airnews.R;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


/**
 * Simple fragment with blur effect behind.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AqiDialogFragment extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";

    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";

    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private DBAccess mAccess;
    private int mIndex=0;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static AqiDialogFragment newInstance(int radius,
                                                float downScaleFactor,
                                                boolean dimming,
                                                boolean debug) {
        AqiDialogFragment fragment = new AqiDialogFragment();
        Bundle args = new Bundle();
        args.putInt(
                BUNDLE_KEY_BLUR_RADIUS,
                radius
        );
        args.putFloat(
                BUNDLE_KEY_DOWN_SCALE_FACTOR,
                downScaleFactor
        );
        args.putBoolean(
                BUNDLE_KEY_DIMMING,
                dimming
        );
        args.putBoolean(
                BUNDLE_KEY_DEBUG,
                debug
        );

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_aqi, null);
        Button btn=(Button)view.findViewById(R.id.button);
        TextView tvDes=(TextView)view.findViewById(R.id.tvDes);
        TextView tvNormalsuggest=(TextView)view.findViewById(R.id.tvNormalsuggest);
        /*final TextView label = ((TextView) view.findViewById(R.id.textView));
        label.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(label, Linkify.WEB_URLS);*/
        builder.setView(view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        mAccess = new DBAccess(getActivity(), "weather", null,1);
        Cursor cl2 = mAccess.getData("AIR", null, null);
        cl2.moveToFirst();
        Cursor cl3 = mAccess.getData("AQI", null, null);
        cl3.moveToFirst();
        if(cl2.getShort(3)>=0&&cl2.getShort(3)<=50){
            mIndex=1;
        }
        else if(cl2.getShort(3)>=51&&cl2.getShort(3)<=100){
            mIndex=2;
        }
        else if(cl2.getShort(3)>=101&&cl2.getShort(3)<=150){
            mIndex=3;
        }
        else if(cl2.getShort(3)>=151&&cl2.getShort(3)<=200){
            mIndex=4;
        }
        else if(cl2.getShort(3)>=201&&cl2.getShort(3)<=300){
            mIndex=5;
        }
        else if(cl2.getShort(3)>=301&&cl2.getShort(3)<=500){
            mIndex=6;
        }
        //Log.e("DataAQI",cl3.getString(2));
        cl3.moveToPosition(mIndex-1);
        tvDes.setText(cl3.getString(3));
        tvNormalsuggest.setText(cl3.getString(2));
        return builder.create();
    }



    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }
}
