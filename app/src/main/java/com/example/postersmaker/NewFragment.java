package com.example.postersmaker;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewFragment extends Fragment {

    TextView s1by1,custom,s16by9,s9by16,s3by4,s4by3, fbcover, instaStory,instPost;
    EditText width, height;
    LinearLayout frame;
    ConstraintLayout rootView;
    Button cancel,ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        s1by1 = view.findViewById(R.id.s1by1);
        s16by9 = view.findViewById(R.id.s16by9);
        s9by16 = view.findViewById(R.id.s9by16);
        s3by4 = view.findViewById(R.id.s3by4);
        s4by3 = view.findViewById(R.id.s4by3);
        fbcover = view.findViewById(R.id.fb_cover);
        custom = view.findViewById(R.id.custom);
        instaStory = view.findViewById(R.id.instaStory);
        instPost = view.findViewById(R.id.instaPost);
        frame = view.findViewById(R.id.frameContainer);
        width = view.findViewById(R.id.width);
        height = view.findViewById(R.id.height);
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);
        rootView = view.findViewById(R.id.rootView1);

        ScaleAnimation expandAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expandAnimation.setDuration(600);




        instPost.setOnClickListener(v -> {
            MainActivity.widthsize = 1;
            MainActivity.heightsize = 1;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        instaStory.setOnClickListener(v -> {
            MainActivity.widthsize = 9;
            MainActivity.heightsize = 16;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        fbcover.setOnClickListener(v -> {
            MainActivity.widthsize = 16;
            MainActivity.heightsize = 9;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        s1by1.setOnClickListener(v -> {
            MainActivity.widthsize = 1;
            MainActivity.heightsize = 1;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        s9by16.setOnClickListener(v -> {
            MainActivity.widthsize = 9;
            MainActivity.heightsize = 16;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        s16by9.setOnClickListener(v -> {
            MainActivity.widthsize = 16;
            MainActivity.heightsize = 9;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        s3by4.setOnClickListener(v -> {
            MainActivity.widthsize = 3;
            MainActivity.heightsize = 4;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        s4by3.setOnClickListener(v -> {
            MainActivity.widthsize = 4;
            MainActivity.heightsize = 3;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        custom.setOnClickListener(v -> {
                frame.setVisibility(View.VISIBLE);
                rootView.setAlpha(0.5f);
            frame.startAnimation(expandAnimation);
            ifenable(false);
        });
        ok.setOnClickListener(v -> {
            if(width.getText().toString().isEmpty()){
                width.setError("Enter Width");
            }
            else if(height.getText().toString().isEmpty()){
                height.setError("Enter Height");
            }
            else {
                int w = Integer.parseInt((width.getText().toString()));
                int h = Integer.parseInt((height.getText().toString()));
                float ratio =  ((float) w / h);

                    MainActivity.widthsize = (int) ratio;
                    MainActivity.heightsize =  1;
                rootView.setAlpha(1f);
                frame.setVisibility(View.GONE);
                    ifenable(true);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

            }

        });
        cancel.setOnClickListener(v -> {
            frame.setVisibility(View.GONE);
            ifenable(true);
            width.setError(null);
            height.setError(null);
            rootView.setAlpha(1f);
        });


        return view;
    }
    public void ifenable(Boolean b) {
        s1by1.setEnabled(b);
        s16by9.setEnabled(b);
        s9by16.setEnabled(b);
        s3by4.setEnabled(b);
        s4by3.setEnabled(b);
        custom.setEnabled(b);

    }
}