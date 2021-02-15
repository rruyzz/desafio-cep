package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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
        input.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn.isEnabled = input.length() == 10

                if (isUpdating) {
                    isUpdating = false
                    return
                }
                val hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1
                var str = s.toString().filterNot { it == '.' || it == '-' }
                if (count > before) {
                    if (str.length > 5) {
                        str = "${str.substring(0, 2)}.${str.substring(2, 5)}-${str.substring(5)}"
                    } else if (str.length > 2) {
                        str = "${str.substring(0, 2)}.${str.substring(2)}"
                    }
                    isUpdating = true
                    input.setText(str)
                    input.setSelection(input.text?.length ?: 0)
                } else {
                    isUpdating = true
                    input.setText(str)
                    input.setSelection(
                        Math.max(
                            0,
                            Math.min(if (hasMask) start - before else start, str.length)
                        )
                    )
                }
            }
        })

        viewModel.adress.observe(viewLifecycleOwner, Observer {
            navigate(adress.value!!)
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

    private fun unMask(s: String): String {
        return s.replace(".", "").replace("-", "")
    }

}