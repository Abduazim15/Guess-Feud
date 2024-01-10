package com.skipissue.guess.fragments

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skipissue.guess.R
import com.skipissue.guess.adapter.VariantAdapter
import com.skipissue.guess.databinding.MainFragmentBinding
import com.skipissue.guess.model.QuestionEntity
import com.skipissue.guess.model.VariantResponse
import com.skipissue.guess.viewmodels.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val binding: MainFragmentBinding by viewBinding()
    private val viewModel: GameViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val questions = listOf(
            QuestionEntity("easy", "Why does cats"),
            QuestionEntity("easy", "how to grow"),
            QuestionEntity("easy", "chocolate flavored"),
            QuestionEntity("easy", "what is the best kind of"),
            QuestionEntity("easy", "how to mix a"),
            QuestionEntity("easy", "does anyone else hate"),
            QuestionEntity("easy", "jessica"),
            QuestionEntity("easy", "wrestling is"),
            QuestionEntity("easy", "has a dog ever been"),
            QuestionEntity("easy", "why are dogs"),
            QuestionEntity("easy", "is tom cruise in"),
            QuestionEntity("easy", "why did they build the"),
            QuestionEntity("easy", "can you drink expired"),
            )
        val question = questions.random()
        val adapter by lazy { VariantAdapter(question) }
        binding.recycler.adapter = adapter
        binding.inputLay.prefixText = question.question
        viewModel.getAutoCompletes(question.question)
        lifecycleScope.launch {
            viewModel.stateSuccess.collect { data ->
                adapter.submitList(data)
            }
        }
        binding.button.setOnClickListener {
            val query = binding.answerEdt.text?.toString()
            if (!query.isNullOrBlank()) {
                val index = isContain(query, adapter)
                if (index != -1) {
                    (binding.recycler.findViewHolderForAdapterPosition(index) as VariantAdapter.HistoryViewHolder).startAnim()
                } else {
                    if (vibrator.hasVibrator()) {
                        val duration = 500L
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            vibrator.vibrate(duration)
                        }
                    }}
            }
        }
    }

    fun isContain(query: String, adapter: VariantAdapter): Int {
        for (i in 0..<adapter.currentList.size) {
            if (adapter.currentList[i].phrase.contains(query))
                return i
        }
        return -1
    }
}