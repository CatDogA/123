package com.chumi.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.chumi.widget.SamePageSearchBox.hideKeyboard;

/**
 *
 * Created by CHUMI.Jim on 2016/11/28.
 */

public class AnotherPageSearchBox extends LinearLayout {
//    private RelativeLayout rlSearch;
    private EditText edtSearch;
    private ImageView imgClear;
    private Button btn;

    private AnotherPageSearchBox.SearchClickListener searchClickListener;
    //    private TextChanged textChanged;
    private Context context;

    public AnotherPageSearchBox(Context context) {
        this(context,null);
        this.context = context;
    }

    public AnotherPageSearchBox(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_search_another_page, this, true);
//        rlSearch = (RelativeLayout) view.findViewById(R.id.common_search_rl);
        edtSearch = (EditText) view.findViewById(R.id.common_search_edt);
        imgClear = (ImageView) view.findViewById(R.id.img_clear);
        btn = (Button) view.findViewById(R.id.btn);
        
        setUpView();
    }

    private void setUpView() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hideKeyboard(context);
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event != null
                        && event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    String key = edtSearch.getText().toString().trim();
                    edtSearch.setText(key);
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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isContentEmpty()) {
                    imgClear.setVisibility(GONE);
                    btn.setText(R.string.widget_cancel);
                    searchClickListener.onContentChangedEmpty();
                } else {
                    imgClear.setVisibility(VISIBLE);
                    btn.setText(R.string.widget_search);
                }
            }
        });

        imgClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isContentEmpty()) {
                    searchClickListener.onCancelClicked();
                } else {
                    searchClickListener.onSearchClicked(getSearchContent());
                }
            }
        });
    }

    public interface SearchClickListener{
        void onSearchClicked(String key);
        void onCancelClicked();
        void onContentChangedEmpty();
    }

    public void setOnSearchClick(AnotherPageSearchBox.SearchClickListener searchClick){
        this.searchClickListener = searchClick;
    }

    public boolean isContentEmpty() {
        String editable = edtSearch.getText().toString().trim();
        int len = editable.length();
        return len == 0;
    }

    public String getSearchContent(){
        return edtSearch.getText().toString().trim();
    }

    public void setSearchContent(String key){
        edtSearch.setText(key);
    }

    public void setHint(String hint){
        this.edtSearch.setHint(hint);
    }

    public void setBtnTextColor(int color) {
        btn.setTextColor(color);
    }

}
