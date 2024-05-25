package com.example.footballapp.model;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.footballapp.R;

public class VideoDialogFragment extends DialogFragment {

    private static final String ARG_URL_VIDEO = "urlVideo";

    public static VideoDialogFragment newInstance(String urlVideo) {
        VideoDialogFragment fragment = new VideoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL_VIDEO, urlVideo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_dialog, container, false);
        VideoView videoView = view.findViewById(R.id.videoView);

        if (getArguments() != null) {
            String urlVideo = getArguments().getString(ARG_URL_VIDEO);
            videoView.setVideoURI(Uri.parse(urlVideo));
            MediaController mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }

        return view;    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
