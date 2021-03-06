package ru.mirea.shamrin.mireaproject;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton play, pause, stop;
    MediaPlayer mediaPlayer;
    boolean playing = false;

    public MusicPlayer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicPlayer.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPlayer newInstance(String param1, String param2) {
        MusicPlayer fragment = new MusicPlayer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        play = view.findViewById(R.id.btnPlay);
        pause = view.findViewById(R.id.btnPause);
        stop = view.findViewById(R.id.btnStop);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
        play.setOnClickListener(oclPlay);
        pause.setOnClickListener(oclPause);
        stop.setOnClickListener(oclStop);
        return view;
    }
    // ???????????????????? ???????????? ?????????????????????????????? ????????????????????:
    View.OnClickListener oclPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playing == false) {
                mediaPlayer.start();
                playing = true;
            }
        }
    };
    // ???????????????????? ???????????? ?????????? ?????????????????????? ????????????????????:
    View.OnClickListener oclPause = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playing == true) {
                mediaPlayer.pause();
                playing = false;
            }
        }
    };
    // ???????????????????? ???????????? ?????????????????? ?????????????????????? ????????????????????:
    View.OnClickListener oclStop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playing == true) {
                mediaPlayer.stop();
                playing = false;
            }
        }
    };
}