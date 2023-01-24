package com.example.sakhi.Fragments

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.example.sakhi.R
import com.example.sakhi.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingButton.setOnClickListener {
            popUpMenuSetting()
        }
    }

    private fun popUpMenuSetting() {
        val popMenu: PopupMenu =
            PopupMenu(context, binding.settingButton, Gravity.END, 0, R.style.MyPopupMenu)
        popMenu.menuInflater.inflate(R.menu.user_menu, popMenu.menu)
        popMenu.setOnMenuItemClickListener {
            item->
            when(item.itemId) {
                R.id.change_to_user_details->
                    navigateToUserDetails()
                R.id.logout->
                    logoutUser()
            }
            true
        }
        popMenu.show()
    }

    private fun navigateToUserDetails() {
        findNavController().navigate(R.id.action_homeFragment_to_userDetails)
    }

    private fun logoutUser() {
        Firebase.auth.signOut()
        findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
    }

}