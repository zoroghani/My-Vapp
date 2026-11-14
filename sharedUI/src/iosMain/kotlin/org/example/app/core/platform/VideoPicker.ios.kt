package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.Foundation.NSURL
import platform.darwin.NSObject
import platform.UIKit.UIImagePickerControllerMediaURL

// 1. Delegate کلاس جو نتیجہ واپس بھیجے گی
class VideoPickerDelegate(
    private val onResult: (String?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
    
    override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
        val url = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
        onResult(url?.path) // URL کا پاتھ واپس بھیجیں
        picker.dismissViewControllerAnimated(true, null)
    }
    
    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onResult(null)
        picker.dismissViewControllerAnimated(true, null)
    }
}

@Composable
actual fun rememberVideoPicker(onVideoSelected: (String) -> Unit): () -> Unit {
    return remember {
        {
            val picker = UIImagePickerController()
            // فوٹو لائبریری کھولیں
            picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            // صرف ویڈیوز کی اجازت دیں
            picker.mediaTypes = listOf("public.movie")
            
            // Delegate سیٹ کریں
            picker.delegate = VideoPickerDelegate { path ->
                if (path != null) {
                    onVideoSelected(path)
                }
            }
            
            // اسکرین پر پکر دکھائیں
            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(picker, true, null)
        }
    }
}
