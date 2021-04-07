package com.test.weather_app.views.city

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.test.weather_app.MainViewModelFactory
import com.test.weather_app.R
import com.test.weather_app.repository.Repository
import com.test.weather_app.utils.Constants
import com.test.weather_app.utils.Strings
import com.test.weather_app.utils.Utils
import com.test.weather_app.viewmodels.CityWeatherViewModel
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_city.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class CityFragment : Fragment() {

    lateinit var forecastViewModel: CityWeatherViewModel
    val cityListAdapter : CityForecastAdapter by lazy {
        CityForecastAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_city, container, false)

        val viewModelFactory = MainViewModelFactory(Repository(), requireContext())
        forecastViewModel  =
            ViewModelProvider(this, viewModelFactory).get(CityWeatherViewModel::class.java)

        observeViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.forecastList.apply {
            adapter = cityListAdapter
            layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)

        }

        arguments?.let {
            val safeArgs =
                CityFragmentArgs.fromBundle(it)
            forecastViewModel.getWeatherForecast(safeArgs.cityName)
            temp?.text = "${safeArgs.temp}${Constants.CELSIUS}"
        }

    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        forecastViewModel.weatherForecast.observe(this, Observer { forecastData ->
            forecastData?.let {
                progress?.visibility =View.GONE
                forecastData.list?.let { it1 -> cityListAdapter.setData(it1) }
                forecastData.list?.get(0)?.let {
                    var sunset = false
                    it.sys?.sunset?.let {
                        sunset = (((Calendar.getInstance().timeInMillis - it)/1000)/60)/60 > 0
                    }
//                    temp.text = "${it.main?.temp.toString()}${Constants.CELSIUS}"
                    weatherCityIcon.setImageResource(
                        Utils.getIconName(
                            it.weather?.get(
                                0
                            )?.main, sunset
                        )
                    )
                    humidity.text = "${Strings.HUMIDITY} : ${it.main?.humidity.toString()}%"
                    cityWithDay.text = "${Strings.FEELS_LIKE} : ${it.main?.feels_like}${Constants.CELSIUS}"
                }

                location.text = forecastData.city?.name


            }
        })
        forecastViewModel.error.observe(requireActivity(), Observer { error ->
            error?.let {


            }
        })
        forecastViewModel.loading.observe(requireActivity(), Observer { loading ->
            loading?.let {
                if(it)
                progress?.visibility =View.VISIBLE
                else {
                    progress?.visibility = View.GONE

                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

}
