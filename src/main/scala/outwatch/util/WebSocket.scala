package outwatch.util

import org.scalajs.dom.raw.{CloseEvent, ErrorEvent, MessageEvent}
import outwatch.Sink
import rxscalajs.Observable


object WebSocket {
  implicit def toSink(socket: WebSocket): Sink[String] = socket.sink
  implicit def toSource(socket: WebSocket): Observable[MessageEvent] = socket.source
}

case class WebSocket private(url: String) {
  val ws = new org.scalajs.dom.WebSocket(url)

  lazy val source = Observable.create[MessageEvent](observer => {
    ws.onmessage = (e: MessageEvent) => observer.next(e)
    ws.onerror = (e: ErrorEvent) => observer.error(e)
    ws.onclose = (e: CloseEvent) => observer.complete()
  })

  lazy val sink = Sink.create[String](s => ws.send(s))

}

