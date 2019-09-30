package com.markedya.basim.Framnts;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.markedya.basim.Firebase_Side.FirebaseVar;
import com.markedya.basim.Firebase_Side.ProductsData;
import com.markedya.basim.MainActivity;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {
    public static String key_Branch , key_Menu ;
    private BranchFragment branchFragment = new BranchFragment();
    private MainActivity mainActivity = new MainActivity();
    private ProductsData productsData = new ProductsData();
    private FirebaseVar firebaseVar = new FirebaseVar();
    private RecyclerView recyclerView ;
    private Button buttonGoToBask , buttonBasket ;
    private TextView textCounter ;
    private UseAtAll useAtAll = new UseAtAll();

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_products, container, false);

        key_Branch = getArguments().getString("key"); //fetching value by key
        key_Menu = branchFragment.key ;

        collectViewHere(view);


        getCrossToTextCounter(view);
        setTextBar(view);
      //  getCrossToBasket(view);


        if (mainActivity.direc == 0 ){
            productsData.getProducts(firebaseVar.mMainMenuEn.child(key_Menu).child("branch").child(key_Branch).child("products"),getContext(),recyclerView , textCounter);
        }else{
            productsData.getProducts(firebaseVar.mMainMenuAr.child(key_Menu).child("branch").child(key_Branch).child("products"),getContext(),recyclerView , textCounter);

        }


        useAtAll.onBasketClicked(buttonGoToBask,view);


        return view ;
    }

    public void collectViewHere(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerProducts);
         buttonGoToBask= (Button) view.findViewById(R.id.btnGoToBasket);
        GridLayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    // this method to cross to text counter from here and also can pass it to anywhere
    @SuppressLint("SetTextI18n")
    public void getCrossToTextCounter(View mView){
        View getActView = getActivity().findViewById(R.id.txtCounterBasket);
        if (getActView instanceof TextView){
            textCounter = (TextView)getActView ;

            if (useAtAll.getData(mView.getContext()) >= 1){
                textCounter.setText(""+useAtAll.getData(mView.getContext()));
            }else{
                textCounter.setVisibility(View.INVISIBLE);
            }

        }
    }

    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.products));

        }
    }


    // this method to cross to btn basket from here and also can pass it to anywhere
    /*
    public void getCrossToBasket(final View mView){
        View getActView = getActivity().findViewById(R.id.basket_button);
        if (getActView instanceof Button){
            buttonBasket = (Button)getActView ;
            useAtAll.onBasketClicked(buttonBasket , mView);
        }
    }
*/
}
