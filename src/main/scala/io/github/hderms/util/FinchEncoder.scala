package io.github.hderms.util

object FinchEncoder {
  import io.finch.{Application, Encode}
  import scala.collection.TraversableOnce
  import scala.language.higherKinds
  import java.nio.charset.Charset
  import com.twitter.io.Buf

  type Aux[A, CT <: String] = Encode[A] { type ContentType = CT }
  type Csv[A, F[_] <: TraversableOnce[_]] = Aux[F[A], Application.Csv]

  def instance[A, CT <: String](fn: (A, Charset) => Buf): Aux[A, CT] =
    new Encode[A] {
      type ContentType = CT

      def apply(a: A, cs: Charset): Buf = fn(a, cs)
    }

  def csv[A, F[_] <: TraversableOnce[_]](
      fn: (F[A], Charset) => Buf): Csv[A, F] =
    instance[F[A], Application.Csv](fn)
}
