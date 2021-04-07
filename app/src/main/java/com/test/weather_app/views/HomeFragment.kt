package com.test.weather_app.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.weather_app.MainViewModelFactory
import com.test.weather_app.R
import com.test.weather_app.repository.Repository
import com.test.weather_app.utils.Strings
import com.test.weather_app.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_location.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class HomeFragment : Fragment() {

    private val listAdapter: ListAdapter by lazy {
        ListAdapter()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        getWeatherData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.swipeRefresh.setOnRefreshListener {
            viewModel.getWeather()
        }
        view.listView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        val itemTouchHelper = ItemTouchHelper(helper())
        itemTouchHelper.attachToRecyclerView(view.listView)
    }

    private fun helper():ItemTouchHelper.Callback{

        return object: ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // Specify the directions of movement
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(-1, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Notify your adapter that an item is moved from x position to y position
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                // true: if you want to start dragging on long press
                // false: if you want to handle it yourself
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteLocation(viewHolder.adapterPosition)
            }


            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
            }

        }

    }

    private fun getWeatherData() {
        observeViewModel()
        viewModel.getWeather()

    }

    private fun observeViewModel() {
        viewModel.weatherInfo.observe(requireActivity(), Observer { result ->
            result?.let {
                if(it.isEmpty()) {
                    view?.listView?.visibility = View.GONE
                    view?.error?.visibility = View.VISIBLE
                    view?.error?.text = Strings.NO_LOCATIONS

                }
                else {
                    view?.listView?.visibility = View.VISIBLE
                    view?.error?.visibility = View.GONE
                }
                listAdapter.setData(it)
            }
        })

        viewModel.error.observe(requireActivity(),Observer{
            errorMsg -> errorMsg?.let {
            view?.listView?.visibility = View.GONE
            view?.error?.visibility = View.VISIBLE
            view?.error?.text =it
        }
        })
        viewModel.progress.observe(requireActivity(), Observer { progress ->
            progress?.let {
                if (it) {
                    view?.swipeRefresh?.isRefreshing = true
                    view?.listView?.visibility = View.GONE
                } else {
                    view?.swipeRefresh?.isRefreshing = false
                    view?.listView?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_location, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_loc -> addLocation()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    private fun addLocation() {
        findNavController().navigate(R.id.addLocationFragment)
    }


    override fun onDetach() {
        super.onDetach()
    }
}
