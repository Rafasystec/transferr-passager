package br.com.transferr.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R

import br.com.transferr.driver.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.helpers.HelperCamera
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.activity_driver_infor.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class DriverEditInfo : Fragment() {

    private val camera      = HelperCamera()
    private val photoName   = "photoProfile.jpg"
    private var driver: Driver?=null
    var file: File? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        setupToolbar(R.id.toolbar,"Meus Dados",true)
        initViews()
        camera.init(savedInstanceState)
        return inflater.inflate(R.layout.fragment_driver_edit_info, container, false)
    }

    private fun initViews(){
        btnCamera.setOnClickListener{btnCameraClick()}
        btnAlterPass.setOnClickListener{btnAlterPassClick()}
        getDriver()
        loadPhoto(Prefes.ID_CAR)
    }


    private fun getDriver(){
        initProgressBar()
        DriverService.getDriverByCar(Prefes.prefsCar,
                object : OnResponseInterface<Driver> {
                    override fun onSuccess(driver: Driver?) {
                        stopProgressBar()
                        initScreenFields(driver!!)
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        showError(t?.message!!)
                    }

                })
    }

}// Required empty public constructor
