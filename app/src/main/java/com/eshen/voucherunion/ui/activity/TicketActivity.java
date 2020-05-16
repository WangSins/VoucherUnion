package com.eshen.voucherunion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseActivity;
import com.eshen.voucherunion.model.domain.TicketResult;
import com.eshen.voucherunion.presenter.ITicketPagePresenter;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.utils.ToastUtils;
import com.eshen.voucherunion.utils.UrlUtils;
import com.eshen.voucherunion.view.ITicketPageCallback;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/4
 */
public class TicketActivity extends BaseActivity implements ITicketPageCallback {

    private ITicketPagePresenter ticketPresenter;

    private boolean hasTaobaoApp = false;

    @BindView(R.id.ticket_back)
    public View backIv;

    @BindView(R.id.ticket_cover)
    public ImageView coverIv;

    @BindView(R.id.ticket_cover_loading)
    public View coverLoadingIv;

    @BindView(R.id.ticket_cover_retry)
    public TextView coverRetryTv;

    @BindView(R.id.ticket_code)
    public EditText codeEt;

    @BindView(R.id.ticket_copy_or_open_button)
    public TextView copyOrOpenTv;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initPresenter() {
        ticketPresenter = PresenterManager.getInstance().getTicketPagePresenter();
        ticketPresenter.registerViewCallback(this);
        //判断是否安装淘宝
        //com.taobao.taobao
        //检查是否安装
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            hasTaobaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            hasTaobaoApp = false;
        }
        //根据这个值去修改UI
        copyOrOpenTv.setText(hasTaobaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected void initEvent() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        copyOrOpenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令
                //拿到内容
                String ticketCode = codeEt.getText().toString().trim();
                if (TextUtils.isEmpty(ticketCode)) {
                    ToastUtils.showToast("没获取到淘口令");
                    return;
                }
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板
                ClipData clipData = ClipData.newPlainText("taobao_ticket_code", ticketCode);
                clipboardManager.setPrimaryClip(clipData);
                //判断有没有淘宝
                if (hasTaobaoApp) {
                    //如果有就打开淘宝
                    Intent intent = new Intent();
                    //intent.setAction("android.intent.action.MAIN");
                    //intent.addCategory("android.intent.category.LAUNCHER");
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    intent.setComponent(componentName);
                    startActivity(intent);
                } else {
                    //没有就提示复制成功
                    ToastUtils.showToast("已经复制，粘贴分享或打开淘宝");
                }
            }
        });
    }

    @Override
    protected void release() {
        if (ticketPresenter != null) {
            ticketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (coverIv != null && !TextUtils.isEmpty(cover)) {
            ViewGroup.LayoutParams layoutParams = coverIv.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = (width > height ? width : height) / 2;
            String coverPath = UrlUtils.getCoverPath(cover);
            Glide.with(this).load(coverPath).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (coverLoadingIv != null) {
                        coverLoadingIv.setVisibility(View.GONE);
                    }
                    if (coverRetryTv != null) {
                        coverRetryTv.setVisibility(View.GONE);
                    }
                    coverIv.setImageDrawable(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
        if (codeEt != null && result.getData().getTbk_tpwd_create_response() != null) {
            codeEt.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
    }

    @Override
    public void onError() {
        if (coverLoadingIv != null) {
            coverLoadingIv.setVisibility(View.GONE);
        }
        if (coverRetryTv != null) {
            coverRetryTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (coverLoadingIv != null) {
            coverLoadingIv.setVisibility(View.VISIBLE);
        }
        if (coverRetryTv != null) {
            coverRetryTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {
    }
}
