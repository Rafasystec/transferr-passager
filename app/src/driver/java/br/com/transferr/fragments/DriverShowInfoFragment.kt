package br.com.transferr.fragments


import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.*
import br.com.transferr.R
import br.com.transferr.activities.LoginActivity
import br.com.transferr.broadcast.InternetBroadCast
import br.com.transferr.extensions.*
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.CarService
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.fragment_driver_show_info.*


/**
 * A simple [Fragment] subclass.
 */
class DriverShowInfoFragment : SuperClassFragment() {

    //lateinit var locationManager: LocationManager
    var car:Car?=null
    var isFirstTime:Boolean = true
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
                switchFragmentToMainContent(DriverEditInfo())
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.myInformation))
        registerInternetReceiver()
        getCarFromWebService()
        initView()
    }
    private fun initView(){
        swtOnOffAlwaysParam.setOnCheckedChangeListener{buttonView, isChecked ->  updateAlwaysParameter(isChecked)}
        checkNetwork()
    }

    private fun initScreenFields(driver: Driver){
        this.car = driver.car
        Prefes.prefsCar = car?.id!!
        lblColorValue.text = car?.color
        lblDriverValue.text= driver?.name
        lblModelValue.text = car?.model
        lblPlacaValue.text = car?.carIdentity
        Prefes.prefsDriver = driver?.id!!
        swtOnOffAlwaysParam.isChecked = this.car!!.alwaysOnMap;
    }

    private fun getCarFromWebService(){
        var progress = showLoadingDialog()
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(body: Driver?) {
                        initScreenFields(body!!)
                        progress.dismiss()
                    }

                    override fun onError(message: String) {
                        progress.dismiss()
                        toast(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        progress.dismiss()
                        toast("Erro ao logar ${t?.message}")
                    }

                }
        )

    }

    private fun checkNetwork(){
        var isConnectedNow = isConnected()
        if(!isConnectedNow){
            showMessage(isConnectedNow)
        }
    }

    private fun showMessage(connectedNow: Boolean) = if(!connectedNow){toast(getString(R.string.youAreOffLine))}else{}
    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(activity!!)
    }

    private fun callLoginActivity(){
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun updateAlwaysParameter(isChecked:Boolean){
        if(isFirstTime){
            isFirstTime = false
            return
        }
        var alert = showLoadingDialog()
        CarService.changeAlwaysParameter(car?.id!!,isChecked,object : OnResponseInterface<ResponseOK> {
            override fun onSuccess(body: ResponseOK?) {
                alert.dismiss()
                showAlert(R.string.dataSavedSuccessfully)
            }

            override fun onError(message: String) {
                alert.dismiss()
                showAlertValidation(message)
            }

            override fun onFailure(t: Throwable?) {
                alert.dismiss()
                showAlertFailure(t?.message!!)
            }

        })
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
