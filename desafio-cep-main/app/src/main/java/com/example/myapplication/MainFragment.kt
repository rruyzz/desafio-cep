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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    lateinit var adress: MutableLiveData<Endereco>
    lateinit var cep: String

    private val viewModel by viewModels<MainView> {
        object : ViewModelProvider.Factory{
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

        input.addTextChangedListener(object : TextWatcher{
            var isUpdating = false
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn.isEnabled = input.length() == 10

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
                    input.setText(str)
                    input.setSelection(input.text?.length ?: 0)
                } else {
                    isUpdating = true
                    input.setText(str)
                    input.setSelection(Math.max(0,
                        Math.min(if (hasMask) start - before else start, str.length)))
                }
            }
        })

        btn.setOnClickListener {
            cep = input.text.toString()
            cep = unMask(cep)
            adress = viewModel.getEndereco(cep)
        }
    }

    private fun navigate() {
        var cep = input.text.toString()
        cep = cep.replace(".", "").replace("-", "")
        var bundle = bundleOf("cep" to cep)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_mainFragment_to_secondFragment, bundle)
    }

    private fun unMask(s: String) : String {
        return s.replace(".","").replace("-","")
    }

}

//    private fun getCep(cep: String){
//
//        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val okHttp = OkHttpClient.Builder().addInterceptor(logger)
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://viacep.com.br/ws/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttp.build())
//            .build().create(Service::class.java)
//
//        GlobalScope.launch (Dispatchers.IO){
//            val response = retrofit.getEndereco(cep).awaitResponse()
//            val adress = response.body()!!
//            withContext(Dispatchers.Main){
//                tv.text = adress.logradouro
//            }
//        }
//    }
//tv.text = adress.value?.logradouro