package br.com.transferr.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import br.com.transferr.R
import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.app.TimePickerDialog
import android.widget.*
import br.com.transferr.adapters.SpTourOptionAdapter
import br.com.transferr.dialogs.DialogTimePickerFragment
import br.com.transferr.extensions.*
import br.com.transferr.model.PlainTour
import br.com.transferr.model.TourOption
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.webservices.TourOptionService
import kotlinx.android.synthetic.driver.activity_frm_plain_tour.*
import kotlinx.android.synthetic.driver.fragment_driver_add_plan_tour_content.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DriverAddPlanTourFragment : SuperClassFragment() {

    var plainTour:PlainTour?=null
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
        plainTour!!.date = DateUtil.addDaysForADate(Date(),2)
        edtPlanTourTime.setOnClickListener {
            //DialogTimePickerFragment().showNow(activity?.supportFragmentManager,"timePicker")
            showTimePicker(  {time ->
                run {
                    edtPlanTourTime.text = time
                }
            },plainTour?.date)
        }
        edtPlanTourDate.setOnClickListener {
            showDatePicker({ date ->
                run {
                    edtPlanTourDate.text = date
                }
            },plainTour?.date)
        }
        btnPlanTourDelete.setOnClickListener {

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add_plan_tour,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.menuPlanTourSave ->{
                true
            }else ->return super.onOptionsItemSelected(item)
        }

    }

    fun initSpinners(){

        //val adapter = ArrayAdapter<String>(activity,R.layout.layout_spinner_item, listOf("Location One","Location Two","Location Three"))
        //spNameLocation.adapter = adapter
        spPlanTourBusySeats.adapter = ArrayAdapter<Int>(activity,R.layout.layout_spinner_item, listOf(1,2,3))

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



    }// Required empty public constructor
