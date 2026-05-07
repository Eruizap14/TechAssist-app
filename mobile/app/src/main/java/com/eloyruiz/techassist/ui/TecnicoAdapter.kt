package com.eloyruiz.techassist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eloyruiz.techassist.R

data class TecnicoItem(
    val nombre:    String,
    val nivel:     String,
    val consultas: Int
)

class TecnicoAdapter(private var items: List<TecnicoItem>) :
    RecyclerView.Adapter<TecnicoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre:    TextView = view.findViewById(R.id.tvNombreTecnico)
        val tvNivel:     TextView = view.findViewById(R.id.tvNivelTecnico)
        val tvConsultas: TextView = view.findViewById(R.id.tvConsultasTecnico)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tecnico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text    = item.nombre
        holder.tvNivel.text     = item.nivel
        holder.tvConsultas.text = item.consultas.toString()

        // Alternar fondo para facilitar lectura — según 5.2.6
        holder.itemView.setBackgroundColor(
            if (position % 2 == 0) 0xFFFFFFFF.toInt()
            else                   0xFFF5F5F5.toInt()
        )
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevosItems: List<TecnicoItem>) {
        items = nuevosItems
        notifyDataSetChanged()
    }
}