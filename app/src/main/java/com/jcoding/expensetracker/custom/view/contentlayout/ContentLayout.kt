package com.jcoding.expensetracker.custom.view.contentlayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jcoding.expensetracker.databinding.ContentLayoutBinding


class ContentLayout(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {
    interface RetryClickListener {
        fun onRetryButtonClick()
    }

    private var retryListener: RetryClickListener? = null
    private lateinit var binding: ContentLayoutBinding


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1) {
            throw RuntimeException("only one layout child allowed")
        }
        binding = ContentLayoutBinding.inflate(LayoutInflater.from(context), this)
        binding.retryLabelTextView.setOnClickListener { retryListener?.onRetryButtonClick() }
    }

    private fun hideContent() {
        getChildAt(0).visibility = INVISIBLE
    }

    private fun showContent() {
        getChildAt(0).visibility = VISIBLE
    }

    private fun hideErrorTextView() {
        binding.errorMessageTextView.visibility = GONE
    }

    private fun showErrorTextView() {
        binding.errorMessageTextView.visibility = VISIBLE
    }

    private fun hideRetryView() {
        binding.retryLabelTextView.visibility = GONE
    }

    private fun showRetryView() {
        binding.retryLabelTextView.visibility = VISIBLE
    }

    private fun showProgressView() {
        binding.progressBar.visibility = VISIBLE
    }

    private fun hideProgressView() {
        binding.progressBar.visibility = GONE
    }


    private fun startProgress() {
        hideContent()
        hideErrorTextView()
        hideRetryView()
        showProgressView()
    }

    private fun stopProgress() {
        hideContent()
        hideErrorTextView()
        hideRetryView()
        hideProgressView()
    }


    private fun onDataNotAvailable(text: String?) {
        hideContent()
        hideRetryView()
        hideProgressView()
        binding.errorMessageTextView.text = text
        showErrorTextView()
    }

    private fun showError(message: String?) {
        hideContent()
        binding.errorMessageTextView.text = message
        showErrorTextView()
        showRetryView()
        hideProgressView()
    }


    fun setContentStatus(status: ContentStatus) {
        when (status) {
            ContentStatus.DataAvailable -> {
                hideProgressView()
                hideErrorTextView()
                hideRetryView()
                showContent()
            }
            is ContentStatus.Loading -> {
                if (status.status) {
                    startProgress()
                } else {
                    stopProgress()
                }
            }
            is ContentStatus.DataNotAvailable -> {
                onDataNotAvailable(status.text)
            }
            is ContentStatus.Error -> {
                showError(status.text)
            }
        }
    }


    fun setOnRetryClickListener(onRetryClick: () -> Unit) {
        this.retryListener = object : RetryClickListener {
            override fun onRetryButtonClick() {
                onRetryClick.invoke()
            }
        }
    }


}