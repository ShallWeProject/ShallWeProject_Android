package com.shall_we.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shall_we.R
import com.shall_we.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private fun initProfile() {

        fun checkRbChecked(): Boolean {
            if (binding.btnMan.isChecked || binding.btnWoman.isChecked || binding.btnNone.isChecked) {
                return true
            }
            return false
        }

        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                changeBtnClickable(binding.edtAge.text.toString().isNotEmpty() && checkRbChecked())
            }
        }
        binding.edtAge.addTextChangedListener(editTextWatcher)

        binding.btnNextProfile.setOnClickListener {
            Toast.makeText(view?.context,"다음 클릭됨", Toast.LENGTH_SHORT).show()
        }
    }

    fun changeBtnClickable(boolean: Boolean){
        if (boolean) {
            binding.btnNextProfile.setBackgroundResource(R.drawable.btn_next)
            binding.btnNextProfile.isClickable = true
        }
        else {
            binding.btnNextProfile.setBackgroundResource(R.drawable.btn_next_black)
            binding.btnNextProfile.isClickable = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfile()
    }
}