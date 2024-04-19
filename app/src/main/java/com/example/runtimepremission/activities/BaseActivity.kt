package com.example.runtimepremission.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.runtimepremission.Constants.Constants
import com.example.runtimepremission.FilePath
import com.example.runtimepremission.FilePath.getPhotoFileUriForCamera
import com.example.runtimepremission.FilePath.getRealPathFromURI_API28
import com.example.runtimepremission.FilePath.getRealPathFromURI_API29
import com.example.runtimepremission.R
import com.example.runtimepremission.callBack.PremissionCallBack
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseActivity : AppCompatActivity() {

    private val RC_STORAGE_PREMISSION = 11000
    private val RC_REQUEST_PREMISSION = 12000
    val FILE_REQUEST_CODE = 1000
    val CAMERA_REQUEST_CODE = 1001
    var premissionCallBack: PremissionCallBack? = null

    /**
     * Have a body
     * May be implement in subclass or not
     */
    open fun onlyOpen() {

    }

    /**
     * Have no body
     * Must implement in subclass
     */
    abstract fun abstractFun()

    /**
     * Have a body
     * Not be implement in subclass
     */
    protected  fun protectedFun() {

    }

    private  fun privateFunGetData() {

    }


    /**
     * Have a body
     * implement in subclass if open is add with protected function
     */
    protected open fun protectedOpenFun() {

    }


    /**
     * Have no body
     * implement in subclass if abstract is add with protected function
     */
    protected abstract fun abstractOpenFun()


    private var permissionsArrayTill29 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    private var permissionsArrayTill32 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private var permissionsArrayTill34 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.CAMERA,
    )
    private var premArray = arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    /**
     * Here check premission granted or not
     * @param premArray
     * @return boolean true or false
     * */
    internal fun allPermissionsGranted(premArray: Array<String>): Boolean {

        var isGranted = false

        for (permission in premArray) {
            isGranted =
                ContextCompat.checkSelfPermission(
                    this@BaseActivity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
        }

        return isGranted
    }


    /**
     * Here check premission granted or not
     * @param premArray
     * @return boolean true or false
     * */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun checkIsPermissionsGranted(): Boolean {
        premArray =  permissionArray()

        var isGranted = false


        for (permission in premArray) {
            isGranted =
                ContextCompat.checkSelfPermission(
                    this@BaseActivity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
        }

        return isGranted
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun permissionArray() : Array<String>  {
        var OsVersion = ""
        if(checkIsAndroidTill29()) {
            premArray = permissionsArrayTill29
            OsVersion = "Version Till29"

        } else if(checkIsAndroidTill32()) {
            premArray = permissionsArrayTill32
            OsVersion = "Version Till32"

        } else if(checkIsAndroidTill34()) {
            premArray = permissionsArrayTill34
            OsVersion = "Version Till34"

        }

        Log.d("BaseActivity : ",OsVersion)

        return premArray
    }

    /**
     * Here request dangerous permission to user
     */
    internal fun requestPermissions(prem: Array<String>, permissionsCallBack: PremissionCallBack) {

        this.premissionCallBack = permissionsCallBack

        ActivityCompat.requestPermissions(this, prem, RC_STORAGE_PREMISSION)
    }

    /**
     * Here request dangerous premission to user
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun requestRunTimePermissions(permissionsCallBack: PremissionCallBack) {

        premArray = permissionArray()

        this.premissionCallBack = permissionsCallBack

        ActivityCompat.requestPermissions(this, premArray, RC_REQUEST_PREMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_STORAGE_PREMISSION) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            ) {
                premissionCallBack!!.onPremissionDeny(Constants.PREMISSION.GIVE_REASON_TO_USER)

            } else {

                if (checkIsAndroid11()) {

                    if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
                        premissionCallBack!!.onPremissionAllow()

                } else if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED)

                    premissionCallBack!!.onPremissionAllow()

                else premissionCallBack!!.onPremissionDeny(Constants.PREMISSION.GO_TO_SETTINGS)

            }
        } else if (requestCode == RC_REQUEST_PREMISSION) {

            if(checkIsAndroidTill29()) {
                if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
                     premissionCallBack!!.onPremissionAllow()
                else premissionCallBack!!.onPremissionDeny(Constants.PREMISSION.GO_TO_SETTINGS)
            }
            if(checkIsAndroidTill32()) {
                if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
                    premissionCallBack!!.onPremissionAllow()
                else premissionCallBack!!.onPremissionDeny(Constants.PREMISSION.GO_TO_SETTINGS)
            }
            if(checkIsAndroidTill34()) {
                if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
                    premissionCallBack!!.onPremissionAllow()
                else premissionCallBack!!.onPremissionDeny(Constants.PREMISSION.GO_TO_SETTINGS)
            }

        }
    }

    /**
     * Alert dialog show when user click on never ask again button
     * */
    internal fun showDialogWhenNeverAskPress() {

        val mAlertDialog = AlertDialog.Builder(this@BaseActivity)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle(getString(R.string.app_name)) //set alertdialog title
        mAlertDialog.setMessage("You denied camera permission. To enable it please go in  permission section under app settings") //set alertdialog message
        mAlertDialog.setPositiveButton("OK") { dialog, id ->
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + this.packageName)
                )
            )
            finish()

        }
        mAlertDialog.setCancelable(false)
        mAlertDialog.show()
    }

    /**
     * Alert dialog show when user click on never ask again button
     * */
    internal fun whyPermissionIsRequired(msg: String) {

        val mAlertDialog = AlertDialog.Builder(this@BaseActivity)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle(getString(R.string.app_name)) //set alertdialog title
        mAlertDialog.setMessage(msg) //set alertdialog message
        mAlertDialog.setPositiveButton("OK") { dialog, id ->

            premissionCallBack!!.onPremissionRequestAgain()
        }
        mAlertDialog.setCancelable(false)
        mAlertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_STORAGE_PREMISSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CAMERA
                        ),
                        RC_STORAGE_PREMISSION
                    )
                } else
                    whyPermissionIsRequired(msg = "This is required for access your files")
            }
        } else {
            if (resultCode == Activity.RESULT_OK && requestCode == FILE_REQUEST_CODE && data != null) {

                val selectedFile = data.data
                val filePathFromUri = data.data?.let { FilePath.getPathForSelectedFile(this, it) }
                val file = File(filePathFromUri)
                val absolutePath = file.absolutePath

                Log.d(">>> selectedFile ", selectedFile.toString())
                Log.d(">>> absolutePath ", absolutePath.toString())

            } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

              if (checkIsAndroidEqualAndAbove10())

                Log.d(">>> absolutePath ",((this as MainActivity).cameraPhotoUri).toString())
               // Log.d(">>> absolutePath ",getRealPathFromURI_API29(this,(this as MainActivity).cameraPhotoUri).toString())
              else
                 getRealPathFromURI_API28(this, getPhotoFileUriForCamera(this))?.let { Log.d(">>> absolutePath ", it) }
                // uriToFilename(getPhotoFileUri(this))?.let { Log.d(">>> absolutePath ", it) }

            }
        }
    }

    /**
     * Here check Api level
     */
    internal fun checkIsAndroid11(): Boolean {

        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R
    }

    /**
     * Here we check device till OS 10
     */
    private fun checkIsAndroidTill29(): Boolean {

        return Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q
    }

    /**
     * Here we  check for OS 11 (30), 12(31,32)
     */
    private fun checkIsAndroidTill32(): Boolean {

        return Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.S_V2
    }

    /**
     * Here we check for OS 13 to above
     */
    private fun checkIsAndroidTill34(): Boolean {

        return Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.TIRAMISU
    }

    /**
     * Here check Api Level 29(ANDROID 10)
     */
    private fun checkIsAndroidEqualAndAbove10(): Boolean {

        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    }

    /**
     *  Here creating an image directory Store in cache memeory
     *
     *  @param context
     *  @return
     */
//    internal fun getImageTempDirectory(context: Context): File? {
//        val storageDir: File =
//            File(
//                "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/${
//                    ""
//                }"
//            )
//
//        try {
//            // Make sure the directory exists.
//            storageDir.mkdirs()
//        } catch (e: IOException) {
//            // Error occurred while creating the File
//            // LogHelper.error(tag = TAG, msg = "getImageTempDirectory: mkdirs error- ${e.message}")
//        }
//
//        if (storageDir.isDirectory) {
//            val newFile =
//                File(storageDir.toString() + File.separator + System.currentTimeMillis() + ".png")
//            return newFile
//        } else
//            return null
//    }
    
    internal fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(format, 95, it))
                    throw IOException("Failed to save bitmap.")
            } ?: throw IOException("Failed to open output stream.")

            return uri

        } catch (e: IOException) {

            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(orphanUri, null, null)
            }

            throw e
        }
    }


}