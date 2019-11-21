package halla.icsw.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
TextView number;
TextView money;
NfcAdapter nfcAdapter;
PendingIntent pendingIntent;
TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts=new TextToSpeech(this,this);

        number=findViewById(R.id.number);
        money=findViewById(R.id.money);
        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
       Intent intent=new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
       pendingIntent=PendingIntent.getActivity(this,0,intent,0);

    }

    @Override
    protected void onPause() {
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter!=null){
            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        IsoDep id=IsoDep.get(tag);


        byte[] number_cod={0x00,(byte)0xA4,0x00,0x00,0x02,0x42,0x00};
        byte[] money_cod={(byte) 0x90,0x4C,0x00,0x00,0x04};
        byte[] nummber=null;
        byte[] moneys=null;
        try {
            id.connect();
            nummber=id.transceive(number_cod);
            moneys=id.transceive(money_cod);
            id.close();
        } catch (IOException e) {
           number.setText(e.toString());
        }
        if(tag!=null){
            number.setText("카드번호 : "+toHexString(nummber));
            String say="교통 카드 잔액은 "+toHexMoney(moneys)+"원 입니다.";
            money.setText(toHexMoney(moneys)+"원");
            if(say.length()>0){
                if(tts.isSpeaking())
                    tts.stop();
                tts.setSpeechRate(1.5f);
                tts.speak(say,TextToSpeech.QUEUE_FLUSH,null);
            }

        }
    }
    public static String toHexString(byte[] data){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            if(i>=8&&i<=15){
                sb.append(String.format("%02x", b & 0xff));
                if(i%2==1&&i!=15)
                    sb.append("-");
            }
        }

        return sb.toString();
    }
    public static String toHexMoney(byte[] data){
        StringBuilder sb = new StringBuilder();
        for (int i =0; i <data.length; i++) {
            byte b = data[i];
            if(b<0)
                b+=256;
            if(i<data.length-2)
                sb.append(String.format("%02x", b & 0xff));

        }
        long v = Long.parseLong(sb.toString(), 16);
        return String.valueOf(v);


    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS)
        {
            Locale locale=Locale.getDefault();
            if(tts.isLanguageAvailable(locale)>=TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(locale);
            else
                Toast.makeText(this, "지원하지 않는 언어 오류", Toast.LENGTH_SHORT).show();
        }else if(status==TextToSpeech.ERROR){
            Toast.makeText(this, "음성합성 초기화 오류", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null)tts.shutdown();
    }
}
