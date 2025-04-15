package com.example.eduadmin.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduadmin.data.Course
import com.example.eduadmin.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoursesViewModel(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore) : ViewModel(){


    private val _get = MutableStateFlow<Resource<List<Course>>>(Resource.Unspecified())
    val get : StateFlow<Resource<List<Course>>>
        get() = _get.asStateFlow()

    private val _set = MutableStateFlow<Resource<Course>>(Resource.Unspecified())
    val set : StateFlow<Resource<Course>>
        get() = _set.asStateFlow()





    fun getCourses(){
        firestore.collection("courses").get()
            .addOnSuccessListener {
                val courses = it.toObjects(Course::class.java)
                if(courses!=null){
                    viewModelScope.launch {
                        _get.emit(Resource.Success(courses))
                    }
                }
            }
            .addOnFailureListener {
                Log.d("khan","error ${it.message}")
                viewModelScope.launch {
                    _get.emit(Resource.Error(it.message.toString()))
                }
            }
    }



    fun delCourse(course: Course) {
        viewModelScope.launch {
            _set.emit(Resource.Loading())
        }
        firestore.collection("courses").document(course.courseId)
            .delete()
            .addOnSuccessListener {
                viewModelScope.launch {
                    _set.emit(Resource.Success(course))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _set.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}