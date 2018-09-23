package wordtextcounter.details.main.feature.input

import android.support.annotation.StringRes

data class ExtraStatGroup(@StringRes val groupName: Int, val stats: List<ExtraStat>)