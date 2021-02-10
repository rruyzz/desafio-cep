package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        edit.addTextChangedListener(object : TextWatcher{
            var isUpdating = false
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating){
                    isUpdating = false
                    return
                }
                val hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1
                var str = s.toString().filterNot{ it =='.' || it == '-'}
                if (count> before){
                    if(str.length > 5 ){
                        str= "${str.substring(0,2)}.${str.substring(2,5)}-${str.substring(5)}"
                    } else if (str.length>2){
                        str= "${str.substring(0,2)}.${str.substring(2)}"
                    }
                    isUpdating = true
                    edit.setText(str)
                    edit.setSelection(edit.text?.length ?: 0)
                } else {
                    isUpdating = true
                    edit.setText(str)
                    edit.setSelection(Math.max(0,
                        Math.min(if (hasMask) start - before else start, str.length)))
                }
            }
        })

        btn.setOnClickListener {
            var cep = edit.text.toString()
            cep = cep.replace(".", "")
            cep = cep.replace("-", "")
            var bundle = bundleOf("cep" to cep)
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_mainFragment_to_secondFragment, bundle)
        }
    }

}