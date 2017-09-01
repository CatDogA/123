package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.SaveUsedPaths;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ShowQueryPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/7/21.
 */

public class ShowPictureLayout extends RelativeLayout {
    public static String SHOW_QUERY_PHOTO = "show_query_photo";
    public static String SHOW_PHOTO = "show_photo";
    private Context context;
    private ArrayList<PathInfo> paths;
    private String title;
    private MyGridView gv;
    private GridViewAdapter adapter;
    private Boolean isPic;
    private static String TAG = "ShowPicture";
    private Button btn;
    private LoadingDialog dialog;
    private DmsApplication app;
    private File root;
    private ArrayList<Bitmap> bits;
    private ArrayList<ArrayList<PathInfo>> allPaths;
    private int type;
    private Boolean isUpdate;
    public static ArrayList<PathInfo> pathsDelete;
    private ImageView img;
    private int img_path;

    public ShowPictureLayout(Context context, ArrayList<PathInfo> paths, String title, int img_path, Boolean isPic, ArrayList<ArrayList<PathInfo>> allPaths, int type, Boolean isUpdate) {
        super(context);
        this.context = context;
        this.title = title;
        this.paths = paths;
        this.isPic = isPic;
        this.allPaths = allPaths;
        this.isUpdate = isUpdate;
        this.type = type;
        this.img_path = img_path;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.layout_show_picture, this, true);
        initView(v);
        initData();
        setUpView();
    }

    private void setUpView() {
        Drawable homepressed = getResources().getDrawable(img_path);
        homepressed.setBounds(0, 0, homepressed.getMinimumWidth(), homepressed.getMinimumHeight());
        btn.setCompoundDrawables(homepressed, null, null, null);
        gv.setAdapter(adapter);
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isUpdate) {
                    bits.remove(position);
                    adapter.notifyDataSetChanged();
                    for (ArrayList<PathInfo> pathInfos : allPaths) {
                        if (paths.size() > 0) {
                            if (pathsDelete == null) {
                                pathsDelete = new ArrayList<PathInfo>();
                            }
                            Log.i(TAG, "onItemLongClick:paths.get(0).type        " + paths.get(0).type);
                            switch (paths.get(0).type) {
                                case 15:
                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
                                    if (pathInfos.size() > 0) {
                                        if (pathInfos.get(0).type == 15) {
                                            pathInfos.remove(position);        //得到剩余的路径
                                        }
                                    }
                                    break;
                                case 16:
                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
                                    if (pathInfos.size() > 0) {
                                        if (pathInfos.get(0).type == 16) {
                                            pathInfos.remove(position);        //得到剩余的路径
                                        }
                                    }
                                    break;
                                case 18:
                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
                                    if (pathInfos.size() > 0) {
                                        if (pathInfos.get(0).type == 18) {
                                            pathInfos.remove(position);        //得到剩余的路径
                                        }
                                    }
                                    break;
                                case 19:
                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
                                    if (pathInfos.size() > 0) {
                                        if (pathInfos.get(0).type == 19) {
                                            pathInfos.remove(position);        //得到剩余的路径
                                        }
                                    }
                                    break;
                                case 20:
                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
                                    if (pathInfos.size() > 0) {
                                        if (pathInfos.get(0).type == 20) {
                                            pathInfos.remove(position);        //得到剩余的路径
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
                return true;
            }
        });

        if (isPic) {
            //如果有文件，就不要再下载
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (isUpdate) {
//                        if (position == 0) {
//                            Log.i(TAG, "onItemClick: type           " + type);
//                            SolvePicturePopupWindow pop = new SolvePicturePopupWindow(context, type);
//                            pop.showPopup(gv);
//                        } else if (paths.size() > 0)
                        {
                            if (paths.get(position).file_id == 0) {          //新添加的
                                Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                Bundle b = new Bundle();
                                b.putString(ShowPictureLayout.SHOW_PHOTO, paths.get(position).path);
                                intent.putExtras(b);
                                context.startActivity(intent);
                            } else {
                                //有则直接取，无则下载
                                File file = new File(root.getPath() + "/report_pic");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File file2 = new File(file, paths.get(position).name);
                                if (file2.exists()) {
                                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString(ShowPictureLayout.SHOW_PHOTO, file2.getPath());
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                } else {
                                    savePic(position);
                                }
                            }
                        }
                    } else {
                        //查询
                        if (paths.size() > 0) {
                            File file = new File(root.getPath() + "/report_pic");
                            if (!file.exists()) {
                                file.mkdir();
                            }
                            File file2 = new File(file, paths.get(position).name);
                            if (file2.exists()) {
                                Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                Bundle b = new Bundle();
                                b.putString(ShowPictureLayout.SHOW_PHOTO, file2.getPath());
                                intent.putExtras(b);
                                context.startActivity(intent);
                            } else {
                                savePic(position);
                            }
                        }
                    }
                }
            });
        }

        if (!isPic) {
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //修改
                    if (isUpdate) {
//                        if (position == 0) {
//                            Log.i(TAG, "onItemClick: type           " + type);
//                            SolvePicturePopupWindow pop = new SolvePicturePopupWindow(context, type);
//                            pop.showPopup(gv);
//                        }
                        if (paths.size() > 0) {
                            if (position == 0) {
                                //0说明是修改后的,直接进入
                                if (paths.get(0).file_id == 0) {
                                    Log.i(TAG, "onItemClick: " + "修改");
                                    Intent intent = new Intent(context, ShowVideoActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString(ReportAttachSubFragment.VIDEO_PATH, paths.get(0).path);
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                } else {
                                    //加载视频
                                    File file = new File(root.getPath() + "/report_video");
                                    if (!file.exists()) {
                                        file.mkdir();
                                    }
                                    File file2 = new File(file, paths.get(0).name);
                                    if (file2.exists()) {
                                        Log.i(TAG, "onItemClick: " + "已有，直接查看");
                                        Intent intent = new Intent(context, ShowVideoActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                                        intent.putExtras(b);
                                        context.startActivity(intent);
                                    } else {
                                        Log.i(TAG, "onItemClick: " + "保存");
                                        saveVideo();
                                    }
                                }
                            }
                        }
                        //查询
                    } else {
                        //加载视频
                        File file = new File(root.getPath() + "/report_video");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File file2 = new File(file, paths.get(0).name);
                        if (file2.exists()) {
                            Log.i(TAG, "onItemClick: " + "已经有了");
                            Intent intent = new Intent(context, ShowVideoActivity.class);
                            Bundle b = new Bundle();
                            b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                            intent.putExtras(b);
                            context.startActivity(intent);
                        } else {
                            Log.i(TAG, "onItemClick: " + "还没有，要加载");
                            saveVideo();
                        }
                    }
                }
            });
        }
        if(isUpdate){
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SolvePicturePopupWindow pop = new SolvePicturePopupWindow(context, type);
                    pop.showPopup(gv);
                }
            });
        }
    }

    private String saveVideo() {
        dialog.showLoadingDlg();
        final String[] vPath = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(0).name;
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(0).path);
                vPath[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_video");
                Log.i(TAG, "run:从后台加载视频path     " + paths.get(0).path + "   加载之后的路径    " + vPath[0]);

                if (!vPath[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    Intent intent = new Intent(context, ShowVideoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.VIDEO_PATH, vPath[0]);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            }
        }).start();
        return vPath[0];
    }

    private String savePic(final int position) {
        dialog.showLoadingDlg();
        final String[] s = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(position).name;
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(position).path);
                s[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_pic");
                if (!s[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ShowPictureLayout.SHOW_PHOTO, s[0]);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            }
        }).start();
        return s[0];
    }

    private void initData() {
//        paths
        app = DmsApplication.getInstance();
        bits = new ArrayList<>();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.add_pic);    //不用了
//        if (isUpdate) {
//            bits.add(bitmap);
//        }
        for (PathInfo path : paths) {
            //从压缩图片路径中获取bitmap
            Bitmap bit = BitmapFactory.decodeFile(path.cp_path);
            Log.i(TAG, "initData:cp_path     " + path.cp_path);
            bits.add(bit);
        }
        Log.i(TAG, "initData: bits" + bits.size());
        adapter = new GridViewAdapter(context, bits);
    }

    private void initView(View v) {
        gv = (MyGridView) v.findViewById(R.id.gv_show_pic);
        btn = (Button) v.findViewById(R.id.text_title);
        dialog = new LoadingDialog(context, "正在加载");
    }
}
