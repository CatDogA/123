package com.chumi.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 *
 * 组合控件ListView，包含下拉刷新，加载更多
 *
 * Created by CHUMI.Darren on 2016/6/24.
 */
public class RefreshAndMoreList extends RelativeLayout {
	private SwipeRefreshLayout swipeRefresh; // 下拉刷新
	private ListView listView;  // 内容列表
	private View footerView;  // 加载更多
	boolean loadingMore = false; // 正在加载更多
	private OnRefreshAndMoreListener listener;

	public RefreshAndMoreList(Context context) {
		this(context, null);
	}

	public RefreshAndMoreList(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.refresh_more_layout, this, true);
		// 内容列表
		listView = (ListView) this.findViewById(R.id.list_view);
		// 下拉刷新
		swipeRefresh = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
		swipeRefresh.setColorSchemeResources(R.color.refreshSchemeRed, R.color.refreshSchemeOrange, R.color.refreshSchemeBlue);
		swipeRefresh.setRefreshing(false);
		// 加载更多
		footerView = LayoutInflater.from(context).inflate(R.layout.loading_footer, listView, false);
		listView.addFooterView(footerView);
		footerView.setVisibility(View.GONE); // 先隐藏

		setupView();
	}

	/**
	 * 设置下拉刷新和加载更多的回调接口
	 *
	 * @param listener 监听器
	 */
	public void setOnRefreshAndMoreListener(OnRefreshAndMoreListener listener){
		this.listener = listener;
	}

	/**
	 * 设置下拉刷新状态
	 *
	 * @param refreshing 状态
	 */
	public void setRefreshing(boolean refreshing){
		swipeRefresh.setRefreshing(refreshing);
	}

	/**
	 * 获取组合控件中的ListView
	 * @return listView
	 */
	public ListView getListView(){
		return listView;
	}

	/**
	 * 还原加载更多的状态，数据请求完成后需要调用
	 */
	public void revertMoreState(){
		if (footerView != null) {
			footerView.setVisibility(View.GONE);
		}
		loadingMore = false;
	}

	private void setupView() {
		// 下拉刷新
		swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				listener.onRefresh();
			}
		});

		// 加载更多
		listView.setOnScrollListener(new EndlessScrollListener());
	}


	/**
	 * 加载更多滑动监听
	 */
	private class EndlessScrollListener implements AbsListView.OnScrollListener {
//
		int currentFirstVisibleItem = 0; // 屏幕显示的第一行序号
		int currentVisibleItemCount = 0; // 显示的所有行数
		int totalItemCount = 0;  //总共的行数
		int currentScrollState = 0;  // 当前滚动状态

		@Override
		public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
							 int totalItemCount) {
			this.currentFirstVisibleItem = firstVisibleItem;
			this.currentVisibleItemCount = visibleItemCount;
			this.totalItemCount = totalItemCount;

//			Log.d(TAG, "onScroll====currentFirstVisibleItem="+currentFirstVisibleItem
//					+"，currentVisibleItemCount=" + currentVisibleItemCount
//					+"，totalItemCount="+totalItemCount);
		}

		@Override
		public void onScrollStateChanged(AbsListView absListView, int scrollState) {
			this.currentScrollState = scrollState;

			// 停止滑动，不是在顶部，已显示完全部
			if (this.currentScrollState == SCROLL_STATE_IDLE && this.currentVisibleItemCount > 0
					&& this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
				if (!loadingMore) {
					loadingMore = true;
					footerView.setVisibility(View.VISIBLE);

					listener.onLoadMore();
				}
			}
		}
	}

	/**
	 * 回调接口
	 */
	public interface OnRefreshAndMoreListener{

		/**
		 * 下拉刷新回调
		 */
		void onRefresh();

		/**
		 * 加载更多回调
		 */
		void onLoadMore();
	}
}
