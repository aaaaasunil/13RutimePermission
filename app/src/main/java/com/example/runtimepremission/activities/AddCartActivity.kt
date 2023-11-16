package com.example.runtimepremission.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runtimepremission.FilePath
import com.example.runtimepremission.R
import com.example.runtimepremission.adapter.AddCartAdapter
import com.example.runtimepremission.model.CartItems

class AddCartActivity : AppCompatActivity() {

    var cartItems = arrayListOf<CartItems>()

    var startActivityLauncher: ActivityResultLauncher<Intent>? = null
    var takePicturePreviewLauncher: ActivityResultLauncher<Void>? = null

    var takePictureLauncher: ActivityResultLauncher<Uri>? = null
    var openDocumentLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cart)


        cartItems.add(CartItems(1))
        cartItems.add(CartItems(1))
        cartItems.add(CartItems(1))
        cartItems.add(CartItems(1))
        cartItems.add(CartItems(5))
        cartItems.add(CartItems(6))

        val addCartAdapter = AddCartAdapter(this, cartItems)
        val recyclerview = findViewById<RecyclerView>(R.id.rvView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = addCartAdapter


        onActivityResultMethods()

    }

    private fun onActivityResultMethods() {

        startActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                }
            }

        takePicturePreviewLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {

                Log.d("Intent PicturePreview", it.toString())

            }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            Log.d("Intent Picture", it.toString())

        }

        openDocumentLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {

            Log.d("Intent Document", it.toString())

        }

        findViewById<TextView>(R.id.tvButton).setOnClickListener {

             openDocumentLauncher(openDocumentLauncher = openDocumentLauncher!!)
            //takePictureLauncher(takePictureLauncher = takePictureLauncher!!)
            //takePicturePreviewLauncher(takePicturePreviewLauncher = takePicturePreviewLauncher!!)

        }

    }

    private fun openSomeActivityForResult(startActivityLauncher: ActivityResultLauncher<Intent>) {

        val intent = Intent(this, MainActivity::class.java)
        startActivityLauncher.launch(intent)

    }

    private fun takePictureLauncher(takePictureLauncher: ActivityResultLauncher<Uri>) {

        val cameraPhotoUri = FilePath.getPhotoFileUriForCamera(this)
        takePictureLauncher.launch(cameraPhotoUri)

    }

    private fun openDocumentLauncher(openDocumentLauncher: ActivityResultLauncher<Array<String>>) {

        openDocumentLauncher.launch(
            arrayOf(
                "application/pdf",
                "application/msword",
                "application/ms-doc",
                "application/doc",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            )
        )

    }

//    private fun takePicturePreviewLauncher(takePicturePreviewLauncher:ActivityResultLauncher<Void>) {
//
//        takePicturePreviewLauncher.launch()
//
//    }


}