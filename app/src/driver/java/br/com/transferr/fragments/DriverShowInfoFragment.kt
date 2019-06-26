package br.com.transferr.fragments


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.*
import br.com.transferr.R
import br.com.transferr.broadcast.InternetBroadCast
import br.com.transferr.extensions.*
import br.com.transferr.main.util.LanguageDeviceUtil
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.CarService
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.fragment_driver_show_info.*
import kotlinx.android.synthetic.main.layout_no_internet_connection.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton


/**
 * A simple [Fragment] subclass.
 */
class DriverShowInfoFragment : SuperClassFragment() {

    var car:Car?=null
    //var isFirstTime:Boolean = true
    var internetBroadCast = InternetBroadCast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_driver_show_info, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_edit_driver,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.action_edit_driver->{
                if(isConnected()) {
                    switchFragmentToMainContent(DriverEditInfo())
                    true
                }else false
            }else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.myInformation))
        initScreenAndControls()
        btnTryAgain.setOnClickListener { initScreenAndControls() }
    }

    private fun initScreenAndControls() {
        registerInternetReceiver()
        getCarFromWebService()
        initView()
    }

    private fun initView(){
        swtOnOffAlwaysParam.setOnClickListener { updateAlwaysParameter() }
        checkNetwork()
    }

    private fun initScreenFields(driver: Driver){
        this.car = driver.car
        Prefes.prefsCar = car?.id!!
        lblColorValue.text = LanguageDeviceUtil.getColorAsString(car?.color!!)
        lblDriverValue.text= driver?.name
        lblModelValue.text = car?.model
        lblPlacaValue.text = car?.carIdentity
        Prefes.prefsDriver = driver?.id!!
        swtOnOffAlwaysParam.isChecked = this.car!!.alwaysOnMap
    }

    private fun getCarFromWebService(){
        val progress = showLoadingDialogWithDelay(title = getString(R.string.gettingTheCar))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(body: Driver?) {
                        try {
                            initScreenFields(body!!)
                        }finally {
                            progress.dismiss()
                        }
                    }
                }
        , activity!!,progress)

    }

    private fun checkNetwork(){
        var isConnectedNow = isConnected()
        if(!isConnectedNow){
            showMessage(isConnectedNow)
            llNoInternetConn.visibility = View.VISIBLE
            llDriverShowInfo.visibility = View.GONE
        }else{
            llNoInternetConn.visibility = View.GONE
            llDriverShowInfo.visibility = View.VISIBLE
        }
    }

    private fun showMessage(connectedNow: Boolean) = if(!connectedNow){toast(getString(R.string.youAreOffLine))}else{}
    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(activity!!)
    }

    private fun updateAlwaysParameter(){
        if(!swtOnOffAlwaysParam.isChecked) {
            activity?.alert(getString(R.string.turnAlwaysParamOff), activity?.getString(R.string.Advice)!!) {
                okButton {
                    confirmUpdateAlwaysParameter()
                    it.dismiss()
                }
                cancelButton {
                    swtOnOffAlwaysParam.isChecked = !swtOnOffAlwaysParam.isChecked
                    it.dismiss()
                }
            }?.show()
        }else{
            confirmUpdateAlwaysParameter()
        }

    }

    private fun confirmUpdateAlwaysParameter() {
        var isChecked = swtOnOffAlwaysParam.isChecked
        var alert = showLoadingDialogWithDelay()
        if(car != null) {
            CarService.changeAlwaysParameter(car?.id!!, isChecked, object : OnResponseInterface<ResponseOK> {
                override fun onSuccess(body: ResponseOK?) {
                    alert.dismiss()
                }

            }, activity, alert)
        }else {
            try{
                swtOnOffAlwaysParam.isChecked = false
            }finally {
                alert.dismiss()
            }
        }
    }

    private fun registerInternetReceiver(){
       activity?.registerReceiver(internetBroadCast,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterInternetReceiver()
    }

    private fun unregisterInternetReceiver(){
        try {
            activity?.unregisterReceiver(InternetBroadCast())
            LocalBroadcastManager.getInstance(activity!!)
                    .unregisterReceiver(internetBroadCast)
        }catch (illegal:RuntimeException){
            Log.e("ERRO",illegal.message)
        }
    }

}// Required empty public constructor
