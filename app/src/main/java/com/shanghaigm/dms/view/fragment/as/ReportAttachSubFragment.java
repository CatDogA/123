package com.shanghaigm.dms.view.fragment.as;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ShowPhotoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

import java.util.ArrayList;

public class ReportAttachSubFragment extends BaseFragment {
    private static ReportAttachSubFragment reportAttachSubFragment;
    private Button btn_car_sign, btn_trouble, btn_repair, btn_other;
    private ArrayList<Bitmap> bits_car_sign, bits_trouble, bits_repair, bits_other;
    private static String TAG = "ReportAttachSub";
    public static String SHOW_PHOTO = "show_photo";
    private GridView gv_car_sign, gv_trouble, gv_repair, gv_other;
    private GridViewAdapter adapter_car_sign, adapter_trouble, adapter_repair, adapter_other;
    private ArrayList<String> paths1, paths2, paths3, paths4;
    private Button btn_sub;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_attach_sub, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        btn_car_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 1);
                pop.showPopup(btn_car_sign);
            }
        });
        btn_trouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 2);
                pop.showPopup(btn_car_sign);
            }
        });
        btn_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 3);
                pop.showPopup(btn_car_sign);
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 4);
                pop.showPopup(btn_car_sign);
            }
        });
        adapter_car_sign = new GridViewAdapter(getActivity(), bits_car_sign);
        adapter_trouble = new GridViewAdapter(getActivity(), bits_trouble);
        adapter_repair = new GridViewAdapter(getActivity(), bits_repair);
        adapter_other = new GridViewAdapter(getActivity(), bits_other);

        gv_car_sign.setAdapter(adapter_car_sign);
        gv_trouble.setAdapter(adapter_trouble);
        gv_repair.setAdapter(adapter_repair);
        gv_other.setAdapter(adapter_other);

        gv_car_sign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                Bundle b = new Bundle();
                b.putParcelable(ReportAttachSubFragment.SHOW_PHOTO, bits_car_sign.get(position));
                intent.putExtras(b);
                getActivity().startActivity(intent);
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView(View v) {
        btn_sub = (Button) v.findViewById(R.id.btn_sub);

        bits_car_sign = ((ReportAddActivity) getActivity()).getBitmaps();
        bits_trouble = ((ReportAddActivity) getActivity()).getBitmaps2();
        bits_repair = ((ReportAddActivity) getActivity()).getBitmaps3();
        bits_other = ((ReportAddActivity) getActivity()).getBitmaps4();

        paths1 = ((ReportAddActivity) getActivity()).getUris();
        paths2 = ((ReportAddActivity) getActivity()).getUris2();
        paths3 = ((ReportAddActivity) getActivity()).getUris3();
        paths4 = ((ReportAddActivity) getActivity()).getUris4();

        btn_car_sign = (Button) v.findViewById(R.id.btn_car_sign);
        btn_trouble = (Button) v.findViewById(R.id.btn_trouble);
        btn_repair = (Button) v.findViewById(R.id.btn_repair);
        btn_other = (Button) v.findViewById(R.id.btn_other);

        gv_car_sign = (GridView) v.findViewById(R.id.gv_car_sign);
        gv_repair = (GridView) v.findViewById(R.id.gv_repair);
        gv_trouble = (GridView) v.findViewById(R.id.gv_trouble);
        gv_other = (GridView) v.findViewById(R.id.gv_other);
    }

    public static ReportAttachSubFragment getInstance() {
        if (reportAttachSubFragment == null) {
            reportAttachSubFragment = new ReportAttachSubFragment();
        }
        return reportAttachSubFragment;
    }
}
