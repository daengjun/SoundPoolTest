package com.snf.soundpooltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 스캐너 소리 설정
     */
//    private SoundPool scanSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

    private SoundPool scanSoundPool;

    /**
     * 스캐너 음
     */
    int scanErrorBeep = -1;

    Button num1, num2, num3, num4;

    HashMap<Integer, Integer> soundMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        scanSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

        soundMap = new HashMap<>();
        soundMap.put(1, scanSoundPool.load(this, R.raw.scan_fail, 1));
        soundMap.put(2, scanSoundPool.load(this, R.raw.scan_success, 1));
        soundMap.put(3, scanSoundPool.load(this, R.raw.scan_fail, 1));
        soundMap.put(4, scanSoundPool.load(this, R.raw.scan_overlap, 1));

        num1 = findViewById(R.id.Beep1);
        num1.setOnClickListener(this);
        num2 = findViewById(R.id.Beep2);
        num2.setOnClickListener(this);
        num3 = findViewById(R.id.Beep3);
        num3.setOnClickListener(this);
        num4 = findViewById(R.id.Beep4);
        num4.setOnClickListener(this);

    }

    public void sound_Klogis(int i) {

        // 1. 스캔화면이 아닐때 --- 진동) 길게 한번 (예, 1초) --- 소리) SCAN_FAIL.wav
        // 2. 스캔성공 --- 진동) 짧게 한번 (예, 0.2초) --- 소리) SCAN_SUCCESS.wav
        // 3. 스캔실패 --- 진동) 길게 한번 (예, 1초) --- 소리) SCAN_FAIL.wav
        // 4. 중복스캔 --- 진동) 짧게 두번 (예, 0.1초+0.3초) --- 소리) SCAN_OVERLAP.wav

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (am == null) return;
        // int maxMusicVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // int errBeepVol = (int)(maxMusicVol * 0.6);
        // am.setStreamVolume(AudioManager.STREAM_MUSIC, errBeepVol,
        // AudioManager.FLAG_VIBRATE);

        switch (i) {
            case 1:// 1. 스캔화면이 아닐때

                if (am.getStreamVolume(AudioManager.STREAM_SYSTEM) == 0) {

                    Vibrator scanVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    scanVibrator.vibrate(1000);

                } else {

                    // scanErrorBeep = scanSoundPool.load(this, R.raw.scan_fail, 1);
                    scanSoundPool.play(soundMap.get(i), 1, 1, 0, 0, 1);
                }

                break;

            case 2:// 2. 스캔성공

                //불륨값이 0일때 진동
                if (am.getStreamVolume(AudioManager.STREAM_SYSTEM) == 0) {

                    Vibrator scanVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    scanVibrator.vibrate(200);

                } else {

                    // scanErrorBeep = scanSoundPool.load(this, R.raw.scan_success,
                    // 1);
                    scanSoundPool.play(soundMap.get(i), 1, 1, 0, 0, 1);
                }

                break;

            case 3:// 3. 스캔실패

                if (am.getStreamVolume(AudioManager.STREAM_SYSTEM) == 0) {

                    Vibrator scanVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    scanVibrator.vibrate(200);

                } else {

                    // scanErrorBeep = scanSoundPool.load(this, R.raw.scan_fail, 1);
                    scanSoundPool.play(soundMap.get(i), 1, 1, 0, 0, 1);
                }

                break;

            case 4:// 4. 중복스캔

                if (am.getStreamVolume(AudioManager.STREAM_SYSTEM) == 0) {

                    Vibrator scanVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] VibratorPattern = {50, 100, 100, 300};
                    scanVibrator.vibrate(VibratorPattern, -1);

                } else {

                    // scanErrorBeep = scanSoundPool.load(this, R.raw.scan_overlap,
                    // 1);
                    scanSoundPool.play(soundMap.get(i), 1, 1, 0, 0, 1);
                }

                break;

        }

        am.unloadSoundEffects();

// Unload Sound effects. This method can be called to free some memory when sound effects are disabled.
// 효과음을 언로드합니다. 사운드 효과가 비활성화된 경우 이 메서드를 호출하여 일부 메모리를 확보할 수 있습니다.

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Beep1:
                sound_Klogis(1);
                break;

            case R.id.Beep2:
                sound_Klogis(2);
                break;

            case R.id.Beep3:
                sound_Klogis(3);
                break;

            case R.id.Beep4:
                sound_Klogis(4);
                break;

        }
    }
}