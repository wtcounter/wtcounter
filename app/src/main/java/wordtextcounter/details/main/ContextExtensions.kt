@file:Suppress("NOTHING_TO_INLINE")

package wordtextcounter.details.main

import android.content.Context
import android.content.Context.MODE_PRIVATE
import wordtextcounter.details.main.util.Constants.PREF_NAME

inline fun Context.getPreference() = getSharedPreferences(PREF_NAME, MODE_PRIVATE)