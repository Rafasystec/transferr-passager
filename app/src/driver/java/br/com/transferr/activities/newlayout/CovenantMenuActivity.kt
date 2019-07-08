package br.com.transferr.activities.newlayout

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import br.com.transferr.R
import br.com.transferr.activities.SuperClassActivity
import br.com.transferr.model.Covenant
import br.com.transferr.model.enums.EnumCategory
import br.com.transferr.passenger.extensions.defaultRecycleView
import kotlinx.android.synthetic.main.layout_content_covenant_list.*

class CovenantMenuActivity : SuperClassActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covenant_menu)
    }

    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View {
        defaultRecycleView(rcAutoParts)
        defaultRecycleView(rcFeeding)
        defaultRecycleView(rcInsurance)
        defaultRecycleView(rcWorkshopList)



        return super.onCreateView(parent, name, context, attrs)
    }


    fun getCovenants() : List<Covenant> {

        var covenant = Covenant()
        with(covenant){
            description = "Covenio Padre Cícero Auto-peças"
            category = EnumCategory.AUTOPARTS
            urlLogo = "https://media.licdn.com/dms/image/C4D0BAQGqZFO3y7mE1Q/company-logo_200_200/0?e=2159024400&v=beta&t=Nca71jPdX0aiVPrtyVdlk516Apneb5YwTJrxXoq1qxE"
            message = "Tudo em auto peças, voce tem 10% de deconto em qualquer compra"
        }
        var covenant1 = Covenant()
        with(covenant1){
            description = "Covenio Bezerra Oliveira"
            category = EnumCategory.AUTOPARTS
            urlLogo = "http://bezerraoliveira.com.br/wp/wp-content/uploads/2016/09/logo-bezerra-oliveira-autopecas@2x.png"
            message = "Tudo em auto peças, voce tem 10% de deconto em qualquer compra"
        }
        var covenants = mutableListOf<Covenant>(covenant,covenant1)
        return covenants
    }
}
