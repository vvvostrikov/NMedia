package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.setText(intent?.getStringExtra(Intent.EXTRA_TEXT))
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?): Intent =
            Intent(context, PostContentActivity::class.java).apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, input)
                type = "text/plain"
            }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else null
        }
    }
}