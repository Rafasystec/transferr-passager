package br.com.transferr.fragments


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.extensions.toast
import br.com.transferr.helpers.HelperCamera
import br.com.transferr.main.util.HelperBase64
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.util.ImageUtil
import br.com.transferr.webservices.DriverService
import br.com.transferr.webservices.UserService
import com.mvc.imagepicker.ImagePicker
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.driver.fragment_driver_edit_info.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import java.io.File
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class DriverEditInfo : SuperClassFragment() {

    private val camera      = HelperCamera()
    private val photoName   = "photoProfile.jpg"
    private var driver: Driver?=null
    var file: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_driver_edit_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(R.id.toolbar,getString(R.string.myInformation),true)
        initViews()
        camera.init(savedInstanceState)
        checkCameraPermission()
        ImagePicker.setMinQuality(600, 600);
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews(){
        //btnCamera.setOnClickListener{btnCameraClick()}
        imgProfile.setOnClickListener{btnCameraClick()}
        btnAlterPass.setOnClickListener{btnAlterPassClick()}
        getDriver()
    }

    private fun loadPhoto(driver:Driver){
        var url = driver.car?.photo
        progressProfile.visibility  = View.VISIBLE
        imgProfile.visibility       = View.GONE
        Picasso.with(activity).load(url)
                .fit()
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.no_photo_64)
                .into(imgProfile,object : Callback {
                    override fun onSuccess() {
                        progressProfile.visibility  = View.GONE
                        imgProfile.visibility       = View.VISIBLE
                    }

                    override fun onError() {
                        progressProfile.visibility  = View.GONE
                        imgProfile.visibility       = View.VISIBLE
                    }

                })
    }

    private fun initScreenFields(driver:Driver){
        this.driver                 = driver
        lblDriverNameValue.text     = driver.name
        lblDriverNameValue
        lblCfpValue.text            = driver.countryRegister
        lblDtNascimentoValue.text   = DateUtil.format(driver.birthDate!!,"dd/MM/yyyy")
        loadPhoto(driver)
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
                        showError(t)
                    }

                })
    }

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

    private fun postImageProfile(bitmap: Bitmap){
        var anexo = AnexoPhoto()
        if(this.driver == null){
            stopProgressBar()
            toast("Nenhum motorista para relacionar com a foto!")
            return
        }
        anexo.identity      = this.driver!!.id.toString()
        anexo.anexoBase64   = HelperBase64.getPhotoString(bitmap)//FileUtil.toBase64(camera.file!!)
        DriverService.savePhoto(anexo!!,
                object : OnResponseInterface<ResponseOK>{
                    override fun onSuccess(body: ResponseOK?) {
                        stopProgressBar()
                        toast("Foto salva com sucesso!")
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        showError(t)
                    }

                }
        )

    }

    private fun btnCameraClick(){
        ImagePicker.pickImage(this, "Selecione a Imagem:")
        //startActivityForResult(camera.open(activity!!,photoName),0)
    }

    private fun btnAlterPassClick(){
        if(validate()){
            initProgressBar()
            callServiceToChangeThePassword()
        }
    }

    private fun validate():Boolean{
        val oldPassword = txtOldPassword.text.toString().trim()
        val newPassword = txtNewPassword.text.toString().trim()
        val confPassword= txtConfirmNewPassword.text.toString().trim()
        if(oldPassword.isEmpty()){
            toast("Por favor informe a sua senha antiga!")
            return false
        }else if(newPassword.isEmpty()){
            toast("Por favor informe a nova senha!")
            return false
        }else if(confPassword.isEmpty()){
            toast("Por favor confirme a senha!")
            return false
        }else{
            if(oldPassword == newPassword){
                toast("Por favor informe uma senha diferente da sua antiga")
                return false
            }
            if(newPassword != confPassword){
                toast("As senhas não conferem!")
                return false
            }
        }
        return true
    }

    private fun callServiceToChangeThePassword(){
        var oldPassword = txtOldPassword.text.toString()
        var newPassword = txtNewPassword.text.toString()
        UserService.changePassword(Prefes.prefsLogin,oldPassword,newPassword,
                object : OnResponseInterface<ResponseOK>{
                    override fun onSuccess(body: ResponseOK?) {
                        cleraPasswprd()
                        stopProgressBar()
                        toast("Senha alterada com sucesso!")
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        showError(t)
                    }

                }
        )
    }

    private fun cleraPasswprd(){
        txtOldPassword.setText("")
        txtConfirmNewPassword.setText("")
        txtNewPassword.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable("file",file)
        camera.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //log("result code: $resultCode")
        if(resultCode == Activity.RESULT_OK){
            var bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data)
            val imagePathFromResult = ImagePicker.getImagePathFromResult(activity, requestCode, resultCode, data)
            var matrix: Matrix? = null
            try {
                val exif = ExifInterface(imagePathFromResult)
                val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                val rotationInDegrees = ImageUtil.exifToDegrees(rotation)
                matrix = Matrix()
                if (rotation.toFloat() != 0f) {
                    matrix.preRotate(rotationInDegrees.toFloat())
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            //Se a camera retornou vamos mostar o arquivo da foto
            //val bitmap = camera.getBitmap(600,600)
            if(bitmap != null) {
                bitmap = ImageUtil.rotate(bitmap,matrix)
                imgProfile.setImageBitmap(bitmap)
                camera.save(bitmap)
                //showImage(camera.file)
                postImageProfile(bitmap)
            }
        }
    }

    private fun showImage(file: File?){
        if(file != null && file.exists()){
            val w = imgProfile.width
            val h = imgProfile.height
            val bitmap = ImageUtil.resize(file,w,h)
            imgProfile.setImageBitmap(bitmap)
        }
    }

    val PERMISSIONS_FOR_THIS_ACTIVITY = 3
    fun checkCameraPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            //Ask for permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)){
                //Should explain
                activity!!.alert(message = "Sem as permissões necessário o aplicativo não poderá continuar funcionando. Deseja conceder as permissões?",title = "Permissões Necessárias") {
                    onCancelled { activity!!.finish() }
                    okButton {
                        ActivityCompat.requestPermissions(activity!!,
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                PERMISSIONS_FOR_THIS_ACTIVITY)
                    }
                    cancelButton { activity!!.finish() }
                }.show()
            }else{
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_FOR_THIS_ACTIVITY)
            }
        }
    }




}// Required empty public constructor
