package com.mhamdaoui.smartcardreader

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.*
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.Settings.ACTION_NFCSHARING_SETTINGS
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import android.provider.Settings.ACTION_NFC_SETTINGS
import android.view.View
import java.io.File


class MainActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        val response = isoDep.transceive(Utils.hexStringToByteArray(
                "00A4040007A0000002471001"))
        //runOnUiThread { textView.append("PAYMENT SUCCESSFUL - THANK YOU!\n") }
        isoDep.close()

   /*     val filename = "test.txt"
        val filelocation = File(Environment.getExternalStorageDirectory().absolutePath, filename)
        val path = Uri.fromFile(filelocation)*/

        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("fsabri@live.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, "Cherry Tax Receipt - Thank You!")
        i.putExtra(Intent.EXTRA_TEXT, "Cherry Donation \tInvoice# INV0001 \n" +
                "\n" +
                "\n" +
                "Invoice total \$5.00 \n" +
                "Click below to download the PDF invoice. \n" +
                " \n" +
                "Due: 05 Mar 2019 \n\n" +
                "Copy/paste the URL below into your browser:\n" +
                "https://doc.getinvoicesimple.com/v/xlyywgr/INV0001 \n" +
                "\n" +
                "\n" +
                "This message was sent by Cherry Donation\n" +
                " \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n")
        //i.putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }finally {
            isoDep.close()
        }

/*        //sendFile(textView)
        val fileName = "test.txt"

        // Retrieve the path to the user's public pictures directory
        val fileDirectory = Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS)

        // Create a new file using the specified directory and name
        val fileToTransfer = File(fileDirectory, fileName)
        fileToTransfer.setReadable(true, false)


        val txt = createTextRecord(null, "kuyguyfkutf")
        val uri = createUri(Uri.fromFile(fileToTransfer))
        val msg = NdefMessage(txt)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.setNdefPushMessage(msg, this)*/
    }

  /*  fun sendFile(view: View) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Check whether NFC is enabled on device
        if (!nfcAdapter?.isEnabled!!) {
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this, "Please enable NFC.",
                    Toast.LENGTH_SHORT).show()
            startActivity(Intent(ACTION_NFC_SETTINGS))
        } else if (!nfcAdapter!!.isNdefPushEnabled) {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this, "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show()
            startActivity(Intent(ACTION_NFCSHARING_SETTINGS))
        } else {
            // NFC and Android Beam both are enabled

            // File to be transferred
            // For the sake of this tutorial I've placed an image
            // named 'wallpaper.png' in the 'Pictures' directory
            val fileName = "test.txt"

            // Retrieve the path to the user's public pictures directory
            val fileDirectory = Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)

            // Create a new file using the specified directory and name
            val fileToTransfer = File(fileDirectory, fileName)
            fileToTransfer.setReadable(true, false)

            nfcAdapter!!.setBeamPushUris(
                    arrayOf<Uri>(Uri.fromFile(fileToTransfer)), this)
            var msg = createUri(Uri.fromFile(fileToTransfer))
            nfcAdapter!!.setNdefPushMessage(arrayOf<Uri>(Uri.fromFile(fileToTransfer)), this)
        }
    }*/

}
