package com.jesushz.spendless.transactions.domain

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.util.formatToReadableDate
import com.jesushz.spendless.core.util.getDateFormat
import java.io.File

class ExportTransactionsUseCase(
    private val context: Context
) {

    fun execute(
        transactions: List<Transaction>
    ): Result<Uri> {
        val fileName = "transactions${getDateFormat()}.csv"
        val csvHeader = "ID,Category,Amount,Receiver,Note,Repeat,Transaction Type,Date"
        val csvData = transactions.joinToString("\n") { transaction ->
            listOf(
                transaction.id,
                transaction.category?.title.orEmpty(),
                transaction.amount,
                transaction.receiver,
                transaction.note,
                transaction.repeat.title,
                transaction.transactionType.title,
                formatToReadableDate(transaction.date)
            ).joinToString(",") { it.toString().replace(",", " ") }
        }

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            ?: return Result.failure(Exception("Failed to create file URI"))

        try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.bufferedWriter().use { writer ->
                    writer.write(csvHeader)
                    writer.newLine()
                    writer.write(csvData)
                }
            }
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            return Result.success(uri)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}