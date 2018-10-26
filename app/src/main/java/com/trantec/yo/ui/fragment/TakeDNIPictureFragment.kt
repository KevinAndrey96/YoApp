package com.trantec.yo.ui.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.trantec.yo.R
import com.trantec.yo.constants.AppConstants
import com.trantec.yo.ui.main.TakeDNICaptureActivity
import kotlinx.android.synthetic.main.fragment_take_dnipicture.*
import libs.mjn.prettydialog.PrettyDialog


/**
 * A simple [Fragment] subclass.
 *
 */
class TakeDNIPictureFragment : Fragment() {


    private val takePictureRequestCode = 12


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_dnipicture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageButtonTakeDNICapture.setOnClickListener{
            startActivityForResult(Intent(activity, TakeDNICaptureActivity::class.java), takePictureRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
Toast.makeText(context, "1", Toast.LENGTH_SHORT)
        if(requestCode == takePictureRequestCode){
            Toast.makeText(context, "2", Toast.LENGTH_SHORT)
            if(resultCode == Activity.RESULT_OK){
                Logger.d(data!!.extras.getLong(AppConstants.DNI_OBJECT_NAME))
                //mainActivity = activity as MainActivity

                /*val context = MainActivity.getContext(mainActivity!!)
                context!!.changeFragment(ResultCaptureDNIFragment(), data!!.extras)*/

                val tx = fragmentManager!!.beginTransaction()
                val fragment = ResultCaptureDNIFragment()
                fragment.arguments = data!!.extras
                tx.replace(R.id.main_fragment, fragment)
                tx.commit()

            }else if(resultCode == Activity.RESULT_CANCELED){
                Logger.d(data!!.extras.getString(AppConstants.DNI_CAPTURE_OBJECT_MESSAGE))

                PrettyDialog(activity!!)
                        .setTitle("Información")
                        .setMessage(data!!.extras.getString(AppConstants.DNI_CAPTURE_OBJECT_MESSAGE))
                        .show()
            }
        }
    }


}
