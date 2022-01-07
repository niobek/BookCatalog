package com.montismobile.booklibrary.search

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.databinding.FragmentGetIsbnBinding
import com.montismobile.booklibrary.utils.getBitmapFromUri
import java.io.File
import java.util.concurrent.Executors

private const val REQUEST_PHOTO = 3
private const val TAG = "GetISBNFragment"
class GetISBNFragment : Fragment() {

    private lateinit var binding:FragmentGetIsbnBinding
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val executor = Executors.newSingleThreadExecutor()
    private val detector = TextRecognition.getClient()
    private var isbnNumber:String =""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_isbn, container, false)
        binding.buttonCamera.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
        binding.takeBookInfoButton.setOnClickListener {
            if (binding.bookIsbnEdittext.text.toString().isNotEmpty()) {
                val action =
                    GetISBNFragmentDirections.actionGetISBNFragmentToBookFragment()
                action.isbnNumber = binding.bookIsbnEdittext.text.toString()
                this.findNavController().navigate(action)
            } else {
                Toast.makeText(context, getString(R.string.needISBN), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPhotoFile(view)
        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.book_isbn))
    }
    fun createPhotoFile(view:View)
    {
        photoFile = File(context?.filesDir, "isbnPicture.jpg")
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(view.context, "com.montismobile.booklibrary.fileprovider",
                photoFile)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK-> return
            requestCode == REQUEST_PHOTO ->{
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                val bitmap = getBitmapFromUri(photoUri, requireActivity())
                binding.isbnPhoto.setImageBitmap(bitmap)
                if (bitmap != null) {
                    executor.execute {
                        runISBNDetection(bitmap)
                    }
                }
            }
        }
    }

    fun runISBNDetection(bitmap:Bitmap): Task<Text>
    {
        val image = InputImage.fromBitmap(bitmap,0)
        return detector.process(image)
                .addOnSuccessListener { rawText->
                    isbnNumber = parseDetectedText(rawText.text)
                    binding.bookIsbnEdittext.setText(isbnNumber)

                }
                .addOnFailureListener{
                    exception->
                    processAndShowErrorMessage(exception)
                }

    }

    private fun parseDetectedText(rawText: String?):String {
        var ISBNText:String = ""
        rawText?.let{

            val checkISBN= rawText.contains("ISBN", false)
            if(checkISBN)
            {
                val ISBNIndex = rawText.indexOf("ISBN",0,true)
                if (ISBNIndex != -1)
                {
                    val indexOfNewLine = rawText.indexOf('\n',ISBNIndex)
                    val substring = rawText.substring(ISBNIndex+4, indexOfNewLine)
                    val pattern = Regex("[[0-9]+[-]+[A-Z]+[a-z]+]+")
                    val found = pattern.find(substring, 0)
                    ISBNText = found?.value?:""
                }

            } else {
                Toast.makeText(context, getString(R.string.isbn_error), Toast.LENGTH_SHORT).show()

            }
        }
        return  ISBNText
    }

    fun processAndShowErrorMessage(exception:Exception)
    {
        try {
            val mlKitException = exception as MlKitException
            if(mlKitException.errorCode == MlKitException.UNAVAILABLE)
            {
                Toast.makeText(context, getString(R.string.waitForLoading), Toast.LENGTH_LONG).show()
            }
        } catch(ex:java.lang.Exception)
        {
            Log.e(TAG, "error while text recognation $ex.message")
        }
    }


}