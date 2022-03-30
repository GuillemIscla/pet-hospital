#Variance in a pet hospital

##Introduction
This example is kind of a real life visualization on defining variance on types, I hope you can understand that there is an actual motivation behind this concept. 
You can about it too in the official [scala documentation](https://docs.scala-lang.org/tour/variances.html)
The topic is also visited in Martin Ordesky's scala course

You can go to the code and change the + and - to get errors that tell you about the variance in the types.

A side note... It took me long to get this concept right. My advice is that for complicated things is ok just to know they exist, 
they have their rules and know where to look up for those rules. Eventually one gets better and better after working with that.


## Rules of variance
This is a summary of the rules in variance. 

The <: and :> notation is used to express the subtype relation. "A <: B" reads as "A is subtype of B" while "A :> B" reads as "A is supertype of B".

A higher kinded type is a type that depends on other types such as List[T] which depends on T. Each higher kinded types is 
actually a family of types, one type for each T you put as the inner type.

###Covariance
Covariance is the type of variance which preserve the type chain of the inner types, as the Co- prefix suggests.

When a higher kinded type H is covariant on T we will write H[+T]

So if we have A <: B and the higher kinded type H[+T], then it follows that H[A] <: H[B]

###Contravariance
Contravariance is the type of variance which preserve the type chain of the inner types in reverse order, as the Contra- prefix suggests.

When a higher kinded type H is contravariant on T we will write H[-T]

So if we have A <: B and the higher kinded type H[-T], then it follows that H[A] :> H[B]

###Invariance
Invariance is the type of variance which does not infer any subtyping structure on the higher kinded types of that type, as the In- prefix suggests.

When a higher kinded type H is invariant on T we will write H[T]

So even if we have A <: B and the higher kinded type H[T], not H[A] <: H[B] neither H[A] :> H[B] follow


##Going to the job
Imagine you were to work as an assistant in a pet hospital. 
The job requires you to learn about type variance and you don't know what it is. 
You do know that some types are subtypes of others though. This is your introduction day.

This pet hospital had problems with pet snakes so at the moment it is just dealing with cats and dogs

```
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
```

###Morning with covariance (+):
The manager asks you to go to the first two waiting customers and implement their code in the forms.

```
trait Customer[+P]{
  val personName:String
  def getPatient:P
}
val customerForm1:Customer[Pet] = ???
val customerForm2:Customer[Pet] = ???
```

You go to the customers and talk to them but got confused so ask the manager:
You: I was expecting them to return a pet but they really just return dogs and cats!! What should I do?
Manager: Don't worry, listen carefully.
1) Cat and Dog extend Pet
2) A Customer[Pet] can have output in their extended functions of any type that extends Pet

Knowing that, you start coding and is what you ended up with:
```
trait Customer[+P]{
  val personName:String
  def getPatient:P
}
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
```


You find some forms for Customer[Dog], they don't seem to work on a Customer[Pet]

```
  /** It does not compile :( */
// val customerForm3:Customer[Dog] = new Customer[Pet]{val personName:String = ""; def getPatient():Pet = ???}


  /** No wonder it does not compile! I should not be allowed to do this: */
//customerForm3.getPatient().sayWoof() //getPatient().sayWoof() can be done by Customer[Dog] but not by Customer[Pet]
```

###Noon with contravariance (-):
You seem to have learned how the types work now. 
At noon when having lunch you meet Chuck a dog doctor and Cathy a cat doctor. 
They tell you about them and their work:

```
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
```

They are very explicit on that Cathy only inputs cats and Chuck only inputs dogs in their respective method treat, 
it was not like the customer forms you just did. If you were to try this, it could not work but notice that with the trait Customer, 
it was working when Customer[Pet] was at the left side of the =  and Customer[Cat] at the right side.


```
  /** It does not compile :( */
//val doctorForm1:Doctor[Pet] = new Doctor[Cat]{def treat(p: Cat): Unit = ()}

  /** No wonder it does not compile! I should not be allowed to do this: */
val myDog:Dog = ???
//doctorForm1.treat(myDog) //treat with input dog can be done by Doctor[Pet] but not by Doctor[Cat]
```

Chuck shows you some pics of kinds of dogs he treats:

```
trait BullDog extends Dog {
  override def sayWoof(): String = "WOOF!! WOOF!!"
}

trait Chiwawa extends Dog {
  override def sayWoof(): String = "Woiiiiif"
}
```


You are surprised:
You: Wait! Those things are not the same type, right? And you are a Doctor[Dog], does it mean we have some Doctor[Chiwawa] that helps you?
Chuck: Don't worry, listen carefully.
1) BullDog and Chiwawa extend Dog
2) A Doctor[Dog] can have input in their extended functions of any type that extends Dog

You code some snippet to check if what Chuck is saying really compiles:

```
case object Chuck extends Doctor[Dog] {
  override def treat(p: Dog): Unit =
  println(s"I am treating the dog: ${p.name}. It noisily says ${p.sayWoof()} all the time")
}

val doctorForm2:Doctor[Chiwawa] = Chuck
val doctorForm3:Doctor[BullDog] = Chuck
```

