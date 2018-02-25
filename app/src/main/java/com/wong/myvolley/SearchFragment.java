package com.wong.myvolley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;


public class SearchFragment extends Fragment implements View.OnClickListener,TextView.OnEditorActionListener{


 private ImageButton searchBtn;
 private EditText editText;
 private CoordinatorLayout myLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);

        ToolBarHelper.setMiddleTitle(getActivity(),"归属地查询",toolbar);


        searchBtn = (ImageButton)view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        editText = (EditText)view.findViewById(R.id.editText);
        myLayout = (CoordinatorLayout)view.findViewById(R.id.myLayout);
        editText.setOnEditorActionListener(this);
    }




    private void volley_Get(String phone){
        String url = "http://apis.juhe.cn/mobile/get?phone="+phone+"&key=335adcc4e891ba4e4be6d7534fd54c5d";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                ResultFragment resultFragment = new ResultFragment();
                Bundle bundle = new Bundle();
                bundle.putString("json",s);
                bundle.putString("phone",editText.getText().toString());
                resultFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,resultFragment).addToBackStack(null).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(myLayout, "网络请求失败！", Snackbar.LENGTH_LONG).setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                    }
                }).show();

            }
        });
        request.setTag("phoneTag");
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchBtn:
                //验证合法的手机号码
                Form form = new Form();
                Validate validate = new Validate(editText);
                NotEmptyValidator notEmptyValidator = new NotEmptyValidator(getActivity(),R.string.phone_error);
                validate.addValidator(notEmptyValidator);
                form.addValidates(validate);
                if(form.validate()){
                    volley_Get(editText.getText().toString());
                }
                break;
        }

    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            // 先隐藏键盘
            ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            editText.clearFocus();
            //验证合法的手机号码
            Form form = new Form();
            Validate validate = new Validate(editText);
            NotEmptyValidator notEmptyValidator = new NotEmptyValidator(getActivity(),R.string.phone_error);
            validate.addValidator(notEmptyValidator);
            form.addValidates(validate);
            if(form.validate()){
                volley_Get(editText.getText().toString());
            }

            return true;
        }
        return false;
    }
}
