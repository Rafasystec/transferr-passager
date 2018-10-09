package br.com.transferr.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.transferr.R
import br.com.transferr.adapters.SpTourOptionAdapter
import br.com.transferr.extensions.*
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Driver
import br.com.transferr.model.PlainTour
import br.com.transferr.model.TourOption
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.webservices.PlainTourService
import br.com.transferr.webservices.TourOptionService
import kotlinx.android.synthetic.driver.fragment_driver_add_plan_tour.*
import kotlinx.android.synthetic.driver.fragment_driver_add_plan_tour_content.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DriverAddPlanTourFragment : SuperClassFragment() {

    var plainTour:PlainTour?=null
    var stringDate = ""
    var stringTime = ""
    var seatOccuped:Int = 0
    var selectedTourOption:TourOption?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_add_plan_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbarAddPlan,getString(R.string.addPlanTour),true)
        setHasOptionsMenu(true)
        getAllTourOption()
        initSpinners()
        plainTour = PlainTour()
        plainTour!!.open = false
        //TODO Check if the previous screen sent a tour by parameter
        setOnClickOnTheButtons()
    }

    private fun setOnClickOnTheButtons() {
        edtPlanTourTime.setOnClickListener {
            //DialogTimePickerFragment().showNow(activity?.supportFragmentManager,"timePicker")
            showTimePicker({ time ->
                run {
                    edtPlanTourTime.text = time
                    stringTime = time
                }
            }, plainTour?.date)
        }
        edtPlanTourDate.setOnClickListener {
            showDatePicker({ date ->
                run {
                    edtPlanTourDate.text = date
                    stringDate = date
                }
            }, plainTour?.date)
        }
        btnPlanTourDelete.setOnClickListener {
            toast("Delete this Plan")
        }
        btnAddPlanTourClose.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add_plan_tour,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.menuPlanTourSave ->{
                save(plainTour!!)
                true
            }else ->return super.onOptionsItemSelected(item)
        }
    }

    fun initSpinners(){
        spPlanTourBusySeats.adapter = ArrayAdapter<Int>(activity,R.layout.layout_spinner_item, listOf(1,2,3,4,5,6,7,8,9,10))
    }

    private fun initializeSpinnerLocation(tourOptions:List<TourOption>){
        spNameLocation.adapter = SpTourOptionAdapter(activity!!,tourOptions)
        spNameLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //toast("Selecione um local!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var selectedLocation = tourOptions[position]
                if(selectedLocation != null){
                    //requestPlain.idTourOption = selectedLocation.id!!
                }
            }

        }
        spNameLocation.prompt = "selecione"
        spNameLocation.requestFocus()
        spNameLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedTourOption = tourOptions[position]
            }

        }
    }

    fun getAllTourOption(){
        TourOptionService.getAll(
                object : OnResponseInterface<List<TourOption>> {
                    override fun onSuccess(body: List<TourOption>?) {
                        initializeSpinnerLocation(body!!)
                    }

                    override fun onError(message: String) {
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        showError(t)
                    }
                })
    }

    fun validate(plainTour: PlainTour) :Boolean{
        if(plainTour == null){
            showAlertValidation(R.string.noPlanTourToSave)
            return false
        }
        if(stringDate.isEmpty()){
            showAlertValidation(R.string.pleaseInformDate)
            return false
        }
        if(stringTime.isEmpty()){
            showAlertValidation(R.string.pleaseInformTime)
            return false
        }
        if(selectedTourOption == null){
            showAlertValidation(R.string.pleaseSelectATour)
            return false
        }
        return true
    }

    fun save(plainTour: PlainTour){
        if(validate(plainTour)){
            bindFields()
            post(plainTour)
        }
    }

    fun post(planTour: PlainTour){
        var alert = showLoadingDialog()
        PlainTourService.save(planTour,object : OnResponseInterface<PlainTour>{
            override fun onSuccess(pPlanTour: PlainTour?) {
                alert.dismiss()
                plainTour = pPlanTour!!
                showAlert(R.string.dataSavedSuccessfully,{activity?.finish()})
            }

            override fun onError(message: String) {
                alert.dismiss()
                showAlertError(message)
            }

            override fun onFailure(t: Throwable?) {
                alert.dismiss()
                showAlertFailure(R.string.SystemFailure)
            }

        })
    }

    private fun transformDateAndTime() : Date{
        return DateUtil.toDate(stringDate+" "+stringTime,"dd/MM/yyyy HH:mm")
    }

    private fun bindFields() {
        if (plainTour == null) {
            plainTour = PlainTour()
        }

        with(plainTour!!) {
            date        = transformDateAndTime()
            driver      = Prefes.driver
            tourOption  = selectedTourOption
            //---------------------------------------------------------
            //Passenger One
            //---------------------------------------------------------
            namePassenger1  = tvPlanTourPassengerOneName.text.toString()
            notesPassenger1 = edtPassengerOneNotes.text.toString()
            telPassenger1   = edtPassengerOnePhone.text.toString()
            //---------------------------------------------------------
            //Passenger Two
            //---------------------------------------------------------
            namePassenger2  = tvPlanTourPassengerTwoName.text.toString()
            notesPassenger2 = edtPassengerTwoNotes.text.toString()
            telPassenger2   = edtPassengerTwoPhone . text . toString ()
            //---------------------------------------------------------
            //Passenger Three
            //---------------------------------------------------------
            namePassenger3  = tvPlanTourPassengerThreeName.text.toString()
            notesPassenger3 = edtPassengerThreeNotes.text.toString()
            telPassenger3   = edtPassengerThreePhone.text.toString()
            //---------------------------------------------------------
            notesOfPlain    = edtPlanTourNotes.text.toString()
        }

    }

}