###Afternoon with invariance (no sign):
After treating the animals, some of them will spend the night at the hospital in containers. 
It would seem that the hospital got rid of the containers for cats of a brand named Schrodinger, 
apparently cats were dead inside (or were they?).

Anyway these ones seem easy to deal with:

```
trait Container[P]{
  def put(p:P):Unit
  def checkIfFull:Boolean
  def get():P
}
```

You see that there are Container[Cat], Container[Dog] and Container[Pet], so you ask the manager which ones to use.
The manager says:
- Cat and Dog extend Pet. Oh, you knew that already.
- You can input any pet into a Container[Pet]. They seem very useful, but tomorrow when you use the get() method you won't actually know if it will output a dog or a cat
- When using get on a Container[Dog] you can be sure you output a dog. They seem very useful, but when you have a cat to input you cannot really use this type of container.

So that means really you have to get the exact type every time then:
```
/** Doesn't compile :( */
// val containerForm1:Container[Dog] = new Container[Pet]{
//   def put(p:Pet):Unit = ???
//   def checkIfFull:Boolean = false
//   def get():Pet = ???
// }


/** No wonder it does not compile! I cannot do this: */
// containerForm1.get().sayWoof() // get().sayWoof() can be done by Container[Dog] but not by Container[Pet]

/** Doesn't compile either :'( */
// val containerForm2:Container[Pet] = new Container[Dog]{
//   def put(p:Dog):Unit = ???
//   def checkIfFull:Boolean = false
//   def get():Dog = ???
// }


/** No wonder it does not compile! I cannot do this: */
// val myCat:Cat = ???
// containerForm2.put(myCat) //put with input cat can be done by Container[Pet] but not by Container[Dog]
```

###At the end of the day. Comparing the variances
The manager meets with you to explain the whole system again comparing the cases you've been facing:

Regarding the task you had to do in the morning.
This is the type customer:
```
trait Customer[+P]{
  val personName:String
  def getPatient():P
}
```

Notice that the P has a +, this means is a  covariant inner type.
In our case:
- Dog is a subtype of Pet or Dog <: Pet
- And since P is covariant Customer[Dog] is subtype of Customer[Pet] or Customer[Dog] <: Customer[Pet]
- Notice that the typing is the same order in the higher kinded type: Dog <: Pet and Customer[Dog] <: Customer[Pet], that is why is called covariant
- That means a Customer[Dog] can do anything a Customer[Pet] can! For example a Customer[Pet] gives you a pet as an output for the method getPatient, this is also the type of output of the Customer[Dog] at that method.
- One rule we have when we do that, is that P can only be in the output type of an extended function. Never on the input! We say that the covariant position is the output. Notice that the P only appears in the output of the method getPatient, so we respect that restriction.

Now for the task you had to do at noon:
This is the type doctor:
```
trait Doctor[-P]{
  def treat(p:P):Unit
}
```
Notice that the P has a -, this means is a contravariant inner type.
In our case:
- Chiwawa is a subtype of Dog or Chiwawa <: Dog
- And since P is contravariant  Doctor[Dog] is subtype of Doctor[Chiwawa] or Doctor[Dog] <: Doctor[Chiwawa].
- Notice that the typing turns to be the opposite order in the higher kinded type: Chiwawa <: Dog and Doctor[Dog] <: Doctor[Chiwawa], that is why is called contravariant
- That means a Doctor[Dog] can do anything a Doctor[Chiwawa] can! For example a Doctor[Chiwawa] accepts a chiwawa as an input for the method treat, this is also a valid input type for the Doctor[Dog] at that method
- One rule we have when we do that, is that P can only be in the input type of an extended function. Never on the output! We say that the contravariant position is the input. Notice that on our case, P only appears as an input for the method treat, so we respect that restriction.


Now for the task you had to do in the afternoon:
This is the type Container:
```
trait Container[P]{
  def put(p:P):Unit
  def checkIfFull:Boolean
  def get():P
}
```

Notice that the P has not a + nor a -, this means is an invariant inner type
In our case:
- Dog is a subtype of Pet or Dog <: Pet
- But Container[Pet] is not a subtype of Container[Dog]. Why? Because Container[Pet] cannot do anything a Container[Dog] can. For example, you cannot execute method get and expect a dog in the output, you might get a cat.
- And Container[Dog] is not a subtype of Container[Pet] neither. Why? Because Container[Dog] cannot do anything a Container[Pet] can. For example, you cannot use the method put and input a cat in a Container[Dog], you only can input dogs.
- We are forced to use invariant types when we have to put the P in both input and output of extended functions. Notice that on our case P is input on the function put and is output on the function get.



###After the working day

The manager tells you that is your first day with this concept. You will get better as long as you work with it. The important facts is that they have rules and you now know where to read about them whenever you need. You can try these things though, with the code provided and see the errors that come up (In Intellij should be underlined before clicking compile):

Delete the + in the Customer and look at the forms where you fill the customer information
Delete the - in the Doctor and look at Chuck not being able to treat a Chiwawa
Try to add + and - to the Container and look for errors and its description at the trait
Take it easy and keep coding!

