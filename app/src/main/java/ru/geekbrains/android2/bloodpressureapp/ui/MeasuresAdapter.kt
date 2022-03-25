package ru.geekbrains.android2.bloodpressureapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.android2.bloodpressureapp.databinding.MeasureItemBinding
import ru.geekbrains.android2.bloodpressureapp.databinding.MeasureItemNextBinding
import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj
import ru.geekbrains.android2.bloodpressureapp.utils.dateToStr
import ru.geekbrains.android2.bloodpressureapp.utils.dateToStrHM
import ru.geekbrains.android2.bloodpressureapp.utils.dayChanged
import ru.geekbrains.android2.bloodpressureapp.utils.setBackgroundPressure

class MeasuresAdapter(
    private val list: ArrayList<MeasureObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickListenerToEdit = MutableLiveData<MeasureObj>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FIRST) MeasureHolder(
            MeasureItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            MeasureHolderNext(
                MeasureItemNextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = list[position]
        if (getItemViewType(position) == TYPE_FIRST) {
            holder as MeasureHolder
            holder.textDate.text = dateToStr(obj.dateOfMeasure)
            holder.textTime.text = dateToStrHM(obj.dateOfMeasure)
            holder.textSys.text = obj.sys.toString()
            holder.textDia.text = obj.dia.toString()
            holder.textPulse.text = obj.pulse.toString()
            holder.binding.root.setOnLongClickListener {
                clickListenerToEdit.postValue(obj)
                true
            }
            holder.binding.root.setBackgroundPressure(obj.sys)
        } else {
            holder as MeasureHolderNext
            holder.textTime.text = dateToStrHM(obj.dateOfMeasure)
            holder.textSys.text = obj.sys.toString()
            holder.textDia.text = obj.dia.toString()
            holder.textPulse.text = obj.pulse.toString()
            holder.binding.root.setOnLongClickListener {
                clickListenerToEdit.postValue(obj)
                true
            }
            holder.binding.root.setBackgroundPressure(obj.sys)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_FIRST
            dayChanged(list[position - 1].dateOfMeasure, list[position].dateOfMeasure) -> TYPE_FIRST
            else -> TYPE_NEXT
        }
    }

    inner class MeasureHolder(val binding: MeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var textDate: TextView = binding.textDate
        var textTime: TextView = binding.textTime
        var textSys: TextView = binding.textSys
        var textDia: TextView = binding.textDia
        var textPulse: TextView = binding.textPulse
    }

    inner class MeasureHolderNext(val binding: MeasureItemNextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var textTime: TextView = binding.textTime
        var textSys: TextView = binding.textSys
        var textDia: TextView = binding.textDia
        var textPulse: TextView = binding.textPulse
    }

    companion object {
        private const val TYPE_FIRST = 1
        private const val TYPE_NEXT = 0
    }
}