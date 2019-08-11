package org.nkudinov.controller

import org.nkudinov.model.{Row, Stat, UserStat}
import org.scalatest.FlatSpec

class ControllerSpec  extends FlatSpec  {

  "decode" should "parse String to Row" in  {

    val line = "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"

    val row = Row("0977dca4-9906-3171-bcec-87ec0df9d745", "kFFzW4O8gXURgP8ShsZ0gcnNT5E=", BigDecimal("0.18715484122922377"), BigInt("982761284"), BigInt("8442009284719321817"))

    assert( RequestProcessor.decode(line) == row)

  }

 "toStat" should "convert Seq(row) to Stat" in {
   val row = Row("0977dca4-9906-3171-bcec-87ec0df9d745", "kFFzW4O8gXURgP8ShsZ0gcnNT5E=", BigDecimal("0.18715484122922377"), BigInt("982761284"), BigInt("8442009284719321817"))

   val stat = Stat(BigInt("8442009284719321817"),1,Seq(UserStat("0977dca4-9906-3171-bcec-87ec0df9d745",BigDecimal("0.1871548412292238"), BigInt("982761284"))))

   assert( RequestProcessor.toStat( Seq(row)) == stat )

 }
 "toStat first 5 rows" should "convert 5 rows to Stat" in {
   val l1 = "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"
   val l2 = "5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998"
   val l3 = "0977dca4-9906-3171-bcec-87ec0df9d745,9ZWcYIblJ7ebN5gATdzzi4e8K7Q=,0.9655429720343038,237475359,3923415930816731861"
   val l4 = "4d968baa-fe56-3ba0-b142-be9f457c9ff4,RnJNTKLYpcUqhjOey+wEIGHC7aw=,0.6532229483547558,1403876285,4756900066502959030"
   val l5 = "0977dca4-9906-3171-bcec-87ec0df9d745,N0fiZEPBjr3bEHn+AHnpy7I1RWo=,0.8857966322563835,1851028776,6448117095479201352"

   val row1 = RequestProcessor.decode(l1)
   val row2 = RequestProcessor.decode(l2)
   val row3 = RequestProcessor.decode(l3)
   val row4 = RequestProcessor.decode(l4)
   val row5 = RequestProcessor.decode(l5)

   val userStat1 = UserStat("0977dca4-9906-3171-bcec-87ec0df9d745",BigDecimal("0.6794981485066369"),BigInt("1851028776"))
   val userStat2 = UserStat("5fac6dc8-ea26-3762-8575-f279fe5e5f51",BigDecimal("0.7626710614484215"),BigInt("1005421520"))
   val userStat3 = UserStat("4d968baa-fe56-3ba0-b142-be9f457c9ff4",BigDecimal("0.6532229483547558"),BigInt("1403876285"))


   val stat1 = Stat(BigInt("30212888860247708058"), 3, Seq( userStat1, userStat2, userStat3))

   assert( RequestProcessor.toStat( Seq(row1,row2,row3,row4,row5)) == stat1 )
 }

  "toStat next 5 rows" should "convert next 5 rows to Stat" in {

    val l6 = "0977dca4-9906-3171-bcec-87ec0df9d745,P/wNtfFfa8jIn0OyeiS1tFvpORc=,0.8851653165728414,1163597258,8294506528003481004"
    val l7 = "0977dca4-9906-3171-bcec-87ec0df9d745,Aizem/PgVMKsulLGquCAsLj674U=,0.5869654624020274,1012454779,2450005343631151248"
    val l8 = "023316ec-c4a6-3e88-a2f3-1ad398172ada,TRQb8nSQEZOA5Ccx8NntYuqDPOw=,0.3790267017026414,652953292,4677453911100967584"
    val l9 = "023316ec-c4a6-3e88-a2f3-1ad398172ada,UfL8VetarqYZparwV4AJtyXGgFM=,0.26029423666931595,1579431460,5620969177909661735"
    val l10 ="0977dca4-9906-3171-bcec-87ec0df9d745,uZNIcWQtwst+9mjQgPkV2rvm7QY=,0.039107542861771316,280709214,4450245425875000740"

    val row6 = RequestProcessor.decode(l6)
    val row7 = RequestProcessor.decode(l7)
    val row8 = RequestProcessor.decode(l8)
    val row9 = RequestProcessor.decode(l9)
    val row10 = RequestProcessor.decode(l10)

    val userStat1 = UserStat("0977dca4-9906-3171-bcec-87ec0df9d745",BigDecimal("0.50374610727888"),BigInt("280709214"))
    val userStat2 = UserStat("023316ec-c4a6-3e88-a2f3-1ad398172ada",BigDecimal("0.3196604691859787"),BigInt("1579431460"))

    val stat2 = Stat(BigInt("25493180386520262311"), 2, Seq( userStat1, userStat2))

    assert( RequestProcessor.toStat( Seq(row6,row7,row8,row9,row10)) == stat2 )
  }

}
