package com.example.ehcf.AyuSynk.NewUI

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ayudevice.ayusynksdk.AyuSynk
import com.ayudevice.ayusynksdk.ble.constants.DeviceConnectionState
import com.ayudevice.ayusynksdk.ble.constants.DeviceStrength
import com.ayudevice.ayusynksdk.ble.listener.AyuDeviceListener
import com.ayudevice.ayusynksdk.denoise.constants.FilterType
import com.ayudevice.ayusynksdk.onlineLiveStreaming.OnlineLiveStreamListener
import com.ayudevice.ayusynksdk.playback.listener.RecorderListener
import com.ayudevice.ayusynksdk.report.SoundFile
import com.ayudevice.ayusynksdk.report.listener.DiagnosisReportUpdateListener
import com.ayudevice.ayusynksdk.utils.logs.AyuLogsListener
import com.example.ehcf.CreateSlot.Adapter.AdapterFamilyListView
import com.example.ehcf.FamailyMember.Model.ModelFamilyList
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityRecorderBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Recorder : Fragment(), AyuDeviceListener, AdapterView.OnItemSelectedListener,
    View.OnClickListener, RecorderListener, OnlineLiveStreamListener, DiagnosisReportUpdateListener,
    AyuLogsListener, AdapterFamilyListView.CheckBox {
    private lateinit var binding: ActivityRecorderBinding
    lateinit var sessionManager: SessionManager
    var countN = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityRecorderBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiForRecording()
        sessionManager = SessionManager(requireContext())
        apiCallFamilyListNew()
        binding.cardHeart.setOnClickListener {
            NavHostFragment.findNavController(this@Recorder).navigate(R.id.RecordHeart)
        }

        binding.cardLungs.setOnClickListener {
            NavHostFragment.findNavController(this@Recorder).navigate(R.id.LungsSound)
        }
        binding.btnBle.setOnClickListener {
            NavHostFragment.findNavController(this@Recorder).navigate(R.id.LiveStream)
        }

        binding!!.imgBack.setOnClickListener {
            NavHostFragment.findNavController(this@Recorder).navigate(R.id.SelectModuleFragment)
        }
//        val fm = supportFragmentManager
//        val fragment = RecordHeartSound()
//        fm.beginTransaction().add(R.id.fragmentcontainer, fragment).commit()
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filters_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding!!.spinnerFilter.adapter = adapter
//        binding!!.spinnerFilter.onItemSelectedListener = this

        AyuSynk.getBleInstance().setAyuVisualizerView(binding!!.ayuVisualizerView)
        AyuSynk.getBleInstance().setRecordingTimeLimit(10)

        binding!!.imgBack.setOnClickListener {
            NavHostFragment.findNavController(this@Recorder).navigate(R.id.SelectModuleFragment)

        }

//        if (usb==1){
//            binding.appCompatTextView2.text="Bluetooth"
//        }else{
//            binding!!.appCompatTextView2.text="USB"
//
//        }


        // binding!!.notifications.movementMethod = ScrollingMovementMethod()
        AyuSynk.getBleInstance().setOnlineStreamerListener(this)
        AyuSynk.getBleInstance().setLogsListener(this)
        AyuSynk.getBleInstance().showLogs(true)
//        binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
//        binding!!.btnLogs.setOnClickListener(this)
//        binding!!.btnShareLogs.setOnClickListener(this)
//        binding!!.btnUpdateDevice.setOnClickListener(this)
    }


    private fun apiCallFamilyListNew() {
        AppProgressBar.showLoaderDialog(requireContext())

        ApiClient.apiService.getFamilyList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelFamilyList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelFamilyList>,
                    response: Response<ModelFamilyList>
                ) {

                    try {
                        // binding.rvSlotTiming.invalidate();
                        if (response.body()!!.status == 0) {
                            myToast(requireActivity(), "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvSlotTimingFamily.apply {
                                adapter =
                                    AdapterFamilyListView(
                                        requireActivity(),
                                        response.body()!!,
                                        this@Recorder
                                    )
                                AppProgressBar.hideLoaderDialog()
                            }
                        } else {
                            binding.rvSlotTimingFamily.apply {
                                //   adapter!!.notifyDataSetChanged();
                                //myToast(this@ShuduleTiming, response.body()!!.message)

                                adapter = AdapterFamilyListView(
                                    requireActivity(),
                                    response.body()!!,
                                    this@Recorder
                                )
                                binding.rvSlotTimingFamily.layoutManager =
                                    GridLayoutManager(context, 3)
                                //    binding.layoutFamilyMemeber.visibility=View.VISIBLE

                                AppProgressBar.hideLoaderDialog()
                            }

                        }
                    } catch (e: Exception) {
                        AppProgressBar.hideLoaderDialog()
                         e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                    }
                }


                override fun onFailure(call: Call<ModelFamilyList>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallFamilyListNew()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }


            })
    }


    override fun onResume() {
        super.onResume()
        AyuSynk.getBleInstance().setAyuDeviceListener(this)
        AyuSynk.getBleInstance().setRecorderListener(this)
        AyuSynk.getBleInstance().setDiagnosisReportUpdateListener(this)
        if (AyuSynk.getBleInstance().isDeviceConnected == DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceConnected()
        } else {
            onDeviceDisconnected()
        }
    }

    override fun onPause() {
        super.onPause()
        AyuSynk.getBleInstance().setAyuDeviceListener(null)
        AyuSynk.getBleInstance().setRecorderListener(null)
        AyuSynk.getBleInstance().setDiagnosisReportUpdateListener(null)
    }

    override fun deviceConnectionStrength(strength: DeviceStrength) {
    }

    override fun deviceConnectionState(state: DeviceConnectionState) {
        if (state != DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceDisconnected()
        } else {
            onDeviceConnected()
        }
    }

    override fun deviceBatteryUpdate(batteryUpdate: Int) {
        if (binding != null) {
            if (binding!!.customBatteryMeter.visibility != View.VISIBLE) binding!!.customBatteryMeter.visibility =
                View.VISIBLE
            binding!!.customBatteryMeter.setBatteryLevel(batteryUpdate)
        }
    }


    private fun onDeviceConnected() {
        deviceBatteryUpdate(AyuSynk.getBleInstance().currentBatteryLevel)
//        binding!!.btnUpdateDevice.isEnabled = true
        binding!!.customBatteryMeter.visibility = View.VISIBLE
        binding!!.imageConnected.visibility = View.VISIBLE
        binding!!.deviceState.setText(R.string.device_connected)

    }

    private fun onDeviceDisconnected() {

//        binding!!.btnUpdateDevice.isEnabled = false
        binding!!.customBatteryMeter.visibility = View.INVISIBLE
        binding!!.deviceState.setText(R.string.device_disconnected)

    }

    private fun setUiForRecording() {
        binding!!.ayuVisualizerView.clear() // Clear visualizer view before starting new recording

    }

    private fun setUIForPlaying() {
        binding!!.ayuVisualizerView.clear() // Clear visualizer view before starting new recording

    }

    private fun setUIForPlayingComplete() {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        var filterType: FilterType? = null
        if (position == 0) filterType = FilterType.NO_FILTER else if (position == 1) filterType =
            FilterType.HEART else if (position == 2) filterType = FilterType.LUNG
        if (filterType != null) AyuSynk.getBleInstance().changeFilter(filterType)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    /**
     * Stop should be called before starting streaming.
     */


    override fun onClick(v: View) {
        when (v.id) {


//            R.id.btn_logs -> if (binding!!.btnLogs.text.toString().lowercase(Locale.getDefault())
//                    .contains("enable")
//            ) {
//                binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
//                AyuSynk.getBleInstance().showLogs(true)
//            } else {
//                binding!!.btnLogs.text = getString(R.string.logsText, "Enable")
//                AyuSynk.getBleInstance().showLogs(false)
//            }
        }
    }


    override fun onOnlineStreamChangeListener(isStreaming: Boolean) {
//        if (isStreaming) {
//            binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Stop")
//            binding!!.btnShareUrl.isEnabled = true
//        } else {
//            binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Start")
//            binding!!.btnShareUrl.isEnabled = false
//        }
//        binding!!.progressBarStream.visibility = View.GONE
//        binding!!.btnOnlineStream.isEnabled = true
    }

    override fun onOnlineStreamError(error: String) {

        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Elapsed time of recording/streaming
     */
    override fun elapsedTime(min: Long, secs: Long) {


    }

    /**
     * Called when recording is completed
     */
    override fun recordingComplete(recordID: Int) {
        //Get recorder data.
        setUIForPlayingComplete()
    }

    /**
     * Called when playing is completed
     */
    override fun playingComplete() {
        setUIForPlayingComplete()
    }

    override fun reportGenerated(soundFile: SoundFile) {


    }

    override fun onReportGenerationError(error: String) {
        // binding!!.progressBarReport.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun logs(message: String) {
//        if (binding != null) {
//            binding!!.notifications.append(message)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        AyuSynk.getBleInstance().close()
    }

    override fun checkBox(id: Int) {
        if (id == 1) {
            binding.checkSelf.isChecked = true
            AdapterFamilyListView.memberID = ""
            //Do Whatever you want in isChecked
        } else {
            binding.checkSelf.isChecked = false
        }
    }
}