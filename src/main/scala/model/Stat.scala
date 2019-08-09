package model

final case class Stat(sum5:BigInt, users:Int,agg:List[(String,BigDecimal,BigInt)]){
  override def toString: String = {
    s"""$sum5
       |$users
       |${agg.mkString("\n")}
     """.stripMargin
  }
}
