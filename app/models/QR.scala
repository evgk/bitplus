package models

import net.glxn.qrgen._
import grizzled.file.GrizzledFile._

object QRGen {
  
    def genQr(data: String, outfile: String) = {
      val f = QRCode.from(data).file()
      f.copyTo(outfile)
    }
}