package com.example.contactlistappproject.ui.addfragment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.contactlistappproject.databinding.FragmentAddContactBinding
import com.example.contactlistappproject.db.ContactEntity
import com.example.contactlistappproject.ui.MainActivity
import com.example.contactlistappproject.utils.Constants.BUNDLE_ID
import com.example.contactlistappproject.utils.Constants.EDIT
import com.example.contactlistappproject.utils.Constants.NEW
import com.example.contactlistappproject.utils.SnackbarListener
import com.example.contactlistappproject.viewmodel.ViewModelDB
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddContactFragment @Inject constructor() : DialogFragment() {

    @Inject
    lateinit var contact: ContactEntity

    private val viewModel: ViewModelDB by viewModels()

    private var contactId = 0
    private var name =""
    private var number =""

    private var type =""
    private var isEdit= false


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Desvincula el ItemTouchHelper del RecyclerView al cerrar el DialogFragment
        (requireActivity() as MainActivity).detachItemTouchHelper()
        (requireActivity() as MainActivity).attachItemTouchHelper()
    }
    var snackbarListener: SnackbarListener? = null

    private lateinit var binding: FragmentAddContactBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddContactBinding.inflate(inflater,container,false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactId = arguments?.getInt(BUNDLE_ID) ?: 0

        if (contactId>0){
            type = EDIT
            isEdit = true
        }else{
            type = NEW
            isEdit = false
        }

        binding.apply {
            closeImage.setOnClickListener {

                dismiss()
            }

            if (type == EDIT){
                viewModel.getContactToEdit(contactId)
                viewModel.contactDetails.observe(viewLifecycleOwner){ itData ->
                    itData.data?.let {
                        editTextName.setText(it.name)
                        editTextNumber.setText(it.number)
                    }
                }
            }

            saveButton.setOnClickListener {
                name = editTextName.text.toString()
                number = editTextNumber.text.toString()
                if(name.isEmpty() || number.isEmpty()){
                    Log.d("matag","mamess")
                    snackbarListener?.showSnackbar("Name and Phone cannot be empty, are you a flutter developer?")
                    dismiss()
                }
                else{
                    contact.id = contactId
                    contact.name = name
                    contact.number = number

                    viewModel.saveContact(isEdit,contact)
                    editTextName.setText("")
                    editTextNumber.setText("")
                    dismiss()
                }

            }
        }
    }

}