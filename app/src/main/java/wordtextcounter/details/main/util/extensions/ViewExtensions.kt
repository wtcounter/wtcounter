package wordtextcounter.details.main.util.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import wordtextcounter.details.main.R

fun View.slideUp() {

  val slideAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
  slideAnimation.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationEnd(animation: Animation?) {

    }

    override fun onAnimationStart(animation: Animation?) {
      visibility = View.VISIBLE
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }
  })
  startAnimation(slideAnimation)

}