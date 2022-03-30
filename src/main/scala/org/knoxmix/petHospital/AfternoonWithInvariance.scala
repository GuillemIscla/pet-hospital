package org.knoxmix.petHospital


trait Container[P]{
  def put(p:P):Unit
  def checkIfFull:Boolean
  def get():P
}

object AfternoonWithInvariance extends App {

/** Doesn't compile :( */
//  val containerForm1:Container[Dog] = new Container[Pet]{
//    def put(p:Pet):Unit = ???
//    def checkIfFull:Boolean = false
//    def get():Pet = ???
//  }


/** No wonder it does not compile! I cannot do this: */
//  containerForm1.get().sayWoof() // get().sayWoof() can be done by Container[Dog] but not by Container[Pet]

/** Doesn't compile either :'( */
//  val containerForm2:Container[Pet] = new Container[Dog]{
//    def put(p:Dog):Unit = ???
//    def checkIfFull:Boolean = false
//    def get():Dog = ???
//  }


/** No wonder it does not compile! I cannot do this: */
//  val myCat:Cat = ???
//  containerForm2.put(myCat) //put with input cat can be done by Container[Pet] but not by Container[Dog]

}
