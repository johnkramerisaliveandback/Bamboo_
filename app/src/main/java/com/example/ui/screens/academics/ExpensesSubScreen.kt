package com.example.ui.screens.academics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Expense
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpensesSubScreen(
    expenses: List<Expense>,
    onSaveExpense: (Expense) -> Unit,
    onDeleteExpense: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val totalAmount = expenses.sumOf { it.amount }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ExpenseSummaryCard(totalAmount = totalAmount)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (expenses.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No expenses recorded.",
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(expenses) { expense ->
                        ExpenseItem(expense = expense, onDelete = { onDeleteExpense(expense.id) })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = BambooPrimaryGreen,
            contentColor = BambooBg
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, amount, category ->
                val newExpense = Expense(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    amount = amount,
                    category = category,
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                )
                onSaveExpense(newExpense)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ExpenseSummaryCard(totalAmount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BambooSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TOTAL EXPENDITURE",
                color = BambooTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = PoppinsFontFamily
            )
            Text(
                text = "₹${String.format(Locale.US, "%.2f", totalAmount)}",
                color = BambooPrimaryGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BambooSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    color = BambooTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily
                )
                Text(
                    text = "${expense.category} • ${expense.date}",
                    color = BambooTextMuted,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily
                )
            }
            Text(
                text = "₹${expense.amount}",
                color = BambooTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BambooAbsentRed)
            }
        }
    }
}

@Composable
fun AddExpenseDialog(onDismiss: () -> Unit, onSave: (String, Double, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    val categories = listOf("Food", "Travel", "Books", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BambooSurface,
        title = { Text("Add Expense", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("What did you spend on?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BambooElevated,
                        unfocusedContainerColor = BambooElevated
                    )
                )
                TextField(
                    value = amount,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                    placeholder = { Text("Amount (₹)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BambooElevated,
                        unfocusedContainerColor = BambooElevated
                    )
                )
                Text("Category", color = BambooTextSecondary, fontSize = 12.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 10.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amt > 0) onSave(title, amt, category)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BambooPrimaryGreen)
            ) {
                Text("Add", color = BambooBg)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BambooTextSecondary)
            }
        }
    )
}
