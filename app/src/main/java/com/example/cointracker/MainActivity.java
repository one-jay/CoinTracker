package com.example.cointracker;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    static CryptoDataPoints[] listCDP;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_portfolio:
                    mTextMessage.setText(R.string.title_portfolio);
                    return true;
                case R.id.navigation_all_crypto:
                    mTextMessage.setText(R.string.title_all_crypto);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getCrypto();
    }

    //////////////////////try to modularize this, from getCrypto dow to saveListCDP/////////////


    void getCrypto(){
        CryptoTask task = new CryptoTask("USD");
        task.start();
    }

    private class CryptoTask extends Thread {
        String _currency;
        CryptoTask(String currency) {
            _currency = currency;
        }

        @Override
        public void run() {
            CryptoDataPoints cryptoDataPoints = new CryptoDataPoints();
            String cryptoResponse = null;
            try {
                cryptoResponse = cryptoDataPoints.getCryptoData("USD" );
            } catch (IOException e) {
                e.printStackTrace();
            }

            final CryptoDataPoints[] listCDP = new Gson().fromJson(cryptoResponse, CryptoDataPoints[].class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    saveListCDP(listCDP);
                }
            });
        }
    }

    void saveListCDP(CryptoDataPoints[] listCDP){
        this.listCDP = listCDP;
    }

    public void DisplayCrypto(View view){
        Toast.makeText(this, listCDP[0].id + listCDP[0].current_price
                + listCDP[1].id + listCDP[1].current_price, Toast.LENGTH_LONG).show();
    }


}

