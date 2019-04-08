package br.edu.utfpr.usandoapigoogle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvEndereco;
    private ImageView ivMapa;

    private StringBuilder saida = new StringBuilder();
    private String enderecoFormatado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvEndereco = findViewById(R.id.tvEndereco);
        ivMapa = findViewById(R.id.ivMapa);

    }

    public void btBuscarEnderecoOnClick(View view) {
        new ConexaoGeocodingThread().start();
    }

    class ConexaoGeocodingThread extends Thread{
        public void run(){
            try {
                String latitude = tvLatitude.getText().toString();
                String longitude = tvLongitude.getText().toString();

                String url = "https://maps.googleapis.com/maps/api/geocode/xml?latlng=" + latitude + "," + longitude + "&key=AIzaSyDTIdUDJHEN84hpDw_kw6UV3Qg3IcsUkbo";

                URL caminho = new URL(url);
                URLConnection con = caminho.openConnection();

                InputStream in = con.getInputStream();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(in));

                String linha = entrada.readLine();
                while (linha != null) {
                    saida.append(linha);
                    linha = entrada.readLine();
                }

                enderecoFormatado = saida.toString().substring(
                        saida.toString().indexOf("<formatted_address>") + 19,
                        saida.toString().indexOf("</formatted_address>")
                );

                MainActivity.this.runOnUiThread(new Thread(){
                   public void run(){
                       tvEndereco.setText( enderecoFormatado );
                   }
                });

                tvEndereco.setText(saida.toString());

            } catch (Exception e){

            }


        }
    }
}
