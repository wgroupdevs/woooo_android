package eu.siacs.conversations.ui.meeting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import eu.siacs.conversations.http.model.meeting.HistoryMeetingModel
import eu.siacs.conversations.http.model.meeting.MeetingAPIRes
import eu.siacs.conversations.http.model.meeting.ScheduleMeetingModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@ViewModelScoped
class MeetingViewModel : ViewModel(), WooAPIService.OnScheduleMeetingApiResult,
    WooAPIService.OnGetScheduledMeetingsApiResult, WooAPIService.OnGetMeetingHistoryApiResult {

    private val mutableScheduledMeetingList: MutableList<ScheduleMeetingModel> = mutableListOf()

    private val scheduledMeetings = MutableLiveData<MutableList<ScheduleMeetingModel>>()
    private val meetingHistory = MutableLiveData<ArrayList<HistoryMeetingModel>>()

    private val createScheduleMeeting = MutableLiveData<ScheduleMeetingModel>()

    private var wooAPIService: WooAPIService? = null

    private val TAG = "ScheduleMeeting_TAG"


    init {
        wooAPIService = wooAPIService ?: WooAPIService.getInstance()
    }


    fun scheduleNewMeeting(scheduleMeetingModel: ScheduleMeetingModel) {
        Completable.fromAction {
            wooAPIService?.scheduleMeeting(scheduleMeetingModel, this)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .isDisposed

    }

    fun getScheduledNewMeetings(accountUUID: String) {
        Completable.fromAction {
            wooAPIService?.getScheduledMeetings(accountUUID, this)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .isDisposed

    }

    fun getMeetingHistory(accountUUID: String) {
        Completable.fromAction {
            wooAPIService?.getMeetingsHistory(accountUUID, this)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .isDisposed

    }

    // Expose LiveData to observe in the UI
    fun getScheduledMeetings(): LiveData<MutableList<ScheduleMeetingModel>> = scheduledMeetings
    fun onCreateScheduleMeeting(): LiveData<ScheduleMeetingModel> = createScheduleMeeting
    fun onMeetingHistory(): LiveData<ArrayList<HistoryMeetingModel>> = meetingHistory

    fun removeMeeting(meeting:ScheduleMeetingModel) {

        mutableScheduledMeetingList.remove(meeting)
        scheduledMeetings.postValue(mutableScheduledMeetingList)

    }

    override fun <T : Any?> OnScheduleMeetingResultFound(result: T) {
        when (result) {
            is MeetingAPIRes<*> -> {
                val res = result as MeetingAPIRes<*>
                res.Data.let {
                    if (it != null) {
                        it as ArrayList<ScheduleMeetingModel>
                        createScheduleMeeting.postValue(it.first())
                        mutableScheduledMeetingList.add(it.first())
                        scheduledMeetings.postValue(mutableScheduledMeetingList)
                    }
                }
            }

            is BaseModelAPIResponse -> {
                Log.d(TAG, "OnScheduleMeetingResultFound AN ERROR OCCURED ")
            }
        }

    }

    override fun <T : Any?> OnGetScheduledMeetingsResultFound(result: T) {
        when (result) {
            is MeetingAPIRes<*> -> {
                val res = result as MeetingAPIRes<*>;
                res.Data.let {
                    if (it != null) {
                        it as ArrayList<ScheduleMeetingModel>
                        mutableScheduledMeetingList.clear()
                        mutableScheduledMeetingList.addAll(it)
                        scheduledMeetings.postValue(mutableScheduledMeetingList)
                    }

                }
            }

            is BaseModelAPIResponse -> {
                Log.d(TAG, "OnScheduleMeetingResultFound AN ERROR OCCURED ")
            }
        }
    }

    override fun <T : Any?> OnGetMeetingHistoryResultFound(result: T) {

        when (result) {
            is MeetingAPIRes<*> -> {
                val res = result as MeetingAPIRes<*>;
                res.Data.let {
                    if (it != null) {
                        it as ArrayList<HistoryMeetingModel>
                        meetingHistory.postValue(it)
                    }

                }
            }

            is BaseModelAPIResponse -> {
                Log.d(TAG, "OnGetMeetingHistoryResultFound AN ERROR OCCURED ")
            }
        }
    }


}