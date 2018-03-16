package examples

import cats.kernel.instances.all._
import spire.implicits._
import breeze2.linalg._
import breeze2.linalg.implicits._

object DenseVectorExamples extends App {

  val dv1 = DenseVector.tabulate[Double](10)(_.toDouble)
  val dv2 = DenseVector.tabulate[Double](10)(_.toDouble * -1.0)

  val dv3 = dv1 |+| dv2
  dv3.foreach(println)
}
