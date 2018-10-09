package br.com.transferr.fragments


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.*
import br.com.transferr.R
import br.com.transferr.activities.DriverInforActivity
import br.com.transferr.activities.LoginActivity
import br.com.transferr.broadcast.InternetBroadCast
import br.com.transferr.extensions.*
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.NetworkUtil
import br.com.transferr.main.util.Prefes
import br.com.transferr.webservices.CarService
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.activity_main.*


/**
 * A simple [Fragment] subclass.
 */
class DriverShowInfoFragment : SuperClassFragment() {

    lateinit var locationManager: LocationManager
    var car:Car?=null
    var internetBroadCast = InternetBroadCast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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
                //startActivity(Intent(activity,DriverEditInfoActivity::class.java))
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
        checkNetwork()
    }
/*
    private fun callFormDriver(){
        val intentInit = Intent(context, DriverInforActivity::class.java)
        startActivity(intentInit)
    }
    */
/*
    private fun stopInitLocation(){
        if(swtOnline.isChecked){
            startService()
            swtOnline.setTextColor(Color.BLUE)
            onlineOffline(true)
        }else{
            stopServiceIntent()
            onlineOffline(false)
            swtOnline.setTextColor(Color.BLACK)
        }
    }
    */
    /*
    private fun startService(){
        if(isLocationPermissionGranted()) {
            if(!isLocationEnabled()) {
                showAlert()
            }else{
                startServiceIntent()
            }

        }
        activity?.startService(Intent(context, LocationTrackingService::class.java))
    }
    */
/*
    private fun stopServiceIntent(){
        activity?.stopService(Intent(context, LocationTrackingService::class.java))
    }

    private fun startServiceIntent(){
        activity?.startService(Intent(context, LocationTrackingService::class.java))
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    */
/*
    private fun showAlert() {
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle("Habilitar Localização")
                .setMessage("Precisamos ativar o GPS.\nPor favor ative-o.")
                .setPositiveButton("Ativar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                    swtOnline.isChecked = true
                    swtOnline.setTextColor(Color.BLUE)
                    startServiceIntent()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    swtOnline.isChecked = false
                    swtOnline.setTextColor(Color.BLACK)
                    stopServiceIntent()
                })
        dialog.show()
    }
    */
/*
    private fun isLocationPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }
    */
/*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null && swtOnline != null) {
            outState?.putBoolean(VariablesUtil.ONLINE, swtOnline.isChecked)
        }
    }

    private fun initScreenFields(car: Car){
        this.car = car
        Prefes.prefsCar = car.id!!
        lblColorValue.text = car.color
        lblDriverValue.text= car.driver?.name
        lblModelValue.text = car.model
        lblPlacaValue.text = car.carIdentity
        swtOnline.isChecked = car.status!= EnumStatus.OFFLINE
        //Prefes.prefsDriver = car?.driver?.id!!
    }
*/
    private fun initScreenFields(driver: Driver){
        this.car = driver.car
        Prefes.prefsCar = car?.id!!
        lblColorValue.text = car?.color
        lblDriverValue.text= driver?.name
        lblModelValue.text = car?.model
        lblPlacaValue.text = car?.carIdentity
        //swtOnline.isChecked = car?.status!= EnumStatus.OFFLINE
        Prefes.prefsDriver = driver?.id!!
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

    /*
    private fun checkUserLogin():Boolean{
        val id = Prefes.prefsLogin
        if(id == null || id <= 0){
            return false
        }
        return true
    }
    */
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


/*
    private fun initProgressBar(){
        activity?.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        activity?.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }
    */
/*
    private fun checkLogin(){
        val isLoged = checkUserLogin()
        if(isLoged) {
            if(isConnected()) {
                getCarFromWebService()
                //stopInitLocation()
            }else{
               // showError("Sem conexão com a Internet.")
            }

        }else{
            callLoginActivity()
        }
    }
    */

    private fun callLoginActivity(){
        startActivity(Intent(context, LoginActivity::class.java))

    }
/*
    private fun onlineOffline(online:Boolean){
        var request = RequestCoordinatesUpdate()
        request.idCar = Prefes.prefsCar
        if(online) {
            //Pegar as coordenadas reais
            CarService.online(request,
                    object : OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            stopProgressBar()
                            log("OK")
                        }

                        override fun onError(message: String) {
                            stopProgressBar()
                            //showValidation(message)
                        }

                        override fun onFailure(t: Throwable?) {
                            stopProgressBar()
                            showError(t)
                        }

                    }
            )
        }else{
            CarService.offline(request,
                    object : OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            stopProgressBar()
                            log("OK- Estou offline")
                        }

                        override fun onError(message: String) {
                            stopProgressBar()
                            //showValidation(message)
                        }

                        override fun onFailure(t: Throwable?) {
                            stopProgressBar()
                            showError(t)
                        }

                    }
            )
        }

    }
    */
    /*
    private fun getBundleValues(savedInstanceState: Bundle?){
        if(savedInstanceState != null){
            swtOnline.isChecked = savedInstanceState.getBoolean(VariablesUtil.ONLINE)
        }
    }
    */

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
