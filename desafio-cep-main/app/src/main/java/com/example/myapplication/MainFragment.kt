package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import android.widget.Toast.makeText as makeText1

class MainFragment : Fragment() {

    lateinit var adress: MutableLiveData<Endereco>
    lateinit var cep: String
    lateinit var data : Endereco

    private val viewModel by viewModels<MainView> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainView(service) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input.setCepMask()

        viewModel.adress.observe(viewLifecycleOwner, Observer {
            if(adress.value!!.erro == true){
//                Toast.makeText(this, "CEP INVALIDO", Toast.LENGTH_LONG).show()

                tv.text = "erro"
            }else {
                navigate(adress.value!!)
            }

        })

        btn.setOnClickListener {
            cep = input.text.toString()
            cep = unMask(cep)
            adress = viewModel.getEndereco(cep)
            progressBar.visibility = View.VISIBLE
            textInputLayout3.visibility = View.INVISIBLE
            btn.visibility = View.INVISIBLE
            input.visibility = View.INVISIBLE
        }
    }

    private fun navigate(adress: Endereco) {
        val action = MainFragmentDirections.actionMainFragmentToSecondFragment(adress)
        findNavController().navigate(action)
    }

    private fun EditText.setCepMask() {
        val editText = this
        val listener = object : MaskedTextChangedListener("[00000]-[000]", editText) {
            override fun afterTextChanged(edit: Editable?) {
                try {
                    super.afterTextChanged(edit)
                } catch (e: Exception) {
                    editText.addTextChangedListener(this)
                    editText.onFocusChangeListener = this
                }
            }

            override fun onTextChanged(
                text: CharSequence,
                cursorPosition: Int,
                before: Int,
                count: Int
            ) {
                super.onTextChanged(text, cursorPosition, before, count)
                btn.isEnabled = input.length() == 9
            }
        }
        editText.addTextChangedListener(listener)
        editText.onFocusChangeListener = listener
    }

    private fun unMask(s: String): String {
        return s.replace(".", "").replace("-", "")
    }

}
