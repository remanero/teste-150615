package com.nutrichecker.br.nutrichecker;

import android.content.Context;
import android.content.SharedPreferences;

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
        /*spEditor.putString("nome", usuario.nome);
        spEditor.putString("email", usuario.email);
        spEditor.putString("senha", usuario.senha);*/
        spEditor.putString("nome", usuario.getNome());
        spEditor.putString("email", usuario.getEmail());
        spEditor.putString("senha", usuario.getSenha());
        spEditor.commit();
    }

    public Usuario getLoggedInUsuario() {
        String nome = usuarioLocalDatabase.getString("nome", "");
        String email = usuarioLocalDatabase.getString("email","");
        String senha = usuarioLocalDatabase.getString("senha","");

        Usuario storedUsuario = new Usuario(nome,email,senha);

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
