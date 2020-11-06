package com.moviles.pharmapp.view.utilities
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.moviles.pharmapp.R

class ProgressBar : DialogFragment() {

    var hilo: Handler? = null
    var end = false
    var listener: IProgresBar? = null

    interface IProgresBar {
        fun fallo()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder =
            AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.progress_bar, null)
        builder.setView(v)
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(false)
        this.isCancelable = false
        hilo = Handler()
        hilo!!.postDelayed({
            try {
                if (hilo != null) {
                    listener?.fallo()
                    dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 10000)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        hilo = null
        end = true
        super.onDismiss(dialog)
    }
}