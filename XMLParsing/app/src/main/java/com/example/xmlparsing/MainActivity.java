package com.example.xmlparsing;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    String result = "";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        Handler h = new Handler() {
            public void handleMessage(Message msg) {
                tv.setText(result);
            }
        };
        new WorkerThread(h).start();

    }

    class WorkerThread extends Thread {
        Handler h;

        WorkerThread(Handler h) {
            this.h = h;
        }

        public void run() {
            try {
                URL url = new URL("http://biz.heraldcorp.com/common_prog/rssdisp.php?ct=010000000000.xml");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("item");
                result+="전체 기사 수: "+nodeList.getLength()+"\n\n";
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childList = node.getChildNodes();
                    for (int j = 0; j < childList.getLength(); j++) {
                        Node cNode=childList.item(j);
                        if (cNode.getNodeName().equals("title"))
                            result += (i +1) + "번 기사 제목 :" + cNode.getFirstChild().getNodeValue()+ " \n";
                        if (cNode.getNodeName().equals("description"))
                            result += (i+ 1) + "번 기사 내용 :" + cNode.getFirstChild().getNodeValue()+ " \n\n";
                    }
                }
                h.sendMessage(new Message());
            } catch (Exception e) {
                tv.setText("파싱에러" + e.getMessage());
            }
        }
    }
}