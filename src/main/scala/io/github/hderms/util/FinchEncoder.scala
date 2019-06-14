package io.github.hderms.util

/**
  * Encapsulates the functionality required to easily define an [[io.finch.Encode]] for CSV
  */
object FinchEncoder {
  import io.finch.{Application, Encode}
  import scala.collection.TraversableOnce
  import scala.language.higherKinds
  import java.nio.charset.Charset
  import com.twitter.io.Buf

  type Aux[A, CT <: String] = Encode[A] { type ContentType = CT }
  type Csv[A, F[_] <: TraversableOnce[_]] = Aux[F[A], Application.Csv]

  /**
    * Defines an [[io.finch.Encode]] for CSV for a traversable provided it can be turned into a [[Buf]]
    * In other words we can use this to build an Encoder for [[Application.Csv]] content type
    *
    * @param fn given a traversable F[_] of A and a particular Charset, we need to be able to turn that into a
    *           [[Buf]]
    * @tparam A The type we are trying to turn into a row of the resulting CSV
    * @tparam F the traversable collection of A as CSVs are really only useful for collections of elements
    * @return an [[Encode[A]] with content type of [[Application.Csv]]
    */
  def csv[A, F[_] <: TraversableOnce[_]](
      fn: (F[A], Charset) => Buf): Csv[A, F] =
    instance[F[A], Application.Csv](fn)

  private def instance[A, CT <: String](fn: (A, Charset) => Buf): Aux[A, CT] =
    new Encode[A] {
      type ContentType = CT

      def apply(a: A, cs: Charset): Buf = fn(a, cs)
    }
}
