package com.example.eduadmin.vmf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eduadmin.vm.CoursesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CoursesFactory(val firebaseAuth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoursesViewModel(firebaseAuth,firebaseFirestore) as T
    }
}