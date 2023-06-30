package com.example.pokedex.swipes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokedex.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFrag(var statsFragMap: HashMap<String, HashMap<String, Long>>) : Fragment() {

    private lateinit var effortVal : HashMap<String,Long>
    private lateinit var statsVal : HashMap<String,Long>
    private lateinit var effortChart : RadarChart
    private lateinit var statsChart : RadarChart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_stats, container, false)
        statsChart = rootView.findViewById(R.id.baseStatsChart)
        effortChart = rootView.findViewById(R.id.effortStatsChart)
        readStatsFragMap()
        setupBaseChart()
        loadBaseChartData()
        setupEffortChart()
        loadEffortChartData()
        return rootView
    }

    private fun readStatsFragMap()
    {
        effortVal = statsFragMap["effort"] as HashMap<String, Long>
        statsVal = statsFragMap["base"] as HashMap<String, Long>
    }

    private fun setupBaseChart() {
        statsChart.apply {
            description.isEnabled = false
            webLineWidth = 1f
            webColor = Color.LTGRAY
            webLineWidthInner = 1f
            webColorInner = Color.LTGRAY
            webAlpha = 100
            xAxis.apply {
                setDrawLabels(true) // Disable the display of axis labels
                setDrawAxisLine(true) // Disable the display of axis lines
                setDrawGridLines(true) // Disable the display of grid lines

                valueFormatter = object : ValueFormatter()
                {
                    private val label = arrayOf("HP","ATK","DEF","SP_ATK","SP_DEF","SPEED")
                    private val values = arrayOf(
                        statsVal["hp"]!!.toFloat(),
                        statsVal["attack"]!!.toFloat(),
                        statsVal["defense"]!!.toFloat(),
                        statsVal["special-attack"]!!.toFloat(),
                        statsVal["special-defense"]!!.toFloat(),
                        statsVal["speed"]!!.toFloat())

                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in label.indices)
                        {
                            "${label[index]}\n${values[index]}"
                        }else {""}
                    }
                }

            }

            yAxis.apply {
                axisMinimum = 0f
                axisMaximum = statsVal.maxOf { it.value }.toFloat()
                setDrawLabels(false) // Disable the display of Y-axis labels
                setDrawAxisLine(false) // Disable the display of Y-axis axis line
                setDrawGridLines(false) // Disable the display of Y-axis grid lines
            }
        }


    }

    private fun setupEffortChart() {
        effortChart.apply {
            description.isEnabled = false
            webLineWidth = 1f
            webColor = Color.LTGRAY
            webLineWidthInner = 1f
            webColorInner = Color.LTGRAY
            webAlpha = 100


            xAxis.apply {
                setDrawLabels(true) // Disable the display of axis labels
                setDrawAxisLine(true) // Disable the display of axis lines
                setDrawGridLines(true) // Disable the display of grid lines


                valueFormatter = object : ValueFormatter()
                {
                    private val label = arrayOf("HP","ATK","DEF","SP_ATK","SP_DEF","SPEED")
                    private val values = arrayOf(
                        effortVal["hp"]!!.toFloat(),
                        effortVal["attack"]!!.toFloat(),
                        effortVal["defense"]!!.toFloat(),
                        effortVal["special-attack"]!!.toFloat(),
                        effortVal["special-defense"]!!.toFloat(),
                        effortVal["speed"]!!.toFloat())

                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in label.indices)
                        {
                            "${label[index]}\n${values[index]}"
                        }
                        else{ "" }
                    }
                }

            }


            yAxis.apply {
                setDrawLabels(false) // Disable the display of Y-axis labels
                setDrawAxisLine(false) // Disable the display of Y-axis axis line
                setDrawGridLines(false) // Disable the display of Y-axis grid lines
            }
        }

    }

    private fun loadBaseChartData() {
        val entries = listOf(
            RadarEntry(statsVal["hp"]!!.toFloat()),
            RadarEntry(statsVal["attack"]!!.toFloat()),
            RadarEntry(statsVal["defense"]!!.toFloat()),
            RadarEntry(statsVal["special-attack"]!!.toFloat()),
            RadarEntry(statsVal["special-defense"]!!.toFloat()),
            RadarEntry(statsVal["speed"]!!.toFloat()),
        )

        val dataSet = RadarDataSet(entries, "Base Stats")
        dataSet.color = Color.BLUE
        dataSet.fillColor = Color.BLUE
        dataSet.setDrawFilled(true)
        dataSet.setDrawHighlightIndicators(false)
        dataSet.setDrawVerticalHighlightIndicator(false)
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.setDrawFilled(true)
        dataSet.setDrawValues(false)
        val radarData = RadarData(dataSet)
        statsChart.data = radarData
        statsChart.invalidate()
    }

    private fun loadEffortChartData() {
        val entries = mutableListOf(
            RadarEntry(effortVal["hp"]!!.toFloat()),
            RadarEntry(effortVal["attack"]!!.toFloat()),
            RadarEntry(effortVal["defense"]!!.toFloat()),
            RadarEntry(effortVal["special-attack"]!!.toFloat()),
            RadarEntry(effortVal["special-defense"]!!.toFloat()),
            RadarEntry(effortVal["speed"]!!.toFloat()),
        )
        val dataSet = RadarDataSet(entries, "Effort Stats")
        dataSet.color = Color.RED
        dataSet.fillColor = Color.RED
        dataSet.setDrawFilled(true)
        dataSet.setDrawHighlightIndicators(false)
        dataSet.setDrawVerticalHighlightIndicator(false)
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.setDrawFilled(true)
        dataSet.setDrawValues(false)
        val radarData = RadarData(dataSet)
        effortChart.data = radarData
        effortChart.invalidate()
    }
}