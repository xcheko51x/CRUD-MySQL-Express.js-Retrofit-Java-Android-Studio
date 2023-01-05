package com.xcheko51x.crud_retrofit_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.xcheko51x.crud_retrofit_java.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlinx.coroutines.CoroutineScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements UsuarioAdapter.OnItemClicked {

    ActivityMainBinding binding;

    UsuarioAdapter adaptador;

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    Usuario usuario = new Usuario(-1, "", "");

    Boolean isEditando = false;

    Retrofit retrofit = new RetrofitClient().getRetrofit();
    WebService webService = retrofit.create(WebService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerView();

        obtenerUsuarios();

        binding.btnAddUpdate.setOnClickListener(view -> {
            Boolean isValido = validarCampos();
            if (isValido) {
                if (!isEditando) {
                    agregarUsuario();
                } else {
                    actualizarUsuario();
                }
            } else {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupRecyclerView() {
        adaptador = new UsuarioAdapter(this, listaUsuarios);
        adaptador.setOnClick(this);
        binding.rvUsuarios.setAdapter(adaptador);
    }

    public Boolean validarCampos() {
        return !(binding.etNombre.getText().toString().equals("") || binding.etEmail.getText().toString().equals(""));
    }

    public void obtenerUsuarios() {
        Call<UsuarioResponse> call = webService.obtenerUsuarios();
        call.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                listaUsuarios = response.body().listaUsuarios;
                setupRecyclerView();
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR CONSULTAR TODOS", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void agregarUsuario() {
        this.usuario.setIdUsuario(-1);
        this.usuario.setNombre(binding.etNombre.getText().toString());
        this.usuario.setEmail(binding.etEmail.getText().toString());

        Call<String> call = webService.agregarUsuario(this.usuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                obtenerUsuarios();
                limpiarCampos();
                limpiarObjeto();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR ADD", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void actualizarUsuario() {
        this.usuario.setNombre(binding.etNombre.getText().toString());
        this.usuario.setEmail(binding.etEmail.getText().toString());

        Call<String> call = webService.actualizarUsuario(this.usuario.getIdUsuario(), this.usuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                obtenerUsuarios();
                limpiarCampos();
                limpiarObjeto();

                binding.btnAddUpdate.setText("Agregar Usuario");
                binding.btnAddUpdate.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                isEditando = false;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR UPDATE", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void limpiarCampos() {
        binding.etNombre.setText("");
        binding.etEmail.setText("");
    }

    public void limpiarObjeto() {
        this.usuario.setIdUsuario(-1);
        this.usuario.setNombre("");
        this.usuario.setEmail("");
    }

    @Override
    public void editarUsuario(Usuario usuario) {
        binding.etNombre.setText(usuario.getNombre());
        binding.etEmail.setText(usuario.getEmail());
        binding.btnAddUpdate.setText("Actualizar Usuario");
        binding.btnAddUpdate.setBackgroundTintList(getResources().getColorStateList(R.color.purple_500));
        this.usuario = usuario;
        isEditando = true;
    }

    @Override
    public void borrarUsuario(int idUsuario) {

        Call<String> call = webService.borrarUsuario(idUsuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                obtenerUsuarios();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR DELETE", Toast.LENGTH_LONG).show();
            }
        });
    }
}