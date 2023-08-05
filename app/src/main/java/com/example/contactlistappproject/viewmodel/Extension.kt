package com.example.contactlistappproject.viewmodel

import android.view.View

fun View.isVisible(isShowingProgressBar: Boolean, container: View){
    if (isShowingProgressBar){
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    }else{
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

}