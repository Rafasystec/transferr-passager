package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.TabLocationAdapter
import br.com.transferr.passager.model.Location
import kotlinx.android.synthetic.main.activity_location_detail.*


class LocationDetailActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)
        setupViewPageTabs()
    }

    fun setupViewPageTabs(){
        viewPageLocation.offscreenPageLimit = 1
        viewPageLocation.adapter = TabLocationAdapter(context,supportFragmentManager,intent.getSerializableExtra(Location.LOCATION) as Location)
        tabLayoutLocation.setupWithViewPager(viewPageLocation)
        val colorText = ContextCompat.getColor(context,R.color.colorPrimaryDark)
        tabLayoutLocation.setTabTextColors(colorText,colorText)
    }
}
