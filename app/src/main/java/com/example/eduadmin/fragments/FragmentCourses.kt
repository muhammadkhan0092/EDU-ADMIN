package com.example.eduadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduadmin.adapters.CoursesAdapter
import com.example.eduadmin.data.Course
import com.example.eduadmin.databinding.FragmentCoursesBinding
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.utils.VerticalItemDecoration
import com.example.eduadmin.vm.CoursesViewModel
import com.example.eduadmin.vmf.CoursesFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentCourses : Fragment(){
    private lateinit var binding: FragmentCoursesBinding
    private val viewModel by viewModels<CoursesViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        CoursesFactory(firebaseAuth,firestore)
    }
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var courses : MutableList<Course>
    private lateinit var course: Course
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCoursesRv()
        getData()
        observeGetCourses()
        onDelClicked()
        observeSetCourses()
    }

    private fun onDelClicked() {
        coursesAdapter.onDel = {
            val course = it
            viewModel.delCourse(course)
        }
    }

    private fun setupCoursesRv() {
        coursesAdapter = CoursesAdapter()
        binding.recyclerView.adapter = coursesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.addItemDecoration(VerticalItemDecoration(30))
    }

    private fun observeGetCourses() {
        lifecycleScope.launch {
            viewModel.get.collectLatest {
                  when(it){
                      is Resource.Error -> {
                          binding.progressBar3.visibility = View.INVISIBLE
                      }
                      is Resource.Loading -> {
                          binding.progressBar3.visibility = View.VISIBLE
                      }
                      is Resource.Success -> {
                          binding.progressBar3.visibility = View.INVISIBLE
                          if(it.data!=null){
                              courses = it.data.toMutableList()
                              coursesAdapter.differ.submitList(courses)
                          }
                      }
                      is Resource.Unspecified -> {

                      }
                  }
            }
        }
    }

    private fun observeSetCourses() {
        lifecycleScope.launch {
            viewModel.set.collectLatest {
                    when(it){
                        is Resource.Error -> {
                            binding.progressBar3.visibility = View.INVISIBLE
                        }
                        is Resource.Loading -> {
                            binding.progressBar3.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            if(it.data!=null){
                               course = it.data
                                refreshAdapter(course)
                            }
                        }
                        is Resource.Unspecified -> {

                        }
                    }

            }
        }
    }

    private fun refreshAdapter(course: Course) {
        val currentList = coursesAdapter.differ.currentList.toMutableList()
        currentList.remove(course)
        coursesAdapter.differ.submitList(currentList.toList())
        binding.progressBar3.visibility = View.INVISIBLE
    }

    private fun getData() {
        viewModel.getCourses()
    }

}