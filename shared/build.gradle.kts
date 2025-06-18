import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
}

group = "com.tuempresa"
version = "1.0.1"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tasks.register("assembleXCFramework", Exec::class) {
        group = "build"
        description = "Assembles XCFramework for iOS"

        val frameworkName = "shared"
        val outputDir = file("$buildDir/XCFramework")

        doFirst {
            outputDir.mkdirs()
        }

        commandLine(
            "xcodebuild",
            "-create-xcframework",
            "-framework", "$buildDir/bin/iosX64/releaseFramework/$frameworkName.framework",
            "-framework", "$buildDir/bin/iosSimulatorArm64/releaseFramework/$frameworkName.framework",
            "-framework", "$buildDir/bin/iosArm64/releaseFramework/$frameworkName.framework",
            "-output", "$outputDir/$frameworkName.xcframework"
        )
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0.1"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("kmp") {
                from(components["kotlin"])
                groupId = "com.tuempresa"
                artifactId = "kmplibrarytest"
                version = "1.0.1"
            }
           
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/migueBarrera/testkmpshared")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME_GITHUB")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN_GITHUB")
                }
            }
        }
    }
}


android {
    namespace = "com.example.myapplication"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}