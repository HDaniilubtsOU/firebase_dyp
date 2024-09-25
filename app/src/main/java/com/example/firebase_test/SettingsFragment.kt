package com.example.firebase_test


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebase_test.databinding.FragmentSettingZalogBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.UUID


class SettingsFragment : Fragment(){
    private lateinit var binding: FragmentSettingZalogBinding
    private var dbFirebase = Firebase.firestore
    private lateinit var textF: EditText
    private lateinit var sendB: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingZalogBinding.inflate(inflater, container, false)

        textF = binding.textEditText
        sendB = binding.send

//        sendB.setOnClickListener{
//            val textFeedback = textF.text.toString().trim()
//            Log.d("SettingsFragment", "Text feedback: $textFeedback")
//
//            val userTextF = hashMapOf(
//                "text" to textFeedback
//            )
//
//            val textFID = FirebaseAuth.getInstance().currentUser!!.uid
//
//            dbFirebase.collection("textF").document(textFID).set(userTextF)
//                .addOnSuccessListener {
//                    Toast.makeText(requireContext(), "Successfully send message", Toast.LENGTH_SHORT).show()
//                    textF.text?.clear()
//                }
//                .addOnFailureListener{
//                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
//                }
//        }
        sendB.setOnClickListener {
            val textFeedback = textF.text.toString().trim()
            Log.d("SettingsFragment", "Text feedback: $textFeedback")
            val userTextF = hashMapOf(
                "text" to textFeedback
            )

            dbFirebase.collection("textF").document(UUID.randomUUID().toString()).set(userTextF)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Successfully sent message", Toast.LENGTH_SHORT).show()
                    textF.text?.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

}