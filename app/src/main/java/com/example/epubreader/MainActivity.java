package com.example.epubreader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

public class MainActivity extends AppCompatActivity {
    private static final String EBOOK_FILE_PATH = "/sdcard/Download/le_petit_prince_fr.epub";
    private static String LOG_TAG = MainActivity.class.getSimpleName();

    //=== Read ebook option 1 (View into a WebView)
    private void viewBook(Book book) {
        InputStream inputStream = null;

        WebView wv = (WebView) findViewById(R.id.wv_text);

        Spine spine = new Spine(book.getTableOfContents());

        StringBuilder sb = new StringBuilder();
        for (SpineReference bookSection : spine.getSpineReferences()) {
            Resource res = bookSection.getResource();
            try {
                inputStream = res.getInputStream();

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        wv.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EpubReader epubReader = new EpubReader();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(EBOOK_FILE_PATH);
            Book book = epubReader.readEpub(fileInputStream);
            viewBook(book);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//=== Read ebook option 2 (View into a TextView)
// TextView txtText = (TextView) findViewById(R.id.txt_text);
//        try {
//            Book book = epubReader.readEpub(new FileInputStream(EBOOK_FILE_PATH));
//
//            Spine spine = new Spine(book.getTableOfContents());
//            for (SpineReference bookSection : spine.getSpineReferences()) {
//                Resource res = bookSection.getResource();
//                try {
//                    InputStream is = res.getInputStream();
//                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
//
//                    String line;
//
//                    while ((line = r.readLine()) != null) {
//                        if (line.trim() != "") {
//                            line = Html.fromHtml(line).toString();
//                            txtText.setText(txtText.getText() + line);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

}
