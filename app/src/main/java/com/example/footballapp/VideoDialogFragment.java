package com.example.footballapp;

import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class VideoDialogFragment extends DialogFragment {
    private String videoUrl;

    // Constructor để truyền đường dẫn URL của video vào Fragment
    public VideoDialogFragment(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // Override phương thức onCreateView để tạo giao diện cho DialogFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_dialog, container, false);

        // Tìm VideoView trong giao diện và thiết lập đường dẫn video
        VideoView videoView = rootView.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoUrl));

        // Tạo MediaController để điều khiển video
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Bắt đầu phát video
        videoView.start();

        return rootView;
    }

    // Override phương thức onCreateDialog nếu bạn muốn tùy chỉnh hơn cho Dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
