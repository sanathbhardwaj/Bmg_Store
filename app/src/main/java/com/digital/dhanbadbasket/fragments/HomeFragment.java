package com.digital.dhanbadbasket.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.digital.dhanbadbasket.Adapters.AllViewPageAdapter;
import com.digital.dhanbadbasket.CartActivity;
import com.digital.dhanbadbasket.MainActivity;
import com.digital.dhanbadbasket.Models.BannerSliderModel;
import com.digital.dhanbadbasket.Models.CategoryModel;
import com.digital.dhanbadbasket.Models.AllViewPageModel;
import com.digital.dhanbadbasket.R;
import com.digital.dhanbadbasket.SearchActivity;
import com.digital.dhanbadbasket.SearchLocationActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    DatabaseReference myRef, mCartRef;
    private List<BannerSliderModel> bannerSliderModelList;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ValueEventListener listener, listener1;
    private RelativeLayout shimmer_rl, main_rl, no_internet_rl, search_bar;
    private Button retry_btn;
    private ImageView cart, cart_mark;
    private RelativeLayout location, add_bubble;
    private RecyclerView testing;
    String uid = FirebaseAuth.getInstance().getUid();
    TextView location_tv;
    UpdateManager mUpdateManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bannerSliderModelList = new ArrayList<>();
        testing = view.findViewById(R.id.recycler);
        shimmer_rl = view.findViewById(R.id.shimmer_rl);
        main_rl = view.findViewById(R.id.main_rl);
        no_internet_rl = view.findViewById(R.id.no_internet_rl);
        retry_btn = view.findViewById(R.id.retry_btn);
        cart = view.findViewById(R.id.cart);
        location = view.findViewById(R.id.location);
        cart_mark = view.findViewById(R.id.cart_mark);
        mShimmerViewContainer = view.findViewById(R.id.shimmer);
        search_bar = view.findViewById(R.id.search_bar);
        location_tv = view.findViewById(R.id.location_tv);
        location_tv.setText("Locating...");
        add_bubble = view.findViewById(R.id.add_bubble);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isOnline()){
            if (mShimmerViewContainer.isShimmerVisible()){
                mShimmerViewContainer.stopShimmer();
                shimmer_rl.setVisibility(View.GONE);
            }
            no_internet_rl.setVisibility(View.VISIBLE);
        }
        else {
            no_internet_rl.setVisibility(View.GONE);
        }

        mUpdateManager = UpdateManager.Builder((AppCompatActivity) getActivity()).mode(UpdateManagerConstant.IMMEDIATE);
        // Call start() to check for updates and install them
        mUpdateManager.start();

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });

        retry_btn.setOnClickListener(v -> {
            no_internet_rl.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        location.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchLocationActivity.class)));
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        final List<AllViewPageModel> allViewPageModelList = new ArrayList<>();
        final AllViewPageAdapter allViewPageAdapter = new AllViewPageAdapter(allViewPageModelList);
        testing.setAdapter(allViewPageAdapter);
        testing.setNestedScrollingEnabled(false);

        myRef = FirebaseDatabase.getInstance().getReference("home");
        mCartRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (mShimmerViewContainer.isShimmerVisible()){
                        mShimmerViewContainer.stopShimmer();
                        shimmer_rl.setVisibility(View.GONE);
                        no_internet_rl.setVisibility(View.GONE);
                        main_rl.setVisibility(View.VISIBLE);
                    }
                    long view_type = ds.child("view_type").getValue(long.class);
                    switch (String.valueOf(view_type)){
                        case "0":
                            int count = Integer.parseInt(ds.child("number").getValue().toString());
                            for(int i = 1; i <= count; i++){
                                bannerSliderModelList.add(new BannerSliderModel(ds.child("banner_"+i).getValue().toString()));
                            }
                            allViewPageModelList.add(new AllViewPageModel(0, bannerSliderModelList));
                            break;
                        case "1":
                            allViewPageModelList.add(new AllViewPageModel(1, ds.child("banner").getValue().toString(), ds.child("background").getValue().toString()));
                            break;
                        case "4":
                            List<CategoryModel> categoryModelList = new ArrayList<>();
                            int category_count = Integer.parseInt(ds.child("number_of_categories").getValue().toString());
                            for(int i = 1; i <= category_count; i++){
                                categoryModelList.add(new CategoryModel(ds.child("categories").child(String.valueOf(i)).child("name").getValue().toString()
                                        ,ds.child("categories").child(String.valueOf(i)).child("image").getValue().toString()
                                        ,ds.child("categories").child(String.valueOf(i)).child("offer").getValue().toString()));
                            }
                            allViewPageModelList.add(new AllViewPageModel(4,"title", categoryModelList));
                            break;
                        default:
                            break;
                    }
                    allViewPageAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String sub_locality = null, locality = null;

                if (dataSnapshot.child("address").child("1").child("sub_locality").exists()){
                    sub_locality = dataSnapshot.child("address").child("1").child("sub_locality").getValue().toString();
                }
                if (dataSnapshot.child("address").child("1").child("locality").exists()){
                    locality = dataSnapshot.child("address").child("1").child("locality").getValue().toString();
                }

                if (sub_locality==null && locality==null){
                    location_tv.setText("+ Add Location");
                    add_bubble.setVisibility(View.VISIBLE);
                }
                else if(sub_locality==null){
                    location_tv.setText(locality);
                }
                else {
                    location_tv.setText(sub_locality+", "+locality);
                }

                if (dataSnapshot.child("cart").exists()){
                    cart_mark.setVisibility(View.VISIBLE);
                }
                else {
                    cart_mark.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        myRef.addListenerForSingleValueEvent(listener);
        mCartRef.addValueEventListener(listener1);
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myRef != null && listener != null) {
            myRef.removeEventListener(listener);
        }
        if (mCartRef != null && listener1 != null) {
            mCartRef.removeEventListener(listener1);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }
}