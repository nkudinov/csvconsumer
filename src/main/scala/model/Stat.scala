package org.nkudinov.model

final case class Stat(sum5:BigInt, uniqueNumberOfUsers:Int, usersStat:Seq[UserStat]){
   def show: String = {
    s"""$sum5
       |$uniqueNumberOfUsers
       |${usersStat.mkString("\n")}
     """.stripMargin
  }

}
