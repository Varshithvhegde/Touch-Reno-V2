package com.touchreno.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.touchreno.myapplication.MyAcount;
import com.touchreno.myapplication.R;
import com.touchreno.myapplication.activities.AccessoriesActivity;
import com.touchreno.myapplication.activities.BottomsActivity;
import com.touchreno.myapplication.activities.TopsActivity;
import com.touchreno.myapplication.adapters.NavAccessoriesAdapter;
import com.touchreno.myapplication.adapters.NavBottomsAdapter;
import com.touchreno.myapplication.adapters.NavTopsAdapter;
import com.touchreno.myapplication.models.NavAccessoriesModel;
import com.touchreno.myapplication.models.NavBottomsModel;
import com.touchreno.myapplication.models.NavTopsModel;
import com.touchreno.myapplication.myCartSys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touchreno.myapplication.myOrderSys;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    LinearLayout home,cart,order,profile;
    CardView chairs,beds,access;
    RecyclerView topRec, botRec, accRec;
    FirebaseFirestore db;
    //Tops Items
    List<NavTopsModel> navTopsModelList;
    NavTopsAdapter navTopsAdapter;
    //Bottoms Items
    List<NavBottomsModel> navBottomsModelList;
    NavBottomsAdapter navBottomsAdapter;
    //Accessories
    List<NavAccessoriesModel> navAccessoriesModelList;
    NavAccessoriesAdapter navAccessoriesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db=FirebaseFirestore.getInstance();
        chairs= root.findViewById(R.id.catchair);
        beds=root.findViewById(R.id.catbeds);
        access=root.findViewById(R.id.catacc);
        chairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getContext(), BottomsActivity.class);
               startActivity(intent);
            }
        });
        beds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TopsActivity.class);
                startActivity(intent);

            }
        });
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccessoriesActivity.class);
                startActivity(intent);

            }
        });
        home=root.findViewById(R.id.homenav);
        cart=root.findViewById(R.id.cartnav);
        order=root.findViewById(R.id.ordernav);
        profile=root.findViewById(R.id.profile);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TO-Do
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), myCartSys.class);
                startActivity(i);
//                ((Activity) getActivity()).overridePendingTransition(0, 0);


            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), myOrderSys.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MyAcount.class);
                startActivity(i);
            }
        });
        //Tops Items
        topRec=root.findViewById(R.id.recyclertop);
        topRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        navTopsModelList=new ArrayList<>();
        navTopsAdapter=new NavTopsAdapter(getActivity(),navTopsModelList);
        topRec.setAdapter(navTopsAdapter);
        db.collection("Beds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavTopsModel navTopsModel = document.toObject(NavTopsModel.class);
                                navTopsModelList.add(navTopsModel);
                                navTopsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Bottoms Items
        botRec=root.findViewById(R.id.recyclerbottom);
        botRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        navBottomsModelList=new ArrayList<>();
        navBottomsAdapter=new NavBottomsAdapter(getActivity(),navBottomsModelList);
        botRec.setAdapter(navBottomsAdapter);
        db.collection("Chairs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavBottomsModel navBottomsModel = document.toObject(NavBottomsModel.class);
                                navBottomsModelList.add(navBottomsModel);
                                navBottomsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Accessories
        accRec=root.findViewById(R.id.recycleraccessories);
        accRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        navAccessoriesModelList=new ArrayList<>();
        navAccessoriesAdapter=new NavAccessoriesAdapter(getActivity(),navAccessoriesModelList);
        accRec.setAdapter(navAccessoriesAdapter);
        db.collection("Accessories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavAccessoriesModel navAccessoriesModel = document.toObject(NavAccessoriesModel.class);
                                navAccessoriesModelList.add(navAccessoriesModel);
                                navAccessoriesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return root;
    }
}