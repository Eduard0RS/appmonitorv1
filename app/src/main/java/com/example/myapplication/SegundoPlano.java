package com.example.myapplication;

import android.os.AsyncTask;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public class SegundoPlano extends AsyncTask<Void, Void,String> {
    @Override
    protected String doInBackground(Void... voids){

        StringBuilder respostaServer =new StringBuilder();

        try {
            //URL desejada


            URL urlRota=new URL("https://fall-protection.herokuapp.com/monitor");

            //Criando conexao
            HttpURLConnection conexao=(HttpURLConnection) urlRota.openConnection();

            //Tipo de metodo
            conexao.setRequestMethod("GET");

            //Tipo de conteudo q vai receber
            conexao.setRequestProperty("Content-type","application/json");

            conexao.setDoOutput(true);

            conexao.setConnectTimeout(10000);
            conexao.connect();

            Scanner scanner =new Scanner(urlRota.openStream());
            while(scanner.hasNext()){
                respostaServer.append(scanner.next());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respostaServer.toString();
    }
}
