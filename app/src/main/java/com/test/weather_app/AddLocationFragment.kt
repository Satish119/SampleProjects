package com.test.weather_app


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.weather_app.repository.Repository
import com.test.weather_app.viewmodels.MainViewModel
import com.test.weather_app.viewmodels.MapViewModel
import com.test.weather_app.views.CustomMarkerInfoWindowView
import kotlinx.android.synthetic.main.fragment_add_location.*
import kotlinx.android.synthetic.main.fragment_add_location.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddLocationFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var map: GoogleMap

    private lateinit var mapViewModel: MapViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_location, container, false)
        view.mapView.onCreate(savedInstanceState)
        view.mapView.getMapAsync(this)

        val repository = Repository()
        viewModelFactory = MainViewModelFactory(repository, requireContext())
        mapViewModel =
            ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)

        observeViewModel()

        return view
    }


    private fun observeViewModel() {
        mapViewModel.data.observe(viewLifecycleOwner, Observer { data ->
            data?.let {

                loading?.visibility = View.GONE
                error?.visibility = View.GONE
                findNavController().navigate(R.id.action_addLocationFragment_to_homeFragment)

            }
        })
        mapViewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            errorMsg?.let {
                if (it.isNotEmpty()) {
                    loading.visibility = View.GONE
                    error.visibility = View.VISIBLE
                }

            }
        })
        mapViewModel.progress.observe(viewLifecycleOwner, Observer { progress ->
            progress?.let {
                if (it) {
                    loading?.visibility = View.VISIBLE
                    error?.visibility = View.GONE
                } else {
                    loading?.visibility = View.GONE
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddLocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddLocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        findLocation()
        map.apply {
            setInfoWindowAdapter(CustomMarkerInfoWindowView(context, mapViewModel))
            setOnMapClickListener { latLng ->
                latLng?.let {
                    clear()
                    animateCamera(CameraUpdateFactory.newLatLng(it))
                    addMarker(MarkerOptions().position(it)).showInfoWindow()

                    mapViewModel.addWeatherLocation(it.latitude, it.longitude)
                }
            }
        }

    }

//    fun getCurrentLatLngInfo(){
//        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//
//        // Use the builder to create a FindCurrentPlaceRequest.
//        val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//        // Get the likely places - that is, the businesses and other points of interest that
//        // are the best match for the device's current location.
//        val placeResult = placesClient.findCurrentPlace(request)
//    }


    private fun findLocation() {
        if (ContextCompat.checkSelfPermission(
                context ?: return,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.activity ?: return, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_LOCATION_PERMISSION_REQUEST
            )
        } else {
            val sydney = LatLng((-34).toDouble(), 151.0)
            map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FINE_LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                    findLocation()
                }
            }
        }
    }


    private val FINE_LOCATION_PERMISSION_REQUEST = 1
}
