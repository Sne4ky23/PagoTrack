package com.pagotrack.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.pagotrack.app.R
import com.pagotrack.app.data.AppDatabase
import com.pagotrack.app.data.ExpenseRepository
import com.pagotrack.app.parser.PaymentParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentNotificationListener : NotificationListenerService() {
    
    private lateinit var repository: ExpenseRepository
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private val paymentApps = setOf(
        "com.santander.app",
        "es.bbva.bbvacontigo",
        "com.caixabank.mobile",
        "com.revolut.revolutapp",
        "de.number26.android",
        "com.wise.android",
        "es.bizum.cajasur",
        "com.google.android.gms",
        "com.apple.mobility.icloud",
        "com.paypal.android.app",
        "com.instagram.android",
        "com.whatsapp"
    )
    
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(applicationContext)
        repository = ExpenseRepository(db.expenseDao())
        createNotificationChannel()
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        
        if (!isPaymentApp(sbn.packageName)) return
        
        val notification = sbn.notification
        val title = notification.extras.getString("android.title", "")
        val text = notification.extras.getCharSequence("android.text", "").toString()
        
        val parsed = PaymentParser.parseNotification(title, text) ?: return
        
        val expense = PaymentParser.toExpense(parsed)
        
        scope.launch {
            val id = repository.insertExpense(expense)
            showConfirmationNotification(id, parsed.merchant, parsed.amount, parsed.currency)
        }
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
    
    private fun isPaymentApp(packageName: String): Boolean {
        return paymentApps.any { packageName.contains(it) } ||
                packageName.contains("bank") ||
                packageName.contains("payment") ||
                packageName.contains("wallet") ||
                packageName.contains("bizum")
    }
    
    private fun showConfirmationNotification(
        expenseId: Long,
        merchant: String,
        amount: Double,
        currency: String
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val confirmIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "com.pagotrack.app.action.EXPENSE_CONFIRM"
            putExtra("EXPENSE_ID", expenseId)
        }
        val confirmPendingIntent = PendingIntent.getBroadcast(
            this,
            expenseId.toInt(),
            confirmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val discardIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "com.pagotrack.app.action.EXPENSE_DISCARD"
            putExtra("EXPENSE_ID", expenseId)
        }
        val discardPendingIntent = PendingIntent.getBroadcast(
            this,
            (expenseId + 10000).toInt(),
            discardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("$merchant - $amount $currency")
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_menu_save, getString(R.string.action_confirm), confirmPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.action_discard), discardPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        notificationManager.notify(expenseId.toInt(), notification)
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.channel_description)
        }
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    
    companion object {
        const val CHANNEL_ID = "pagotrack_channel"
    }
}
