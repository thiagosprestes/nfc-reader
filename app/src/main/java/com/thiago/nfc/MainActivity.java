package com.thiago.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView nfcAntennaInfo;
    private Button enableNfc;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAntennaInfo = findViewById(R.id.nfcAntennaInfo);
        enableNfc = findViewById(R.id.enableNfc);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        onVerifyNfc();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public void onVerifyNfc() {
        if(nfcAdapter == null) {
            nfcAntennaInfo.setText("This device does not support NFC");
            return;
        }

        if(!nfcAdapter.isEnabled()) {
            nfcAntennaInfo.setText("Please, enable NFC to continue");
            enableNfc.setVisibility(View.VISIBLE);
            return;
        }

        nfcAntennaInfo.setText("This device supports NFC");
        enableNfc.setVisibility(View.INVISIBLE);
    }

    public void handleEnableNfc(View v) {
        Intent nfcSettings = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(nfcSettings);
    }

    public void onRead() {
        Bundle options = new Bundle();
        Integer flags = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 5000);

        NfcAdapter.ReaderCallback callback = new NfcAdapter.ReaderCallback() {
            public void onTagDiscovered(Tag tag) {
                System.out.println(tag);
            }
        };

        nfcAdapter.enableReaderMode(this, callback, flags, options);
    }

    @Override
    public void onResume() {
        super.onResume();
        onVerifyNfc();
        onRead();
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableReaderMode(this);
    }
}