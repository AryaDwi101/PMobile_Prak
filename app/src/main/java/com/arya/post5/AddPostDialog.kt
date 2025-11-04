package com.arya.post5 // Pastikan ini adalah nama package-mu

// Import-import yang diperlukan untuk Izin, Galeri, dan UI
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.arya.post5.databinding.DialogAddPostBinding // Pastikan nama package ini benar

class AddPostDialog : DialogFragment() {

    // --- INTERFACE #1: Untuk Menambah Post Baru ---
    interface OnPostAddedListener {
        // 'image' bisa berupa Uri (dari galeri) atau Int (default)
        fun onPostAdded(username: String, caption: String, image: Any)
    }
    private var addListener: OnPostAddedListener? = null
    fun setOnPostAddedListener(listener: OnPostAddedListener) {
        this.addListener = listener
    }

    // --- INTERFACE #2: Untuk Mengedit Post ---
    interface OnPostEditedListener {
        // Kita hanya perlu mengirim kembali teks yang diedit dan posisinya
        fun onPostEdited(position: Int, newUsername: String, newCaption: String)
    }
    private var editListener: OnPostEditedListener? = null
    fun setOnPostEditedListener(listener: OnPostEditedListener) {
        this.editListener = listener
    }

    // --- View Binding ---
    private var _binding: DialogAddPostBinding? = null
    private val binding get() = _binding!!

    // Variabel untuk menyimpan URI gambar yang dipilih dari galeri
    private var selectedImageUri: Uri? = null

    private var editPosition: Int = -1

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchGallery()
        } else {
            Toast.makeText(requireContext(), "Izin galeri ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data?.data != null) {
                selectedImageUri = data.data
                binding.ivPostPreview.setImageURI(selectedImageUri)
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchGallery()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(requireContext(), "Izin diperlukan untuk mengakses galeri", Toast.LENGTH_SHORT).show()
                permissionLauncher.launch(permission)
            }

            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddPostBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setDialogDimensions(0.90, 0.85)
    }

    private fun setDialogDimensions(widthPercentage: Double, heightPercentage: Double) {
        val widthPercent = widthPercentage.toFloat()
        val heightPercent = heightPercentage.toFloat()
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = requireContext().getSystemService(WindowManager::class.java)
            val bounds = windowManager.currentWindowMetrics.bounds
            val width = (bounds.width() * widthPercent).toInt()
            val height = (bounds.height() * heightPercent).toInt()
            dialog?.window?.setLayout(width, height)
        } else {
            @Suppress("DEPRECATION")
            val windowManager = requireContext().getSystemService(WindowManager::class.java)
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = (displayMetrics.widthPixels * widthPercent).toInt()
            val height = (displayMetrics.heightPixels * heightPercent).toInt()
            dialog?.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val username = it.getString(ARG_USERNAME)
            val caption = it.getString(ARG_CAPTION)
            editPosition = it.getInt(ARG_POSITION, -1) // Ambil posisi

            if (editPosition != -1) {
                binding.etUsername.setText(username)
                binding.etCaption.setText(caption)
                binding.tvDialogTitle.text = "Edit Postingan"
                binding.tvDialogSubtitle.text = "Perbarui Postinganmu"
                binding.btnAddImage.visibility = View.GONE
                binding.ivPostPreview.visibility = View.GONE
            }
        }

        binding.btnSavePost.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val caption = binding.etCaption.text.toString().trim()

            if (username.isBlank() || caption.isBlank()) {
                Toast.makeText(requireContext(), "Username dan Caption tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editPosition != -1) {
                editListener?.onPostEdited(editPosition, username, caption)
            } else {
                val imageToPost: Any = selectedImageUri ?: R.drawable.post_image_2
                addListener?.onPostAdded(username, caption, imageToPost)
            }

            dismiss()
        }

        binding.btnAddImage.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        const val TAG = "AddPostDialog"

        private const val ARG_USERNAME = "arg_username"
        private const val ARG_CAPTION = "arg_caption"
        private const val ARG_POSITION = "arg_position"

        fun newInstance(): AddPostDialog {
            return AddPostDialog()
        }

        fun newInstanceForEdit(post: Post, position: Int): AddPostDialog {
            val dialog = AddPostDialog()
            val args = Bundle().apply {
                putString(ARG_USERNAME, post.username)
                putString(ARG_CAPTION, post.caption)
                putInt(ARG_POSITION, position)
            }
            dialog.arguments = args
            return dialog
        }
    }
}