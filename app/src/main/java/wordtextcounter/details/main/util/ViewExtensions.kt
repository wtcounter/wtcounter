package wordtextcounter.details.main.util

import android.app.Activity
import android.content.ContextWrapper
import android.support.design.widget.FloatingActionButton
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.TranslateAnimation
import com.orhanobut.logger.Logger

infix fun FloatingActionButton.onClick(function: () -> Unit) {

  val activity = getActivity()

  val dm = DisplayMetrics()
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  val statusBarHeight = resources.getDimensionPixelSize(resourceId)
  val originalPosition = intArrayOf(0, 0)

  setOnClickListener {

    activity?.windowManager?.defaultDisplay?.getMetrics(dm)

    getLocationOnScreen(originalPosition)

    val xDest = dm.widthPixels / 2 - measuredWidth / 2
    val yDest = dm.heightPixels / 2 - measuredHeight / 2 - statusBarHeight

    val anim = TranslateAnimation(
        0f, (xDest - originalPosition[0]).toFloat(), 0f,
        (yDest - originalPosition[1]).toFloat()
    )
    anim.interpolator = AnticipateInterpolator()
    anim.duration = 400
    anim.setAnimationListener(object : AnimationListener {
      override fun onAnimationRepeat(animation: Animation?) {

      }

      override fun onAnimationEnd(animation: Animation?) {
        visibility = View.GONE
        function()
      }

      override fun onAnimationStart(animation: Animation?) {

      }

    })
    startAnimation(anim)
  }

}

fun FloatingActionButton.backToPosition() {
  val activity = getActivity()

  val dm = DisplayMetrics()
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  val statusBarHeight = resources.getDimensionPixelSize(resourceId)
  val originalPosition = intArrayOf(0, 0)

  activity?.windowManager?.defaultDisplay?.getMetrics(dm)

  getLocationOnScreen(originalPosition)

  Logger.d("Original position ${originalPosition[0]} ${originalPosition[1]}")

  val xDest = dm.widthPixels / 2 - measuredWidth / 2
  val yDest = dm.heightPixels / 2 - measuredHeight / 2 - statusBarHeight

  val anim = TranslateAnimation(
      (xDest - originalPosition[0]).toFloat(), 0f,
      (yDest - originalPosition[1]).toFloat(), 0f
  )
  anim.interpolator = AnticipateOvershootInterpolator()
  anim.duration = 400
  anim.setAnimationListener(object : AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
      visibility = View.VISIBLE
    }

    override fun onAnimationStart(animation: Animation?) {

    }

  })
  startAnimation(anim)
}

private fun View.getActivity(): Activity? {
  var context = context
  while (context is ContextWrapper) {
    if (context is Activity) {
      return context
    }
    context = context.baseContext
  }
  return null
}