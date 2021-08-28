package com.seook.api_example

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.seook.api_example.databinding.ActivityMainBinding

import com.seook.api_example.retrofit.RetrofitManager
import com.seook.api_example.utils.RESPONSE_STATE
import com.seook.api_example.utils.SEARCH_TYPE

class MainActivity : AppCompatActivity() {

    private var currentSearchType : SEARCH_TYPE = SEARCH_TYPE.PHOTO

    private lateinit var binding : ActivityMainBinding

    val TAG : String ="로그"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        Log.d(TAG, "MainActivity - onCreate() called")

        

        binding.searchTermRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            //switch 문

            when(checkedId){
                R.id.photoSearchRadioBtn ->{
                    binding.searchTermTextLayout.hint = "사진검색"
                    binding.searchTermTextLayout.startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.PHOTO
                }
                R.id.userSearchRadioBtn ->{
                    binding.searchTermTextLayout.hint="사용자 검색"
                    binding.searchTermTextLayout.startIconDrawable = resources.getDrawable(R.drawable.photo, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.USER
                }
            }

        }

        binding.searchTermEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().count() > 0){
                    binding.frameSearchBtn.visibility = View.VISIBLE
                    //스크롤뷰를 올린다.
                    binding.mainScrollview.scrollTo(0,200)
                    binding.searchTermTextLayout.helperText = " "
                }else{
                    binding.frameSearchBtn.visibility = View.INVISIBLE
                }
                if(s.toString().count() ==12){
                    Toast.makeText(this@MainActivity,"검색어는 12자까지만 입력해라",Toast.LENGTH_LONG).show()
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }

        })

        //검색 버튼 클릭시
        binding.searchBtn.setOnClickListener {
            //progressbar가 동작안해요
            this.handleSearchButtonUi()

            val userSearchInput =binding.searchTermEditText.text.toString()
                //검색 api호출
            RetrofitManager.instance.searchPhotos(searchTerm = binding.searchTermEditText.text.toString(),completion = {
                responseState, responseDataArrayList ->
                when(responseState){
                    RESPONSE_STATE.OKAY->{
                        Log.d(TAG, "api호출성공 : ${responseDataArrayList?.size}")

                        val intent = Intent(this,PhottoCollectionActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("photo_array_list", responseDataArrayList)
                        intent.putExtra("array_bundle",bundle)
                        intent.putExtra("search_term",userSearchInput)
                        startActivity(intent)

                    }RESPONSE_STATE.FAIL->{
                        Toast.makeText(this,"api실패",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "api호출실패 : $responseDataArrayList")
                    }
                    RESPONSE_STATE.NO_CONTENT ->{
                        Toast.makeText(this,"검색결과가 없습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
                binding.btnProgress.visibility = View.INVISIBLE
                binding.searchBtn.text = "검색"
                binding.searchTermEditText.setText("")

            })

        }
    }

    private fun handleSearchButtonUi(){
        binding.btnProgress.visibility = View.VISIBLE
        binding.searchBtn.text =""
//        Handler().postDelayed({
//            binding.btnProgress.visibility = View.INVISIBLE
//            binding.searchBtn.text = "검색"
//        }, 1500)
    }
}