package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/4.
 */

public class LoadOrScanPopupWindow extends PopupWindow {
    private static String TAG = "LoadOrScanPopupWindow";
    private Context context;
    private Button btn_scan, btn_load;
    private ArrayList<PathInfo> paths;
    private Boolean isUpdate;
    private int position;
    private File root;
    private LoadingDialog dialog;
    private RelativeLayout rl_back,rl_out;

    public LoadOrScanPopupWindow(Context context, ArrayList<PathInfo> pathInfos, Boolean isUpdate, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_load_or_scan, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        this.isUpdate = isUpdate;
        this.position = position;
        paths = pathInfos;
        initView(v);
        setUpView();
    }

    private void setUpView() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rl_out.setVisibility(View.GONE);
        if (paths.get(0).file_id == 0) {
            btn_load.setVisibility(View.GONE);
        }
        dialog = new LoadingDialog(context, "正在加载", 120000);
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        btn_scan.setOnClickListener(new View.OnClickListener() {    //加载到本地并观看
            @Override
            public void onClick(View v) {
                //修改
                if (isUpdate) {
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
                                String name = paths.get(0).name;
                                if (name.endsWith("jpg")) {
                                    name = name.replace("jpg", "3gp");
                                }
                                File file2 = new File(file, name);
                                if (file2.exists()) {
                                    Log.i(TAG, "onItemClick: " + "已有，直接查看");
                                    Intent intent = new Intent(context, ShowVideoActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                } else {
                                    Log.i(TAG, "onItemClick: " + "保存");
                                    saveVideo(false);
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
                    String name = paths.get(0).name;
                    if (name.endsWith("jpg")) {
                        name = name.replace("jpg", "3gp");
                    }
                    File file2 = new File(file, name);
                    if (file2.exists()) {
                        Log.i(TAG, "onItemClick: " + "已经有了");
                        Intent intent = new Intent(context, ShowVideoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        Log.i(TAG, "onItemClick: " + "还没有，要加载");
                        saveVideo(false);
                    }
                }
            }
        });
        btn_load.setOnClickListener(new View.OnClickListener() {      //加载到相册
            @Override
            public void onClick(View v) {
                if (isUpdate) {
                    if (paths.size() > 0) {

                        //加载视频
                        File file = new File(root.getPath() + "/report_video");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        String name = paths.get(0).name;
                        if (name.endsWith("jpg")) {
                            name = name.replace("jpg", "3gp");
                        }
                        File file2 = new File(file, name);
                        if (file2.exists()) {
                            Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "onItemClick: " + "保存");
                            saveVideo(true);
                        }
                    }
                    //查询
                } else {
                    //加载视频
                    File file = new File(root.getPath() + "/report_video");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String name = paths.get(0).name;
                    if (name.endsWith("jpg")) {
                        name = name.replace("jpg", "3gp");
                    }
                    File file2 = new File(file, name);
                    if (file2.exists()) {
                        Log.i(TAG, "onItemClick: " + "已经有了");
                        Intent intent = new Intent(context, ShowVideoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        Log.i(TAG, "onItemClick: " + "还没有，要加载");
                        saveVideo(true);
                    }
                }
            }
        });
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    private String saveVideo(final Boolean isLoad) {
        dialog.showLoadingDlg();
        final String[] vPath = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(0).name;
                if (name.endsWith("jpg")) {
                    name = name.replace("jpg", "3gp");
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(0).path);
                vPath[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_video");
                Log.i(TAG, "run:从后台加载视频path     " + paths.get(0).path + "   加载之后的路径    " + vPath[0]);

                if (!vPath[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    if (isLoad) {
                        //strDir视频路径
                        Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, ShowVideoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ReportAttachSubFragment.VIDEO_PATH, vPath[0]);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    }
                }
            }
        }).start();
        return vPath[0];
    }

    private void initView(View v) {
        btn_load = (Button) v.findViewById(R.id.btn_load);
        btn_scan = (Button) v.findViewById(R.id.btn_scan);

        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
        rl_out = (RelativeLayout) v.findViewById(R.id.rl_out);
    }

}
