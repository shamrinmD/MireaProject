package ru.mirea.shamrin.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkResource extends Fragment {

    TextView contentView;
    WebView webView;
    Button btnDownload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_resource, container, false);

        contentView = view.findViewById(R.id.contentView);
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this::onClickDownload);
        return view;
    }
    public void onClickDownload(View v) {
        contentView.setText("Загрузка...");
        new Thread(new Runnable() {
            public void run() {
                try{
                    String content = getContent("https://www.mirea.ru/");
                    webView.post(new Runnable() {
                        public void run() {
                            webView.loadDataWithBaseURL("https://www.mirea.ru/",content, "text/html", "UTF-8", "https://www.mirea.ru/");
                        }
                    });
                    contentView.post(new Runnable() {
                        public void run() {
                            contentView.setText(content);
                        }
                    });
                }
                catch (IOException ex){
                    contentView.post(new Runnable() {
                        public void run() {
                            contentView.setText("error " + ex.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    //Метод getContent() загружает веб-страницу с помощью класса HttpsURLConnection и возвращает код загруженной страницы в виде строки.
    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}