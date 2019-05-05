package app


import react.*
import react.dom.*
import logo.*
import ticker.*
import editor.*
import joker.joker

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div("App-header") {
            logo()
            h2 {
                +"Welcome to React with Kotlin"
            }
        }
        p("App-ticker") {
            ticker()
        }
        axiosSearch()
        joker()
    }
}

fun RBuilder.app() = child(App::class) {}
