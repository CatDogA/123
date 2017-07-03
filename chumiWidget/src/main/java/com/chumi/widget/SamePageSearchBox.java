package com.chumi.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 流量、线索查询时，暂时为右半边模糊查询
 * Created by CHUMI.Jim on 2016/6/22.
 */
public class SamePageSearchBox extends LinearLayout {
    private RelativeLayout rl_search;
    private EditText edt_search;
    private TextView txt_search;
    private ImageView img_clear;

    private SearchClickListener searchClickListener;
//    private TextChanged textChanged;
    private Context context;
    private boolean searchShow;

    public SamePageSearchBox(Context context) {
        this(context,null);
        this.context = context;
    }

    public SamePageSearchBox(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_search_same_page, this, true);
        rl_search = (RelativeLayout) view.findViewById(R.id.common_search_rl);
        edt_search = (EditText) view.findViewById(R.id.common_search_edt);
        txt_search = (TextView) view.findViewById(R.id.common_search_txt);
        txt_search.requestFocus();
        img_clear = (ImageView) view.findViewById(R.id.img_clear);

        rl_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hideKeyboard(context);
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event != null
                        && event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    Log.d("+++++", "====");
                    //do something;
                    String key = edt_search.getText().toString().trim();
                    edt_search.setText(key);
                    if ("".equals(key)) {
                        Toast.makeText(context, context.getString(R.string.widget_search_tip), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    searchClickListener.onSearchClicked(key);
                    return true;
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Editable editable = edt_search.getText();
//                int len = editable.length();
//                if (len > 0){
//                    img_clear.setVisibility(VISIBLE);
//                }else{
//                    img_clear.setVisibility(GONE);
//                }
            }
        });

        img_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearch();
            }
        });
    }

    public interface SearchClickListener {
        void onSearchClicked(String key);
    }

    public void setOnSearchClick(SearchClickListener searchClick){
        this.searchClickListener = searchClick;
    }

//    public interface TextChanged{
//        void afterTextChanged(Editable editable);
//    }

//    public void addTextChanged(TextChanged textChanged){
//        this.textChanged = textChanged;
//    }

    public void showSearch(){
        searchShow = true;
        edt_search.setVisibility(View.VISIBLE);
        txt_search.setVisibility(View.GONE);
        img_clear.setVisibility(VISIBLE);
        edt_search.requestFocus();
        showKeyboard(context);
    }

    public void hideSearch(){
        searchShow = false;
        edt_search.setText("");
        edt_search.setVisibility(View.GONE);
        txt_search.setVisibility(View.VISIBLE);
        img_clear.setVisibility(GONE);
        txt_search.requestFocus();
        hideKeyboard(context);
    }

    public boolean isSearchShow(){
        return searchShow;
    }

    public boolean isContentEmpty() {
        String editable = edt_search.getText().toString().trim();
        int len = editable.length();
        return len == 0;
    }

    public String getSearchContent(){
        return edt_search.getText().toString().trim();
    }

    public void setHint(String hint){
        this.edt_search.setHint(hint);
    }

    public void setTextViewHint(String hint){
        this.txt_search.setHint(hint);
    }


    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     */
    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     *
     * @param context 上下文
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
