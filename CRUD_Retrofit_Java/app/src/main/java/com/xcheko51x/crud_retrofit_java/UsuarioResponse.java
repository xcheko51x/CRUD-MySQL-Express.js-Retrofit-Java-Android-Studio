package com.xcheko51x.crud_retrofit_java;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsuarioResponse {
    @SerializedName("listaUsuarios")
    ArrayList<Usuario> listaUsuarios;
}
