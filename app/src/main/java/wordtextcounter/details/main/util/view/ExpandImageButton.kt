package wordtextcounter.details.main.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.orhanobut.logger.Logger

class ExpandImageButton : ImageView {

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,
      attributeSet, defStyleAttr)

  init {
    Logger.d("In init block")
  }
}