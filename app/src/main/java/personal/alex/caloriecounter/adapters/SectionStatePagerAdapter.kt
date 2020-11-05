package personal.alex.caloriecounter.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import personal.alex.caloriecounter.fragment.CalorieCounterFragment

class SectionStatePagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    val NUM_ITEMS = 1

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> CalorieCounterFragment().newInstance(1,"Calorie Counter Frag")
            else -> CalorieCounterFragment().newInstance(1,"Calorie Counter Frag")
        }
    }
}