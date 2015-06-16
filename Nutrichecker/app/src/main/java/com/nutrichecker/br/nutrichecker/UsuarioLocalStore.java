package com.nutrichecker.br.nutrichecker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by renato on 14/06/2015.
 */
public class UsuarioLocalStore {

    public static  final String SP_NAME = "userDetails";
    SharedPreferences usuarioLocalDatabase;

    public  UsuarioLocalStore(Context context) {
        usuarioLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUsuarioData(Usuario usuario){
        SharedPreferences.Editor spEditor = usuarioLocalDatabase.edit();
        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        spEditor.putString("usuario",json);
        spEditor.commit();
    }

    public Usuario getLoggedInUsuario() {
        Gson gson = new Gson();
        String json = usuarioLocalDatabase.getString("usuario","");
        Usuario storedUsuario = gson.fromJson(json,Usuario.class);
        return storedUsuario;
    }

    public void setUsuarioLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = usuarioLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public boolean getUsuarioLoggedIn(){
        if(usuarioLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        } else {
            return false;
        }
    }

    public void clearUsuarioData(){
        SharedPreferences.Editor spEditor = usuarioLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
