package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.activities.PlaceOrderActivity;
import com.example.myapplication.adapters.MyCartAdapter;
import com.example.myapplication.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class myCartSys extends AppCompatActivity {
    LinearLayout home,cart;
    RelativeLayout cart1, cart2;
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    Button buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_sys);
        db=FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recyclercart);
        cart1=findViewById(R.id.cart1);
        cart2=findViewById(R.id.cart2);
        cart2.setVisibility(View.GONE);
        buy=findViewById(R.id.buy);
        home=findViewById(R.id.homenav);
        cart=findViewById(R.id.cartnav);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), myCartSys.class);
//                startActivity(i);
////                ((Activity) getActivity()).overridePendingTransition(0, 0);


            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        overTotalAmount=findViewById(R.id.total);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        cartModelList=new ArrayList<>();
        cartAdapter=new MyCartAdapter(getApplicationContext(),cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String documentId = documentSnapshot.getId();
                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                cartModel.setDocumentId(documentId);
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                cart2.setVisibility(View.VISIBLE);
                                cart1.setVisibility(View.GONE);
                            }
                        }
                    }
                });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).
                        collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).
                                            collection("MyOrder").document(snapshot.getId()).delete();
                                }
                                Intent intent=new Intent(getApplicationContext(), PlaceOrderActivity.class);
                                intent.putExtra("itemList", (Serializable) cartModelList);
                                startActivity(intent);
                            }
                        });
            }
        });
    }
    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill=intent.getIntExtra("totalAmount",0);
            overTotalAmount.setText("₹"+totalBill);
        }
    };
}