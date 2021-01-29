package com.sample.android.qapital.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.qapital.data.SavingsGoal
import com.sample.android.qapital.data.source.GoalsRepository
import com.sample.android.qapital.util.Resource
import com.sample.android.qapital.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class SavingsGoalsViewModel(
    private val repository: GoalsRepository,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel<List<SavingsGoal>>(schedulerProvider) {

    override val requestObservable: Observable<List<SavingsGoal>> = repository.getSavingsGoals()

    init {
        loadSavingsGoals(false)
    }

    fun loadSavingsGoals(isRefreshing: Boolean) {
        goalLiveData.value = Resource.Loading(isRefreshing)
        if (isRefreshing) {
            repository.refreshGoals()
        }
        super.sendRequest()
    }

    class Factory @Inject constructor(
        private val repository: GoalsRepository,
        private val schedulerProvider: BaseSchedulerProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SavingsGoalsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SavingsGoalsViewModel(repository, schedulerProvider) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}