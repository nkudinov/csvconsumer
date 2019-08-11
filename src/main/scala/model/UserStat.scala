package org.nkudinov.model

final case class UserStat( uuid:String, avg3:BigDecimal, mostRecent4:BigInt){
  override def toString: String = s"$uuid,$avg3,$mostRecent4"
}
