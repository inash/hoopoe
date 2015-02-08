package com.hoopoe.message

case class iTG (
  deviceType: String,  // protocol
  imei: String,        // imei
  utctime: String,     // gprmc_time
  date: String,        // gprmc_date
  checksum: String,
  event: String,       // device_status1
  tid: String,         // device_name
  tevent: String,      // device_status2
  tpower: String,      // device_analog2
  tsignalrssi: String, // device_analog1
  reportcount: String, // device_status3
  gsmrssi: String,     // gsm_rssi
  battery: String      // device_battery
) extends Message

/** Sample message:
 *  protocol, imei, time, date, checksum, device_status1, device_name, device_status2,,,device_status2 
 *  iTG,351535052044971,235111,011520,*02,AUTO,,,,,N2460,M31,4389mV */

case object iTG {
  def apply(message: String): iTG = {
    val params = message.split(",").toList
//    val m = iTG.getClass.getMethods.find(x => x.getName == "apply" && x.isBridge).get.invoke(iTG, params map (_.asInstanceOf[AnyRef]): _*).asInstanceOf[iTG]
//    m
    val m = iTG(
      params(0), params(1), params(2), params(3), params(4), params(5), params(6),
      params(7), params(8), params(9), params(10), params(11), params(12)
    )
    m
  }
}
