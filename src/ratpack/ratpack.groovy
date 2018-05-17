import static ratpack.groovy.Groovy.ratpack

import com.syve.ratpack.demo.Banner

ratpack {

    bindings {
        bind Banner
    }

    handlers {
        get {
            render "Hello World!"
        }
        get(":name") {
            render "Hello $pathTokens.name!"
        }
    }
}