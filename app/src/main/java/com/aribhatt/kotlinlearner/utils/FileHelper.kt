package com.aribhatt.kotlinlearner.utils

import android.content.Context

class FileHelper {

    companion object {
        fun getTextFromResources(context: Context, resId: Int): String {
            return context.resources.openRawResource(resId).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }
        fun getTextFromAsset(context: Context, filename: String): String {
            return context.assets.open(filename).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }
    }
}