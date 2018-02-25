package com.wong.myvolley;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class ResultFragment extends Fragment implements View.OnClickListener{




    private TextView phone;
    private TextView company;
    private TextView sheng;
    private TextView shi;
    private TextView quhao;
    private TextView zip;
    private ImageButton dialBtn;
    private String phoneNum;
    private CoordinatorLayout result_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        initView(view);
        showData(getArguments());
        return view;
    }

    private void initView(View view){

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_rs);
        ToolBarHelper.setMiddleTitle(getActivity(),"查询结果",toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        phone = (TextView)view.findViewById(R.id.phone);
        company = (TextView)view.findViewById(R.id.company);
        sheng = (TextView)view.findViewById(R.id.sheng);
        shi = (TextView)view.findViewById(R.id.shi);
        quhao = (TextView)view.findViewById(R.id.quhao);
        zip = (TextView)view.findViewById(R.id.zip);
        dialBtn = (ImageButton)view.findViewById(R.id.dialBtn);
        dialBtn.setOnClickListener(this);
        result_layout = (CoordinatorLayout)view.findViewById(R.id.result_layout);
    }

    private void showData(Bundle bundle){

        String json = bundle.getString("json");
        phoneNum = bundle.getString("phone");
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("result");
        if(jsonObject == null){
            String err = phoneNum+"现在不在家";
            phone.setText(err);
            dialBtn.setVisibility(View.INVISIBLE);

            return;
        }

        CellPhone cellPhone = JSON.parseObject(jsonObject.toJSONString(),CellPhone.class);

        String num = "号码："+phoneNum;
        String com = "运营商："+cellPhone.getCompany();
        String province = "省："+cellPhone.getProvince();
        String city = "市："+cellPhone.getCity();
        String areaCode = "区号："+cellPhone.getAreacode();
        String youbian = "邮编："+cellPhone.getZip();
        phone.setText(num);
        company.setText(com);
        sheng.setText(province);
        shi.setText(city);
        quhao.setText(areaCode);
        zip.setText(youbian);

        if(phoneNum.length() == 11){
            dialBtn.setVisibility(View.VISIBLE);
        }else{
            dialBtn.setVisibility(View.INVISIBLE);
        }
    }


    final public static int CALL_PHONE_REQUEST_CODE=10086;

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {



        if(Build.VERSION.SDK_INT >= 23){

            int checkCallPhone = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            if(checkCallPhone != PackageManager.PERMISSION_DENIED){
                dial(phoneNum);
            }else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_REQUEST_CODE);
            }
            return;

        }else{
            dial(phoneNum);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CALL_PHONE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    dial(phoneNum);
                }else{
                    Snackbar.make(result_layout,"请求权限失败",Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                break;
        }

    }

    private void dial(String mobile){
        if(mobile.length() != 11) return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + mobile);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialBtn:
                if(phoneNum.length() == 11){
                    callPhone(phoneNum);
                }
                break;
        }
    }
}
