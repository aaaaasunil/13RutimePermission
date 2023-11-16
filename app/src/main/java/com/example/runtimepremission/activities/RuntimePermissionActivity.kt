package com.example.runtimepremission.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.runtimepremission.Constants.Constants
import com.example.runtimepremission.FilePath
import com.example.runtimepremission.R
import com.example.runtimepremission.callBack.PremissionCallBack

class RuntimePermissionActivity : PermissionBaseActivity(), PremissionCallBack {

    var cameraPhotoUri : Uri?=null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun init() {

        if (checkIsPermissionsGranted())
            setUpUI()
        else {
            requestRunTimePermissions(permissionsCallBack = this)
        }
    }

    private fun setUpUI() {

        Toast.makeText(this, "onPermissionAllow", Toast.LENGTH_SHORT).show()

//        capturePhoto()
        selectMediaAndFile()
    }

    private fun selectMediaAndFile() {

        findViewById<Button>(R.id.btnSelectDocumnet).setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.type = "*/*"
            val mimeTypes = arrayOf("application/pdf", "application/pdf")

            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, FILE_REQUEST_CODE)

        }
        findViewById<Button>(R.id.btnSelectImageVideo).setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val type = "image/* video/*"
            intent.setType(type)
            startActivityForResult(intent, FILE_REQUEST_CODE)
        }

        findViewById<Button>(R.id.btnOpenCamera).setOnClickListener {

            /* Save pic in CacheDir storage */
//           val file = File(
//               this.externalCacheDir!!.absolutePath,
//               "/" + System.currentTimeMillis() + ".png"
//           )
//
//           val photoURI = FileProvider.getUriForFile(
//               this,
//               BuildConfig.APPLICATION_ID+".provider",
//               file
//           )

            /* Save pic in private storage */
//            val photoURI = FileProvider.getUriForFile(
//                this,
//                BuildConfig.APPLICATION_ID + ".provider",
//                getImageTempDirectory(this)!!
////                commonDocumentDirPath("RunTimePremission")!!
//            )
            cameraPhotoUri = FilePath.getPhotoFileUriForCamera(this)
            val picIntent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(picIntent, CAMERA_REQUEST_CODE)

        }
    }

    override fun onPremissionAllow() {

        setUpUI()

    }

    override fun onPremissionDeny(eventKey: String) {

        if (eventKey.equals(Constants.PREMISSION.GIVE_REASON_TO_USER))
            whyPermissionIsRequired(msg = "This is required for access camera")
        else showDialogWhenNeverAskPress()

    }

    override fun onPremissionNeverAsk() {

    }

    override fun onPremissionRequestAgain() {

       // requestPermissions(prem = permissionsArrayFor11, this)

    }

}