package com.benjaminearley.stockcost.ui.productAdd

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.benjaminearley.stockcost.databinding.DialogProductAddBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProductAddDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<ProductAddViewModel>()

    private var _binding: DialogProductAddBinding? = null
    private val binding get() = _binding!!

    init {
        lifecycleScope.launchWhenResumed { viewModel.dismissDialog.collect { dismiss() } }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProductAddBinding.inflate(inflater, container, false)

        with(binding) {

            viewModel.isLoading.observe(viewLifecycleOwner) {
                TransitionManager.beginDelayedTransition(root, Fade())
                error.isInvisible = it
                inputLayout.isInvisible = it
                loading.isVisible = it
            }

            viewModel.error.observe(viewLifecycleOwner) { message ->
                message
                    ?.let { error.text = error.context.getString(it) }
                    ?: let { error.text = null }
            }

            add.setOnClickListener {
                viewModel.add(securityId.text?.toString())
            }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}