package com.eshen.voucherunion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eshen.voucherunion.R;
import com.eshen.voucherunion.base.BaseFragment;
import com.eshen.voucherunion.model.domain.Histories;
import com.eshen.voucherunion.model.domain.IBaseInfo;
import com.eshen.voucherunion.model.domain.SearchRecommend;
import com.eshen.voucherunion.model.domain.SearchResult;
import com.eshen.voucherunion.presenter.ISearchPagePresenter;
import com.eshen.voucherunion.ui.adapter.LinearItemContentAdapter;
import com.eshen.voucherunion.ui.custom.TextFlowLayout;
import com.eshen.voucherunion.utils.KeyboardUtil;
import com.eshen.voucherunion.utils.PresenterManager;
import com.eshen.voucherunion.utils.SizeUtils;
import com.eshen.voucherunion.utils.TicketUtil;
import com.eshen.voucherunion.utils.ToastUtils;
import com.eshen.voucherunion.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Sin on 2020/5/2
 */
public class SearchFragment extends BaseFragment implements ISearchPageCallback {

    private ISearchPagePresenter searchPresenter;

    @BindView(R.id.search_history_view)
    public TextFlowLayout historyView;
    @BindView(R.id.search_history_container)
    public LinearLayout historyContainer;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout recommendView;
    @BindView(R.id.search_recommend_container)
    public LinearLayout recommendContainer;

    @BindView(R.id.search_history_delete)
    public ImageView historyDelete;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout resultContainer;

    @BindView(R.id.search_result_list)
    public RecyclerView resultList;

    private LinearItemContentAdapter searchResultAdapter;

    @BindView(R.id.search_input_box)
    public EditText searchInputBox;

    @BindView(R.id.search_clear_btn)
    public ImageView searchClearBtn;

    @BindView(R.id.search_btn)
    public TextView searchBtn;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View LoadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        resultList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);

            }
        });
        //设置适配器
        searchResultAdapter = new LinearItemContentAdapter();
        resultList.setAdapter(searchResultAdapter);

        //设置相关内容
        resultContainer.setEnableRefresh(false);
        resultContainer.setEnableLoadmore(true);
        resultContainer.setEnableOverScroll(false);
        resultContainer.setBottomHeight(SizeUtils.dip2px(getContext(), 30));
    }

    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getSearchPagePresenter();
        searchPresenter.registerViewCallback(this);
        searchPresenter.getRecommendWords();
        searchPresenter.getHistories();
    }

    @Override
    protected void initListener() {
        searchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度不为零，显示删除按钮,否则隐藏
                searchClearBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                searchBtn.setText(hasInput(false) ? getString(R.string.text_search) : getString(R.string.text_search_cancel));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && searchPresenter != null) {
                    //判断拿到的内容是否为空
                    String keyword = v.getText().toString();
                    if (TextUtils.isEmpty(keyword)) {
                        return false;
                    }
                    //发起搜索
                    toSearch(keyword);
                }
                return false;
            }
        });
        searchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputBox.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInput(false)) {
                    //如果有输入内容搜索
                    toSearch(searchInputBox.getText().toString().trim());
                }else {
                    //回到历史记录界面
                    switch2HistoryPage();
                }
                KeyboardUtil.hide(getContext(), v);
            }
        });
        historyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除历史
                if (searchPresenter != null) {
                    searchPresenter.delHistories();
                }
            }
        });
        historyView.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowTextItemClick(String s) {
                toSearch(s);
            }
        });
        recommendView.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowTextItemClick(String s) {
                toSearch(s);
            }
        });
        searchResultAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                handleItemClick(item);
            }
        });
        resultContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (searchPresenter != null) {
                    searchPresenter.loaderMore();
                }
            }
        });

    }

    private void toSearch(String s) {
        if (searchPresenter != null) {
            searchPresenter.doSearch(s);
            searchInputBox.setText(s);
            searchInputBox.setFocusable(true);
            searchInputBox.setFocusableInTouchMode(true);
            searchInputBox.requestFocus();
            searchInputBox.setSelection(s.length());
            resultList.scrollToPosition(0);
        }

    }

    /**
     * 切换到历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (searchPresenter != null) {
            searchPresenter.getHistories();
        }
        recommendContainer.setVisibility(recommendView.getTextListSize() != 0 ? View.VISIBLE : View.GONE);
        resultContainer.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return searchInputBox.getText().toString().length() > 0;
        } else {
            return searchInputBox.getText().toString().trim().length() > 0;
        }
    }

    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(), item);
    }

    @Override
    protected void onRetryClick() {
        //重新加载内容
        if (searchPresenter != null) {
            searchPresenter.reSearch();
        }
    }

    @Override
    protected void release() {
        if (searchPresenter != null) {
            searchPresenter.unregisterViewCallback(this);
        }
    }


    @Override
    public void onHistoriesLoaded(Histories histories) {
        setUpState(State.SUCCESS);
        if (histories == null || histories.getHistories().size() == 0) {
            historyContainer.setVisibility(View.GONE);
        } else {
            historyContainer.setVisibility(View.VISIBLE);
            historyView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        //更新历史记录
        if (searchPresenter != null) {
            searchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        //设置数据
        searchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        //隐藏历史记录和推荐
        recommendContainer.setVisibility(View.GONE);
        historyContainer.setVisibility(View.GONE);
        //显示搜索结果
        resultContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        searchResultAdapter.addData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        if (resultContainer != null) {
            resultContainer.finishLoadmore();
        }
        ToastUtils.showToast("加载了" + result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() + "条数据");
    }

    @Override
    public void onMoreLoaderError() {
        if (resultContainer != null) {
            resultContainer.finishLoadmore();
        }
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoaderEmpty() {
        if (resultContainer != null) {
            resultContainer.finishLoadmore();
        }
        ToastUtils.showToast("没有更多的商品");
    }

    @Override
    public void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWord) {
        List<String> recommendWords = new ArrayList<>();
        for (SearchRecommend.DataBean dataBean : recommendWord) {
            recommendWords.add(dataBean.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            recommendContainer.setVisibility(View.GONE);
        } else {
            recommendContainer.setVisibility(View.VISIBLE);
            recommendView.setTextList(recommendWords);
        }
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }
}
