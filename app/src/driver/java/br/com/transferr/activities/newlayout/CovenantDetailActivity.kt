package br.com.transferr.activities.newlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import br.com.transferr.main.util.PicassoUtil
import br.com.transferr.model.COVENANT_PARAM_NAME
import br.com.transferr.model.Covenant
import kotlinx.android.synthetic.driver.content_covenant_detail.*


class CovenantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covenant_detail)
        initFillFields()
    }


    private fun initFillFields(){
        val covenant = intent.getSerializableExtra(COVENANT_PARAM_NAME) as Covenant
        txtDetailCovenantMessage.text = covenant.message
        txtDetailCovenantAdvice.text = "Texto fixo aqui"
        PicassoUtil.build(covenant.urlLogo!!,imgDetailCovenantLogo)
    }

}
