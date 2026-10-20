package com.emobot.control
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale
class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
  private var tts: TextToSpeech? = null
  private val btRepo = BluetoothRepository()
  private var audioManager: AudioManager? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tts = TextToSpeech(this, this)
    audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
    setContent {
      MaterialTheme {
        var text by remember { mutableStateOf("") }
        var log by remember { mutableStateOf("EmoBot Listo - BT 5.0 PRO") }
        Column(Modifier.padding(16.dp)) {
          Text("EmoBot Control - BT 5.0 PRO + PAM8403")
          Spacer(Modifier.height(8.dp))
          Button(onClick = { 
            audioManager?.mode = AudioManager.MODE_NORMAL
            audioManager?.startBluetoothSco()
            audioManager?.isBluetoothA2dpOn = true
            log = "Audio redirigido a parlante 8 Ohm"
          }) { Text("Conectar Audio a Parlante") }
          Spacer(Modifier.height(8.dp))
          OutlinedTextField(value=text, onValueChange={text=it}, label={Text("Habla con Eilik")}, modifier=Modifier.fillMaxWidth())
          Button(onClick = {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            if(text.contains("feliz",true)) btRepo.sendCommand("FELIZ") else btRepo.sendCommand("HABLANDO")
            log = "Enviado: $text"
            text = ""
          }) { Text("Enviar") }
          Text(log)
        }
      }
    }
  }
  override fun onInit(status: Int) {
    if(status==TextToSpeech.SUCCESS){ tts?.language=Locale("es","ES"); tts?.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()) }
  }
}
