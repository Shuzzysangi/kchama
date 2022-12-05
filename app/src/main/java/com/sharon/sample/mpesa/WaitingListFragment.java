package com.sharon.sample.mpesa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WaitingListFragment extends Fragment {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<User> users;
    FirebaseDatabase database;
    DatabaseReference ref, usersRef, awardedUsersRef, cyclesRef;
    ImageView spinImage;
    Animation spinAnimation;
    ProgressDialog loader;
    AlertDialog.Builder builder;
    String spinDateStr;
    Date dateNow, spinDate;
    boolean canSpin = false;


    // required default constructor
    public WaitingListFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // inflate layout for this fragment
        return  inflater.inflate(R.layout.fragment_waiting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // show progress dialog
        loader = new ProgressDialog(getContext());
        builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        
        loader.setMessage("Getting users in waiting list... Please wait...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

      // set data for the view
        users = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        spinImage = view.findViewById(R.id.spinImage);
        myAdapter = new MyAdapter(getContext(), users);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        usersRef = database.getReference("user");
        awardedUsersRef = database.getReference("users/awarded");
        cyclesRef = database.getReference("/cycles");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);

        // get spin date from database
        cyclesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    spinDateStr = snapshot.getValue().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat();
                    try {
                        spinDate = formatter.parse(spinDateStr);
                        dateNow = new Date();
                        long timeDiff = spinDate.getTime()-dateNow.getTime();
                        long timeDiffInHours = (timeDiff/(1000 * 60 * 60)) % 24;
                        // check if spin date already reached
                        if(timeDiffInHours >0 && timeDiffInHours <= 2){
                            canSpin = true;
                            return;
                        }

                        if(timeDiffInHours < 0){
                            canSpin = false;
                        }
                        // user cannot spin
                        canSpin = false;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // listen for data change in users database reference
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if all users have been awarded
                if(!snapshot.exists()){
                    loader.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "No Members registered in the system.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // clear existing users
                users.clear();
                User user;
                boolean allUsersAwarded = true; // will change once we found a member who is not awarded yet;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        user = ds.getValue(User.class);
                        // check if user had been awarded or not
                        if (!user.isAwarded()) {
                            allUsersAwarded = false;
                            users.add(user);
                        }
                    }

                myAdapter.notifyDataSetChanged();
                if(loader.isShowing()) loader.dismiss();

                if(allUsersAwarded){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), "All members have been awarded.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(loader.isShowing()) loader.dismiss();
            }
        });

        // set spin animation
        spinAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.spin);

        // listen when spinning finishes and select a winner
        spinAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWinner();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        // Actual spinning
        spinImage.setOnClickListener(v -> {
            // check for spin date
            if(!canSpin){
                Toast.makeText(getActivity(), "Spin date not yet. Spin date is on: "+spinDateStr, Toast.LENGTH_LONG).show();
                return;
            }

            if(users.size() == 0){
                Toast.makeText(getActivity().getApplicationContext(), "All members have been awarded.", Toast.LENGTH_SHORT).show();
                return;
            }

            spinImage.startAnimation(spinAnimation);
            /**
             * NOTE: Selecting a winner will be invoked once the spin animation finishes
             * in the onAnimationEnd() method
             */
        });
    }

    public User getRandomUser(){
        Random random = new Random();
        if(users.size() == 0) return null;

        return users.get(random.nextInt(users.size()));
    }

    // get winner
    public void  getWinner(){
        // put winner selection logic here
        User user = getRandomUser();
        // when no users are left
        if(user == null) {
            Toast.makeText(getActivity().getApplicationContext(), "All members have been awarded.", Toast.LENGTH_SHORT).show();
            return;
        };
        // get the position of the user in users ArrayList
        int userPosition = users.indexOf(user);

        users.remove(user);
        myAdapter.notifyItemRemoved(userPosition);
        myAdapter.notifyItemRangeChanged(userPosition, users.size());

        Toast.makeText(getActivity().getApplicationContext(), "The winner was: "+user.getName(), Toast.LENGTH_SHORT).show();

        // update this user as awarded (in the /user ref)
        user.setAwarded(true);
        usersRef.child(user.getId()).setValue(user);

        // add the selected user to awarded list database (in the /users/awarded ref)
        awardedUsersRef.child(user.getId()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), user.getName()+" successfully added to Awarded Members list.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Unable to add "+user.getName()+" to Awarded Members list", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    }

