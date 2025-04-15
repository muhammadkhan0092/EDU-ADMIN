package com.example.eduadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduadmin.R
import com.example.eduadmin.adapters.CoursesAdapter
import com.example.eduadmin.adapters.SupportAdapter
import com.example.eduadmin.data.Course
import com.example.eduadmin.databinding.FragmentCoursesBinding
import com.example.eduadmin.databinding.FragmentSupportBinding
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.utils.VerticalItemDecoration
import com.example.eduadmin.vm.CoursesViewModel
import com.example.eduadmin.vm.SupportViewModel
import com.example.eduadmin.vmf.CoursesFactory
import com.example.eduadmin.vmf.SupportFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentSupport : Fragment(){
    private lateinit var binding: FragmentSupportBinding
    private lateinit var adapter: SupportAdapter
    private val viewModel by viewModels<SupportViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        SupportFactory(firebaseAuth,firestore)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCoursesRv()
        getMessages()
        observeChats()
        onClick()
    }

    private fun onClick() {
        adapter.onClick={msg->
            val bundle = Bundle().also {
                it.putParcelable("message",msg)
            }
            findNavController().navigate(R.id.action_fragmentSupport_to_fragmentSupportDetail,bundle)
        }
    }

    private fun observeChats() {
        lifecycleScope.launch {
            viewModel.get.collectLatest {
                when (it) {
                    is Resource.Error -> {
                        binding.progressBar5.visibility = View.INVISIBLE
                    }

                    is Resource.Loading -> {
                        binding.progressBar5.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBar5.visibility = View.INVISIBLE
                        if(it.data!=null){
                            adapter.differ.submitList(it.data)
                        }
                    }

                    is Resource.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun getMessages() {
        viewModel.getChats()
    }

    private fun setupCoursesRv() {
        adapter = SupportAdapter()
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rv.addItemDecoration(VerticalItemDecoration(30))
    }


}