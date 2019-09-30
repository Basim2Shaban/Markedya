package com.markedya.basim.Framnts;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markedya.basim.Adapters.BasketAdapter;
import com.markedya.basim.Adapters.ProductsAdapter;
import com.markedya.basim.Firebase_Side.FirebaseVar;
import com.markedya.basim.Models.ProductsModel;
import com.markedya.basim.Models.SpinnerModel;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Basket extends Fragment {
    public ArrayList<ProductsModel> basketList = new ArrayList<>();
    private ArrayList<Float> totalList = new ArrayList<>();
    private RecyclerView recyclerView ;
    private Spinner spinnerDeliv ;
    private TextView textHowMuchDelivery;
    public TextView textAllProductsPrice , textCounter ;
    private ImageView iconSpiner ;
    private Button btnSendOrder ;
    private EditText edtName , edtMob1 , edtMob2 , edtAdress ,edtNotes ;
    private UseAtAll useAtAll = new UseAtAll();
    private FirebaseVar firebaseVar = new FirebaseVar();
    private ProgressDialog progressDialog ;
    private String name , mobile1 , mobile2 , adress , notes;
    public static float deliveryPrice = 0;
    private static float totalIs = 0;




    public Basket() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);


        View getActView = getActivity().findViewById(R.id.txtCounterBasket);
        if (getActView instanceof TextView) {
            textCounter = (TextView) getActView;

            if (useAtAll.getData(view.getContext()) >= 1) {
                textCounter.setText("" + useAtAll.getData(view.getContext()));
            } else {
                textCounter.setVisibility(View.INVISIBLE);
            }
        }


        collectViewsHere(view);

        setDataToRecycler(view.getContext());

        handleSpinner(view , iconSpiner);

        setTotalPrice();

        setTextBar(view);

        onClickSendOrder(btnSendOrder,view);


        return view ;

    }




    public void setDataToRecycler(Context context){
        basketList.clear();
        basketList.addAll(useAtAll.getDataForList(context));
        basketList.size();
        BasketAdapter basketAdapter = new BasketAdapter(basketList,context , textCounter);
        recyclerView.setAdapter(basketAdapter);
    }

    // to get data for spinner and send it and get selected position
    public void handleSpinner(View view , ImageView iconSpiner){
        final ArrayList<SpinnerModel> arrayListSpin = new ArrayList<>();
        arrayListSpin.add(new SpinnerModel("اختر منطقه",0));
        arrayListSpin.add(new SpinnerModel("مدينة السلام",5));
        arrayListSpin.add(new SpinnerModel("مدينة العبور",15));
        arrayListSpin.add(new SpinnerModel("مدينة الشروق",15));
        arrayListSpin.add(new SpinnerModel("مدينة المستقبل",15));
        arrayListSpin.add(new SpinnerModel("قباء",10));
        arrayListSpin.add(new SpinnerModel("النزهة",7));
        arrayListSpin.add(new SpinnerModel("النزهة(2)",5));

        ArrayList<String> arrayListStrings = new ArrayList<>();
        for (int i = 0 ; i <arrayListSpin.size() ; i ++){
            arrayListStrings.add(arrayListSpin.get(i).getPlace());
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayListStrings);
        spinnerDeliv.setAdapter(arrayAdapter);


        deliveryPrice = arrayListSpin.get(spinnerDeliv.getSelectedItemPosition()).getPlaceDeliveryPrice();

        spinnerDeliv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textHowMuchDelivery.setText(arrayListSpin.get(position).getPlaceDeliveryPrice()+" " + getActivity().getString(R.string.Le));
                deliveryPrice = arrayListSpin.get(position).getPlaceDeliveryPrice() ;
                additionTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        iconSpiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDeliv.performClick();
            }
        });

    }

    public void collectViewsHere(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.rec_basket);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        spinnerDeliv = (Spinner)view.findViewById(R.id.spinner_coosePlace);
        textHowMuchDelivery = (TextView)view.findViewById(R.id.txt_howMuchDelivery);
        textAllProductsPrice = (TextView)view.findViewById(R.id.txt_AllProductsPrice);
        iconSpiner = (ImageView)view.findViewById(R.id.icon_spinner);

        edtName = (EditText)view.findViewById(R.id.editText_BasName);
        edtMob1 = (EditText)view.findViewById(R.id.editText_BasMob1);
        edtMob2 = (EditText)view.findViewById(R.id.editText_BasMob2);
        edtAdress = (EditText)view.findViewById(R.id.editText_BasAdress);
        edtNotes = (EditText)view.findViewById(R.id.editText_BasNotes);
        btnSendOrder = (Button)view.findViewById(R.id.button_BasSendOrd);


    }

    // to addition total count and set it to the list total
    public void setTotalPrice(){
        float value ;
        for (int i =0 ; i< basketList.size() ; i++){
            if (Double.parseDouble(basketList.get(i).getUnit()) >= 1.0) {
                 value = Float.parseFloat(basketList.get(i).getUnit() ) * Float.parseFloat(basketList.get(i).getPrice()) ;
                totalList.add(value);

            }else{
                float correctNum =  Float.parseFloat(basketList.get(i).getUnit()) * 1000 /100 ;
                float priceOfElements = Float.parseFloat(basketList.get(i).getPrice()) / 10 ;
                value = correctNum * priceOfElements ;
                totalList.add(value);
            }
        }

        additionTotal();



    }

    // for get delivery price and addition to list total and get the count and desplay it
    @SuppressLint("SetTextI18n")
    public void additionTotal(){
        float val = 0 ;
        for (int i = 0 ; i<totalList.size() ; i++){
            val = val + totalList.get(i);
        }

        totalIs = deliveryPrice + val ;
        textAllProductsPrice.setText(totalIs+" "+getActivity().getString(R.string.Le));

    }


    // get data from edit to string
    public void getDataFromEdits(){
        name = edtName.getText().toString().trim();
        mobile1 = edtMob1.getText().toString().trim();
        mobile2 = edtMob2.getText().toString().trim();
        adress = edtAdress.getText().toString().trim();
        notes = edtNotes.getText().toString().trim();
    }

    // for call it when click send order
    public void sendOrder(final View view){
        final HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("mobile1",mobile1);
        hashMap.put("mobile2",mobile2);
        hashMap.put("adress",adress);
        hashMap.put("notes",notes);
        hashMap.put("total", String.valueOf(totalIs));
        hashMap.put("read_state", "");

        final HashMap hashList = new HashMap();
        hashList.put("list",basketList);


        Calendar calendar = Calendar.getInstance();
        final long milisec = calendar.getTimeInMillis();

        Random random = new Random();
        final long m = random.nextInt(50000000);


        firebaseVar.mOrders.child(String.valueOf(m + milisec)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){

                    firebaseVar.mOrders.child(String.valueOf(m + milisec)).updateChildren(hashList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                edtName.setText("");
                                edtMob1.setText("");
                                edtMob2.setText("");
                                edtAdress.setText("");
                                edtNotes.setText("");
                                basketList.clear();
                                ProductsAdapter.basket_list.clear();
                                useAtAll.keepData(basketList,view.getContext());
                                totalList.clear();
                                deliveryPrice = 0 ;
                                progressDialog.dismiss();
                                goOut(view);
                                Toast.makeText(getActivity(), getString(R.string.doneOrder), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(view.getContext(), getString(R.string.ErrorSendOrd), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });


        }


    public void onClickSendOrder(Button button , final View view){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (!name.isEmpty() &&! mobile1.isEmpty()  &&! adress.isEmpty()){
                   if (deliveryPrice != 0 ){
                       progressDialog = new ProgressDialog(view.getContext());
                       progressDialog.setTitle(getString(R.string.PleaseWait));
                       progressDialog.setMessage(getString(R.string.ProgressMessOrd));
                       progressDialog.show();
                       if (edtMob1.length() <= 10 ){
                           progressDialog.dismiss();
                           Toast.makeText(getActivity(), getString(R.string.errorNum), Toast.LENGTH_SHORT).show();
                       }else {
                           sendOrder(view);
                       }
                   }else {
                       Toast.makeText(view.getContext(), getString(R.string.deliveryplace), Toast.LENGTH_SHORT).show();
                   }


                }else{
                    Toast.makeText(view.getContext(), getString(R.string.tostEmptyRow), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // here we send user in another fragment after order
    public void goOut(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new HomeFrag();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, myFragment,"home")
        .addToBackStack(null).commit();
    }


    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.Basket));

        }
    }




}
