package com.example.musicplayer1


import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView



class MainActivity : AppCompatActivity() {



    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekbar: SeekBar = findViewById(R.id.seekbar)
        val playButton: Button = findViewById(R.id.play)
        val mediaPlayer = MediaPlayer.create(this,R.raw.led_zeppelin_black_dog)
        val name: TextView = findViewById(R.id.textView)
        val photo: ImageView = findViewById(R.id.imageView)
        val playedTime: TextView = findViewById(R.id.playedTime)
        val remainingTime: TextView = findViewById(R.id.leftTime)
        val preButton5: Button = findViewById(R.id.previous5)
        val nxtButton5: Button = findViewById(R.id.next5)
        val preButton10: Button = findViewById(R.id.previous10)
        val nxtButton10: Button = findViewById(R.id.next10)
        val volumeSeekBar: SeekBar = findViewById(R.id.seekBar)
        val lowVolumeImage: ImageView = findViewById(R.id.imageView2)
        val highVolumeImage: ImageView = findViewById(R.id.imageView3)




        mediaPlayer.start()
        playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24)




        seekbar.progress = 0
        seekbar.max = mediaPlayer.duration

        photo.setImageDrawable(null)
        photo.setBackgroundResource(R.drawable.led_zeppelin_black_dog)

        name.text = "led_zeppelin_black_dog"


        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.max = maxVolume

        val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.progress = currVolume



        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p1,0)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        preButton5.setOnClickListener {
                mediaPlayer.seekTo(mediaPlayer.currentPosition - 5000)
        }
        nxtButton5.setOnClickListener {
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 5000)
        }
        preButton10.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
        }
        nxtButton10.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
        }


        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying){
                mediaPlayer.start()
                playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24)
            }
            else{
                mediaPlayer.pause()
                playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24)
            }
        }


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2){
                    mediaPlayer.seekTo(p1)

                    var p1S = p1 / 1000
                    var p1Min = (p1S / 60)
                    var p1Sec = (p1S % 60)

                    playedTime.text = "${p1Min}:${p1Sec}"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        runnable = Runnable {

            seekbar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable,0)


            volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            if (volumeSeekBar.progress > volumeSeekBar.max / 2){
                highVolumeImage.setBackgroundResource(R.drawable.ic_baseline_volume_up_24)
            }
            else if (volumeSeekBar.progress <= volumeSeekBar.max / 2 && volumeSeekBar.progress > 0){
                highVolumeImage.setImageDrawable(null)
                highVolumeImage.setBackgroundResource(R.drawable.ic_baseline_volume_down_24)
            }
            else if (volumeSeekBar.progress == 0){
                highVolumeImage.setBackgroundResource(R.drawable.ic_baseline_volume_off_24)
            }


            val remTimeSec = ((mediaPlayer.duration / 1000) - (mediaPlayer.currentPosition / 1000))

            playedTime.text = (((mediaPlayer.currentPosition / 1000) - ((mediaPlayer.currentPosition / 1000) % 60)) / 60).toString() + ":" + ((mediaPlayer.currentPosition / 1000) % 60).toString()
            remainingTime.text = "-" + ((remTimeSec - (remTimeSec % 60)) / 60).toString() + ":" + (((mediaPlayer.duration / 1000) - (mediaPlayer.currentPosition / 1000)) % 60).toString()

        }
        handler.postDelayed(runnable,0)

        mediaPlayer.setOnCompletionListener {
//            mediaPlayer.pause()
//            playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24)
            mediaPlayer.start()
            playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24)

        }
    }
}