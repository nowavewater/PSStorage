package com.waldemartech.psstorage.ui.tool

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.ui.widget.base.theme.LocalNavController
import com.waldemartech.psstorage.ui.widget.button.HeightSpacer
import com.waldemartech.psstorage.ui.widget.button.JellyButton

@Composable
fun ToolMenuScreen(
    toolMenuViewModel: ToolMenuViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val context = LocalContext.current // 获取当前的 Context

    // 注册一个 ActivityResultLauncher 来处理文件创建的结果
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            if (uri != null) {
                // 文件 URI 已获取，现在可以写入数据
                toolMenuViewModel.exportData(uri)
            } else {
            //    Toast.makeText(context, "文件 URI 获取失败。", Toast.LENGTH_SHORT).show()
            }
        } else {
        //    Toast.makeText(context, "文件创建已取消。", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeightSpacer()

        JellyButton(text = "Remove Dup") {
            toolMenuViewModel.removeDuplicated()
        }
        HeightSpacer()

        JellyButton(text = "Export") {
        // 创建一个 Intent 来创建文档
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain" // 设置 MIME 类型，例如：纯文本
                putExtra(Intent.EXTRA_TITLE, "ps_store_backup.json") // 默认文件名
                // 提示：你不能直接指定到 Documents 文件夹，
                // 用户会在文件选择器中导航到他们想要的位置。
                // 但系统通常会把 Documents 或 Downloads 作为常见选项。
            }
            createFileLauncher.launch(intent) // 启动文件创建 Intent
        }
        HeightSpacer()

        JellyButton(text = "Import") {
            // 创建一个 Intent 来创建文档
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain" // 设置 MIME 类型，例如：纯文本
                putExtra(Intent.EXTRA_TITLE, "ps_store_backup.json") // 默认文件名
                // 提示：你不能直接指定到 Documents 文件夹，
                // 用户会在文件选择器中导航到他们想要的位置。
                // 但系统通常会把 Documents 或 Downloads 作为常见选项。
            }
            createFileLauncher.launch(intent) // 启动文件创建 Intent
        }
    }

}