package editor


import kotlinext.js.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import react.*
import react.dom.*
import kotlin.js.*

// Import the axios library (run "npm install axios --save" to install)
@JsModule("axios")
external fun <T> axios(config: AxiosConfigSettings): Promise<AxiosResponse<T>>

// Type definition
external interface AxiosConfigSettings {
    var url: String
    var method: String
    var baseUrl: String
    var timeout: Number
    var data: dynamic
    var transferRequest: dynamic
    var transferResponse: dynamic
    var headers: dynamic
    var params: dynamic
    var withCredentials: Boolean
    var adapter: dynamic
    var auth: dynamic
    var responseType: String
    var xsrfCookieName: String
    var xsrfHeaderName: String
    var onUploadProgress: dynamic
    var onDownloadProgress: dynamic
    var maxContentLength: Number
    var validateStatus: (Number) -> Boolean
    var maxRedirects: Number
    var httpAgent: dynamic
    var httpsAgent: dynamic
    var proxy: dynamic
    var cancelToken: dynamic
}

external interface AxiosResponse<T> {
    val data: T
    val status: Number
    val statusText: String
    val headers: dynamic
    val config: AxiosConfigSettings
}

data class DateResult(val default: String, val holiday: Boolean)

interface AxiosProps : RProps {
}

interface AxiosState : RState {
    var date: String
    var dateResult: DateResult
    var errorMessage: String
}

external interface DateData {
    val default: String
    val holiday: Boolean
}

class AxiosSearch(props: AxiosProps) : RComponent<AxiosProps, AxiosState>(props) {
    override fun AxiosState.init(props: AxiosProps) {
        date = ""
        dateResult = DateResult("", true)
        errorMessage = ""
    }

    private fun remoteSearchDate(date: String) {
        val config: AxiosConfigSettings = jsObject {
            url = "https://datazen.katren.ru/calendar/day/$date/"
        }

        axios<DateData>(config).then { response ->
            setState {
                dateResult = DateResult(response.data.default, response.data.holiday)
                errorMessage = ""
            }
            console.log(response)
        }.catch { error ->
            setState {
                dateResult = DateResult("", false)
                errorMessage = error.message ?: ""
            }
            console.log(error)
        }
    }

    private fun handleDateChange(targetValue: String) {
        remoteSearchDate(targetValue)
    }

    override fun RBuilder.render() {
        div {
            h1 {+"Выберите дату"}
            input(type = InputType.date, name = "date") {
                key = "dateInput"
                attrs {
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        handleDateChange(target.value)
                    }
                }
            }
            br {}
            h1 {
                +"Этот день ${if(state.dateResult.holiday) "" else "не " }является выходным"
            }
        }
    }
}

fun RBuilder.axiosSearch() = child(AxiosSearch::class) {
}