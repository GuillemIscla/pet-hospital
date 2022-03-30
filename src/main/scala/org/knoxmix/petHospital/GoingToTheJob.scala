package org.knoxmix.petHospital

trait Pet {
  val name:String
}

trait Cat extends Pet {
  val name: String
  val hairColor: String
}

trait Dog extends Pet {
  val name: String
  def sayWoof():String
}