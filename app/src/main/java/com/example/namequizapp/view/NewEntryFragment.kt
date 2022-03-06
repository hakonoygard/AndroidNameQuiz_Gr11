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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.example.namequizapp.BaseFragment
import com.example.namequizapp.R
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryModel
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.FragmentNewEntryBinding
import com.example.namequizapp.utils.ImageUtils.convertToString
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewEntryFragment : BaseFragment<FragmentNewEntryBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewEntryBinding {
        return FragmentNewEntryBinding.inflate(inflater, container, false)
    }

    private val viewModel: QuizEntryViewModel by activityViewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(requireContext()) as AppDatabase).quizEntryDao())
        )
    }

    private var inputName: Editable? = null

    private lateinit var imageUri: Uri


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


    /**
     * Function to check if image and name are provided before saving to database
     */
    private fun validateInputBeforeSubmit() {
        inputName = binding.inputTextName.text
        if(inputName != null && this::imageUri.isInitialized){
            saveImage()
        } else {
            Toast.makeText(context,
                "Please provide image and name.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Stores image to shared variable and redirects to database activity.
     */
    private fun saveImage() {
        activity?.let {
            val bitmap =
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(it.contentResolver, imageUri))
            lifecycle.coroutineScope.launch(Dispatchers.IO){
                viewModel.insertQuizEntry(
                    QuizEntryModel(
                        name = inputName.toString(),
                        image = bitmap.convertToString()
                    )
                )
            }
            Toast.makeText(context,
                "Successfully added entry!",
                Toast.LENGTH_SHORT
            ).show()
        }

        view?.findNavController()?.navigate(R.id.action_newEntryFragment_to_entryOverviewFragment)

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