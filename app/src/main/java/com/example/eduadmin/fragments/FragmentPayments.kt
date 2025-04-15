package com.example.eduadmin.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduadmin.adapters.PaymentsAdapter
import com.example.eduadmin.data.Course
import com.example.eduadmin.databinding.DialogUpdatePaymentBinding
import com.example.eduadmin.databinding.FragmentPaymentsBinding
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.utils.VerticalItemDecoration
import com.example.eduadmin.vm.PaymentsViewModel
import com.example.eduadmin.vmf.PaymentsFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentPayments : Fragment(){
    private lateinit var binding: FragmentPaymentsBinding
    private lateinit var dialogBinding : DialogUpdatePaymentBinding
    private  var quantity :Int = 0
    private lateinit var dialog: Dialog
    private lateinit var course: Course
    private lateinit var oldCourse : Course
    private val viewModel by viewModels<PaymentsViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        PaymentsFactory(firebaseAuth,firestore)
    }
    private lateinit var adapter : PaymentsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        getData()
        observeCourses()
        onPayClicked()
        observeSetCourses()
    }

    private fun observeSetCourses() {
        lifecycleScope.launch {
            viewModel.set.collectLatest {
                when(it){
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        dialogBinding.progressBar4.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        val data = it.data
                        dialogBinding.progressBar4.visibility = View.INVISIBLE
                        dialog.dismiss()
                        refreshAdapter(data!!)
                    }
                    is Resource.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun refreshAdapter(data: Course) {
        val currentList = adapter.differ.currentList.toMutableList()
        Log.d("khan","before ${currentList.size}")
        currentList.remove(oldCourse) // Ensure oldCourse is properly defined
        Log.d("khan","after removing ${currentList.size}")
        if (data.sold != data.paid) {
            Log.d("khan", "number is ${data.sold} - ${data.paid} =  ${data.sold - data.paid}")

            val newCourse = data.copy()
            currentList.add(newCourse)

            // Submit the updated list
            adapter.differ.submitList(currentList.toList())
            Log.d("khan","after adding ${currentList.size}")
        }
        else{
            adapter.differ.submitList(currentList)
        }

        binding.progressBar2.visibility = View.INVISIBLE
    }


    private fun onPayClicked() {
        adapter.onClick = {
            course = it
            oldCourse = it
            showCustomDialog()
        }
    }

    private fun observeCourses() {
        lifecycleScope.launch {
            viewModel.get.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressBar2.visibility = View.INVISIBLE
                    }
                    is Resource.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar2.visibility = View.INVISIBLE
                        val data = it.data
                        if(data!=null){
                            adapter.differ.submitList(data)
                        }
                    }
                    is Resource.Unspecified -> {

                    }

                }
            }
        }
    }

    private fun getData() {
     viewModel.getCourses()
    }
    private fun setupRv() {
        adapter = PaymentsAdapter()
        binding.rvPayment.adapter = adapter
        binding.rvPayment.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.rvPayment.addItemDecoration(VerticalItemDecoration(30))
    }

    private fun showCustomDialog() {
        dialogBinding = DialogUpdatePaymentBinding.inflate(LayoutInflater.from(requireContext()))
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.button2.setOnClickListener {
            quantity = dialogBinding.editTextText.text.toString().toInt()
            dialogBinding.progressBar4.visibility = View.VISIBLE
            if(quantity==0){
                Toast.makeText(requireContext(), "Enter quantity", Toast.LENGTH_SHORT).show()
            }
            else if(quantity>(course.sold-course.paid)){
                Toast.makeText(requireContext(), "quanity exceed the payable quantity", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val newcourse = course.copy(paid = course.paid+quantity)
                viewModel.setCourse(newcourse)
            }
        }
        dialog!!.show()
    }



}