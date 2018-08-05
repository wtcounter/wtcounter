package wordtextcounter.details.main.util.view

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.widget.ImageView
import wordtextcounter.details.main.R

class ExpandImageButton : ImageView {

  constructor(context: Context) : super(context)

  constructor(
      context: Context,
      attributeSet: AttributeSet
  ) : super(context, attributeSet)

  constructor(
      context: Context,
      attributeSet: AttributeSet,
      defStyleAttr: Int
  ) : super(
      context,
      attributeSet, defStyleAttr
  )

  var expanded: Boolean
  private val avMoreToLess: AnimatedVectorDrawableCompat?
  private val avLessToMore: AnimatedVectorDrawableCompat?

  init {
    expanded = false
    avMoreToLess = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_more_to_less)
    avLessToMore = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_less_to_more)
    setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_expand_more_black_24dp))
  }

  infix fun onClick(action: () -> Unit) {
    setOnClickListener {
      expandArrow()
      action()
    }
  }

  internal fun expandArrow() {
    if (!expanded) {
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        setImageDrawable(avMoreToLess)
        avMoreToLess?.start()
      } else {
        setImageDrawable(
            AppCompatResources.getDrawable(context, R.drawable.ic_expand_less_black_24dp)
        )
      }
    } else {
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        setImageDrawable(avLessToMore)
        avLessToMore?.start()
      } else {
        setImageDrawable(
            AppCompatResources.getDrawable(context, R.drawable.ic_expand_more_black_24dp)
        )
      }
    }
    expanded = !expanded

  }
}