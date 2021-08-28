package com.seook.api_example.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.seook.api_example.model.Photo
import com.seook.api_example.utils.API
import com.seook.api_example.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat


class RetrofitManager {
    val TAG : String ="로그"

    companion object{
        val instance = RetrofitManager()
    }

    //http 콜
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    //사진검색 api호출
    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_STATE, ArrayList<Photo>)->Unit){

        //searchTerm이 비어있으면 ""를 반환하고 아니면 그냥 searchTerm을 반환해라
        val term = searchTerm.let{
            it
        }?:""

        val call = iRetrofit?.searchPhotos(searchTerm = term).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            //응답 성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response: ${response.raw()}")

                when(response.code()){
                    200->{
                        response.body()?.let{

                            var parsedPhotoDataArray = ArrayList<Photo>()
                            val body = it.asJsonObject
                            val results = body.getAsJsonArray("results")

                            val total = body.get("total").asInt
                            Log.d(TAG, "RetrofitManager - onResponse() called/ total : $total")
                            //만약 데이터가 없다면 no _content로 보낸다.
                            if(total == 0){
                                completion(RESPONSE_STATE.NO_CONTENT, parsedPhotoDataArray)
                                //데이터가 있다면
                            }else {
                                results.forEach { resultItem ->
                                    val resultItemObject = resultItem.asJsonObject
                                    val user = resultItemObject.get("user").asJsonObject
                                    val username = user.get("username").asString
                                    val likesCount = resultItemObject.get("likes").asInt
                                    val thumbnailLink =
                                        resultItemObject.get("urls").asJsonObject.get("thumb").asString
                                    val createdAt = resultItemObject.get("created_at").asString

                                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    val formatter = SimpleDateFormat("yyyy년\nMM월 dd일")
                                    val outputDateString = formatter.format(parser.parse(createdAt))

                                    val photoItem = Photo(
                                        author = username,
                                        likesCount = likesCount,
                                        thumbnail = thumbnailLink,
                                        createdAt = outputDateString
                                    )
                                    parsedPhotoDataArray.add(photoItem)

                                }
                                //completion으로 응답값만 받음.
                                completion(RESPONSE_STATE.OKAY, parsedPhotoDataArray)
                            }
                        }


                    }
                }


            }

            //응답 실패시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null!!)
            }

        })

    }
}