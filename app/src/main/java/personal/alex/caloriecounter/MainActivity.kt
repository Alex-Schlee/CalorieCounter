package personal.alex.caloriecounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import personal.alex.caloriecounter.adapters.SectionStatePagerAdapter


class MainActivity : AppCompatActivity() {

    lateinit var mViewPager: FragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = findViewById<ViewPager>(R.id.container)
        mViewPager = SectionStatePagerAdapter(supportFragmentManager)
        viewPager.adapter = mViewPager




    }



}
