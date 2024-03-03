package com.example.mytestgo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.mytestgo.utils.CircularProgressBar
import com.example.mytestgo.utils.StoragePermissionHelper
import com.maning.updatelibrary.InstallUtils

class MainActivity : AppCompatActivity() {
    lateinit var  circularProgressBar :CircularProgressBar
    private val storagePermissionHelper by lazy { StoragePermissionHelper(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)

        circularProgressBar.setProgress(50)
        findViewById<TextView>(R.id.download).setOnClickListener{
            // 动画模拟10秒下载完
            /*val animator = ValueAnimator.ofInt(0, 100)
            animator.duration = 5000 // 5秒
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener { valueAnimator ->
                val progress = valueAnimator.animatedValue as Int
                circularProgressBar.setProgress(progress)
            }
            animator.start()*/
            // 请求权限
            storagePermissionHelper.requestStoragePermission(object : StoragePermissionHelper.PermissionCallback {
                override fun onPermissionGranted() {
                    // 权限被授予
                    // 执行需要权限的操作
                    goDownload("https://lanxiong1122.github.io/app-release.apk",this@MainActivity)
                }
                //https://lanxiong1122.github.io/myTestGo.zip
                override fun onPermissionDenied() {
                    // 权限被拒绝
                    // 可以提示用户或执行其他操作
                }
            })
        }
    }

    @SuppressLint("CheckResult")
    private fun goDownload(downloadUrl : String,mContext : Activity){
        InstallUtils.with(mContext)
            .setApkUrl(downloadUrl)                   // 必须-下载地址
            // .setApkPath(Constants.APK_SAVE_PATH)    // 非必须-下载保存的文件的完整路径+/name.apk，使用自定义路径需要获取读写权限
            .setCallBack(object : InstallUtils.DownloadCallBack {  // 非必须-下载回调
                override fun onStart() {
                    //下载开始
                    Toast.makeText(mContext,"正在下载中",Toast.LENGTH_SHORT).show()
                }

                override fun onComplete(path: String) {
                    //下载完成
                    Log.e("路径", path)
                    InstallUtils.installAPK(
                        mContext,
                        path,
                        object : InstallUtils.InstallCallBack {
                            override fun onSuccess() {
                                //onSuccess：表示系统的安装界面被打开
                                //防止用户取消安装，在这里可以关闭当前应用，以免出现安装被取消
                                Toast.makeText(mContext,"正在安装程序",Toast.LENGTH_SHORT).show()
                            }

                            override fun onFail(e: java.lang.Exception) {
                                Toast.makeText(mContext,"安装失败",Toast.LENGTH_SHORT).show()
                                // 通过浏览器去下载APK
                                // InstallUtils.installAPKWithBrower(this@WebviewActivity, path)
                            }
                        })

                }

                @SuppressLint("SetTextI18n")
                override fun onLoading(total: Long, current: Long) {
                    //下载中
                    Log.e("总进度", total.toString())
                    Log.e("进度", current.toString())
                    Log.e("进度tiao总进度tiao总进度tiao总", "" + total.toInt())

                    circularProgressBar.setProgress(((current.toFloat() / total) * 100).toInt())
                    Log.e("进度tiao部分", "" + current.toInt())
                    Log.e("百分比", "" + ((current.toFloat() / total) * 100).toInt() + "%")

                }

                override fun onFail(e: Exception) {
                    //下载失败
                }

                override fun cancle() {
                    //下载取消
                }
            }).startDownload()   //开始下载
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        storagePermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}