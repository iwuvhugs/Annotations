package com.example.annotations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Annotation
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.example.annotations.extensions.AnnotationAction
import com.example.annotations.extensions.openUrl
import com.example.annotations.extensions.setClickListenerToText
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fullText = getText(R.string.need_help_text) as SpannedString
        val spannableString = SpannableString(fullText)

        val annotations = fullText.getSpans(0, fullText.length, Annotation::class.java)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                Snackbar.make(window.decorView.rootView, "URL is clicked", LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint?) {
                ds?.isUnderlineText = false
            }
        }
        annotations?.find { it.value == "help_link" }?.let {
            spannableString.apply {
                setSpan(
                        clickableSpan,
                        fullText.getSpanStart(it),
                        fullText.getSpanEnd(it),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(ForegroundColorSpan(
                        ContextCompat.getColor(this@MainActivity, R.color.colorAccent)),
                        fullText.getSpanStart(it),
                        fullText.getSpanEnd(it),
                        0
                )
            }
        }

        textView.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
        }

        configureMultipleClickTextView()
    }

    companion object {
        const val androidStudioTermsAndConditions = "https://developer.android.com/studio/terms"
        const val googlePrivacyAndPolicy = "https://policies.google.com/privacy?hl=en-US"
    }

    private fun configureMultipleClickTextView() {
        val termsAndConditionsKey = AnnotationAction("terms_and_conditions") {
            openUrl(androidStudioTermsAndConditions)
        }
        val privacyAndPolicyKey = AnnotationAction("privacy_and_policy") {
            openUrl(googlePrivacyAndPolicy)
        }

        multipleClicksTextView.setClickListenerToText(R.string.terms_and_conditions,
            listOf(termsAndConditionsKey, privacyAndPolicyKey))
    }
}
