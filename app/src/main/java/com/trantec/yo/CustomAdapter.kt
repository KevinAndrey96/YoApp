import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.trantec.yo.R
import com.trantec.yo.Reports
import com.trantec.yo.ui.fragment.PayFragment
import com.yopresto.app.yoprestoapp.dto.ListReportActive
import com.trantec.yo.ui.main.MainActivity
import java.security.AccessController.getContext
import android.R.attr.resource




class CustomAdapter internal constructor(internal var main: Reports) : BaseAdapter() {

    //var reportsList:ArrayList<ListReportActive>? = null


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
        var nombre_persona: TextView? = null
    }

   /* private var mcontext: Context? = null
    fun CustomAdapter(context: Context) {
        this.mcontext = context
    }*/

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder = ViewHolderItem()
        if (convertView == null) {

            //val inflater = mcontext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val inflater = main.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //val inflater = getLayoutInflater().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.cell, null)

            holder.fecha = convertView!!.findViewById(R.id.fecha) as TextView
            holder.nombre = convertView.findViewById(R.id.nombre) as TextView
            holder.valor = convertView.findViewById(R.id.valor) as TextView
            holder.nombre_persona = convertView.findViewById(R.id.name) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolderItem
        }


        holder.nombre!!.setText(this.main.listActive.get(position).nombre)
        holder.fecha!!.setText(this.main.listActive.get(position).fecha)
        holder.valor!!.setText(this.main.listActive.get(position).valor)
        holder.nombre_persona!!.setText(this.main.listActive.get(position).nombre_persona)

        return convertView
    }

}