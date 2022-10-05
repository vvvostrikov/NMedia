package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.ActivityPostContentBinding

class PostContentFragment
// (private  val initialContent:String?)
    : Fragment() {
    private val initialContent
        get() = requireArguments().getString(INITIAL_CONTENT_KEY)
    private val initialKeyFragment
        get() = requireArguments().getString(INITIAL_FRAGMENT_KEY)
//    override fun onBackPressed() {
//        setResult(Activity.RESULT_CANCELED, Intent())
//        super.onBackPressed()
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ActivityPostContentBinding.inflate(layoutInflater, container, false).also { binding ->

        //val inputContent = intent?.getStringExtra(Intent.EXTRA_TEXT)
        // binding.edit.setText(intent?.getStringExtra(Intent.EXTRA_TEXT))

        binding.edit.setText(initialContent)
        binding.edit.requestFocus()

        binding.ok.setOnClickListener(onOkButtonClicked(binding))
    }.root

    private fun onOkButtonClicked(binding: ActivityPostContentBinding): (View) -> Unit =
        {
            //val intent = Intent()
            val text = binding.edit.text
            if (!text.isNullOrBlank()) {
                val resultBundle = Bundle(1)
                resultBundle.putString(RESULT_KEY, text.toString())
                setFragmentResult(requestKey = initialKeyFragment ?: REQUEST_KEY, resultBundle)
                //setFragmentResult(requestKey = REQUEST_KEY_SINGLE,resultBundle)
                // setResult(Activity.RESULT_CANCELED, intent)
            }
            findNavController().popBackStack()
        }


    companion object {
        const val RESULT_KEY = "postNewContent"
        const val REQUEST_KEY = "requestKey"
        const val REQUEST_KEY_SINGLE = "singlePost"
        const val INITIAL_CONTENT_KEY = "initialContent"
        const val INITIAL_FRAGMENT_KEY = "initialFragmentKey"

        // fun create(initialContentPost:String?)= PostContentFragment().apply {
        //    this.arguments=createBundle(initialContentPost) }
        fun createBundle(initialContentPost: String?, initialFragmentKey: String) =
            Bundle(2).apply {
                putString(INITIAL_CONTENT_KEY, initialContentPost)
                putString(INITIAL_FRAGMENT_KEY, initialFragmentKey)
            }

    }

}