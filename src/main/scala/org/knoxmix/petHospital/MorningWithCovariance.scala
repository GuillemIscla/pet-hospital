package org.knoxmix.petHospital


trait Customer[+P]{
  val personName:String
  def getPatient:P
}

object MorningWithCovariance extends App {

  val customerForm1:Customer[Pet] = new Customer[Cat]{
    val personName:String = "John"
    def getPatient:Cat = new Cat{
      val name:String = "Misha"
      val hairColor:String = "black"
    }
  }

  val customerForm2:Customer[Pet] = new Customer[Dog]{
    val personName:String = "Carla"
    def getPatient:Dog = new Dog{
      val name:String = "Bobby"
      def sayWoof():String = "Woof"
    }
  }

  /** It does not compile :( */
//  val customerForm3:Customer[Dog] = new Customer[Pet]{val personName:String = ""; def getPacient():Pet = ???}


  /** No wonder it does not compile! I should not be allowed to do this: */
//  customerForm3.getPatient().sayWoof() //getPatient().sayWoof() can be done by Customer[Dog] but not by Customer[Pet]

}
