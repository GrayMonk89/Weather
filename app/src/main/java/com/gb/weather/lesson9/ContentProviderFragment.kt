package com.gb.weather.lesson9

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gb.weather.R
import com.gb.weather.databinding.FragmentContentProviderBinding
import com.gb.weather.utils.REQUEST_CODE

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explainToAFool()
        } else {
            mRequestPermission()
        }
    }

    private fun explainToAFool() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.access_to_contacts)
            .setMessage(R.string.explain)
            .setPositiveButton(R.string.access_granted) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(R.string.denial) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    explainToAFool()
                }
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        ) // или DESC
        cursor?.let {
            for (i in 0 until it.count){
                if(cursor.moveToPosition(i)){
                    val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name:String = cursor.getString(columnNameIndex)
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text= name
                    })
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
    }
}
