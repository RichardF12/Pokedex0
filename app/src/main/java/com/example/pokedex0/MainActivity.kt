package com.example.pokedex0

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.example.pokedex0.utilities.NetworkUtils

import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    internal var mPokemonNumber: EditText
    internal var mSearchButton: Button
    internal var mResultText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()

        mSearchButton.setOnClickListener { view ->
            val pokemonNumber = mPokemonNumber.text.toString().trim { it <= ' ' }
            if (pokemonNumber.isEmpty()) {
                mResultText!!.setText(R.string.text_nothing_to_show)
            } else {

                FetchPokemonTask().execute(pokemonNumber)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    internal fun bindView() {
        mPokemonNumber = findViewById(R.id.et_pokemon_number)
        mSearchButton = findViewById(R.id.bt_search_pokemon)
    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID)

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null || pokemonInfo != "") {
                mResultText!!.text = pokemonInfo
            } else {
                mResultText!!.text = getString(R.string.text_not_found_message)
            }
        }
    }
}