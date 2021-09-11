package com.los3molineros.michelinstarrestaurants.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.los3molineros.michelinstarrestaurants.R
import com.los3molineros.michelinstarrestaurants.common.AppConstants
import com.los3molineros.michelinstarrestaurants.common.CommonFunctions

class AppRatingDialog(private val context: Context, val optionClick: (Int) -> Unit): AppCompatActivity() {
    private val dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_rating_app)

        val mBtnYes: Button = dialog.findViewById(R.id.btnYes)
        val mBtnNotNow = dialog.findViewById<Button>(R.id.btnNotNow)
        val mBtnNeverMore = dialog.findViewById<Button>(R.id.btnNeverMore)

        val mTxtTittle = dialog.findViewById<TextView>(R.id.txtTittle)
        val mTxtDescription = dialog.findViewById<TextView>(R.id.txtDescription)

        val mIvExit = dialog.findViewById<ImageView>(R.id.ivExit)


        mTxtTittle.typeface = CommonFunctions.returnTypefaceKimbalt(context)
        mTxtDescription.typeface = CommonFunctions.returnTypefaceKingthings(context)
        mBtnNeverMore.typeface = CommonFunctions.returnTypefaceKingthings(context)
        mBtnNotNow.typeface = CommonFunctions.returnTypefaceKingthings(context)
        mBtnYes.typeface = CommonFunctions.returnTypefaceKingthings(context)

        mBtnYes.setOnClickListener {
            optionClick(AppConstants.PRESSED_YES)
            dialog.cancel()
        }

        mBtnNotNow.setOnClickListener {
            optionClick(AppConstants.PRESSED_LATER)
            dialog.cancel()
        }

        mBtnNeverMore.setOnClickListener {
            optionClick(AppConstants.PRESSED_NO)
            dialog.cancel()
        }

        mIvExit.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }
}