package com.example.epubreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {
    private static final String EBOOK_FILE_PATH = "/sdcard/Download/le_petit_prince_fr.epub";
    private static String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WebView wv = (WebView)findViewById(R.id.wv_text);
        EpubReader epubReader = new EpubReader();

        try {
            Book book = epubReader.readEpub(new FileInputStream(EBOOK_FILE_PATH));

            Spine spine = new Spine(book.getTableOfContents());

            StringBuilder sb = new StringBuilder();
            for (SpineReference bookSection : spine.getSpineReferences()) {
                Resource res = bookSection.getResource();
                try {
                    InputStream is = res.getInputStream();

                    BufferedReader r = new BufferedReader(new InputStreamReader(is));

                    String line = null;

                    while ((line = r.readLine()) != null) {

                        sb.append(line);
                    }
                    is.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(LOG_TAG,sb.toString());


            wv.loadDataWithBaseURL( null,sb.toString(), "text/html", "utf-8",null);

        } catch (Exception e) {
            e.printStackTrace();
        }



//=== Read ebook option 2
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

//=== READ ebook option 1
//        TextView txtText = (TextView) findViewById(R.id.txt_text);
//        try {
//            book = epubReader.readEpub(new FileInputStream(EBOOK_FILE_PATH));
//            List<TOCReference> tocReferences = book.getTableOfContents().getTocReferences();
//            for(TOCReference tocReference : tocReferences) {
//                InputStream is = tocReference.getResource().getInputStream();
//                BufferedReader r = new BufferedReader(new InputStreamReader(is));
//
//                String line;
//                TextView txtText = (TextView) findViewById(R.id.txt_text);
//                while ((line = r.readLine()) != null) {
//                    if(line.trim() != "") {
//                        line = Html.fromHtml(line).toString();
//                        txtText.setText(txtText.getText() + line);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
