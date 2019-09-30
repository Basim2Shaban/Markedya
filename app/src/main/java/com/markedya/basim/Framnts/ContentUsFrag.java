package com.markedya.basim.Framnts;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.markedya.basim.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentUsFrag extends Fragment {
private EditText edtName , edtEmail , edtMessage ;
private Button btnSend ;
private String name , message , email ;

    public ContentUsFrag() {
        // Required empty public constructor
    }
// https://markedya.000webhostapp.com/app/send.php
    // done // no

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_us, container, false);

        collectViewsHere(view);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (!name.isEmpty()&&! message.isEmpty() &&! email.isEmpty()){
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("برجاء الانتظار");
                    progressDialog.setMessage("جار ارسال رسالتك ");
                    progressDialog.show();
                    sendMessage(email,name,message,progressDialog);
                }else{
                    Toast.makeText(getActivity(), "من فضلك ادخل البيانات !!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view ;

    }

    public void collectViewsHere(View view){
        edtName = (EditText)view.findViewById(R.id.editTextName);
        edtEmail = (EditText)view.findViewById(R.id.editTextEmail);
        edtMessage = (EditText)view.findViewById(R.id.editTextYMessage);
        btnSend = (Button)view.findViewById(R.id.btnSend);
    }

    public void getDataFromEdits(){
        name = edtName.getText().toString().trim();
        message = edtMessage.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
    }

    // set message to Admin
    public void sendMessage(final String email , final String name , final String message , final ProgressDialog progressDialog){
        String uri = "https://markedya.000webhostapp.com/app/send.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(getActivity(), "تم ارسال الرساله", Toast.LENGTH_SHORT).show();
                edtEmail.setText("");
                edtName.setText("");
                edtMessage.setText("");
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("message",name + "\n" + message);

                return hashMap ;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.content));

        }
    }

}
