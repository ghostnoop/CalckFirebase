package com.testfirebaseapp.siminfo

import android.content.Context
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager


fun acces_to_sim(ctx: Context): MutableList<String> {
    val listInfo = mutableListOf<String>()
    val manager =
        ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    manager.getActiveModemCount()
    manager.networkCountryIso
    manager.networkOperatorName

    listInfo.add(manager.simOperatorName)
    listInfo.add(manager.simCountryIso)
    listInfo.add(manager.networkOperatorName)
    listInfo.add(manager.networkCountryIso)
    return listInfo

}