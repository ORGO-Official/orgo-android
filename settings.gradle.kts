pluginManagement {
    includeBuild("build-logic")     // To setup composite Build for version catalog (플러그인에서 의존성 찾아서 빌드해줌)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}
rootProject.name = "ORGO"
include (":app")
include(":feature:login")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":core:model")
include(":feature:map")
include(":core:common")
include(":core:network")
include(":core:designsystem")
include(":core:datastore")
include(":feature:mypage")
include(":feature:mountain_detail")
include(":feature:profile_edit")
include(":feature:settings")
include(":feature:climb_complete")
include(":feature:badge")
