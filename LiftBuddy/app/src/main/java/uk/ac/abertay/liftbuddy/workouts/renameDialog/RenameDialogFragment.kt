package uk.ac.abertay.liftbuddy.workouts.renameDialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import uk.ac.abertay.liftbuddy.R

class RenameDialogFragment : DialogFragment() {
    private lateinit var et_rename_field: EditText
    private lateinit var listener: DialogListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = it.layoutInflater
            val view = inflater.inflate(R.layout.layout_dialog, null)

            builder.setView(view)
                .setMessage("Rename")
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        val renamed: String = et_rename_field.text.toString()
                        listener.applyRename(renamed)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            et_rename_field = view.findViewById(R.id.et_rename_field)
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface DialogListener {
        fun applyRename(name: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            // Instantiate the NoticeDialogListener to send message to host
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            // Throw exception if listener is not implemented
            throw ClassCastException((context.toString() +
                    " must implement DialogListener"))
        }

    }
}