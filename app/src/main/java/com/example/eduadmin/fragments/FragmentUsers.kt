package com.example.eduadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduadmin.adapters.UsersAdapter
import com.example.eduadmin.data.User
import com.example.eduadmin.databinding.FragmentUsersBinding
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.utils.VerticalItemDecoration
import com.example.eduadmin.vm.CoursesViewModel
import com.example.eduadmin.vm.UsersViewModel
import com.example.eduadmin.vmf.CoursesFactory
import com.example.eduadmin.vmf.UsersFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentUsers : Fragment(){
    private lateinit var binding: FragmentUsersBinding
    private var users : MutableList<User> = mutableListOf()
    private val viewModel by viewModels<UsersViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        UsersFactory(firebaseAuth,firestore)
    }
    private lateinit var adapter: UsersAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        getUsers()
        observeUsers()


    }

    private fun setupRv() {
        adapter = UsersAdapter()
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rv.addItemDecoration(VerticalItemDecoration(30))
    }

    private fun getUsers() {
        viewModel.getUsers()
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            viewModel.get.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Try again later", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        if(it.data!=null){
                            users = it.data.toMutableList()
                            adapter.differ.submitList(users)
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Error fetching users", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Unspecified -> {

                    }
                }
            }
        }
    }

}