package com.example.namequizapp

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.namequizapp.databinding.ActivityNewEntryBinding

/**
 * Activity for handling adding new entries to memory
 */
class NewEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewEntryBinding

    private var inputName: Editable? = null

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityNewEntryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setUpListeners()
    }

    /**
     *  Initializes listeners in view
     */
    private fun setUpListeners() {
        binding.ivNewImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResult.launch(intent)
        }
        binding.btnSubmit.setOnClickListener {
            inputName = binding.inputTextName.text
            if(inputName != null && this::imageUri.isInitialized){
                saveImage()
            } else {
                Toast.makeText(applicationContext,
                    "Please provide image and name.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Stores image to shared variable and redirects to database activity.
     */
    private fun saveImage() {
        val bitmap =
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, imageUri))
        (application as QuizApp).quizEntries.add(QuizEntry(inputName.toString(), bitmap))
        Toast.makeText(applicationContext,
            "Successfully added entry!",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, DatabaseActivity::class.java))

    }


    /**
     * Listener for successful fetch of image from phone gallery
     */
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                activityResult.data?.data?.let {
                    binding.ivNewImage.setImageURI(it)
                    imageUri = it
                }
                
            }
        }
}