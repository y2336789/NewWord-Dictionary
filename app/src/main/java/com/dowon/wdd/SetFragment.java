package com.dowon.wdd;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetFragment extends Fragment {

    MainActivity activity;

    TextView textView, textView2, textView3;
    String mean, eng_name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SetFragment() {
        // Required empty public constructor
    }

    public static SetFragment newInstance() {
        return new SetFragment();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_word_result, container, false);

        textView = rootView.findViewById(R.id.result_name);
        textView2 = rootView.findViewById(R.id.result_mean);

        String word = "어쩔티비";
        textView.setText(word);


        DocumentReference docRef = db.collection("word").document(word);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("test1", "DocumentSnapshot data : " + document.getData());
                        Log.d("test1", "mean : " + document.getString("mean"));
                    } else {
                        Log.d("test1", "failed");
                    }
                    mean = document.getString("mean");
//                  eng_name = document.getString("eng");
//                  Log.d("test2", mean);
                    textView2.setText(mean);
//                  textView3.setText(eng_name);
                } else {
                    Log.d("test1", "get failed with ", task.getException());
                }
            }
        });
//        Log.d("test2", mean);





        return rootView;
    }
}


//package com.dowon.wdd;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//
//public class SetFragment extends Fragment {
//
//    public SetFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }
//}