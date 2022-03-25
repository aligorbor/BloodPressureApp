package ru.geekbrains.android2.bloodpressureapp.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.geekbrains.android2.bloodpressureapp.R
import ru.geekbrains.android2.bloodpressureapp.databinding.CustomAlertDialogBinding
import ru.geekbrains.android2.bloodpressureapp.databinding.FragmentMeasuresBinding
import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj
import ru.geekbrains.android2.bloodpressureapp.utils.showSnackBar

class MeasuresFragment : Fragment() {
    private var _binding: FragmentMeasuresBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MeasuresViewModel

    var progressDialog: ProgressDialog? = null
    private var popupInputDialogView: View? = null
    private var sysInput: EditText? = null
    private var diaInput: EditText? = null
    private var pulseInput: EditText? = null
    private var saveMeasureButton: ImageView? = null
    private var cancelMeasureButton: ImageView? = null
    private var deleteMeasureButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeasuresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeasuresViewModel::class.java).apply {
            getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
            getListOfMeasures()
        }
        progressDialog = ProgressDialog(requireContext())

        binding.fab.setOnClickListener { fabButtonView ->
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(R.string.create_measure)
            alertDialogBuilder.setCancelable(true)
            initPopupViewControls()
            alertDialogBuilder.setView(popupInputDialogView)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            saveMeasureButton?.setOnClickListener { saveButtonView ->
                saveData(alertDialog)
            }
            cancelMeasureButton?.setOnClickListener { cancelButtonView ->
                alertDialog.cancel()
            }
            deleteMeasureButton?.setOnClickListener { cancelButtonView ->
                alertDialog.cancel()
            }
        }
    }

    private fun renderData(appState: AppStateMeasures) {
        when (appState) {
            is AppStateMeasures.Success -> {
                progressDialog?.dismiss()
                initMeasuresList(appState.measures)
            }
            is AppStateMeasures.Loading -> {
                progressDialog?.show()
            }
            is AppStateMeasures.Error -> {
                binding.root.showSnackBar(
                    appState.error.message ?: "",
                    getString(R.string.reload),
                    {
                        viewModel.getListOfMeasures()
                    })
            }
        }
    }

    private fun saveData(alertDialog: AlertDialog, measureObj: MeasureObj = MeasureObj()) {
        if (sysInput?.text.toString().isNotEmpty() && diaInput?.text.toString().isNotEmpty()
        ) {
            alertDialog.cancel()
            progressDialog?.show()
            measureObj.sys = sysInput?.text.toString().toInt()
            measureObj.dia = diaInput?.text.toString().toInt()
            measureObj.pulse = pulseInput?.text.toString().toInt()
            viewModel.saveMeasure(measureObj)
        } else {
            showAlert(getString(R.string.save_err), getString(R.string.save_err_msg))
        }
    }

    private fun initMeasuresList(list: List<MeasureObj>) {
        if (list.isEmpty()) {
            binding.emptyText.visibility = View.VISIBLE
            return
        }
        binding.emptyText.visibility = View.GONE

        val adapter = MeasuresAdapter(list as ArrayList<MeasureObj>)

        adapter.clickListenerToEdit.observe(viewLifecycleOwner) { measureObj ->
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.update_measure))
            alertDialogBuilder.setCancelable(true)
            initPopupViewControls(
                measureObj.sys,
                measureObj.dia,
                measureObj.pulse
            )

            alertDialogBuilder.setView(popupInputDialogView)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            saveMeasureButton?.setOnClickListener { saveButtonView ->
                saveData(alertDialog, measureObj)
            }
            cancelMeasureButton?.setOnClickListener { cancelButtonView ->
                alertDialog.cancel()
            }
            deleteMeasureButton?.setOnClickListener { cancelButtonView ->
                viewModel.deleteMeasure(measureObj)
                alertDialog.cancel()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.adapter = adapter
    }


    private fun initPopupViewControls(sys: Int = 0, dia: Int = 0, pulse: Int = 0) {
        val bndng = CustomAlertDialogBinding.inflate(LayoutInflater.from(requireContext()))
        popupInputDialogView = bndng.root
        sysInput = bndng.textSys
        diaInput = bndng.textDia
        pulseInput = bndng.textPulse
        saveMeasureButton = bndng.imageSave
        cancelMeasureButton = bndng.imageCancel
        deleteMeasureButton = bndng.imageDelete
        if (sys > 0) {
            sysInput?.setText(sys.toString())
            diaInput?.setText(dia.toString())
            pulseInput?.setText(pulse.toString())
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, which ->
                dialog.cancel()
            }
        val ok = builder.create()
        ok.show()
    }

    private fun openFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MeasuresFragment()
    }
}