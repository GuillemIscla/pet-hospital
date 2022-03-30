package org.knoxmix.petHospital


trait Doctor[-P]{
  def treat(p:P):Unit
}

case object Cathy extends Doctor[Cat] {
  override def treat(p: Cat): Unit =
    println(s"I am treating the cat: ${p.name}. The color of his fur is: ${p.hairColor}")
}

case object Chuck extends Doctor[Dog] {
  override def treat(p: Dog): Unit =
    println(s"I am treating the dog: ${p.name}. It noisily says ${p.sayWoof()} all the time")
}

trait BullDog extends Dog {
  override def sayWoof(): String = "WOOF!! WOOF!!"
}

trait Chiwawa extends Dog {
  override def sayWoof(): String = "Woiiiiif"
}

object NoonWithContravariance extends App {

  val doctorForm2:Doctor[Chiwawa] = Chuck
  val doctorForm3:Doctor[BullDog] = Chuck

  //It does not compile :(
//  val doctorForm1:Doctor[Pet] = new Doctor[Cat]{def treat(p: Cat): Unit = ()}

  //No wonder it does not compile! I should not be allowed to do this:
//  val myDog:Dog = ???
//  doctorForm1.treat(myDog) //treat with input dog can be done by Doctor[Pet] but not by Doctor[Cat]

}
