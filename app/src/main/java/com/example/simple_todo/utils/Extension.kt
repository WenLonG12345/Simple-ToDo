package com.example.simple_todo.utils

import android.content.Context
import android.widget.Toast

fun String?.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}