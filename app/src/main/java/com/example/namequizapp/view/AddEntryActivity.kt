package com.example.namequizapp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryModel
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.ActivityAddEntryBinding
import com.example.namequizapp.utils.ImageUtils.convertToString
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Activity that handles adding new entries(image,name) to the database
 */
class AddEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEntryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivNewImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResult.launch(intent)
        }
        binding.btnSubmit.setOnClickListener{
            validateInputBeforeSubmit()
        }

        binding.inputTextName.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inputTextName.hideKeyboard()
                validateInputBeforeSubmit()
                return@OnKeyListener true
            }
            false
        })
    }



    private val viewModel: QuizEntryViewModel by viewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(applicationContext) as AppDatabase).quizEntryDao())
        )
    }

    private var inputName: Editable? = null

    private lateinit var imageUri: Uri

    /**
     * Function to check if image and name are provided before saving to database
     */
    private fun validateInputBeforeSubmit() {
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

    /**
     * Stores image to shared variable and redirects to database activity.
     */
    private fun saveImage() {
            val bitmap =
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, imageUri))
            lifecycle.coroutineScope.launch(Dispatchers.IO){
                viewModel.insertQuizEntry(
                    QuizEntryModel(
                        name = inputName.toString(),
                        image = bitmap.convertToString()
                    )
                )
            }
            Toast.makeText(applicationContext,
                "Successfully added entry!",
                Toast.LENGTH_SHORT
            ).show()


        startActivity(Intent(applicationContext, DatabaseActivity::class.java))

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

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}