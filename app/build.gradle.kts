plugins {
    id("com.android.application")
}
android {
    namespace = "com.madss.grocery"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.madss.grocery"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //..........for viewpager
    implementation("com.google.android.material:material:1.8.0")
    //implementation("androidx.viewpager2:viewpager2:1.0.0-beta02")
    //..........for animatedbottombar  lib
    implementation("nl.joery.animatedbottombar:library:1.1.0")
    //..........for glide lib
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //..........for picasso lib
    //implementation("com.squareup.picasso:picasso:2.8")
    // retrofit api related
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // ViewPager2 (an updated version of ViewPager)
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.material:material:1.4.0")
    //......... For Slider
    implementation ("io.github.chayanforyou:slider:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("androidx.cardview:cardview:latest_version")
    implementation ("me.zhanghai.android.materialratingbar:library:1.4.0")

    implementation("androidx.paging:paging-runtime:3.1.0")

    //for implementing the swipeRefersh functionality
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")



}