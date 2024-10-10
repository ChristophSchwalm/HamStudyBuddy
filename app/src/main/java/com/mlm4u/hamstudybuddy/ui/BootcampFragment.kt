package com.mlm4u.hamstudybuddy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mlm4u.hamstudybuddy.databinding.FragmentBootcampBinding

class BootcampFragment : Fragment() {

    private lateinit var vb: FragmentBootcampBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentBootcampBinding.inflate(layoutInflater)
        return vb.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    //Hier wird eine WebView gestartet die Videos auf Youtube zeigt
        val webView: WebView = vb.wvYoutube
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        webView.loadUrl("https://www.youtube.com/playlist?list=PLDpWnjHk5ERbcgpLPUaU0iTsD-wrmfENk")
    }
}