package com.example.eduadmin.vmf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eduadmin.vm.CoursesViewModel
import com.example.eduadmin.vm.SupportDetailViewModel
import com.example.eduadmin.vm.SupportViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SupportDetailFactory(val firebaseAuth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SupportDetailViewModel(firebaseAuth,firebaseFirestore) as T
    }
}