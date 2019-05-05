package joker


import editor.AxiosConfigSettings
import editor.AxiosResponse
import kotlinext.js.*
import kotlinx.html.js.*
import react.*
import react.dom.*
import kotlin.js.*

// Import the axios library (run "npm install axios --save" to install)
@JsModule("axios")
external fun <T> axios(config: AxiosConfigSettings): Promise<AxiosResponse<T>>

interface AxiosState : RState {
    var joke: String
}

external interface JokeData {
    val value: String
}

class Joker(props: RProps) : RComponent<RProps, AxiosState>(props) {
    override fun AxiosState.init(props: RProps) {
        joke = ""
    }

    private fun remoteSearchDate() {
        val config: AxiosConfigSettings = jsObject {
            url = "https://api.chucknorris.io/jokes/random"
        }

        axios<JokeData>(config).then { response ->
            setState {
                joke = response.data.value
            }
            console.log(response)
        }.catch { error ->
            setState {
                joke = "Error"
            }
            console.log(error)
        }
    }

    override fun RBuilder.render() {
        div {
            p { +"Тут можно получить случайную шутку про Чака Норриса" }
            button {
                attrs {
                    onClickFunction = {
                        remoteSearchDate()
                    }
                }
                +"Получить шутку"
            }
            br {}
            h1 {
                +"Шутка: ${state.joke}"
            }
        }
    }
}

fun RBuilder.joker() = child(Joker::class) {
}