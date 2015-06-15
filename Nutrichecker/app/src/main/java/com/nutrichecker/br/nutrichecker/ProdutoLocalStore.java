package com.nutrichecker.br.nutrichecker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by renato on 15/06/2015.
 */
public class ProdutoLocalStore {

    public static  final String SP_NAME = "produtoDetails";
    SharedPreferences produtoLocalDatabase;

    public  ProdutoLocalStore(Context context) {
        produtoLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUsuarioData(Produto produto){

        SharedPreferences.Editor spEditor = produtoLocalDatabase.edit();
//        spEditor.putString("descricao", produto.getDescricao());
//        spEditor.putString("codigoBarra", produto.getCodigoBarra());
//
//        spEditor.commit();

        Gson gson = new Gson();
        String json = gson.toJson(produto);
        spEditor.putString("produto",json);
        spEditor.commit();
    }

    public Produto getLoggedInProduto() {
//        String descricao = produtoLocalDatabase.getString("descricao", "");
//        String codigoBarra = produtoLocalDatabase.getString("codigoBarra","");
//        Produto storedProduto = new Produto(descricao, codigoBarra);
//        return storedProduto;

        Gson gson = new Gson();
        String json = produtoLocalDatabase.getString("produto","");
        Produto storedProduto = gson.fromJson(json,Produto.class);
        return storedProduto;
    }

    public void setProdutoLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = produtoLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public boolean getProdutoLoggedIn(){
        if(produtoLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        } else {
            return false;
        }
    }

    public void clearProdutoData(){
        SharedPreferences.Editor spEditor = produtoLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
