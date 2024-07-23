package com.example.weathertracker.permissionlauncher

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.weathertracker.R

@Composable
fun PermissionDeclineDialog(
    @StringRes text: Int,
    @StringRes confirmButtonText: Int,
    onConfirmButtonClick: () -> Unit = {}
) {
    // dialog state used for showing the alert dialog
    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},      // not implementing this as we don't want to close the alert dialog when the user click anywhere else on the screen
            confirmButton = {
                Text(
                    text = stringResource(confirmButtonText),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        showDialog = false
                        onConfirmButtonClick()      // lambda function to handle user clicking on confirm button
                    }
                )
            },
            title = {
                Text(text = stringResource(id = R.string.alert_dialog_title))
            },
            text = {
                Text(text = stringResource(text))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionDeclineDialogPreview() {
    PermissionDeclineDialog(
        R.string.permission_reason,
        confirmButtonText = R.string.ok
    )
}