package com.example.u4_objednavka

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.u4_objednavka.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("cs-CZ")).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }

        fun basePrice(): Int = when (binding.rgBikes.checkedRadioButtonId) {
            binding.rbBike2.id -> BIKE_M20_PRICE
            binding.rbBike3.id -> BIKE_M30_PRICE
            else -> BIKE_M10_PRICE
        }

        fun totalPrice(): Int = basePrice() +
            (if (binding.cbFork.isChecked) FORK_PRICE else 0) +
            (if (binding.cbSaddle.isChecked) SADDLE_PRICE else 0) +
            (if (binding.cbHandleBar.isChecked) HANDLEBAR_PRICE else 0)

        fun updatePriceEstimate() {
            binding.tvPriceEstimate.text = getString(
                R.string.price_estimate,
                currencyFormat.format(totalPrice())
            )
        }

        binding.rgBikes.setOnCheckedChangeListener { _, checkedId ->
            val imageRes = when (checkedId) {
                binding.rbBike2.id -> R.drawable.oiz_m20_tr
                binding.rbBike3.id -> R.drawable.oiz_m30_tr
                else -> R.drawable.oiz_m10_tr
            }
            binding.ivBike.setImageResource(imageRes)
            updatePriceEstimate()
        }

        binding.cbFork.setOnCheckedChangeListener { _, _ -> updatePriceEstimate() }
        binding.cbSaddle.setOnCheckedChangeListener { _, _ -> updatePriceEstimate() }
        binding.cbHandleBar.setOnCheckedChangeListener { _, _ -> updatePriceEstimate() }

        binding.ivBike.setImageResource(R.drawable.oiz_m10_tr)
        updatePriceEstimate()

        binding.btnOrder.setOnClickListener {
            val bike = when (binding.rgBikes.checkedRadioButtonId) {
                binding.rbBike2.id -> binding.rbBike2
                binding.rbBike3.id -> binding.rbBike3
                else -> binding.rbBike1
            }
            val formattedPrice = currencyFormat.format(totalPrice())
            val extras = mutableListOf<String>()
            if (binding.cbFork.isChecked) extras.add(getString(R.string.better_fork))
            if (binding.cbSaddle.isChecked) extras.add(getString(R.string.better_saddle))
            if (binding.cbHandleBar.isChecked) extras.add(getString(R.string.better_handlebar))

            binding.tvOrder.text = if (extras.isEmpty()) {
                getString(R.string.order_summary, bike.text, formattedPrice)
            } else {
                getString(
                    R.string.order_summary_with_upgrades,
                    bike.text,
                    extras.joinToString("; "),
                    formattedPrice
                )
            }

            Toast.makeText(
                this,
                getString(R.string.order_confirmation, bike.text, formattedPrice),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private companion object {
        const val BIKE_M10_PRICE = 129_990
        const val BIKE_M20_PRICE = 104_990
        const val BIKE_M30_PRICE = 84_990
        const val FORK_PRICE = 12_000
        const val SADDLE_PRICE = 3_500
        const val HANDLEBAR_PRICE = 2_500
    }
}
