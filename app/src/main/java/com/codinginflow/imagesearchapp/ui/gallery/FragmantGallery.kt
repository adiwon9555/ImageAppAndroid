package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmantGalleryBinding
import com.codinginflow.imagesearchapp.databinding.ItemUnsplashPhotoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmantGallery : Fragment(R.layout.fragmant_gallery), UnsplashPhotoAdaptar.ItemClickListener {
    private val viewModel: GalleryViewModel by viewModels()

    //To make binding global
    // adding ? after a type is safe call operator which means  the variable can be nullable, other wise kotlin does not allow
    var _binding: FragmantGalleryBinding? = null

    //To avoid writing the safe call operator always inside the class we can do null check here and assign it to other operator
    // !! is double bang operator which means if _binding is null then throw null pointer exception or else return the not nullable type
    val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val unsplashPhotoAdaptar = UnsplashPhotoAdaptar(this)
        _binding = FragmantGalleryBinding.bind(view)
        binding.apply {
            recyclerView.adapter = unsplashPhotoAdaptar.withLoadStateHeaderAndFooter(
                header = UnsplashLoadStateFooterAdaptar { unsplashPhotoAdaptar.retry() },
                footer = UnsplashLoadStateFooterAdaptar { unsplashPhotoAdaptar.retry() }
            )
            recyclerView.setHasFixedSize(true)
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            unsplashPhotoAdaptar.submitData(viewLifecycleOwner.lifecycle, it)
        }


        unsplashPhotoAdaptar.addLoadStateListener { combinedLoadStates ->
            //combinedLoadStates contains loadState of diff scenario like when the data is refreshed or when appended etc.
            binding.apply {
                recyclerView.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                //for smoothness
                recyclerView.itemAnimator = null
                progressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                textViewError.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                retryButton.isVisible = combinedLoadStates.source.refresh is LoadState.Error

                //For Empty
                if(combinedLoadStates.source.refresh is LoadState.NotLoading
                    && combinedLoadStates.append.endOfPaginationReached
                    && unsplashPhotoAdaptar.itemCount < 1){
                    textViewEmpty.isVisible = true
                    recyclerView.isVisible = false
                }else{
                    textViewEmpty.isVisible = false
                }

                retryButton.setOnClickListener{
                    unsplashPhotoAdaptar.retry()
                }
            }

        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_menu,menu)
        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView = searchIcon.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onItemClickListener(unsplashPhoto: UnsplashPhoto) {
        val action = FragmantGalleryDirections.actionFragmantGalleryToFragmantDetails(unsplashPhoto)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //To make binding object only available within viewCreated and viewDestroyed Scope
        _binding = null
    }


}