package com.example.contactlistappproject.ui.deletefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.contactlistappproject.R
import com.example.contactlistappproject.databinding.FragmentDeleteBinding
import com.example.contactlistappproject.viewmodel.ViewModelDB
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeleteFragment @Inject constructor() : DialogFragment() {

    private lateinit var binding: FragmentDeleteBinding
    private val viewModel : ViewModelDB by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDeleteBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finalDeleteTextView.setText("You will delete ALL your contacts")
        binding.apply {
            cancelButton.setOnClickListener { dismiss() }
            confirmButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deleteAllContacts()
                        dismiss()
                }
            }
        }
    }


}