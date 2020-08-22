package com.shuaiyu.stickynote

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.googlecode.tesseract.android.TessBaseAPI
import com.wildma.pictureselector.PictureBean
import com.wildma.pictureselector.PictureSelector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
        btn_me.setOnClickListener {
            PictureSelector
                .create(this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture();
        }
    }

    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    private val DATAPATH: String = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator

    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private val tessdata = DATAPATH + File.separator.toString() + "tessdata"

    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private val DEFAULT_LANGUAGE = "chi_sim"

    /**
     * assets中的文件名
     */
    private val DEFAULT_LANGUAGE_NAME = "$DEFAULT_LANGUAGE.traineddata"

    /**
     * 保存到SD卡中的完整文件名
     */
    private val LANGUAGE_PATH =
        tessdata + File.separator.toString() + DEFAULT_LANGUAGE_NAME

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                val pictureBean: PictureBean =
                    data.getParcelableExtra(PictureSelector.PICTURE_RESULT)!!
                val tessBaseAPI = TessBaseAPI()
                //需下载字体识别库  TODO
                tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
                tessBaseAPI.setImage(BitmapFactory.decodeFile(pictureBean.path))

                if (pictureBean.isCut) {
                    img_me.setImageBitmap(BitmapFactory.decodeFile(pictureBean.path))
                } else {
                    img_me.setImageURI(pictureBean.uri)
                }





                val text = tessBaseAPI.utF8Text
                Log.e("eee", "onActivityResult: $text" )
                tv_me.text=text;
                //使用 Glide 加载图片
                /*Glide.with(this)
                        .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                        .apply(RequestOptions.centerCropTransform()).into(mIvImage);*/
            }
        }
    }

    /**
     * 将assets中的识别库复制到SD卡中
     * @param path  要存放在SD卡中的 完整的文件名。这里是"/storage/emulated/0//tessdata/chi_sim.traineddata"
     * @param name  assets中的文件名 这里是 "chi_sim.traineddata"
     */
    fun copyToSD(path: String, name: String) {
        Log.i("TAG", "copyToSD: $path")
        Log.i("TAG", "copyToSD: $name")

        //如果存在就删掉
        val f = File(path)
        if (f.exists()) {
            f.delete()
        }
        if (!f.exists()) {
            val p = File(f.parent)
            if (!p.exists()) {
                p.mkdirs()
            }
            try {
                f.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var `is`: InputStream? = null
        var os: OutputStream? = null
        try {
            `is` = this.assets.open(name)
            val file = File(path)
            os = FileOutputStream(file)
            val bytes = ByteArray(2048)
            var len = 0
            while (`is`.read(bytes).also({ len = it }) != -1) {
                os.write(bytes, 0, len)
            }
            Log.e("TAG", "copyToSD: " )
            os.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) `is`.close()
                if (os != null) os.close()
                Log.e("TAG", "创建成功")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
