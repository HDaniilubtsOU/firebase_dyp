package com.example.firebase_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.firebase_test.databinding.FragmentHomeBinding
import com.google.android.gms.dynamic.SupportFragmentWrapper


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button3.setOnClickListener{
            openNewFragment()
            Toast.makeText(context, "click zgłoś", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openNewFragment() {
        val newFragment = WysFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}