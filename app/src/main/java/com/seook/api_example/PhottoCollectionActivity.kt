package com.seook.api_example


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.seook.api_example.model.Photo
import com.seook.api_example.rv.PhotoGridRecyclerViewAdapter

class PhottoCollectionActivity:AppCompatActivity() {

    var photoList = ArrayList<Photo>()
    val TAG : String ="로그"
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_collection)

        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<Photo>

        Log.d(TAG, "PhottoCollectionActivity - onCreate() called ${photoList}")

//        topAppBar.title = searchTerm

        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        this.photoGridRecyclerViewAdapter.submitList(photoList)

        val rv = findViewById<RecyclerView>(R.id.myPhotoRV)
        rv.layoutManager= GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rv.adapter = this.photoGridRecyclerViewAdapter

    }
}