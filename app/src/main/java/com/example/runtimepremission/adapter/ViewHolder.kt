package com.example.runtimepremission.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.runtimepremission.R
import com.example.runtimepremission.model.CartItems
import java.lang.String
import kotlin.Int

class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {

    var tvCount: TextView? = null;
    var tvIncrease: TextView? = null;
    var tvDecrease: TextView? = null;

    fun bindView(cartItems: CartItems) {

        tvCount!!.setText(""+cartItems.quantity)
        setOnClickLstener()
    }

    private fun setOnClickLstener() {
        tvIncrease!!.setOnClickListener {
            var count: Int = String.valueOf(
                tvCount!!.getText()
            ).toInt()
            count++
            tvCount!!.setText("" + count)
        }
        tvDecrease!!.setOnClickListener {

            var count: Int = String.valueOf(tvCount!!.getText()).toInt()
            if (count == 1) {
                tvCount!!.setText("1")
            } else {
                count -= 1
                tvCount!!.setText("" + count)
            }

        }
    }

    init {
        tvCount = viewHolder.findViewById<TextView>(R.id.tvCount)
        tvDecrease = viewHolder.findViewById<TextView>(R.id.tvDecrease)
        tvIncrease = viewHolder.findViewById<TextView>(R.id.tvIncrease)

    }
}