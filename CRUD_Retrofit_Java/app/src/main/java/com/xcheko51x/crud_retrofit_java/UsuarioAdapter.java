package com.xcheko51x.crud_retrofit_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    Context context;
    ArrayList<Usuario> listaUsuarios;

    public OnItemClicked onClick = null;

    public UsuarioAdapter(Context context, ArrayList<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuarioAdapter.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_usuario, parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        holder.tvIdUsuario.setText(usuario.getIdUsuario()+"");
        holder.tvNombre.setText(usuario.getNombre());
        holder.tvEmail.setText(usuario.getEmail());

        holder.btnEditar.setOnClickListener(view -> {
            onClick.editarUsuario(usuario);
        });

        holder.btnBorrar.setOnClickListener(view -> {
            onClick.borrarUsuario(usuario.getIdUsuario());
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdUsuario;
        TextView tvNombre;
        TextView tvEmail;
        TextView btnEditar;
        TextView btnBorrar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIdUsuario = itemView.findViewById(R.id.tvIdUsuario);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }

    public interface OnItemClicked {
        void editarUsuario(Usuario usuario);
        void borrarUsuario(int idUsuario);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}
