package com.aribhatt.kotlinlearner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.aribhatt.kotlinlearner.databinding.ActivityMainBinding
import com.aribhatt.kotlinlearner.ui.activities.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val bundle = msg.data
            val message = bundle?.getString("MESSAGE_KEY")
            log(message ?: "Nothing was sent")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //findViewById<FloatingActionButton>(R.id.fab)
        binding.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            val intent = Intent(this, DetailActivity::class.java);
            intent.setAction(Intent.ACTION_VIEW)
            intent.putExtra("name", "aribhatt")
            startActivity(intent)

        }
    }
    private fun runBackgroundCode() {
        thread (start = true){
            val bundle = Bundle()
            for (i in 1..10) {
                bundle.putString("MESSAGE_KEY", "Looping $i")
                Message().also {
                    it.data = bundle
                    handler.sendMessage(it)
                }
                Thread.sleep(1000)

            }
            bundle.putString("MESSAGE_KEY", "All done!")
            Message().also {
                it.data = bundle
                handler.sendMessage(it)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            val result = fetchSomething()
            Log.i("MainActivity", result)
        }
    }
    private fun log(str: String) {

    }

    private suspend fun fetchSomething() : String {
        delay(2000)
        return "Fake api response"
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(binding.root, "Replace with your own action", Snackbar.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        //super.onOptionsItemSelected(item)
    }*/
}