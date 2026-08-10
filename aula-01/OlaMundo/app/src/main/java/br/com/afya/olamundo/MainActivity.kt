package br.com.afya.olamundo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val TAG = "OlaMundo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(TAG, "onCreate: Activity foi criada e layout inflado.")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: Activity tornou-se visível ao usuário.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: Activity está pronta e em primeiro plano.")
    }

    override fun onPause() {
        super.onPause()
        Log.w(TAG, "onPause: Perdeu o foco. Pausando operações secundárias.")
    }

    override fun onStop() {
        super.onStop()
        Log.w(TAG, "onStop: Não está mais visível na tela.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Usuário retornou à Activity.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: Activity sendo destruída e memória liberada.")
    }
}