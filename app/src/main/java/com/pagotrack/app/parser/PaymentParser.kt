package com.pagotrack.app.parser

import com.pagotrack.app.data.Expense
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ParsedPayment(
    val amount: Double,
    val currency: String,
    val merchant: String
)

object PaymentParser {
    
    // Regex patterns for different currencies and amounts
    private val eurPattern = Regex("""(\d+[.,]\d{2}|\d+)\s*€|EUR|€\s*(\d+[.,]\d{2}|\d+)""", RegexOption.IGNORE_CASE)
    private val usdPattern = Regex("""(\$|USD)\s*(\d+[.,]\d{2}|\d+)|(\d+[.,]\d{2}|\d+)\s*(\$|USD)""", RegexOption.IGNORE_CASE)
    private val gbpPattern = Regex("""(£|GBP)\s*(\d+[.,]\d{2}|\d+)|(\d+[.,]\d{2}|\d+)\s*(£|GBP)""", RegexOption.IGNORE_CASE)
    
    // List of common merchants and payment apps
    private val merchants = listOf(
        "Santander", "BBVA", "CaixaBank", "ING", "Sabadell",
        "Revolut", "N26", "Wise", "TransferWise",
        "Bizum", "PayPal", "Google Pay", "Google Wallet", "Apple Pay",
        "Mercadona", "Carrefour", "Alcampo", "Lidl", "Día",
        "Zara", "H&M", "Inditex", "Nike", "Adidas",
        "Amazon", "Ebay", "AliExpress", "MediaMarkt",
        "McDonald's", "Burger King", "Starbucks", "Deliveroo", "Uber Eats",
        "Spotify", "Netflix", "Disney+", "HBO Max", "Prime Video",
        "Gasolinera", "CEPSA", "Repsol", "Shell", "BP",
        "Restaurante", "Hotel", "Hostel", "Airbnb",
        "Correos", "DHL", "UPS", "FedEx",
        "Teléfónica", "Orange", "Vodafone", "Jazztel"
    )
    
    fun parseNotification(title: String, text: String): ParsedPayment? {
        val fullText = "$title $text"
        
        // Extract amount and currency
        val (amount, currency) = extractAmountAndCurrency(fullText) ?: return null
        
        // Extract merchant
        val merchant = extractMerchant(fullText)
        
        return ParsedPayment(
            amount = amount,
            currency = currency,
            merchant = merchant
        )
    }
    
    private fun extractAmountAndCurrency(text: String): Pair<Double, String>? {
        // Try EUR first
        eurPattern.find(text)?.let {
            val amountStr = extractNumber(it.value)
            val amount = parseAmount(amountStr)
            if (amount > 0) return Pair(amount, "EUR")
        }
        
        // Try USD
        usdPattern.find(text)?.let {
            val amountStr = extractNumber(it.value)
            val amount = parseAmount(amountStr)
            if (amount > 0) return Pair(amount, "USD")
        }
        
        // Try GBP
        gbpPattern.find(text)?.let {
            val amountStr = extractNumber(it.value)
            val amount = parseAmount(amountStr)
            if (amount > 0) return Pair(amount, "GBP")
        }
        
        // Try generic number pattern (assume EUR)
        val genericPattern = Regex("""(\d+[.,]\d{2}|\d{2,})""")
        genericPattern.find(text)?.let {
            val amountStr = it.value
            val amount = parseAmount(amountStr)
            if (amount > 0 && amount < 100000) return Pair(amount, "EUR")
        }
        
        return null
    }
    
    private fun extractNumber(text: String): String {
        return Regex("""(\d+[.,]\d{2}|\d+)""").find(text)?.value ?: ""
    }
    
    private fun parseAmount(amountStr: String): Double {
        if (amountStr.isEmpty()) return 0.0
        val normalized = amountStr.replace(",", ".")
        return normalized.toDoubleOrNull() ?: 0.0
    }
    
    private fun extractMerchant(text: String): String {
        val lowerText = text.lowercase()
        
        for (merchant in merchants) {
            if (lowerText.contains(merchant.lowercase())) {
                return merchant
            }
        }
        
        // Try to extract a capitalized word that looks like a merchant
        val capitalizedPattern = Regex("""([A-Z][a-z]+(?:\s+[A-Z][a-z]+)?)""")
        capitalizedPattern.find(text)?.let {
            return it.value
        }
        
        return "Desconocido"
    }
    
    fun toExpense(parsed: ParsedPayment): Expense {
        return Expense(
            amount = parsed.amount,
            currency = parsed.currency,
            merchant = parsed.merchant,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            confirmed = false
        )
    }
}
