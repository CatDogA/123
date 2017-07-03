package com.chumi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 *
 * Created by CHUMI.Jim on 2016/11/24.
 */

public class AvatarView extends RelativeLayout {
    private ImageView imgAvatar;
    private View coverLayer;

    public AvatarView(Context context) {
        super(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) { return; }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.avatar_view, this, true);
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        coverLayer = view.findViewById(R.id.avatar_cover_layer);
    }

    public ImageView getImgAvatar(){
        return imgAvatar;
    }

    /**
     * 在drawable添加文件如下，可修改色值和圆角程度为自己喜欢的值：
     <?xml version="1.0" encoding="utf-8"?>
     <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
         <item>
             <shape android:shape="rectangle" >
                 <solid android:color="@android:color/transparent" />
                 <stroke android:color="@android:color/white" android:width="2.1dp"/>
             </shape>
         </item>
         <item >
             <shape android:shape="rectangle" >
                 <solid android:color="@android:color/transparent" />
                 <corners android:radius="5dp" />
                 <stroke android:color="@android:color/white" android:width="2.1dp"/>
             </shape>
         </item>
     </layer-list>
     * @param resId 文件id
     */
    public void setCoverLayerBackground(int resId){
        coverLayer.setBackgroundResource(resId);
    }
}
