package personal.alex.caloriecounter.adapters

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPagerAdapter(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var isTurnedOn : Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isTurnedOn) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isTurnedOn) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.isTurnedOn = enabled
    }
}