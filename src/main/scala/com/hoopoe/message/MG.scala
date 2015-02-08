package com.hoopoe.message

case class MG (
  dataHead: String,
  protocolVersion: String,
  imei: String,
  deviceName: String,
  gprsRealStoredFlag: String,
  date: String, //Date,
  time: String, //Date,
  gpsFixFlag: String,
  latitude: String,
  ns: String,
  longitude: String,
  we: String,
  satelliteBds: String,
  satelliteGps: String,
  satelliteGlonass: String,
  hdop: String,
  speed: String,
  course: String,
  altitude: String,
  mileage: String,
  mcc: String,
  mnc: String,
  lac: String,
  cellId: String,
  gsmSignalStrength: String,
  digitalInput: String,
  digitalOutput: String,
  analogInput1: String,
  analogInput2: String,
  analogInput3: String,
  temperatureSensor1: String,
  temperatureSensor2: String,
  rfid: String,
  externalAccStatus: String,
  batteryPercentage: String,
  alertEventType: String
) extends Message

/** Sample message:
 *  $MGV002,860719020193193,DeviceName,R,240214,104742,A,2238.20471,N,11401.97967,
 *  E,00,03,00,1.20,0.462,356.23,137.9,1.5,460,07,262C,0F54,25,0000,0000,0,0,0,
 *  28.5,28.3,,10,100,Timer;! */

case object MG {
  def apply(message: String): MG = {
    val v: Seq[String] = message.split(",").toSeq
    val m = MG(
        "$", v(0).substring(1), v(1), v(2), v(3), v(4), v(5), v(6), v(7), v(8), v(9), v(10), 
        v(11), v(12), v(13), v(14), v(15), v(16), v(17), v(18), v(19), v(20), 
        v(21), v(22), v(23), v(24), v(25), v(26), v(27), v(28), v(29), v(30), 
        v(31), v(32), v(33), v(34).split(";")(0)) 
    m
  }
}
