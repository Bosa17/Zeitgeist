package com.getcinderella.app.Fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.getcinderella.app.Models.RemoteUserConnection;
import com.getcinderella.app.R;
import com.getcinderella.app.Utils.DataHelper;
import com.getcinderella.app.Utils.FileUtils;
import com.getcinderella.app.Utils.Permissions;
import com.getcinderella.app.Utils.ScratchCardView;


public class RemoteCardDialog extends BlurPopupWindow {

    private static String mRemoteUserFb_dp;
    private static String mRemoteUserId;
    private static String mRemoteUserName;
    private static String mRemoteUserQuote;
    private static Context context;
    private static boolean mRemoteUserIsPrivate=false;
    private static long mRemoteUserCharisma;
    private Bitmap bmp;
    private RemoteUserConnection remoteUser;
    private DataHelper dataHelper;
    private TextView mUserRemote;
    private Button cont;
    private Button cont2;
    private LinearLayout addKarmaDialog;
    private LinearLayout remoteScv;
    private LinearLayout remotePrivate;
    private ProgressBar roundandround;
    private RatingBar mUserAddCharisma;
    private ImageView scratch;
    private Button addKarmaConfirm;
    private ScratchCardView scv;
    public RemoteCardDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View createContentView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_remote_dialog, parent, false);
        scv=view.findViewById(R.id.remoteUserMask_scv);
        remoteUser=new RemoteUserConnection();
        dataHelper=new DataHelper(getContext());
        mUserRemote=view.findViewById(R.id.remoteUserName_scv);
        scratch = view.findViewById(R.id.scratch);
        mUserAddCharisma =view.findViewById(R.id.remoteUserAddCharisma);
        addKarmaConfirm=view.findViewById(R.id.addKarmaConfirm);
        remoteScv=view.findViewById(R.id.remoteScv);
        remotePrivate=view.findViewById(R.id.remotePrivate);
        roundandround=view.findViewById(R.id.roundandround);
        cont=view.findViewById(R.id.cont);
        cont2=view.findViewById(R.id.cont2);
        addKarmaDialog=view.findViewById(R.id.addKarmaDialog);
        new AsyncTask<Void, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... voids) {
                bmp = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(mRemoteUserFb_dp).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    final int width = options.outWidth;
                    final int height = options.outHeight;

                    int inSampleSize = 1;
                    if (height > 300 || width > 300) {
                        int halfWidth = width / 2;
                        int halfHeight = height / 2;

                        while ((halfHeight / inSampleSize) >= 300 && (halfWidth / inSampleSize) >= 300
                                && !isCancelled() )
                        {
                            inSampleSize *= 2;
                        }
                    }

                    if (isCancelled()) {
                        return null;
                    }

                    options.inSampleSize = inSampleSize;
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bmp = BitmapFactory.decodeStream(input,null,options);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return bmp;
            }
            @Override
            protected void onPostExecute(@NonNull Bitmap result) {

                //Add image to ImageView
                if (result!=null) {
                    scv.setImageBitmap(result);
                    init();
                }
                else {
                    mRemoteUserIsPrivate=true;
                    init();
                    return;
                }

            }


        }.execute();
        scv.setRevealListener(new ScratchCardView.IRevealListener() {
            @Override
            public void onRevealed(ScratchCardView tv) {
                mUserRemote.setVisibility(VISIBLE);
                cont.setVisibility(VISIBLE);
            }

            @Override
            public void onRevealPercentChangedListener(ScratchCardView siv, float percent) {
                if (percent>0.1){
                    scratch.clearAnimation();
                    scratch.setVisibility(GONE);
                }
                if (percent>0.5){
                    siv.reveal();
                }
            }
        });
        addKarmaConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dataHelper.updateCharismaWithUid(mRemoteUserId, mRemoteUserCharisma +(long) mUserAddCharisma.getRating());
                updateWidgets();
            }
        });
        cont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                remoteUser.setRemoteUserId(mRemoteUserId);
                remoteUser.setRemoteUserQuote(mRemoteUserQuote);
                remoteUser.setRemoteUserName(mRemoteUserName);
                remoteUser.setRemoteUserCharisma(mRemoteUserCharisma);
                if (Permissions.hasAllPermissions(getContext())) {
                    remoteUser.setRemoteUserDp(FileUtils.storeImage(bmp));
                }
                else{
                    Toast.makeText(context,"Cannot make connections because permissions not granted", Toast.LENGTH_LONG).show();
                }
                if (!dataHelper.getRemoteUserIds().contains(mRemoteUserId)) {
                    long conn_no=dataHelper.getPartners()+1;
                    dataHelper.putPartner(conn_no);
                    dataHelper.addNewRemoteUser(remoteUser, conn_no);
                }
                dismiss();
            }
        });
        cont2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mUserRemote.setText(mRemoteUserName);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);
        view.setVisibility(INVISIBLE);
        return view;
    }

    private void init(){
        roundandround.setVisibility(GONE);
        addKarmaDialog.setVisibility(VISIBLE);
    }

    private void updateWidgets(){
        if (!mRemoteUserIsPrivate){
            addKarmaDialog.setVisibility(GONE);
            remoteScv.setVisibility(VISIBLE);
            Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            animShake.setStartOffset(300);
            scratch.startAnimation(animShake);
        }
        else {
            addKarmaDialog.setVisibility(GONE);
            remotePrivate.setVisibility(VISIBLE);
        }
    }
    @Override
    protected void onShow() {
        super.onShow();
        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                getContentView().setVisibility(VISIBLE);
                int height = getContentView().getMeasuredHeight();
                ObjectAnimator.ofFloat(getContentView(), "translationY", height, 0).setDuration(getAnimationDuration()).start();
            }
        });
    }


    @Override
    protected ObjectAnimator createShowAnimator() {
        return null;
    }

    @Override
    protected ObjectAnimator createDismissAnimator() {
        int height = getContentView().getMeasuredHeight();
        return ObjectAnimator.ofFloat(getContentView(), "translationY", 0, height).setDuration(getAnimationDuration());
    }

    public static class Builder extends BlurPopupWindow.Builder<RemoteCardDialog> {
        public Builder(Context c,String ruid,long charisma,String url,String name,String quote,boolean isPrivate) {
            super(c);
            context=c;
            mRemoteUserFb_dp=url;
            mRemoteUserId=ruid;
            mRemoteUserCharisma =charisma;
            mRemoteUserName=name;
            mRemoteUserQuote=quote;
            mRemoteUserIsPrivate=isPrivate;
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000).setDismissOnClickBack(false)
                    .setDismissOnTouchBackground(false);
        }

        @Override
        protected RemoteCardDialog createPopupWindow() {
            return new RemoteCardDialog(mContext);
        }
    }
}