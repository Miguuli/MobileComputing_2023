object androidx {
    object core {
        private val version = "1.9.0"
        val ktx = "androidx.core:core-ktx:$version"
    }

    object compose {
        private val version = "1.4.1"
        object ui {
            val ui = "androidx.compose.ui:ui:$version"
            val preview = "androidx.compose.ui:ui-tooling-preview:$version"
        }
        object foundation{
            val foundation = "androidx.compose.foundation:foundation:$version"
            val foundation_layout = "androidx.compose.foundation:foundation-layout:$version"
        }
        val material = "androidx.compose.material:material:$version"
        val material_icons = "androidx.compose.material:material-icons-extended:$version"
        val material3 = "androidx.compose.material3:material3:1.0.1"
    }

    object lifecycle {
        private val version = "2.5.1"
        val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
    }

    object navigation {
        private val version = "2.5.3"
        val compose = "androidx.navigation:navigation-compose:$version"

        object hilt {
            private val version = "1.0.0"
            val compose = "androidx.hilt:hilt-navigation-compose:$version"
        }
    }

    object activity {
        private val version = ""
    }

    object constraintlayout {
        private val version = "1.0.1"
        val compose = "androidx.constraintlayout:constraintlayout-compose:$version"
    }

    object room {
        private val version = "2.4.3"
        val ktx = "androidx.room:room-ktx:$version"
        val runtime = "androidx.room:room-runtime:$version"
        val compiler = "androidx.room:room-compiler:$version"
    }

    object datastore {
        private val version = "1.1.0-alpha04"
        val datastore_core = "androidx.datastore:datastore-core-android:$version"
    }

    object protobuf {
        private val version = "3.19.4"
        val javalite = "com.google.protobuf:protobuf-javalite:$version"
        val protoc = "com.google.protobuf:protoc:$version"
    }
}