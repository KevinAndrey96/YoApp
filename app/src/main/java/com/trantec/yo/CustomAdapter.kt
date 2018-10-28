import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.trantec.yo.ui.main.MainActivity

import android.R.attr.name
import com.trantec.yo.R
import com.trantec.yo.Reports

class CustomAdapter internal constructor(internal var main: Reports) : BaseAdapter() {

    override fun getCount(): Int {
        return main.listActive.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    internal class ViewHolderItem {
        var fecha: TextView? = null
        var nombre: TextView? = null
        var valor: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder = ViewHolderItem()
        if (convertView == null) {
            val inflater = main.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.cell, null)

            holder.fecha = convertView!!.findViewById(R.id.fecha) as TextView
            holder.nombre = convertView.findViewById(R.id.nombre) as TextView
            holder.valor = convertView.findViewById(R.id.valor) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolderItem
        }


        holder.nombre!!.setText(this.main.listActive.get(position).nombre)
        holder.fecha!!.setText(this.main.listActive.get(position).fecha)
        holder.valor!!.setText(this.main.listActive.get(position).valor)

        return convertView
    }

}